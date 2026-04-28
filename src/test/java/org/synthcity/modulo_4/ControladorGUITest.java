package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_3.prediccion.PredictionResult;

import static org.junit.jupiter.api.Assertions.*;

class ControladorGUITest extends JavaFxTestBase {

    @Test
    void mostrarSistemaActualizaPanelCiudadYResumen() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("Controlador", 2, 2);
            ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));

            EvaluadorCiudad evaluador = new EvaluadorCiudad();
            ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);
            ResultadoEvaluacion evaluacion = evaluador.evaluar(simulacion);
            PredictionResult prediccion = evaluador.predecir(simulacion);

            PanelCiudad panelCiudad = new PanelCiudad();
            PanelResumenSistema panelResumen = new PanelResumenSistema();
            ResultadoRepositoryFake repo = new ResultadoRepositoryFake();

            ControladorGUI controlador = new ControladorGUI(panelCiudad, panelResumen, repo);
            controlador.mostrarSistema(ciudad, evaluacion, prediccion);

            assertEquals(4, panelCiudad.getChildren().size());
        });
    }

    @Test
    void limpiarVistaLimpiaPanelesYReferencias() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("Limpiar", 2, 2);
            EvaluadorCiudad evaluador = new EvaluadorCiudad();
            ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);
            ResultadoEvaluacion evaluacion = evaluador.evaluar(simulacion);
            PredictionResult prediccion = evaluador.predecir(simulacion);

            PanelCiudad panelCiudad = new PanelCiudad();
            PanelResumenSistema panelResumen = new PanelResumenSistema();
            ControladorGUI controlador = new ControladorGUI(panelCiudad, panelResumen, new ResultadoRepositoryFake());

            controlador.mostrarSistema(ciudad, evaluacion, prediccion);
            controlador.limpiarVista();

            assertTrue(panelCiudad.getChildren().isEmpty());
        });
    }

    @Test
    void guardarResultadoActual_delegaEnRepositorio() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("Guardar", 2, 2);
            ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));

            EvaluadorCiudad evaluador = new EvaluadorCiudad();
            ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);
            ResultadoEvaluacion evaluacion = evaluador.evaluar(simulacion);
            PredictionResult prediccion = evaluador.predecir(simulacion);

            ResultadoRepositoryFake repo = new ResultadoRepositoryFake();
            ControladorGUI controlador = new ControladorGUI(new PanelCiudad(), new PanelResumenSistema(), repo);

            controlador.mostrarSistema(ciudad, evaluacion, prediccion);
            controlador.guardarResultadoActual();

            assertTrue(repo.guardado);
            assertEquals("Guardar", repo.nombreCiudadGuardada);
        });
    }

    @Test
    void guardarSinEstadoCompleto_lanzaFormatoSalidaException() {
        ControladorGUI controlador = new ControladorGUI(null, null, new ResultadoRepositoryFake());

        assertThrows(FormatoSalidaException.class, controlador::guardarResultadoActual);
    }

    @Test
    void obtenerUltimoResultadoGuardado_delegaEnRepositorio() {
        ControladorGUI controlador = new ControladorGUI(null, null, new ResultadoRepositoryFake());

        assertTrue(controlador.obtenerUltimoResultadoGuardado().contains("ULTIMO"));
    }

    @Test
    void listarResultadosGuardados_delegaEnRepositorio() {
        ControladorGUI controlador = new ControladorGUI(null, null, new ResultadoRepositoryFake());

        assertTrue(controlador.listarResultadosGuardados().contains("HISTORIAL"));
    }

    static class ResultadoRepositoryFake extends ResultadoRepository {
        boolean guardado = false;
        String nombreCiudadGuardada;

        ResultadoRepositoryFake() {
            super(null);
        }

        @Override
        public void guardarResultado(Ciudad ciudad, ResultadoEvaluacion evaluacion, PredictionResult prediccion) {
            guardado = true;
            nombreCiudadGuardada = ciudad.getNombre();
        }

        @Override
        public String obtenerUltimoResultado() {
            return "ULTIMO RESULTADO FAKE";
        }

        @Override
        public String listarResultadosBasicos() {
            return "HISTORIAL FAKE";
        }
    }
}