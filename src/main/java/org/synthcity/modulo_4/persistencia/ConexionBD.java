package org.synthcity.modulo_4.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    // Parámetros de configuración obligatorios del sistema
    private static final String URL = "jdbc:mysql://localhost:3306/synthcity";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Ajustado según credenciales de desarrollo

    private static Connection conexion = null;

    // Constructor privado para evitar instanciación externa (Pattern Singleton)
    private ConexionBD() {}

    /**
     * Proporciona la instancia única de conexión a la base de datos MySQL.
     * @return Connection objeto de conexión activo.
     * @throws SQLException si ocurre un error en el handshake o las credenciales fallan.
     */
    public static synchronized Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                // Forzar la carga del driver de MySQL 8.x
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[INFRAESTRUCTURA] Conexión establecida con éxito a synthcity.");
            } catch (ClassNotFoundException e) {
                System.err.println("[ERROR] No se encontró el driver JDBC de MySQL.");
                throw new SQLException("Driver no disponible", e);
            }
        }
        return conexion;
    }

    /**
     * Cierra de forma segura la conexión al apagar la aplicación.
     */
    public static synchronized void cerrarConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                    System.out.println("[INFRAESTRUCTURA] Conexión a la base de datos cerrada de manera segura.");
                }
            } catch (SQLException e) {
                System.err.println("[ERROR] Error al cerrar la conexión de la base de datos: " + e.getMessage());
            }
        }
    }
}