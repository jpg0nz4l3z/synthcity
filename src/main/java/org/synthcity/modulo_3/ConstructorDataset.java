package org.synthcity.modulo_3;

import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ConstructorDataset {

    // Los datos del Sprint 3
    private final List<RegistroDato> registros;

    public ConstructorDataset() {
        this.registros = new ArrayList<>();
    }

    public void agregarRegistro(RegistroDato dato) {
        if (dato != null) {
            this.registros.add(dato);
        }
    }
    public void exportarCSV(String rutaArchivo) {
        if (registros.isEmpty()) {
            System.out.println("El dataset está vacío, no hay nada que exportar.");
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            // Nombres de las columnas
            writer.println("densidad,ratioEnergetico,ratioCoberturaServicios,contaminacion,contaminacionAcumulada,estabilidadMedia,tendenciaEstabilidad,tendenciaContaminacion,bienestar,scoreViabilidad,colapsoDetectado,ciclosEjecutados,saturacionDetectada,objetivo");

            // Recorrer los registros y escribirlos
            for (RegistroDato registro : registros) {
                StringBuilder linea = new StringBuilder();

                // Features numéricas
                double[] features = registro.toArray();
                for (double feature : features) {
                    linea.append(feature).append(",");
                }

                // Variable objetivo al final y línea
                linea.append(registro.getObjetivo());
                writer.println(linea.toString());
            }
            System.out.println("Dataset exportado correctamente a: " + rutaArchivo);

        } catch (IOException e) {
            System.err.println("Error al exportar el CSV: " + e.getMessage());
        }
    }


    public List<RegistroDato> getDataset() {
        return this.registros;
    }

    public int getTamano() {
        return this.registros.size();
    }

    public boolean estaVacio() {
        return this.registros.isEmpty();
    }

    public void limpiar() {
        this.registros.clear();
    }
}
