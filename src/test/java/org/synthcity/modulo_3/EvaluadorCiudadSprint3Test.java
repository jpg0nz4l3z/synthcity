package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.*;
import org.synthcity.modulo_2.SimuladorCiudad;

import static org.junit.jupiter.api.Assertions.*;

class EvaluadorCiudadSprint3Test {

    private final SimuladorCiudad simulador = new SimuladorCiudad();

    @Test
    void evaluar_registraDatoEnDataset() {
        Ciudad ciudad = new Ciudad("Dataset", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));

        EvaluadorCiudad evaluador = new EvaluadorCiudad();

        evaluador.evaluar(simulador.simular(ciudad));

        assertEquals(1, evaluador.getConstructorDataset().getTamano());
        assertFalse(evaluador.getConstructorDataset().estaVacio());
    }

    @Test
    void ciudadConColapsoEnergetico_devuelveTendenciaColapsando() {
        Ciudad ciudad = new Ciudad("Colapso", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 1)));

        EvaluadorCiudad evaluador = new EvaluadorCiudad();

        ResultadoEvaluacion evaluacion = evaluador.evaluar(simulador.simular(ciudad));

        assertEquals(TendenciaTemporal.COLAPSANDO, evaluacion.getTendenciaTemporal());
        assertTrue(evaluacion.getAlertas().contains(AlertaEvaluacion.COLAPSO_DETECTADO));
    }

    @Test
    void ciudadSaturada_detectaNecesidadExpansion() {
        Ciudad ciudad = new Ciudad("Saturada", 2, 2);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueServicios(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(1, 1)));

        EvaluadorCiudad evaluador = new EvaluadorCiudad();

        ResultadoEvaluacion evaluacion = evaluador.evaluar(simulador.simular(ciudad));

        assertTrue(evaluacion.getAlertas().contains(AlertaEvaluacion.NECESIDAD_EXPANSION));
        assertEquals(TendenciaTemporal.SATURANDO, evaluacion.getTendenciaTemporal());
    }

    @Test
    void evaluarConCiudadYSimulable_expandeYResimula() {
        Ciudad ciudad = new Ciudad("Expandible", 2, 2);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueServicios(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(1, 1)));

        EvaluadorCiudad evaluador = new EvaluadorCiudad(
                ciudadParaSimular -> simulador.simular(ciudadParaSimular)
        );

        ResultadoEvaluacion evaluacion = evaluador.evaluar(simulador.simular(ciudad), ciudad);

        assertTrue(evaluacion.isExpansionEjecutada());
        assertNotNull(evaluacion.getResultadoExpansion());
        assertTrue(ciudad.getFilas() > 2);
        assertTrue(ciudad.getColumnas() > 2);
    }

    @Test
    void evaluarSinCiudadNoEjecutaExpansionAunqueLaNecesite() {
        Ciudad ciudad = new Ciudad("SinExpansionReal", 2, 2);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueServicios(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(1, 1)));

        EvaluadorCiudad evaluador = new EvaluadorCiudad();

        ResultadoEvaluacion evaluacion = evaluador.evaluar(simulador.simular(ciudad));

        assertFalse(evaluacion.isExpansionEjecutada());
        assertNull(evaluacion.getResultadoExpansion());
        assertTrue(evaluacion.getAlertas().contains(AlertaEvaluacion.NECESIDAD_EXPANSION));
    }
}