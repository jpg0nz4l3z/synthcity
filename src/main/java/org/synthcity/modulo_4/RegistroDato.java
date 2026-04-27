package org.synthcity.modulo_4;

public class RegistroDato {

    private final String nombreCiudad;
    private final double densidad;
    private final double ratioEnergetico;
    private final double coberturaServicios;
    private final double contaminacion;
    private final double estabilidad;
    private final double scoreViabilidad;
    private final int objetivo;

    public RegistroDato(String nombreCiudad,
                        double densidad,
                        double ratioEnergetico,
                        double coberturaServicios,
                        double contaminacion,
                        double estabilidad,
                        double scoreViabilidad,
                        int objetivo) {
        this.nombreCiudad      = nombreCiudad;
        this.densidad          = densidad;
        this.ratioEnergetico   = ratioEnergetico;
        this.coberturaServicios = coberturaServicios;
        this.contaminacion     = contaminacion;
        this.estabilidad       = estabilidad;
        this.scoreViabilidad   = scoreViabilidad;
        this.objetivo          = objetivo;
    }


    public double[] toArray() {
        return new double[]{
                densidad,
                ratioEnergetico,
                coberturaServicios,
                contaminacion,
                estabilidad,
                scoreViabilidad
        };
    }

    public static String[] getFeatureNames() {
        return new String[]{
                "densidad",
                "ratio_energetico",
                "cobertura_servicios",
                "contaminacion",
                "estabilidad",
                "score_viabilidad"
        };
    }

    public String getNombreCiudad()    { return nombreCiudad; }
    public double getDensidad()        { return densidad; }
    public double getRatioEnergetico() { return ratioEnergetico; }
    public double getContaminacion()   { return contaminacion; }
    public double getEstabilidad()     { return estabilidad; }
    public double getScoreViabilidad() { return scoreViabilidad; }
    public int getObjetivo()           { return objetivo; }
}