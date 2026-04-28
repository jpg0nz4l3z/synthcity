package org.synthcity.modulo_4;

import java.util.Locale;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.prediccion.PredictionResult;
import org.synthcity.modulo_3.ResultadoEvaluacion;

public class PanelResumenSistema extends VBox {

    private final Label nombreCiudad;
    private final Label tipoEstructural;
    private final Label numeroBloques;
    private final Label densidad;
    private final Label nivelEvaluacion;
    private final Label score;
    private final Label mensajeEvaluacion;
    private final Label tendencia;
    private final Label scorePredicho;
    private final Label mensajePrediccion;

    public PanelResumenSistema() {
        setSpacing(10);

        Label tituloCiudad = new Label("--- CIUDAD ---");
        VBox infoCiudad = new VBox(5);
        nombreCiudad = new Label("Nombre: ");
        tipoEstructural = new Label("Tipo: ");
        numeroBloques = new Label("Bloques: ");
        densidad = new Label("Densidad: ");
        infoCiudad.getChildren().addAll(nombreCiudad, tipoEstructural, numeroBloques, densidad);

        Label tituloEvaluacion = new Label("--- EVALUACION ---");
        VBox evaluacionBox = new VBox(5);
        nivelEvaluacion = new Label("Nivel: ");
        score = new Label("Score: ");
        mensajeEvaluacion = new Label("Mensaje evaluacion: ");
        mensajeEvaluacion.setWrapText(true);
        evaluacionBox.getChildren().addAll(nivelEvaluacion, score, mensajeEvaluacion);

        Label tituloPrediccion = new Label("--- PREDICCION ---");
        VBox prediccionBox = new VBox(5);
        tendencia = new Label("Tendencia: ");
        scorePredicho = new Label("Score predicho: ");
        mensajePrediccion = new Label("Mensaje prediccion: ");
        mensajePrediccion.setWrapText(true);
        prediccionBox.getChildren().addAll(tendencia, scorePredicho, mensajePrediccion);

        getChildren().addAll(tituloCiudad, infoCiudad, tituloEvaluacion, evaluacionBox, tituloPrediccion, prediccionBox);
    }

    public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion, PredictionResult prediccion) {
        if (ciudad == null || evaluacion == null || evaluacion.getMetricaCiudad() == null) {
            limpiar();
            return;
        }

        nombreCiudad.setText("Nombre: " + ciudad.getNombre());
        tipoEstructural.setText("Tipo: " + evaluacion.getMetricaCiudad().getTipoEstructural());
        numeroBloques.setText("Bloques: " + evaluacion.getMetricaCiudad().getTotalBloques());
        densidad.setText("Densidad: " + String.format(Locale.ROOT, "%.2f",
                evaluacion.getMetricaCiudad().getDensidad()));

        nivelEvaluacion.setText("Nivel: " + evaluacion.getNivelEvaluacion());
        score.setText("Score: " + String.format(Locale.ROOT, "%.2f", evaluacion.getScoreViabilidad()));
        mensajeEvaluacion.setText("Mensaje: " + evaluacion.getMensaje());

        if (prediccion != null) {
            tendencia.setText("Tendencia: " + prediccion.getTendenciaPredicha());
            scorePredicho.setText("Score predicho: " + String.format(Locale.ROOT, "%.2f",
                    prediccion.getScorePredicho()));
            mensajePrediccion.setText("Mensaje: " + prediccion.getMensajePrediccion());
        } else {
            tendencia.setText("Tendencia: N/A");
            scorePredicho.setText("Score predicho: N/A");
            mensajePrediccion.setText("Mensaje: Predicción no disponible");
        }
    }

    public void limpiar() {
        nombreCiudad.setText("Nombre: ");
        tipoEstructural.setText("Tipo: ");
        numeroBloques.setText("Bloques: ");
        densidad.setText("Densidad: ");
        nivelEvaluacion.setText("Nivel: ");
        score.setText("Score: ");
        mensajeEvaluacion.setText("Mensaje evaluacion: ");
        tendencia.setText("Tendencia: ");
        scorePredicho.setText("Score predicho: ");
        mensajePrediccion.setText("Mensaje prediccion: ");
    }
}
