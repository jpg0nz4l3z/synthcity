package org.synthcity.modulo_4;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.synthcity.modulo_1.Ciudad;

public class VentanaPrincipal {
    private Stage stage;
    private BorderPane root;
    private PanelCiudad panelCiudad;
    private PanelResumenSistema panelResumen;
    private ControladorGUI controlador;

    private Button btnRefrescar;
    private Button btnGuardar;
    private Button btnLimpiar;

    public VentanaPrincipal() {
        inicializarComponentes();
        construirLayout();
        configurarEventos();
    }

    private void inicializarComponentes() {
        panelCiudad = new PanelCiudad();
        panelResumen = null;

        btnRefrescar = new Button("Refrescar");
        btnGuardar = new Button("Guardar");
        btnLimpiar = new Button("Limpiar");

        String estiloBoton = "-fx-font-size: 18px; -fx-padding: 12 24; -fx-background-radius: 8;";
        btnRefrescar.setStyle(estiloBoton);
        btnGuardar.setStyle(estiloBoton);
        btnLimpiar.setStyle(estiloBoton);
    }

    private void construirLayout() {
        root = new BorderPane();

        // --- Sombra para el panel central (grid)
        DropShadow gridShadow = new DropShadow();
        gridShadow.setRadius(5);
        gridShadow.setOffsetX(2);
        gridShadow.setOffsetY(2);
        gridShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        panelCiudad.setEffect(gridShadow);
        root.setCenter(panelCiudad);

        // --- BOTTOM: Separador horizontal + botones
        HBox botonera = new HBox(20);
        botonera.getChildren().addAll(btnRefrescar, btnGuardar, btnLimpiar);
        botonera.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f5f5f5;");
        botonera.setPrefHeight(100);

        Separator horizontalSep = new Separator();
        horizontalSep.setOrientation(Orientation.HORIZONTAL);

        VBox bottomContainer = new VBox();
        bottomContainer.getChildren().addAll(horizontalSep, botonera);
        bottomContainer.setStyle("-fx-padding: 0;");
        root.setBottom(bottomContainer);

        // --- RIGHT: Contenedor con separador vertical + panel resumen
        VBox panelDerecho = new VBox();
        panelDerecho.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-background-color: white;");

        // Placeholder inicial
        Label placeholder = new Label("Panel de resumen\n(próximamente)");
        placeholder.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-alignment: center;");
        placeholder.setWrapText(true);
        panelDerecho.getChildren().add(placeholder);

        // Sombra para el panel derecho
        DropShadow resumenShadow = new DropShadow();
        resumenShadow.setRadius(10);
        resumenShadow.setOffsetX(-2);
        resumenShadow.setColor(Color.rgb(0, 0, 0, 0.4));
        panelDerecho.setEffect(resumenShadow);

        Separator verticalSep = new Separator();
        verticalSep.setOrientation(Orientation.VERTICAL);
        verticalSep.setStyle("-fx-padding: 0 5 0 0;");

        HBox rightContainer = new HBox(verticalSep, panelDerecho);
        root.setRight(rightContainer);
    }

    private void configurarEventos() {
        btnRefrescar.setOnAction(e -> {
            if (controlador != null) controlador.refrescarVista();
        });
        btnGuardar.setOnAction(e -> {
            if (controlador != null) controlador.guardarResultadoActual();
        });
        btnLimpiar.setOnAction(e -> {
            if (controlador != null) controlador.limpiarVista();
        });
    }

    public void setControlador(ControladorGUI controlador) {
        this.controlador = controlador;
    }

    public void setPanelResumen(PanelResumenSistema panelResumen) {
        this.panelResumen = panelResumen;
        // Accedemos al HBox derecho y dentro a su segundo hijo (el VBox panelDerecho)
        HBox rightContainer = (HBox) root.getRight();
        VBox panelDerecho = (VBox) rightContainer.getChildren().get(1);
        panelDerecho.getChildren().clear();
        if (panelResumen != null) {
            panelDerecho.getChildren().add(panelResumen);
        } else {
            // Si se asigna null, volvemos a poner el placeholder
            Label placeholder = new Label("Panel de resumen");
            placeholder.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-alignment: center;");
            placeholder.setWrapText(true);
            panelDerecho.getChildren().add(placeholder);
        }
    }

    public void actualizarCiudad(Ciudad ciudad) {
        panelCiudad.mostrarCiudad(ciudad);
    }

    public void mostrar() {
        if (stage == null) {
            stage = new Stage();
            Scene scene = new Scene(root, 900, 700);
            stage.setScene(scene);
            stage.setTitle("SynthCity - Visor de Ciudad");
        }
        stage.show();
    }
}
