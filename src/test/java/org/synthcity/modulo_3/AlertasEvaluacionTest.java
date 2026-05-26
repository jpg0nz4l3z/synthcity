package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueIndustrial;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_2.SimuladorCiudad;

import static org.junit.jupiter.api.Assertions.*;

class AlertasEvaluacionTest {

    private final SimuladorCiudad simulador = new SimuladorCiudad();
    private final EvaluadorCiudad evaluador = new EvaluadorCiudad();

    @Test
    void deficitEnergetico_generaAlertaCorrecta() {
        Ciudad ciudad = new Ciudad("DefEnergia", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 1)));

        ResultadoEvaluacion re = evaluador.evaluar(simulador.simular(ciudad));

        assertTrue(re.getAlertas().contains(AlertaEvaluacion.DEFICIT_ENERGETICO));
    }

    @Test
    void deficitServicios_generaAlertaCorrecta() {
        Ciudad ciudad = new Ciudad("DefServicios", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 3)));

        ResultadoEvaluacion re = evaluador.evaluar(simulador.simular(ciudad));

        assertTrue(re.getAlertas().contains(AlertaEvaluacion.DEFICIT_SERVICIOS));
    }

    @Test
    void riesgoSaturacion_generaAlertaCorrecta() {
        Ciudad ciudad = new Ciudad("Saturada", 5, 5);

        for (int i = 0; i < 22; i++) {
            ciudad.addBloque(new BloqueEnergia(new Posicion(i / 5, i % 5)));
        }

        ResultadoEvaluacion re = evaluador.evaluar(simulador.simular(ciudad));

        assertTrue(re.getAlertas().contains(AlertaEvaluacion.RIESGO_SATURACION));
    }

    @Test
    void multiplesAlertas_seAcumulanBien() {
        Ciudad ciudad = new Ciudad("Multi", 5, 5);

        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 3)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 4)));

        for (int i = 5; i < 22; i++) {
            ciudad.addBloque(new BloqueResidencial(new Posicion(i / 5, i % 5)));
        }

        ResultadoEvaluacion re = evaluador.evaluar(simulador.simular(ciudad));

        assertTrue(re.getNumeroAlertas() >= 2);
        assertTrue(re.tieneAlertas());
    }
}