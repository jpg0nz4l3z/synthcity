package org.synthcity.modulo_4.persistencia.repositorios;

import org.synthcity.modulo_4.persistencia.ConexionBD;
import org.synthcity.modulo_4.persistencia.entidades.EvaluacionEntidad;
import java.sql.*;

public class EvaluacionRepository {

    public EvaluacionEntidad guardar(EvaluacionEntidad evaluacion) throws SQLException {
        String sql = "INSERT INTO evaluacion (simulacion_id, nivel, score, mensaje) VALUES (?, ?, ?, ?)";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, evaluacion.getSimulacionId());
            ps.setString(2, evaluacion.getNivel());
            ps.setDouble(3, evaluacion.getScore());
            ps.setString(4, evaluacion.getMensaje());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    evaluacion.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al guardar evaluación: No se obtuvo el ID autogenerado.");
                }
            }
        }
        return evaluacion;
    }

    public EvaluacionEntidad buscarPorSimulacionId(Long simulacionId) throws SQLException {
        String sql = "SELECT id, simulacion_id, nivel, score, mensaje FROM evaluacion WHERE simulacion_id = ?";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, simulacionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EvaluacionEntidad(
                            rs.getLong("id"),
                            rs.getLong("simulacion_id"),
                            rs.getString("nivel"),
                            rs.getDouble("score"),
                            rs.getString("mensaje")
                    );
                }
            }
        }
        return null;
    }
}