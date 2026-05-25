package org.synthcity.modulo_4.repository;

import org.synthcity.modulo_4.DatabaseManager;
import org.synthcity.modulo_4.FormatoSalidaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RankingRepository {

    private final DatabaseManager dbManager;

    public RankingRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void guardarRanking(String contenidoRanking) {
        if (contenidoRanking == null || contenidoRanking.isBlank()) {
            return;
        }
        String sql = "INSERT INTO ranking (contenido) VALUES (?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, contenidoRanking);
            ps.executeUpdate();
            System.out.println("[Persistencia S4] Historial de ranking global consolidado en base de datos.");

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error de persistencia JDBC al registrar el ranking de simulación.", e);
        }
    }

    public String obtenerUltimoRanking() {
        String sql = "SELECT contenido, fecha FROM ranking ORDER BY id DESC LIMIT 1";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return "=== RANKING HISTÓRICO (" + rs.getTimestamp("fecha") + ") ===\n"
                        + rs.getString("contenido");
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error de consulta JDBC al recuperar el último ranking consolidado.", e);
        }
        return "No existen rankings guardados en el sistema.";
    }
}