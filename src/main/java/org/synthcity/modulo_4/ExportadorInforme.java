package org.synthcity.modulo_4;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.prediccion.PredictionResult;
import org.synthcity.modulo_3.ResultadoEvaluacion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Responsabilidad de Persona 2: Encapsula la lógica de exportación del informe final.
 * Utiliza BufferedWriter con try-with-resources y procesamiento de colecciones con Streams. [cite: 2317, 2591]
 */
public class ExportadorInforme {

    public void exportar(String rutaArchivo, Ciudad ciudad, ResultadoEvaluacion evaluacion,
                         PredictionResult prediccion, List<ResultadoEvaluacion> ranking,
                         String nombrePredictorActivo) throws IOException {

        // Paso obligatorio: Uso de try-with-resources para asegurar el cierre del flujo.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {

            // 1. Cabecera con nombre de la ciudad y fecha de generación. [cite: 2320, 2321]
            writer.write("=================================================\n");
            writer.write(" INFORME FINAL DE LA CIUDAD: " + ciudad.getNombre() + "\n");
            writer.write(" Fecha de generación: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
            writer.write("=================================================\n\n");

            // 2. Resumen estructural: dimensiones, tipo y densidad. [cite: 2321]
            writer.write("--- RESUMEN ESTRUCTURAL ---\n");
            writer.write("- Dimensiones: " + ciudad.getFilas() + "x" + ciudad.getColumnas() + "\n");
            writer.write("- Tipo Estructural: " + ciudad.getTipoEstructural() + "\n");
            writer.write("- Densidad: " + ciudad.getDensidad() + "\n\n");

            // 3. Evaluación: nivel, score, mensaje y alertas.
            writer.write("--- EVALUACIÓN ACTUAL ---\n");
            if (evaluacion != null) {
                writer.write("- Nivel: " + evaluacion.getNivel() + "\n");
                writer.write("- Score: " + evaluacion.getScoreViabilidad() + "\n");
                writer.write("- Mensaje: " + evaluacion.getMensaje() + "\n");

                // CORRECCIÓN DEL ERROR: Usamos Stream para convertir AlertaEvaluacion a String antes de unir.
                String alertasTexto = evaluacion.getAlertas().stream()
                        .map(alerta -> alerta.toString()) // O alerta.getMensaje() si existe ese método
                        .collect(Collectors.joining(", "));

                writer.write("- Alertas: " + (alertasTexto.isEmpty() ? "Ninguna" : alertasTexto) + "\n\n");
            }

            // 4. Predicción: tendencia, score, mensaje y predictor activo. [cite: 2323]
            writer.write("--- PREDICCIÓN FUTURA ---\n");
            if (prediccion != null) {
                writer.write("- Predictor activo: " + nombrePredictorActivo + "\n");
                writer.write("- Tendencia: " + prediccion.getTendencia() + "\n");
                writer.write("- Score predicho: " + prediccion.getScorePredicho() + "\n");
                writer.write("- Mensaje: " + prediccion.getMensaje() + "\n\n");
            }

            // 5. Ranking: Sección construida obligatoriamente con Streams. [cite: 2324, 2598, 2635]
            writer.write("--- RANKING GLOBAL DE CIUDADES ---\n");
            if (ranking != null && !ranking.isEmpty()) {
                String textoRanking = ranking.stream()
                        .map(e -> e.getNombreCiudad() + " - Score: " + e.getScoreViabilidad())
                        .collect(Collectors.joining("\n"));
                writer.write(textoRanking + "\n");
            } else {
                writer.write("No hay suficientes datos para generar un ranking.\n");
            }
        }
    }
}