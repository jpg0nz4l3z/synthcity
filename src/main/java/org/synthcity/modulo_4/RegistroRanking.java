package org.synthcity.modulo_4;

public class RegistroRanking {
    private final String nombreCiudad;
    private final double scoreViabilidad;
    private final String nivelEvaluacion;

    public RegistroRanking(String nombreCiudad, double scoreViabilidad, String nivelEvaluacion) {
        this.nombreCiudad = nombreCiudad;
        this.scoreViabilidad = scoreViabilidad;
        this.nivelEvaluacion = nivelEvaluacion;
    }

    public String getNombreCiudad() { return nombreCiudad; }
    public double getScoreViabilidad() { return scoreViabilidad; }
    public String getNivelEvaluacion() { return nivelEvaluacion; }

}