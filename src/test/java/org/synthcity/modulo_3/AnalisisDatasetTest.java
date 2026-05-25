package org.synthcity.modulo_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_3.prediccion.AnalisisDataset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnalisisDatasetTest {
    private List<RegistroDato> dataset;
    private List<ResultadoEvaluacion> evaluaciones;

    @BeforeEach
    void setUp() {
        dataset = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dataset.add(new RegistroDato(
                    0.9,        // densidad alta
                    0.2,        // ratioEnergetico bajo
                    0.2,        // ratioCoberturaServicios bajo
                    80.0,       // contaminacion alta
                    400.0,      // contaminacionAcumulada
                    0.15,       // estabilidadMedia baja
                    -0.3,       // tendenciaEstabilidad negativa
                    10.0,       // tendenciaContaminacion
                    0.2,        // bienestar bajo
                    10.0 + i,   // scoreViabilidad: 10, 11, 12, 13, 14
                    true,       // colapsoDetectado
                    3,          // ciclosEjecutados
                    false,      // saturacionDetectada
                    1           // objetivo = CRITICO
            ));
        }
        for (int i = 0; i < 5; i++) {
            dataset.add(new RegistroDato(
                    0.7,
                    0.5,
                    0.5,
                    55.0,
                    165.0,
                    0.35,
                    -0.1,
                    5.0,
                    0.4,
                    30.0 + i,   // scoreViabilidad: 30, 31, 32, 33, 34
                    false,
                    5,
                    true,
                    2           // objetivo = INESTABLE
            ));
        }
        for (int i = 0; i < 5; i++) {
            dataset.add(new RegistroDato(
                    0.5,
                    0.8,
                    0.8,
                    20.0,
                    60.0,
                    0.65,
                    0.05,
                    2.0,
                    0.65,
                    65.0 + i,   // scoreViabilidad: 65, 66, 67, 68, 69
                    false,
                    8,
                    false,
                    3           // objetivo = FUNCIONAL
            ));
        }
        for (int i = 0; i < 5; i++) {
            dataset.add(new RegistroDato(
                    0.3,
                    0.95,
                    0.95,
                    5.0,
                    15.0,
                    0.90,
                    0.1,
                    1.0,
                    0.85,
                    85.0 + i,   // scoreViabilidad: 85, 86, 87, 88, 89
                    false,
                    10,
                    false,
                    4           // objetivo = OPTIMO
            ));
        }
        evaluaciones = new ArrayList<>();
        evaluaciones.add(crearEvaluacion("CiudadA", NivelEvaluacion.FUNCIONAL,  65.0));
        evaluaciones.add(crearEvaluacion("CiudadB", NivelEvaluacion.OPTIMO,     92.0));
        evaluaciones.add(crearEvaluacion("CiudadC", NivelEvaluacion.CRITICO,    12.0));
        evaluaciones.add(crearEvaluacion("CiudadD", NivelEvaluacion.INESTABLE,  41.0));
    }
    @Test
    void d1_filtrarPorObjetivo_todosLosDevueltosSonDeLaClasePedida() {
        List<RegistroDato> soloFuncional = AnalisisDataset.filtrarPorObjetivo(dataset, 3);

        assertTrue(
                soloFuncional.stream().allMatch(r -> r.getObjetivo() == 3),
                "Todos los registros devueltos deben tener objetivo=3 (FUNCIONAL)"
        );
    }

    @Test
    void d2_filtrarPorObjetivo_cuentaEsCorrecta() {
        // Creamos 5 registros FUNCIONAL en setUp → deben ser exactamente 5
        List<RegistroDato> soloFuncional = AnalisisDataset.filtrarPorObjetivo(dataset, 3);
        assertEquals(5, soloFuncional.size(), "Deben devolverse exactamente 5 registros FUNCIONAL");
    }

    @Test
    void d2b_filtrarPorObjetivo_ningunoDeOtraClase() {
        List<RegistroDato> soloCritico = AnalisisDataset.filtrarPorObjetivo(dataset, 1);

        assertFalse(
                soloCritico.stream().anyMatch(r -> r.getObjetivo() != 1),
                "No debe aparecer ningún registro con objetivo distinto de CRITICO"
        );
    }

    @Test
    void d2c_filtrarPorObjetivo_claseAusente_devuelveVacio() {
        // No hay registros con objetivo=0 en el dataset de prueba
        List<RegistroDato> sinDatos = AnalisisDataset.filtrarPorObjetivo(dataset, 0);
        assertTrue(sinDatos.isEmpty(), "Filtrar por objetivo=0 debe devolver lista vacía");
    }
    @Test
    void d3_calcularMediaFeature_densidad_valorCorrecto() {
        // densidades: 5×0.9 + 5×0.7 + 5×0.5 + 5×0.3 = 12.0 / 20 = 0.6
        double esperada = (5 * 0.9 + 5 * 0.7 + 5 * 0.5 + 5 * 0.3) / 20.0;
        double real = AnalisisDataset.calcularMediaFeature(dataset, 0); // índice 0 = densidad
        assertEquals(esperada, real, 0.0001, "La media de densidad no coincide");
    }

    @Test
    void d3b_calcularMediaFeature_scoreViabilidad_valorCorrecto() {
        // scores: (10+11+12+13+14) + (30+31+32+33+34) + (65+66+67+68+69) + (85+86+87+88+89)
        double suma = (10+11+12+13+14) + (30+31+32+33+34)
                + (65+66+67+68+69) + (85+86+87+88+89);
        double esperada = suma / 20.0;
        double real = AnalisisDataset.calcularMediaFeature(dataset, 9); // índice 9 = scoreViabilidad
        assertEquals(esperada, real, 0.0001, "La media de scoreViabilidad no coincide");
    }

    @Test
    void d4_calcularMediaFeature_listaVacia_devuelveCero() {
        double media = AnalisisDataset.calcularMediaFeature(new ArrayList<>(), 0);
        assertEquals(0.0, media, 0.0001, "La media de lista vacía debe ser 0.0");
    }
    @Test
    void d5_construirRanking_primerElementoTieneScoreMasAlto() {
        List<ResultadoEvaluacion> ranking = AnalisisDataset.construirRanking(evaluaciones);
        assertEquals(92.0, ranking.get(0).getScoreViabilidad(), 0.001,
                "El primer elemento debe ser el de score más alto (92.0)");
    }

    @Test
    void d5b_construirRanking_ultimoElementoTieneScoreMasBajo() {
        List<ResultadoEvaluacion> ranking = AnalisisDataset.construirRanking(evaluaciones);
        assertEquals(12.0, ranking.get(ranking.size() - 1).getScoreViabilidad(), 0.001,
                "El último elemento debe ser el de score más bajo (12.0)");
    }

    @Test
    void d5c_construirRanking_ordenDescendenteCompleto() {
        List<ResultadoEvaluacion> ranking = AnalisisDataset.construirRanking(evaluaciones);
        for (int i = 0; i < ranking.size() - 1; i++) {
            assertTrue(
                    ranking.get(i).getScoreViabilidad() >= ranking.get(i + 1).getScoreViabilidad(),
                    "Elemento " + i + " debe tener score >= que elemento " + (i + 1)
            );
        }
    }

    @Test
    void d6_construirRanking_noModificaListaOriginal() {
        double primerScoreAntes = evaluaciones.get(0).getScoreViabilidad(); // 65.0
        AnalisisDataset.construirRanking(evaluaciones);
        assertEquals(primerScoreAntes, evaluaciones.get(0).getScoreViabilidad(), 0.001,
                "construirRanking no debe modificar la lista original");
    }

    @Test
    void d7_construirRanking_unSoloElemento_noLanzaExcepcion() {
        List<ResultadoEvaluacion> uno = List.of(crearEvaluacion("Solo", NivelEvaluacion.FUNCIONAL, 55.0));
        List<ResultadoEvaluacion> ranking = AnalisisDataset.construirRanking(uno);
        assertEquals(1, ranking.size());
        assertEquals(55.0, ranking.get(0).getScoreViabilidad(), 0.001);
    }

    @Test
    void d8_construirRanking_listaVacia_devuelveVacio() {
        List<ResultadoEvaluacion> ranking = AnalisisDataset.construirRanking(new ArrayList<>());
        assertTrue(ranking.isEmpty(), "Ranking de lista vacía debe ser lista vacía");
    }
    @Test
    void d9_calcularDistribucion_detectaCuatroClases() {
        Map<Integer, Long> dist = AnalisisDataset.calcularDistribucion(dataset);
        // Hay clases 1, 2, 3, 4 → 4 entradas en el mapa
        assertEquals(4, dist.size(), "Deben detectarse exactamente 4 clases");
    }

    @Test
    void d10_calcularDistribucion_cadaClaseTieneCincoRegistros() {
        Map<Integer, Long> dist = AnalisisDataset.calcularDistribucion(dataset);
        assertEquals(5L, dist.get(1), "CRITICO debe tener 5 registros");
        assertEquals(5L, dist.get(2), "INESTABLE debe tener 5 registros");
        assertEquals(5L, dist.get(3), "FUNCIONAL debe tener 5 registros");
        assertEquals(5L, dist.get(4), "OPTIMO debe tener 5 registros");
    }
    @Test
    void d11_filtrarValidos_descartaObjetivoCero() {
        // Añadimos un registro extra con objetivo=0
        List<RegistroDato> conSinDatos = new ArrayList<>(dataset);
        conSinDatos.add(new RegistroDato(
                0.5, 0.5, 0.5, 30.0, 90.0,
                0.5, 0.0, 3.0, 0.5,
                50.0, false, 5, false,
                0
        ));

        List<RegistroDato> validos = AnalisisDataset.filtrarValidos(conSinDatos);

        // El de objetivo=0 se descarta
        assertTrue(
                validos.stream().noneMatch(r -> r.getObjetivo() == 0),
                "filtrarValidos no debe devolver registros con objetivo=0"
        );
        assertEquals(20, validos.size(), "Deben quedar los 20 registros válidos originales");
    }
    @Test
    void d12_tieneClasesSuficientes_conCuatroClases_devuelveTrue() {
        assertTrue(AnalisisDataset.tieneClasesSuficientes(dataset),
                "Dataset con 4 clases debe tener clases suficientes");
    }

    @Test
    void d13_tieneClasesSuficientes_soloUnaClase_devuelveFalse() {
        // Solo los registros CRITICO (objetivo=1)
        List<RegistroDato> soloUna = AnalisisDataset.filtrarPorObjetivo(dataset, 1);
        assertFalse(AnalisisDataset.tieneClasesSuficientes(soloUna),
                "Dataset con una sola clase NO tiene clases suficientes para Weka");
    }
    @Test
    void d14_sinValoresInvalidos_datasetLimpio_devuelveTrue() {
        assertTrue(AnalisisDataset.sinValoresInvalidos(dataset),
                "El dataset de prueba no debe tener valores inválidos");
    }
    @Test
    void d15_imprimirResumen_noLanzaExcepcion() {
        assertDoesNotThrow(
                () -> AnalisisDataset.imprimirResumen(dataset),
                "imprimirResumen no debe lanzar ninguna excepción"
        );
    }
    private ResultadoEvaluacion crearEvaluacion(String nombre,
                                                NivelEvaluacion nivel,
                                                double score) {
        // Usamos el constructor completo con MetricaCiudad real
        // Para ello simulamos una ciudad mínima
        org.synthcity.modulo_1.Ciudad ciudad =
                new org.synthcity.modulo_1.Ciudad(nombre, 5, 5);
        ciudad.addBloque(new org.synthcity.modulo_1.bloques.BloqueEnergia(
                new org.synthcity.modulo_1.Posicion(0, 0)));
        ciudad.addBloque(new org.synthcity.modulo_1.bloques.BloqueResidencial(
                new org.synthcity.modulo_1.Posicion(0, 1)));

        org.synthcity.modulo_2.ResultadoSimulacion rs =
                new org.synthcity.modulo_2.SimuladorCiudad().simular(ciudad);
        MetricaCiudad metrica = new MetricaCiudad(rs);

        // Usamos el constructor de 7 parámetros con el score que queremos para el test
        return new ResultadoEvaluacion(
                nombre,
                metrica,
                nivel,
                "Evaluación de prueba para ranking",
                score,
                java.util.EnumSet.noneOf(AlertaEvaluacion.class),
                "Sin riesgo relevante."
        );
    }
}



