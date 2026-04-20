/*package org.synthcity.modulo_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.EstadoSimulacion;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Modulo3Test {

    private EvaluadorCiudad evaluador;
    private Map<TipoBloque, Integer> tiposMockResidencial;

    @BeforeEach
    void setUp() {
        evaluador = new EvaluadorCiudad();
        tiposMockResidencial = conteoBase();
        tiposMockResidencial.put(TipoBloque.RESIDENCIAL, 10);
    }

    private Map<TipoBloque, Integer> conteoBase() {
        Map<TipoBloque, Integer> m = new EnumMap<>(TipoBloque.class);
        for (TipoBloque t : TipoBloque.values()) {
            m.put(t, 0);
        }
        return m;
    }

    private Map<TipoBloque, Integer> conteoBalanceado() {
        Map<TipoBloque, Integer> m = conteoBase();
        m.put(TipoBloque.RESIDENCIAL, 3);
        m.put(TipoBloque.ENERGIA, 2);
        m.put(TipoBloque.SERVICIOS, 3);
        m.put(TipoBloque.TRANSPORTE, 2);
        return m;
    }

    private Map<TipoBloque, Integer> conteoMixtoConIndustria() {
        Map<TipoBloque, Integer> m = conteoBase();
        m.put(TipoBloque.RESIDENCIAL, 5);
        m.put(TipoBloque.ENERGIA, 1);
        m.put(TipoBloque.INDUSTRIAL, 1);
        m.put(TipoBloque.SERVICIOS, 2);
        m.put(TipoBloque.TRANSPORTE, 1);
        return m;
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
        ResultadoSimulacion mock = new ResultadoSimulacion("Test", 5, 5, 25, 10, 5, 4, tiposMockResidencial, EstadoSimulacion.EJECUTADA);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(mock));
    }

    @Test
    void testValidacionesExtra() {
        ResultadoSimulacion neg = new ResultadoSimulacion("Neg", 5, 5, 25, -1, 0, 0, tiposMockResidencial, EstadoSimulacion.EJECUTADA);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(neg));

        ResultadoSimulacion sup = new ResultadoSimulacion("Sup", 5, 5, 25, 10, 15, 0, tiposMockResidencial, EstadoSimulacion.EJECUTADA);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(sup));

        ResultadoSimulacion sinMapa = new ResultadoSimulacion("NoMap", 5, 5, 25, 10, 5, 5, null, EstadoSimulacion.EJECUTADA);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(sinMapa));
    }

    // =========================================================
    // 2. MOTOR DE REGLAS — SPRINT 2 (score + cortes duros)
    // =========================================================

    @Test
    void testCiudadSinDatos() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Alpha", 5, 5, 25, 0, 0, 0, tiposMockResidencial, EstadoSimulacion.CIUDAD_VACIA);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.SIN_DATOS, res.getNivelEvaluacion());
    }

    @Test
    void testCiudadCriticaSinActivos() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Beta", 5, 5, 25, 10, 0, 10, tiposMockResidencial, EstadoSimulacion.SIN_BLOQUES_ACTIVOS);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.CRITICO, res.getNivelEvaluacion());
    }

    @Test
    void testCiudadOptimaBalanceadaPlenaActividad() {
        Map<TipoBloque, Integer> conteo = conteoBalanceado();
        ResultadoSimulacion mock = new ResultadoSimulacion("Gamma", 5, 5, 20, 10, 10, 0, conteo, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.OPTIMO, res.getNivelEvaluacion());
        assertTrue(res.getScoreViabilidad() >= 85.0);
    }

    @Test
    void testCiudadFuncionalActividadModerada() {
        Map<TipoBloque, Integer> conteo = conteoMixtoConIndustria();
        ResultadoSimulacion mock = new ResultadoSimulacion("Delta", 5, 5, 20, 10, 7, 3, conteo, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertEquals(NivelEvaluacion.FUNCIONAL, res.getNivelEvaluacion());
        assertTrue(res.getScoreViabilidad() >= 65.0 && res.getScoreViabilidad() < 85.0);
    }

    @Test
    void testCiudadInestableResidencialMayoritaria() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Epsilon", 5, 5, 25, 10, 8, 2, tiposMockResidencial, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertTrue(res.getNivelEvaluacion() == NivelEvaluacion.INESTABLE
                || res.getNivelEvaluacion() == NivelEvaluacion.CRITICO);
    }

    // =========================================================
    // 3. MÉTRICAS
    // =========================================================

    @Test
    void testMetricaCiudadVacia() {
        ResultadoSimulacion sim = new ResultadoSimulacion("Vacia", 5, 5, 25, 0, 0, 0, tiposMockResidencial, EstadoSimulacion.CIUDAD_VACIA);
        MetricaCiudad metrica = new MetricaCiudad(sim);
        assertEquals(0.0, metrica.getPorcentajeActivos());
        assertEquals(1.0, metrica.getPorcentajeInactivos());
    }

    @Test
    void testCalculoMetricasRatios() {
        ResultadoSimulacion sim = new ResultadoSimulacion("RatioTest", 5, 5, 25, 10, 7, 3, tiposMockResidencial, EstadoSimulacion.EJECUTADA);
        MetricaCiudad metrica = new MetricaCiudad(sim);

        assertEquals(10, metrica.getTotalBloques());
        assertEquals(7, metrica.getBloquesActivos());
        assertEquals(3, metrica.getBloquesInactivos());
        assertEquals(0.7, metrica.getPorcentajeActivos(), 0.001);
        assertEquals(0.3, metrica.getPorcentajeInactivos(), 0.001);
        assertEquals(1.0, metrica.getPorcentajeActivos() + metrica.getPorcentajeInactivos(), 0.001);
    }

    @Test
    void testConstructorMasNulos() {
        ResultadoSimulacion sim = new ResultadoSimulacion("Test", 5, 5, 25, 10, 5, 5, tiposMockResidencial, EstadoSimulacion.EJECUTADA);
        MetricaCiudad m = new MetricaCiudad(sim);

        assertThrows(ResultadoSimulacionInvalidoException.class, () -> new ResultadoEvaluacion("City", m, null, "Mensaje"));
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> new ResultadoEvaluacion("City", m, NivelEvaluacion.FUNCIONAL, null));
    }

    // =========================================================
    // 4. MENSAJES Y RESULTADO
    // =========================================================

    @Test
    void testMensajeCoherenteCritico() {
        ResultadoSimulacion mock = new ResultadoSimulacion("Beta", 5, 5, 25, 10, 0, 10, tiposMockResidencial, EstadoSimulacion.SIN_BLOQUES_ACTIVOS);
        ResultadoEvaluacion res = evaluador.evaluar(mock);
        assertTrue(res.getMensaje().toLowerCase().contains("no tiene actividad"));
    }

    @Test
    void testResultadoAutosuficiente() {
        Map<TipoBloque, Integer> conteo = conteoBalanceado();
        ResultadoSimulacion sim = new ResultadoSimulacion("Zeta", 5, 5, 20, 10, 8, 2, conteo, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(sim);

        assertNotNull(res.getMetricaCiudad());
        assertNotNull(res.getNivelEvaluacion());
        assertNotNull(res.getMensaje());
        assertNotNull(res.getAlertas());
        assertEquals("Zeta", res.getNombreCiudad());
        assertTrue(res.getScoreViabilidad() >= 0.0 && res.getScoreViabilidad() <= 100.0);
    }
}*/