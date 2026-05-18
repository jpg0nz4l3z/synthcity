package org.synthcity.modulo_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_3.prediccion.PredictionResult;


public class ResultadoRepository implements Persistible<ResultadoEvaluacion> {

    private final DatabaseManager dbManager;

    public ResultadoRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void guardar(ResultadoEvaluacion evaluacion) {
        if (evaluacion == null) {
            throw new FormatoSalidaException("La evaluación no puede ser null.");
        }
        throw new FormatoSalidaException(
                "Para guardar una evaluación completa usa guardarResultado(ciudad, evaluacion, prediccion)."
        );
    }

    @Override
    public List<ResultadoEvaluacion> listar() {
        throw new FormatoSalidaException(
                "No se puede reconstruir ResultadoEvaluacion completo desde la estructura relacional de forma directa."
        );
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

        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false); // Activación de transacción atómica para evitar estados corruptos [cite: 1008]

            // 1. ORDEN DE GUARDADO: Guardar Ciudad [cite: 1014]
            long ciudadId = -1;
            String sqlCiudad = "INSERT INTO ciudad (nombre, filas, columnas) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlCiudad, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, evaluacion.getNombreCiudad());
                ps.setInt(2, ciudad.getFilas());
                ps.setInt(3, ciudad.getColumnas());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        ciudadId = rs.getLong(1);
                    }
                }
            }

            // 2. ORDEN DE GUARDADO: Historial de Expansiones (Si aplica) [cite: 1016-1017]
            if (evaluacion.isExpansionEjecutada()) {
                String sqlExp = "INSERT INTO expansion (ciudad_id, dimensiones_anteriores, dimensiones_posteriores, resultado_motivo) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlExp)) {
                    ps.setLong(1, ciudadId);
                    ps.setString(2, "Estructura Base");
                    ps.setString(3, ciudad.getFilas() + "x" + ciudad.getColumnas());
                    ps.setString(4, "Expansión automática por saturación de densidad.");
                    ps.executeUpdate();
                }
            }

            // 3. ORDEN DE GUARDADO: Cabecera de Simulación [cite: 1018]
            long simulacionId = -1;
            String sqlSim = "INSERT INTO simulacion (ciudad_id, ciclos) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlSim, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, ciudadId);
                ps.setInt(2, 1); // Iteración de control del flujo actual
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        simulacionId = rs.getLong(1);
                    }
                }
            }

            // 4. ORDEN DE GUARDADO: Detalle de Ciclos individuales con variables de entrada [cite: 524, 1019]
            String sqlCiclo = "INSERT INTO ciclo (simulacion_id, numero, estado) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlCiclo)) {
                ps.setLong(1, simulacionId);
                ps.setInt(2, 1);
                // Serializamos las variables de entrada del modelo para cumplir el escenario de validación [cite: 524]
                String estadoMetricas = "Densidad: " + metrica.getDensidad() +
                        " | Actividad: " + metrica.getPorcentajeActivos() +
                        " | Energía: " + metrica.getRatioEnergetico() +
                        " | Cobertura: " + metrica.getRatioCoberturaServicios() +
                        " | Contaminación: " + metrica.getContaminacion() +
                        " | Estabilidad: " + metrica.getEstabilidadBasica();
                ps.setString(3, estadoMetricas);
                ps.executeUpdate();
            }

            // 5. ORDEN DE GUARDADO: Guardar Evaluación [cite: 1020]
            long evaluacionId = -1;
            String sqlEval = "INSERT INTO evaluacion (simulacion_id, nivel, score, mensaje) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEval, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, simulacionId);
                ps.setString(2, evaluacion.getNivelEvaluacion().name());
                ps.setDouble(3, evaluacion.getScoreViabilidad());
                ps.setString(4, evaluacion.getMensaje());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        evaluacionId = rs.getLong(1);
                    }
                }
            }

            // 6. REQUISITO EXPLICITO PASO 2: Guardar predicción identificando el predictor activo
            String sqlPred = "INSERT INTO prediccion (evaluacion_id, tipo, tendencia, score, mensaje, modelo_id) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlPred)) {
                ps.setLong(1, evaluacionId);

                // Detección del tipo de predictor basándonos en el mensaje o metadatos de negocio [cite: 927]
                String tipoPredictor = "HEURISTICO";
                if (prediccion.getMensajePrediccion() != null &&
                        (prediccion.getMensajePrediccion().toLowerCase().contains("weka") ||
                                prediccion.getMensajePrediccion().toLowerCase().contains("ml"))) {
                    tipoPredictor = "ML";
                }

                ps.setString(2, tipoPredictor); // Almacenamiento directo del tipo de predictor
                ps.setString(3, prediccion.getTendenciaPredicha().name());
                ps.setDouble(4, prediccion.getScorePredicho());
                ps.setString(5, prediccion.getMensajePrediccion());
                ps.setNull(6, java.sql.Types.BIGINT); // ID del modelo Weka (vinculado en el Paso 3)
                ps.executeUpdate();
            }

            conn.commit(); // Consolidación de los cambios relacionales [cite: 1009]

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { /* Silenciar */ }
            }
            throw new FormatoSalidaException("Error crítico en la transacción jerárquica de guardado.", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { /* Silenciar */ }
            }
        }
    }

    public String obtenerUltimoResultado() {
        if (dbManager == null) {
            throw new IllegalStateException("DatabaseManager no puede ser null para consultar resultados.");
        }

        String sql = "SELECT c.nombre, e.nivel, e.score, p.tipo, p.tendencia, p.score AS score_p, p.mensaje " +
                "FROM ciudad c " +
                "JOIN simulacion s ON c.id = s.ciudad_id " +
                "JOIN evaluacion e ON s.id = e.simulacion_id " +
                "JOIN prediccion p ON e.id = p.evaluacion_id " +
                "ORDER BY c.id DESC LIMIT 1";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return "=== ULTIMO RESULTADO RECUPERADO ===\n"
                        + "Ciudad: " + rs.getString("nombre") + "\n"
                        + "Nivel: " + rs.getString("nivel") + "\n"
                        + "Score: " + rs.getDouble("score") + "\n"
                        + "Predictor Originario: " + rs.getString("tipo") + "\n"
                        + "Tendencia: " + rs.getString("tendencia") + "\n"
                        + "Score predicho: " + rs.getDouble("score_p") + "\n"
                        + "Mensaje: " + rs.getString("mensaje") + "\n"
                        + "===================================";
            }
            return "No hay registros previos en la base de datos.";

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al recuperar el último resultado estructurado.", e);
        }
    }


    public String listarResultadosBasicos() {
        if (dbManager == null) {
            throw new IllegalStateException("DatabaseManager no puede ser null para listar resultados.");
        }

        String sql = "SELECT c.nombre, e.nivel, e.score, p.tendencia, s.fecha " +
                "FROM ciudad c " +
                "JOIN simulacion s ON c.id = s.ciudad_id " +
                "JOIN evaluacion e ON s.id = e.simulacion_id " +
                "JOIN prediccion p ON e.id = p.evaluacion_id " +
                "ORDER BY s.fecha DESC";

        StringBuilder sb = new StringBuilder("HISTORIAL DE SIMULACIONES:\n");

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                sb.append("- ")
                        .append(rs.getString("nombre"))
                        .append(" | Nivel: ")
                        .append(rs.getString("nivel"))
                        .append(" | Score: ")
                        .append(rs.getDouble("score"))
                        .append(" | Tendencia: ")
                        .append(rs.getString("tendencia"))
                        .append(" | Fecha: ")
                        .append(rs.getTimestamp("fecha"))
                        .append("\n");
            }

            if (!hayResultados) {
                sb.append("No hay registros guardados.\n");
            }
            return sb.toString();

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al listar resultados relacionales.", e);
        }
    }

    public void guardarHistorial(ResultadoSimulacion historial, String nombreCiudad) {
        if (historial == null) {
            throw new IllegalArgumentException("El historial de simulación no puede ser null.");
        }
        if (nombreCiudad == null || nombreCiudad.isBlank()) {
            throw new IllegalArgumentException("El nombre de la ciudad no puede ser null ni vacío.");
        }

        // Enlaza la simulación a la última versión registrada de la ciudad para evitar desalineación [cite: 253]
        String sqlSim = "INSERT INTO simulacion (ciudad_id, ciclos) VALUES ((SELECT id FROM ciudad WHERE nombre = ? ORDER BY id DESC LIMIT 1), ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlSim)) {

            ps.setString(1, nombreCiudad);
            ps.setInt(2, historial.getCiclosEjecutados());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al guardar el historial segregado de simulación.", e);
        }
    }
}