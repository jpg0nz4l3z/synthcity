package org.synthcity.modulo_4.repository;

import org.synthcity.modulo_4.DatabaseManager;
import org.synthcity.modulo_4.FormatoSalidaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Repositorio especializado encargado de registrar las estimaciones futuras y su motor originario.
 */
public class PrediccionRepository {

    private final DatabaseManager dbManager;

    public PrediccionRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * ALINEACIÓN S4: Persiste el resultado predictivo integrándose a la transacción controlada activa.
     */
    public void guardarPrediccion(Connection conn, long evaluacionId, String tipoPredictor, String tendencia, double scorePredicho, String mensaje, Long modeloId) throws SQLException {
        String sql = "INSERT INTO prediccion (evaluacion_id, tipo, tendencia, score, mensaje, modelo_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, evaluacionId);
            ps.setString(2, tipoPredictor.toUpperCase()); // Guarda 'HEURISTICA' o 'ML' conforme al script de tu BD
            ps.setString(3, tendencia);
            ps.setDouble(4, scorePredicho);
            ps.setString(5, mensaje);

            if (modeloId != null && modeloId > 0) {
                ps.setLong(6, modeloId);
            } else {
                ps.setNull(6, Types.BIGINT); // Nulo controlado si el motor es heurístico
            }

            ps.executeUpdate();
        }
    }
}