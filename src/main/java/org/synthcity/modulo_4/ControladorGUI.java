package org.synthcity.modulo_4;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.RegistroDato;
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
    private ResultadoSimulacion historialActual;
    private ResultadoEvaluacion evaluacionActual;
    private PredictionResult prediccionActual;

    private final SimuladorCiudad simulador;
    private final EvaluadorCiudad evaluador;

    private final PanelCiudad panelCiudad;
    private final PanelResumenSistema panelResumen;
    private final PanelEvolucionTemporal panelEvolucion;
    private PanelNotificacionExpansion panelNotificacion;

    private final ResultadoRepository resultadoRepository;
    private final DatasetRepository datasetRepository;


    public ControladorGUI(SimuladorCiudad simulador,
                          EvaluadorCiudad evaluador,
                          PanelCiudad panelCiudad,
                          PanelResumenSistema panelResumen,
                          PanelEvolucionTemporal panelEvolucion,
                          ResultadoRepository resultadoRepository,
                          DatasetRepository datasetRepository
    ) {


        if (simulador == null || evaluador == null) {
            throw new FormatoSalidaException("Simulador y evaluador son obligatorios.");
        }


        this.simulador = simulador;
        this.evaluador = evaluador;
        this.panelCiudad = panelCiudad;
        this.panelResumen = panelResumen;
        this.panelEvolucion = panelEvolucion;
        this.resultadoRepository = resultadoRepository;
        this.datasetRepository = datasetRepository;

    }

    // Sistema Completo
    public void ejecutarSistemaCompleto(Ciudad ciudad) {
        if (ciudad == null) {
            throw new FormatoSalidaException("La ciudad no puede ser null.");
        }
        //System.out.println("[CONTROLADOR] Iniciando flujo completo: " + ciudad.getNombre());

        /*ResultadoSimulacion historial = simulador.simular(ciudad);
        ResultadoEvaluacion evaluacion = evaluador.evaluar(historial,ciudad);

        int filasAntes = ciudad.getFilas();
        int columnasAntes = ciudad.getColumnas();
        String tipoAntes = ciudad.getTipoEstructural().name();

        if (evaluacion.isExpansionEjecutada()) {
            historial = simulador.simular(ciudad);
            evaluacion = evaluador.evaluar(historial,ciudad);
        }*/

        ResultadoSimulacion historial = simulador.simular(ciudad);

        int filasAntes = ciudad.getFilas();
        int columnasAntes = ciudad.getColumnas();
        String tipoAntes = ciudad.getTipoEstructural().name();

        ResultadoEvaluacion evaluacion = evaluador.evaluar(historial, ciudad);

        if (evaluacion.isExpansionEjecutada()) {
            historial = simulador.simular(ciudad);
        }

        PredictionResult prediccion = evaluador.predecir(historial);

        mostrarSistema(
                ciudad,
                evaluacion,
                prediccion,
                historial,
                evaluacion.isExpansionEjecutada(),
                filasAntes,
                columnasAntes,
                tipoAntes
        );

        guardarDatasetSiExiste(ciudad);
    }

    public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion,
                               PredictionResult prediccion) {
        mostrarSistema(ciudad, evaluacion, prediccion, null, false, 0, 0, "");
    }

    public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion,
                               PredictionResult prediccion,
                               ResultadoSimulacion historial,
                               boolean fueExpandida,
                               int filasAntes, int columnasAntes,
                               String tipoAntes) {
        this.ciudadActual = ciudad;
        this.evaluacionActual = evaluacion;
        this.prediccionActual = prediccion;
        this.historialActual = historial;


        if (panelCiudad != null) {
            panelCiudad.mostrarCiudad(ciudad);
        }

        if (panelResumen != null) {
            panelResumen.mostrarSistema(ciudad, evaluacion, prediccion);
        }

        if (panelEvolucion != null) {
            panelEvolucion.mostrarHistorial(historial);
        }

        if (panelNotificacion != null) {
            if (fueExpandida && ciudad != null) {
                String tipoDespues = ciudad.getTipoEstructural().name();
                panelNotificacion.mostrarExpansion(
                        filasAntes,
                        columnasAntes,
                        ciudad.getFilas(),
                        ciudad.getColumnas(),
                        tipoAntes,
                        tipoDespues,
                        !tipoAntes.equals(tipoDespues)
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
        if (ciudadActual == null || evaluacionActual == null || prediccionActual == null) {
            throw new FormatoSalidaException("No hay un resultado completo para guardar.");
        }

        resultadoRepository.guardarResultado(ciudadActual, evaluacionActual, prediccionActual);
    }

    public void guardarHistorialActual() {
        if (resultadoRepository == null) {
            throw new FormatoSalidaException("No hay repositorio disponible para guardar historial.");
        }
        if (historialActual == null || ciudadActual == null) {
            throw new FormatoSalidaException("No hay historial disponible para guardar.");
        }

        resultadoRepository.guardarHistorial(historialActual, ciudadActual.getNombre());
    }

    public void guardarDatasetActual() {
        if (datasetRepository == null) {
            throw new FormatoSalidaException("No hay repositorio disponible para guardar dataset.");
        }
        if (ciudadActual == null) {
            throw new FormatoSalidaException("No hay ciudad actual para asociar al dataset.");
        }

        List<RegistroDato> registros = evaluador.getConstructorDataset().getDataset();
        datasetRepository.guardarDataset(registros, ciudadActual.getNombre());
    }

    public void exportarDatasetCSV(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isBlank()) {
            throw new FormatoSalidaException("La ruta del CSV no puede ser nula o vacía.");
        }

        List<RegistroDato> registros = evaluador.getConstructorDataset().getDataset();

        if (registros.isEmpty()) {
            throw new FormatoSalidaException("No hay registros de dataset para exportar.");
        }

        String cabecera = "densidad,ratioEnergetico,ratioCoberturaServicios,"
                + "contaminacion,contaminacionAcumulada,estabilidadMedia,"
                + "tendenciaEstabilidad,tendenciaContaminacion,bienestar,"
                + "scoreViabilidad,colapsoDetectado,ciclosEjecutados,"
                + "saturacionDetectada,objetivo";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(cabecera);
            writer.newLine();

            registros.stream()
                    .map(registro -> {
                        String features = Arrays.stream(registro.toArray())
                                .mapToObj(String::valueOf)
                                .collect(Collectors.joining(","));

                        return features + "," + registro.getObjetivo();
                    })
                    .forEach(linea -> {
                        try {
                            writer.write(linea);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException("Error al escribir una línea del CSV.", e);
                        }
                    });

        } catch (IOException e) {
            throw new FormatoSalidaException("Error al exportar el dataset CSV.", e);
        } catch (RuntimeException e) {
            throw new FormatoSalidaException("Error al escribir el contenido del CSV.", e);
        }
    }

    private void guardarDatasetSiExiste(Ciudad ciudad) {
        if (datasetRepository == null || ciudad == null) {
            return;
        }

        List<RegistroDato> registros = evaluador.getConstructorDataset().getDataset();

        if (!registros.isEmpty()) {
            datasetRepository.guardarDataset(registros, ciudad.getNombre());
        }
    }


    //-----------------------------------------------------------

    public void refrescarVista() {
        if (ciudadActual != null && evaluacionActual != null) {
            mostrarSistema(ciudadActual, evaluacionActual, prediccionActual, historialActual, false, 0, 0, "");
        }
    }

    public void limpiarVista() {
        ciudadActual = null;
        evaluacionActual = null;
        prediccionActual = null;
        historialActual = null;

        if (panelCiudad != null) panelCiudad.limpiar();
        if (panelResumen != null) panelResumen.limpiar();
        if (panelEvolucion != null) panelEvolucion.limpiar();
        if (panelNotificacion != null) panelNotificacion.ocultar();
    }

    public void setPanelNotificacion(PanelNotificacionExpansion panel) {
        this.panelNotificacion = panel;
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


    private boolean necesitaYPuedeExpandirse(ResultadoEvaluacion evaluacion, Ciudad ciudad) {
        double densidad = evaluacion.getMetricaCiudad().getDensidad();
        double indiceSaturacion = evaluacion.getMetricaCiudad().getIndiceSaturacion();
        double ratioServicios = evaluacion.getMetricaCiudad().getRatioCoberturaServicios();

        boolean necesita = densidad >= 0.80
                || indiceSaturacion >= 0.75
                || (densidad >= 0.70 && ratioServicios < 1.0);

        return necesita && ciudad.puedeExpandirse();
    }
}




