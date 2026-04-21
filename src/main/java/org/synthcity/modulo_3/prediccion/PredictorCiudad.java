package org.synthcity.modulo_3.prediccion;

import org.synthcity.modulo_3.TendenciaPredicha;

public class PredictorCiudad {

    private static final double UMBRAL_ACTIVIDAD_RIESGO_ALTO = 0.40;
    private static final double UMBRAL_ACTIVIDAD_BALANCE = 0.70;
    private static final double UMBRAL_DENSIDAD_SATURACION = 0.85;
    private static final double UMBRAL_DEFICIT_RATIO = 0.80;
    private static final int UMBRAL_CONTAMINACION_ALTA = 60;

    private static final double PESO_ACTIVIDAD = 30.0;
    private static final double PESO_ENERGIA = 20.0;
    private static final double PESO_SERVICIOS = 20.0;
    private static final double PESO_ESTABILIDAD = 15.0;
    private static final double PESO_BIENESTAR = 15.0;
    private static final double PENAL_CONTAMINACION = 10.0;
    private static final double PENAL_SATURACION = 10.0;

    public PredictionResult predecir(PredictionInput input) {
        if (input == null) {
            throw new IllegalArgumentException("PredictionInput no puede ser null.");
        }

        if (esCasoSinBase(input)) {
            return new PredictionResult(
                    TendenciaPredicha.SIN_BASE,
                    0.0,
                    0.20,
                    "No existe base suficiente para proyectar una tendencia futura."
            );
        }

        double score = calcularScore(input);
        TendenciaPredicha tendencia = determinarTendencia(input, score);
        double confianza = calcularConfianza(input, tendencia);
        String mensaje = generarMensaje(tendencia, score);

        return new PredictionResult(tendencia, score, confianza, mensaje);
    }

    private boolean esCasoSinBase(PredictionInput input) {
        return input.getTotalBloques() == 0;
    }

    private double calcularScore(PredictionInput input) {
        double score = 0.0;

        score += input.getPorcentajeActivos() * PESO_ACTIVIDAD;
        score += Math.min(1.0, input.getRatioEnergetico()) * PESO_ENERGIA;
        score += Math.min(1.0, input.getRatioCoberturaServicios()) * PESO_SERVICIOS;
        score += input.getEstabilidadBasica() * PESO_ESTABILIDAD;
        score += input.getBienestar() * PESO_BIENESTAR;

        if (input.getContaminacion() >= UMBRAL_CONTAMINACION_ALTA) {
            score -= PENAL_CONTAMINACION;
        }
        if (input.getDensidad() >= UMBRAL_DENSIDAD_SATURACION) {
            score -= PENAL_SATURACION;
        }

        if (score < 0.0) return 0.0;
        if (score > 100.0) return 100.0;
        return score;
    }

    private TendenciaPredicha determinarTendencia(PredictionInput input, double score) {
        if (input.getPorcentajeActivos() < UMBRAL_ACTIVIDAD_RIESGO_ALTO) {
            return TendenciaPredicha.RIESGO_ALTO;
        }

        boolean hayDeficitEnergeticoOServicios =
                input.getRatioEnergetico() < UMBRAL_DEFICIT_RATIO
                        || input.getRatioCoberturaServicios() < UMBRAL_DEFICIT_RATIO;

        if (input.getDensidad() >= UMBRAL_DENSIDAD_SATURACION && hayDeficitEnergeticoOServicios) {
            return TendenciaPredicha.SATURACION_PROBABLE;
        }

        if (input.getPorcentajeActivos() >= UMBRAL_ACTIVIDAD_BALANCE
                && !hayDeficitEnergeticoOServicios
                && input.getContaminacion() < UMBRAL_CONTAMINACION_ALTA) {
            if (score >= 70.0) {
                return TendenciaPredicha.MEJORA_PROBABLE;
            }
            return TendenciaPredicha.ESTABLE;
        }

        return TendenciaPredicha.RIESGO_MODERADO;
    }

    private double calcularConfianza(PredictionInput input, TendenciaPredicha tendencia) {
        if (tendencia == TendenciaPredicha.SIN_BASE) {
            return 0.20;
        }

        double confianza = 0.60;

        if (input.getTotalBloques() >= 10) {
            confianza += 0.10;
        }
        if (input.getConteoPorTipo().size() >= 3) {
            confianza += 0.10;
        }
        if (input.getEstabilidadBasica() >= 0.60) {
            confianza += 0.10;
        }

        return Math.min(confianza, 0.95);
    }

    private String generarMensaje(TendenciaPredicha tendencia, double score) {
        return switch (tendencia) {
            case SIN_BASE ->
                    "No se puede predecir una tendencia consistente porque no hay base estructural suficiente.";
            case ESTABLE ->
                    "La ciudad presenta una proyección estable con score " + String.format(java.util.Locale.ROOT, "%.2f", score) + ".";
            case MEJORA_PROBABLE ->
                    "La ciudad muestra condiciones favorables para una evolución positiva a corto plazo.";
            case RIESGO_MODERADO ->
                    "La ciudad presenta señales de riesgo moderado que pueden agravarse si no se corrigen desequilibrios.";
            case RIESGO_ALTO ->
                    "La ciudad presenta riesgo alto por falta de actividad suficiente para sostener el sistema.";
            case SATURACION_PROBABLE ->
                    "La ciudad muestra alta densidad con déficits internos, lo que indica saturación probable.";
        };
    }
}