package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.*;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;

import static org.junit.jupiter.api.Assertions.*;

class EvaluadorCiudadTest {

    private final SimuladorCiudad simulador = new SimuladorCiudad();
    private final EvaluadorCiudad evaluador = new EvaluadorCiudad();

    @Test
    void resultadoSimulacionNulo_lanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                evaluador.evaluar(null));
    }

    @Test
    void ciudadVacia_devuelveSinDatos() {
        Ciudad ciudad = new Ciudad("Vacia", 10, 10);
        ResultadoSimulacion rs = simulador.simular(ciudad);

        ResultadoEvaluacion re = evaluador.evaluar(rs);

        assertEquals(NivelEvaluacion.SIN_DATOS, re.getNivelEvaluacion());
    }

    @Test
    void actividadCero_devuelveCritico() {
        Ciudad ciudad = new Ciudad("Critica", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.desactivarBloque(new Posicion(0, 0));

        ResultadoSimulacion rs = simulador.simular(ciudad);
        ResultadoEvaluacion re = evaluador.evaluar(rs);

        assertEquals(NivelEvaluacion.CRITICO, re.getNivelEvaluacion());
    }

    @Test
    void buenaActividadPeroMalaEnergia_devuelveInestableOCritico() {
        Ciudad ciudad = new Ciudad("EnergiaMala", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 2)));

        ResultadoSimulacion rs = simulador.simular(ciudad);
        ResultadoEvaluacion re = evaluador.evaluar(rs);

        assertTrue(
                re.getNivelEvaluacion() == NivelEvaluacion.INESTABLE ||
                        re.getNivelEvaluacion() == NivelEvaluacion.CRITICO
        );
    }

    @Test
    void actividadAltaEnergiaYServiciosCorrectosContaminacionBaja_devuelveFuncionalUOptimo() {
        Ciudad ciudad = new Ciudad("Buena", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 3)));

        ResultadoSimulacion rs = simulador.simular(ciudad);
        ResultadoEvaluacion re = evaluador.evaluar(rs);

        assertTrue(
                re.getNivelEvaluacion() == NivelEvaluacion.FUNCIONAL ||
                        re.getNivelEvaluacion() == NivelEvaluacion.OPTIMO
        );
    }

    @Test
    void scoreViabilidad_enRangoValido() {
        Ciudad ciudad = new Ciudad("Score", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));

        ResultadoEvaluacion re = evaluador.evaluar(simulador.simular(ciudad));

        assertTrue(re.getScoreViabilidad() >= 0.0);
        assertTrue(re.getScoreViabilidad() <= 100.0);
    }

    @Test
    void resultadoEvaluacion_esAutosuficiente() {
        Ciudad ciudad = new Ciudad("Auto", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));

        ResultadoEvaluacion re = evaluador.evaluar(simulador.simular(ciudad));

        assertNotNull(re.getNombreCiudad());
        assertNotNull(re.getMetricaCiudad());
        assertNotNull(re.getNivelEvaluacion());
        assertNotNull(re.getMensaje());
        assertNotNull(re.getResumenRiesgo());
        assertNotNull(re.getAlertas());
    }

    @Test
    void modulo4NoNecesitaVolverARresultadoSimulacionParaCompletarSemantica() {
        Ciudad ciudad = new Ciudad("Integracion", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));

        ResultadoEvaluacion re = evaluador.evaluar(simulador.simular(ciudad));

        assertNotNull(re.getNombreCiudad());
        assertNotNull(re.getNivelEvaluacion());
        assertNotNull(re.getMensaje());
        assertNotNull(re.getMetricaCiudad());
        assertTrue(re.getScoreViabilidad() >= 0.0);
    }
}