package org.synthcity.modulo_4;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.PredictionResult;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_3.Predictor;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class ControladorGUI {

    private Ciudad ciudadActual;
    private ResultadoEvaluacion evaluacionActual;
    private PredictionResult prediccionActual;
    private List<ResultadoEvaluacion> rankingActual;
    private Predictor predictorActivo;

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

    public void exportarInformeActual(String rutaArchivo) {
        // Validación obligatoria: No exportar si no hay una evaluación en curso [cite: 2340]
        if (evaluacionActual == null) {
            mostrarMensajeError("No se puede exportar: primero debe ejecutarse una simulación y evaluación válida.");
            return;
        }

        ExportadorInforme exportador = new ExportadorInforme();

        try {
            // Llamada al método de exportación usando las variables reales del controlador
            // Se usa un operador ternario para el predictor en caso de que Persona 1 aún no lo haya inicializado.
            exportador.exportar(
                    rutaArchivo,
                    ciudadActual,
                    evaluacionActual,
                    prediccionActual,
                    rankingActual,
                    (predictorActivo != null) ? predictorActivo.getNombre() : "Predictor no especificado"
            );

            mostrarMensajeExito("Informe exportado correctamente en:\n" + rutaArchivo);

        } catch (IOException e) {
            // Requisito Persona 2: Gestión de error obligatoria sin silenciar la excepción [cite: 2599, 2533]
            mostrarMensajeError("Error crítico al guardar el informe. Verifique la ruta y permisos del archivo.\nDetalles: " + e.getMessage());
        }
    }
}
