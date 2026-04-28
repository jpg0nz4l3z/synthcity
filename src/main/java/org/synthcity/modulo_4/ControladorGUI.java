package org.synthcity.modulo_4;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.prediccion.PredictionResult;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorGUI {

    private Ciudad ciudadActual;
    private ResultadoEvaluacion evaluacionActual;
    private PredictionResult prediccionActual;
    private ResultadoSimulacion historialActual;
    private final List<RegistroDato> datasetActual = new ArrayList<>();

    private final PanelCiudad panelCiudad;
    private final PanelResumenSistema panelResumen;
    private final ResultadoRepository resultadoRepository;
    private final DatasetRepository datasetRepository;
    private PanelEvolucionTemporal panelEvolucion;
    private PanelNotificacionExpansion panelNotificacion;

    private final SimuladorCiudad simulador;
    private final EvaluadorCiudad evaluador;


    public ControladorGUI(SimuladorCiudad simulador,
                          EvaluadorCiudad evaluador,
                          PanelCiudad panelCiudad,
                          PanelResumenSistema panelResumen,
                          ResultadoRepository resultadoRepository,
                          DatasetRepository datasetRepository,
                          PanelEvolucionTemporal panelEvolucion) {
        this.simulador           = simulador;
        this.evaluador           = evaluador;
        this.panelCiudad = panelCiudad;
        this.panelResumen = panelResumen;
        this.resultadoRepository = resultadoRepository;
        this.datasetRepository   = datasetRepository;
        this.panelEvolucion      = panelEvolucion;
    }
    // Sistema Completo
    public void ejecutarSistemaCompleto(Ciudad ciudad) {
        if (ciudad == null) {
            System.err.println("[CONTROLADOR] Ciudad nula, cancelando.");
            return;
        }
        System.out.println("[CONTROLADOR] Iniciando flujo completo: " + ciudad.getNombre());

        ResultadoSimulacion historial = simulador.simular(ciudad);
        ResultadoEvaluacion evaluacion = evaluador.evaluar(historial);

        int filasAntes    = ciudad.getFilas();
        int columnasAntes = ciudad.getColumnas();
        String tipoAntes  = ciudad.getTipoEstructural().name();
        boolean fueExpandida = necesitaYPuedeExpandirse(evaluacion, ciudad);

        if (fueExpandida) {
            ciudad.expandirSegunPolitica();
            historial  = simulador.simular(ciudad);
            evaluacion = evaluador.evaluar(historial);
        }

        RegistroDato registro = construirRegistroDato(ciudad, evaluacion, historial);
        datasetActual.add(registro);

        mostrarSistema(ciudad, evaluacion, null, historial,
                fueExpandida, filasAntes, columnasAntes, tipoAntes);

        if (datasetRepository != null) {
            try {
                datasetRepository.guardarRegistro(registro, ciudad.getNombre());
            } catch (Exception e) {
                System.err.println("[CONTROLADOR] Error al persistir: " + e.getMessage());
            }
        }
    }

    public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion,
                               PredictionResult prediccion){
        mostrarSistema(ciudad, evaluacion, prediccion, null, false, 0, 0, ""); }

    public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion,
                               PredictionResult prediccion,
                               ResultadoSimulacion historial,
                               boolean fueExpandida,
                               int filasAntes, int columnasAntes,
                               String tipoAntes) {
        this.ciudadActual = ciudad;
        this.evaluacionActual = evaluacion;
        this.prediccionActual = prediccion;
        this.historialActual  = historial;

        if (panelCiudad != null) {
            if (fueExpandida) {
            panelCiudad.mostrarCiudad(ciudad);
        } else {
                panelCiudad.refrescar();
            }
        }
        if (panelResumen != null) {
            panelResumen.mostrarSistema(ciudad, evaluacion,historial);
            if (fueExpandida) {
                panelResumen.mostrarExpansion(
                        filasAntes, columnasAntes,
                        ciudad.getFilas(), ciudad.getColumnas(),
                        tipoAntes, ciudad.getTipoEstructural().name(),
                        !tipoAntes.equals(ciudad.getTipoEstructural().name())
                );
            }
        }
        if (panelEvolucion != null && historial != null) {
            panelEvolucion.mostrarHistorial(historial);
        }

        if (panelNotificacion != null) {
            if (fueExpandida) {
                boolean cambioTipo = !tipoAntes.equals(ciudad.getTipoEstructural().name());
                panelNotificacion.mostrarExpansion(
                        filasAntes, columnasAntes,
                        ciudad.getFilas(), ciudad.getColumnas(),
                        tipoAntes, ciudad.getTipoEstructural().name(),
                        cambioTipo
                );
            } else {
                panelNotificacion.ocultar();
            }
        }
    }
    public void guardarResultadoActual() {
        if (resultadoRepository == null) {
            throw new FormatoSalidaException("No hay repositorio disponible.");
        }
        if (ciudadActual == null || evaluacionActual == null) {
            throw new FormatoSalidaException("No hay resultado completo para guardar.");
        }
        resultadoRepository.guardarResultado(ciudadActual,evaluacionActual);
    }

    public void guardarHistorialActual() {
        if (resultadoRepository != null && historialActual != null && ciudadActual != null) {
            resultadoRepository.guardarHistorial(historialActual, ciudadActual.getNombre());
        }
    }

    public void guardarDatasetActual() {
        if (datasetRepository != null && !datasetActual.isEmpty() && ciudadActual != null) {
            datasetRepository.guardarDataset(datasetActual, ciudadActual.getNombre());
        }
    }

    public void exportarDatasetCSV(String rutaArchivo) {
        if (datasetActual.isEmpty()) {
            System.err.println("[CSV] No hay datos para exportar.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {

            writer.write(String.join(",", RegistroDato.getFeatureNames()));
            writer.newLine();

            datasetActual.stream()
                    .map(r -> {
                        String features = Arrays.stream(r.toArray())
                                .mapToObj(v -> String.format(java.util.Locale.ROOT, "%.6f", v))
                                .collect(Collectors.joining(","));
                        return features + "," + r.getObjetivo();
                    })
                    .forEach(linea -> {
                        try {
                            writer.write(linea);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException("Error al escribir línea CSV.", e);
                        }
                    });

            System.out.println("[CSV] Dataset exportado a: " + rutaArchivo);

        } catch (IOException e) {
            throw new FormatoSalidaException("Error al exportar CSV: " + rutaArchivo, e);
        }
    }

    public void refrescarVista() {
        if (ciudadActual != null && evaluacionActual != null) {
            mostrarSistema(ciudadActual, evaluacionActual, prediccionActual,historialActual, false, 0, 0, "");
        }
    }

    public void limpiarVista() {
        ciudadActual = null;
        evaluacionActual = null;
        prediccionActual = null;
        historialActual  = null;

        if (panelCiudad != null) panelCiudad.limpiar();
        if (panelResumen != null) panelResumen.limpiar();
        if (panelEvolucion    != null) panelEvolucion.limpiar();
        if (panelNotificacion != null) panelNotificacion.ocultar();
    }
    public void setPanelNotificacion(PanelNotificacionExpansion panel) {
        this.panelNotificacion = panel;
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
    public List<RegistroDato> getDatasetActual() {
        return new ArrayList<>(datasetActual);
    }
private boolean necesitaYPuedeExpandirse(ResultadoEvaluacion evaluacion, Ciudad ciudad) {
    double densidad         = evaluacion.getMetricaCiudad().getDensidad();
    double indiceSaturacion = evaluacion.getMetricaCiudad().getIndiceSaturacion();
    double ratioServicios   = evaluacion.getMetricaCiudad().getRatioCoberturaServicios();

    boolean necesita = densidad >= 0.80
            || indiceSaturacion >= 0.75
            || (densidad >= 0.70 && ratioServicios < 1.0);

    return necesita && ciudad.puedeExpandirse();
}

private RegistroDato construirRegistroDato(Ciudad ciudad,
                                           ResultadoEvaluacion evaluacion,
                                           ResultadoSimulacion historial) {
    int objetivo = switch (evaluacion.getNivelEvaluacion()) {
        case SIN_DATOS  -> 0;
        case CRITICO    -> 1;
        case INESTABLE  -> 2;
        case FUNCIONAL  -> 3;
        case OPTIMO     -> 4;
    };

    return new RegistroDato(
            ciudad.getNombre(),
            evaluacion.getMetricaCiudad().getDensidad(),
            evaluacion.getMetricaCiudad().getRatioEnergetico(),
            evaluacion.getMetricaCiudad().getRatioCoberturaServicios(),
            evaluacion.getMetricaCiudad().getContaminacion(),
            evaluacion.getMetricaCiudad().getEstabilidadBasica(),
            evaluacion.getScoreViabilidad(),
            objetivo,
            1,
            historial.getEstadoSimulacion().name()
    );
}



