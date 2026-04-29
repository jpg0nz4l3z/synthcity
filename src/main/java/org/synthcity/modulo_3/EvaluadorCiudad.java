package org.synthcity.modulo_3;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.ResultadoExpansion;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_3.prediccion.PredictionInput;
import org.synthcity.modulo_3.prediccion.PredictionResult;
import org.synthcity.modulo_3.prediccion.PredictorCiudad;

import java.util.EnumSet;
import java.util.Set;

public class EvaluadorCiudad implements Evaluable {

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

    private final Simulable simulable;
    private final GestorExpansion gestorExpansion;
    private final ConstructorDataset constructorDataset;

    public EvaluadorCiudad() {
        this(null, new GestorExpansion(), new ConstructorDataset());
    }

    public EvaluadorCiudad(Simulable simulable) {
        this(simulable, new GestorExpansion(), new ConstructorDataset());
    }

    public EvaluadorCiudad(Simulable simulable,
                           GestorExpansion gestorExpansion,
                           ConstructorDataset constructorDataset) {
        if (gestorExpansion == null || constructorDataset == null) {
            throw new ResultadoSimulacionInvalidoException("Las dependencias del evaluador no pueden ser nulas.");
        }
        this.simulable = simulable;
        this.gestorExpansion = gestorExpansion;
        this.constructorDataset = constructorDataset;
    }

    public ResultadoEvaluacion evaluar(ResultadoSimulacion resultado) {
        return evaluar(resultado, null);
    }

    @Override
    public ResultadoEvaluacion evaluar(ResultadoSimulacion resultado, Ciudad ciudad) {
        validarEntrada(resultado);

        ResultadoExpansion resultadoExpansion = null;
        boolean expansionEjecutada = false;

        MetricaCiudad metricaInicial = construirMetrica(resultado);
        boolean necesitaExpansionInicial = gestorExpansion.necesitaExpansion(metricaInicial);

        ResultadoSimulacion resultadoFinal = resultado;

        if (ciudad != null
                && simulable != null
                && necesitaExpansionInicial
                && gestorExpansion.puedeExpandirseAhora(ciudad, metricaInicial)) {

            resultadoExpansion = ciudad.expandir();
            expansionEjecutada = true;

            resultadoFinal = simulable.simular(ciudad);
            validarEntrada(resultadoFinal);
        }

        MetricaCiudad metrica = construirMetrica(resultadoFinal);

        double score = calcularScoreViabilidad(metrica);
        Set<AlertaEvaluacion> alertas = detectarAlertas(metrica);

        if (necesitaExpansionInicial || metrica.isNecesidadExpansionDetectada()) {
            alertas.add(AlertaEvaluacion.NECESIDAD_EXPANSION);
        }

        normalizarAlertaColapso(alertas);

        TendenciaTemporal tendenciaTemporal = determinarTendenciaTemporal(metrica);
        NivelEvaluacion nivel = determinarNivelEvaluacion(metrica, score, alertas, tendenciaTemporal);
        String mensaje = generarMensaje(nivel, metrica, score, alertas);
        String resumenRiesgo = generarResumenRiesgo(alertas);

        ResultadoEvaluacion evaluacion = new ResultadoEvaluacion(
                resultadoFinal.getNombreCiudad(),
                metrica,
                nivel,
                mensaje,
                score,
                alertas,
                resumenRiesgo,
                tendenciaTemporal,
                expansionEjecutada,
                resultadoExpansion,
                metrica.getCiclosEjecutados()
        );

        constructorDataset.agregarRegistro(construirRegistroDato(metrica, evaluacion));

        return evaluacion;
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

        double densidadOk = 1.0 - Math.min(1.0, m.getDensidad());
        double energiaOk = Math.min(1.0, m.getRatioEnergetico());
        double serviciosOk = Math.min(1.0, m.getRatioCoberturaServicios());
        double contaminacionOk = 1.0 - Math.min(1.0, m.getContaminacionAcumulada() / 100.0);
        double estabilidadOk = m.getEstabilidadMedia();

        double score = 0.0;
        score += densidadOk * 20.0;
        score += energiaOk * 25.0;
        score += serviciosOk * 20.0;
        score += contaminacionOk * 15.0;
        score += estabilidadOk * 20.0;

        return Math.max(0.0, Math.min(100.0, score));
    }

    private Set<AlertaEvaluacion> detectarAlertas(MetricaCiudad m) {
        Set<AlertaEvaluacion> alertas = EnumSet.noneOf(AlertaEvaluacion.class);

        if (m.getTotalBloques() <= 0) return alertas;

        if (m.hayDeficitEnergetico()) alertas.add(AlertaEvaluacion.DEFICIT_ENERGETICO);
        if (m.hayDeficitServicios()) alertas.add(AlertaEvaluacion.DEFICIT_SERVICIOS);
        if (m.tieneRiesgoPorDensidad()) alertas.add(AlertaEvaluacion.RIESGO_SATURACION);
        if (m.tieneContaminacionAlta()) alertas.add(AlertaEvaluacion.CONTAMINACION_ALTA);
        if (m.getPorcentajeActivos() < UMBRAL_ACTIVIDAD_BAJA) alertas.add(AlertaEvaluacion.ACTIVIDAD_BAJA);
        if (m.getEstabilidadBasica() < UMBRAL_ESTABILIDAD_INSUFICIENTE)
            alertas.add(AlertaEvaluacion.ESTABILIDAD_INSUFICIENTE);
        if (m.estaEnTendenciaNegativa()) alertas.add(AlertaEvaluacion.TENDENCIA_NEGATIVA);
        if (m.tieneContaminacionCreciente()) alertas.add(AlertaEvaluacion.CONTAMINACION_CRECIENTE);
        if (m.colapsoDetectado()) alertas.add(AlertaEvaluacion.COLAPSO_DETECTADO);

        normalizarAlertaColapso(alertas);
        return alertas;
    }

    private void normalizarAlertaColapso(Set<AlertaEvaluacion> alertas) {
        if (alertas.size() >= UMBRAL_ALERTAS_COLAPSO) {
            alertas.add(AlertaEvaluacion.RIESGO_COLAPSO_POTENCIAL);
        }
    }

    private NivelEvaluacion determinarNivelEvaluacion(
            MetricaCiudad m,
            double score,
            Set<AlertaEvaluacion> alertas,
            TendenciaTemporal tendenciaTemporal) {

        if (m.getTotalBloques() == 0) {
            return NivelEvaluacion.SIN_DATOS;
        }

        if (m.getBloquesActivos() == 0
                || tendenciaTemporal == TendenciaTemporal.COLAPSANDO
                || alertas.contains(AlertaEvaluacion.COLAPSO_DETECTADO)
                || alertas.contains(AlertaEvaluacion.RIESGO_COLAPSO_POTENCIAL)) {
            return NivelEvaluacion.CRITICO;
        }

        if (tendenciaTemporal == TendenciaTemporal.SATURANDO && score < 65.0) {
            return NivelEvaluacion.CRITICO;
        }

        if (score >= UMBRAL_SCORE_OPTIMO && tendenciaTemporal != TendenciaTemporal.DETERIORANDO) {
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

    private TendenciaTemporal determinarTendenciaTemporal(MetricaCiudad m) {
        if (m.getTotalBloques() <= 0 || m.getCiclosEjecutados() <= 0) return TendenciaTemporal.SIN_DATOS;
        if (m.colapsoDetectado()) return TendenciaTemporal.COLAPSANDO;
        if (m.saturacionDetectada() || m.isNecesidadExpansionDetectada()) return TendenciaTemporal.SATURANDO;
        if (m.estaEnTendenciaNegativa() || m.tieneContaminacionCreciente()) return TendenciaTemporal.DETERIORANDO;
        if (m.estaEnTendenciaPositiva()) return TendenciaTemporal.MEJORANDO;
        return TendenciaTemporal.ESTABLE;
    }

    private RegistroDato construirRegistroDato(MetricaCiudad metrica, ResultadoEvaluacion evaluacion) {
        return new RegistroDato(
                metrica.getDensidad(),
                metrica.getRatioEnergetico(),
                metrica.getRatioCoberturaServicios(),
                metrica.getContaminacion(),
                metrica.getContaminacionAcumulada(),
                metrica.getEstabilidadMedia(),
                metrica.getTendenciaEstabilidad(),
                metrica.getTendenciaContaminacion(),
                metrica.getBienestar(),
                evaluacion.getScoreViabilidad(),
                metrica.colapsoDetectado(),
                metrica.getCiclosEjecutados(),
                metrica.saturacionDetectada(),
                codificarNivel(evaluacion.getNivelEvaluacion())
        );
    }

    private int codificarNivel(NivelEvaluacion nivel) {
        return switch (nivel) {
            case SIN_DATOS -> 0;
            case CRITICO -> 1;
            case INESTABLE -> 2;
            case FUNCIONAL -> 3;
            case OPTIMO -> 4;
        };
    }

    public ConstructorDataset getConstructorDataset() {
        return constructorDataset;
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