package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_4.stubs.MetricaCiudad;
import org.synthcity.modulo_4.stubs.NivelEvaluacion;
import org.synthcity.modulo_4.stubs.ResultadoEvaluacion;
import org.synthcity.modulo_4.stubs.TipoBloque;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PresentadorCiudadTest {

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
    void presentar_deberia_lanzar_excepcion_si_metrica_es_null() {
        PresentadorCiudad presentador = new PresentadorCiudad();

        ResultadoEvaluacion resultado = new ResultadoEvaluacion(
                "NeoMadrid",
                null,
                NivelEvaluacion.FUNCIONAL,
                "Mensaje válido"
        );

        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> presentador.presentar(resultado)
        );

        assertEquals("La metrica no puede ser null.", ex.getMessage());
    }

    @Test
    void presentar_deberia_lanzar_excepcion_si_nivel_es_null() {
        PresentadorCiudad presentador = new PresentadorCiudad();

        ResultadoEvaluacion resultado = new ResultadoEvaluacion(
                "NeoMadrid",
                crearMetricaValida(),
                null,
                "Mensaje válido"
        );

        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> presentador.presentar(resultado)
        );

        assertEquals("El nivel de evaluacion no puede ser null.", ex.getMessage());
    }

    @Test
    void presentar_deberia_lanzar_excepcion_si_mensaje_es_null() {
        PresentadorCiudad presentador = new PresentadorCiudad();

        ResultadoEvaluacion resultado = new ResultadoEvaluacion(
                "NeoMadrid",
                crearMetricaValida(),
                NivelEvaluacion.FUNCIONAL,
                null
        );

        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> presentador.presentar(resultado)
        );

        assertEquals("El mensaje no puede ser null ni vacio.", ex.getMessage());
    }

    @Test
    void presentar_deberia_lanzar_excepcion_si_total_bloques_es_negativo() {
        PresentadorCiudad presentador = new PresentadorCiudad();

        MetricaCiudad metrica = new MetricaCiudad(
                -1,
                0,
                0,
                0.0,
                0.0,
                crearConteoBase()
        );

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
        Map<TipoBloque, Integer> conteo = crearConteoBase();
        conteo.put(TipoBloque.RESIDENCIAL, 5);
        conteo.put(TipoBloque.ENERGIA, 2);
        conteo.put(TipoBloque.INDUSTRIAL, 1);
        conteo.put(TipoBloque.SERVICIOS, 1);
        conteo.put(TipoBloque.TRANSPORTE, 1);

        return new MetricaCiudad(10, 7, 3, 0.70, 0.30, conteo);
    }

    private Map<TipoBloque, Integer> crearConteoBase() {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            conteo.put(tipo, 0);
        }
        return conteo;
    }
}
