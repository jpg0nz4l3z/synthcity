package org.synthcity.modulo_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.PredictionResult;
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
        String sql = "INSERT INTO resultados (nombre_ciudad, nivel_evaluacion, score_viabilidad, "
                + "mensaje_evaluacion, tendencia_predicha, score_predicho, mensaje_prediccion, "
                + "densidad, porcentaje_actividad, ratio_energetico, ratio_cobertura, "
                + "contaminacion, estabilidad_basica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ciudad.getNombre());
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
            System.out.println("[JDBC] Resultado persistido correctamente.");
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al guardar el resultado en base de datos.", e);
        }
    }

    public void obtenerUltimoResultado() {
        if (dbManager == null) {
            throw new IllegalStateException("DatabaseManager no puede ser null para consultar resultados.");
        }

        String sql = "SELECT * FROM resultados ORDER BY id DESC LIMIT 1";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("=== ULTIMO RESULTADO RECUPERADO ===");
                System.out.println("Ciudad: " + rs.getString("nombre_ciudad"));
                System.out.println("Score: " + rs.getDouble("score_viabilidad"));
                System.out.println("Tendencia: " + rs.getString("tendencia_predicha"));
                System.out.println("Densidad guardada: " + rs.getDouble("densidad"));
                System.out.println("===================================");
            } else {
                System.out.println("No hay registros previos en la base de datos.");
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al recuperar el ultimo resultado.", e);
        }
    }

    public void listarResultadosBasicos() {
        if (dbManager == null) {
            throw new IllegalStateException("DatabaseManager no puede ser null para listar resultados.");
        }

        String sql = "SELECT nombre_ciudad, nivel_evaluacion, score_viabilidad, fecha_registro FROM resultados";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("HISTORIAL DE SIMULACIONES:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("nombre_ciudad")
                        + " | Nivel: " + rs.getString("nivel_evaluacion")
                        + " | Score: " + rs.getDouble("score_viabilidad")
                        + " | Fecha: " + rs.getTimestamp("fecha_registro"));
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al listar resultados.", e);
        }
    }
}
