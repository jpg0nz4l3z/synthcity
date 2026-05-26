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

    private final Label titulo;
    private final Label ciclos;
    private final Label estabilidad;
    private final Label contaminacion;
    private final Label motivoParada;

    public PanelEvolucionTemporal() {
        setSpacing(5);

        titulo = new Label("--- EVOLUCION TEMPORAL ---");
        ciclos = new Label("Ciclos: ");
        estabilidad = new Label("Estabilidad: ");
        contaminacion = new Label("Contaminacion: ");
        motivoParada = new Label("Motivo parada: ");

        getChildren().addAll(titulo, ciclos, estabilidad, contaminacion, motivoParada);
    }

    public void mostrarHistorial(ResultadoSimulacion historial) {
        if (historial == null) {
            limpiar();
            return;
        }

        ciclos.setText("Ciclos: " + historial.getCiclosEjecutados());
        motivoParada.setText("Motivo parada: " + historial.getMotivoParada());

        if (historial.getCiclos().isEmpty()) {
            estabilidad.setText("Estabilidad: N/A");
            contaminacion.setText("Contaminacion: N/A");
            return;
        }

        EstadoCiclo ultimo = historial.getCiclos().getLast();

        estabilidad.setText("Estabilidad final: " + ultimo.getEstabilidad());
        contaminacion.setText("Contaminacion acumulada: " + ultimo.getContaminacionAcumulada());

        StringBuilder detalle = new StringBuilder();

        for (EstadoCiclo ciclo : historial.getCiclos()) {
            detalle.append("Ciclo ")
                    .append(ciclo.getNumeroCiclo())
                    .append(" → Estabilidad: ")
                    .append(String.format("%.2f", ciclo.getEstabilidad()))
                    .append(" | Contam: ")
                    .append(ciclo.getContaminacionAcumulada())
                    .append("\n");
        }

        Label detalleLabel = new Label(detalle.toString());
        detalleLabel.setWrapText(true);

// IMPORTANTE: limpiar antes de añadir
        getChildren().removeIf(n -> n instanceof Label && n != titulo && n != ciclos && n != estabilidad && n != contaminacion && n != motivoParada);

        getChildren().add(detalleLabel);
    }

    public void limpiar() {
        ciclos.setText("Ciclos: ");
        estabilidad.setText("Estabilidad: ");
        contaminacion.setText("Contaminacion: ");
        motivoParada.setText("Motivo parada: ");
    }


}