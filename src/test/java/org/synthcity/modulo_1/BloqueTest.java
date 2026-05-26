package org.synthcity.modulo_1;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.bloques.*;
import org.synthcity.modulo_2.ReglasSimulacion;

import static org.junit.jupiter.api.Assertions.*;

class BloqueTest {

    @Test
    void crearBloqueDevuelveSubclaseCorrectaParaCadaTipo() {
        assertInstanceOf(BloqueResidencial.class,
                Bloque.crearBloque(TipoBloque.RESIDENCIAL, new Posicion(0, 0)));
        assertInstanceOf(BloqueEnergia.class,
                Bloque.crearBloque(TipoBloque.ENERGIA, new Posicion(0, 1)));
        assertInstanceOf(BloqueIndustrial.class,
                Bloque.crearBloque(TipoBloque.INDUSTRIAL, new Posicion(1, 0)));
        assertInstanceOf(BloqueServicios.class,
                Bloque.crearBloque(TipoBloque.SERVICIOS, new Posicion(1, 1)));
        assertInstanceOf(BloqueTransporte.class,
                Bloque.crearBloque(TipoBloque.TRANSPORTE, new Posicion(2, 0)));
    }

    @Test
    void crearBloqueMantieneTipoYPosicion() {
        Posicion posicion = new Posicion(4, 7);
        Bloque bloque = Bloque.crearBloque(TipoBloque.SERVICIOS, posicion);

        assertEquals(TipoBloque.SERVICIOS, bloque.getTipo());
        assertEquals(posicion, bloque.getPosicion());
        assertTrue(bloque.estaActivo());
    }

    @Test
    void crearBloqueRechazaTipoYPosicionNulos() {
        assertThrows(IllegalArgumentException.class, () ->
                Bloque.crearBloque(null, new Posicion(0, 0)));

        assertThrows(IllegalArgumentException.class, () ->
                Bloque.crearBloque(TipoBloque.RESIDENCIAL, null));
    }

    @Test
    void crearBloqueConEstadoPermiteReconstruirBloquesActivosEInactivos() {
        Bloque activo = Bloque.crearBloque(TipoBloque.ENERGIA, new Posicion(0, 0), true);
        Bloque inactivo = Bloque.crearBloque(TipoBloque.ENERGIA, new Posicion(0, 1), false);

        assertTrue(activo.estaActivo());
        assertFalse(inactivo.estaActivo());
    }

    @Test
    void bloquesCreadosPorFactoriaMantienenComportamientoFuncionalDeSubclases() {
        assertComportamientoEquivalente(
                new BloqueResidencial(new Posicion(0, 0)),
                Bloque.crearBloque(TipoBloque.RESIDENCIAL, new Posicion(0, 0))
        );
        assertComportamientoEquivalente(
                new BloqueEnergia(new Posicion(0, 1)),
                Bloque.crearBloque(TipoBloque.ENERGIA, new Posicion(0, 1))
        );
        assertComportamientoEquivalente(
                new BloqueIndustrial(new Posicion(1, 0)),
                Bloque.crearBloque(TipoBloque.INDUSTRIAL, new Posicion(1, 0))
        );
        assertComportamientoEquivalente(
                new BloqueServicios(new Posicion(1, 1)),
                Bloque.crearBloque(TipoBloque.SERVICIOS, new Posicion(1, 1))
        );
        assertComportamientoEquivalente(
                new BloqueTransporte(new Posicion(2, 0)),
                Bloque.crearBloque(TipoBloque.TRANSPORTE, new Posicion(2, 0))
        );
    }

    @Test
    void bloqueEnergiaActivo_devuelveProduccionEnergiaMayorQueCero() {
        BloqueEnergia bloque = new BloqueEnergia(new Posicion(0, 0));

        assertTrue(bloque.estaActivo());
        assertTrue(bloque.getProduccionEnergia() > 0);
    }

    @Test
    void bloqueResidencialActivo_devuelveDemandaServiciosMayorQueCero() {
        BloqueResidencial bloque = new BloqueResidencial(new Posicion(0, 1));

        assertTrue(bloque.estaActivo());
        assertTrue(bloque.getDemandaServicios() > 0);
    }

    @Test
    void bloqueIndustrialActivo_devuelveContaminacionYPresionIndustrialMayoresQueCero() {
        BloqueIndustrial bloque = new BloqueIndustrial(new Posicion(1, 0));

        assertTrue(bloque.estaActivo());
        assertTrue(bloque.getContaminacion() > 0);
        assertTrue(bloque.getPresionIndustrial() > 0);
    }

    @Test
    void bloqueServiciosActivo_devuelveCoberturaServiciosMayorQueCero() {
        BloqueServicios bloque = new BloqueServicios(new Posicion(1, 1));

        assertTrue(bloque.estaActivo());
        assertTrue(bloque.getCoberturaServicios() > 0);
    }

    @Test
    void bloqueTransporteActivo_devuelveSoporteTransporteMayorQueCero() {
        BloqueTransporte bloque = new BloqueTransporte(new Posicion(2, 0));

        assertTrue(bloque.estaActivo());
        assertTrue(bloque.getSoporteTransporte() > 0);
    }

    @Test
    void todosLosBloquesDevuelvenConsumoEnergeticoMayorQueCero() {
        BloqueEnergia energia = new BloqueEnergia(new Posicion(0, 0));
        BloqueResidencial residencial = new BloqueResidencial(new Posicion(0, 1));
        BloqueIndustrial industrial = new BloqueIndustrial(new Posicion(1, 0));
        BloqueServicios servicios = new BloqueServicios(new Posicion(1, 1));
        BloqueTransporte transporte = new BloqueTransporte(new Posicion(2, 0));

        assertTrue(energia.getConsumoEnergetico() > 0);
        assertTrue(residencial.getConsumoEnergetico() > 0);
        assertTrue(industrial.getConsumoEnergetico() > 0);
        assertTrue(servicios.getConsumoEnergetico() > 0);
        assertTrue(transporte.getConsumoEnergetico() > 0);
    }

    @Test
    void bloqueEnergia_devuelveValoresCoherentesConReglasSimulacion() {
        BloqueEnergia bloque = new BloqueEnergia(new Posicion(0, 0));

        assertEquals(ReglasSimulacion.ENERGIA_POR_BLOQUE_ENERGIA, bloque.getProduccionEnergia());
        assertEquals(ReglasSimulacion.CONSUMO_ENERGIA, bloque.getConsumoEnergetico());

        assertEquals(0, bloque.getDemandaServicios());
        assertEquals(0, bloque.getCoberturaServicios());
        assertEquals(0, bloque.getPresionIndustrial());
        assertEquals(0, bloque.getSoporteTransporte());
        assertEquals(0, bloque.getContaminacion());
    }

    @Test
    void bloqueResidencial_devuelveValoresCoherentesConReglasSimulacion() {
        BloqueResidencial bloque = new BloqueResidencial(new Posicion(0, 1));

        assertEquals(0, bloque.getProduccionEnergia());
        assertEquals(ReglasSimulacion.CONSUMO_RESIDENCIAL, bloque.getConsumoEnergetico());
        assertEquals(ReglasSimulacion.DEMANDA_POR_RESIDENCIAL, bloque.getDemandaServicios());

        assertEquals(0, bloque.getCoberturaServicios());
        assertEquals(0, bloque.getPresionIndustrial());
        assertEquals(0, bloque.getSoporteTransporte());
        assertEquals(0, bloque.getContaminacion());
    }

    @Test
    void bloqueIndustrial_devuelveValoresCoherentesConReglasSimulacion() {
        BloqueIndustrial bloque = new BloqueIndustrial(new Posicion(1, 0));

        assertEquals(0, bloque.getProduccionEnergia());
        assertEquals(ReglasSimulacion.CONSUMO_INDUSTRIAL, bloque.getConsumoEnergetico());
        assertEquals(0, bloque.getDemandaServicios());
        assertEquals(0, bloque.getCoberturaServicios());
        assertEquals(ReglasSimulacion.PRESION_POR_INDUSTRIAL, bloque.getPresionIndustrial());
        assertEquals(0, bloque.getSoporteTransporte());
        assertEquals(ReglasSimulacion.CONTAMINACION_POR_INDUSTRIAL, bloque.getContaminacion());
    }

    @Test
    void bloqueServicios_devuelveValoresCoherentesConReglasSimulacion() {
        BloqueServicios bloque = new BloqueServicios(new Posicion(1, 1));

        assertEquals(0, bloque.getProduccionEnergia());
        assertEquals(ReglasSimulacion.CONSUMO_SERVICIOS, bloque.getConsumoEnergetico());
        assertEquals(0, bloque.getDemandaServicios());
        assertEquals(ReglasSimulacion.COBERTURA_POR_SERVICIO, bloque.getCoberturaServicios());

        assertEquals(0, bloque.getPresionIndustrial());
        assertEquals(0, bloque.getSoporteTransporte());
        assertEquals(0, bloque.getContaminacion());
    }

    @Test
    void bloqueTransporte_devuelveValoresCoherentesConReglasSimulacion() {
        BloqueTransporte bloque = new BloqueTransporte(new Posicion(2, 0));

        assertEquals(0, bloque.getProduccionEnergia());
        assertEquals(ReglasSimulacion.CONSUMO_TRANSPORTE, bloque.getConsumoEnergetico());
        assertEquals(0, bloque.getDemandaServicios());
        assertEquals(0, bloque.getCoberturaServicios());
        assertEquals(0, bloque.getPresionIndustrial());
        assertEquals(ReglasSimulacion.TRANSPORTE_SOPORTE, bloque.getSoporteTransporte());
        assertEquals(0, bloque.getContaminacion());
    }

    @Test
    void bloqueResidencial_esGeneradorDemanda() {
        BloqueResidencial bloque = new BloqueResidencial(new Posicion(0, 0));

        assertTrue(bloque.esGeneradorDemanda());
    }

    @Test
    void bloquesNoResidenciales_noSonGeneradoresDemanda() {
        assertFalse(new BloqueEnergia(new Posicion(0, 0)).esGeneradorDemanda());
        assertFalse(new BloqueIndustrial(new Posicion(0, 1)).esGeneradorDemanda());
        assertFalse(new BloqueServicios(new Posicion(1, 0)).esGeneradorDemanda());
        assertFalse(new BloqueTransporte(new Posicion(1, 1)).esGeneradorDemanda());
    }

    @Test
    void bloquesSinInfluenciaEspacial_devuelvenRadioCero() {
        assertEquals(0, new BloqueEnergia(new Posicion(0, 0)).getRadioInfluencia());
        assertEquals(0, new BloqueIndustrial(new Posicion(0, 1)).getRadioInfluencia());
        assertEquals(0, new BloqueResidencial(new Posicion(1, 0)).getRadioInfluencia());
    }

    @Test
    void bloqueServicios_devuelveRadioCoberturaServicios() {
        BloqueServicios bloque = new BloqueServicios(new Posicion(0, 0));

        assertEquals(ReglasSimulacion.RADIO_COBERTURA_SERVICIOS, bloque.getRadioInfluencia());
    }

    @Test
    void bloqueTransporte_devuelveRadioInfluenciaTransporte() {
        BloqueTransporte bloque = new BloqueTransporte(new Posicion(0, 0));

        assertEquals(ReglasSimulacion.RADIO_INFLUENCIA_TRANSPORTE, bloque.getRadioInfluencia());
    }

    private void assertComportamientoEquivalente(Bloque esperado, Bloque creado) {
        assertEquals(esperado.getTipo(), creado.getTipo());
        assertEquals(esperado.getProduccionEnergia(), creado.getProduccionEnergia());
        assertEquals(esperado.getConsumoEnergetico(), creado.getConsumoEnergetico());
        assertEquals(esperado.getDemandaServicios(), creado.getDemandaServicios());
        assertEquals(esperado.getCoberturaServicios(), creado.getCoberturaServicios());
        assertEquals(esperado.getPresionIndustrial(), creado.getPresionIndustrial());
        assertEquals(esperado.getSoporteTransporte(), creado.getSoporteTransporte());
        assertEquals(esperado.getContaminacion(), creado.getContaminacion());
        assertEquals(esperado.getRadioInfluencia(), creado.getRadioInfluencia());
        assertEquals(esperado.esGeneradorDemanda(), creado.esGeneradorDemanda());
    }
}
