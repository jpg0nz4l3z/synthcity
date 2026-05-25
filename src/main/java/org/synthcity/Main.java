package org.synthcity;

import javafx.application.Application;
import javafx.stage.Stage;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.*;

import org.synthcity.modulo_2.SimuladorCiudad;

import org.synthcity.modulo_3.EvaluadorCiudad;
import org.synthcity.modulo_3.GestorExpansion;
import org.synthcity.modulo_3.ConstructorDataset;
import org.synthcity.modulo_3.prediccion.*;

import org.synthcity.modulo_4.*;
import org.synthcity.modulo_4.PanelRanking;

class Launcher {
    public static void main(String[]args){
        Application.launch(Main.class, args);
    }
}

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SimuladorCiudad simulador = new SimuladorCiudad();

        // Crear instancias de ambos predictores
        Predictor predictorHeuristico = new PredictorHeuristico();
        PredictorWeka predictorML = new PredictorWeka();

        // Crear evaluador con el predictor heurístico por defecto
        EvaluadorCiudad evaluador = new EvaluadorCiudad(
                simulador::simular,
                new GestorExpansion(),
                new ConstructorDataset(),
                predictorHeuristico
        );

        DatabaseManager db = new DatabaseManager();
        db.inicializarTablaResultados();
        db.inicializarTablaHistorial();
        db.inicializarTablaDataset();

        ResultadoRepository repo = new ResultadoRepository(db);
        DatasetRepository datasetRepo = new DatasetRepository(db);

        PanelCiudad panelCiudad = new PanelCiudad();
        PanelResumenSistema panelResumen = new PanelResumenSistema();
        PanelEvolucionTemporal panelEvolucion = new PanelEvolucionTemporal();
        PanelNotificacionExpansion panelNotificacion = new PanelNotificacionExpansion();
        PanelRanking panelRanking = new PanelRanking();

        ControladorGUI controlador = new ControladorGUI(
                simulador,
                evaluador,
                panelCiudad,
                panelResumen,
                panelEvolucion,
                repo,
                datasetRepo,
                panelRanking
        );

        // Inyectar ambos predictores en el controlador
        controlador.setPredictores(predictorHeuristico, predictorML);

        VentanaPrincipal ventana = new VentanaPrincipal(
                panelCiudad,
                panelResumen,
                panelEvolucion,
                panelNotificacion,
                panelRanking
        );
        ventana.setControlador(controlador);

        ventana.mostrar();
        controlador.ejecutarSistemaCompleto(construirCiudadContaminada());
        controlador.ejecutarSistemaCompleto(construirCiudadSinEnergia());
        controlador.ejecutarSistemaCompleto(construirCiudadSaturada());
        controlador.ejecutarSistemaCompleto(construirCiudadEquilibrada());
        controlador.ejecutarSistemaCompleto(construirCiudadExpandible());

    }

    private Ciudad construirCiudadEquilibrada() {
        Ciudad c = new Ciudad("NeoMadrid", 10, 10);
        c.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        c.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        c.addBloque(new BloqueResidencial(new Posicion(1, 0)));
        c.addBloque(new BloqueResidencial(new Posicion(1, 1)));
        c.addBloque(new BloqueIndustrial(new Posicion(2, 2)));
        c.addBloque(new BloqueServicios(new Posicion(3, 3)));
        c.addBloque(new BloqueTransporte(new Posicion(4, 4)));
        return c;
    }

    private Ciudad construirCiudadSinEnergia() {
        Ciudad c = new Ciudad("DeficitEnergetico", 10, 10);
        c.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        c.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        c.addBloque(new BloqueResidencial(new Posicion(0, 2)));
        c.addBloque(new BloqueIndustrial(new Posicion(1, 0)));
        c.addBloque(new BloqueIndustrial(new Posicion(1, 1)));
        c.addBloque(new BloqueServicios(new Posicion(2, 0)));
        return c;
    }

    private Ciudad construirCiudadContaminada() {
        Ciudad c = new Ciudad("IndustrialPesada", 10, 10);
        c.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        c.addBloque(new BloqueIndustrial(new Posicion(1, 0)));
        c.addBloque(new BloqueIndustrial(new Posicion(1, 1)));
        c.addBloque(new BloqueIndustrial(new Posicion(1, 2)));
        c.addBloque(new BloqueIndustrial(new Posicion(1, 3)));
        c.addBloque(new BloqueResidencial(new Posicion(2, 0)));
        return c;
    }

    private Ciudad construirCiudadSaturada() {
        Ciudad c = new Ciudad("Saturada", 3, 3);
        c.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        c.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        c.addBloque(new BloqueResidencial(new Posicion(0, 2)));
        c.addBloque(new BloqueIndustrial(new Posicion(1, 0)));
        c.addBloque(new BloqueIndustrial(new Posicion(1, 1)));
        c.addBloque(new BloqueServicios(new Posicion(1, 2)));
        c.addBloque(new BloqueEnergia(new Posicion(2, 0)));
        c.addBloque(new BloqueTransporte(new Posicion(2, 1)));
        return c;
    }

    private Ciudad construirCiudadExpandible() {
        Ciudad c = new Ciudad("MegaDensa", 5, 5);

        for (int i = 0; i < 24; i++) { // 24 de 25 → 0.96 densidad
            int fila = i / 5;
            int col = i % 5;

            if (i % 3 == 0) {
                c.addBloque(new BloqueResidencial(new Posicion(fila, col)));
            } else if (i % 3 == 1) {
                c.addBloque(new BloqueIndustrial(new Posicion(fila, col)));
            } else {
                c.addBloque(new BloqueServicios(new Posicion(fila, col)));
            }
        }

        return c;
    }

    private Ciudad construirCiudadVacia() {
        return new Ciudad("CiudadVacia", 10, 10);
    }
}
