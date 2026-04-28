package org.synthcity.modulo_2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoCicloTest {

    @Test
    void estadoCicloValido_guardaDatosCorrectamente() {
        EstadoCiclo ciclo = new EstadoCiclo(
                1,
                10,
                5,
                5,
                2,
                3,
                1.0,
                0.8,
                4,
                2,
                5,
                5,
                0.75,
                0.80,
                0.50,
                false,
                EstadoSimulacion.SIMULACION_ESTABLE
        );

        assertEquals(1, ciclo.getNumeroCiclo());
        assertEquals(10, ciclo.getEnergiaProducida());
        assertEquals(5, ciclo.getConsumoEnergetico());
        assertEquals(5, ciclo.getEquilibrioEnergetico());
        assertEquals(2, ciclo.getDemandaServicios());
        assertEquals(3, ciclo.getCoberturaServicios());
        assertEquals(1.0, ciclo.getCoberturaServiciosPonderada(), 0.0001);
        assertEquals(0.8, ciclo.getEficienciaTransporte(), 0.0001);
        assertEquals(4, ciclo.getPresionIndustrial());
        assertEquals(2, ciclo.getSoporteTransporte());
        assertEquals(5, ciclo.getContaminacionCiclo());
        assertEquals(5, ciclo.getContaminacionAcumulada());
        assertEquals(0.75, ciclo.getBienestar(), 0.0001);
        assertEquals(0.80, ciclo.getEstabilidad(), 0.0001);
        assertEquals(0.50, ciclo.getDensidad(), 0.0001);
        assertFalse(ciclo.isNecesidadExpansionDetectada());
        assertEquals(EstadoSimulacion.SIMULACION_ESTABLE, ciclo.getEstadoSimulacion());
    }

    @Test
    void hayDeficitEnergetico_devuelveTrueSiBalanceEsNegativo() {
        EstadoCiclo ciclo = new EstadoCiclo(
                1,
                2,
                5,
                -3,
                0,
                0,
                1.0,
                0.0,
                0,
                0,
                0,
                0,
                0.5,
                0.5,
                0.2,
                false,
                EstadoSimulacion.DEFICIT_ENERGETICO
        );

        assertTrue(ciclo.hayDeficitEnergetico());
    }

    @Test
    void hayDeficitServicios_devuelveTrueSiCoberturaMenorQueDemanda() {
        EstadoCiclo ciclo = new EstadoCiclo(
                1,
                10,
                5,
                5,
                6,
                3,
                0.5,
                0.0,
                0,
                0,
                0,
                0,
                0.5,
                0.5,
                0.2,
                false,
                EstadoSimulacion.DEFICIT_SERVICIOS
        );

        assertTrue(ciclo.hayDeficitServicios());
    }

    @Test
    void numeroCicloMenorOIgualACero_lanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new EstadoCiclo(
                        0,
                        10,
                        5,
                        5,
                        0,
                        0,
                        1.0,
                        0.0,
                        0,
                        0,
                        0,
                        0,
                        0.5,
                        0.5,
                        0.2,
                        false,
                        EstadoSimulacion.SIMULACION_ESTABLE
                ));
    }

    @Test
    void metricasNegativas_lanzanExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new EstadoCiclo(
                        1,
                        -1,
                        5,
                        -6,
                        0,
                        0,
                        1.0,
                        0.0,
                        0,
                        0,
                        0,
                        0,
                        0.5,
                        0.5,
                        0.2,
                        false,
                        EstadoSimulacion.SIMULACION_ESTABLE
                ));
    }

    @Test
    void equilibrioEnergeticoIncoherente_lanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new EstadoCiclo(
                        1,
                        10,
                        5,
                        99,
                        0,
                        0,
                        1.0,
                        0.0,
                        0,
                        0,
                        0,
                        0,
                        0.5,
                        0.5,
                        0.2,
                        false,
                        EstadoSimulacion.SIMULACION_ESTABLE
                ));
    }

    @Test
    void contaminacionAcumuladaMenorQueContaminacionCiclo_lanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new EstadoCiclo(
                        1,
                        10,
                        5,
                        5,
                        0,
                        0,
                        1.0,
                        0.0,
                        0,
                        0,
                        10,
                        5,
                        0.5,
                        0.5,
                        0.2,
                        false,
                        EstadoSimulacion.SIMULACION_ESTABLE
                ));
    }

    @Test
    void valoresFueraDeRango01_lanzanExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new EstadoCiclo(
                        1,
                        10,
                        5,
                        5,
                        0,
                        0,
                        1.5,
                        0.0,
                        0,
                        0,
                        0,
                        0,
                        0.5,
                        0.5,
                        0.2,
                        false,
                        EstadoSimulacion.SIMULACION_ESTABLE
                ));
    }

    @Test
    void estadoSimulacionNulo_lanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new EstadoCiclo(
                        1,
                        10,
                        5,
                        5,
                        0,
                        0,
                        1.0,
                        0.0,
                        0,
                        0,
                        0,
                        0,
                        0.5,
                        0.5,
                        0.2,
                        false,
                        null
                ));
    }

    @Test
    void toStringIncluyeDatosPrincipales() {
        EstadoCiclo ciclo = new EstadoCiclo(
                1,
                10,
                5,
                5,
                2,
                3,
                1.0,
                0.8,
                4,
                2,
                5,
                5,
                0.75,
                0.80,
                0.50,
                true,
                EstadoSimulacion.SIMULACION_ESTABLE
        );

        String texto = ciclo.toString();

        assertTrue(texto.contains("numeroCiclo=1"));
        assertTrue(texto.contains("energiaProducida=10"));
        assertTrue(texto.contains("estadoSimulacion=SIMULACION_ESTABLE"));
        assertTrue(texto.contains("necesidadExpansionDetectada=true"));
    }
}