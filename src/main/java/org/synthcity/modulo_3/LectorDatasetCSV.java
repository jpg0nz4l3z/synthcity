package org.synthcity.modulo_3.prediccion;

import org.synthcity.modulo_3.ConversorClaseObjetivo;
import org.synthcity.modulo_3.RegistroDato;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee el dataset de entrenamiento desde un archivo CSV y devuelve una lista de RegistroDato.
 *
 * Formato esperado del CSV (15 columnas):
 *   nombre_ciudad, densidad, ratio_energetico, ratio_cobertura_servicios,
 *   contaminacion, contaminacion_acumulada, estabilidad_media,
 *   tendencia_estabilidad, tendencia_contaminacion, bienestar,
 *   score_viabilidad, ciclos_ejecutados, colapso_detectado,
 *   saturacion_detectada, objetivo
 *
 * La columna nombre_ciudad (índice 0) se ignora como feature.
 * La columna objetivo (índice 14) contiene la etiqueta nominal: CRITICO, INESTABLE, FUNCIONAL u OPTIMO.
 *
 * Registros con objetivo inválido o con número incorrecto de columnas son rechazados
 * con excepción indicando el número de línea.
 */
public class LectorDatasetCSV {

    private static final int COLUMNAS_ESPERADAS = 15;

    /**
     * Lee el CSV y devuelve la lista de RegistroDato válidos.
     *
     * @param rutaArchivo ruta absoluta o relativa al archivo CSV
     * @return lista de RegistroDato (nunca null, puede estar vacía)
     * @throws IllegalArgumentException si el archivo no existe, no puede leerse o tiene errores
     */
    public List<RegistroDato> leer(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isBlank()) {
            throw new IllegalArgumentException("La ruta del archivo CSV no puede ser nula ni vacía.");
        }

        List<RegistroDato> registros = new ArrayList<>();
        int numeroLinea = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {

            // Saltar cabecera
            String cabecera = reader.readLine();
            numeroLinea++;
            if (cabecera == null) {
                throw new IllegalArgumentException("El archivo CSV está vacío: " + rutaArchivo);
            }

            String linea;
            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                if (linea.isBlank()) continue;

                String[] partes = linea.split(",");
                if (partes.length != COLUMNAS_ESPERADAS) {
                    throw new IllegalArgumentException(
                            "Línea " + numeroLinea + ": se esperaban " + COLUMNAS_ESPERADAS +
                                    " columnas pero se encontraron " + partes.length + ".");
                }

                try {
                    RegistroDato registro = parsearLinea(partes, numeroLinea);
                    registros.add(registro);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            "Línea " + numeroLinea + ": error al parsear número: " + e.getMessage(), e);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            "Línea " + numeroLinea + ": " + e.getMessage(), e);
                }
            }

        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "No se puede leer el archivo CSV '" + rutaArchivo + "': " + e.getMessage(), e);
        }

        if (registros.isEmpty()) {
            throw new IllegalArgumentException(
                    "El archivo CSV no contiene registros de datos: " + rutaArchivo);
        }

        return registros;
    }

    /**
     * Parsea una línea del CSV y construye el RegistroDato correspondiente.
     * El orden de columnas es fijo (ver cabecera del archivo).
     */
    private RegistroDato parsearLinea(String[] partes, int numeroLinea) {
        // partes[0] = nombre_ciudad  → se ignora
        double densidad                = Double.parseDouble(partes[1].trim());
        double ratioEnergetico         = Double.parseDouble(partes[2].trim());
        double ratioCoberturaServicios = Double.parseDouble(partes[3].trim());
        double contaminacion           = Double.parseDouble(partes[4].trim());
        double contaminacionAcumulada  = Double.parseDouble(partes[5].trim());
        double estabilidadMedia        = Double.parseDouble(partes[6].trim());
        double tendenciaEstabilidad    = Double.parseDouble(partes[7].trim());
        double tendenciaContaminacion  = Double.parseDouble(partes[8].trim());
        double bienestar               = Double.parseDouble(partes[9].trim());
        double scoreViabilidad         = Double.parseDouble(partes[10].trim());
        int    ciclosEjecutados        = Integer.parseInt(partes[11].trim());
        boolean colapsoDetectado       = Boolean.parseBoolean(partes[12].trim());
        boolean saturacionDetectada    = Boolean.parseBoolean(partes[13].trim());

        // partes[14] = objetivo nominal → convertir a entero interno
        String etiqueta = partes[14].trim().toUpperCase();
        int objetivo = ConversorClaseObjetivo.convertirEntero(etiqueta);

        return new RegistroDato(
                densidad, ratioEnergetico, ratioCoberturaServicios,
                contaminacion, contaminacionAcumulada, estabilidadMedia,
                tendenciaEstabilidad, tendenciaContaminacion, bienestar,
                scoreViabilidad, colapsoDetectado, ciclosEjecutados,
                saturacionDetectada, objetivo
        );
    }
}