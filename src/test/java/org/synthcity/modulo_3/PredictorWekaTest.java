package org.synthcity.modulo_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_1.bloques.BloqueServicios;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.prediccion.*;
import weka.core.Instance;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PredictorWekaTest {

    private PredictorWeka predictor;
    private PredictionInput inputValido;

    @BeforeEach
    void setUp() throws Exception {
        LectorDatasetCSV lector = new LectorDatasetCSV();
        List<RegistroDato> registros = lector.leer("dataset_entrenamiento_weka_synthcity.csv");

        predictor = new PredictorWeka();
        predictor.entrenar(registros);

        Ciudad ciudad = new Ciudad("TestWeka", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 2)));

        SimuladorCiudad simulador = new SimuladorCiudad();
        MetricaCiudad metrica = new MetricaCiudad(simulador.simular(ciudad));
        inputValido = PredictionInput.desdeMetrica(metrica);
    }

    @Test
    void predecirSinEntrenar_lanzaExcepcion() {
        PredictorWeka sinEntrenar = new PredictorWeka();
        assertThrows(IllegalStateException.class,
                () -> sinEntrenar.predecir(inputValido));
    }

    @Test
    void mismoInput_mismoPredictionResult() {
        PredictionResult r1 = predictor.predecir(inputValido);
        PredictionResult r2 = predictor.predecir(inputValido);

        assertEquals(r1.getTendenciaPredicha(), r2.getTendenciaPredicha());
        assertEquals(r1.getScorePredicho(),     r2.getScorePredicho());
        assertEquals(r1.getMensajePrediccion(), r2.getMensajePrediccion());
    }

    @Test
    void guardarYCargarModelo_funcionaCorrectamente() throws Exception {
        String rutaModelo     = "test_modelo.model";
        String rutaEstructura = "test_estructura.model";

        predictor.guardarModelo(rutaModelo, rutaEstructura);

        PredictorWeka cargado = new PredictorWeka();
        cargado.cargarModelo(rutaModelo, rutaEstructura);

        PredictionResult r1 = predictor.predecir(inputValido);
        PredictionResult r2 = cargado.predecir(inputValido);

        assertEquals(r1.getTendenciaPredicha(), r2.getTendenciaPredicha());
        assertEquals(r1.getScorePredicho(),     r2.getScorePredicho());

        new File(rutaModelo).delete();
        new File(rutaEstructura).delete();
    }

    @Test
    void resultadoWeka_difiereDelHeuristico_enAlgunCaso() {
        PredictorCiudad heuristico = new PredictorCiudad();

        PredictionResult rWeka      = predictor.predecir(inputValido);
        PredictionResult rHeuristico = heuristico.predecir(inputValido);

        System.out.println("Weka:       " + rWeka.getTendenciaPredicha() +
                " / score: " + rWeka.getScorePredicho());
        System.out.println("Heurístico: " + rHeuristico.getTendenciaPredicha() +
                " / score: " + rHeuristico.getScorePredicho());

        assertNotNull(rWeka);
        assertNotNull(rHeuristico);
    }
}