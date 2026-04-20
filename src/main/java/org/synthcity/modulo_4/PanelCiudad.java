package org.synthcity.modulo_4;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.synthcity.modulo_1.bloques.Bloque;
import org.synthcity.modulo_1.Ciudad;

public class PanelCiudad extends GridPane {
    private Ciudad ciudadActual;

    public PanelCiudad() {
        setGridLinesVisible(true);   // útil para depurar
        setAlignment(Pos.CENTER);
    }

    public void mostrarCiudad(Ciudad ciudad) {
        this.ciudadActual = ciudad;
        refrescar();
    }

    public void limpiar() {
        getChildren().clear();
    }

    public void refrescar() {
        if (ciudadActual == null) return;
        limpiar();

        int filas = ciudadActual.getFilas();
        int columnas = ciudadActual.getColumnas();

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                Bloque bloque = ciudadActual.getBloque(fila, col);
                StackPane celda = (bloque == null) ? crearCeldaVacia() : crearCeldaBloque(bloque);
                add(celda, col, fila);
            }
        }
    }

    private StackPane crearCeldaVacia() {
        StackPane celda = new StackPane();
        Rectangle rect = new Rectangle(50, 50);
        rect.setFill(Color.LIGHTGREEN);
        rect.setStroke(Color.BLACK);
        celda.getChildren().add(rect);
        return celda;
    }

    private StackPane crearCeldaBloque(Bloque bloque) {
        StackPane celda = new StackPane();
        Rectangle rect = new Rectangle(50, 50);
        rect.setStroke(Color.BLACK);

        aplicarEstiloSegunTipo(bloque, rect);

        Text texto = new Text(obtenerInicial(bloque));
        texto.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        celda.getChildren().addAll(rect, texto);

        aplicarIndicadorEstado(bloque, celda);

        return celda;
    }

    private void aplicarEstiloSegunTipo(Bloque bloque, Rectangle rect) {
        Color color = switch (bloque.getTipo()) {
            case RESIDENCIAL -> Color.LIGHTGRAY;
            case ENERGIA     -> Color.YELLOW;
            case INDUSTRIAL  -> Color.GRAY;
            case SERVICIOS   -> Color.LIGHTBLUE;
            case TRANSPORTE  -> Color.ORANGE;
            default          -> Color.WHITE;
        };
        rect.setFill(color);
    }

    private void aplicarIndicadorEstado(Bloque bloque, StackPane celda) {
        if (!bloque.estaActivo()) {
            celda.setOpacity(0.6);
        }
    }

    private String obtenerInicial(Bloque bloque) {
        return switch (bloque.getTipo()) {
            case RESIDENCIAL -> "R";
            case ENERGIA     -> "E";
            case INDUSTRIAL  -> "I";
            case SERVICIOS   -> "S";
            case TRANSPORTE  -> "T";
            default          -> "?";
        };
    }
}