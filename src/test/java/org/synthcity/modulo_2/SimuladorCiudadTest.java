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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimuladorCiudadTest {

    private final SimuladorCiudad simulador = new SimuladorCiudad();

    @Test
    void ciudadNula_lanzaCiudadInvalidaParaSimulacionException() {
        assertThrows(CiudadInvalidaParaSimulacionException.class, () -> simulador.simular(null));
    }

    @Test
    void ciudadVacia_devuelveResultadoSinCiclos() {
        Ciudad ciudad = new Ciudad("Vacia", 10, 10);

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(EstadoSimulacion.CIUDAD_VACIA, resultado.getEstadoSimulacion());
        assertEquals(MotivoParadaSimulacion.CIUDAD_VACIA, resultado.getMotivoParada());
        assertEquals(0, resultado.getCiclosEjecutados());
        assertTrue(resultado.getCiclos().isEmpty());
        assertTrue(resultado.ciudadEstaVacia());
        assertFalse(resultado.hayBloquesActivos());
    }

    @Test
    void ciudadSinBloquesActivos_devuelveResultadoSinCiclos() {
        Ciudad ciudad = new Ciudad("Inactiva", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.desactivarBloque(new Posicion(0, 0));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(EstadoSimulacion.SIN_BLOQUES_ACTIVOS, resultado.getEstadoSimulacion());
        assertEquals(MotivoParadaSimulacion.SIN_BLOQUES_ACTIVOS, resultado.getMotivoParada());
        assertEquals(0, resultado.getCiclosEjecutados());
        assertEquals(1, resultado.getBloquesTotales());
        assertEquals(0, resultado.getBloquesActivos());
        assertEquals(1, resultado.getBloquesInactivos());
    }

    @Test
    void ciudadEquilibrada_ejecutaTodosLosCiclosYConservaConteos() {
        Ciudad ciudad = crearCiudadEquilibrada();

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(MotivoParadaSimulacion.CICLOS_COMPLETADOS, resultado.getMotivoParada());
        assertEquals(ReglasSimulacion.MAX_CICLOS, resultado.getCiclosEjecutados());
        assertEquals(resultado.getCiclosEjecutados(), resultado.getCiclos().size());
        assertEquals(5, resultado.getBloquesTotales());
        assertEquals(5, resultado.getBloquesActivos());
        assertEquals(0, resultado.getBloquesInactivos());
        assertEquals(2, resultado.getCantidadPorTipo(TipoBloque.ENERGIA));
        assertFalse(resultado.hayDeficitEnergetico());
    }

    @Test
    void ciudadConDeficitEnergeticoSostenido_paraPorColapso() {
        Ciudad ciudad = new Ciudad("ColapsoEnergetico", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 1)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(MotivoParadaSimulacion.COLAPSO_ENERGETICO, resultado.getMotivoParada());
        assertEquals(EstadoSimulacion.COLAPSO_ENERGETICO, resultado.getEstadoSimulacion());
        assertEquals(ReglasSimulacion.CICLOS_CONSECUTIVOS_COLAPSO, resultado.getCiclosEjecutados());
        assertTrue(resultado.hayDeficitEnergetico());
    }

    @Test
    void ciudadConSaturacionCriticaSostenida_paraPorSaturacion() {
        Ciudad ciudad = new Ciudad("Saturada", 2, 2);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueServicios(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(1, 1)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(1.0, resultado.getDensidad(), 0.0001);
        assertEquals(MotivoParadaSimulacion.SATURACION_CRITICA, resultado.getMotivoParada());
        assertEquals(EstadoSimulacion.SATURACION_CRITICA, resultado.getEstadoSimulacion());
        assertEquals(ReglasSimulacion.CICLOS_CONSECUTIVOS_SATURACION, resultado.getCiclosEjecutados());
        assertTrue(resultado.isNecesidadExpansionDetectada());
    }

    @Test
    void coberturaServiciosPonderada_esAltaCuandoServiciosEstanCerca() {
        Ciudad ciudad = new Ciudad("ServiciosCerca", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 1)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertTrue(resultado.getCoberturaServiciosPonderada() >= 0.70);
        assertTrue(resultado.getCiclos().get(0).getCoberturaServiciosPonderada() >= 0.70);
    }

    @Test
    void coberturaServiciosPonderada_esBajaCuandoServiciosEstanLejos() {
        Ciudad ciudad = new Ciudad("ServiciosLejos", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueServicios(new Posicion(9, 9)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(0.0, resultado.getCoberturaServiciosPonderada(), 0.0001);
    }

    @Test
    void eficienciaTransporte_esCeroSinBloquesDeTransporte() {
        Ciudad ciudad = new Ciudad("SinTransporte", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 2)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(0.0, resultado.getEficienciaTransporte(), 0.0001);
    }

    @Test
    void eficienciaTransporte_subeConTransporteProximo() {
        Ciudad ciudad = new Ciudad("TransporteCerca", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(1, 0)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertTrue(resultado.getEficienciaTransporte() > 0.0);
    }

    @Test
    void contaminacionAcumulada_creceMonotonicamente() {
        Ciudad ciudad = new Ciudad("IndustrialEstable", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 1)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);
        List<EstadoCiclo> ciclos = resultado.getCiclos();

        for (int i = 1; i < ciclos.size(); i++) {
            assertTrue(ciclos.get(i).getContaminacionAcumulada() >= ciclos.get(i - 1).getContaminacionAcumulada());
        }
        assertEquals(ReglasSimulacion.MAX_CICLOS * ReglasSimulacion.CONTAMINACION_POR_INDUSTRIAL,
                resultado.getContaminacionAcumulada());
        assertEquals(resultado.getContaminacionAcumulada(), resultado.getContaminacion());
        assertEquals(
                ciclos.get(ciclos.size() - 1).getContaminacionAcumulada() - ciclos.get(0).getContaminacionAcumulada(),
                resultado.getTendenciaContaminacion()
        );
    }

    @Test
    void estabilidadMediaYtendenciaSalenDelHistorial() {
        Ciudad ciudad = new Ciudad("Tendencia", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 1)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        double media = resultado.getCiclos()
                .stream()
                .mapToDouble(EstadoCiclo::getEstabilidad)
                .average()
                .orElseThrow();
        double tendencia = resultado.getCiclos().get(resultado.getCiclosEjecutados() - 1).getEstabilidad()
                - resultado.getCiclos().get(0).getEstabilidad();

        assertEquals(media, resultado.getEstabilidadMedia(), 0.000001);
        assertEquals(media, resultado.getEstabilidadBasica(), 0.000001);
        assertEquals(tendencia, resultado.getTendenciaEstabilidad(), 0.000001);
        assertTrue(resultado.getTendenciaEstabilidad() <= 0.0);
    }

    @Test
    void simulacionEsDeterministaParaMismaCiudadSinExpansion() {
        Ciudad ciudad = crearCiudadEquilibrada();

        ResultadoSimulacion primera = simulador.simular(ciudad);
        ResultadoSimulacion segunda = simulador.simular(ciudad);

        assertEquals(primera.getMotivoParada(), segunda.getMotivoParada());
        assertEquals(primera.getCiclosEjecutados(), segunda.getCiclosEjecutados());
        assertEquals(primera.getContaminacionAcumulada(), segunda.getContaminacionAcumulada());
        assertEquals(primera.getTendenciaEstabilidad(), segunda.getTendenciaEstabilidad(), 0.000001);
        assertEquals(primera.getCoberturaServiciosPonderada(), segunda.getCoberturaServiciosPonderada(), 0.000001);
        assertFalse(segunda.isHistorialReiniciadoPorExpansion());
    }

    @Test
    void ciudadExpandida_iniciaHistorialNuevoYReseteaFlag() {
        Ciudad ciudad = crearCiudadEquilibrada();

        ResultadoSimulacion antes = simulador.simular(ciudad);

        ciudad.expandir();

        assertTrue(ciudad.fueExpandidaDesdeUltimaSimulacion());

        ResultadoSimulacion despues = simulador.simular(ciudad);

        assertTrue(despues.isHistorialReiniciadoPorExpansion());
        assertFalse(ciudad.fueExpandidaDesdeUltimaSimulacion());
        assertEquals(1, despues.getCiclos().get(0).getNumeroCiclo());
        assertTrue(despues.getDensidad() < antes.getDensidad());
    }

    @Test
    void resultadoSimulacion_esAutosuficienteParaModulo3() {
        ResultadoSimulacion resultado = simulador.simular(crearCiudadEquilibrada());
        Map<TipoBloque, Integer> conteo = resultado.getConteoPorTipo();

        assertNotNull(resultado.getNombreCiudad());
        assertNotNull(resultado.getTipoEstructural());
        assertNotNull(resultado.getEstadoSimulacion());
        assertNotNull(resultado.getMotivoParada());
        assertNotNull(resultado.getCiclos());
        assertEquals(resultado.getBloquesTotales(), resultado.getBloquesActivos() + resultado.getBloquesInactivos());
        assertEquals(resultado.getEnergiaProducida() - resultado.getConsumoEnergetico(), resultado.getEquilibrioEnergetico());
        assertEquals(resultado.getCiclosEjecutados(), resultado.getCiclos().size());

        for (TipoBloque tipo : TipoBloque.values()) {
            assertTrue(conteo.containsKey(tipo));
            assertNotNull(conteo.get(tipo));
        }
    }

    private Ciudad crearCiudadEquilibrada() {
        Ciudad ciudad = new Ciudad("Equilibrada", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueServicios(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(2, 0)));
        return ciudad;
    }
}
