package org.synthcity.modulo_4;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PanelNotificacionExpansion extends VBox {

    private final Label labelTitulo;
    private final Label labelDimensiones;
    private final Label labelTipoEstructural;
    private final Label labelResimulacion;

    public PanelNotificacionExpansion() {
        setSpacing(6);
        setStyle(
                "-fx-background-color: #FF8C00;" +
                        "-fx-padding: 12;" +
                        "-fx-border-color: #CC6600;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;"
        );

        labelTitulo = new Label("CIUDAD EXPANDIDA AUTOMÁTICAMENTE");
        labelTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: white;");

        labelDimensiones = new Label();
        labelDimensiones.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

        labelTipoEstructural = new Label();
        labelTipoEstructural.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

        labelResimulacion = new Label("El sistema resimuló automáticamente sobre la nueva configuración.");
        labelResimulacion.setStyle("-fx-font-style: italic; -fx-font-size: 11px; -fx-text-fill: #FFF0D0;");

        getChildren().addAll(
                labelTitulo,
                labelDimensiones,
                labelTipoEstructural,
                labelResimulacion
        );

        // Oculto por defecto. setManaged(false) hace que no ocupe
        // espacio en el layout cuando está invisible.
        setVisible(false);
        setManaged(false);
    }


    public void mostrarExpansion(
            int filasAntes, int columnasAntes,
            int filasNuevas, int columnasNuevas,
            String tipoAntes, String tipoNuevo,
            boolean cambioTipo) {

        labelDimensiones.setText(
                "Dimensiones: " + filasAntes + "×" + columnasAntes +
                        "  →  " + filasNuevas + "×" + columnasNuevas
        );

        if (cambioTipo) {
            labelTipoEstructural.setText(
                    "Tipo estructural: " + tipoAntes + " → " + tipoNuevo
            );
        } else {
            labelTipoEstructural.setText("Tipo estructural: sin cambio (" + tipoAntes + ")");
        }

        setVisible(true);
        setManaged(true);
    }


    public void ocultar() {
        setVisible(false);
        setManaged(false);
    }
}