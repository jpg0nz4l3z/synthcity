package org.synthcity.modulo_4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3311/synthcity?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "synthcity";
    private static final String PASSWORD = "syn123";

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL JDBC ('com.mysql.cj.jdbc.Driver') no encontrado en el classpath.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void inicializarTablaResultados() {
        // 1. Tabla Ciudad (Módulo 1) - Cabecera básica de dimensiones
        String sqlCiudad = "CREATE TABLE IF NOT EXISTS ciudad (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "nombre VARCHAR(100), " +
                "filas INT, " +
                "columnas INT" +
                ")";
        ejecutarDDL(sqlCiudad, "Tabla 'ciudad' inicializada con éxito.");

        // 2. Tabla Bloque (Módulo 1) - Relación jerárquica con clave foránea en cascada
        String sqlBloque = "CREATE TABLE IF NOT EXISTS bloque (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "ciudad_id BIGINT, " +
                "tipo VARCHAR(50), " +
                "x INT, " +
                "y INT, " +
                "activo BOOLEAN, " +
                "FOREIGN KEY (ciudad_id) REFERENCES ciudad(id) ON DELETE CASCADE" +
                ")";
        ejecutarDDL(sqlBloque, "Tabla 'bloque' vinculada mediante clave foránea en cascada.");

        // 3. Tabla Evaluación (Módulo 3) - Guarda scores de viabilidad agregados
        String sqlEvaluacion = "CREATE TABLE IF NOT EXISTS evaluacion (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "simulacion_id BIGINT, " +
                "nivel VARCHAR(50), " +
                "score DOUBLE, " +
                "mensaje TEXT, " +
                "FOREIGN KEY (simulacion_id) REFERENCES simulacion(id) ON DELETE CASCADE" +
                ")";
        ejecutarDDL(sqlEvaluacion, "Tabla 'evaluacion' estructurada correctamente.");

        // 4. Tabla Predicción (Módulo 3/4) - Separada de la evaluación con columna identificadora de predictor
        String sqlPrediccion = "CREATE TABLE IF NOT EXISTS prediccion (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "evaluacion_id BIGINT, " +
                "tipo VARCHAR(50), " + // Guarda textualmente 'HEURISTICO' o 'ML'
                "tendencia VARCHAR(50), " +
                "score DOUBLE, " +
                "mensaje TEXT, " +
                "modelo_id BIGINT, " +
                "FOREIGN KEY (evaluacion_id) REFERENCES evaluacion(id) ON DELETE CASCADE" +
                ")";
        ejecutarDDL(sqlPrediccion, "Tabla 'prediccion' preparada para trazabilidad de caja negra.");

        // 5. Tabla Ranking (Módulo 4) - Almacenamiento histórico de clasificaciones procesadas
        String sqlRanking = "CREATE TABLE IF NOT EXISTS ranking (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "contenido TEXT, " +
                "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        ejecutarDDL(sqlRanking, "Tabla 'ranking' desplegada.");
    }

    public void inicializarTablaHistorial() {
        // 6. Tabla Cabecera Simulación (Módulo 2)
        String sqlSimulacion = "CREATE TABLE IF NOT EXISTS simulacion (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "ciudad_id BIGINT, " +
                "ciclos INT, " +
                "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (ciudad_id) REFERENCES ciudad(id) ON DELETE CASCADE" +
                ")";
        ejecutarDDL(sqlSimulacion, "Tabla 'simulacion' configurada.");

        // 7. Tabla Detalle de Ciclos (Módulo 2) - Almacena las variables analíticas ciclo por ciclo
        String sqlCiclo = "CREATE TABLE IF NOT EXISTS ciclo (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "simulacion_id BIGINT, " +
                "numero INT, " +
                "estado TEXT, " + // Contiene cadena de variables analíticas (energía, contaminación, servicios, etc.)
                "FOREIGN KEY (simulacion_id) REFERENCES simulacion(id) ON DELETE CASCADE" +
                ")";
        ejecutarDDL(sqlCiclo, "Tabla 'ciclo' de detalle segregado desplegada.");

        // 8. Tabla Historial Expansiones (Módulo 1) - Previene mezclas indeseadas pre y post expansión
        String sqlExpansion = "CREATE TABLE IF NOT EXISTS expansion (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "ciudad_id BIGINT, " +
                "dimensiones_anteriores VARCHAR(50), " +
                "dimensiones_posteriores VARCHAR(50), " +
                "resultado_motivo TEXT, " +
                "FOREIGN KEY (ciudad_id) REFERENCES ciudad(id) ON DELETE CASCADE" +
                ")";
        ejecutarDDL(sqlExpansion, "Tabla 'expansion' preparada para control de saltos de grid.");
    }

    public void inicializarTablaDataset() {
        // 9. Tabla Dataset Referencia (Módulo 3/4) - Almacena propiedades estructurales, no registros individuales
        String sqlRef = "CREATE TABLE IF NOT EXISTS dataset_referencia (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "ruta_csv VARCHAR(255), " +
                "columnas INT, " +
                "registros INT, " +
                "objetivo VARCHAR(50)" +
                ")";

        // 10. Tabla Modelo Weka (Módulo 3/4) - Mapea metadatos de los clasificadores binarios persistidos en disco
        String sqlModelo = "CREATE TABLE IF NOT EXISTS modelo_weka (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "ruta_modelo VARCHAR(255), " +
                "ruta_estructura VARCHAR(255), " +
                "dataset_id BIGINT, " +
                "algoritmo VARCHAR(50), " +
                "FOREIGN KEY (dataset_id) REFERENCES dataset_referencia(id) ON DELETE SET NULL" +
                ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlRef);
            stmt.execute(sqlModelo);
            System.out.println("[JDBC] Infraestructura de control de Machine Learning integrada correctamente.");

        } catch (SQLException e) {
            // Se mantiene el uso de la excepción personalizada del equipo
            throw new FormatoSalidaException("Error crítico al inicializar las estructuras de metadatos del dataset.", e);
        }
    }

    private void ejecutarDDL(String sql, String mensajeOk) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("[JDBC] " + mensajeOk);
        } catch (SQLException e) {
            System.err.println("[JDBC] Error en la inicialización relacional: " + e.getMessage());
        }
    }
}