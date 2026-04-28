package org.synthcity.modulo_4;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_3.prediccion.PredictionResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class ResultadoRepositoryJdbcTest {

    @AfterEach
    void limpiarDatosDeTest() throws Exception {
        DatabaseManager db = new DatabaseManager();

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM resultados WHERE nombre_ciudad LIKE 'Jdbc%'"
             )) {
            ps.executeUpdate();
        }
    }

    @Test
    void conexionBaseDatosFunciona() throws Exception {
        DatabaseManager db = new DatabaseManager();

        try (Connection conn = db.getConnection()) {
            assertNotNull(conn);
            assertFalse(conn.isClosed());
        }
    }

    @Test
    void tablaResultadosSeCreaOValidaCorrectamente() {
        DatabaseManager db = new DatabaseManager();

        assertDoesNotThrow(db::inicializarTablaResultados);
    }

    @Test
    void resultadoActualSeGuardaCorrectamente() throws Exception {
        DatabaseManager db = new DatabaseManager();
        db.inicializarTablaResultados();

        ResultadoRepository repo = new ResultadoRepository(db);

        Ciudad ciudad = crearCiudad("JdbcGuardar");
        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);
        ResultadoEvaluacion evaluacion = evaluador.evaluar(simulacion);
        PredictionResult prediccion = evaluador.predecir(simulacion);

        repo.guardarResultado(ciudad, evaluacion, prediccion);

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM resultados WHERE nombre_ciudad = ? ORDER BY id DESC LIMIT 1"
             )) {

            ps.setString(1, "JdbcGuardar");

            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("JdbcGuardar", rs.getString("nombre_ciudad"));
                assertEquals(evaluacion.getNivelEvaluacion().name(), rs.getString("nivel_evaluacion"));
                assertEquals(prediccion.getTendenciaPredicha().name(), rs.getString("tendencia_predicha"));
            }
        }
    }

    @Test
    void registroGuardadoContieneCamposNumericosAnaliticosCorrectos() throws Exception {
        DatabaseManager db = new DatabaseManager();
        db.inicializarTablaResultados();

        ResultadoRepository repo = new ResultadoRepository(db);

        Ciudad ciudad = crearCiudad("JdbcAnalitica");
        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);
        ResultadoEvaluacion evaluacion = evaluador.evaluar(simulacion);
        PredictionResult prediccion = evaluador.predecir(simulacion);

        repo.guardarResultado(ciudad, evaluacion, prediccion);

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM resultados WHERE nombre_ciudad = ? ORDER BY id DESC LIMIT 1"
             )) {

            ps.setString(1, "JdbcAnalitica");

            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());

                assertEquals(evaluacion.getScoreViabilidad(), rs.getDouble("score_viabilidad"), 0.0001);
                assertEquals(prediccion.getScorePredicho(), rs.getDouble("score_predicho"), 0.0001);
                assertEquals(evaluacion.getMetricaCiudad().getDensidad(), rs.getDouble("densidad"), 0.0001);
                assertEquals(evaluacion.getMetricaCiudad().getPorcentajeActivos(), rs.getDouble("porcentaje_actividad"), 0.0001);
                assertEquals(evaluacion.getMetricaCiudad().getRatioEnergetico(), rs.getDouble("ratio_energetico"), 0.0001);
                assertEquals(evaluacion.getMetricaCiudad().getRatioCoberturaServicios(), rs.getDouble("ratio_cobertura"), 0.0001);
                assertEquals(evaluacion.getMetricaCiudad().getContaminacion(), rs.getDouble("contaminacion"), 0.0001);
                assertEquals(evaluacion.getMetricaCiudad().getEstabilidadBasica(), rs.getDouble("estabilidad_basica"), 0.0001);
            }
        }
    }

    @Test
    void obtenerUltimoResultadoDevuelveTextoConCamposBasicos() {
        DatabaseManager db = new DatabaseManager();
        db.inicializarTablaResultados();

        ResultadoRepository repo = new ResultadoRepository(db);

        String ultimo = repo.obtenerUltimoResultado();

        assertNotNull(ultimo);
        assertFalse(ultimo.isBlank());
    }

    @Test
    void listarResultadosBasicosDevuelveTexto() {
        DatabaseManager db = new DatabaseManager();
        db.inicializarTablaResultados();

        ResultadoRepository repo = new ResultadoRepository(db);

        String listado = repo.listarResultadosBasicos();

        assertNotNull(listado);
        assertTrue(listado.contains("HISTORIAL DE SIMULACIONES"));
    }

    private Ciudad crearCiudad(String nombre) {
        Ciudad ciudad = new Ciudad(nombre, 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        return ciudad;
    }
}