package org.synthcity.modulo_3;
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_2.ResultadoSimulacion;

import static org.junit.jupiter.api.Assertions.*;

public class MetricaCiudadTest {
    @Test
    void simulacionNula() {
        assertThrows(NullPointerException.class, () -> {
            new MetricaCiudad(null);
        });
    }

    @Test
    void pruebaBasica() {
        ResultadoSimulacion r = new ResultadoSimulacion();

        MetricaCiudad m = new MetricaCiudad(r);

        assertNotNull(m);
    }
}
