package org.synthcity.modulo_4;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.synthcity.modulo_1.Ciudad;

import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.control.TitledPane;

import javafx.scene.control.ComboBox;

public class    VentanaPrincipal {
    private Stage stage;
    private BorderPane root;
    private final PanelRanking panelRanking;
    private PanelCiudad panelCiudad;
    private PanelResumenSistema panelResumen;
    private ControladorGUI controlador;
    private final PanelEvolucionTemporal panelEvolucion;
    private final PanelNotificacionExpansion panelNotificacion;

    private Button btnRefrescar;
    private Button btnGuardar;
    private Button btnLimpiar;

    private Button btnGuardarHistorial;
    private Button btnGuardarDataset;
    private Button btnExportarCSV;
    private Button btnVerRanking;



    private ComboBox<String> selectorPredictor;

    public VentanaPrincipal(PanelCiudad panelCiudad,
                            PanelResumenSistema panelResumen,
                            PanelEvolucionTemporal panelEvolucion,
                            PanelNotificacionExpansion panelNotificacion,
                            PanelRanking panelRanking) {
        this.panelCiudad = panelCiudad;
        this.panelResumen = panelResumen;
        this.panelEvolucion = panelEvolucion;
        this.panelNotificacion = panelNotificacion;
        this.panelRanking = panelRanking;
        inicializarComponentes();
        construirLayout();
        configurarEventos();
    }

    private void inicializarComponentes() {

        btnRefrescar = new Button("Refrescar");
        btnGuardar = new Button("Guardar");
        btnLimpiar = new Button("Limpiar");

        selectorPredictor = new ComboBox<>();

        selectorPredictor.getItems().addAll(
                "Predictor Heurístico",
                "Predictor ML"
        );

        String estiloBoton = "-fx-font-size: 18px; -fx-padding: 12 24; -fx-background-radius: 8;";

        selectorPredictor.setValue("Predictor Heurístico");
        selectorPredictor.setStyle(estiloBoton);


        btnRefrescar.setStyle(estiloBoton);
        btnGuardar.setStyle(estiloBoton);
        btnLimpiar.setStyle(estiloBoton);

        btnGuardarHistorial = new Button("Guardar historial");
        btnGuardarDataset = new Button("Guardar dataset");
        btnExportarCSV = new Button("Exportar CSV");
        btnVerRanking = new Button("Ver Top 5");

        btnGuardarHistorial.setStyle(estiloBoton);
        btnGuardarDataset.setStyle(estiloBoton);
        btnExportarCSV.setStyle(estiloBoton);
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
        ScrollPane scrollCiudad = new ScrollPane(panelCiudad);
        scrollCiudad.setFitToWidth(true);
        scrollCiudad.setFitToHeight(true);
        scrollCiudad.setPannable(true);
        root.setCenter(scrollCiudad);
        //root.setCenter(panelCiudad);

        // --- BOTTOM: Separador horizontal + botones
        HBox botonera = new HBox(20);
        /*botonera.getChildren().addAll(
                btnRefrescar, btnGuardar, btnLimpiar,
                new Separator(Orientation.VERTICAL)
        );*/



        botonera.getChildren().addAll(
                btnRefrescar,
                btnGuardar,
                btnGuardarHistorial,
                btnGuardarDataset,
                btnExportarCSV,
                btnVerRanking,
                btnLimpiar
        );
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
        panelDerecho.setPrefWidth(300);
        panelDerecho.setMinWidth(300);
        panelDerecho.setMaxWidth(300);
        // Placeholder inicial
        if (panelResumen != null) {
            panelDerecho.getChildren().add(panelResumen);
        }

        if (panelEvolucion != null) {
            panelDerecho.getChildren().add(panelEvolucion);
        }

        if (panelNotificacion != null) {
            panelDerecho.getChildren().add(panelNotificacion);
        }
        if (panelRanking != null) {
            panelDerecho.getChildren().add(panelRanking);
        }

        if (panelResumen == null && panelEvolucion == null && panelNotificacion == null) {
            Label placeholder = new Label("Panel de resumen\n(próximamente)");
            placeholder.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-alignment: center;");
            placeholder.setWrapText(true);
            panelDerecho.getChildren().add(placeholder);
        }

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

        selectorPredictor.setOnAction(event -> {
            if (controlador != null) {

                boolean usarML =
                        selectorPredictor.getValue().equals("Predictor ML");

                controlador.cambiarPredictor(usarML);
            }
        });

        btnVerRanking.setOnAction(e -> {
            if (controlador != null) {
                controlador.calcularYMostrarRanking();
            }
        });


        btnGuardarHistorial.setOnAction(e -> {
            if (controlador != null) controlador.guardarHistorialActual();
        });

        btnGuardarDataset.setOnAction(e -> {
            if (controlador != null) controlador.guardarDatasetActual();
        });

        btnExportarCSV.setOnAction(e -> {
            if (controlador != null) {
                controlador.exportarDatasetCSV("dataset_synthcity.csv");
            }
        });

    }

    public void setControlador(ControladorGUI controlador) {
        this.controlador = controlador;
        if (controlador != null && panelNotificacion != null) {
            controlador.setPanelNotificacion(panelNotificacion);
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
