package org.synthcity.modulo_4;

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

public class ModeloWekaRepository {

    private final DatabaseManager dbManager;

    public ModeloWekaRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void guardarModeloEntrenado(Object clasificadorWeka, String rutaModelo, String rutaEstructura, long datasetId, String algoritmo) {
        if (clasificadorWeka == null) {
            throw new IllegalArgumentException("El objeto clasificador de Weka no puede ser nulo.");
        }

        // 1. Persistencia física: serialización en archivo binario de disco duro
        File archivo = new File(rutaModelo);
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs(); // Garantiza la creación física de la carpeta /data/modelo
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(clasificadorWeka);
            System.out.println("[Persistencia ML] Fichero binario .model guardado de forma segura en: " + rutaModelo);
        } catch (IOException e) {
            throw new FormatoSalidaException("Error crítico de E/S al escribir el archivo binario del modelo predictivo.", e);
        }

        // 2. Persistencia relacional: almacenamiento en la tabla 'modelo_weka'
        String sql = "INSERT INTO modelo_weka (ruta_modelo, ruta_estructura, dataset_id, algoritmo) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rutaModelo);
            ps.setString(2, rutaEstructura);
            ps.setLong(3, datasetId); // Clave foránea mandatoria hacia 'dataset_referencia'
            ps.setString(4, algoritmo); // Ej: "J48" o "Id3"
            ps.executeUpdate();
            System.out.println("[Persistencia ML] Metadatos de auditoría del modelo registrados en base de datos.");

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al registrar los metadatos de Machine Learning en base de datos.", e);
        }
    }

    public Object cargarModeloExistente(String rutaModelo) {
        File archivo = new File(rutaModelo);
        if (!archivo.exists()) {
            System.out.println("[Persistencia ML] No se localizó un modelo entrenado previo. Se iniciará flujo de entrenamiento base.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            Object clasificador = ois.readObject();
            System.out.println("[Persistencia ML] Caja negra cargada con éxito desde disco duro sin reentrenar.");
            return clasificador;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            throw new FormatoSalidaException("El fichero binario .model está corrupto o es incompatible con el entorno actual.", e);
        }
    }
}