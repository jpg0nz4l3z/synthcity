package org.synthcity.modulo_1;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.bloques.Bloque;
import org.synthcity.modulo_2.EstadoCiclo;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersistenciaCiudadSprint4Test {

    private final SimuladorCiudad simulador = new SimuladorCiudad();

    @Test
    void listarBloquesExponeTipoPosicionYEstadoParaPersistencia() {
        Ciudad ciudad = new Ciudad("Persistible", 10, 10);

        ciudad.addBloque(Bloque.crearBloque(TipoBloque.RESIDENCIAL, new Posicion(0, 0), true));
        ciudad.addBloque(Bloque.crearBloque(TipoBloque.ENERGIA, new Posicion(0, 1), false));
        ciudad.addBloque(Bloque.crearBloque(TipoBloque.INDUSTRIAL, new Posicion(1, 0), true));
        ciudad.addBloque(Bloque.crearBloque(TipoBloque.SERVICIOS, new Posicion(1, 1), false));
        ciudad.addBloque(Bloque.crearBloque(TipoBloque.TRANSPORTE, new Posicion(2, 0), true));

        List<Bloque> bloques = ciudad.listarBloques();

        assertEquals(5, bloques.size());

        assertBloquePersistido(ciudad, new Posicion(0, 0), TipoBloque.RESIDENCIAL, true);
        assertBloquePersistido(ciudad, new Posicion(0, 1), TipoBloque.ENERGIA, false);
        assertBloquePersistido(ciudad, new Posicion(1, 0), TipoBloque.INDUSTRIAL, true);
        assertBloquePersistido(ciudad, new Posicion(1, 1), TipoBloque.SERVICIOS, false);
        assertBloquePersistido(ciudad, new Posicion(2, 0), TipoBloque.TRANSPORTE, true);
    }

    @Test
    void ciudadReconstruidaYRepobladaTieneMismaEstructuraQueOriginal() {
        Ciudad original = construirCiudadPersistible();

        Ciudad reconstruida = reconstruirDesdeGetters(original);

        assertEquals(original.getNombre(), reconstruida.getNombre());
        assertEquals(original.getFilas(), reconstruida.getFilas());
        assertEquals(original.getColumnas(), reconstruida.getColumnas());
        assertEquals(original.getCapacidadMaxima(), reconstruida.getCapacidadMaxima());
        assertEquals(original.getTipoEstructural(), reconstruida.getTipoEstructural());
        assertEquals(original.getExpansionesRealizadas(), reconstruida.getExpansionesRealizadas());
        assertEquals(original.contarBloques(), reconstruida.contarBloques());

        for (Bloque bloqueOriginal : original.listarBloques()) {
            Bloque bloqueReconstruido = reconstruida.getBloque(bloqueOriginal.getPosicion());

            assertNotNull(bloqueReconstruido);
            assertEquals(bloqueOriginal.getTipo(), bloqueReconstruido.getTipo());
            assertEquals(bloqueOriginal.getPosicion(), bloqueReconstruido.getPosicion());
            assertEquals(bloqueOriginal.estaActivo(), bloqueReconstruido.estaActivo());
        }
    }

    @Test
    void ciudadReconstruidaProduceMismaSimulacionQueOriginal() {
        Ciudad original = construirCiudadPersistible();
        Ciudad reconstruida = reconstruirDesdeGetters(original);

        ResultadoSimulacion resultadoOriginal = simulador.simular(original);
        ResultadoSimulacion resultadoReconstruido = simulador.simular(reconstruida);

        assertResultadosSimulacionEquivalentes(resultadoOriginal, resultadoReconstruido);
    }

    @Test
    void ciudadExpandidaReconstruidaConservaExpansionesYPuedeExpandirseIgualQueOriginal() {
        Ciudad original = construirCiudadPersistible();

        original.expandir(); // 10x10 -> 20x20
        original.addBloque(Bloque.crearBloque(TipoBloque.SERVICIOS, new Posicion(15, 15), true));
        original.marcarSimulacionEjecutada();

        Ciudad reconstruida = reconstruirDesdeGetters(original);

        assertEquals(original.getFilas(), reconstruida.getFilas());
        assertEquals(original.getColumnas(), reconstruida.getColumnas());
        assertEquals(original.getTipoEstructural(), reconstruida.getTipoEstructural());
        assertEquals(original.getExpansionesRealizadas(), reconstruida.getExpansionesRealizadas());
        assertEquals(original.puedeExpandirse(), reconstruida.puedeExpandirse());

        ResultadoSimulacion resultadoOriginal = simulador.simular(original);
        ResultadoSimulacion resultadoReconstruido = simulador.simular(reconstruida);

        assertResultadosSimulacionEquivalentes(resultadoOriginal, resultadoReconstruido);
    }

    private Ciudad construirCiudadPersistible() {
        Ciudad ciudad = new Ciudad("Persistible", 10, 10);

        ciudad.addBloque(Bloque.crearBloque(TipoBloque.ENERGIA, new Posicion(0, 0), true));
        ciudad.addBloque(Bloque.crearBloque(TipoBloque.ENERGIA, new Posicion(0, 1), true));
        ciudad.addBloque(Bloque.crearBloque(TipoBloque.RESIDENCIAL, new Posicion(1, 0), true));
        ciudad.addBloque(Bloque.crearBloque(TipoBloque.RESIDENCIAL, new Posicion(1, 1), false));
        ciudad.addBloque(Bloque.crearBloque(TipoBloque.INDUSTRIAL, new Posicion(2, 0), true));
        ciudad.addBloque(Bloque.crearBloque(TipoBloque.SERVICIOS, new Posicion(2, 1), true));
        ciudad.addBloque(Bloque.crearBloque(TipoBloque.TRANSPORTE, new Posicion(3, 0), false));

        return ciudad;
    }

    private Ciudad reconstruirDesdeGetters(Ciudad original) {
        Ciudad reconstruida = Ciudad.reconstruir(
                original.getNombre(),
                original.getFilas(),
                original.getColumnas(),
                original.getExpansionesRealizadas()
        );

        for (Bloque bloque : original.listarBloques()) {
            Posicion posicion = bloque.getPosicion();

            Bloque copia = Bloque.crearBloque(
                    bloque.getTipo(),
                    new Posicion(posicion.getFila(), posicion.getColumna()),
                    bloque.estaActivo()
            );

            reconstruida.addBloque(copia);
        }

        return reconstruida;
    }

    private void assertBloquePersistido(Ciudad ciudad,
                                        Posicion posicion,
                                        TipoBloque tipoEsperado,
                                        boolean activoEsperado) {
        Bloque bloque = ciudad.getBloque(posicion);

        assertNotNull(bloque);
        assertEquals(tipoEsperado, bloque.getTipo());
        assertEquals(posicion, bloque.getPosicion());
        assertEquals(activoEsperado, bloque.estaActivo());
    }

    private void assertResultadosSimulacionEquivalentes(ResultadoSimulacion esperado,
                                                        ResultadoSimulacion actual) {
        assertEquals(esperado.getNombreCiudad(), actual.getNombreCiudad());
        assertEquals(esperado.getFilas(), actual.getFilas());
        assertEquals(esperado.getColumnas(), actual.getColumnas());
        assertEquals(esperado.getCapacidadMaxima(), actual.getCapacidadMaxima());

        assertEquals(esperado.getBloquesTotales(), actual.getBloquesTotales());
        assertEquals(esperado.getBloquesActivos(), actual.getBloquesActivos());
        assertEquals(esperado.getBloquesInactivos(), actual.getBloquesInactivos());
        assertEquals(esperado.getConteoPorTipo(), actual.getConteoPorTipo());

        assertEquals(esperado.getEstadoSimulacion(), actual.getEstadoSimulacion());
        assertEquals(esperado.getMotivoParada(), actual.getMotivoParada());
        assertEquals(esperado.getTipoEstructural(), actual.getTipoEstructural());

        assertEquals(esperado.getDensidad(), actual.getDensidad(), 0.000001);
        assertEquals(esperado.getEnergiaProducida(), actual.getEnergiaProducida());
        assertEquals(esperado.getConsumoEnergetico(), actual.getConsumoEnergetico());
        assertEquals(esperado.getEquilibrioEnergetico(), actual.getEquilibrioEnergetico());
        assertEquals(esperado.getDemandaServicios(), actual.getDemandaServicios());
        assertEquals(esperado.getCoberturaServicios(), actual.getCoberturaServicios());
        assertEquals(esperado.getPresionIndustrial(), actual.getPresionIndustrial());
        assertEquals(esperado.getSoporteTransporte(), actual.getSoporteTransporte());
        assertEquals(esperado.getContaminacion(), actual.getContaminacion());
        assertEquals(esperado.getContaminacionAcumulada(), actual.getContaminacionAcumulada());

        assertEquals(esperado.getBienestar(), actual.getBienestar(), 0.000001);
        assertEquals(esperado.getEstabilidadBasica(), actual.getEstabilidadBasica(), 0.000001);
        assertEquals(esperado.getEstabilidadMedia(), actual.getEstabilidadMedia(), 0.000001);
        assertEquals(esperado.getRatioEnergetico(), actual.getRatioEnergetico(), 0.000001);
        assertEquals(esperado.getRatioCoberturaServicios(), actual.getRatioCoberturaServicios(), 0.000001);
        assertEquals(esperado.getCoberturaServiciosPonderada(), actual.getCoberturaServiciosPonderada(), 0.000001);
        assertEquals(esperado.getEficienciaTransporte(), actual.getEficienciaTransporte(), 0.000001);

        assertEquals(esperado.getCiclosEjecutados(), actual.getCiclosEjecutados());
        assertEquals(esperado.getCiclos().size(), actual.getCiclos().size());

        for (int i = 0; i < esperado.getCiclos().size(); i++) {
            assertCiclosEquivalentes(esperado.getCiclos().get(i), actual.getCiclos().get(i));
        }
    }

    private void assertCiclosEquivalentes(EstadoCiclo esperado, EstadoCiclo actual) {
        assertEquals(esperado.getNumeroCiclo(), actual.getNumeroCiclo());
        assertEquals(esperado.getEnergiaProducida(), actual.getEnergiaProducida());
        assertEquals(esperado.getConsumoEnergetico(), actual.getConsumoEnergetico());
        assertEquals(esperado.getEquilibrioEnergetico(), actual.getEquilibrioEnergetico());
        assertEquals(esperado.getDemandaServicios(), actual.getDemandaServicios());
        assertEquals(esperado.getCoberturaServicios(), actual.getCoberturaServicios());
        assertEquals(esperado.getPresionIndustrial(), actual.getPresionIndustrial());
        assertEquals(esperado.getSoporteTransporte(), actual.getSoporteTransporte());
        assertEquals(esperado.getContaminacionCiclo(), actual.getContaminacionCiclo());
        assertEquals(esperado.getContaminacionAcumulada(), actual.getContaminacionAcumulada());

        assertEquals(esperado.getCoberturaServiciosPonderada(), actual.getCoberturaServiciosPonderada(), 0.000001);
        assertEquals(esperado.getEficienciaTransporte(), actual.getEficienciaTransporte(), 0.000001);
        assertEquals(esperado.getBienestar(), actual.getBienestar(), 0.000001);
        assertEquals(esperado.getEstabilidad(), actual.getEstabilidad(), 0.000001);
        assertEquals(esperado.getDensidad(), actual.getDensidad(), 0.000001);

        assertEquals(esperado.isNecesidadExpansionDetectada(), actual.isNecesidadExpansionDetectada());
        assertEquals(esperado.getEstadoSimulacion(), actual.getEstadoSimulacion());
    }
}
