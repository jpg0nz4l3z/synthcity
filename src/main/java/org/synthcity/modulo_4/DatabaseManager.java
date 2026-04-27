package org.synthcity.modulo_4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    // Se añade el parámetro para que MySQL cree la base de datos si no existe
    private static final String URL = "jdbc:mysql://localhost:3311/synthcity_db?createDatabaseIfNotExist=true";
    private static final String USER = "synthcity";
    private static final String PASSWORD = "syn123";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void inicializarTablaResultados() {
        // La tabla incluye los campos de evaluación, predicción y variables analíticas
        String sql = "CREATE TABLE IF NOT EXISTS resultados (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nombre_ciudad VARCHAR(100), " +
                "nivel_evaluacion VARCHAR(50), " +
                "score_viabilidad DOUBLE, " +
                "mensaje_evaluacion TEXT, " +
                "tendencia_predicha VARCHAR(50), " +
                "score_predicho DOUBLE, " +
                "mensaje_prediccion TEXT, " +
                "densidad DOUBLE, " +
                "porcentaje_actividad DOUBLE, " +
                "ratio_energetico DOUBLE, " +
                "ratio_cobertura DOUBLE, " +
                "contaminacion DOUBLE, " +
                "estabilidad_basica DOUBLE, " +
                "fue_expandida BOOLEAN DEFAULT FALSE, " +
                "fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        ejecutarDDL(sql, "Tabla 'resultados' inicializada.");
    }
    public void inicializarTablaHistorial() {
        String sql = "CREATE TABLE IF NOT EXISTS historial_simulacion (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nombre_ciudad VARCHAR(100), " +
                "ciclos_ejecutados INT, " +
                "motivo_parada VARCHAR(50), " +
                "estabilidad_media DOUBLE, " +
                "contaminacion_acumulada DOUBLE, " +
                "equilibrio_energetico_ultimo INT, " +
                "tipo_estructural VARCHAR(50), " +
                "fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        ejecutarDDL(sql, "Tabla 'historial_simulacion' inicializada.");
    }
    public void inicializarTablaDataset() {
        String sql = "CREATE TABLE IF NOT EXISTS dataset (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nombre_ciudad VARCHAR(100), " +
                "densidad DOUBLE, " +
                "ratio_energetico DOUBLE, " +
                "cobertura_servicios DOUBLE, " +
                "contaminacion DOUBLE, " +
                "estabilidad DOUBLE, " +
                "score_viabilidad DOUBLE, " +
                "objetivo INT, " +
                "fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        ejecutarDDL(sql, "Tabla 'dataset' inicializada.");
    }
    private void ejecutarDDL(String sql, String mensajeOk) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
            System.out.println("[JDBC] Base de datos e infraestructura preparadas correctamente.");

        } catch (SQLException e) {
            System.err.println("[JDBC] Error en la inicialización: " + e.getMessage());
        }
    }
}