package org.synthcity.modulo_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.ResultadoEvaluacion;


public class ResultadoRepository implements Persistible {

    private final DatabaseManager dbManager;

    public ResultadoRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    @Override
    public void guardar(Object dato) {
        if (dato instanceof ResultadoSimulacion rs) {
            guardarHistorial(rs, rs.getNombreCiudad());
        } else if (dato instanceof Object[] arr
                && arr.length >= 2
                && arr[0] instanceof Ciudad c
                && arr[1] instanceof ResultadoEvaluacion e) {
            guardarResultado(c, e);
        } else {
            throw new IllegalArgumentException(
                    "ResultadoRepository.guardar() recibió un tipo no soportado.");
        }
    }

    @Override
    public List<String> listar() {
        List<String> lista = new ArrayList<>();
        if (dbManager == null) return lista;
        String sql = "SELECT nombre_ciudad, nivel_evaluacion, score_viabilidad, " +
                "fue_expandida, fecha_registro " +
                "FROM resultados ORDER BY id DESC LIMIT 50";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("nombre_ciudad") +
                        " | Nivel: " + rs.getString("nivel_evaluacion") +
                        " | Score: " + rs.getDouble("score_viabilidad") +
                        " | Expandida: " + rs.getBoolean("fue_expandida") +
                        " | " + rs.getTimestamp("fecha_registro"));
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al listar resultados.", e);
        }
        return lista;
    }

    public void guardarResultado(Ciudad ciudad, ResultadoEvaluacion evaluacion ) {
        if (dbManager == null) {
            throw new IllegalStateException("DatabaseManager no puede ser null para guardar resultados.");
        }
        if (ciudad == null || evaluacion == null ) {
            throw new IllegalArgumentException("Ciudad y evaluacion son obligatorias para persistir.");
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

            ps.setString(1, ciudad.getNombre());
            ps.setString(2, evaluacion.getNivelEvaluacion().name());
            ps.setDouble(3, evaluacion.getScoreViabilidad());
            ps.setString(4, evaluacion.getMensaje());
            ps.setDouble(5, metrica.getDensidad());
            ps.setDouble(6, metrica.getPorcentajeActivos());
            ps.setDouble(7, metrica.getRatioEnergetico());
            ps.setDouble(8, metrica.getRatioCoberturaServicios());
            ps.setDouble(9, metrica.getContaminacion());
            ps.setDouble(10, metrica.getEstabilidadBasica());
            ps.setBoolean(11, evaluacion.fueExpandida());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al guardar el resultado en base de datos.", e);
        }
    }
    public void guardarHistorial(ResultadoSimulacion historial, String nombreCiudad) {
        if (historial == null) {
            throw new IllegalArgumentException("El historial no puede ser null.");
        }
        if (nombreCiudad == null || nombreCiudad.isBlank()) {
            throw new IllegalArgumentException("El nombre de la ciudad es obligatorio.");
        }

        String sql = "INSERT INTO historial_simulacion (" +
                "nombre_ciudad, ciclos_ejecutados, motivo_parada, " +
                "estabilidad_media, contaminacion_acumulada, " +
                "equilibrio_energetico_ultimo, tipo_estructural" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombreCiudad);
            ps.setInt(2, 1); // Sprint 3: 1 ciclo. Sprint 4 usará getCiclos().size()
            ps.setString(3, historial.getEstadoSimulacion().name());
            ps.setDouble(4, historial.getEstabilidadBasica());
            ps.setInt(5, historial.getContaminacion());
            ps.setInt(6, historial.getEquilibrioEnergetico());
            ps.setString(7, historial.getTipoEstructural().name());

            ps.executeUpdate();
            System.out.println("[JDBC] Historial guardado para: " + nombreCiudad);

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al guardar historial.", e);
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

    public void guardarHistorial(ResultadoSimulacion historial, String nombreCiudad) {
        if (historial == null) {
            throw new IllegalArgumentException("El historial no puede ser null.");
        }

        String sql = "INSERT INTO historial_simulacion (" +
                "nombre_ciudad, ciclos_ejecutados, motivo_parada, " +
                "estabilidad_media, contaminacion_acumulada, " +
                "equilibrio_energetico_ultimo, tipo_estructural" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombreCiudad);
            ps.setInt(2, 1);
            ps.setString(3, historial.getEstadoSimulacion().name());
            ps.setDouble(4, historial.getEstabilidadBasica());
            ps.setInt(5, historial.getContaminacion());
            ps.setInt(6, historial.getEquilibrioEnergetico());
            ps.setString(7, historial.getTipoEstructural().name());

            ps.executeUpdate();
            System.out.println("[JDBC] Historial guardado para: " + nombreCiudad);

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al guardar historial.", e);
        }
    }
}

