package org.synthcity.modulo_4;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.prediccion.PredictionResult;
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
        if (resultadoRepository == null) {
            throw new FormatoSalidaException("No hay repositorio disponible para guardar.");
        }
        if (ciudadActual == null || evaluacionActual == null || prediccionActual == null) {
            throw new FormatoSalidaException("No hay un resultado completo para guardar.");
        }

        resultadoRepository.guardarResultado(ciudadActual, evaluacionActual, prediccionActual);
    }

    public String obtenerUltimoResultadoGuardado() {
        if (resultadoRepository == null) {
            return "Repositorio no disponible.";
        }

        return resultadoRepository.obtenerUltimoResultado();
    }

    public String listarResultadosGuardados() {
        if (resultadoRepository == null) {
            return "Repositorio no disponible.";
        }

        return resultadoRepository.listarResultadosBasicos();
    }
}
