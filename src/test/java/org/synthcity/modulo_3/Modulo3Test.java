package org.synthcity.modulo_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.EstadoSimulacion; // Asegúrate de importar el enum

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Modulo3Test {

    private EvaluadorCiudad evaluador;
    private Map<TipoBloque, Integer> tiposMock;

    @BeforeEach
    void setUp() {
        evaluador = new EvaluadorCiudad();
        tiposMock = new HashMap<>();
        // Inicializamos el mapa con todos los tipos para cumplir el contrato [cite: 65]
        for (TipoBloque t : TipoBloque.values()) {
            tiposMock.put(t, 0);
        }
        tiposMock.put(TipoBloque.RESIDENCIAL, 10);
    }

    // =========================================================
    // 1. TESTS DE SEGURIDAD
    // =========================================================

    @Test
    void testEntradaNulaLanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(null));
    }

    @Test
    void testMatematicasIncoherentesLanzaExcepcion() {
        // CORRECCIÓN: Ahora pasamos los 9 parámetros (nombre, filas, cols, capMax, total, activos, inactivos, mapa, estado)
        ResultadoSimulacion mock = new ResultadoSimulacion("Test", 5, 5, 25, 10, 5, 4, tiposMock, EstadoSimulacion.EJECUTADA);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(mock));
    }

    @Test
    void testValidacionesExtra() {
        // Bloques negativos
        ResultadoSimulacion neg = new ResultadoSimulacion("Neg", 5, 5, 25, -1, 0, 0, tiposMock, EstadoSimulacion.EJECUTADA);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(neg));

        // Activos superan al total
        ResultadoSimulacion sup = new ResultadoSimulacion("Sup", 5, 5, 25, 10, 15, 0, tiposMock, EstadoSimulacion.EJECUTADA);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(sup));

        // Mapa de tipos nulo [cite: 64, 2219]
        ResultadoSimulacion sinMapa = new ResultadoSimulacion("NoMap", 5, 5, 25, 10, 5, 5, null, EstadoSimulacion.EJECUTADA);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(sinMapa));
    }

    // =========================================================
    // 2. TESTS DEL MOTOR DE REGLAS (Orden No Alterable [cite: 2477])
    // =========================================================

    @Test
    void testCiudadSinDatos() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Alpha", 5, 5, 25, 0, 0, 0, tiposMock, EstadoSimulacion.CIUDAD_VACIA);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.SIN_DATOS, res.getNivelEvaluacion());
    }

    @Test
    void testCiudadCritica() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Beta", 5, 5, 25, 10, 0, 10, tiposMock, EstadoSimulacion.SIN_BLOQUES_ACTIVOS);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.CRITICO, res.getNivelEvaluacion());
    }

    @Test
    void testCiudadOptima() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Gamma", 5, 5, 25, 10, 10, 0, tiposMock, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.OPTIMO, res.getNivelEvaluacion());
    }

    @Test
    void testCiudadFuncional() {
        // 6 activos de 10 = 60% (Umbral mínimo aceptable [cite: 2490])
        ResultadoSimulacion mock = new ResultadoSimulacion("Delta", 5, 5, 25, 10, 6, 4, tiposMock, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.FUNCIONAL, res.getNivelEvaluacion());
    }

    @Test
    void testCiudadInestable() {
        // 5 activos de 10 = 50% (Por debajo del 60% [cite: 2492-2494])
        ResultadoSimulacion mock = new ResultadoSimulacion("Epsilon", 5, 5, 25, 10, 5, 5, tiposMock, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.INESTABLE, res.getNivelEvaluacion());
    }

    // =========================================================
    // 3. PRUEBAS DE MÉTRICAS (Persona 2)
    // =========================================================

    @Test
    void testMetricaCiudadVacia() {
        ResultadoSimulacion sim = new ResultadoSimulacion("Vacia", 5, 5, 25, 0, 0, 0, tiposMock, EstadoSimulacion.CIUDAD_VACIA);
        MetricaCiudad metrica = new MetricaCiudad(sim);

        // Control de división por cero [cite: 1796, 2272]
        assertEquals(0.0, metrica.getPorcentajeActivos(), "El porcentaje debe ser 0.0");
        assertEquals(1.0, metrica.getPorcentajeInactivos(), "Inactivos debe ser 1.0");
    }

    @Test
    void testCalculoMetricasRatios() {
        ResultadoSimulacion sim = new ResultadoSimulacion("RatioTest", 5, 5, 25, 10, 7, 3, tiposMock, EstadoSimulacion.EJECUTADA);
        MetricaCiudad metrica = new MetricaCiudad(sim);

        // Verificamos que se usen los getters correctos de ResultadoSimulacion [cite: 2249]
        assertEquals(10, metrica.getTotalBloques());
        assertEquals(7, metrica.getBloquesActivos());
        assertEquals(3, metrica.getBloquesInactivos());

        assertEquals(0.7, metrica.getPorcentajeActivos(), 0.001);
        assertEquals(0.3, metrica.getPorcentajeInactivos(), 0.001);
        assertEquals(1.0, metrica.getPorcentajeActivos() + metrica.getPorcentajeInactivos(), 0.001);
    }

    @Test
    void testConstructorMasNulos() {
        ResultadoSimulacion sim = new ResultadoSimulacion("Test", 5, 5, 25, 10, 5, 5, tiposMock, EstadoSimulacion.EJECUTADA);
        MetricaCiudad m = new MetricaCiudad(sim);

        // Verificamos robustez del constructor de ResultadoEvaluacion [cite: 1898-1900, 2070]
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> new ResultadoEvaluacion("City", m, null, "Mensaje"));
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> new ResultadoEvaluacion("City", m, NivelEvaluacion.FUNCIONAL, null));
    }

    // =========================================================
    // 4. PRUEBAS DE RESULTADO Y MENSAJES (Persona 4)
    // =========================================================

    @Test
    void testMensajeCoherenteCritico() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Beta", 5, 5, 25, 10, 0, 10, tiposMock, EstadoSimulacion.SIN_BLOQUES_ACTIVOS);
        ResultadoEvaluacion res = evaluador.evaluar(mock);

        // El mensaje debe ser coherente con el nivel CRÍTICO [cite: 78, 2734]
        assertTrue(res.getMensaje().toLowerCase().contains("no tiene actividad"));
    }

    @Test
    void testResultadoAutosuficiente() {
        ResultadoSimulacion sim = new ResultadoSimulacion("Zeta", 5, 5, 25, 10, 8, 2, tiposMock, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(sim);

        // Verificamos que el objeto entregado al M4 sea completo [cite: 71-72, 1897]
        assertNotNull(res.getMetricaCiudad());
        assertNotNull(res.getNivelEvaluacion());
        assertNotNull(res.getMensaje());
        assertEquals("Zeta", res.getNombreCiudad());
    }
}