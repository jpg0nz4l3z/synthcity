package org.synthcity;

import javafx.application.Application;
import javafx.stage.Stage;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.*;

import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_2.ResultadoSimulacion;

import org.synthcity.modulo_3.EvaluadorCiudad;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_3.prediccion.*;

import org.synthcity.modulo_4.ControladorGUI;
import org.synthcity.modulo_4.DatabaseManager;
import org.synthcity.modulo_4.PanelCiudad;
import org.synthcity.modulo_4.PanelResumenSistema;
import org.synthcity.modulo_4.ResultadoRepository;
import org.synthcity.modulo_4.VentanaPrincipal;

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
        Ciudad ciudad = construirCiudadDemo();

        ResultadoSimulacion resultado = new SimuladorCiudad().simular(ciudad);

        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        ResultadoEvaluacion evaluacion = evaluador.evaluar(resultado);
        PredictionResult prediccion = evaluador.predecir(resultado);

        DatabaseManager db = new DatabaseManager();
        db.inicializarTablaResultados();

        ResultadoRepository repo = new ResultadoRepository(db);

        PanelCiudad panelCiudad = new PanelCiudad();
        PanelResumenSistema panelResumen = new PanelResumenSistema();

        ControladorGUI controlador = new ControladorGUI(panelCiudad, panelResumen, repo);

        VentanaPrincipal ventana = new VentanaPrincipal(panelCiudad, panelResumen);
        ventana.setControlador(controlador);

        ventana.mostrar();
        controlador.mostrarSistema(ciudad, evaluacion, prediccion);
    }

    private Ciudad construirCiudadDemo() {
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
}
