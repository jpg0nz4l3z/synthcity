package org.synthcity.modulo_4.repository;

import org.synthcity.modulo_4.DatabaseManager;
import org.synthcity.modulo_4.FormatoSalidaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExpansionRepository {

    private final DatabaseManager dbManager;

    public ExpansionRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void guardarExpansion(long ciudadId, String dimAnterior, String dimPosterior, String motivo) {
        String sql = "INSERT INTO expansion (ciudad_id, dimensiones_anteriores, dimensiones_posteriores, resultado_motivo) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, ciudadId);
            ps.setString(2, dimAnterior);
            ps.setString(3, dimPosterior);
            ps.setString(4, motivo);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error de persistencia JDBC al registrar historial de expansión estructural.", e);
        }
    }
}