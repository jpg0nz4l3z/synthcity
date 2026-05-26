package org.synthcity.modulo_4;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_3.prediccion.PredictionResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PanelResumenSistemaTest extends JavaFxTestBase {

    @Test
    void muestraNombreDensidadNivelScoreYTendenciaCorrectos() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("ResumenGUI", 10, 10);
            ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
            ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));

            EvaluadorCiudad evaluador = new EvaluadorCiudad();
            ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);
            ResultadoEvaluacion evaluacion = evaluador.evaluar(simulacion);
            PredictionResult prediccion = evaluador.predecir(simulacion);

            PanelResumenSistema panel = new PanelResumenSistema();
            panel.mostrarSistema(ciudad, evaluacion, prediccion);

            List<String> textos = obtenerTextosLabels(panel);

            assertTrue(contiene(textos, "Nombre: ResumenGUI"));
            assertTrue(contiene(textos, "Tipo: " + evaluacion.getMetricaCiudad().getTipoEstructural()));
            assertTrue(contiene(textos, "Bloques: " + evaluacion.getMetricaCiudad().getTotalBloques()));
            assertTrue(contiene(textos, "Nivel: " + evaluacion.getNivelEvaluacion()));
            assertTrue(contiene(textos, "Tendencia: " + prediccion.getTendenciaPredicha()));
        });
    }

    @Test
    void muestraPrediccionNoDisponibleSiPrediccionEsNull() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("SinPred", 10, 10);
            ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);
            ResultadoEvaluacion evaluacion = new EvaluadorCiudad().evaluar(simulacion);

            PanelResumenSistema panel = new PanelResumenSistema();
            panel.mostrarSistema(ciudad, evaluacion, null);

            List<String> textos = obtenerTextosLabels(panel);

            assertTrue(contiene(textos, "Tendencia: N/A"));
            assertTrue(contiene(textos, "Score predicho: N/A"));
            assertTrue(contiene(textos, "Predicción no disponible"));
        });
    }

    @Test
    void limpiarReseteaContenido() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("LimpiarResumen", 10, 10);
            ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);
            EvaluadorCiudad evaluador = new EvaluadorCiudad();
            ResultadoEvaluacion evaluacion = evaluador.evaluar(simulacion);
            PredictionResult prediccion = evaluador.predecir(simulacion);

            PanelResumenSistema panel = new PanelResumenSistema();
            panel.mostrarSistema(ciudad, evaluacion, prediccion);
            panel.limpiar();

            List<String> textos = obtenerTextosLabels(panel);

            assertTrue(contiene(textos, "Nombre: "));
            assertTrue(contiene(textos, "Tipo: "));
            assertTrue(contiene(textos, "Bloques: "));
            assertTrue(contiene(textos, "Densidad: "));
            assertTrue(contiene(textos, "Nivel: "));
            assertTrue(contiene(textos, "Score: "));
        });
    }

    private List<String> obtenerTextosLabels(Pane root) {
        List<String> textos = new ArrayList<>();
        recorrer(root, textos);
        return textos;
    }

    private void recorrer(Node node, List<String> textos) {
        if (node instanceof Label label) {
            textos.add(label.getText());
        }
        if (node instanceof Pane pane) {
            for (Node hijo : pane.getChildren()) {
                recorrer(hijo, textos);
            }
        }
    }

    private boolean contiene(List<String> textos, String fragmento) {
        return textos.stream().anyMatch(t -> t.contains(fragmento));
    }
}