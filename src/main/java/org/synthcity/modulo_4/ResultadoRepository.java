package org.synthcity.modulo_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 1. Identificacion y Evaluacion (Planos estructural y analitico)
            pstmt.setString(1, ciudad.getNombre());
            pstmt.setString(2, evaluacion.getNivel().toString());
            pstmt.setDouble(3, evaluacion.getScore());
            pstmt.setString(4, evaluacion.getMensaje());

            // 2. Resultado de la Capa Predictiva
            pstmt.setString(5, prediccion.getTendencia());
            pstmt.setDouble(6, prediccion.getScorePredicho());
            pstmt.setString(7, prediccion.getMensaje());

            // 3. Variables Analiticas (Soporte para futura prediccion basada en datos)
            // Asegurate de que estos getters existan en tu modelo de Ciudad
            pstmt.setDouble(8, ciudad.getDensidad());
            pstmt.setDouble(9, ciudad.getPorcentajeActividad());
            pstmt.setDouble(10, ciudad.getRatioEnergetico());
            pstmt.setDouble(11, ciudad.getRatioCoberturaServicios());
            pstmt.setDouble(12, ciudad.getNivelContaminacion());
            pstmt.setDouble(13, ciudad.getEstabilidadBasica());

            pstmt.executeUpdate();
            System.out.println("[JDBC] Registro realizado: El resultado ha sido persistido.");

        } catch (SQLException e) {
            System.err.println("[JDBC] Error en la insercion de datos: " + e.getMessage());
        }
    }
}