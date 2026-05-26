package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ComparadorCiudadesTest {

    @Test
    void dosCiudadesConScoreDistinto_seOrdenanBien() {
        ResultadoEvaluacion a = new ResultadoEvaluacion(
                "A",
                crearMetricaDummy(),
                NivelEvaluacion.FUNCIONAL,
                "ok",
                80.0,
                Set.of(),
                "sin riesgos"
        );

        ResultadoEvaluacion b = new ResultadoEvaluacion(
                "B",
                crearMetricaDummy(),
                NivelEvaluacion.INESTABLE,
                "ok",
                60.0,
                Set.of(),
                "sin riesgos"
        );

        List<ResultadoEvaluacion> ordenadas = ComparadorCiudades.ordenarPorViabilidad(List.of(b, a));

        assertEquals("A", ordenadas.get(0).getNombreCiudad());
        assertEquals("B", ordenadas.get(1).getNombreCiudad());
    }

    @Test
    void empatePorScore_seResuelveConCriterioDocumentado() {
        ResultadoEvaluacion a = new ResultadoEvaluacion(
                "A",
                crearMetricaDummy(),
                NivelEvaluacion.FUNCIONAL,
                "ok",
                80.0,
                Set.of(),
                "sin riesgos"
        );

        ResultadoEvaluacion b = new ResultadoEvaluacion(
                "B",
                crearMetricaDummy(),
                NivelEvaluacion.FUNCIONAL,
                "ok",
                80.0,
                Set.of(AlertaEvaluacion.DEFICIT_ENERGETICO),
                "riesgo"
        );

        List<ResultadoEvaluacion> ordenadas = ComparadorCiudades.ordenarPorViabilidad(List.of(b, a));

        assertEquals("A", ordenadas.get(0).getNombreCiudad());
        assertEquals("B", ordenadas.get(1).getNombreCiudad());
    }

    @Test
    void comparacionNoDependeDeStringsArbitrarios() {
        ResultadoEvaluacion a = new ResultadoEvaluacion(
                "CiudadZ",
                crearMetricaDummy(),
                NivelEvaluacion.CRITICO,
                "mensaje arbitrario 1",
                70.0,
                Set.of(),
                "riesgo"
        );

        ResultadoEvaluacion b = new ResultadoEvaluacion(
                "CiudadA",
                crearMetricaDummy(),
                NivelEvaluacion.OPTIMO,
                "mensaje arbitrario 2",
                70.0,
                Set.of(),
                "riesgo"
        );

        int cmp = ComparadorCiudades.compararPorViabilidad(a, b);

        assertTrue(cmp > 0);
    }

    private MetricaCiudad crearMetricaDummy() {
        org.synthcity.modulo_1.Ciudad ciudad = new org.synthcity.modulo_1.Ciudad("Dummy", 10, 10);
        org.synthcity.modulo_2.SimuladorCiudad simulador = new org.synthcity.modulo_2.SimuladorCiudad();
        org.synthcity.modulo_2.ResultadoSimulacion rs = simulador.simular(ciudad);
        return new MetricaCiudad(rs);
    }
}