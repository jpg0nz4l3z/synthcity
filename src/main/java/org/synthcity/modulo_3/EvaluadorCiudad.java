package org.synthcity.modulo_3;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_2.ResultadoSimulacion;
import java.util.List;

public class EvaluadorCiudad implements Evaluable {

    private final Simulable simulable;
    private final GestorExpansion gestorExpansion;
    private final ConstructorDataset constructorDataset;

    public EvaluadorCiudad(Simulable simulable, GestorExpansion gestorExpansion, ConstructorDataset constructorDataset) {
        this.simulable = simulable;
        this.gestorExpansion = gestorExpansion;
        this.constructorDataset = constructorDataset;
    }

    @Override
    public ResultadoEvaluacion evaluar(ResultadoSimulacion resultado, Ciudad ciudad) {
        validarEntrada(resultado);
        boolean expansionRealizada = false;

        MetricaCiudad metrica = construirMetrica(resultado);
        double score = calcularScoreViabilidad(metrica);
        List<AlertaEvaluacion> alertas = detectarAlertas(metrica);
        TendenciaTemporal tendencia = determinarTendencia(metrica);


        if (!expansionRealizada && gestorExpansion.necesitaExpansion(metrica)
                && gestorExpansion.puedeExpandirseAhora(ciudad, metrica)) {
            ciudad.expandir();
            resultado = simulable.simular(ciudad);
            expansionRealizada = true;

            metrica = construirMetrica(resultado);
            score = calcularScoreViabilidad(metrica);
            alertas = detectarAlertas(metrica);
            tendencia = determinarTendencia(metrica);
        }

        NivelEvaluacion nivel = determinarNivelEvaluacion(metrica, score, alertas, tendencia);
        String mensaje = generarMensajeEvaluacion(metrica, nivel, score, alertas, tendencia, expansionRealizada);
        ResultadoEvaluacion resultadoEvaluacion = construirResultado(
                resultado.getNombreCiudad(),
                metrica,
                nivel,
                mensaje,
                score,
                tendencia,
                alertas,
                expansionRealizada,
                resultado.getCiclosEjecutados()
        );
        RegistroDato dato = construirRegistroDato(metrica, resultadoEvaluacion);
        constructorDataset.agregarRegistro(dato);
        return resultadoEvaluacion;
    }


    private void validarEntrada(ResultadoSimulacion resultado) {
        if (resultado == null) {
            throw new ResultadoSimulacionInvalidoException("El resultado de simulación no puede ser nulo.");
        }
        if (!(resultado.getBloquesTotales() >= 0 &&
                resultado.getBloquesTotales() == resultado.getBloquesActivos() + resultado.getBloquesInactivos())) {
            throw new ResultadoSimulacionInvalidoException("Incoherencia en los contadores de bloques.");
        }
        if (resultado.getConteoPorTipo() != null) {
            throw new ResultadoSimulacionInvalidoException("El conteo por tipo no puede ser nulo.");
        }
        int sumaPorTipos = resultado.getConteoPorTipo().values().stream().mapToInt(Integer::intValue).sum();

        if (sumaPorTipos != resultado.getBloquesTotales()) {
            throw new ResultadoSimulacionInvalidoException(
                    "Incoherencia: la suma de los conteos por tipo (" + sumaPorTipos +
                            ") no coincide con el total de bloques (" + resultado.getBloquesTotales() + ")."
            );
        }
        if (resultado.getCiclosEjecutados() == 0 && !resultado.getEstado().esVacioLegal()) {
            throw new ResultadoSimulacionInvalidoException(
                    "Simulación con cero ciclos pero estado no vacío. No se puede evaluar."
            );
        }
    }

    private MetricaCiudad construirMetrica(ResultadoSimulacion resultado) {
        return new MetricaCiudad(resultado);
    }

    private double calcularScoreViabilidad(MetricaCiudad m) {
        double score = 0;

        score += m.getPorcentajeActivos() * 20;
        score += m.getRatioEnergetico() * 20;
        score += m.getRatioCoberturaServicios() * 20;
        score += m.getBienestar() * 15;
        score += 15;

        TendenciaTemporal t = determinarTendencia(m);
        if (t == TendenciaTemporal.MEJORANDO) score += 10;
        else if (t == TendenciaTemporal.DETERIORANDO) score -= 10;

        if (m.tieneContaminacionCreciente()) score -= 10;
        if (m.saturacionDetectada()) score -= 10;

        return Math.max(0, Math.min(100, score));
    }

    private List<AlertaEvaluacion> detectarAlertas(MetricaCiudad metrica) {
        List<AlertaEvaluacion> alertas = new java.util.ArrayList<>();
        if (metrica.getPorcentajeActivos() < 0.3) alertas.add(AlertaEvaluacion.BAJA_ACTIVIDAD);
        if (metrica.getRatioCoberturaServicios() < 0.5) alertas.add(AlertaEvaluacion.DEFICIT_SERVICIOS);
        if (metrica.estaEnTendenciaNegativa()) alertas.add(AlertaEvaluacion.TENDENCIA_NEGATIVA);
        if (metrica.tieneContaminacionCreciente()) alertas.add(AlertaEvaluacion.CONTAMINACION_CRECIENTE);
        if (metrica.isNecesidadExpansionDetectada()) alertas.add(AlertaEvaluacion.NECESIDAD_EXPANSION);
        if (metrica.colapsoDetectado()) alertas.add(AlertaEvaluacion.COLAPSO_DETECTADO);
        if(metrica.getRatioEnergetico()<0.4) alertas.add(AlertaEvaluacion.DEFICIT_ENERGETICO);
        if(metrica.getContaminacion()>0.7) alertas.add(AlertaEvaluacion.CONTAMINACION_ALTA);
        if(metrica.getIndiceSaturacion()>0.9) alertas.add(AlertaEvaluacion.SATURACION);

        return alertas;
    }

    private TendenciaTemporal determinarTendencia(MetricaCiudad metrica) {
        if (metrica.colapsoDetectado()) return TendenciaTemporal.COLAPSANDO;
        if (metrica.saturacionDetectada()) return TendenciaTemporal.SATURANDO;
        if (metrica.getCiclosEjecutados() < 2) return TendenciaTemporal.SIN_DATOS;
        double tendenciaEstabilidad = metrica.getTendenciaEstabilidad();
        if (tendenciaEstabilidad > 0.1) return TendenciaTemporal.MEJORANDO;
        if (tendenciaEstabilidad < -0.1) return TendenciaTemporal.DETERIORANDO;
        return TendenciaTemporal.ESTABLE;
    }


    private NivelEvaluacion determinarNivelEvaluacion(MetricaCiudad m, double score,
                                                      List<AlertaEvaluacion> alertas,
                                                      TendenciaTemporal tendencia) {
        if (m.getTotalBloques() == 0) return NivelEvaluacion.SIN_DATOS;
        if (m.getBloquesActivos() == 0 || tendencia == TendenciaTemporal.COLAPSANDO) {
            return NivelEvaluacion.CRITICO;
        }
        if (score >= 85 && tendencia != TendenciaTemporal.DETERIORANDO) return NivelEvaluacion.OPTIMO;
        if (score >= 65) return NivelEvaluacion.FUNCIONAL;
        if (score >= 40) return NivelEvaluacion.INESTABLE;

        return NivelEvaluacion.CRITICO;
    }

    private String generarMensajeEvaluacion(MetricaCiudad metrica, NivelEvaluacion nivel,
                                            double score,
                                            List<AlertaEvaluacion> alertas,
                                            TendenciaTemporal tendencia,
                                            boolean expansionRealizada) {
        StringBuilder sb = new StringBuilder();
        sb.append("Evaluación: ").append(nivel);
        sb.append(". Score: ").append(String.format("%.2f", score));
        if (expansionRealizada) {
            sb.append(" (se ejecutó expansión automática)");
        }
        sb.append(". Tendencia: ").append(tendencia);
        if (!alertas.isEmpty()) {
            sb.append(" Alertas: ").append(alertas);
        }
        return sb.toString();
    }

    private ResultadoEvaluacion construirResultado(String nombreCiudad, MetricaCiudad metrica,
                                                   NivelEvaluacion nivel, String mensaje,
                                                   double score,
                                                   TendenciaTemporal tendencia,
                                                   List<AlertaEvaluacion> alertas,
                                                   boolean expansionRealizada,
                                                   int ciclosSimulados) {
        return new ResultadoEvaluacion(
                nombreCiudad,
                metrica,
                nivel,
                mensaje,
                score,
                tendencia,
                alertas,
                expansionRealizada,
                ciclosSimulados
        );
    }

    private RegistroDato construirRegistroDato(MetricaCiudad metrica, ResultadoEvaluacion resultadoEval) {
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
                resultadoEval.getScoreViabilidad(),
                metrica.colapsoDetectado(),
                metrica.getCiclosEjecutados(),
                metrica.saturacionDetectada(),
                resultadoEval.getObjetivoCodificado()
        );
    }
}