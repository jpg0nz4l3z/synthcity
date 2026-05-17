package com.synthcity.modulo4.persistencia.repositorios;

import com.synthcity.modulo4.persistencia.ConexionBD;
import com.synthcity.modulo4.persistencia.entidades.PrediccionEntidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrediccionRepository {

    public PrediccionEntidad guardar(PrediccionEntidad prediccion) throws SQLException {
        String sql = "INSERT INTO prediccion (evaluacion_id, tipo, tendencia, score, mensaje, modelo_id) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, prediccion.getEvaluacionId());
            ps.setString(2, prediccion.getTipo());
            ps.setString(3, prediccion.getTendencia());
            ps.setDouble(4, prediccion.getScore());
            ps.setString(5, prediccion.getMensaje());

            if (prediccion.getModeloId() != null) {
                ps.setLong(6, prediccion.getModeloId());
            } else {
                ps.setNull(6, Types.BIGINT);
            }

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    prediccion.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al guardar predicción: No se obtuvo el ID autogenerado.");
                }
            }
        }
        return prediccion;
    }

    public List<PrediccionEntidad> buscarPorEvaluacionId(Long evaluacionId) throws SQLException {
        List<PrediccionEntidad> predicciones = new ArrayList<>();
        String sql = "SELECT id, evaluacion_id, tipo, tendencia, score, mensaje, modelo_id FROM prediccion WHERE evaluacion_id = ?";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, evaluacionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long modeloIdVal = rs.getLong("modelo_id");
                    if (rs.wasNull()) {
                        modeloIdVal = null;
                    }
                    predicciones.add(new PrediccionEntidad(
                            rs.getLong("id"),
                            rs.getLong("evaluacion_id"),
                            rs.getString("tipo"),
                            rs.getString("tendencia"),
                            rs.getDouble("score"),
                            rs.getString("mensaje"),
                            modeloIdVal
                    ));
                }
            }
        }
        return predicciones;
    }
}