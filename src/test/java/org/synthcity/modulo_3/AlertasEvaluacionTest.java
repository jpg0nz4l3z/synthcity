package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AlertasEvaluacionTest {

    private final EvaluadorCiudad evaluador = new EvaluadorCiudad();

    private Map<TipoBloque, Integer> conteoBase() {
        Map<TipoBloque, Integer> m = new EnumMap<>(TipoBloque.class);
        for (TipoBloque t : TipoBloque.values()) {
            m.put(t, 0);
        }
        return m;
    }

    // D.41 — déficit energético genera alerta correcta.
    // Escenario: consumo > producción; cobertura de servicios sana; densidad baja;
    // estabilidad y actividad suficientes para que la única alerta esperable sea DEFICIT_ENERGETICO.
    @Test
    void deficitEnergetico_generaAlertaCorrecta() {
        Map<TipoBloque, Integer> conteo = conteoBase();
        conteo.put(TipoBloque.RESIDENCIAL, 3);
        conteo.put(TipoBloque.SERVICIOS, 3);

        ResultadoSimulacion sim = new ResultadoSimulacion(
                "SoloEnergia", 5, 5, 25, 6, 5, 1, conteo, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(sim);

        Set<AlertaEvaluacion> alertas = res.getAlertas();
        assertTrue(alertas.contains(AlertaEvaluacion.DEFICIT_ENERGETICO));
        assertFalse(alertas.contains(AlertaEvaluacion.DEFICIT_SERVICIOS));
        assertFalse(alertas.contains(AlertaEvaluacion.RIESGO_SATURACION));
    }

    // D.42 — déficit de servicios genera alerta correcta.
    // Escenario: producción energética suficiente; cobertura de servicios insuficiente
    // frente a la demanda residencial; resto de factores sanos.
    @Test
    void deficitServicios_generaAlertaCorrecta() {
        Map<TipoBloque, Integer> conteo = conteoBase();
        conteo.put(TipoBloque.RESIDENCIAL, 5);
        conteo.put(TipoBloque.ENERGIA, 2);
        conteo.put(TipoBloque.SERVICIOS, 1);

        ResultadoSimulacion sim = new ResultadoSimulacion(
                "SoloServicios", 5, 5, 25, 8, 7, 1, conteo, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(sim);

        Set<AlertaEvaluacion> alertas = res.getAlertas();
        assertTrue(alertas.contains(AlertaEvaluacion.DEFICIT_SERVICIOS));
        assertFalse(alertas.contains(AlertaEvaluacion.DEFICIT_ENERGETICO));
        assertFalse(alertas.contains(AlertaEvaluacion.RIESGO_SATURACION));
    }

    // D.43 — riesgo de saturación genera alerta correcta.
    // Escenario: total/capacidad >= 0.85 con un mix equilibrado para aislar la alerta.
    @Test
    void riesgoSaturacion_generaAlertaCorrecta() {
        Map<TipoBloque, Integer> conteo = conteoBase();
        conteo.put(TipoBloque.RESIDENCIAL, 2);
        conteo.put(TipoBloque.ENERGIA, 2);
        conteo.put(TipoBloque.SERVICIOS, 2);
        conteo.put(TipoBloque.TRANSPORTE, 2);

        ResultadoSimulacion sim = new ResultadoSimulacion(
                "Saturada", 3, 3, 9, 8, 7, 1, conteo, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(sim);

        Set<AlertaEvaluacion> alertas = res.getAlertas();
        assertTrue(alertas.contains(AlertaEvaluacion.RIESGO_SATURACION));
        assertFalse(alertas.contains(AlertaEvaluacion.DEFICIT_ENERGETICO));
        assertFalse(alertas.contains(AlertaEvaluacion.DEFICIT_SERVICIOS));
    }

    // D.44 — múltiples alertas se acumulan bien.
    // Escenario residencial-mayoritario sin energía ni servicios: deben aparecer
    // simultáneamente déficit energético, déficit de servicios y estabilidad insuficiente.
    @Test
    void multiplesAlertas_seAcumulanBien() {
        Map<TipoBloque, Integer> conteo = conteoBase();
        conteo.put(TipoBloque.RESIDENCIAL, 10);

        ResultadoSimulacion sim = new ResultadoSimulacion(
                "Acumulada", 5, 5, 25, 10, 5, 5, conteo, EstadoSimulacion.EJECUTADA);
        ResultadoEvaluacion res = evaluador.evaluar(sim);

        Set<AlertaEvaluacion> alertas = res.getAlertas();
        assertTrue(alertas.contains(AlertaEvaluacion.DEFICIT_ENERGETICO));
        assertTrue(alertas.contains(AlertaEvaluacion.DEFICIT_SERVICIOS));
        assertTrue(alertas.contains(AlertaEvaluacion.ESTABILIDAD_INSUFICIENTE));
        assertTrue(res.tieneAlertas());
        assertTrue(res.getNumeroAlertas() >= 3);
    }
}
