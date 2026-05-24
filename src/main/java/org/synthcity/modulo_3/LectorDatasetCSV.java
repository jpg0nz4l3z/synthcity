package org.synthcity.modulo_3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorDatasetCSV {

    public List<RegistroDato> leer(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            throw new ResultadoSimulacionInvalidoException("La ruta del archivo CSV no puede estar vacía.");
        }

        List<RegistroDato> registros = new ArrayList<>();
        int numeroLinea = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // Saltar la primera línea (cabecera)
            if ((linea = reader.readLine()) != null) {
                numeroLinea++;
            }

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                try {
                    RegistroDato registro = parsearLinea(linea, numeroLinea);
                    registros.add(registro);
                } catch (Exception e) {
                    System.err.println("[LECTOR CSV] ⚠ Error en línea " + numeroLinea + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            throw new ResultadoSimulacionInvalidoException("No se pudo leer el archivo: " + rutaArchivo, e);
        }

        if (registros.isEmpty()) {
            throw new ResultadoSimulacionInvalidoException("No se encontraron registros válidos en el CSV.");
        }

        System.out.println("[LECTOR CSV] ✅ Éxito: " + registros.size() + " registros cargados desde " + rutaArchivo);
        return registros;
    }

    private RegistroDato parsearLinea(String linea, int numeroLinea) {
        String[] partes = linea.split(",");

        if (partes.length < 14) {
            throw new IllegalArgumentException("Línea " + numeroLinea + " tiene pocas columnas: " + partes.length);
        }

        try {
            return new RegistroDato(
                    Double.parseDouble(partes[0].trim()),
                    Double.parseDouble(partes[1].trim()),
                    Double.parseDouble(partes[2].trim()),
                    Double.parseDouble(partes[3].trim()),
                    Double.parseDouble(partes[4].trim()),
                    Double.parseDouble(partes[5].trim()),
                    Double.parseDouble(partes[6].trim()),
                    Double.parseDouble(partes[7].trim()),
                    Double.parseDouble(partes[8].trim()),
                    Double.parseDouble(partes[9].trim()),
                    Boolean.parseBoolean(partes[10].trim()),
                    Integer.parseInt(partes[11].trim()),
                    Boolean.parseBoolean(partes[12].trim()),
                    Integer.parseInt(partes[13].trim())
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error de formato numérico en línea " + numeroLinea, e);
        }
    }
}