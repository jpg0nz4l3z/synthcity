package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EvaluadorCiudadTest {

    @Test
    void testEvaluarLanzaExcepcionCuandoSimulacionEsNula() {
        // 1. Preparar el entorno (Arrange)
        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        Object simulacionNula = null;

        // 2. Ejecutar y Comprobar (Act & Assert)
        // Le decimos a JUnit: "Espero que al ejecutar esto, salte esta Excepción"
        Exception excepcionLanzada = assertThrows(ResultadoSimulacionInvalidoException.class, () -> {
            evaluador.evaluar(simulacionNula);
        });

        // 3. Comprobar que el mensaje de error es exactamente el que escribiste
        assertEquals("El ResultadoSimulacion recibido es nulo.", excepcionLanzada.getMessage());
    }
}