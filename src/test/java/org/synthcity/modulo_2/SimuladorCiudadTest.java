package org.synthcity.modulo_2;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueIndustrial;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_1.bloques.BloqueServicios;
import org.synthcity.modulo_1.bloques.BloqueTransporte;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimuladorCiudadTest {

    private final SimuladorCiudad simulador = new SimuladorCiudad();

    @Test
    void ciudadNula_lanzaCiudadInvalidaParaSimulacionException() {
        assertThrows(CiudadInvalidaParaSimulacionException.class, () ->
                simulador.simular(null));
    }

    @Test
    void ciudadVacia_devuelveResultadoCoherenteConCiudadVacia() {
        Ciudad ciudad = new Ciudad("Vacía", 10, 10);

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(EstadoSimulacion.CIUDAD_VACIA, resultado.getEstadoSimulacion());
        assertEquals(0, resultado.getBloquesTotales());
        assertEquals(0, resultado.getBloquesActivos());
        assertEquals(0, resultado.getBloquesInactivos());
        assertTrue(resultado.ciudadEstaVacia());
        assertFalse(resultado.hayBloquesActivos());
    }

    @Test
    void ciudadConUnicoBloqueEnergiaActivo_calculaProduccionCorrecta() {
        Ciudad ciudad = new Ciudad("Energia", 5, 5);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(ReglasSimulacion.ENERGIA_POR_BLOQUE_ENERGIA, resultado.getEnergiaProducida());
    }

    @Test
    void ciudadConUnicoBloqueResidencialActivo_calculaDemandaCorrecta() {
        Ciudad ciudad = new Ciudad("Residencial", 5, 5);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(ReglasSimulacion.DEMANDA_POR_RESIDENCIAL, resultado.getDemandaServicios());
    }

    @Test
    void ciudadConUnicoBloqueIndustrialActivo_calculaContaminacionCorrecta() {
        Ciudad ciudad = new Ciudad("Industrial", 5, 5);
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 0)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(ReglasSimulacion.CONTAMINACION_POR_INDUSTRIAL, resultado.getContaminacion());
    }

    @Test
    void ciudadConUnicoBloqueServiciosActivo_calculaCoberturaCorrecta() {
        Ciudad ciudad = new Ciudad("Servicios", 5, 5);
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 0)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(ReglasSimulacion.COBERTURA_POR_SERVICIO, resultado.getCoberturaServicios());
    }

    @Test
    void ciudadConUnicoBloqueTransporteActivo_calculaSoporteCorrecto() {
        Ciudad ciudad = new Ciudad("Transporte", 5, 5);
        ciudad.addBloque(new BloqueTransporte(new Posicion(0, 0)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(ReglasSimulacion.TRANSPORTE_SOPORTE, resultado.getSoporteTransporte());
    }

    @Test
    void ciudadConMezclaDeTipos_calculaSumasCorrectamente() {
        Ciudad ciudad = new Ciudad("Mixta", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 3)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(0, 4)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertEquals(ReglasSimulacion.ENERGIA_POR_BLOQUE_ENERGIA, r.getEnergiaProducida());
        assertEquals(
                ReglasSimulacion.CONSUMO_ENERGIA +
                        ReglasSimulacion.CONSUMO_RESIDENCIAL +
                        ReglasSimulacion.CONSUMO_INDUSTRIAL +
                        ReglasSimulacion.CONSUMO_SERVICIOS +
                        ReglasSimulacion.CONSUMO_TRANSPORTE,
                r.getConsumoEnergetico()
        );
        assertEquals(ReglasSimulacion.DEMANDA_POR_RESIDENCIAL, r.getDemandaServicios());
        assertEquals(ReglasSimulacion.COBERTURA_POR_SERVICIO, r.getCoberturaServicios());
        assertEquals(ReglasSimulacion.PRESION_POR_INDUSTRIAL, r.getPresionIndustrial());
        assertEquals(ReglasSimulacion.TRANSPORTE_SOPORTE, r.getSoporteTransporte());
        assertEquals(ReglasSimulacion.CONTAMINACION_POR_INDUSTRIAL, r.getContaminacion());
    }

    @Test
    void ciudadConMezclaDeActivosEInactivos_cuentaBienActivosEInactivos() {
        Ciudad ciudad = new Ciudad("Activos", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 2)));

        ciudad.desactivarBloque(new Posicion(0, 1));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertEquals(3, r.getBloquesTotales());
        assertEquals(2, r.getBloquesActivos());
        assertEquals(1, r.getBloquesInactivos());
        assertTrue(r.hayBloquesActivos());
        assertEquals(r.getBloquesTotales(), r.getBloquesActivos() + r.getBloquesInactivos());
    }

    @Test
    void conteoPorTipo_correctoYTiposAusentesAparecenACero() {
        Ciudad ciudad = new Ciudad("Conteos", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 2)));

        ResultadoSimulacion r = simulador.simular(ciudad);
        Map<TipoBloque, Integer> mapa = r.getConteoPorTipo();

        assertNotNull(mapa);
        for (TipoBloque tipo : TipoBloque.values()) {
            assertTrue(mapa.containsKey(tipo));
            assertNotNull(mapa.get(tipo));
            assertTrue(mapa.get(tipo) >= 0);
        }

        assertEquals(2, r.getCantidadPorTipo(TipoBloque.RESIDENCIAL));
        assertEquals(1, r.getCantidadPorTipo(TipoBloque.ENERGIA));
        assertEquals(0, r.getCantidadPorTipo(TipoBloque.INDUSTRIAL));
        assertEquals(0, r.getCantidadPorTipo(TipoBloque.SERVICIOS));
        assertEquals(0, r.getCantidadPorTipo(TipoBloque.TRANSPORTE));
    }

    @Test
    void ciudadConEnergiaSuficiente_tieneEquilibrioEnergeticoNoNegativo() {
        Ciudad ciudad = new Ciudad("EnergiaOK", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertTrue(r.getEquilibrioEnergetico() >= 0);
        assertFalse(r.hayDeficitEnergetico());
    }

    @Test
    void ciudadConEnergiaInsuficiente_tieneDeficitEnergetico() {
        Ciudad ciudad = new Ciudad("DeficitEnergia", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 1)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertTrue(r.getEnergiaProducida() == 0);
        assertTrue(r.getEquilibrioEnergetico() < 0);
        assertTrue(r.hayDeficitEnergetico());
        assertEquals(EstadoSimulacion.DEFICIT_ENERGETICO, r.getEstadoSimulacion());
    }

    @Test
    void ciudadSinBloquesDeEnergia_tieneEnergiaProducidaCero() {
        Ciudad ciudad = new Ciudad("SinEnergia", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertEquals(0, r.getEnergiaProducida());
    }

    @Test
    void bloqueEnergiaInactivo_noContribuyeALaProduccion() {
        Ciudad ciudad = new Ciudad("EnergiaInactiva", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.desactivarBloque(new Posicion(0, 0));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertEquals(0, r.getEnergiaProducida());
        assertEquals(EstadoSimulacion.SIN_BLOQUES_ACTIVOS, r.getEstadoSimulacion());
    }

    @Test
    void ciudadConMasCoberturaQueDemanda_noTieneDeficitServicios() {
        Ciudad ciudad = new Ciudad("ServiciosOK", 10, 10);
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertTrue(r.getCoberturaServicios() >= r.getDemandaServicios());
        assertFalse(r.hayDeficitServicios());
    }

    @Test
    void ciudadConMasDemandaQueCobertura_tieneDeficitServicios() {
        Ciudad ciudad = new Ciudad("DeficitServicios", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 3)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertTrue(r.getDemandaServicios() > r.getCoberturaServicios());
        assertTrue(r.hayDeficitServicios());
        assertEquals(EstadoSimulacion.DEFICIT_SERVICIOS, r.getEstadoSimulacion());
    }

    @Test
    void ciudadSinResidenciales_tieneDemandaServiciosCero() {
        Ciudad ciudad = new Ciudad("SinResidenciales", 10, 10);
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 0)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertEquals(0, r.getDemandaServicios());
    }

    @Test
    void demandaServiciosCorrectaConVariosResidenciales() {
        Ciudad ciudad = new Ciudad("Demanda", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 2)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertEquals(3 * ReglasSimulacion.DEMANDA_POR_RESIDENCIAL, r.getDemandaServicios());
    }

    @Test
    void ciudadSinIndustriales_tieneContaminacionBaseCero() {
        Ciudad ciudad = new Ciudad("SinIndustria", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertEquals(0, r.getContaminacion());
    }

    @Test
    void industrialesInactivos_noContaminan() {
        Ciudad ciudad = new Ciudad("IndustriaInactiva", 10, 10);
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 0)));
        ciudad.desactivarBloque(new Posicion(0, 0));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertEquals(0, r.getContaminacion());
    }

    @Test
    void presionIndustrialAumentaConMasIndustrialesActivos() {
        Ciudad ciudad1 = new Ciudad("Industria1", 10, 10);
        ciudad1.addBloque(new BloqueIndustrial(new Posicion(0, 0)));

        Ciudad ciudad2 = new Ciudad("Industria2", 10, 10);
        ciudad2.addBloque(new BloqueIndustrial(new Posicion(0, 0)));
        ciudad2.addBloque(new BloqueIndustrial(new Posicion(0, 1)));

        ResultadoSimulacion r1 = simulador.simular(ciudad1);
        ResultadoSimulacion r2 = simulador.simular(ciudad2);

        assertTrue(r2.getPresionIndustrial() > r1.getPresionIndustrial());
    }

    @Test
    void densidadAlta_aniadePenalizacionCorrectaDeContaminacion() {
        Ciudad ciudad = new Ciudad("Densa", 5, 5); // capacidad 25

        // 10 bloques de energía
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 3)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 4)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 2)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 3)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 4)));

        // 10 bloques industriales
        ciudad.addBloque(new BloqueIndustrial(new Posicion(2, 0)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(2, 1)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(2, 2)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(2, 3)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(2, 4)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(3, 0)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(3, 1)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(3, 2)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(3, 3)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(3, 4)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertEquals(20.0 / 25.0, r.getDensidad(), 0.0001);
        assertEquals(
                10 * ReglasSimulacion.CONTAMINACION_POR_INDUSTRIAL + ReglasSimulacion.EXTRA_CONTAMINACION_DENSIDAD,
                r.getContaminacion()
        );
        assertEquals(EstadoSimulacion.DESEQUILIBRIO_ESTRUCTURAL, r.getEstadoSimulacion());
    }

    @Test
    void soporteDeTransporteSeCalculaCorrectamente() {
        Ciudad ciudad = new Ciudad("Transportes", 10, 10);
        ciudad.addBloque(new BloqueTransporte(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(0, 1)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertEquals(2 * ReglasSimulacion.TRANSPORTE_SOPORTE, r.getSoporteTransporte());
    }

    @Test
    void densidadBaja_daResultadoCoherente() {
        Ciudad ciudad = new Ciudad("BajaDensidad", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertTrue(r.getDensidad() < ReglasSimulacion.PENALIZACION_DENSIDAD_ALTA);
        assertTrue(r.getContaminacion() >= 0);
    }

    @Test
    void ciudadExpandidaConMismosBloques_tieneMenorDensidad() {
        Ciudad ciudad = new Ciudad("Expandible", 5, 5);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 3)));

        ResultadoSimulacion antes = simulador.simular(ciudad);

        ciudad.expandir(10, 10);

        ResultadoSimulacion despues = simulador.simular(ciudad);

        assertTrue(despues.getDensidad() < antes.getDensidad());
        assertEquals(antes.getBloquesTotales(), despues.getBloquesTotales());
    }

    @Test
    void sinBloquesActivos_estadoCorrecto() {
        Ciudad ciudad = new Ciudad("Inactiva", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.desactivarBloque(new Posicion(0, 0));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertEquals(EstadoSimulacion.SIN_BLOQUES_ACTIVOS, r.getEstadoSimulacion());
    }

    @Test
    void mismaCiudad_produceSiempreElMismoResultado() {
        Ciudad ciudad = new Ciudad("Determinista", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 2)));

        ResultadoSimulacion r1 = simulador.simular(ciudad);
        ResultadoSimulacion r2 = simulador.simular(ciudad);

        assertEquals(r1.getBloquesTotales(), r2.getBloquesTotales());
        assertEquals(r1.getBloquesActivos(), r2.getBloquesActivos());
        assertEquals(r1.getBloquesInactivos(), r2.getBloquesInactivos());
        assertEquals(r1.getEnergiaProducida(), r2.getEnergiaProducida());
        assertEquals(r1.getConsumoEnergetico(), r2.getConsumoEnergetico());
        assertEquals(r1.getEquilibrioEnergetico(), r2.getEquilibrioEnergetico());
        assertEquals(r1.getDemandaServicios(), r2.getDemandaServicios());
        assertEquals(r1.getCoberturaServicios(), r2.getCoberturaServicios());
        assertEquals(r1.getPresionIndustrial(), r2.getPresionIndustrial());
        assertEquals(r1.getSoporteTransporte(), r2.getSoporteTransporte());
        assertEquals(r1.getContaminacion(), r2.getContaminacion());
        assertEquals(r1.getEstadoSimulacion(), r2.getEstadoSimulacion());
        assertEquals(r1.getDensidad(), r2.getDensidad(), 0.000001);
        assertEquals(r1.getBienestar(), r2.getBienestar(), 0.000001);
        assertEquals(r1.getEstabilidadBasica(), r2.getEstabilidadBasica(), 0.000001);
        assertEquals(r1.getRatioEnergetico(), r2.getRatioEnergetico(), 0.000001);
        assertEquals(r1.getRatioCoberturaServicios(), r2.getRatioCoberturaServicios(), 0.000001);
        assertEquals(r1.getConteoPorTipo(), r2.getConteoPorTipo());
    }

    @Test
    void resultadoSimulacion_esAutosuficienteYCoherente() {
        Ciudad ciudad = new Ciudad("Autosuficiente", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertNotNull(r.getNombreCiudad());
        assertTrue(r.getFilas() > 0);
        assertTrue(r.getColumnas() > 0);
        assertTrue(r.getCapacidadMaxima() > 0);
        assertNotNull(r.getTipoEstructural());
        assertNotNull(r.getEstadoSimulacion());
        assertNotNull(r.getConteoPorTipo());

        assertEquals(r.getBloquesTotales(), r.getBloquesActivos() + r.getBloquesInactivos());
        assertEquals(r.getEnergiaProducida() - r.getConsumoEnergetico(), r.getEquilibrioEnergetico());
        assertTrue(r.getDensidad() >= 0.0 && r.getDensidad() <= 1.0);
        assertTrue(r.getBienestar() >= 0.0 && r.getBienestar() <= 1.0);
        assertTrue(r.getEstabilidadBasica() >= 0.0 && r.getEstabilidadBasica() <= 1.0);
        assertTrue(r.getRatioEnergetico() >= 0.0);
        assertTrue(r.getRatioCoberturaServicios() >= 0.0);

        for (TipoBloque tipo : TipoBloque.values()) {
            assertTrue(r.getConteoPorTipo().containsKey(tipo));
        }
    }

    @Test
    void ciudadConSistemaEquilibrado_daEstadoEstableOInestablePeroNoDeficitNiVacia() {
        Ciudad ciudad = new Ciudad("Equilibrada", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 3)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(0, 4)));

        ResultadoSimulacion r = simulador.simular(ciudad);

        assertTrue(
                r.getEstadoSimulacion() == EstadoSimulacion.SIMULACION_ESTABLE ||
                        r.getEstadoSimulacion() == EstadoSimulacion.SIMULACION_INESTABLE
        );
    }
}