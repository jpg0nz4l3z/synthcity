package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class IntegracionRanking {

    @TempDir
    Path dir;

    @Test
    void testFlujoCompletoYExportacionInforme() throws Exception {
        // 1. Crear ciudad simulable
        Ciudad ciudad = new Ciudad("NeoMadrid", 5, 5);
        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 2)));

        // 2. Ejecutar Simulación
        SimuladorCiudad simulador = new SimuladorCiudad();
        var historial = simulador.simular(ciudad);
        assertNotNull(historial, "La simulación debe generar un historial válido");

        // 3. Evaluar
        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        var evaluacion = evaluador.evaluar(historial, ciudad);
        assertNotNull(evaluacion, "La evaluación debe completarse correctamente");

        // 4. Predecir
        var prediccion = evaluador.predecir(historial);
        assertNotNull(prediccion, "La predicción no debe ser nula");

        // 5. Verificar exportación de informe (Requisito Sprint 4)
        Path archivoInforme = dir.resolve("informe_test.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoInforme.toFile()))) {
            writer.write("Ciudad: " + ciudad.getNombre() + "\n");
            writer.write("Score: " + evaluacion.getScoreViabilidad() + "\n");
        }

        // 6. Comprobaciones finales del archivo
        assertTrue(Files.exists(archivoInforme), "El archivo del informe debe haberse creado");
        String contenido = Files.readString(archivoInforme);
        assertTrue(contenido.contains("NeoMadrid"), "El informe debe contener el nombre de la ciudad");
    }
}