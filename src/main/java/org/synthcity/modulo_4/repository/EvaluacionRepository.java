package org.synthcity.modulo_4.repository;

import org.synthcity.modulo_4.DatabaseManager;
import org.synthcity.modulo_4.FormatoSalidaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio especializado encargado de almacenar los scores de viabilidad del sistema.
 */
public class EvaluacionRepository {

    private final DatabaseManager dbManager;

    public EvaluacionRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * ALINEACIÓN S4: Inserta la evaluación y retorna su clave primaria usando la conexión de la transacción.
     */
    public long guardarEvaluacion(Connection conn, long simulacionId, String nivel, double score, String mensaje) throws SQLException {
        String sql = "INSERT INTO evaluacion (simulacion_id, nivel, score, mensaje) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, simulacionId);
            ps.setString(2, nivel);
            ps.setDouble(3, score);
            ps.setString(4, mensaje);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return -1;
    }

    /**
     * Carga las evaluaciones históricas asociadas.
     */
    public List<String> cargarEvaluacionesPorSimulacion(long simulacionId) {
        String sql = "SELECT id, nivel, score, mensaje FROM evaluacion WHERE simulacion_id = ?";
        List<String> evaluaciones = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, simulacionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    evaluaciones.add("ID: " + rs.getLong("id") + " | Nivel: " + rs.getString("nivel") + " | Score: " + rs.getDouble("score"));
                }
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al extraer evaluaciones históricas.", e);
        }
        return evaluaciones;
    }
}