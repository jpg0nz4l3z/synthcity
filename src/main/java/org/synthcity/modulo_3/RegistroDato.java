package org.synthcity.modulo_3;

public class RegistroDato {

    // Features obligatorias congeladas
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
    private final boolean colapsoDetectado;
    private final int ciclosEjecutados;
    private final boolean saturacionDetectada;

    // Variable objetivo codificada (0-4)
    private final int objetivo;

    public RegistroDato(double densidad, double ratioEnergetico, double ratioCoberturaServicios,
                        double contaminacion, double contaminacionAcumulada, double estabilidadMedia,
                        double tendenciaEstabilidad, double tendenciaContaminacion, double bienestar,
                        double scoreViabilidad, boolean colapsoDetectado, int ciclosEjecutados,
                        boolean saturacionDetectada, int nivelEvaluacionNumerico) {
        if (densidad < 0.0 || densidad > 1.0) {
            throw new ResultadoSimulacionInvalidoException("densidad inválida");
        }
        if (ratioEnergetico < 0.0 || ratioCoberturaServicios < 0.0) {
            throw new ResultadoSimulacionInvalidoException("ratios no pueden ser negativos");
        }
        if (estabilidadMedia < 0.0 || estabilidadMedia > 1.0) {
            throw new ResultadoSimulacionInvalidoException("estabilidadMedia inválida");
        }
        if (bienestar < 0.0 || bienestar > 1.0) {
            throw new ResultadoSimulacionInvalidoException("bienestar inválido");
        }
        if (scoreViabilidad < 0.0 || scoreViabilidad > 100.0) {
            throw new ResultadoSimulacionInvalidoException("scoreViabilidad inválido");
        }
        if (ciclosEjecutados < 0) {
            throw new ResultadoSimulacionInvalidoException("ciclosEjecutados no puede ser negativo");
        }
        if (contaminacion < 0.0 || contaminacionAcumulada < 0.0) {
            throw new ResultadoSimulacionInvalidoException("contaminación no puede ser negativa");
        }
        if (nivelEvaluacionNumerico < 0 || nivelEvaluacionNumerico > 4) {
            throw new ResultadoSimulacionInvalidoException("objetivo inválido");
        }


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
        this.colapsoDetectado = colapsoDetectado;
        this.ciclosEjecutados = ciclosEjecutados;
        this.saturacionDetectada = saturacionDetectada;

        this.objetivo = nivelEvaluacionNumerico;
    }


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
                colapsoDetectado ? 1.0 : 0.0,
                (double) ciclosEjecutados,
                saturacionDetectada ? 1.0 : 0.0
        };
    }


    public int getObjetivo() {
        return objetivo;
    }

    public double getDensidad() {
        return densidad;
    }

    public double getRatioEnergetico() {
        return ratioEnergetico;
    }

    public double getRatioCoberturaServicios() {
        return ratioCoberturaServicios;
    }

    public double getContaminacion() {
        return contaminacion;
    }

    public double getContaminacionAcumulada() {
        return contaminacionAcumulada;
    }

    public double getEstabilidadMedia() {
        return estabilidadMedia;
    }

    public double getTendenciaEstabilidad() {
        return tendenciaEstabilidad;
    }

    public double getTendenciaContaminacion() {
        return tendenciaContaminacion;
    }

    public double getBienestar() {
        return bienestar;
    }

    public double getScoreViabilidad() {
        return scoreViabilidad;
    }

    public boolean isColapsoDetectado() {
        return colapsoDetectado;
    }

    public int getCiclosEjecutados() {
        return ciclosEjecutados;
    }

    public boolean isSaturacionDetectada() {
        return saturacionDetectada;
    }


    @Override
    public String toString() {
        return "RegistroDato{" +
                "objetivo='" + objetivo + '\'' +
                ", scoreViabilidad=" + scoreViabilidad +
                ", ciclosEjecutados=" + ciclosEjecutados +
                ", colapsoDetectado=" + colapsoDetectado +
                ", saturacionDetectada=" + saturacionDetectada +
                '}';
    }
}