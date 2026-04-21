package org.synthcity.modulo_4;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.PredictionResult;
import org.synthcity.modulo_3.ResultadoEvaluacion;

public class ControladorGUI {

    private Ciudad ciudadActual;
    private ResultadoEvaluacion evaluacionActual;
    private PredictionResult prediccionActual;

    private final PanelCiudad panelCiudad;
    private final PanelResumenSistema panelResumen;
    private final ResultadoRepository resultadoRepository;

    public ControladorGUI(PanelCiudad panelCiudad,
                          PanelResumenSistema panelResumen,
                          ResultadoRepository resultadoRepository) {
        this.panelCiudad = panelCiudad;
        this.panelResumen = panelResumen;
        this.resultadoRepository = resultadoRepository;
    }

    public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion, PredictionResult prediccion) {
        this.ciudadActual = ciudad;
        this.evaluacionActual = evaluacion;
        this.prediccionActual = prediccion;

        if (panelCiudad != null) {
            panelCiudad.mostrarCiudad(ciudad);
        }
        if (panelResumen != null) {
            panelResumen.mostrarSistema(ciudad, evaluacion, prediccion);
        }
    }

    public void refrescarVista() {
        if (ciudadActual != null && evaluacionActual != null) {
            mostrarSistema(ciudadActual, evaluacionActual, prediccionActual);
        }
    }

    public void limpiarVista() {
        ciudadActual = null;
        evaluacionActual = null;
        prediccionActual = null;

        if (panelCiudad != null) {
            panelCiudad.limpiar();
        }
        if (panelResumen != null) {
            panelResumen.limpiar();
        }
    }

    public void guardarResultadoActual() {
        if (resultadoRepository != null
                && ciudadActual != null
                && evaluacionActual != null
                && prediccionActual != null) {
            resultadoRepository.guardarResultado(ciudadActual, evaluacionActual, prediccionActual);
        }
    }
}
