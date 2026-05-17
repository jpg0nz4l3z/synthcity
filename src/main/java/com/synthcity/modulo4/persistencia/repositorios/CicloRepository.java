package com.synthcity.modulo4.persistencia.repositorios;

import com.synthcity.modulo4.persistencia.ConexionBD;
import com.synthcity.modulo4.persistencia.entidades.CicloEntidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CicloRepository {

    public void guardarCiclos(List<CicloEntidad> ciclos) throws SQLException {
        String sql = "INSERT INTO ciclo (simulacion_id, numero, estado) VALUES (?, ?, ?)";
        Connection conn = ConexionBD.getConexion();

        boolean autoCommitOriginal = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (CicloEntidad ciclo : ciclos) {
                ps.setLong(1, ciclo.getSimulacionId());
                ps.setInt(2, ciclo.getNumero());
                ps.setString(3, ciclo.getEstado());
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

    public List<CicloEntidad> buscarPorSimulacionId(Long simulacionId) throws SQLException {
        List<CicloEntidad> ciclos = new ArrayList<>();
        String sql = "SELECT id, simulacion_id, numero, estado FROM ciclo WHERE simulacion_id = ? ORDER BY numero ASC";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, simulacionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ciclos.add(new CicloEntidad(
                            rs.getLong("id"),
                            rs.getLong("simulacion_id"),
                            rs.getInt("numero"),
                            rs.getString("estado")
                    ));
                }
            }
        }
        return ciclos;
    }
}