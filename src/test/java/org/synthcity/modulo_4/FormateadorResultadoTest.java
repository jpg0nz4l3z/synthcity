/*package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.NivelEvaluacion;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_3.ResultadoSimulacionInvalidoException;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FormateadorResultadoTest {

    @Test
    void formatear_deberia_generar_texto_con_datos_principales() {
        FormateadorResultado formateador = new FormateadorResultado();
        ResultadoEvaluacion resultado = crearResultadoValido();

        String texto = formateador.formatear(resultado);

        assertNotNull(texto);
        assertTrue(texto.contains("NeoMadrid"));
        assertTrue(texto.contains("FUNCIONAL"));
        assertTrue(texto.contains("Bloques totales: 10"));
        assertTrue(texto.contains("Residencial: 5"));
        assertTrue(texto.contains("VARIABLES URBANAS"));
    }

    @Test
    void formatear_deberia_lanzar_excepcion_si_resultado_es_null() {
        FormateadorResultado formateador = new FormateadorResultado();

        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> formateador.formatear(null)
        );

        assertEquals("No se puede formatear un resultado null.", ex.getMessage());
    }

    @Test
    void resultadoEvaluacion_deberia_rechazar_metrica_null() {
        ResultadoSimulacionInvalidoException ex = assertThrows(
                ResultadoSimulacionInvalidoException.class,
                () -> new ResultadoEvaluacion("NeoMadrid", null, NivelEvaluacion.FUNCIONAL, "Mensaje valido")
        );

        assertEquals("El resultado de evaluación no puede estar incompleto.", ex.getMessage());
    }

    @Test
    void metrica_deberia_detectar_conteo_incompleto() {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        conteo.put(TipoBloque.RESIDENCIAL, 5);

        ResultadoSimulacion simulacion = new ResultadoSimulacion(
                "NeoMadrid",
                2,
                3,
                6,
                5,
                5,
                0,
                conteo,
                EstadoSimulacion.EJECUTADA
        );

        assertThrows(ResultadoSimulacionInvalidoException.class, () -> new MetricaCiudad(simulacion));
    }

    private ResultadoEvaluacion crearResultadoValido() {
        MetricaCiudad metrica = new MetricaCiudad(crearResultadoSimulacionValido());
        return new ResultadoEvaluacion(
                "NeoMadrid",
                metrica,
                NivelEvaluacion.FUNCIONAL,
                "La ciudad presenta un nivel suficiente de actividad.",
                72.5,
                java.util.EnumSet.noneOf(org.synthcity.modulo_3.AlertaEvaluacion.class),
                "Sin riesgos relevantes detectados."
        );
    }

    private ResultadoSimulacion crearResultadoSimulacionValido() {
        Map<TipoBloque, Integer> conteo = crearConteoBase();
        conteo.put(TipoBloque.RESIDENCIAL, 5);
        conteo.put(TipoBloque.ENERGIA, 2);
        conteo.put(TipoBloque.INDUSTRIAL, 1);
        conteo.put(TipoBloque.SERVICIOS, 1);
        conteo.put(TipoBloque.TRANSPORTE, 1);

        return new ResultadoSimulacion(
                "NeoMadrid",
                2,
                5,
                10,
                10,
                7,
                3,
                conteo,
                EstadoSimulacion.EJECUTADA
        );
    }

    private Map<TipoBloque, Integer> crearConteoBase() {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            conteo.put(tipo, 0);
        }
        return conteo;
    }
}*/
