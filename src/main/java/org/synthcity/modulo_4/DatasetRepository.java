package org.synthcity.modulo_4;

import org.synthcity.modulo_3.RegistroDato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatasetRepository implements Persistible<RegistroDato> {

    private final DatabaseManager dbManager;

    public DatasetRepository(DatabaseManager dbManager) {
        if (dbManager == null) {
            throw new FormatoSalidaException("DatabaseManager no puede ser null.");
        }
        this.dbManager = dbManager;
    }

    // =========================================================================
    // OPERACIONES OBLIGATORIAS SPRINT 4 (Metadatos Estructurales del Dataset)
    // =========================================================================

    public long guardarReferencia(String rutaCsv, int columnas, int registros, String variableObjetivo) {
        String sql = "INSERT INTO dataset_referencia (ruta_csv, columnas, registros, objetivo) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, rutaCsv);
            ps.setInt(2, columnas);
            ps.setInt(3, registros);
            ps.setString(4, variableObjetivo);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al registrar los metadatos de control del dataset (S4).", e);
        }
        return -1;
    }

    public long obtenerIdPorRuta(String rutaCsv) {
        String sql = "SELECT id FROM dataset_referencia WHERE ruta_csv = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rutaCsv);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("[DatasetRepository] Error al buscar id por ruta: " + e.getMessage());
        }
        return -1;
    }

    // =========================================================================
    // LÓGICA DE REGISTROS HEREDADA DEL SPRINT 3 (Se preserva intacta)
    // =========================================================================

    @Override
    public void guardar(RegistroDato dato) {
        guardarRegistro(dato, "Ciudad_Anonima");
    }

    @Override
    public List<RegistroDato> listar() {
        return listarRegistros();
    }

    public List<RegistroDato> listarRegistros() {
        String sql = "SELECT * FROM dataset_registros ORDER BY id DESC LIMIT 100";
        List<RegistroDato> registros = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                registros.add(new RegistroDato(
                        rs.getDouble("densidad"),
                        rs.getDouble("ratio_energetico"),
                        rs.getDouble("ratio_cobertura_servicios"),
                        rs.getDouble("contaminacion"),
                        rs.getDouble("contaminacion_acumulada"),
                        rs.getDouble("estabilidad_media"),
                        rs.getDouble("tendencia_estabilidad"),
                        rs.getDouble("tendencia_contaminacion"),
                        rs.getDouble("bienestar"),
                        rs.getDouble("score_viabilidad"),
                        rs.getBoolean("colapso_detectado"),
                        rs.getInt("ciclos_ejecutados"),
                        rs.getBoolean("saturacion_detectada"),
                        Integer.parseInt(rs.getString("objetivo"))
                ));
            }
            return registros;
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al listar registros del dataset.", e);
        }
    }

    public void guardarRegistro(RegistroDato dato, String nombreCiudad) {
        if (dato == null) {
            throw new IllegalArgumentException("RegistroDato no puede ser null.");
        }
        if (nombreCiudad == null || nombreCiudad.isBlank()) {
            throw new IllegalArgumentException("El nombre de la ciudad es obligatorio.");
        }

        String sql = "INSERT INTO dataset_registros (" +
                "nombre_ciudad, densidad, ratio_energetico, ratio_cobertura_servicios, " +
                "contaminacion, contaminacion_acumulada, estabilidad_media, " +
                "tendencia_estabilidad, tendencia_contaminacion, bienestar, score_viabilidad, " +
                "colapso_detectado, ciclos_ejecutados, saturacion_detectada, objetivo" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            rellenarStatement(ps, dato, nombreCiudad);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al guardar el registro del dataset.", e);
        }
    }

    public void guardarDataset(List<RegistroDato> registros, String nombreCiudad) {
        if (registros == null) {
            throw new FormatoSalidaException("La lista de registros no puede ser null.");
        }
        if (registros.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO dataset_registros (" +
                "nombre_ciudad, densidad, ratio_energetico, ratio_cobertura_servicios, " +
                "contaminacion, contaminacion_acumulada, estabilidad_media, " +
                "tendencia_estabilidad, tendencia_contaminacion, bienestar, score_viabilidad, " +
                "colapso_detectado, ciclos_ejecutados, saturacion_detectada, objetivo" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (RegistroDato registro : registros) {
                if (registro == null) {
                    throw new FormatoSalidaException("El dataset no puede contener registros nulos.");
                }
                rellenarStatement(ps, registro, nombreCiudad);
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al guardar el dataset.", e);
        }
    }

    public int contarRegistros() {
        String sql = "SELECT COUNT(*) FROM dataset_registros";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al contar registros del dataset.", e);
        }
    }

    public List<String> listarRegistrosBasicos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre_ciudad, score_viabilidad, objetivo, fecha_registro " +
                "FROM dataset_registros ORDER BY id DESC LIMIT 50";
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

    private void rellenarStatement(PreparedStatement ps, RegistroDato registro, String nombreCiudad) throws SQLException {
        ps.setString(1, nombreCiudad);
        ps.setDouble(2, registro.getDensidad());
        ps.setDouble(3, registro.getRatioEnergetico());
        ps.setDouble(4, registro.getRatioCoberturaServicios());
        ps.setDouble(5, registro.getContaminacion());
        ps.setDouble(6, registro.getContaminacionAcumulada());
        ps.setDouble(7, registro.getEstabilidadMedia());
        ps.setDouble(8, registro.getTendenciaEstabilidad());
        ps.setDouble(9, registro.getTendenciaContaminacion());
        ps.setDouble(10, registro.getBienestar());
        ps.setDouble(11, registro.getScoreViabilidad());
        ps.setBoolean(12, registro.isColapsoDetectado());
        ps.setInt(13, registro.getCiclosEjecutados());
        ps.setBoolean(14, registro.isSaturacionDetectada());
        ps.setString(15, String.valueOf(registro.getObjetivo()));
    }
}