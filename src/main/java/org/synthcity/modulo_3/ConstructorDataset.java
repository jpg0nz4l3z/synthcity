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
        if (dato == null) {
            throw new ResultadoSimulacionInvalidoException("No se puede agregar un registro nulo.");
        }
        this.registros.add(dato);
    }

    public void exportarCSV(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isBlank()) {
            throw new ResultadoSimulacionInvalidoException("La ruta del archivo no puede ser nula o vacía.");
        }

        if (registros.isEmpty()) {
            throw new ResultadoSimulacionInvalidoException("El dataset está vacío, no se puede exportar.");
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            // Nombres de las columnas
            writer.println("densidad,ratioEnergetico,ratioCoberturaServicios,contaminacion,contaminacionAcumulada,estabilidadMedia,tendenciaEstabilidad,tendenciaContaminacion,bienestar,scoreViabilidad,colapsoDetectado,ciclosEjecutados,saturacionDetectada,objetivo");

            // Recorrer los registros y escribirlos
            for (RegistroDato registro : registros) {
                StringBuilder linea = new StringBuilder();

                // Features numéricas
                double[] features = registro.toArray();
                for (int i = 0; i < features.length; i++) {
                    linea.append(features[i]).append(",");
                }
                linea.append(registro.getObjetivo());
                writer.println(linea.toString());
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al exportar el CSV.", e);
        }
    }


    public List<RegistroDato> getDataset() {
        return List.copyOf(this.registros);
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
