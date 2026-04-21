package org.synthcity.modulo_3;

public class PredictionResult {

    private final TendenciaPredicha tendenciaPredicha;
    private final double scorePredicho;
    private final double confianza;
    private final String mensajePrediccion;

    public PredictionResult(
            TendenciaPredicha tendenciaPredicha,
            double scorePredicho,
            double confianza,
            String mensajePrediccion
    ) {
        if (tendenciaPredicha == null) {
            throw new IllegalArgumentException("La tendencia predicha no puede ser null.");
        }
        if (scorePredicho < 0.0 || scorePredicho > 100.0) {
            throw new IllegalArgumentException("El score predicho debe estar entre 0 y 100.");
        }
        if (confianza < 0.0 || confianza > 1.0) {
            throw new IllegalArgumentException("La confianza debe estar entre 0.0 y 1.0.");
        }
        if (mensajePrediccion == null || mensajePrediccion.isBlank()) {
            throw new IllegalArgumentException("El mensaje de prediccion no puede ser null ni vacio.");
        }

        this.tendenciaPredicha = tendenciaPredicha;
        this.scorePredicho = scorePredicho;
        this.confianza = confianza;
        this.mensajePrediccion = mensajePrediccion;
    }

    public TendenciaPredicha getTendenciaPredicha() {
        return tendenciaPredicha;
    }

    public double getScorePredicho() {
        return scorePredicho;
    }

    public double getConfianza() {
        return confianza;
    }

    public String getMensajePrediccion() {
        return mensajePrediccion;
    }

    public TendenciaPredicha getTendencia() {
        return tendenciaPredicha;
    }

    public double getScore() {
        return scorePredicho;
    }

    public String getMensaje() {
        return mensajePrediccion;
    }

    @Override
    public String toString() {
        return "PredictionResult{" +
                "tendenciaPredicha=" + tendenciaPredicha +
                ", scorePredicho=" + scorePredicho +
                ", confianza=" + confianza +
                ", mensajePrediccion='" + mensajePrediccion + '\'' +
                '}';
    }
}