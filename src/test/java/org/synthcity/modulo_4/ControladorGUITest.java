package org.synthcity.modulo_4;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.NivelEvaluacion;
import org.synthcity.modulo_3.PredictionResult;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_3.TendenciaPredicha;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ControladorGUITest {

    private ControladorGUI controlador;

    @BeforeAll
    static void initJFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
        }
    }

    @BeforeEach
    public void setUp() {
        PanelCiudad fakePanelCiudad = new PanelCiudad();
        PanelResumenSistema fakePanelResumen = new PanelResumenSistema();

        ResultadoRepository fakeRepo = new ResultadoRepository(null) {
            @Override
            public void guardarResultado(Ciudad c, ResultadoEvaluacion e, PredictionResult p) {
            }
        };

        controlador = new ControladorGUI(fakePanelCiudad, fakePanelResumen, fakeRepo);
    }

    @Test
    public void testMostrarSistema_FlujoNormal_CaminoBasico() {
        Ciudad ciudad = new Ciudad("Test", 5, 5);
        ResultadoEvaluacion evaluacion = crearEvaluacion("Test");
        PredictionResult prediccion = new PredictionResult(
                TendenciaPredicha.ESTABLE,
                70.0,
                0.75,
                "Tendencia estable"
        );

        assertDoesNotThrow(() -> controlador.mostrarSistema(ciudad, evaluacion, prediccion));
    }

    private ResultadoEvaluacion crearEvaluacion(String nombre) {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            conteo.put(tipo, 0);
        }
        conteo.put(TipoBloque.RESIDENCIAL, 2);
        conteo.put(TipoBloque.ENERGIA, 1);
        conteo.put(TipoBloque.SERVICIOS, 1);
        conteo.put(TipoBloque.TRANSPORTE, 1);

        ResultadoSimulacion simulacion = new ResultadoSimulacion(
                nombre,
                5,
                5,
                25,
                5,
                5,
                0,
                conteo,
                EstadoSimulacion.EJECUTADA
        );

        return new ResultadoEvaluacion(
                nombre,
                new MetricaCiudad(simulacion),
                NivelEvaluacion.FUNCIONAL,
                "Ciudad funcional",
                70.0,
                java.util.EnumSet.noneOf(org.synthcity.modulo_3.AlertaEvaluacion.class),
                "Sin riesgos relevantes detectados."
        );
    }
}
