package org.synthcity.modulo_4;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.RegistroDato;
import org.synthcity.modulo_3.ResultadoEvaluacion;

import java.io.IOException;

import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;
import org.synthcity.modulo_3.GestorExpansion;
import org.synthcity.modulo_3.ConstructorDataset;
import org.synthcity.modulo_3.prediccion.PredictionResult;
import org.synthcity.modulo_3.prediccion.Predictor;
import org.synthcity.modulo_3.prediccion.PredictorHeuristico;
import org.synthcity.modulo_3.prediccion.PredictorWeka;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class ControladorGUI {
    private Ciudad ciudadActual;
    private ResultadoSimulacion historialActual;
    private boolean usarML = false;
    private ResultadoEvaluacion evaluacionActual;
    private PredictionResult prediccionActual;
    private List<ResultadoEvaluacion> rankingActual;
    private final SimuladorCiudad simulador;
    private EvaluadorCiudad evaluador;

    // Predictores inyectados para cambiar dinámicamente
    private Predictor predictorHeuristico;
    private Predictor predictorML;
    private Predictor predictorActivo;

    private final PanelCiudad panelCiudad;
    private final PanelResumenSistema panelResumen;
    private final PanelEvolucionTemporal panelEvolucion;
    private PanelNotificacionExpansion panelNotificacion;
    private PanelRanking panelRanking = new PanelRanking();

    private final ResultadoRepository resultadoRepository;
    private final DatasetRepository datasetRepository;


    public ControladorGUI(SimuladorCiudad simulador,
                          EvaluadorCiudad evaluador,
                          PanelCiudad panelCiudad,
                          PanelResumenSistema panelResumen,
                          PanelEvolucionTemporal panelEvolucion,
                          ResultadoRepository resultadoRepository,
                          DatasetRepository datasetRepository) {
        this(simulador, evaluador, panelCiudad, panelResumen, panelEvolucion, resultadoRepository, datasetRepository, new PanelRanking());
    }

    public ControladorGUI(SimuladorCiudad simulador,
                          EvaluadorCiudad evaluador,
                          PanelCiudad panelCiudad,
                          PanelResumenSistema panelResumen,
                          PanelEvolucionTemporal panelEvolucion,
                          ResultadoRepository resultadoRepository,
                          DatasetRepository datasetRepository,
                          PanelRanking panelRanking) {

        if (simulador == null || evaluador == null) {
            throw new FormatoSalidaException("Simulador y evaluador son obligatorios.");
        }

        this.simulador = simulador;
        this.evaluador = evaluador;
        this.panelCiudad = panelCiudad;
        this.panelResumen = panelResumen;
        this.panelEvolucion = panelEvolucion;
        this.panelRanking = panelRanking;
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

    public void setPredictores(Predictor heuristico, Predictor ml) {
        if (heuristico == null || ml == null) {
            throw new FormatoSalidaException("Los predictores no pueden ser null.");
        }
        this.predictorHeuristico = heuristico;
        this.predictorML = ml;
        this.predictorActivo = heuristico;
    }

    public void cambiarPredictor(boolean usarML) {
        this.usarML = usarML;

        // Seleccionar el predictor según el usuario
        this.predictorActivo = usarML ? predictorML : predictorHeuristico;

        // Reconstruir el evaluador con el nuevo predictor inyectado
        this.evaluador = new EvaluadorCiudad(
                simulador::simular,
                new GestorExpansion(),
                new ConstructorDataset(),
                this.predictorActivo
        );

        if (usarML) {
            System.out.println("[CONTROLADOR] Predictor ML (Weka) activado");
            entrenarModeloWekaConDatos();
        } else {
            System.out.println("[CONTROLADOR] Predictor heurístico activado");
        }

        // Si hay ciudad actual, re-ejecutar el flujo con el nuevo predictor
        if (ciudadActual != null) {
            ejecutarSistemaCompleto(ciudadActual);
        }
    }

    private void entrenarModeloWekaConDatos() {
        if (!(predictorActivo instanceof PredictorWeka)) {
            return;
        }

        PredictorWeka predictorWeka = (PredictorWeka) predictorActivo;

        try {
            List<RegistroDato> datosEntrenamiento = cargarDatosDelCSV("dataset_entrenamiento_weka_synthcity.csv");

            if (datosEntrenamiento.isEmpty()) {
                System.out.println("[CONTROLADOR] CSV no encontrado, intentando usar datos de la base de datos...");
                if (datasetRepository != null) {
                    datosEntrenamiento = datasetRepository.listarRegistros();
                }
            }

            if (datosEntrenamiento.isEmpty()) {
                System.out.println("[CONTROLADOR] Usando datos mínimos de entrenamiento...");
                datosEntrenamiento = crearDatosMinimos();
            }

            if (!datosEntrenamiento.isEmpty()) {
                predictorWeka.entrenar(datosEntrenamiento);
                System.out.println("[CONTROLADOR] Modelo Weka entrenado con " + datosEntrenamiento.size() + " registros.");
            }
        } catch (Exception e) {
            System.err.println("[CONTROLADOR] Error al entrenar modelo Weka: " + e.getMessage());
        }
    }

    private List<RegistroDato> crearDatosMinimos() {
        List<RegistroDato> datos = new ArrayList<>();
        datos.add(new RegistroDato(0.3, 0.8, 0.9, 20, 50, 0.7, 0.1, 0.2, 0.8, 75, false, 10, false, 4));
        datos.add(new RegistroDato(0.5, 0.6, 0.7, 40, 100, 0.5, 0.0, 0.1, 0.6, 65, false, 15, false, 3));
        datos.add(new RegistroDato(0.8, 0.3, 0.4, 80, 200, 0.3, -0.1, 0.3, 0.4, 45, true, 20, true, 2));
        datos.add(new RegistroDato(0.9, 0.2, 0.3, 90, 250, 0.2, -0.2, 0.4, 0.3, 35, true, 25, true, 1));
        return datos;
    }

    private List<RegistroDato> cargarDatosDelCSV(String nombreArchivo) {
        List<RegistroDato> datos = new ArrayList<>();

        java.io.File archivo = encontrarArchivo(nombreArchivo);
        if (archivo == null || !archivo.exists()) {
            System.err.println("[CONTROLADOR] No se encontró el archivo CSV en ubicaciones conocidas.");
            return datos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primerLinea = true;
            int numLinea = 0;

            while ((linea = br.readLine()) != null) {
                numLinea++;
                if (primerLinea) {
                    primerLinea = false;
                    continue;
                }

                String[] campos = linea.split(",");
                if (campos.length < 15) {
                    System.err.println("[CONTROLADOR] Línea " + numLinea + " tiene " + campos.length + " campos (esperaba 15)");
                    continue;
                }

                try {
                    double densidad = Double.parseDouble(campos[1].trim());
                    double ratioEnergetico = Double.parseDouble(campos[2].trim());
                    double ratioCoberturaServicios = Double.parseDouble(campos[3].trim());
                    double contaminacion = Double.parseDouble(campos[4].trim());
                    double contaminacionAcumulada = Double.parseDouble(campos[5].trim());
                    double estabilidadMedia = Double.parseDouble(campos[6].trim());
                    double tendenciaEstabilidad = Double.parseDouble(campos[7].trim());
                    double tendenciaContaminacion = Double.parseDouble(campos[8].trim());
                    double bienestar = Double.parseDouble(campos[9].trim());
                    double scoreViabilidad = Double.parseDouble(campos[10].trim());
                    int ciclosEjecutados = Integer.parseInt(campos[11].trim());
                    boolean colapsoDetectado = Boolean.parseBoolean(campos[12].trim());
                    boolean saturacionDetectada = Boolean.parseBoolean(campos[13].trim());
                    String objetivo = campos[14].trim();

                    int objetivoNumerico = convertirObjetivoANumerico(objetivo);

                    RegistroDato registro = new RegistroDato(
                            densidad, ratioEnergetico, ratioCoberturaServicios,
                            contaminacion, contaminacionAcumulada, estabilidadMedia,
                            tendenciaEstabilidad, tendenciaContaminacion, bienestar,
                            scoreViabilidad, colapsoDetectado, ciclosEjecutados,
                            saturacionDetectada, objetivoNumerico
                    );
                    datos.add(registro);
                } catch (NumberFormatException e) {
                    System.err.println("[CONTROLADOR] Error al parsear línea " + numLinea + ": " + e.getMessage());
                }
            }

            System.out.println("[CONTROLADOR] Cargados " + datos.size() + " registros del CSV de entrenamiento desde: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[CONTROLADOR] Error al leer archivo CSV: " + e.getMessage());
        }

        return datos;
    }

    private java.io.File encontrarArchivo(String nombreArchivo) {
        String[] rutas = {
                nombreArchivo,
                new java.io.File(nombreArchivo).getAbsolutePath(),
                "." + java.io.File.separator + nombreArchivo,
                System.getProperty("user.dir") + java.io.File.separator + nombreArchivo
        };

        for (String ruta : rutas) {
            java.io.File archivo = new java.io.File(ruta);
            if (archivo.exists()) {
                System.out.println("[CONTROLADOR] Encontrado archivo CSV en: " + archivo.getAbsolutePath());
                return archivo;
            }
        }

        return null;
    }

    private int convertirObjetivoANumerico(String objetivo) {
        return switch(objetivo.toUpperCase()) {
            case "OPTIMO" -> 4;
            case "FUNCIONAL" -> 3;
            case "INESTABLE" -> 2;
            case "CRITICO" -> 1;
            default -> 0;
        };
    }

    public void exportarInformeActual(String rutaArchivo) {
        if (evaluacionActual == null) {
            System.err.println("No se puede exportar: primero debe ejecutarse una simulación válida.");
            return;
        }

        ExportadorInforme exportador = new ExportadorInforme();

        try {
            exportador.exportar(
                    rutaArchivo,
                    ciudadActual,
                    evaluacionActual,
                    prediccionActual,
                    rankingActual,
                    usarML ? "Predictor ML (Weka)" : "Predictor Heurístico"
            );

            // Mensaje de éxito por consola limpia
            System.out.println("Informe exportado correctamente en: " + rutaArchivo);

        } catch (IOException e) {
            // Requisito del Sprint 4: No silenciar el error y mostrar mensaje claro [cite: 176, 499, 502]
            System.err.println("Error crítico al guardar el informe. Detalles: " + e.getMessage());
        }
    }
    public void calcularYMostrarRanking() {
        if (resultadoRepository == null || datasetRepository == null) {
            panelRanking.actualizarRanking("Repositorio no disponible.");
            return;
        }

        try {
            List<String> historialLista = datasetRepository.listarRegistrosBasicos();

            if (historialLista == null || historialLista.isEmpty()) {
                System.out.println("[RANKING] No hay registros en la base de datos.");
                panelRanking.actualizarRanking("No hay evaluaciones guardadas.");
                return;
            }

            System.out.println("[RANKING] Se encontraron " + historialLista.size() + " registros.");
            historialLista.forEach(linea -> System.out.println("[RANKING] Línea: " + linea));

            String textoTop = historialLista.stream()
                    .filter(linea -> linea != null && linea.contains("| Score: "))
                    .sorted((l1, l2) -> {
                        try {
                            double s1 = Double.parseDouble(l1.split("\\| Score: ")[1].split(" \\|")[0].trim());
                            double s2 = Double.parseDouble(l2.split("\\| Score: ")[1].split(" \\|")[0].trim());
                            return Double.compare(s2, s1);
                        } catch (Exception e) {
                            System.err.println("[RANKING] Error al parsear: " + l1 + " -> " + e.getMessage());
                            return 0;
                        }
                    })
                    .limit(5)
                    .map(linea -> "▶ " + linea)
                    .collect(Collectors.joining("\n"));

            if (textoTop == null || textoTop.isBlank()) {
                System.out.println("[RANKING] No se encontraron registros válidos con score.");
                panelRanking.actualizarRanking("No hay evaluaciones con datos válidos.");
            } else {
                System.out.println("[RANKING] Ranking actualizado: " + textoTop);
                panelRanking.actualizarRanking(textoTop);
            }

        } catch (Exception e) {
            System.err.println("[RANKING] Error al calcular ranking: " + e.getMessage());
            e.printStackTrace();
            panelRanking.actualizarRanking("Error: " + e.getMessage());
        }
    }

    public String filtrarHistorialPorNivel(String nivelDeseado) {
        if (resultadoRepository == null || nivelDeseado == null) {
            return "Filtro no disponible.";
        }

        List<String> historialLista = datasetRepository.listarRegistrosBasicos();

        if (historialLista == null || historialLista.isEmpty()) {
            return "No hay registros.";
        }

        return historialLista.stream()
                .filter(linea -> linea != null && linea.toLowerCase().contains("obj: " + nivelDeseado.toLowerCase()))
                .map(linea -> "▶ " + linea)
                .collect(Collectors.joining("\n"));
    }
}



