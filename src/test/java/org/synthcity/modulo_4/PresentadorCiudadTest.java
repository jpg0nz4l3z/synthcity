package org.synthcity.modulo_4;

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

/*class PresentadorCiudadTest {

    @Test
    void presentar_deberia_devolver_salida_valida_si_resultado_es_correcto() {
        PresentadorCiudad presentador = new PresentadorCiudad();
        ResultadoEvaluacion resultado = crearResultadoValido();

        SalidaTexto salida = presentador.presentar(resultado);

        assertNotNull(salida);
        assertTrue(salida.getContenido().contains("NeoMadrid"));
        assertTrue(salida.getContenido().contains("FUNCIONAL"));
        assertTrue(salida.getContenido().contains("Bloques totales: 10"));
    }

    @Test
    void presentar_deberia_lanzar_excepcion_si_resultado_es_null() {
        PresentadorCiudad presentador = new PresentadorCiudad();

        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> presentador.presentar(null)
        );

        assertEquals("ResultadoEvaluacion no puede ser null.", ex.getMessage());
    }

    @Test
    void presentar_deberia_lanzar_excepcion_si_nombre_es_null() {
        PresentadorCiudad presentador = new PresentadorCiudad();

        ResultadoEvaluacion resultado = new ResultadoEvaluacion(
                null,
                crearMetricaValida(),
                NivelEvaluacion.FUNCIONAL,
                "Mensaje válido"
        );

        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> presentador.presentar(resultado)
        );

        assertEquals("El nombre de la ciudad es obligatorio.", ex.getMessage());
    }

    @Test
    void constructor_deberia_lanzar_excepcion_si_metrica_es_null() {
        ResultadoSimulacionInvalidoException ex = assertThrows(
                ResultadoSimulacionInvalidoException.class,
                () -> new ResultadoEvaluacion(
                        "NeoMadrid",
                        null,
                        NivelEvaluacion.FUNCIONAL,
                        "Mensaje válido"
                )
        );

        assertEquals("El resultado de evaluación no puede estar incompleto.", ex.getMessage());
    }

    @Test
    void constructor_deberia_lanzar_excepcion_si_nivel_es_null() {
        ResultadoSimulacionInvalidoException ex = assertThrows(
                ResultadoSimulacionInvalidoException.class,
                () -> new ResultadoEvaluacion(
                        "NeoMadrid",
                        crearMetricaValida(),
                        null,
                        "Mensaje válido"
                )
        );

        assertEquals("El resultado de evaluación no puede estar incompleto.", ex.getMessage());
    }

    @Test
    void constructor_deberia_lanzar_excepcion_si_mensaje_es_null() {
        ResultadoSimulacionInvalidoException ex = assertThrows(
                ResultadoSimulacionInvalidoException.class,
                () -> new ResultadoEvaluacion(
                        "NeoMadrid",
                        crearMetricaValida(),
                        NivelEvaluacion.FUNCIONAL,
                        null
                )
        );

        assertEquals("El resultado de evaluación no puede estar incompleto.", ex.getMessage());
    }

    @Test
    void presentar_deberia_lanzar_excepcion_si_total_bloques_es_negativo() {
        PresentadorCiudad presentador = new PresentadorCiudad();

        Map<TipoBloque, Integer> conteo = crearConteoBase();

        ResultadoSimulacion simulacion = new ResultadoSimulacion(
                "NeoMadrid",
                2,
                3,
                6,
                -1,
                0,
                0,
                conteo,
                EstadoSimulacion.EJECUTADA
        );

        MetricaCiudad metrica = new MetricaCiudad(simulacion);

        ResultadoEvaluacion resultado = new ResultadoEvaluacion(
                "NeoMadrid",
                metrica,
                NivelEvaluacion.CRITICO,
                "Mensaje válido"
        );

        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> presentador.presentar(resultado)
        );

        assertEquals("Total de bloques invalido.", ex.getMessage());
    }

    private ResultadoEvaluacion crearResultadoValido() {
        return new ResultadoEvaluacion(
                "NeoMadrid",
                crearMetricaValida(),
                NivelEvaluacion.FUNCIONAL,
                "La ciudad presenta un nivel suficiente de actividad."
        );
    }

    private MetricaCiudad crearMetricaValida() {
        ResultadoSimulacion simulacion = crearResultadoSimulacionValido();
        return new MetricaCiudad(simulacion);
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
    @Test
    public void testGenerarResumen_FlujoCompleto_CaminoBasico() {
        PresentadorCiudad presentador = new PresentadorCiudad();
        Ciudad ciudad = new Ciudad("NeoTokyo", 10, 10);

        ResultadoEvaluacion evaluacionFake = new ResultadoEvaluacion() {
            @Override public String getNivelEvaluacion() { return "Optimo"; }
            @Override public double getScoreViabilidad() { return 95.5; }
        };

        PredictionResult prediccionFake = new PredictionResult() {
            @Override public String getTendenciaPredicha() { return "Mejora Constante"; }
        };

        SalidaTexto resultado = presentador.generarResumen(ciudad, evaluacionFake, prediccionFake);

        assertNotNull(resultado);
        assertEquals("NeoTokyo", resultado.getNombreCiudad());
        assertEquals("Resumen Breve", resultado.getTitulo());
        assertTrue(resultado.getContenido().contains("NeoTokyo"));
        assertTrue(resultado.getContenido().contains("Optimo"));
        assertTrue(resultado.getContenido().contains("95.5"));
        assertTrue(resultado.getContenido().contains("Mejora Constante"));
    }

    private Map<TipoBloque, Integer> crearConteoBase() {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            conteo.put(tipo, 0);
        }
        return conteo;
    }
}*/
