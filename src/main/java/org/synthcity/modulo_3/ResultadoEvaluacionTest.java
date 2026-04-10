package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResultadoEvaluacionTest {

    @Test
    void testCreacion() {

        NivelEvaluacion nivel = NivelEvaluacion.FUNCIONAL;
        String mensaje = GeneradorMensajes.generarMensaje(nivel);

        MetricaCiudad metrica = null; // puedes dejarlo null si no lo necesitas ahora

        assertThrows(IllegalArgumentException.class, () -> {
            new ResultadoEvaluacion("Madrid", metrica, nivel, mensaje);
        });
    }
}