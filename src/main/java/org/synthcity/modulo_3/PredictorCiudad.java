package org.synthcity.modulo_3;

import org.synthcity.modulo_2.EstadoSimulacion;

public class PredictorCiudad {

    public PredictionResult predecir(PredictionInput input) {
        if (input == null) {
            throw new IllegalArgumentException("PredictionInput no puede ser null.");
        }

        if (input.getTotalBloques() == 0 || input.getEstadoSimulacion() == EstadoSimulacion.CIUDAD_VACIA) {
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

    private double calcularScore(PredictionInput input) {
        double score = 0.0;

        score += input.getPorcentajeActivos() * 35.0;
        score += input.getDiversidadTipos() * 20.0;
        score += input.getRatioEnergia() * 10.0;
        score += input.getRatioServicios() * 10.0;
        score += input.getRatioTransporte() * 10.0;
        score += input.getIndiceEquilibrioBase() * 15.0;

        if (input.getPresionIndustrial() > 0.40) {
            score -= 8.0;
        }

        if (input.getDensidadOcupacion() > 0.85) {
            score -= 7.0;
        }

        if (score < 0.0) {
            return 0.0;
        }
        if (score > 100.0) {
            return 100.0;
        }
        return score;
    }

    private TendenciaPredicha determinarTendencia(PredictionInput input, double score) {
        if (input.getEstadoSimulacion() == EstadoSimulacion.SIN_BLOQUES_ACTIVOS) {
            return TendenciaPredicha.RIESGO_OPERATIVO;
        }

        if (input.getPorcentajeActivos() < 0.40) {
            return TendenciaPredicha.RIESGO_OPERATIVO;
        }

        boolean desequilibrio = input.getRatioEnergia() == 0.0
                || input.getRatioServicios() == 0.0
                || input.getDiversidadTipos() < 0.40;

        if (desequilibrio) {
            return TendenciaPredicha.DESEQUILIBRIO_ESTRUCTURAL;
        }

        if (score >= 75.0 && input.getPorcentajeActivos() >= 0.70) {
            return TendenciaPredicha.EXPANSION_SALUDABLE;
        }

        if (score >= 60.0) {
            return TendenciaPredicha.ESTABLE;
        }

        return TendenciaPredicha.RECUPERACION_PROBABLE;
    }

    private double calcularConfianza(PredictionInput input, TendenciaPredicha tendencia) {
        double confianza = 0.60;

        if (input.getTotalBloques() >= 10) {
            confianza += 0.10;
        }
        if (input.getDiversidadTipos() >= 0.60) {
            confianza += 0.10;
        }
        if (tendencia == TendenciaPredicha.SIN_BASE) {
            confianza = 0.20;
        }

        if (confianza > 0.95) {
            return 0.95;
        }
        return confianza;
    }

    private String generarMensaje(TendenciaPredicha tendencia, double score) {
        return switch (tendencia) {
            case SIN_BASE ->
                    "No se puede predecir una tendencia consistente porque no hay base estructural suficiente.";
            case RIESGO_OPERATIVO ->
                    "La ciudad presenta riesgo operativo por falta de actividad suficiente para sostener el sistema.";
            case DESEQUILIBRIO_ESTRUCTURAL ->
                    "La ciudad muestra desequilibrios estructurales que pueden comprometer su evolución.";
            case ESTABLE ->
                    "La ciudad presenta una proyeccion estable con score " + String.format("%.2f", score) + ".";
            case RECUPERACION_PROBABLE ->
                    "La ciudad podria evolucionar favorablemente si consolida su actividad y su equilibrio interno.";
            case EXPANSION_SALUDABLE ->
                    "La ciudad muestra condiciones favorables para una expansion saludable y sostenida.";
        };
    }
}
