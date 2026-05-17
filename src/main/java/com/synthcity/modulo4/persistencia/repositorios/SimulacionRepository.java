package com.synthcity.modulo4.persistencia.repositorios;

import com.synthcity.modulo4.persistencia.ConexionBD;
import com.synthcity.modulo4.persistencia.entidades.SimulacionEntidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimulacionRepository {

    public SimulacionEntidad guardar(SimulacionEntidad simulacion) throws SQLException {
        String sql = "INSERT INTO simulacion (ciudad_id, ciclos, fecha) VALUES (?, ?, ?)";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, simulacion.getCiudadId());
            ps.setInt(2, simulacion.getCiclos());
            ps.setTimestamp(3, simulacion.getFecha() != null ? simulacion.getFecha() : new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    simulacion.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al guardar simulación: No se obtuvo el ID generado.");
                }
            }
        }
        return simulacion;
    }

    public List<SimulacionEntidad> buscarPorCiudadId(Long ciudadId) throws SQLException {
        List<SimulacionEntidad> simulaciones = new ArrayList<>();
        String sql = "SELECT id, ciudad_id, ciclos, fecha FROM simulacion WHERE ciudad_id = ? ORDER BY fecha DESC";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ciudadId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    simulaciones.add(new SimulacionEntidad(
                            rs.getLong("id"),
                            rs.getLong("ciudad_id"),
                            rs.getInt("ciclos"),
                            rs.getTimestamp("fecha")
                    ));
                }
            }
        }
        return simulaciones;
    }
}