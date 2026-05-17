package com.synthcity.modulo4.persistencia.repositorios;

import com.synthcity.modulo4.persistencia.ConexionBD;
import com.synthcity.modulo4.persistencia.entidades.BloqueEntidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BloqueRepository {

    public void guardarBloques(List<BloqueEntidad> bloques) throws SQLException {
        String sql = "INSERT INTO bloque (ciudad_id, tipo, x, y, activo) VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConexion();

        // Desactivamos auto-commit para asegurar atomicidad en el lote de bloques
        boolean autoCommitOriginal = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (BloqueEntidad bloque : bloques) {
                ps.setLong(1, bloque.getCiudadId());
                ps.setString(2, bloque.getTipo());
                ps.setInt(3, bloque.getX());
                ps.setInt(4, bloque.getY());
                ps.setBoolean(5, bloque.isActivo());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(autoCommitOriginal);
        }
    }

    public List<BloqueEntidad> buscarPorCiudadId(Long ciudadId) throws SQLException {
        List<BloqueEntidad> bloques = new ArrayList<>();
        String sql = "SELECT id, ciudad_id, tipo, x, y, activo FROM bloque WHERE ciudad_id = ?";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ciudadId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bloques.add(new BloqueEntidad(
                            rs.getLong("id"),
                            rs.getLong("ciudad_id"),
                            rs.getString("tipo"),
                            rs.getInt("x"),
                            rs.getInt("y"),
                            rs.getBoolean("activo")
                    ));
                }
            }
        }
        return bloques;
    }
}