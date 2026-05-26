package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistroDatoTest {

    @Test
    void registroValido_guardaFeaturesYObjetivo() {
        RegistroDato registro = new RegistroDato(
                0.5,
                1.2,
                0.9,
                10,
                30,
                0.8,
                -0.1,
                5,
                0.7,
                75,
                false,
                10,
                false,
                3
        );

        assertEquals(3, registro.getObjetivo());
        assertEquals(13, registro.toArray().length);
        assertEquals(0.5, registro.getDensidad());
        assertEquals(75, registro.getScoreViabilidad());
        assertFalse(registro.isColapsoDetectado());
        assertFalse(registro.isSaturacionDetectada());
    }

    @Test
    void densidadFueraDeRango_lanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new RegistroDato(
                        1.5, 1.0, 1.0,
                        0, 0, 0.5,
                        0, 0, 0.5,
                        50, false, 1, false, 2
                )
        );
    }

    @Test
    void ratiosNegativos_lanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new RegistroDato(
                        0.5, -1.0, 1.0,
                        0, 0, 0.5,
                        0, 0, 0.5,
                        50, false, 1, false, 2
                )
        );
    }

    @Test
    void contaminacionNegativa_lanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new RegistroDato(
                        0.5, 1.0, 1.0,
                        -1, 0, 0.5,
                        0, 0, 0.5,
                        50, false, 1, false, 2
                )
        );
    }

    @Test
    void objetivoFueraDeRango_lanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new RegistroDato(
                        0.5, 1.0, 1.0,
                        0, 0, 0.5,
                        0, 0, 0.5,
                        50, false, 1, false, 9
                )
        );
    }
}