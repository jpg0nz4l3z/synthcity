package org.synthcity.modulo_3;

import java.util.ArrayList;
import java.util.List;

public class ComparadorCiudades {

    /**
     * Versión provisional y compatible con el contrato actual del repo.
     *
     * El documento revisado pide comparar por scoreViabilidad, pero como el
     * ResultadoEvaluacion actual todavía no expone score ni alertas, aquí usamos
     * un criterio estable y documentado:
     *
     * 1. NivelEvaluacion (OPTIMO > FUNCIONAL > INESTABLE > CRITICO > SIN_DATOS)
     * 2. porcentajeActivos (mayor es mejor)
     * 3. totalBloques (mayor es mejor)
     * 4. nombreCiudad (orden alfabético para estabilidad del resultado)
     *
     * Cuando Persona 1 amplíe ResultadoEvaluacion con scoreViabilidad, este método
     * debe migrarse a comparación por score.
     */
    public int compararPorViabilidad(ResultadoEvaluacion a, ResultadoEvaluacion b) {
        validarEvaluacion(a);
        validarEvaluacion(b);

        int comparacionNivel = Integer.compare(
                prioridadNivel(b.getNivelEvaluacion()),
                prioridadNivel(a.getNivelEvaluacion())
        );
        if (comparacionNivel != 0) {
            return comparacionNivel;
        }

        int comparacionActividad = Double.compare(
                b.getMetricaCiudad().getPorcentajeActivos(),
                a.getMetricaCiudad().getPorcentajeActivos()
        );
        if (comparacionActividad != 0) {
            return comparacionActividad;
        }

        int comparacionTamano = Integer.compare(
                b.getMetricaCiudad().getTotalBloques(),
                a.getMetricaCiudad().getTotalBloques()
        );
        if (comparacionTamano != 0) {
            return comparacionTamano;
        }

        String nombreA = a.getNombreCiudad() == null ? "" : a.getNombreCiudad();
        String nombreB = b.getNombreCiudad() == null ? "" : b.getNombreCiudad();

        return nombreA.compareToIgnoreCase(nombreB);
    }

    public List<ResultadoEvaluacion> ordenarPorViabilidad(List<ResultadoEvaluacion> evaluaciones) {
        if (evaluaciones == null) {
            throw new IllegalArgumentException("La lista de evaluaciones no puede ser null.");
        }

        List<ResultadoEvaluacion> copia = new ArrayList<>(evaluaciones);
        copia.sort(this::compararPorViabilidad);
        return copia;
    }

    /**
     * Coherencia mínima entre la evaluación actual y la predicción.
     * No exige igualdad, pero sí evita contradicciones absurdas.
     */
    public boolean esCoherenteConPrediccion(ResultadoEvaluacion evaluacion, PredictionResult prediccion) {
        validarEvaluacion(evaluacion);

        if (prediccion == null) {
            throw new IllegalArgumentException("La prediccion no puede ser null.");
        }

        NivelEvaluacion nivel = evaluacion.getNivelEvaluacion();
        TendenciaPredicha tendencia = prediccion.getTendenciaPredicha();

        if (nivel == NivelEvaluacion.SIN_DATOS) {
            return tendencia == TendenciaPredicha.SIN_BASE;
        }

        if (nivel == NivelEvaluacion.CRITICO) {
            return tendencia != TendenciaPredicha.MEJORA_PROBABLE
                    && tendencia != TendenciaPredicha.ESTABLE;
        }

        if (nivel == NivelEvaluacion.INESTABLE) {
            return tendencia != TendenciaPredicha.MEJORA_PROBABLE;
        }

        if (nivel == NivelEvaluacion.FUNCIONAL || nivel == NivelEvaluacion.OPTIMO) {
            return tendencia != TendenciaPredicha.RIESGO_ALTO;
        }

        return true;
    }

    private void validarEvaluacion(ResultadoEvaluacion evaluacion) {
        if (evaluacion == null) {
            throw new IllegalArgumentException("La evaluacion no puede ser null.");
        }
        if (evaluacion.getMetricaCiudad() == null) {
            throw new IllegalArgumentException("La evaluacion debe tener metrica.");
        }
        if (evaluacion.getNivelEvaluacion() == null) {
            throw new IllegalArgumentException("La evaluacion debe tener nivel.");
        }
    }

    private int prioridadNivel(NivelEvaluacion nivel) {
        return switch (nivel) {
            case SIN_DATOS -> 0;
            case CRITICO -> 1;
            case INESTABLE -> 2;
            case FUNCIONAL -> 3;
            case OPTIMO -> 4;
        };
    }
}
