package org.synthcity.modulo_4;

import java.util.Locale;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.PredictionResult;
import org.synthcity.modulo_3.ResultadoEvaluacion;

import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_3.AlertaEvaluacion;
import java.util.stream.Collectors;
import javafx.scene.control.Separator;

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
    private final Label ciclosEjecutados;
    private final Label motivoParada;
    private final Label tendenciaTemporal;
    private final Label alertasActivas;
    private final VBox seccionExpansion;
    private final Label expansionDimensiones;
    private final Label expansionTipoEstructural;

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
        alertasActivas = new Label("Alertas: ");
        alertasActivas.setWrapText(true);
        evaluacionBox.getChildren().add(alertasActivas);

        Label tituloPrediccion = new Label("--- PREDICCION ---");
        VBox prediccionBox = new VBox(5);
        tendencia = new Label("Tendencia: ");
        scorePredicho = new Label("Score predicho: ");
        mensajePrediccion = new Label("Mensaje prediccion: ");
        mensajePrediccion.setWrapText(true);
        prediccionBox.getChildren().addAll(tendencia, scorePredicho, mensajePrediccion);
        // sección simulación
        Label tituloSimulacion = new Label("--- SIMULACION ---");
        VBox simulacionBox = new VBox(5);
        ciclosEjecutados  = new Label("Ciclos: ");
        motivoParada      = new Label("Parada: ");
        tendenciaTemporal = new Label("Tendencia: ");
        simulacionBox.getChildren().addAll(ciclosEjecutados, motivoParada, tendenciaTemporal);

        // sección expansión (oculta por defecto)
        expansionDimensiones    = new Label();
        expansionTipoEstructural = new Label();
        Label expansionTitulo   = new Label("⚡ EXPANSION AUTOMATICA");
        expansionTitulo.setStyle("-fx-font-weight: bold; -fx-text-fill: #CC6600;");
        seccionExpansion = new VBox(4);
        seccionExpansion.setStyle(
                "-fx-background-color: #FFF3E0; -fx-border-color: #FF8C00;" +
                        "-fx-border-width: 1; -fx-padding: 8;"
        );
        seccionExpansion.getChildren().addAll(
                expansionTitulo, expansionDimensiones, expansionTipoEstructural
        );
        seccionExpansion.setVisible(false);
        seccionExpansion.setManaged(false);

        getChildren().addAll(tituloCiudad, infoCiudad, tituloEvaluacion, evaluacionBox, tituloPrediccion, prediccionBox);
    }

    public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion, PredictionResult prediccion) {
        if (ciudad == null || evaluacion == null) {
            limpiar();
            return;
        }

        nombreCiudad.setText("Nombre: " + ciudad.getNombre());
        tipoEstructural.setText("Tipo: " + ciudad.getTipoEstructural());
        numeroBloques.setText("Bloques: " + ciudad.getOcupacionActual());
        densidad.setText("Densidad: " + String.format(Locale.ROOT, "%.2f", ciudad.getDensidad()));

        nivelEvaluacion.setText("Nivel: " + evaluacion.getNivelEvaluacion());
        score.setText("Score: " + String.format(Locale.ROOT, "%.2f", evaluacion.getScoreViabilidad()));
        mensajeEvaluacion.setText("Mensaje: " + evaluacion.getMensaje());

        if (prediccion != null) {
            tendencia.setText("Tendencia: " + prediccion.getTendenciaPredicha());
            scorePredicho.setText("Score predicho: " + String.format(Locale.ROOT, "%.2f", prediccion.getScorePredicho()));
            mensajePrediccion.setText("Mensaje: " + prediccion.getMensajePrediccion());
        } else {
            tendencia.setText("Tendencia: N/A");
            scorePredicho.setText("Score predicho: N/A");
            mensajePrediccion.setText("Mensaje: Prediccion no disponible");
        }
    }

    public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion, ResultadoSimulacion historial) {
        if (ciudad == null || evaluacion == null) {
            limpiar();
            return;
        }

        nombreCiudad.setText("Nombre: " + ciudad.getNombre());
        tipoEstructural.setText("Tipo: " + ciudad.getTipoEstructural());
        numeroBloques.setText("Bloques: " + ciudad.getOcupacionActual());
        densidad.setText("Densidad: " + String.format(Locale.ROOT, "%.2f", ciudad.getDensidad()));

        nivelEvaluacion.setText("Nivel: " + evaluacion.getNivelEvaluacion());
        score.setText("Score: " + String.format(Locale.ROOT, "%.2f", evaluacion.getScoreViabilidad()));
        mensajeEvaluacion.setText("Mensaje: " + evaluacion.getMensaje());
        
        if (evaluacion.tieneAlertas()) {
            String textoAlertas = evaluacion.getAlertas().stream()
                    .map(AlertaEvaluacion::name)
                    .collect(Collectors.joining(", "));
            alertasActivas.setText("Alertas: " + textoAlertas);
        } else {
            alertasActivas.setText("Sin alertas");
        }

        // Simulación — cuando M2 entregue getCiclosEjecutados() y getMotivoParada()
        // sustituye las siguientes líneas por las llamadas reales
        if (historial != null) {
            ciclosEjecutados.setText("Ciclos: (pendiente M2)");
            motivoParada.setText("Parada: " + historial.getEstadoSimulacion());
        } else {
            ciclosEjecutados.setText("Ciclos: Sin datos");
            motivoParada.setText("Parada: Sin datos");
        }

        // Tendencia — cuando M3 entregue getTendenciaTemporal()
        // sustituye por: tendenciaTemporal.setText("Tendencia: " + traducirTendencia(evaluacion.getTendenciaTemporal().name()));
        tendenciaTemporal.setText("Tendencia: (pendiente M3)");

        // Expansión — cuando M3 entregue fueExpandida() y getResultadoExpansion()
        // sustituye por el bloque real
        seccionExpansion.setVisible(false);
        seccionExpansion.setManaged(false);
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
