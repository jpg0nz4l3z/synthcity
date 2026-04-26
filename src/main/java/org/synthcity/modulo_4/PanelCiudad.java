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
        setGridLinesVisible(true);
        setAlignment(Pos.CENTER);
        setHgap(1);
        setVgap(1);
        setMinSize(500, 500);  // ← IMPORTANTE: setMinSize, no setPrefSize
        setStyle("-fx-background-color: #FAFAFA;");
    }

    public void mostrarCiudad(Ciudad ciudad) {
        this.ciudadActual = ciudad;
        refrescar();
    }

    public void limpiar() {
        getChildren().clear();
    }

    public void refrescar() {
        System.out.println("[DEBUG] refrescar() llamado");

        if (ciudadActual == null) {
            System.out.println("[DEBUG] ciudadActual es NULL, saliendo");
            return;
        }

        limpiar();

        int filas = ciudadActual.getFilas();
        int columnas = ciudadActual.getColumnas();

        System.out.println("[DEBUG] Ciudad: " + ciudadActual.getNombre()
                + " | Dimensiones: " + filas + "x" + columnas);
        System.out.println("[DEBUG] Bloques en ciudad: " + ciudadActual.contarBloques());
        System.out.println("[DEBUG] Panel tamaño actual: "
                + getWidth() + "x" + getHeight());
        System.out.println("[DEBUG] Panel tamaño pref: "
                + getPrefWidth() + "x" + getPrefHeight());
        System.out.println("[DEBUG] Panel tamaño min: "
                + getMinWidth() + "x" + getMinHeight());
        System.out.println("[DEBUG] Panel padre: "
                + (getParent() == null ? "NULL" : getParent().getClass().getSimpleName()));
        System.out.println("[DEBUG] Panel en Scene: "
                + (getScene() == null ? "NO" : "SI"));
        double tamCelda = 50;
        setPrefSize(columnas * tamCelda, filas * tamCelda);
        int contador = 0;
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                Bloque bloque = ciudadActual.getBloque(fila, col);
                StackPane celda = (bloque == null) ? crearCeldaVacia() : crearCeldaBloque(bloque);
                add(celda, col, fila);
                contador++;
            }
        }

        System.out.println("[DEBUG] Celdas añadidas: " + contador);
        System.out.println("[DEBUG] Hijos del GridPane tras añadir: " + getChildren().size());
        System.out.println("[DEBUG] Tamaño final del panel: "
                + getWidth() + "x" + getHeight());
        System.out.println("[DEBUG] ---");
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
