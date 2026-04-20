package org.synthcity.modulo_4;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.PredictionResult;
import org.synthcity.modulo_3.ResultadoEvaluacion;

public class PanelResumenSistema extends VBox{

    private Label nombreCiudad;
    private Label tipoEstructural;
    private Label numeroBloques;
    private Label densidad;
    private Label nivelEvaluacion;
    private Label score;
    private Label mensajeEvaluacion;
    private Label tendencia;
    private Label scorePredicho;
    private Label mensajePrediccion;


    public PanelResumenSistema() {

            this.setSpacing(10);

            Label tituloCiudad = new Label("--- CIUDAD ---");

            VBox infoCiudad = new VBox();
            infoCiudad.setSpacing(5);

            nombreCiudad = new Label("Nombre: ");
            tipoEstructural = new Label("Tipo: ");
            numeroBloques = new Label("Bloques: ");
            densidad = new Label("Densidad: ");

            infoCiudad.getChildren().addAll(
                    nombreCiudad,
                    tipoEstructural,
                    numeroBloques,
                    densidad
            );

            this.getChildren().addAll(tituloCiudad, infoCiudad);


            Label tituloEvaluacion = new Label("--- EVALUACIÓN ---");

            VBox evaluacionBox = new VBox();
            evaluacionBox.setSpacing(5);

            nivelEvaluacion = new Label("Nivel: ");
            score = new Label("Score: ");
            mensajeEvaluacion = new Label("Mensaje evaluación: ");

            evaluacionBox.getChildren().addAll(
                    nivelEvaluacion,
                    score,
                    mensajeEvaluacion
            );

            this.getChildren().addAll(tituloEvaluacion, evaluacionBox);


            Label tituloPrediccion = new Label("--- PREDICCIÓN ---");

            VBox prediccionBox = new VBox();
            prediccionBox.setSpacing(5);

            tendencia = new Label("Tendencia: ");
            scorePredicho = new Label("Score predicho: ");
            mensajePrediccion = new Label("Mensaje predicción: ");

            prediccionBox.getChildren().addAll(
                    tendencia,
                    scorePredicho,
                    mensajePrediccion
            );

            this.getChildren().addAll(tituloPrediccion, prediccionBox);
        }

   /* public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion, PredictionResult prediccion){

        nombreCiudad.setText("Nombre: " + ciudad.getNombre());
        tipoEstructural.setText("Tipo: " + ciudad.getTipo());
        numeroBloques.setText("Bloques: " + ciudad.getBloques());
        densidad.setText("Densidad: " + ciudad.getDensidad());

        nivelEvaluacion.setText("Nivel: " + evaluacion.getNivel());
        score.setText("Score: " + evaluacion.getScore());
        mensajeEvaluacion.setText("Mensaje: " + evaluacion.getMensaje());

        if (prediccion != null) {
            tendencia.setText("Tendencia: " + prediccion.getTendencia());
            scorePredicho.setText("Score predicho: " + prediccion.getScore());
            mensajePrediccion.setText("Mensaje: " + prediccion.getMensaje());
        } else {
            tendencia.setText("Tendencia: N/A");
            scorePredicho.setText("Score predicho: N/A");
            mensajePrediccion.setText("Mensaje: Predicción no disponible ");
        }
    }

    public void limpiar(){
        nombreCiudad.setText("Nombre: ");
        tipoEstructural.setText("Tipo: ");
        numeroBloques.setText("Bloques: ");
        densidad.setText("Densidad: ");

        nivelEvaluacion.setText("Nivel: ");
        score.setText("Score: ");
        mensajeEvaluacion.setText("Mensaje evaluación: ");

        tendencia.setText("Tendencia: ");
        scorePredicho.setText("Score predicho: ");
        mensajePrediccion.setText("Mensaje predicción: ");
    }
*/
}