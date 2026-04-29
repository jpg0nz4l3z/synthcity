package org.synthcity.modulo_4;

import javafx.application.Application;
import javafx.stage.Stage;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.*;

import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;

import org.synthcity.modulo_4.ControladorGUI;
import org.synthcity.modulo_4.DatabaseManager;
import org.synthcity.modulo_4.DatasetRepository;
import org.synthcity.modulo_4.PanelCiudad;
import org.synthcity.modulo_4.PanelEvolucionTemporal;
import org.synthcity.modulo_4.PanelResumenSistema;
import org.synthcity.modulo_4.ResultadoRepository;
import org.synthcity.modulo_4.VentanaPrincipal;

class Launcher {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // 1. Crear ciudad inicial
        Ciudad ciudad = construirCiudadDemo();

        // 2. Inicializar base de datos
        DatabaseManager db = new DatabaseManager();
        db.inicializarTablaResultados();
        db.inicializarTablaDataset();
        ResultadoRepository resultadoRepository = new ResultadoRepository(db);
        DatasetRepository datasetRepository     = new DatasetRepository(db);

        // 3. Crear servicios
        SimuladorCiudad simulador = new SimuladorCiudad();
        EvaluadorCiudad evaluador = new EvaluadorCiudad();

        // 4. Crear paneles
        PanelCiudad panelCiudad              = new PanelCiudad();
        PanelResumenSistema panelResumen      = new PanelResumenSistema();
        PanelEvolucionTemporal panelEvolucion = new PanelEvolucionTemporal();

        // 5. Crear controlador
        ControladorGUI controlador = new ControladorGUI(
                simulador, evaluador,
                panelCiudad, panelResumen,
                resultadoRepository, datasetRepository, panelEvolucion
        );

        // 6. Crear ventana
        VentanaPrincipal ventana = new VentanaPrincipal(
                panelCiudad, panelResumen, panelEvolucion
        );
        ventana.setControlador(controlador);

        // 7. Mostrar interfaz
        ventana.mostrar();

        // 8. Ejecutar flujo completo
        controlador.ejecutarSistemaCompleto(ciudad);
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
