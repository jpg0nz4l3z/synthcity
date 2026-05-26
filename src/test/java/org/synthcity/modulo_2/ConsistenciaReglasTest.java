package org.synthcity.modulo_2;


import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueServicios;
import org.synthcity.modulo_1.bloques.BloqueTransporte;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsistenciaReglasTest {

    @Test
    void testRadioServiciosConsistente() {
        double esperado = new BloqueServicios(new Posicion(2,2)).getRadioInfluencia();
        double actual = ReglasSimulacion.RADIO_COBERTURA_SERVICIOS;
        assertEquals(esperado, actual, 0.001,
                "RADIO_COBERTURA_SERVICIOS debe coincidir con BloqueServicios.getRadioInfluencia()");
    }

    @Test
    void testRadioTransporteConsistente() {
        double esperado = new BloqueTransporte(new Posicion(3,2)).getRadioInfluencia();
        double actual = ReglasSimulacion.RADIO_INFLUENCIA_TRANSPORTE;
        assertEquals(esperado, actual, 0.001,
                "RADIO_INFLUENCIA_TRANSPORTE debe coincidir con BloqueTransporte.getRadioInfluencia()");
    }
}