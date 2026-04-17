package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PredictorCiudadTest {

    @Test
    void deberia_devolver_sin_base_si_la_ciudad_esta_vacia() {
        PredictorCiudad predictor = new PredictorCiudad();

        Map<TipoBloque, Integer> conteo = crearConteoBase();

        ResultadoSimulacion simulacion = new ResultadoSimulacion(
                "Vacia",
                5,
                5,
                25,
                0,
                0,
                0,
                conteo,
                EstadoSimulacion.CIUDAD_VACIA
        );

        MetricaCiudad metrica = new MetricaCiudad(simulacion);
        PredictionInput input = PredictionInput.desdeMetrica(metrica);

        PredictionResult result = predictor.predecir(input);

        assertEquals(TendenciaPredicha.SIN_BASE, result.getTendenciaPredicha());
        assertEquals(0.0, result.getScorePredicho(), 0.0001);
    }

    @Test
    void deberia_devolver_riesgo_alto_si_no_hay_bloques_activos() {
        PredictorCiudad predictor = new PredictorCiudad();

        Map<TipoBloque, Integer> conteo = crearConteoBase();
        conteo.put(TipoBloque.RESIDENCIAL, 3);
        conteo.put(TipoBloque.ENERGIA, 1);
        conteo.put(TipoBloque.SERVICIOS, 1);

        ResultadoSimulacion simulacion = new ResultadoSimulacion(
                "Critica",
                2,
                3,
                6,
                5,
                0,
                5,
                conteo,
                EstadoSimulacion.SIN_BLOQUES_ACTIVOS
        );

        MetricaCiudad metrica = new MetricaCiudad(simulacion);
        PredictionInput input = PredictionInput.desdeMetrica(metrica);

        PredictionResult result = predictor.predecir(input);

        assertEquals(TendenciaPredicha.RIESGO_ALTO, result.getTendenciaPredicha());
    }

    @Test
    void deberia_devolver_mejora_probable_con_input_predictivo_favorable() {
        PredictorCiudad predictor = new PredictorCiudad();

        PredictionInput input = new PredictionInput(
                20,
                16,
                4,
                30,
                0.80,
                0.20,
                0.60,
                1.00,
                0.80,
                0.80,
                0.80,
                0.20,
                0.40,
                0.85,
                EstadoSimulacion.EJECUTADA
        );

        PredictionResult result = predictor.predecir(input);

        assertEquals(TendenciaPredicha.MEJORA_PROBABLE, result.getTendenciaPredicha());
        assertTrue(result.getScorePredicho() >= 75.0);
        assertNotNull(result.getMensajePrediccion());
    }

    @Test
    void deberia_lanzar_error_si_input_es_null() {
        PredictorCiudad predictor = new PredictorCiudad();

        assertThrows(IllegalArgumentException.class, () -> predictor.predecir(null));
    }

    private Map<TipoBloque, Integer> crearConteoBase() {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            conteo.put(tipo, 0);
        }
        return conteo;
    }
}
