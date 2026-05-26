package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConstructorDatasetTest {

    @TempDir
    Path tempDir;

    @Test
    void datasetInicial_estaVacio() {
        ConstructorDataset dataset = new ConstructorDataset();

        assertTrue(dataset.estaVacio());
        assertEquals(0, dataset.getTamano());
        assertTrue(dataset.getDataset().isEmpty());
    }

    @Test
    void agregarRegistro_validoIncrementaTamano() {
        ConstructorDataset dataset = new ConstructorDataset();

        dataset.agregarRegistro(crearRegistro());

        assertFalse(dataset.estaVacio());
        assertEquals(1, dataset.getTamano());
        assertEquals(1, dataset.getDataset().size());
    }

    @Test
    void agregarRegistroNulo_lanzaExcepcion() {
        ConstructorDataset dataset = new ConstructorDataset();

        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                dataset.agregarRegistro(null)
        );
    }

    @Test
    void getDataset_devuelveCopiaInmodificable() {
        ConstructorDataset dataset = new ConstructorDataset();
        dataset.agregarRegistro(crearRegistro());

        List<RegistroDato> registros = dataset.getDataset();

        assertThrows(UnsupportedOperationException.class, () ->
                registros.add(crearRegistro())
        );
    }

    @Test
    void limpiar_vaciaDataset() {
        ConstructorDataset dataset = new ConstructorDataset();
        dataset.agregarRegistro(crearRegistro());

        dataset.limpiar();

        assertTrue(dataset.estaVacio());
        assertEquals(0, dataset.getTamano());
    }

    @Test
    void exportarCSV_creaArchivoConCabeceraYRegistro() throws Exception {
        ConstructorDataset dataset = new ConstructorDataset();
        dataset.agregarRegistro(crearRegistro());

        Path archivo = tempDir.resolve("dataset.csv");

        dataset.exportarCSV(archivo.toString());

        List<String> lineas = Files.readAllLines(archivo);

        assertEquals(2, lineas.size());
        assertEquals(
                "densidad,ratioEnergetico,ratioCoberturaServicios,contaminacion,contaminacionAcumulada,estabilidadMedia,tendenciaEstabilidad,tendenciaContaminacion,bienestar,scoreViabilidad,colapsoDetectado,ciclosEjecutados,saturacionDetectada,objetivo",
                lineas.get(0)
        );
        assertTrue(lineas.get(1).endsWith(",3"));
    }

    @Test
    void exportarCSV_conDatasetVacio_lanzaExcepcion() {
        ConstructorDataset dataset = new ConstructorDataset();

        Path archivo = tempDir.resolve("vacio.csv");

        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                dataset.exportarCSV(archivo.toString())
        );
    }

    @Test
    void exportarCSV_conRutaInvalida_lanzaExcepcion() {
        ConstructorDataset dataset = new ConstructorDataset();
        dataset.agregarRegistro(crearRegistro());

        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                dataset.exportarCSV(" ")
        );
    }

    private RegistroDato crearRegistro() {
        return new RegistroDato(
                0.7,
                1.0,
                0.9,
                10,
                30,
                0.8,
                -0.05,
                10,
                0.75,
                70.0,
                false,
                5,
                false,
                3
        );
    }
}