package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueIndustrial;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_1.bloques.BloqueServicios;
import org.synthcity.modulo_1.bloques.BloqueTransporte;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_3.prediccion.PredictionResult;

import static org.junit.jupiter.api.Assertions.*;

class IntegracionModulo4Test extends JavaFxTestBase {

    @Test
    void flujoCompletoCiudadSimulacionEvaluacionPrediccionVistaFunciona() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = crearCiudadCompleta();

            ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);

            EvaluadorCiudad evaluador = new EvaluadorCiudad();
            ResultadoEvaluacion evaluacion = evaluador.evaluar(simulacion);
            PredictionResult prediccion = evaluador.predecir(simulacion);

            PanelCiudad panelCiudad = new PanelCiudad();
            PanelResumenSistema panelResumen = new PanelResumenSistema();

            ControladorGUI controlador = new ControladorGUI(
                    panelCiudad,
                    panelResumen,
                    new ControladorGUITest.ResultadoRepositoryFake()
            );

            controlador.mostrarSistema(ciudad, evaluacion, prediccion);

            assertEquals(ciudad.getFilas() * ciudad.getColumnas(), panelCiudad.getChildren().size());
            assertNotNull(evaluacion.getMetricaCiudad());
            assertNotNull(prediccion);
        });
    }

    @Test
    void presentadorGeneraResumenTextualAuxiliarDelEstadoActual() {
        Ciudad ciudad = crearCiudadCompleta();

        ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);

        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        ResultadoEvaluacion evaluacion = evaluador.evaluar(simulacion);
        PredictionResult prediccion = evaluador.predecir(simulacion);

        PresentadorCiudad presentador = new PresentadorCiudad();
        SalidaTexto salida = presentador.generarResumen(ciudad, evaluacion, prediccion);

        assertNotNull(salida);
        assertTrue(salida.getContenido().contains("Ciudad:"));
        assertTrue(salida.getContenido().contains("Evaluacion:"));
        assertTrue(salida.getContenido().contains("Prediccion:"));
    }

    private Ciudad crearCiudadCompleta() {
        Ciudad ciudad = new Ciudad("Integrada", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(2, 2)));
        ciudad.addBloque(new BloqueServicios(new Posicion(3, 3)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(4, 4)));
        return ciudad;
    }
}