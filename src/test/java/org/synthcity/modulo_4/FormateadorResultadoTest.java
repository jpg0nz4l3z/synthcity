package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_4.stubs.MetricaCiudad;
import org.synthcity.modulo_4.stubs.NivelEvaluacion;
import org.synthcity.modulo_4.stubs.ResultadoEvaluacion;
import org.synthcity.modulo_4.stubs.TipoBloque;

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
    void formatear_deberia_lanzar_excepcion_si_metrica_es_null() {
        FormateadorResultado formateador = new FormateadorResultado();

        ResultadoEvaluacion resultado = new ResultadoEvaluacion(
                "NeoMadrid",
                null,
                NivelEvaluacion.FUNCIONAL,
                "Mensaje válido"
        );

        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> formateador.formatear(resultado)
        );

        assertEquals("No se puede formatear un resultado sin metricas.", ex.getMessage());
    }

    @Test
    void formatear_deberia_lanzar_excepcion_si_falta_un_tipo_en_el_conteo() {
        FormateadorResultado formateador = new FormateadorResultado();

        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        conteo.put(TipoBloque.RESIDENCIAL, 5);

        MetricaCiudad metrica = new MetricaCiudad(
                5,
                5,
                0,
                1.0,
                0.0,
                conteo
        );

        ResultadoEvaluacion resultado = new ResultadoEvaluacion(
                "NeoMadrid",
                metrica,
                NivelEvaluacion.FUNCIONAL,
                "Mensaje válido"
        );

        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> formateador.formatear(resultado)
        );

        assertTrue(ex.getMessage().contains("Falta el tipo"));
    }

    private ResultadoEvaluacion crearResultadoValido() {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            conteo.put(tipo, 0);
        }

        conteo.put(TipoBloque.RESIDENCIAL, 5);
        conteo.put(TipoBloque.ENERGIA, 2);
        conteo.put(TipoBloque.INDUSTRIAL, 1);
        conteo.put(TipoBloque.SERVICIOS, 1);
        conteo.put(TipoBloque.TRANSPORTE, 1);

        MetricaCiudad metrica = new MetricaCiudad(
                10,
                7,
                3,
                0.70,
                0.30,
                conteo
        );

        return new ResultadoEvaluacion(
                "NeoMadrid",
                metrica,
                NivelEvaluacion.FUNCIONAL,
                "La ciudad presenta un nivel suficiente de actividad."
        );
    }
}