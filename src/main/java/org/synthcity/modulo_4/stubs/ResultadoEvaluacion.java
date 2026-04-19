package org.synthcity.modulo_4.stubs;

public class ResultadoEvaluacion {

    private final String nombreCiudad;
    private final MetricaCiudad metricaCiudad;
    private final NivelEvaluacion nivelEvaluacion;
    private final String mensaje;

    public ResultadoEvaluacion(
            String nombreCiudad,
            MetricaCiudad metricaCiudad,
            NivelEvaluacion nivelEvaluacion,
            String mensaje
    ) {
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
}
