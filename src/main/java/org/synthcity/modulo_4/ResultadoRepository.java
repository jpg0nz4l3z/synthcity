package org.synthcity.modulo_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.prediccion.PredictionResult;
import org.synthcity.modulo_3.ResultadoEvaluacion;

public class ResultadoRepository {

    private final DatabaseManager dbManager;

    public ResultadoRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void guardarResultado(Ciudad ciudad, ResultadoEvaluacion evaluacion, PredictionResult prediccion) {
        if (dbManager == null) {
            throw new IllegalStateException("DatabaseManager no puede ser null para guardar resultados.");
        }
        if (ciudad == null || evaluacion == null || prediccion == null) {
            throw new IllegalArgumentException("Ciudad, evaluacion y prediccion son obligatorias para persistir.");
        }

        MetricaCiudad metrica = evaluacion.getMetricaCiudad();

        if (metrica == null) {
            throw new IllegalArgumentException("La evaluación no contiene métricas para persistir.");
        }

        String sql = "INSERT INTO resultados (nombre_ciudad, nivel_evaluacion, score_viabilidad, "
                + "mensaje_evaluacion, tendencia_predicha, score_predicho, mensaje_prediccion, "
                + "densidad, porcentaje_actividad, ratio_energetico, ratio_cobertura, "
                + "contaminacion, estabilidad_basica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, evaluacion.getNombreCiudad());
            ps.setString(2, evaluacion.getNivelEvaluacion().name());
            ps.setDouble(3, evaluacion.getScoreViabilidad());
            ps.setString(4, evaluacion.getMensaje());
            ps.setString(5, prediccion.getTendenciaPredicha().name());
            ps.setDouble(6, prediccion.getScorePredicho());
            ps.setString(7, prediccion.getMensajePrediccion());
            ps.setDouble(8, metrica.getDensidad());
            ps.setDouble(9, metrica.getPorcentajeActivos());
            ps.setDouble(10, metrica.getRatioEnergetico());
            ps.setDouble(11, metrica.getRatioCoberturaServicios());
            ps.setDouble(12, metrica.getContaminacion());
            ps.setDouble(13, metrica.getEstabilidadBasica());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al guardar el resultado en base de datos.", e);
        }
    }

    public String obtenerUltimoResultado() {
        if (dbManager == null) {
            throw new IllegalStateException("DatabaseManager no puede ser null para consultar resultados.");
        }

        String sql = "SELECT * FROM resultados ORDER BY id DESC LIMIT 1";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return "=== ULTIMO RESULTADO RECUPERADO ===\n"
                        + "Ciudad: " + rs.getString("nombre_ciudad") + "\n"
                        + "Nivel: " + rs.getString("nivel_evaluacion") + "\n"
                        + "Score: " + rs.getDouble("score_viabilidad") + "\n"
                        + "Tendencia: " + rs.getString("tendencia_predicha") + "\n"
                        + "Score predicho: " + rs.getDouble("score_predicho") + "\n"
                        + "Densidad guardada: " + rs.getDouble("densidad") + "\n"
                        + "Actividad guardada: " + rs.getDouble("porcentaje_actividad") + "\n"
                        + "Ratio energetico: " + rs.getDouble("ratio_energetico") + "\n"
                        + "Ratio cobertura: " + rs.getDouble("ratio_cobertura") + "\n"
                        + "Contaminacion: " + rs.getDouble("contaminacion") + "\n"
                        + "Estabilidad: " + rs.getDouble("estabilidad_basica") + "\n"
                        + "Fecha: " + rs.getTimestamp("fecha_registro") + "\n"
                        + "===================================";
            }

            return "No hay registros previos en la base de datos.";

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al recuperar el ultimo resultado.", e);
        }
    }

    public String listarResultadosBasicos() {
        if (dbManager == null) {
            throw new IllegalStateException("DatabaseManager no puede ser null para listar resultados.");
        }

        String sql = "SELECT nombre_ciudad, nivel_evaluacion, score_viabilidad, tendencia_predicha, fecha_registro "
                + "FROM resultados ORDER BY fecha_registro DESC";

        StringBuilder sb = new StringBuilder("HISTORIAL DE SIMULACIONES:\n");

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean hayResultados = false;

            while (rs.next()) {
                hayResultados = true;
                sb.append("- ")
                        .append(rs.getString("nombre_ciudad"))
                        .append(" | Nivel: ")
                        .append(rs.getString("nivel_evaluacion"))
                        .append(" | Score: ")
                        .append(rs.getDouble("score_viabilidad"))
                        .append(" | Tendencia: ")
                        .append(rs.getString("tendencia_predicha"))
                        .append(" | Fecha: ")
                        .append(rs.getTimestamp("fecha_registro"))
                        .append("\n");
            }

            if (!hayResultados) {
                sb.append("No hay registros guardados.\n");
            }

            return sb.toString();

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al listar resultados.", e);
        }
    }
}
