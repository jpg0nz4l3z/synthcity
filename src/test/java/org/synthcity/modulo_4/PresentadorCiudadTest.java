package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;
import org.synthcity.modulo_3.EvaluadorCiudad;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_3.prediccion.PredictionResult;

import static org.junit.jupiter.api.Assertions.*;

class PresentadorCiudadTest {

    private final PresentadorCiudad presentador = new PresentadorCiudad();

    @Test
    void resultadoNulo_lanzaFormatoSalidaException() {
        assertThrows(FormatoSalidaException.class, () -> presentador.presentar(null));
    }

    @Test
    void presentarResultadoValidoCiudadSinDatos_generaSalidaTexto() {
        ResultadoEvaluacion evaluacion = crearEvaluacion(new Ciudad("SinDatos", 10, 10));

        SalidaTexto salida = presentador.presentar(evaluacion);

        assertNotNull(salida);
        assertTrue(salida.getContenido().contains("SinDatos"));
        assertTrue(salida.getContenido().contains("Nivel de evaluacion:"));
    }

    @Test
    void presentarResultadoValidoCiudadCritica_generaSalidaTexto() {
        Ciudad ciudad = new Ciudad("Critica", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.desactivarBloque(new Posicion(0, 0));

        ResultadoEvaluacion evaluacion = crearEvaluacion(ciudad);

        SalidaTexto salida = presentador.presentar(evaluacion);

        assertTrue(salida.getContenido().contains("Critica"));
        assertTrue(salida.getContenido().contains("CRITICO"));
    }

    @Test
    void presentarResultadoValidoCiudadFuncionalOOptima_generaSalidaTexto() {
        Ciudad ciudad = new Ciudad("Funcional", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 0)));

        ResultadoEvaluacion evaluacion = crearEvaluacion(ciudad);

        SalidaTexto salida = presentador.presentar(evaluacion);

        assertTrue(salida.getContenido().contains("Funcional"));
        assertTrue(salida.getContenido().contains("Nivel de evaluacion:"));
        assertTrue(salida.getContenido().contains("Score de viabilidad:"));
    }

    @Test
    void generarResumenIncluyeCiudadEvaluacionYPrediccion() {
        Ciudad ciudad = new Ciudad("Resumen", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));

        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);
        ResultadoEvaluacion evaluacion = evaluador.evaluar(simulacion);
        PredictionResult prediccion = evaluador.predecir(simulacion);

        SalidaTexto resumen = presentador.generarResumen(ciudad, evaluacion, prediccion);

        assertTrue(resumen.getContenido().contains("Ciudad: Resumen"));
        assertTrue(resumen.getContenido().contains("Evaluacion:"));
        assertTrue(resumen.getContenido().contains("Prediccion:"));
        assertEquals("Resumen", resumen.getNombreCiudad());
        assertNotNull(resumen.getFechaGeneracion());
    }

    private ResultadoEvaluacion crearEvaluacion(Ciudad ciudad) {
        ResultadoSimulacion simulacion = new SimuladorCiudad().simular(ciudad);
        return new EvaluadorCiudad().evaluar(simulacion);
    }
}