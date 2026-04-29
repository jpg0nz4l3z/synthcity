package org.synthcity.modulo_4;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PanelNotificacionExpansion extends VBox {

    private final Label mensaje;

    public PanelNotificacionExpansion() {
        setSpacing(5);
        mensaje = new Label();
        mensaje.setWrapText(true);
        getChildren().add(mensaje);
        ocultar();
    }

    public void mostrarExpansion(
            int filasAntes, int columnasAntes,
            int filasDespues,
            int columnasDespues,
            String tipoAntes,
            String tipoDespues,
            boolean cambioTipo) {

        setVisible(true);
        setManaged(true);

        String texto = "Expansión ejecutada: "
                + filasAntes + "x" + columnasAntes
                + " -> "
                + filasDespues + "x" + columnasDespues;

        if (cambioTipo) {
            texto += ". Cambio estructural: " + tipoAntes + " -> " + tipoDespues;
        }

        mensaje.setText(texto);
    }

    public void ocultar() {
        mensaje.setText("");
        setVisible(false);
        setManaged(false);
    }
}