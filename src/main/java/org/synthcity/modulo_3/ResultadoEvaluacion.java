package org.synthcity.modulo_3;

public class ResultadoEvaluacion {

    private final String nombreCiudad;
    private final MetricaCiudad metricaCiudad;
    private final NivelEvaluacion nivelEvaluacion;
    private final String mensaje;

    public ResultadoEvaluacion(String nombreCiudad,
                               MetricaCiudad metricaCiudad,
                               NivelEvaluacion nivelEvaluacion,
                               String mensaje) {

        if (metricaCiudad == null || nivelEvaluacion == null || mensaje == null) {
            throw new IllegalArgumentException();
        }

        this.nombreCiudad = nombreCiudad;
        this.metricaCiudad = metricaCiudad;
        this.nivelEvaluacion = nivelEvaluacion;
        this.mensaje = mensaje;
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

    @Override
    public String toString() {
        return "Ciudad: " + nombreCiudad +
                " | Nivel: " + nivelEvaluacion +
                " | Mensaje: " + mensaje;
    }
}