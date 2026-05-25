package org.synthcity.modulo_3;

import org.synthcity.modulo_3.ConversorClaseObjetivo;
import org.synthcity.modulo_3.RegistroDato;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LectorDatasetCSV {

    private static final int COLUMNAS_ESPERADAS = 15;


    public List<RegistroDato> leer(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isBlank()) {
            throw new IllegalArgumentException("La ruta del archivo CSV no puede ser nula ni vacía.");
        }

        List<RegistroDato> registros = new ArrayList<>();
        int numeroLinea = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {


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


    private RegistroDato parsearLinea(String[] partes, int numeroLinea) {

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