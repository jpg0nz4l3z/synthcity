package org.synthcity.modulo_4.repository;

import org.synthcity.modulo_4.DatabaseManager;
import org.synthcity.modulo_4.FormatoSalidaException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ModeloWekaRepository {

    private final DatabaseManager dbManager;

    public ModeloWekaRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void guardarModelo(Object clasificadorWeka, String rutaModelo, String rutaEstructura, long datasetId, String algoritmo) {
        if (clasificadorWeka == null) {
            throw new IllegalArgumentException("El clasificador de Weka no puede ser null.");
        }

        // 1. Almacenamiento físico del archivo binario .model en disco [cite: 465]
        File archivo = new File(rutaModelo);
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs(); // Asegura la existencia de /data/modelo
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(clasificadorWeka);
            System.out.println("[Persistencia ML] Objeto binario serializado con éxito en: " + rutaModelo);
        } catch (IOException e) {
            throw new FormatoSalidaException("Error de entrada/salida al guardar el archivo binario .model.", e);
        }

        // 2. Registro de metadatos de control en la base de datos relacional [cite: 947-951]
        String sql = "INSERT INTO modelo_weka (ruta_modelo, ruta_estructura, dataset_id, algoritmo) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rutaModelo);
            ps.setString(2, rutaEstructura);

            if (datasetId > 0) {
                ps.setLong(3, datasetId);
            } else {
                ps.setNull(3, Types.BIGINT);
            }

            ps.setString(4, algoritmo); // Por ejemplo: "J48" [cite: 29]
            ps.executeUpdate();
            System.out.println("[Persistencia ML] Metadatos del modelo predictivo registrados en base de datos.");

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al registrar el modelo Weka en la base de datos.", e);
        }
    }

    public Object cargarModeloActivo(String rutaModelo) {
        File archivo = new File(rutaModelo);
        if (!archivo.exists()) {
            System.out.println("[Persistencia ML] No se localizó un modelo previo en disco. Flujo de entrenamiento requerido.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            Object clasificador = ois.readObject();
            System.out.println("[Persistencia ML] Modelo persistido recuperado correctamente desde disco sin reentrenar.");
            return clasificador;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            throw new FormatoSalidaException("El archivo del modelo Weka está corrupto o es ilegible.", e);
        }
    }
}