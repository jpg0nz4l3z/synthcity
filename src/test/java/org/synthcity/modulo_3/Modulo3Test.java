package org.synthcity.modulo_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        tiposMock.put(TipoBloque.RESIDENCIAL, 10); // Mapa básico para pasar la validación
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
        // 10 totales, pero 5 activos + 4 inactivos = 9 (Debería fallar)
        ResultadoSimulacion mock = new ResultadoSimulacion("Test", 10, 5, 4, tiposMock);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(mock));
    }
    @Test
    void testValidacionesExtra() {

        ResultadoSimulacion neg = new ResultadoSimulacion("Neg", -1, 0, 0, tiposMock);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(neg));


        ResultadoSimulacion sup = new ResultadoSimulacion("Sup", 10, 15, 0, tiposMock);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(sup));


        ResultadoSimulacion sinMapa = new ResultadoSimulacion("NoMap", 10, 5, 5, null);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(sinMapa));
    }

    // =========================================================
    // 2. TESTS DEL MOTOR DE REGLAS (Nueva Integración)
    // =========================================================

    @Test
    void testCiudadSinDatos() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Alpha", 0, 0, 0, tiposMock);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.SIN_DATOS, res.getNivelEvaluacion());
    }

    @Test
    void testCiudadCritica() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Beta", 10, 0, 10, tiposMock);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.CRITICO, res.getNivelEvaluacion());
    }

    @Test
    void testCiudadOptima() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Gamma", 10, 10, 0, tiposMock);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.OPTIMO, res.getNivelEvaluacion());
    }

    @Test
    void testCiudadFuncional() {
        // 6 activos de 10 = 60% (Justo en el límite del umbral)
        ResultadoSimulacion mock = new ResultadoSimulacion("Delta", 10, 6, 4, tiposMock);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.FUNCIONAL, res.getNivelEvaluacion());
    }

    @Test
    void testCiudadInestable() {
        // 5 activos de 10 = 50% (Por debajo del 60%, pero mayor que 0)
        ResultadoSimulacion mock = new ResultadoSimulacion("Epsilon", 10, 5, 5, tiposMock);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.INESTABLE, res.getNivelEvaluacion());
    }

    // =========================================================
    // 3. PRUEBAS DE MÉTRICAS
    // =========================================================

    @Test
    void testMetricaCiudadVacia() {
        ResultadoSimulacion sim = new ResultadoSimulacion("Vacia", 0, 0, 0, tiposMock);
        MetricaCiudad metrica = new MetricaCiudad(sim);

        // Si total es 0, el porcentaje debe ser 0.0, no NaN
        assertEquals(0.0, metrica.getPorcentajeActivos(), "El porcentaje debe ser 0.0");
        assertEquals(1.0, metrica.getPorcentajeInactivos(), "Inactivos debe ser 1.0 (complemento)");
    }

    @Test
    void testCalculoMetricasRatios() {
        ResultadoSimulacion sim = new ResultadoSimulacion("RatioTest", 10, 7, 3, tiposMock);
        MetricaCiudad metrica = new MetricaCiudad(sim);

        assertEquals(10, metrica.getTotalBloques());
        assertEquals(7, metrica.getBloquesActivos());
        assertEquals(3, metrica.getBloquesInactivos());
        assertNotNull(metrica.getConteoPorTipo());

        assertEquals(0.7, metrica.getPorcentajeActivos(), 0.001);
        assertEquals(0.3, metrica.getPorcentajeInactivos(), 0.001);
        // Relación obligatoria: activos + inactivos = 1.0
        assertEquals(1.0, metrica.getPorcentajeActivos() + metrica.getPorcentajeInactivos(), 0.001);
    }
    @Test
    void testConstructorMasNulos() {
        MetricaCiudad m = new MetricaCiudad(new ResultadoSimulacion("Test", 10, 5, 5, tiposMock));

        // Probar nivel nulo
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new ResultadoEvaluacion("City", m, null, "Mensaje"));

        // Probar mensaje nulo
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new ResultadoEvaluacion("City", m, NivelEvaluacion.FUNCIONAL, null));
    }

    // =========================================================
    // 4. PRUEBAS DE RESULTADO Y MENSAJES
    // =========================================================


    @Test
    void testMensajeCoherenteCritico() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Beta", 10, 0, 10, tiposMock);
        ResultadoEvaluacion res = evaluador.evaluar(mock);

        // El mensaje debe explicar por qué es crítico [cite: 2734]
        assertTrue(res.getMensaje().toLowerCase().contains("no tiene actividad"),
                "El mensaje debería mencionar la falta de actividad");
    }

    @Test
    void testResultadoAutosuficiente() {
        ResultadoSimulacion sim = new ResultadoSimulacion("Zeta", 10, 8, 2, tiposMock);
        ResultadoEvaluacion res = evaluador.evaluar(sim);

        assertNotNull(res.getMetricaCiudad());
        assertNotNull(res.getNivelEvaluacion());
        assertNotNull(res.getMensaje());
        assertFalse(res.getMensaje().isEmpty());
        assertEquals("Zeta", res.getNombreCiudad());
    }
}