package org.synthcity.modulo_4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import org.synthcity.modulo_2.EstadoCiclo;
import org.synthcity.modulo_2.MotivoParadaSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.util.List;
import java.util.stream.Collectors;


public class PanelEvolucionTemporal extends VBox {

    private final Label labelTitulo;
    private final Label labelNumeroCiclos;
    private final Label labelMotivoParada;
    private final Label labelTendencia;
    private final Label labelEstabilidadMedia;
    private final Label labelDeficitEnergetico;
    private final TableView<FilaCiclo> tabla;

    public PanelEvolucionTemporal() {
        setSpacing(8);
        setPadding(new Insets(10));
        setStyle("-fx-border-color: #555555; -fx-border-width: 1;"
                + " -fx-background-color: #f9f9f9;");

        labelTitulo = new Label("EVOLUCIÓN TEMPORAL");
        labelTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");

        labelNumeroCiclos     = new Label("Ciclos ejecutados: —");
        labelMotivoParada     = new Label("Motivo de parada: —");
        labelTendencia        = new Label("Tendencia general: —");
        labelEstabilidadMedia = new Label("Estabilidad media: —");
        labelDeficitEnergetico = new Label("Ciclos con déficit energético: —");

        tabla = construirTabla();

        ScrollPane scroll = new ScrollPane(tabla);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(260);

        getChildren().addAll(
                labelTitulo,
                labelNumeroCiclos,
                labelMotivoParada,
                labelTendencia,
                labelEstabilidadMedia,
                labelDeficitEnergetico,
                scroll
        );
    }


    public void mostrarHistorial(ResultadoSimulacion historial) {
        limpiar();

        if (historial == null) {
            labelMotivoParada.setText("Motivo de parada: Sin datos");
            return;
        }

        PanelEvolucionTemporal
        if (historial.ciudadEstaVacia() || !historial.hayBloquesActivos()) {
            labelNumeroCiclos.setText("Ciclos ejecutados: 0");
            labelMotivoParada.setText("Motivo de parada: "
                    + formatearMotivo(historial.getMotivoParada()));
            labelTendencia.setText("Tendencia general: Sin actividad");
            labelEstabilidadMedia.setText("Estabilidad media: N/A");
            labelDeficitEnergetico.setText("Ciclos con déficit energético: N/A");
            return;
        }

        List<EstadoCiclo> ciclos = historial.getCiclos();


        ObservableList<FilaCiclo> filas = ciclos.stream()
                .map(FilaCiclo::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tabla.setItems(filas);


        long ciclosConDeficit = ciclos.stream()
                .filter(c -> c.getEquilibrioEnergetico() < 0)
                .count();


        String tendencia = calcularTendencia(ciclos);



        labelNumeroCiclos.setText("Ciclos ejecutados: "
                + historial.getCiclosEjecutados());
        labelMotivoParada.setText("Motivo de parada: "
                + formatearMotivo(historial.getMotivoParada()));
        labelTendencia.setText("Tendencia general: " + tendencia);
        labelEstabilidadMedia.setText("Estabilidad media: "
                + String.format("%.3f", historial.getEstabilidadMedia()));
        labelDeficitEnergetico.setText("Ciclos con déficit energético: "
                + ciclosConDeficit + " de " + ciclos.size());
    }


    public void limpiar() {
        tabla.getItems().clear();
        labelNumeroCiclos.setText("Ciclos ejecutados: —");
        labelMotivoParada.setText("Motivo de parada: —");
        labelTendencia.setText("Tendencia general: —");
        labelEstabilidadMedia.setText("Estabilidad media: —");
        labelDeficitEnergetico.setText("Ciclos con déficit energético: —");
    }



    private String calcularTendencia(List<EstadoCiclo> ciclos) {
        if (ciclos.size() < 2) return "INDETERMINADA (un solo ciclo)";


        double estabilidadInicio = ciclos.stream()
                .mapToDouble(EstadoCiclo::getEstabilidad)
                .findFirst()
                .orElse(0.0);


        double estabilidadFin = ciclos.stream()
                .mapToDouble(EstadoCiclo::getEstabilidad)
                .reduce((a, b) -> b)
                .orElse(0.0);

        double diferencia = estabilidadFin - estabilidadInicio;

        if (diferencia > 0.05)       return "MEJORANDO ↑  (+" + String.format("%.3f", diferencia) + ")";
        else if (diferencia < -0.05) return "DETERIORANDO ↓  (" + String.format("%.3f", diferencia) + ")";
        else                          return "ESTABLE →  (Δ" + String.format("%.3f", diferencia) + ")";
    }


    private String formatearMotivo(MotivoParadaSimulacion motivo) {
        if (motivo == null) return "Desconocido";
        return switch (motivo) {
            case CICLOS_COMPLETADOS  -> "Ciclos completados";
            case COLAPSO_ENERGETICO  -> "Colapso energético";
            case SATURACION_CRITICA  -> "Saturación crítica";
            case CIUDAD_VACIA        -> "Ciudad vacía";
            case SIN_BLOQUES_ACTIVOS -> "Sin bloques activos";
        };
    }


    private TableView<FilaCiclo> construirTabla() {
        TableView<FilaCiclo> tv = new TableView<>();
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setPlaceholder(new Label("Sin datos de simulación"));


        TableColumn<FilaCiclo, Integer> colCiclo = new TableColumn<>("Ciclo");
        colCiclo.setCellValueFactory(d -> d.getValue().numeroCicloProperty().asObject());
        colCiclo.setMinWidth(45);

        TableColumn<FilaCiclo, Integer> colEnergia = new TableColumn<>("Energía prod.");
        colEnergia.setCellValueFactory(d -> d.getValue().energiaProducidaProperty().asObject());

        TableColumn<FilaCiclo, Integer> colConsumo = new TableColumn<>("Consumo");
        colConsumo.setCellValueFactory(d -> d.getValue().consumoEnergeticoProperty().asObject());

        TableColumn<FilaCiclo, String> colBalance = new TableColumn<>("Balance ⚡");
        colBalance.setCellValueFactory(d -> d.getValue().balanceProperty());

        TableColumn<FilaCiclo, Integer> colContaminacion = new TableColumn<>("Contam. acum.");
        colContaminacion.setCellValueFactory(d -> d.getValue().contaminacionAcumuladaProperty().asObject());

        TableColumn<FilaCiclo, Double> colEstabilidad = new TableColumn<>("Estabilidad");
        colEstabilidad.setCellValueFactory(d -> d.getValue().estabilidadProperty().asObject());

        TableColumn<FilaCiclo, Double> colBienestar = new TableColumn<>("Bienestar");
        colBienestar.setCellValueFactory(d -> d.getValue().bienestarProperty().asObject());

        TableColumn<FilaCiclo, Double> colCobertura = new TableColumn<>("Cobertura");
        colCobertura.setCellValueFactory(d -> d.getValue().coberturaProperty().asObject());

        TableColumn<FilaCiclo, Double> colEficiencia = new TableColumn<>("Efic. transp.");
        colEficiencia.setCellValueFactory(d -> d.getValue().eficienciaTransporteProperty().asObject());

        tv.getColumns().addAll(
                colCiclo, colEnergia, colConsumo, colBalance,
                colContaminacion, colEstabilidad, colBienestar,
                colCobertura, colEficiencia
        );

        return tv;
    }



    public static class FilaCiclo {

        private final SimpleIntegerProperty numeroCiclo;
        private final SimpleIntegerProperty energiaProducida;
        private final SimpleIntegerProperty consumoEnergetico;
        private final SimpleStringProperty  balance;
        private final SimpleIntegerProperty contaminacionAcumulada;
        private final SimpleDoubleProperty  estabilidad;
        private final SimpleDoubleProperty  bienestar;
        private final SimpleDoubleProperty  cobertura;
        private final SimpleDoubleProperty  eficienciaTransporte;

        public FilaCiclo(EstadoCiclo ciclo) {
            this.numeroCiclo           = new SimpleIntegerProperty(ciclo.getNumeroCiclo());
            this.energiaProducida      = new SimpleIntegerProperty(ciclo.getEnergiaProducida());
            this.consumoEnergetico     = new SimpleIntegerProperty(ciclo.getConsumoEnergetico());
            this.contaminacionAcumulada = new SimpleIntegerProperty(ciclo.getContaminacionAcumulada());
            this.estabilidad           = new SimpleDoubleProperty(redondear(ciclo.getEstabilidad()));
            this.bienestar             = new SimpleDoubleProperty(redondear(ciclo.getBienestar()));
            this.cobertura             = new SimpleDoubleProperty(redondear(ciclo.getCoberturaServiciosPonderada()));
            this.eficienciaTransporte  = new SimpleDoubleProperty(redondear(ciclo.getEficienciaTransporte()));


            int bal = ciclo.getEquilibrioEnergetico();
            this.balance = new SimpleStringProperty(bal >= 0 ? "✓ +" + bal : "✗ " + bal);
        }

        private double redondear(double v) {
            return Math.round(v * 1000.0) / 1000.0;
        }

        public SimpleIntegerProperty numeroCicloProperty()            { return numeroCiclo; }
        public SimpleIntegerProperty energiaProducidaProperty()       { return energiaProducida; }
        public SimpleIntegerProperty consumoEnergeticoProperty()      { return consumoEnergetico; }
        public SimpleStringProperty  balanceProperty()                { return balance; }
        public SimpleIntegerProperty contaminacionAcumuladaProperty() { return contaminacionAcumulada; }
        public SimpleDoubleProperty  estabilidadProperty()            { return estabilidad; }
        public SimpleDoubleProperty  bienestarProperty()              { return bienestar; }
        public SimpleDoubleProperty  coberturaProperty()              { return cobertura; }
        public SimpleDoubleProperty  eficienciaTransporteProperty()   { return eficienciaTransporte; }
    }
}