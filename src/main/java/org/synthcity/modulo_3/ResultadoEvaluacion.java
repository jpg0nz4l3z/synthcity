package org.synthcity.modulo_3;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ResultadoEvaluacion {

    private final String nombreCiudad;
    private final MetricaCiudad metricaCiudad;
    private final NivelEvaluacion nivelEvaluacion;
    private final String mensaje;
    private final double scoreViabilidad;
    private final Set<AlertaEvaluacion> alertas;
    private final String resumenRiesgo;

    public ResultadoEvaluacion(String nombreCiudad,
                               MetricaCiudad metricaCiudad,
                               NivelEvaluacion nivelEvaluacion,
                               String mensaje) {
        this(nombreCiudad, metricaCiudad, nivelEvaluacion, mensaje, 0.0, EnumSet.noneOf(AlertaEvaluacion.class), "Sin información de riesgo.");
    }

    public ResultadoEvaluacion(String nombreCiudad,
                               MetricaCiudad metricaCiudad,
                               NivelEvaluacion nivelEvaluacion,
                               String mensaje,
                               double scoreViabilidad,
                               Set<AlertaEvaluacion> alertas,
                               String resumenRiesgo) {

        if (nombreCiudad == null || nombreCiudad.isBlank()) {
            throw new ResultadoSimulacionInvalidoException("El nombre de la ciudad no puede ser nulo ni vacío.");
        }

        if (metricaCiudad == null || nivelEvaluacion == null || mensaje == null) {
            throw new ResultadoSimulacionInvalidoException("El resultado de evaluación no puede estar incompleto.");
        }
        if (scoreViabilidad < 0.0 || scoreViabilidad > 100.0) {
            throw new ResultadoSimulacionInvalidoException("El score de viabilidad debe estar entre 0 y 100.");
        }
        if (alertas == null) {
            throw new ResultadoSimulacionInvalidoException("El conjunto de alertas no puede ser nulo.");
        }
        if (resumenRiesgo == null) {
            throw new ResultadoSimulacionInvalidoException("El resumen de riesgo no puede ser nulo.");
        }

        this.nombreCiudad = nombreCiudad;
        this.metricaCiudad = metricaCiudad;
        this.nivelEvaluacion = nivelEvaluacion;
        this.mensaje = mensaje;
        this.scoreViabilidad = scoreViabilidad;
        Set<AlertaEvaluacion> copia = EnumSet.noneOf(AlertaEvaluacion.class);
        copia.addAll(alertas);
        this.alertas = Collections.unmodifiableSet(copia);
        this.resumenRiesgo = resumenRiesgo;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public MetricaCiudad getMetricaCiudad() {
        return metricaCiudad;
    }

    public NivelEvaluacion getNivelEvaluacion() {
        return nivelEvaluacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public double getScoreViabilidad() {
        return scoreViabilidad;
    }

    public Set<AlertaEvaluacion> getAlertas() {
        return alertas;
    }

    public List<AlertaEvaluacion> getAlertasOrdenadas() {
        return alertas.stream().sorted().toList();
    }

    public String getResumenRiesgo() {
        return resumenRiesgo;
    }

    public boolean tieneAlertas() {
        return !alertas.isEmpty();
    }

    public int getNumeroAlertas() {
        return alertas.size();
    }

    public boolean esViable() {
        return nivelEvaluacion == NivelEvaluacion.FUNCIONAL
                || nivelEvaluacion == NivelEvaluacion.OPTIMO;
    }

    @Override
    public String toString() {
        return "Ciudad: " + nombreCiudad +
                " | Nivel: " + nivelEvaluacion +
                " | Score: " + String.format("%.2f", scoreViabilidad) +
                " | Alertas: " + alertas.size() +
                " | Mensaje: " + mensaje;
    }
}
