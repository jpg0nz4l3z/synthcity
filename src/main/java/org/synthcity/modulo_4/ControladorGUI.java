package org.synthcity.modulo_4;


import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;
import org.synthcity.modulo_3.NivelEvaluacion;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_4.PanelNotificacionExpansion;
import org.synthcity.modulo_3.ResultadoEvaluacion;


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
    private ResultadoSimulacion historialActual;
    private List<RegistroDato> datasetActual;

    private final SimuladorCiudad simulador;
    private final EvaluadorCiudad evaluador;
    private final PanelCiudad panelCiudad;
    private final PanelResumenSistema panelResumen;
    private final PanelEvolucionTemporal panelEvolucion;
    private PanelNotificacionExpansion panelNotificacion;

    private final Persistible resultadoRepository;
    private final DatasetRepository datasetRepository;



    public ControladorGUI(SimuladorCiudad simulador,
                          EvaluadorCiudad evaluador,
                          Persistible resultadoRepository,
                          DatasetRepository datasetRepository,
                          PanelCiudad panelCiudad,
                          PanelResumenSistema panelResumen,
                          PanelEvolucionTemporal panelEvolucion){
        this.simulador           = simulador;
        this.evaluador           = evaluador;
        this.resultadoRepository = resultadoRepository;
        this.datasetRepository   = datasetRepository;
        this.panelCiudad = panelCiudad;
        this.panelResumen = panelResumen;
        this.panelEvolucion      = panelEvolucion;
        this.datasetActual       = new ArrayList<>();

    }
    public ControladorGUI(PanelCiudad panelCiudad,
                          PanelResumenSistema panelResumen,
                          ResultadoRepository resultadoRepository) {
        this(new SimuladorCiudad(), new EvaluadorCiudad(),
                resultadoRepository, null,
                panelCiudad, panelResumen, null);
    }

    public void setPanelNotificacion(PanelNotificacionExpansion panel) {
        this.panelNotificacion = panel;
    }
    public void ejecutarSistemaCompleto(Ciudad ciudad) {
        if (ciudad == null) {
            System.err.println("[CONTROLADOR] Ciudad null — no se puede ejecutar.");
            return;
        }
        ResultadoSimulacion historial = simulador.simular(ciudad);
        ResultadoEvaluacion evaluacion = evaluador.evaluar(historial);
        RegistroDato registro = construirRegistroDato(ciudad, historial, evaluacion);
        List<RegistroDato> dataset = new ArrayList<>();
        dataset.add(registro);
        mostrarSistema(ciudad, evaluacion, historial, dataset);
    }



    public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion, ResultadoSimulacion historial,
                               List<RegistroDato> dataset) {
        this.ciudadActual = ciudad;
        this.evaluacionActual = evaluacion;
        this.historialActual  = historial;
        this.datasetActual    = (dataset != null)
                ? new ArrayList<>(dataset) : new ArrayList<>();

        boolean huboExpansion = evaluacion != null && evaluacion.fueExpandida();

        if (panelCiudad != null && ciudad != null) {
            if (huboExpansion) {
            panelCiudad.mostrarCiudad(ciudad);
        }else {
                panelCiudad.refrescar();
            }
        }

        if (panelResumen != null) {
            panelResumen.mostrarSistema(ciudad, evaluacion, historial); // actualizacion PanelResumenSistema
        }
        //  Actualizar PanelEvolucionTemporal
        if (panelEvolucion != null && historial != null) {
            panelEvolucion.mostrarHistorial(historial);
        }
        // Notificación de expansión
        if (huboExpansion && panelNotificacion != null) {
            panelNotificacion.mostrarExpansion();
        }
        if (datasetRepository != null && !this.datasetActual.isEmpty()) {
            RegistroDato ultimo = this.datasetActual.get(this.datasetActual.size() - 1);
            String nombre = (ciudad != null) ? ciudad.getNombre() : "desconocida";
            try {
                datasetRepository.guardarRegistro(ultimo, nombre);
            } catch (Exception e) {
                System.err.println("[CONTROLADOR] Error al persistir RegistroDato: "
                        + e.getMessage());
            }
        }
    }

    // Compatibilidad Sprint 2
    public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion) {
        mostrarSistema(ciudad, evaluacion, null, null);
    }
    //  Botón "Guardar historial"
    public void guardarHistorialActual() {
        if (resultadoRepository == null) {
            System.err.println("[CONTROLADOR] ResultadoRepository no disponible.");
            return;
        }
        if (historialActual == null) {
            System.err.println("[CONTROLADOR] No hay historial que guardar.");
            return;
        }
        resultadoRepository.guardar(historialActual);
        System.out.println("[CONTROLADOR] Historial guardado.");
    }

    // Botón "Guardar dataset"
    public void guardarDatasetActual() {
        if (datasetRepository == null) {
            System.err.println("[CONTROLADOR] DatasetRepository no disponible.");
            return;
        }
        if (datasetActual == null || datasetActual.isEmpty()) {
            System.out.println("[CONTROLADOR] Dataset vacío, nada que guardar.");
            return;
        }
        String nombre = (ciudadActual != null) ? ciudadActual.getNombre() : "desconocida";
        datasetRepository.guardarDataset(datasetActual, nombre);
        System.out.println("[CONTROLADOR] Dataset guardado: "
                + datasetActual.size() + " registros.");
    }

    //  Botón "Guardar resultado"
    public void guardarResultadoActual() {
        if (resultadoRepository != null
                && ciudadActual != null
                && evaluacionActual != null) {
            resultadoRepository.guardar(new Object[]{ciudadActual, evaluacionActual});
        }
    }
    // ExportarDatasetCSV
    public void exportarDatasetCSV(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isBlank()) {
            throw new IllegalArgumentException(
                    "La ruta del archivo CSV no puede ser null ni vacía.");
        }
        if (datasetActual == null || datasetActual.isEmpty()) {
            throw new IllegalStateException(
                    "No hay registros en el dataset. " +
                            "Ejecute al menos una evaluación antes de exportar.");
        }

        String cabecera = Arrays.stream(RegistroDato.getFeatureNames())
                .collect(Collectors.joining(","));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {

            writer.write(cabecera);
            writer.newLine();

            // STREAM
            datasetActual.stream()
                    .map(registro -> Arrays.stream(registro.toArray())
                            .mapToObj(valor -> {
                                if (valor == Math.floor(valor) && !Double.isInfinite(valor)) {
                                    return String.valueOf((long) valor);
                                }
                                return String.format(java.util.Locale.ROOT, "%.6f", valor);
                            })
                            .collect(Collectors.joining(",")))
                    .forEach(linea -> {
                        try {
                            writer.write(linea);
                            writer.newLine();
                        } catch (IOException ex) {
                            throw new RuntimeException(
                                    "Error al escribir línea en CSV: " + ex.getMessage(), ex);
                        }
                    });

            System.out.println("[CSV] Dataset exportado: "
                    + datasetActual.size() + " registros → " + rutaArchivo);

        } catch (IOException e) {
            throw new RuntimeException(
                    "No se pudo escribir el CSV en: " + rutaArchivo +
                            ". Comprueba permisos y espacio en disco.", e);
        }
    }


    public void refrescarVista() {
        if (ciudadActual != null && evaluacionActual != null) {
            mostrarSistema(ciudadActual, evaluacionActual,historialActual, datasetActual );
        }
    }

    public void limpiarVista() {
        ciudadActual = null;
        evaluacionActual = null;
        historialActual  = null;
        datasetActual    = new ArrayList<>();

        if (panelCiudad != null) panelCiudad.limpiar();
        if (panelResumen != null)panelResumen.limpiar();
        if (panelEvolucion    != null) panelEvolucion.limpiar();
        if (panelNotificacion != null) panelNotificacion.limpiar();
    }
    // Getters
    public Ciudad              getCiudadActual()     { return ciudadActual; }
    public ResultadoEvaluacion getEvaluacionActual() { return evaluacionActual; }
    public ResultadoSimulacion getHistorialActual()  { return historialActual; }
    public List<RegistroDato>  getDatasetActual()    { return new ArrayList<>(datasetActual); }

    private RegistroDato construirRegistroDato(Ciudad ciudad,
                                               ResultadoSimulacion historial,
                                               ResultadoEvaluacion evaluacion) {
        int objetivo = switch (evaluacion.getNivelEvaluacion()) {
            case SIN_DATOS  -> 0;
            case CRITICO    -> 1;
            case INESTABLE  -> 2;
            case FUNCIONAL  -> 3;
            case OPTIMO     -> 4;
        };

        return new RegistroDato(
                ciudad.getNombre(),
                historial.getDensidad(),
                historial.getRatioEnergetico(),
                historial.getRatioCoberturaServicios(),
                historial.getContaminacion(),
                historial.getEstabilidadBasica(),
                evaluacion.getScoreViabilidad(),
                objetivo
        );
    }
}

