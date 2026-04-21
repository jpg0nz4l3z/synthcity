package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueIndustrial;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_1.bloques.BloqueServicios;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.prediccion.*;

import static org.junit.jupiter.api.Assertions.*;

class PredictorCiudadTest {

    private final SimuladorCiudad simulador = new SimuladorCiudad();
    private final EvaluadorCiudad evaluador = new EvaluadorCiudad();
    private final PredictorCiudad predictor = new PredictorCiudad();

    @Test
    void sinDatos_devuelveSinBase() {
        Ciudad ciudad = new Ciudad("Vacia", 10, 10);
        ResultadoSimulacion rs = simulador.simular(ciudad);
        MetricaCiudad metrica = new MetricaCiudad(rs);
        PredictionInput input = PredictionInput.desdeMetrica(metrica);

        PredictionResult pr = predictor.predecir(input);

        assertEquals(TendenciaPredicha.SIN_BASE, pr.getTendenciaPredicha());
    }

    @Test
    void bajaActividad_devuelveRiesgoAlto() {
        Ciudad ciudad = new Ciudad("BajaActividad", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 2)));

        ciudad.desactivarBloque(new Posicion(0, 0));
        ciudad.desactivarBloque(new Posicion(0, 1));

        PredictionResult pr = evaluador.predecir(simulador.simular(ciudad));

        assertEquals(TendenciaPredicha.RIESGO_ALTO, pr.getTendenciaPredicha());
    }

    @Test
    void densidadExtremaYDeficits_devuelveSaturacionProbable() {
        Ciudad ciudad = new Ciudad("Extrema", 5, 5);

        // 22 bloques en total -> densidad 22/25 = 0.88
        for (int i = 0; i < 22; i++) {
            if (i < 20) {
                ciudad.addBloque(new BloqueResidencial(new Posicion(i / 5, i % 5)));
            } else {
                ciudad.addBloque(new BloqueIndustrial(new Posicion(i / 5, i % 5)));
            }
        }

        PredictionResult pr = evaluador.predecir(simulador.simular(ciudad));

        assertEquals(TendenciaPredicha.SATURACION_PROBABLE, pr.getTendenciaPredicha());
    }

    @Test
    void equilibrioRazonable_devuelveEstableOMejoraProbable() {
        Ciudad ciudad = new Ciudad("Equilibrada", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 3)));

        PredictionResult pr = evaluador.predecir(simulador.simular(ciudad));

        assertTrue(
                pr.getTendenciaPredicha() == TendenciaPredicha.ESTABLE ||
                        pr.getTendenciaPredicha() == TendenciaPredicha.MEJORA_PROBABLE
        );
    }

    @Test
    void predictionResult_enRangosValidos() {
        Ciudad ciudad = new Ciudad("Pred", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));

        PredictionResult pr = evaluador.predecir(simulador.simular(ciudad));

        assertTrue(pr.getScorePredicho() >= 0.0 && pr.getScorePredicho() <= 100.0);
        assertTrue(pr.getConfianza() >= 0.0 && pr.getConfianza() <= 1.0);
        assertNotNull(pr.getMensajePrediccion());
        assertFalse(pr.getMensajePrediccion().isBlank());
    }
}