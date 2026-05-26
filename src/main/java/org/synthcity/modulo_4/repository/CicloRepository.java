package org.synthcity.modulo_4.repository;

import org.synthcity.modulo_4.DatabaseManager;
import org.synthcity.modulo_4.FormatoSalidaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio especializado encargado del volcado analítico estructurado de cada ciclo iterativo.
 */
public class CicloRepository {

    private final DatabaseManager dbManager;

    public CicloRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * ALINEACIÓN S4: Registra el ciclo de forma secuencial compartiendo la conexión transaccional.
     */
    public void guardarCiclosDeSimulacion(Connection conn, long simulacionId, int numeroCiclo, String estadoSerializado) throws SQLException {
        String sql = "INSERT INTO ciclo (simulacion_id, numero, estado) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, simulacionId);
            ps.setInt(2, numeroCiclo);
            ps.setString(3, estadoSerializado);
            ps.executeUpdate();
        }
    }

    /**
     * Recupera el histórico secuencial para el simulador.
     */
    public List<String> cargarEstadosCiclos(long simulacionId) {
        String sql = "SELECT estado FROM ciclo WHERE simulacion_id = ? ORDER BY numero ASC";
        List<String> estados = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, simulacionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    estados.add(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error JDBC al extraer la secuencia cronológica de ciclos.", e);
        }
        return estados;
    }
}