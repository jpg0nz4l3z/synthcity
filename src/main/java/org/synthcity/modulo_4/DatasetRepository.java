package org.synthcity.modulo_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatasetRepository implements Persistible {

    private final DatabaseManager dbManager;

    public DatasetRepository(DatabaseManager dbManager) {
        if (dbManager == null) {
            throw new IllegalArgumentException("DatabaseManager no puede ser null.");
        }
        this.dbManager = dbManager;
    }

    //  Implementación de Persistible

    @Override
    public void guardar(Object dato) {
        if (!(dato instanceof RegistroDato rd)) {
            throw new IllegalArgumentException(
                    "DatasetRepository.guardar() solo acepta RegistroDato.");
        }
        guardarRegistro(rd, rd.getNombreCiudad());
    }

    @Override
    public List<String> listar() {
        return listarRegistrosBasicos();
    }

    //Operaciones de escritura

    public void guardarRegistro(RegistroDato dato, String nombreCiudad) {
        if (dato == null) {
            throw new IllegalArgumentException("RegistroDato no puede ser null.");
        }
        if (nombreCiudad == null || nombreCiudad.isBlank()) {
            throw new IllegalArgumentException("El nombre de la ciudad es obligatorio.");
        }

        String sql = "INSERT INTO dataset " +
                "(nombre_ciudad, densidad, ratio_energetico, cobertura_servicios, " +
                "contaminacion, estabilidad, score_viabilidad, objetivo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        double[] v = dato.toArray();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombreCiudad);
            ps.setDouble(2, v[0]); // densidad
            ps.setDouble(3, v[1]); // ratio_energetico
            ps.setDouble(4, v[2]); // cobertura_servicios
            ps.setDouble(5, v[3]); // contaminacion
            ps.setDouble(6, v[4]); // estabilidad
            ps.setDouble(7, v[5]); // score_viabilidad
            ps.setInt(8, dato.getObjetivo());

            ps.executeUpdate();
            System.out.println("[JDBC] RegistroDato guardado para: " + nombreCiudad);

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al guardar RegistroDato.", e);
        }
    }


    public void guardarDataset(List<RegistroDato> registros, String nombreCiudad) {
        if (registros == null || registros.isEmpty()) {
            System.out.println("[JDBC] Lista vacía — no se guarda nada.");
            return;
        }

        String sql = "INSERT INTO dataset " +
                "(nombre_ciudad, densidad, ratio_energetico, cobertura_servicios, " +
                "contaminacion, estabilidad, score_viabilidad, objetivo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (RegistroDato dato : registros) {
                    double[] v = dato.toArray();
                    ps.setString(1, nombreCiudad);
                    ps.setDouble(2, v[0]); ps.setDouble(3, v[1]);
                    ps.setDouble(4, v[2]); ps.setDouble(5, v[3]);
                    ps.setDouble(6, v[4]); ps.setDouble(7, v[5]);
                    ps.setInt(8, dato.getObjetivo());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            System.out.println("[JDBC] Dataset guardado en transacción: "
                    + registros.size() + " registros.");

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
            throw new FormatoSalidaException("Error en transacción dataset. Rollback ejecutado.", e);
        } finally {
            try {
                if (conn != null) { conn.setAutoCommit(true); conn.close(); }
            } catch (SQLException ex) { /* ignorar */ }
        }
    }

    // Consultas

    public int contarRegistros() {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM dataset");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al contar registros del dataset.", e);
        }
    }


    public List<String> listarRegistrosBasicos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre_ciudad, score_viabilidad, objetivo, fecha_registro " +
                "FROM dataset ORDER BY id DESC LIMIT 50";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("nombre_ciudad") +
                        " | Score: " + String.format("%.2f", rs.getDouble("score_viabilidad")) +
                        " | Obj: " + rs.getInt("objetivo") +
                        " | " + rs.getTimestamp("fecha_registro"));
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al listar dataset.", e);
        }
        return lista;
    }
}