package org.synthcity.modulo_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_2.ResultadoSimulacion;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class EvaluadorCiudadTest {

    private EvaluadorCiudad evaluador;
    private Map<String, Integer> tiposMock;

    @BeforeEach
    void setUp() {
        evaluador = new EvaluadorCiudad();
        tiposMock = new HashMap<>();
        tiposMock.put("RESIDENCIAL", 10); // Mapa básico para pasar la validación
    }

    // =========================================================
    // 1. TESTS DE SEGURIDAD (Tus validaciones originales)
    // =========================================================

    @Test
    void testEntradaNulaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> evaluador.evaluar(null));
    }

    @Test
    void testMatematicasIncoherentesLanzaExcepcion() {
        // 10 totales, pero 5 activos + 4 inactivos = 9 (Debería fallar)
        ResultadoSimulacion mock = new ResultadoSimulacion("Test", 10, 5, 4, tiposMock);
        assertThrows(ResultadoSimulacionInvalidoException.class, () -> evaluador.evaluar(mock));
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
}