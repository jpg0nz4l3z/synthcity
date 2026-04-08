package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_2.ResultadoSimulacion;
import static org.junit.jupiter.api.Assertions.*;

class EvaluadorCiudadTest {

    @Test
    void testSimulacionCorrectaNoLanzaExcepcion() {
        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        // 10 bloques totales = 7 activos + 3 inactivos (Matemática correcta)
        ResultadoSimulacion simulacionValida = new ResultadoSimulacion(10, 7, 3);

        assertDoesNotThrow(() -> {
            evaluador.evaluar(simulacionValida);
        });
    }

    @Test
    void testEvaluarLanzaExcepcionCuandoSimulacionEsNula() {
        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        Exception excepcion = assertThrows(ResultadoSimulacionInvalidoException.class, () -> {
            evaluador.evaluar(null);
        });
        assertEquals("El ResultadoSimulacion recibido es nulo.", excepcion.getMessage());
    }

    @Test
    void testEvaluarLanzaExcepcionConConteosNegativos() {
        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        // Bloques inactivos negativos (-2)
        ResultadoSimulacion simulacionInvalida = new ResultadoSimulacion(10, 12, -2);

        assertThrows(ResultadoSimulacionInvalidoException.class, () -> {
            evaluador.evaluar(simulacionInvalida);
        });
    }

    @Test
    void testEvaluarLanzaExcepcionPorIncoherenciaEstructural() {
        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        // 5 activos + 8 inactivos = 13 (No coincide con el total de 10)
        ResultadoSimulacion simulacionIncoherente = new ResultadoSimulacion(10, 5, 8);

        assertThrows(ResultadoSimulacionInvalidoException.class, () -> {
            evaluador.evaluar(simulacionIncoherente);
        });
    }
}