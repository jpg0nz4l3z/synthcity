package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class ConstructorDatasetTest {

    @Test
    public void testGenerarCSVConEscenariosObligatorios() {
        System.out.println("--- INICIANDO GENERACIÓN DEL DATASET (TEST DE INTEGRACIÓN) ---");

        ConstructorDataset datasetFinal = new ConstructorDataset();

        datasetFinal.agregarRegistro(new RegistroDato(0.4, 0.9, 1.0, 0.1, 0.5, 0.8, 0.2, -0.1, 0.9, 90.0, false, 15, false, 4));
        datasetFinal.agregarRegistro(new RegistroDato(0.9, 0.2, 0.4, 0.8, 5.0, 0.2, -0.5, 0.3, 0.1, 10.0, true, 8, false, 1));
        datasetFinal.agregarRegistro(new RegistroDato(0.85, 0.7, 0.6, 0.6, 3.0, 0.5, -0.1, 0.1, 0.5, 50.0, false, 20, true, 2));

        String rutaArchivo = "dataset_sprint3_pruebas.csv";
        datasetFinal.exportarCSV(rutaArchivo);

        File archivoGenerado = new File(rutaArchivo);
        assertTrue(archivoGenerado.exists(), "El archivo CSV no se ha generado correctamente");
        assertTrue(archivoGenerado.length() > 0, "El archivo CSV está vacío");

        System.out.println("¡CSV validado correctamente por el Test!");
    }
}
