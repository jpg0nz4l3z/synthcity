package org.synthcity.modulo_3;

public class RegistroDato {

    // Features congeladas
    private final double densidad;
    private final double ratioEnergetico;
    private final double ratioCoberturaServicios;
    private final double contaminacion;
    private final double contaminacionAcumulada;
    private final double estabilidadMedia;
    private final double tendenciaEstabilidad;
    private final double tendenciaContaminacion;
    private final double bienestar;
    private final double scoreViabilidad;
    private final int ciclosEjecutados;
    private final boolean colapsoDetectado;
    private final boolean saturacionDetectada;

    // Variable Objetivo
    // 0=MUY_DESFAVORABLE, 1=DESFAVORABLE, 2=ACEPTABLE, 3=FAVORABLE, 4=OPTIMO
    private final String objetivo;

    public RegistroDato(double densidad, double ratioEnergetico, double ratioCoberturaServicios,
                        double contaminacion, double contaminacionAcumulada, double estabilidadMedia,
                        double tendenciaEstabilidad, double tendenciaContaminacion, double bienestar,
                        double scoreViabilidad, int ciclosEjecutados, boolean colapsoDetectado,
                        boolean saturacionDetectada, int nivelEvaluacionNumerico) {
        this.densidad = densidad;
        this.ratioEnergetico = ratioEnergetico;
        this.ratioCoberturaServicios = ratioCoberturaServicios;
        this.contaminacion = contaminacion;
        this.contaminacionAcumulada = contaminacionAcumulada;
        this.estabilidadMedia = estabilidadMedia;
        this.tendenciaEstabilidad = tendenciaEstabilidad;
        this.tendenciaContaminacion = tendenciaContaminacion;
        this.bienestar = bienestar;
        this.scoreViabilidad = scoreViabilidad;
        this.ciclosEjecutados = ciclosEjecutados;
        this.colapsoDetectado = colapsoDetectado;
        this.saturacionDetectada = saturacionDetectada;

        this.objetivo = String.valueOf(nivelEvaluacionNumerico);
    }

    /**
     * ¡CRÍTICO PARA EL SPRINT 4! No cambiar el orden.
     */
    public double[] toArray() {
        return new double[]{
                densidad,
                ratioEnergetico,
                ratioCoberturaServicios,
                contaminacion,
                contaminacionAcumulada,
                estabilidadMedia,
                tendenciaEstabilidad,
                tendenciaContaminacion,
                bienestar,
                scoreViabilidad,
                (double) ciclosEjecutados,
                colapsoDetectado ? 1.0 : 0.0,
                saturacionDetectada ? 1.0 : 0.0
        };
    }

    public String getObjetivo() {
        return objetivo;
    }

    // --- Getters de todas las features ---
    public double getDensidad() { return densidad; }
    public double getRatioEnergetico() { return ratioEnergetico; }
    public double getRatioCoberturaServicios() { return ratioCoberturaServicios; }
    public double getContaminacion() { return contaminacion; }
    public double getContaminacionAcumulada() { return contaminacionAcumulada; }
    public double getEstabilidadMedia() { return estabilidadMedia; }
    public double getTendenciaEstabilidad() { return tendenciaEstabilidad; }
    public double getTendenciaContaminacion() { return tendenciaContaminacion; }
    public double getBienestar() { return bienestar; }
    public double getScoreViabilidad() { return scoreViabilidad; }
    public int getCiclosEjecutados() { return ciclosEjecutados; }
    public boolean isColapsoDetectado() { return colapsoDetectado; }
    public boolean isSaturacionDetectada() { return saturacionDetectada; }

    @Override
    public String toString() {
        return "Objetivo: " + objetivo;
    }
}
