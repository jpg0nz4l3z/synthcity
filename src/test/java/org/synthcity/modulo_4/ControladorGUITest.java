package org.synthcity.modulo_4;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_3.ResultadoEvaluacion;

import static org.junit.jupiter.api.Assertions.*;

/*public class ControladorGUITest {

    private ControladorGUI controlador;
    private PanelCiudad fakePanelCiudad;
    private PanelResumenSistema fakePanelResumen;
    private ResultadoRepository fakeRepo;

    @BeforeAll
    static void initJFX() {
        try { Platform.startup(() -> {}); } catch (IllegalStateException e) {}
    }

    @BeforeEach
    public void setUp() {
        fakePanelCiudad = new PanelCiudad();
        fakePanelResumen = new PanelResumenSistema();

        fakeRepo = new ResultadoRepository(null) {
            @Override
            public void guardarResultado(CiudadTest c, ResultadoEvaluacion e, PredictionResult p) {
                // Simulación vacía del guardado en BD para evitar errores de conexión
            }
        };

        controlador = new ControladorGUI(fakePanelCiudad, fakePanelResumen, fakeRepo);
    }

    @Test
    public void testMostrarSistema_FlujoNormal_CaminoBasico() {
        CiudadTest miCiudad = new CiudadTest("Test", 5, 5);
        ResultadoEvaluacion evaluacion = new ResultadoEvaluacion();
        PredictionResult prediccion = new PredictionResult();

        Platform.runLater(() -> {
            assertDoesNotThrow(() -> {
                controlador.mostrarSistema(miCiudad, evaluacion, prediccion);
            }, "El controlador debe distribuir los datos sin lanzar excepciones");
        });
    }
}*/
