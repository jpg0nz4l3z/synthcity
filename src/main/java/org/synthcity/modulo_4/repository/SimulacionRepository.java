package org.synthcity.modulo_4.repository;

import org.synthcity.modulo_4.DatabaseManager;
import org.synthcity.modulo_4.FormatoSalidaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio especializado encargado de gestionar las cabeceras de las simulaciones urbanas.
 */
public class SimulacionRepository {

    private final DatabaseManager dbManager;

    public SimulacionRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * ALINEACIÓN S4: Guarda la cabecera utilizando la conexión activa de la transacción.
     */
    public long guardarCabeceraSimulacion(Connection conn, long ciudadId, int ciclosTotales) throws SQLException {
        String sql = "INSERT INTO simulacion (ciudad_id, ciclos) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, ciudadId);
            ps.setInt(2, ciclosTotales);
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
     * Carga histórica independiente por ID de ciudad.
     */
    public List<Long> cargarIdsSimulacionesPorCiudad(long ciudadId) {
        String sql = "SELECT id FROM simulacion WHERE ciudad_id = ? ORDER BY fecha DESC";
        List<Long> ids = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, ciudadId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error JDBC al consultar el histórico de simulaciones.", e);
        }
        return ids;
    }
}