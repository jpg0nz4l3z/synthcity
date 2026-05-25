package org.synthcity.modulo_4.repository;

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
import org.synthcity.modulo_4.DatabaseManager;
import org.synthcity.modulo_4.FormatoSalidaException;
import org.synthcity.modulo_4.Persistible;

/**
 * Orquestador central de la capa de datos. Implementa el contrato del Sprint 3,
 * ejecutando el Orden Obligatorio de Guardado y Carga de forma transaccional.
 */
public class ResultadoRepository implements Persistible<ResultadoEvaluacion> {

    private final DatabaseManager dbManager;
    private final CiudadRepository ciudadRepository;
    private final BloqueRepository bloqueRepository;
    private final SimulacionRepository simulacionRepository;
    private final CicloRepository cicloRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final PrediccionRepository prediccionRepository;

    /**
     * Constructor unificado. Instancia la suite de repositorios enlazados al mismo dbManager.
     */
    public ResultadoRepository(DatabaseManager dbManager) {
        if (dbManager == null) {
            throw new FormatoSalidaException("DatabaseManager no puede ser nulo.");
        }
        this.dbManager = dbManager;
        this.bloqueRepository = new BloqueRepository(dbManager);
        this.ciudadRepository = new CiudadRepository(dbManager, this.bloqueRepository);
        this.simulacionRepository = new SimulacionRepository(dbManager);
        this.cicloRepository = new CicloRepository(dbManager);
        this.evaluacionRepository = new EvaluacionRepository(dbManager);
        this.prediccionRepository = new PrediccionRepository(dbManager);
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

    /**
     * ENTRADA PRINCIPAL GUI: Guarda de forma atómica todo el ecosistema de la simulación.
     * CORRECCIÓN: Se propaga la 'conn' activa a todos los sub-repositorios para salvaguardar el entorno ACID.
     */
    public void guardarResultado(Ciudad ciudad, ResultadoEvaluacion evaluacion, PredictionResult prediccion) {
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
            conn.setAutoCommit(false); // CORRECCIÓN: Inicio real de la transacción unificada

            // 1. ORDEN DE GUARDADO: Guardar cabecera de la Ciudad (Pasando la conexión transaccional)
            long ciudadId = ciudadRepository.guardarCiudad(conn, ciudad.getNombre(), ciudad.getFilas(), ciudad.getColumnas());
            if (ciudadId == -1) throw new SQLException("Error al obtener ID de la ciudad.");

            // 2. ORDEN DE GUARDADO: Guardar los bloques componentes del grid en bucle masivo
            for (int f = 0; f < ciudad.getFilas(); f++) {
                for (int c = 0; c < ciudad.getColumnas(); c++) {
                    String tipoBloque = ciudad.getTipoBloque(f, c);
                    boolean estaActivo = ciudad.isBloqueActivo(f, c);
                    // CORRECCIÓN: Se inyecta la 'conn' para que escriba sobre la misma transacción
                    bloqueRepository.guardarBloque(conn, ciudadId, f, c, tipoBloque, estaActivo);
                }
            }

            // 3. ORDEN DE GUARDADO: Guardar cabecera global de la Simulación
            long simulacionId = simulacionRepository.guardarCabeceraSimulacion(conn, ciudadId, 1);
            if (simulacionId == -1) throw new SQLException("Error al obtener ID de la simulación.");

            // 4. ORDEN DE GUARDADO: Guardar detalle de variables analíticas del ciclo
            String estadoTexto = "Densidad: " + metrica.getDensidad() +
                    " | Actividad: " + metrica.getPorcentajeActivos() +
                    " | Energía: " + metrica.getRatioEnergetico() +
                    " | Expansión Ejecutada: " + evaluacion.isExpansionEjecutada();
            cicloRepository.guardarCiclosDeSimulacion(conn, simulacionId, 1, estadoTexto);

            // 5. ORDEN DE GUARDADO: Guardar Evaluación
            long evaluacionId = evaluacionRepository.guardarEvaluacion(conn, simulacionId,
                    evaluacion.getNivelEvaluacion().name(),
                    evaluacion.getScoreViabilidad(),
                    evaluacion.getMensaje());
            if (evaluacionId == -1) throw new SQLException("Error al obtener ID de la evaluación.");

            // 6. ORDEN DE GUARDADO: Detectar procedencia del predictor y guardar Predicción
            String tipoPredictor = "HEURISTICA";
            if (prediccion.getMensajePrediccion() != null &&
                    (prediccion.getMensajePrediccion().toLowerCase().contains("weka") ||
                            prediccion.getMensajePrediccion().toLowerCase().contains("ml"))) {
                tipoPredictor = "ML";
            }

            prediccionRepository.guardarPrediccion(conn, evaluacionId,
                    tipoPredictor,
                    prediccion.getTendenciaPredicha().name(),
                    prediccion.getScorePredicho(),
                    prediccion.getMensajePrediccion(),
                    null);

            conn.commit(); // CORRECCIÓN: Se consolidan todos los repositorios a la vez
            System.out.println("[Persistencia Central] Transacción relacional consolidada con éxito absoluto.");

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); System.out.println("[Persistencia Central] Rollback ejecutado correctamente."); } catch (SQLException ex) { /* Ignorar */ }
            }
            throw new FormatoSalidaException("Fallo crítico en la transacción unificada de guardado.", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { /* Ignorar */ }
            }
        }
    }

    /**
     * RECUPERACIÓN COMPLETA REQUERIDA (ORDEN OBLIGATORIO DE CARGA):
     * Extrae los datos de las tablas relacionales y rehidrata el objeto Ciudad dejándolo operativo.
     */
    public Ciudad cargarCiudadCompletaDesdeBD(long idCiudad) {
        String sqlCiudad = "SELECT * FROM ciudad WHERE id = ?";
        Ciudad ciudadInstanciada = null;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlCiudad)) {

            ps.setLong(1, idCiudad);
            try (ResultSet rs = ps.executeQuery()) {
                // PASO 1 y 2 DEL ORDEN: Leer cabecera y dimensionar
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    int filas = rs.getInt("filas");
                    int columnas = rs.getInt("columnas");

                    ciudadInstanciada = new Ciudad(nombre, filas, columnas);
                } else {
                    throw new FormatoSalidaException("La ciudad con ID [" + idCiudad + "] no existe en el sistema.");
                }
            }

            // CORRECCIÓN: Sincronización exacta con las firmas y tipos de tu clase CiudadRepository
            List<BloqueRepository.DatosBloqueDTO> celdas = bloqueRepository.cargarBloquesDeCiudad(idCiudad);

            // PASO 4 DEL ORDEN: Reconstruir la matriz asociando cada bloque a su celda correspondiente
            for (BloqueRepository.DatosBloqueDTO celda : celdas) {
                ciudadInstanciada.colocarBloque(celda.x, celda.y, celda.tipo);
                if (!celda.activo) {
                    ciudadInstanciada.desactivarBloque(celda.x, celda.y);
                }
            }

            // COMPROBACIÓN EXTREMA SPRINT 4 (Contrato de Integración):
            ciudadInstanciada.recalcularTipoEstructural();

            System.out.println("[Persistencia Central] Ciudad '" + ciudadInstanciada.getNombre() + "' completamente rehidratada y simulable.");

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error crítico al ejecutar el orden imperativo de carga de la ciudad.", e);
        }

        return ciudadInstanciada;
    }

    /**
     * CONSULTA GUI: Recupera la traza del último registro ingresado combinando las tablas jerárquicas.
     */
    public String obtenerUltimoResultado() {
        String sql = "SELECT c.nombre, e.nivel, e.score, p.tipo, p.tendencia, p.score AS score_p " +
                "FROM ciudad c " +
                "JOIN simulacion s ON c.id = s.ciudad_id " +
                "JOIN evaluacion e ON s.id = e.simulacion_id " +
                "JOIN prediccion p ON e.id = p.evaluacion_id " +
                "ORDER BY c.id DESC LIMIT 1";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return "=== ULTIMO RESULTADO RECUPERADO RELACIONAL ===\n"
                        + "Ciudad: " + rs.getString("nombre") + "\n"
                        + "Nivel: " + rs.getString("nivel") + "\n"
                        + "Score: " + rs.getDouble("score") + "\n"
                        + "Motor Predictor: " + rs.getString("tipo") + "\n"
                        + "Tendencia Predicha: " + rs.getString("tendencia") + "\n"
                        + "Score Predicho: " + rs.getDouble("score_p") + "\n"
                        + "===============================================";
            }
            return "No hay registros previos en la base de datos.";
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al consultar el último resultado unificado.", e);
        }
    }

    /**
     * CONSULTA GUI: Muestra el histórico completo ordenado de forma cronológica descendente.
     */
    public String listarResultadosBasicos() {
        String sql = "SELECT c.nombre, e.nivel, e.score, p.tendencia, s.fecha " +
                "FROM ciudad c " +
                "JOIN simulacion s ON c.id = s.ciudad_id " +
                "JOIN evaluacion e ON s.id = e.simulacion_id " +
                "JOIN prediccion p ON e.id = p.evaluacion_id " +
                "ORDER BY s.fecha DESC";

        StringBuilder sb = new StringBuilder("HISTORIAL DE SIMULACIONES INTEGRADO:\n");

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
                sb.append("No hay registros guardados en la infraestructura.\n");
            }
            return sb.toString();

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al listar el histórico consolidado.", e);
        }
    }

    /**
     * VOLCADO DE HISTORIAL (MÓDULO 2): Enlaza el fin de la simulación iterativa.
     */
    public void guardarHistorial(ResultadoSimulacion historial, String nombreCiudad) {
        if (historial == null || nombreCiudad == null || nombreCiudad.isBlank()) {
            throw new IllegalArgumentException("Parámetros inválidos para registrar el historial.");
        }

        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            long ciudadId = -1;
            String sqlBuscar = "SELECT id FROM ciudad WHERE nombre = ? ORDER BY id DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sqlBuscar)) {
                ps.setString(1, nombreCiudad);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) ciudadId = rs.getLong("id");
                }
            }

            if (ciudadId != -1) {
                // CORRECCIÓN: Se pasa la conexión al repositorio correspondiente
                simulacionRepository.guardarCabeceraSimulacion(conn, ciudadId, historial.getCiclosEjecutados());
                System.out.println("[Persistencia Central] Historial de simulación iterativo enlazado con éxito.");
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al registrar el volcado del historial del Módulo 2.", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { /* Ignorar */ }
            }
        }
    }
}