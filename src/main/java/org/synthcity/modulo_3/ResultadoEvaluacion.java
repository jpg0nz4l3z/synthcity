package org.synthcity.modulo_3;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class ResultadoEvaluacion {

    private final String nombreCiudad;
    private final MetricaCiudad metricaCiudad;
    private final NivelEvaluacion nivelEvaluacion;
    private final String mensaje;

    // Atributos obligatorios Sprint 3
    private final double scoreViabilidad;
    private final TendenciaTemporal tendenciaTemporal;
    private final List<AlertaEvaluacion> alertas;
    private final boolean expansionEjecutada;
    private final int ciclosSimulados;

    public ResultadoEvaluacion(String nombreCiudad,
                               MetricaCiudad metricaCiudad,
                               NivelEvaluacion nivel,
                               String mensaje,
                               double score,
                               TendenciaTemporal tendencia,
                               List<AlertaEvaluacion> alertas,
                               boolean expansion,
                               int ciclos) {

        // Validaciones de seguridad para evitar NullPointerException
        if (nombreCiudad == null || metricaCiudad == null || nivel == null || tendencia == null) {
            throw new IllegalArgumentException("Los datos fundamentales de la evaluación no pueden ser nulos.");
        }

        this.nombreCiudad = nombreCiudad;
        this.metricaCiudad = metricaCiudad;
        this.nivelEvaluacion = nivel;
        this.mensaje = (mensaje != null) ? mensaje : "Sin comentarios adicionales.";
        this.scoreViabilidad = score;
        this.tendenciaTemporal = tendencia;
        this.alertas = (alertas != null) ? Collections.unmodifiableList(new ArrayList<>(alertas)) : Collections.emptyList();
        this.expansionEjecutada = expansion;
        this.ciclosSimulados = ciclos;
    }

    public String getNombreCiudad() { return nombreCiudad; }
    public MetricaCiudad getMetricaCiudad() { return metricaCiudad; }
    public NivelEvaluacion getNivelEvaluacion() { return nivelEvaluacion; }
    public String getMensaje() { return mensaje; }

    // Getters Nuevos Sprint 3
    public double getScoreViabilidad() { return scoreViabilidad; }
    public TendenciaTemporal getTendenciaTemporal() { return tendenciaTemporal; }
    public List<AlertaEvaluacion> getAlertas() { return alertas; }
    public boolean fueExpandida() { return expansionEjecutada; }
    public int getCiclosSimulados() { return ciclosSimulados; }

    @Override
    public String toString() {
        return String.format("Ciudad: %s | Nivel: %s | Score: %.2f | Tendencia: %s | Alertas: %d",
                nombreCiudad, nivelEvaluacion, scoreViabilidad, tendenciaTemporal, alertas.size());
    }
}