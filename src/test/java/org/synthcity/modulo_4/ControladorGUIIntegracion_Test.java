package org.synthcity.modulo_4;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.*;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControladorGUIIntegracionTest {

    @TempDir
    Path dir;

    private ControladorGUI controlador;

    @BeforeEach
    void setUp() {
        // Sin repositorios reales — tests sin BD ni JavaFX
        controlador = new ControladorGUI(
                new SimuladorCiudad(),
                new EvaluadorCiudad(),
                null,   // resultadoRepository — no se prueba aquí
                null,   // datasetRepository   — no se prueba aquí
                null,   // panelCiudad
                null,   // panelResumen
                null    // panelEvolucion
        );
    }

    // TESTS DEL CSV


    @Test
    @DisplayName("CSV tiene cabecera con los nombres correctos de features")
    void exportarCSV_cabeceraCoincideConFeatureNames() throws IOException {
        inyectarDataset(1);
        Path csv = dir.resolve("cabecera.csv");

        controlador.exportarDatasetCSV(csv.toString());

        String[] cabecera = Files.readAllLines(csv).get(0).split(",");
        assertArrayEquals(
                RegistroDato.getFeatureNames(),
                cabecera,
                "La cabecera debe coincidir exactamente con RegistroDato.getFeatureNames()"
        );
    }

    @Test
    @DisplayName("CSV tiene 1 línea de cabecera + N filas de datos")
    void exportarCSV_numeroDeLineasCorrecto() throws IOException {
        int n = 3;
        inyectarDataset(n);
        Path csv = dir.resolve("lineas.csv");

        controlador.exportarDatasetCSV(csv.toString());

        List<String> lineas = Files.readAllLines(csv);
        assertEquals(n + 1, lineas.size(),
                "Debe haber 1 cabecera + " + n + " filas de datos");
    }

    @Test
    @DisplayName("Cada fila del CSV tiene exactamente 6 valores (= toArray().length)")
    void exportarCSV_cadaFilaTieneSeisCols() throws IOException {
        inyectarDataset(2);
        Path csv = dir.resolve("cols.csv");

        controlador.exportarDatasetCSV(csv.toString());

        List<String> lineas = Files.readAllLines(csv);
        int numFeatures = RegistroDato.getFeatureNames().length; // 6

        for (int i = 1; i < lineas.size(); i++) {
            String[] valores = lineas.get(i).split(",");
            assertEquals(numFeatures, valores.length,
                    "Fila " + i + " debe tener " + numFeatures + " valores");
        }
    }

    @Test
    @DisplayName("CSV con 1 registro produce exactamente 2 líneas")
    void exportarCSV_unRegistro_dosLineas() throws IOException {
        inyectarDataset(1);
        Path csv = dir.resolve("uno.csv");

        controlador.exportarDatasetCSV(csv.toString());

        assertEquals(2, Files.readAllLines(csv).size(),
                "1 registro → 2 líneas: cabecera + dato");
    }

    @Test
    @DisplayName("CSV: los valores numéricos son válidos (no vacíos, no NaN)")
    void exportarCSV_valoresNumericos_sonValidos() throws IOException {
        inyectarDataset(1);
        Path csv = dir.resolve("valores.csv");

        controlador.exportarDatasetCSV(csv.toString());

        List<String> lineas = Files.readAllLines(csv);
        // línea 1 = primera fila de datos
        String[] valores = lineas.get(1).split(",");
        for (String v : valores) {
            assertFalse(v.isBlank(), "Ningún valor debe estar vacío");
            assertDoesNotThrow(() -> Double.parseDouble(v),
                    "Cada valor debe ser numérico: " + v);
            assertFalse(Double.isNaN(Double.parseDouble(v)),
                    "Ningún valor debe ser NaN");
        }
    }

    @Test
    @DisplayName("CSV: dataset vacío lanza IllegalStateException")
    void exportarCSV_datasetVacio_lanzaExcepcion() {
        Path csv = dir.resolve("vacio.csv");

        assertThrows(IllegalStateException.class,
                () -> controlador.exportarDatasetCSV(csv.toString()),
                "Dataset vacío debe lanzar IllegalStateException");
    }

    @Test
    @DisplayName("CSV: ruta null lanza IllegalArgumentException")
    void exportarCSV_rutaNula_lanzaExcepcion() {
        inyectarDataset(1);

        assertThrows(IllegalArgumentException.class,
                () -> controlador.exportarDatasetCSV(null),
                "Ruta null debe lanzar IllegalArgumentException");
    }

    @Test
    @DisplayName("CSV: ruta imposible lanza RuntimeException (error no silenciado)")
    void exportarCSV_rutaImposible_errorNoSilenciado() {
        inyectarDataset(1);

        assertThrows(RuntimeException.class,
                () -> controlador.exportarDatasetCSV("/ruta/imposible/x.csv"),
                "Si no puede escribir el archivo debe lanzar excepción, NO silenciarla");
    }


    //  TESTS DE INTEGRACIÓN DEL FLUJO

    @Test
    @DisplayName("mostrarSistema con null en todos los parámetros no lanza excepción")
    void mostrarSistema_todosNull_noLanzaExcepcion() {
        assertDoesNotThrow(
                () -> controlador.mostrarSistema(null, null, null, null),
                "Con parámetros null no debe lanzar excepción"
        );
    }

    @Test
    @DisplayName("mostrarSistema guarda el dataset internamente")
    void mostrarSistema_guardaDatasetInterno() {
        List<RegistroDato> datos = crearDatos(2);

        controlador.mostrarSistema(null, null, null, datos);

        assertEquals(2, controlador.getDatasetActual().size(),
                "El dataset interno debe tener 2 registros");
    }

    @Test
    @DisplayName("limpiarVista vacía el dataset interno")
    void limpiarVista_vaciaDatasett() {
        inyectarDataset(3);

        controlador.limpiarVista();

        assertTrue(controlador.getDatasetActual().isEmpty(),
                "Tras limpiarVista() el dataset debe estar vacío");
    }

    @Test
    @DisplayName("ejecutarSistemaCompleto con ciudad real no lanza excepción")
    void ejecutarSistemaCompleto_ciudadReal_noLanzaExcepcion() {
        Ciudad ciudad = construirCiudadDemo();

        assertDoesNotThrow(
                () -> controlador.ejecutarSistemaCompleto(ciudad),
                "El flujo completo con ciudad real no debe lanzar excepción"
        );
    }

    @Test
    @DisplayName("ejecutarSistemaCompleto genera al menos 1 RegistroDato")
    void ejecutarSistemaCompleto_generaRegistroDato() {
        Ciudad ciudad = construirCiudadDemo();

        controlador.ejecutarSistemaCompleto(ciudad);

        assertFalse(controlador.getDatasetActual().isEmpty(),
                "Tras ejecutarSistemaCompleto debe haber al menos 1 RegistroDato");
    }

    @Test
    @DisplayName("ejecutarSistemaCompleto + exportarCSV produce archivo válido")
    void flujoCompleto_ejecutar_exportarCSV_archivoValido() throws IOException {
        Ciudad ciudad = construirCiudadDemo();
        Path csv = dir.resolve("flujo_completo.csv");

        controlador.ejecutarSistemaCompleto(ciudad);
        controlador.exportarDatasetCSV(csv.toString());

        assertTrue(Files.exists(csv), "El archivo CSV debe existir");

        List<String> lineas = Files.readAllLines(csv);
        assertTrue(lineas.size() >= 2,
                "Debe haber al menos cabecera + 1 fila de datos");

        // Verificar cabecera
        assertArrayEquals(
                RegistroDato.getFeatureNames(),
                lineas.get(0).split(","),
                "La cabecera debe coincidir con RegistroDato.getFeatureNames()"
        );

        // Verificar que cada fila tiene 6 columnas
        for (int i = 1; i < lineas.size(); i++) {
            assertEquals(6, lineas.get(i).split(",").length,
                    "Fila " + i + " debe tener 6 columnas");
        }
    }

    @Test
    @DisplayName("ejecutarSistemaCompleto con ciudad null no lanza excepción")
    void ejecutarSistemaCompleto_ciudadNull_noLanzaExcepcion() {
        assertDoesNotThrow(
                () -> controlador.ejecutarSistemaCompleto(null),
                "Ciudad null debe manejarse sin excepción"
        );
    }

    @Test
    @DisplayName("getDatasetActual devuelve copia defensiva (no la lista interna)")
    void getDatasetActual_devuelveCopiaDefensiva() {
        inyectarDataset(2);

        List<RegistroDato> copia = controlador.getDatasetActual();
        copia.clear(); // modificar la copia

        assertEquals(2, controlador.getDatasetActual().size(),
                "Modificar la copia no debe afectar al dataset interno");
    }

    // ─────────────────────────────────────────────────────────────────────
    // UTILIDADES DE TEST
    // ─────────────────────────────────────────────────────────────────────

    /** Inyecta N registros en el controlador sin pasar por la BD. */
    private void inyectarDataset(int n) {
        controlador.mostrarSistema(null, null, null, crearDatos(n));
    }

    /** Crea N RegistroDato de ejemplo con valores distintos. */
    private List<RegistroDato> crearDatos(int n) {
        List<RegistroDato> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lista.add(new RegistroDato(
                    "Ciudad_" + i,
                    0.30 + i * 0.1,   // densidad
                    1.2  + i * 0.05,  // ratioEnergetico
                    0.8  + i * 0.02,  // coberturaServicios
                    20.0 + i * 5,     // contaminacion
                    0.75 - i * 0.05,  // estabilidad
                    55.0 + i * 10,    // scoreViabilidad
                    2 + (i % 3)       // objetivo 0-4
            ));
        }
        return lista;
    }

    /** Ciudad con bloques reales para el test de flujo completo. */
    private Ciudad construirCiudadDemo() {
        Ciudad c = new Ciudad("CiudadTest", 8, 8);
        c.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        c.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        c.addBloque(new BloqueResidencial(new Posicion(1, 0)));
        c.addBloque(new BloqueResidencial(new Posicion(1, 1)));
        c.addBloque(new BloqueServicios(new Posicion(2, 2)));
        c.addBloque(new BloqueTransporte(new Posicion(3, 3)));
        return c;
    }
}
