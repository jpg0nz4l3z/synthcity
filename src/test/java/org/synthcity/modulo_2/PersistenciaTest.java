package org.synthcity.modulo_2;
/*
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueIndustrial;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_1.bloques.BloqueServicios;
import org.synthcity.modulo_1.bloques.BloqueTransporte;
import org.synthcity.modulo_4.CiudadRepository;


import static org.junit.jupiter.api.Assertions.*;

public class PersistenciaTest {
    private Ciudad construirCiudadPruebaCompleta() {
        Ciudad ciudad = new Ciudad("CiudadTest", 20, 20);

        ciudad.addBloque(new BloqueResidencial(new Posicion(5,5)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(6,5)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(5,6)));


        ciudad.addBloque(new BloqueServicios(new Posicion(10,10)));

        ciudad.addBloque(new BloqueTransporte(new Posicion(15,15)));

        ciudad.addBloque(new BloqueIndustrial(new Posicion(2,2)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(18,18)));

        return ciudad;
    }

    private final SimuladorCiudad simulador = new SimuladorCiudad();
    private final CiudadRepository ciudadRepo = new CiudadRepository();

    @Test
    void testSimulacionSobreCiudadOriginalYRecuperadaSonIdenticas() {

        Ciudad original = construirCiudadPruebaCompleta();

        ResultadoSimulacion resOriginal = simulador.simular(original);

        int idGuardado = ciudadRepo.guardar(original);

        Ciudad recuperada = ciudadRepo.cargar(idGuardado);

        ResultadoSimulacion resRecuperada = simulador.simular(recuperada);

        compararResultadosSimulacion(resOriginal, resRecuperada);
    }

    private void compararResultadosSimulacion(ResultadoSimulacion r1, ResultadoSimulacion r2) {
        assertEquals(r1.getCiclosEjecutados(), r2.getCiclosEjecutados());
        assertEquals(r1.getMotivoParada(), r2.getMotivoParada());
        assertEquals(r1.isNecesidadExpansionDetectada(), r2.isNecesidadExpansionDetectada());
        assertEquals(r1.getTendenciaEstabilidad(), r2.getTendenciaEstabilidad(), 1e-9);
        assertEquals(r1.getTendenciaContaminacion(), r2.getTendenciaContaminacion(), 1e-9);

        var ciclos1 = r1.getCiclos();
        var ciclos2 = r2.getCiclos();
        assertEquals(ciclos1.size(), ciclos2.size());

        for (int i = 0; i < ciclos1.size(); i++) {
            EstadoCiclo e1 = ciclos1.get(i);
            EstadoCiclo e2 = ciclos2.get(i);
            assertEquals(e1.getNumeroCiclo(), e2.getNumeroCiclo());
            assertEquals(e1.getEnergiaProducida(), e2.getEnergiaProducida());
            assertEquals(e1.getConsumoEnergetico(), e2.getConsumoEnergetico());
            assertEquals(e1.getEquilibrioEnergetico(), e2.getEquilibrioEnergetico());
            assertEquals(e1.getDemandaServicios(), e2.getDemandaServicios());
            assertEquals(e1.getCoberturaServicios(), e2.getCoberturaServicios());
            assertEquals(e1.getCoberturaServiciosPonderada(), e2.getCoberturaServiciosPonderada(), 1e-9);
            assertEquals(e1.getEficienciaTransporte(), e2.getEficienciaTransporte(), 1e-9);
            assertEquals(e1.getPresionIndustrial(), e2.getPresionIndustrial());
            assertEquals(e1.getSoporteTransporte(), e2.getSoporteTransporte());
            assertEquals(e1.getContaminacionCiclo(), e2.getContaminacionCiclo());
            assertEquals(e1.getContaminacionAcumulada(), e2.getContaminacionAcumulada());
            assertEquals(e1.getBienestar(), e2.getBienestar(), 1e-9);
            assertEquals(e1.getEstabilidad(), e2.getEstabilidad(), 1e-9);
            assertEquals(e1.getDensidad(), e2.getDensidad(), 1e-9);
            assertEquals(e1.isNecesidadExpansionDetectada(), e2.isNecesidadExpansionDetectada());
            assertEquals(e1.getEstadoSimulacion(), e2.getEstadoSimulacion());
        }
    }
}*/