package org.synthcity.modulo_4.persistencia.repositorios;

import org.synthcity.modulo_4.persistencia.ConexionBD;
import org.synthcity.modulo_4.persistencia.entidades.RankingEntidad;
import java.sql.*;

public class RankingRepository {

    public RankingEntidad guardar(RankingEntidad ranking) throws SQLException {
        String sql = "INSERT INTO ranking (contenido, fecha) VALUES (?, ?)";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ranking.getContenido());
            ps.setTimestamp(2, ranking.getFecha() != null ? ranking.getFecha() : new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ranking.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al guardar ranking: No se obtuvo el ID autogenerado.");
                }
            }
        }
        return ranking;
    }

    public RankingEntidad buscarUltimoRanking() throws SQLException {
        String sql = "SELECT id, contenido, fecha FROM ranking ORDER BY fecha DESC LIMIT 1";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new RankingEntidad(
                        rs.getLong("id"),
                        rs.getString("contenido"),
                        rs.getTimestamp("fecha")
                );
            }
        }
        return null;
    }
}