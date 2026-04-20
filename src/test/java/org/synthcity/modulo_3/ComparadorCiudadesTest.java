/*package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ComparadorCiudadesTest {

    private Map<TipoBloque, Integer> conteoBase() {
        Map<TipoBloque, Integer> m = new EnumMap<>(TipoBloque.class);
        for (TipoBloque t : TipoBloque.values()) {
            m.put(t, 0);
        }
        m.put(TipoBloque.RESIDENCIAL, 5);
        m.put(TipoBloque.ENERGIA, 2);
        m.put(TipoBloque.SERVICIOS, 2);
        m.put(TipoBloque.TRANSPORTE, 1);
        return m;
    }

    private MetricaCiudad metricaDummy(String nombre) {
        ResultadoSimulacion sim = new ResultadoSimulacion(
                nombre, 5, 5, 25, 10, 7, 3, conteoBase(), EstadoSimulacion.EJECUTADA);
        return new MetricaCiudad(sim);
    }

    private ResultadoEvaluacion construir(String nombre,
                                          double score,
                                          NivelEvaluacion nivel,
                                          Set<AlertaEvaluacion> alertas) {
        return new ResultadoEvaluacion(
                nombre,
                metricaDummy(nombre),
                nivel,
                "Mensaje de prueba",
                score,
                alertas,
                "Sin información de riesgo.");
    }

    // F.49 — dos ciudades con score distinto se ordenan bien.
    @Test
    void ordenarPorViabilidad_ordenaDescendentePorScore() {
        ResultadoEvaluacion baja = construir("Baja", 40.0, NivelEvaluacion.INESTABLE, EnumSet.noneOf(AlertaEvaluacion.class));
        ResultadoEvaluacion media = construir("Media", 70.0, NivelEvaluacion.FUNCIONAL, EnumSet.noneOf(AlertaEvaluacion.class));
        ResultadoEvaluacion alta = construir("Alta", 90.0, NivelEvaluacion.OPTIMO, EnumSet.noneOf(AlertaEvaluacion.class));

        List<ResultadoEvaluacion> entrada = new ArrayList<>(Arrays.asList(baja, alta, media));
        List<ResultadoEvaluacion> ordenada = ComparadorCiudades.ordenarPorViabilidad(entrada);

        assertEquals("Alta", ordenada.get(0).getNombreCiudad());
        assertEquals("Media", ordenada.get(1).getNombreCiudad());
        assertEquals("Baja", ordenada.get(2).getNombreCiudad());

        assertEquals(3, entrada.size());
        assertEquals("Baja", entrada.get(0).getNombreCiudad());
    }

    // F.50 — empate por score se resuelve con criterio documentado:
    // menor número de alertas primero; si persiste, orden alfabético por nombre.
    @Test
    void empatePorScore_seResuelveConMenosAlertasYLuegoAlfabeticamente() {
        ResultadoEvaluacion dosAlertas = construir(
                "Zulu", 70.0, NivelEvaluacion.FUNCIONAL,
                EnumSet.of(AlertaEvaluacion.DEFICIT_ENERGETICO, AlertaEvaluacion.CONTAMINACION_ALTA));
        ResultadoEvaluacion unaAlerta = construir(
                "Yoruba", 70.0, NivelEvaluacion.FUNCIONAL,
                EnumSet.of(AlertaEvaluacion.DEFICIT_ENERGETICO));
        ResultadoEvaluacion sinAlertasA = construir(
                "Beta", 70.0, NivelEvaluacion.FUNCIONAL,
                EnumSet.noneOf(AlertaEvaluacion.class));
        ResultadoEvaluacion sinAlertasB = construir(
                "Alpha", 70.0, NivelEvaluacion.FUNCIONAL,
                EnumSet.noneOf(AlertaEvaluacion.class));

        List<ResultadoEvaluacion> ordenada = ComparadorCiudades.ordenarPorViabilidad(
                Arrays.asList(dosAlertas, unaAlerta, sinAlertasA, sinAlertasB));

        assertEquals("Alpha", ordenada.get(0).getNombreCiudad());
        assertEquals("Beta", ordenada.get(1).getNombreCiudad());
        assertEquals("Yoruba", ordenada.get(2).getNombreCiudad());
        assertEquals("Zulu", ordenada.get(3).getNombreCiudad());
    }

    // F.51 — la comparación no depende de strings arbitrarios:
    // dos resultados con nivel y mensaje distintos, pero mismo score y alertas,
    // conservan orden estable por nombre y no se ven alterados por el texto libre.
    @Test
    void comparacionNoDependeDeStringsArbitrarios() {
        ResultadoEvaluacion a = new ResultadoEvaluacion(
                "Alpha",
                metricaDummy("Alpha"),
                NivelEvaluacion.FUNCIONAL,
                "Cadena totalmente libre y larga sin relevancia.",
                60.0,
                EnumSet.of(AlertaEvaluacion.DEFICIT_SERVICIOS),
                "Otra cadena.");
        ResultadoEvaluacion b = new ResultadoEvaluacion(
                "Beta",
                metricaDummy("Beta"),
                NivelEvaluacion.INESTABLE,
                "zzz",
                60.0,
                EnumSet.of(AlertaEvaluacion.CONTAMINACION_ALTA),
                "zzz");

        int cmpAB = ComparadorCiudades.compararPorViabilidad(a, b);
        int cmpBA = ComparadorCiudades.compararPorViabilidad(b, a);

        assertTrue(cmpAB < 0, "Alpha debe preceder a Beta por desempate alfabético");
        assertTrue(cmpBA > 0, "La comparación inversa debe ser consistente");
        assertEquals(0, ComparadorCiudades.compararPorViabilidad(a, a));
    }

    @Test
    void nullsSonTolerados_yQuedanAlFinal() {
        ResultadoEvaluacion real = construir("Real", 80.0, NivelEvaluacion.FUNCIONAL, EnumSet.noneOf(AlertaEvaluacion.class));

        assertEquals(0, ComparadorCiudades.compararPorViabilidad(null, null));
        assertTrue(ComparadorCiudades.compararPorViabilidad(null, real) > 0);
        assertTrue(ComparadorCiudades.compararPorViabilidad(real, null) < 0);
    }
}*/