package org.synthcity.modulo_4;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class PanelRanking extends VBox {
    private final TextArea areaTextoRanking;

    public PanelRanking() {
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-border-color: lightgray; -fx-background-color: white;");

        Label titulo = new Label("--- RANKING DE CIUDADES ---");
        titulo.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");

        areaTextoRanking = new TextArea();
        areaTextoRanking.setEditable(false); // Para que el usuario no lo pueda borrar a mano
        areaTextoRanking.setWrapText(true);
        areaTextoRanking.setPrefRowCount(8);

        getChildren().addAll(titulo, areaTextoRanking);
    }

    // Este es el método que llamará tu ControladorGUI usando Streams
    public void actualizarRanking(String textoRanking) {
        if (textoRanking == null || textoRanking.isBlank()) {
            areaTextoRanking.setText("No hay evaluaciones guardadas.");
        } else {
            areaTextoRanking.setText(textoRanking);
        }
    }

    public void limpiar() {
        areaTextoRanking.clear();
    }
}