/*package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.NivelEvaluacion;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_1.TipoBloque;

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
}*/

package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.NivelEvaluacion;
import org.synthcity.modulo_3.ResultadoEvaluacion;

import java.util.EnumMap;
import java.util.HashMap;
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
        conteo.put(TipoBloque.RESIDENCIAL, 5); // faltan el resto

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

        MetricaCiudad metrica = new MetricaCiudad(simulacion);

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
        ResultadoSimulacion simulacion = crearResultadoSimulacionValido();
        MetricaCiudad metrica = new MetricaCiudad(simulacion);

        return new ResultadoEvaluacion(
                "NeoMadrid",
                metrica,
                NivelEvaluacion.FUNCIONAL,
                "La ciudad presenta un nivel suficiente de actividad."
        );
    }

    private ResultadoSimulacion crearResultadoSimulacionValido() {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            conteo.put(tipo, 0);
        }

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
    @Test
    public void testFormatear_GeneraInformeCorrectamente_CaminoBasico() {
        FormateadorResultado formateador = new FormateadorResultado();

        // 1. Creamos las métricas falsas para el camino ideal
        MetricaCiudad metricaFake = new MetricaCiudad() {
            @Override public int getTotalBloques() { return 100; }
            @Override public int getBloquesActivos() { return 80; }
            @Override public int getBloquesInactivos() { return 20; }
            @Override public double getPorcentajeActivos() { return 0.8; }
            @Override public double getPorcentajeInactivos() { return 0.2; }
            @Override public Map<TipoBloque, Integer> getConteoPorTipo() {
                Map<TipoBloque, Integer> mapa = new HashMap<>();
                mapa.put(TipoBloque.RESIDENCIAL, 50);
                mapa.put(TipoBloque.COMERCIAL, 30);
                mapa.put(TipoBloque.INDUSTRIAL, 20);
                return mapa;
            }
        };

        // 2. Creamos la evaluación falsa inyectando las métricas
        ResultadoEvaluacion resultadoFake = new ResultadoEvaluacion() {
            @Override public String getNombreCiudad() { return "Ciudad Test"; }
            @Override public String getNivelEvaluacion() { return "Estable"; }
            @Override public String getMensaje() { return "Todo correcto"; }
            @Override public MetricaCiudad getMetricaCiudad() { return metricaFake; }
        };

        String informe = formateador.formatear(resultadoFake);

        assertNotNull(informe);
        assertTrue(informe.contains("INFORME DE ESTADO DE CIUDAD"));
        assertTrue(informe.contains("Ciudad Test"));
        assertTrue(informe.contains("Estable"));
    }
}