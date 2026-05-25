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
 * Repositorio encargado exclusivamente de la persistencia de los bloques de la ciudad.
 * Mantiene la integridad referencial vinculando cada celda con su ciudad_id.
 */
public class BloqueRepository {

    private final DatabaseManager dbManager;

    public BloqueRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * ALINEACIÓN: Guarda un bloque individual utilizando la conexión de la transacción activa.
     * Renombrado a 'guardarBloque' para sincronizarse perfectamente con ResultadoRepository.
     */
    public void guardarBloque(Connection conn, long ciudadId, int x, int y, String tipo, boolean activo) throws SQLException {
        String sql = "INSERT INTO bloque (ciudad_id, x, y, tipo, activo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ciudadId);
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setString(4, tipo);
            ps.setBoolean(5, activo);
            ps.executeUpdate();
        }
    }

    /**
     * Recupera todos los bloques pertenecientes a una ciudad específica.
     * Sincronizado con la rehidratación del mapa de celdas en el Orden de Carga.
     */
    public List<DatosBloqueDTO> cargarBloquesDeCiudad(long ciudadId) {
        String sql = "SELECT x, y, tipo, activo FROM bloque WHERE ciudad_id = ?";
        List<DatosBloqueDTO> listaBloques = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, ciudadId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaBloques.add(new DatosBloqueDTO(
                            rs.getInt("x"),
                            rs.getInt("y"),
                            rs.getString("tipo"),
                            rs.getBoolean("activo")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al extraer la matriz de bloques desde la base de datos.", e);
        }
        return listaBloques;
    }

    /**
     * Clase estática interna de transferencia de datos (DTO) para reconstruir el grid urbano.
     */
    public static class DatosBloqueDTO {
        public final int x;
        public final int y;
        public final String tipo;
        public final boolean activo;

        public DatosBloqueDTO(int x, int y, String tipo, boolean activo) {
            this.x = x;
            this.y = y;
            this.tipo = tipo;
            this.activo = activo;
        }
    }
}