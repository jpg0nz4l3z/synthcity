package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueIndustrial;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_1.bloques.BloqueServicios;
import org.synthcity.modulo_1.bloques.BloqueTransporte;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;
import org.synthcity.modulo_3.ResultadoEvaluacion;

import static org.junit.jupiter.api.Assertions.*;

class FormateadorResultadoTest {

    private final FormateadorResultado formateador = new FormateadorResultado();

    @Test
    void resultadoNulo_lanzaFormatoSalidaException() {
        assertThrows(FormatoSalidaException.class, () -> formateador.formatear(null));
    }

    @Test
    void salidaIncluyeNombreNivelMensajeYMetricas() {
        ResultadoEvaluacion evaluacion = crearEvaluacionMixta();

        String salida = formateador.formatear(evaluacion);

        assertTrue(salida.contains("Nombre de la ciudad: NeoMadrid"));
        assertTrue(salida.contains("Nivel de evaluacion: " + evaluacion.getNivelEvaluacion()));
        assertTrue(salida.contains("Interpretacion:"));
        assertTrue(salida.contains(evaluacion.getMensaje()));
        assertTrue(salida.contains("Bloques totales:"));
        assertTrue(salida.contains("Bloques activos:"));
        assertTrue(salida.contains("Bloques inactivos:"));
        assertTrue(salida.contains("Porcentaje de actividad:"));
        assertTrue(salida.contains("Porcentaje de inactividad:"));
    }

    @Test
    void distribucionPorTipoSeMuestraCorrectamente() {
        ResultadoEvaluacion evaluacion = crearEvaluacionMixta();

        String salida = formateador.formatear(evaluacion);

        for (TipoBloque tipo : TipoBloque.values()) {
            String nombre = formatearNombreTipo(tipo);
            assertTrue(salida.contains(nombre + ":"), "Falta el tipo " + tipo);
        }
    }

    @Test
    void salidaIncluyeVariablesUrbanasSprint2() {
        ResultadoEvaluacion evaluacion = crearEvaluacionMixta();

        String salida = formateador.formatear(evaluacion);

        assertTrue(salida.contains("Densidad:"));
        assertTrue(salida.contains("Energia producida/consumida:"));
        assertTrue(salida.contains("Ratio energetico:"));
        assertTrue(salida.contains("Servicios demanda/cobertura:"));
        assertTrue(salida.contains("Ratio servicios:"));
        assertTrue(salida.contains("Contaminacion:"));
        assertTrue(salida.contains("Bienestar:"));
        assertTrue(salida.contains("Estabilidad:"));
    }

    @Test
    void salidaEsLegibleYCompleta() {
        ResultadoEvaluacion evaluacion = crearEvaluacionMixta();

        String salida = formateador.formatear(evaluacion);

        assertTrue(salida.startsWith("========================================"));
        assertTrue(salida.contains("INFORME DE ESTADO DE CIUDAD"));
        assertTrue(salida.contains("RESUMEN ESTRUCTURAL"));
        assertTrue(salida.contains("DISTRIBUCION POR TIPO"));
        assertTrue(salida.contains("VARIABLES URBANAS"));
        assertTrue(salida.contains("FIN DEL INFORME"));
    }

    private ResultadoEvaluacion crearEvaluacionMixta() {
        Ciudad ciudad = new Ciudad("NeoMadrid", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueServicios(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(2, 2)));

        ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);
        return new EvaluadorCiudad().evaluar(simulacion);
    }

    private String formatearNombreTipo(TipoBloque tipo) {
        String nombre = tipo.name().toLowerCase(java.util.Locale.ROOT);
        return Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1);
    }
}