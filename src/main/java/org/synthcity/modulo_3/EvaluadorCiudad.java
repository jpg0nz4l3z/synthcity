package org.synthcity.modulo_3;

import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_3.prediccion.PredictionInput;
import org.synthcity.modulo_3.prediccion.PredictionResult;
import org.synthcity.modulo_3.prediccion.PredictorCiudad;

import java.util.EnumSet;
import java.util.Set;

public class EvaluadorCiudad {

    private static final double PESO_ACTIVIDAD = 25.0;
    private static final double PESO_ENERGIA = 20.0;
    private static final double PESO_SERVICIOS = 20.0;
    private static final double PESO_ESTABILIDAD = 15.0;
    private static final double PESO_BIENESTAR = 20.0;
    private static final double PENAL_CONTAMINACION = 10.0;
    private static final double PENAL_SATURACION = 10.0;

    private static final double UMBRAL_SCORE_OPTIMO = 85.0;
    private static final double UMBRAL_SCORE_FUNCIONAL = 65.0;
    private static final double UMBRAL_SCORE_INESTABLE = 40.0;

    private static final double UMBRAL_DENSIDAD_SATURACION = 0.85;
    private static final double UMBRAL_ACTIVIDAD_BAJA = 0.40;
    private static final double UMBRAL_ESTABILIDAD_INSUFICIENTE = 0.35;
    private static final int UMBRAL_CONTAMINACION_ALTA = 60;
    private static final int UMBRAL_ALERTAS_COLAPSO = 4;

    public ResultadoEvaluacion evaluar(ResultadoSimulacion resultado) {

        validarEntrada(resultado);

        MetricaCiudad metrica = construirMetrica(resultado);

        double score = calcularScoreViabilidad(metrica);

        Set<AlertaEvaluacion> alertas = detectarAlertas(metrica);

        NivelEvaluacion nivel = determinarNivelEvaluacion(metrica, score, alertas);

        String mensaje = generarMensaje(nivel, metrica, score, alertas);

        String resumenRiesgo = generarResumenRiesgo(alertas);

        return new ResultadoEvaluacion(
                resultado.getNombreCiudad(),
                metrica,
                nivel,
                mensaje,
                score,
                alertas,
                resumenRiesgo
        );
    }

    private void validarEntrada(ResultadoSimulacion resultado) {
        if (resultado == null) {
            throw new ResultadoSimulacionInvalidoException("El resultado de simulación no puede ser nulo.");
        }
        if (resultado.getBloquesTotales() < 0 || resultado.getBloquesActivos() < 0 || resultado.getBloquesInactivos() < 0) {
            throw new ResultadoSimulacionInvalidoException("Los contadores de bloques no pueden ser negativos.");
        }
        if (resultado.getBloquesActivos() > resultado.getBloquesTotales()
                || resultado.getBloquesInactivos() > resultado.getBloquesTotales()) {
            throw new ResultadoSimulacionInvalidoException("Los bloques activos o inactivos no pueden superar el total.");
        }
        if ((resultado.getBloquesActivos() + resultado.getBloquesInactivos()) != resultado.getBloquesTotales()) {
            throw new ResultadoSimulacionInvalidoException("Incoherencia: Activos + Inactivos no suma el total.");
        }
        if (resultado.getCapacidadMaxima() < 0) {
            throw new ResultadoSimulacionInvalidoException("La capacidad máxima no puede ser negativa.");
        }
        if (resultado.getConteoPorTipo() == null) {
            throw new ResultadoSimulacionInvalidoException("El conteo por tipo no puede ser nulo.");
        }
        if (resultado.getEstadoSimulacion() == null) {
            throw new ResultadoSimulacionInvalidoException("El estado de simulación no puede ser nulo.");
        }
        if (resultado.getTipoEstructural() == null) {
            throw new ResultadoSimulacionInvalidoException("El tipo estructural no puede ser nulo.");
        }
        if (resultado.getDensidad() < 0.0 || resultado.getDensidad() > 1.0) {
            throw new ResultadoSimulacionInvalidoException("La densidad debe estar entre 0.0 y 1.0.");
        }
    }

    private MetricaCiudad construirMetrica(ResultadoSimulacion resultado) {
        return new MetricaCiudad(resultado);
    }

    private double calcularScoreViabilidad(MetricaCiudad m) {
        if (m.getTotalBloques() <= 0) {
            return 0.0;
        }

        double energia = Math.min(1.0, m.getRatioEnergetico());
        double servicios = Math.min(1.0, m.getRatioCoberturaServicios());

        double score = 0.0;
        score += m.getPorcentajeActivos() * PESO_ACTIVIDAD;
        score += energia * PESO_ENERGIA;
        score += servicios * PESO_SERVICIOS;
        score += m.getEstabilidadBasica() * PESO_ESTABILIDAD;
        score += m.getBienestar() * PESO_BIENESTAR;

        if (m.getContaminacion() >= UMBRAL_CONTAMINACION_ALTA) {
            score -= PENAL_CONTAMINACION;
        }
        if (m.getDensidad() >= UMBRAL_DENSIDAD_SATURACION) {
            score -= PENAL_SATURACION;
        }

        if (score < 0.0) return 0.0;
        if (score > 100.0) return 100.0;
        return score;
    }

    private Set<AlertaEvaluacion> detectarAlertas(MetricaCiudad m) {
        Set<AlertaEvaluacion> alertas = EnumSet.noneOf(AlertaEvaluacion.class);
        if (m.getTotalBloques() <= 0) {
            return alertas;
        }
        if (m.hayDeficitEnergetico()) {
            alertas.add(AlertaEvaluacion.DEFICIT_ENERGETICO);
        }
        if (m.hayDeficitServicios()) {
            alertas.add(AlertaEvaluacion.DEFICIT_SERVICIOS);
        }
        if (m.tieneRiesgoPorDensidad()) {
            alertas.add(AlertaEvaluacion.RIESGO_SATURACION);
        }
        if (m.tieneContaminacionAlta()) {
            alertas.add(AlertaEvaluacion.CONTAMINACION_ALTA);
        }
        if (m.getPorcentajeActivos() < UMBRAL_ACTIVIDAD_BAJA) {
            alertas.add(AlertaEvaluacion.ACTIVIDAD_BAJA);
        }
        if (m.getEstabilidadBasica() < UMBRAL_ESTABILIDAD_INSUFICIENTE) {
            alertas.add(AlertaEvaluacion.ESTABILIDAD_INSUFICIENTE);
        }
        if (alertas.size() >= UMBRAL_ALERTAS_COLAPSO) {
            alertas.add(AlertaEvaluacion.RIESGO_COLAPSO_POTENCIAL);
        }
        return alertas;
    }

    private NivelEvaluacion determinarNivelEvaluacion(MetricaCiudad m, double score, Set<AlertaEvaluacion> alertas) {
        if (m.getTotalBloques() == 0) {
            return NivelEvaluacion.SIN_DATOS;
        }
        if (m.getBloquesActivos() == 0) {
            return NivelEvaluacion.CRITICO;
        }
        if (alertas.contains(AlertaEvaluacion.RIESGO_COLAPSO_POTENCIAL)) {
            return NivelEvaluacion.CRITICO;
        }

        // Reglas por score
        if (score >= UMBRAL_SCORE_OPTIMO) {
            return NivelEvaluacion.OPTIMO;
        }
        if (score >= UMBRAL_SCORE_FUNCIONAL) {
            return NivelEvaluacion.FUNCIONAL;
        }
        if (score >= UMBRAL_SCORE_INESTABLE) {
            return NivelEvaluacion.INESTABLE;
        }
        return NivelEvaluacion.CRITICO;
    }

    private String generarMensaje(NivelEvaluacion nivel, MetricaCiudad m, double score, Set<AlertaEvaluacion> alertas) {
        return GeneradorMensajes.generarMensaje(nivel, m, score, alertas);
    }

    private String generarResumenRiesgo(Set<AlertaEvaluacion> alertas) {
        if (alertas.isEmpty()) {
            return "Sin riesgos relevantes detectados.";
        }
        StringBuilder sb = new StringBuilder("Riesgos detectados: ");
        boolean primero = true;
        for (AlertaEvaluacion alerta : alertas) {
            if (!primero) sb.append(", ");
            sb.append(alerta.name());
            primero = false;
        }
        return sb.toString();
    }

    public PredictionResult predecir(MetricaCiudad metrica) {
        if (metrica == null) {
            throw new ResultadoSimulacionInvalidoException("La métrica no puede ser nula.");
        }

        PredictionInput input = PredictionInput.desdeMetrica(metrica);
        PredictorCiudad predictor = new PredictorCiudad();
        return predictor.predecir(input);
    }

    public PredictionResult predecir(ResultadoSimulacion resultado) {
        validarEntrada(resultado);
        MetricaCiudad metrica = construirMetrica(resultado);
        return predecir(metrica);
    }
}
