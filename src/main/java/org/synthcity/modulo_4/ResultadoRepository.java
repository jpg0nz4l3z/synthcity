package org.synthcity.modulo_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultadoRepository {

    private final DatabaseManager dbManager;

    public ResultadoRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }


    // Inserta un registro completo en la base de datos
    // Este metodo guarda el analisis visible y las variables tecnicas para futuro uso

    public void guardarResultado(Ciudad ciudad, ResultadoEvaluacion evaluacion, PredictionResult prediccion) {

        String sql = "INSERT INTO resultados (nombre_ciudad, nivel_evaluacion, score_viabilidad, " +
                "mensaje_evaluacion, tendencia_predicha, score_predicho, mensaje_prediccion, " +
                "densidad, porcentaje_actividad, ratio_energetico, ratio_cobertura, " +
                "contaminacion, estabilidad_basica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 1. Identificacion y Evaluacion (Planos estructural y analitico)
            ps.setString(1, ciudad.getNombre());
            ps.setString(2, evaluacion.getNivel().toString());
            ps.setDouble(3, evaluacion.getScore());
            ps.setString(4, evaluacion.getMensaje());

            // 2. Resultado de la Capa Predictiva
            ps.setString(5, prediccion.getTendencia());
            ps.setDouble(6, prediccion.getScorePredicho());
            ps.setString(7, prediccion.getMensaje());

            // 3. Variables Analiticas (Soporte para futura prediccion basada en datos)
            // Asegurate de que estos getters existan en tu modelo de Ciudad
            ps.setDouble(8, ciudad.getDensidad());
            ps.setDouble(9, ciudad.getPorcentajeActividad());
            ps.setDouble(10, ciudad.getRatioEnergetico());
            ps.setDouble(11, ciudad.getRatioCoberturaServicios());
            ps.setDouble(12, ciudad.getNivelContaminacion());
            ps.setDouble(13, ciudad.getEstabilidadBasica());

            ps.executeUpdate();
            System.out.println("[JDBC] Registro realizado: El resultado ha sido persistido.");

        } catch (SQLException e) {
            System.err.println("[JDBC] Error en la insercion de datos: " + e.getMessage());
        }
    }

    public void obtenerUltimoResultado() {
        String sql = "SELECT * FROM resultados ORDER BY id DESC LIMIT 1";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("=== ULTIMO RESULTADO RECUPERADO ===");
                System.out.println("Ciudad: " + rs.getString("nombre_ciudad"));
                System.out.println("Score: " + rs.getDouble("score_viabilidad"));
                System.out.println("Tendencia: " + rs.getString("tendencia_predicha"));
                System.out.println("Densidad Guardada: " + rs.getDouble("densidad"));
                System.out.println("===================================");
            } else {
                System.out.println("No hay registros previos en la base de datos.");
            }

        } catch (SQLException e) {
            System.err.println("Error al recuperar el ultimo resultado: " + e.getMessage());
        }
    }

    public void listarResultadosBasicos() {
        String sql = "SELECT nombre_ciudad, nivel_evaluacion, score_viabilidad, fecha_registro FROM resultados";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("HISTORIAL DE SIMULACIONES:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("nombre_ciudad") +
                        " | Nivel: " + rs.getString("nivel_evaluacion") +
                        " | Score: " + rs.getDouble("score_viabilidad") +
                        " | Fecha: " + rs.getTimestamp("fecha_registro"));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar resultados: " + e.getMessage());
        }
    }
}