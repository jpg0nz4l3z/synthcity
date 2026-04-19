package org.synthcity.modulo_3;

import java.util.Set;

public class GeneradorMensajes {

    public static String generarMensaje(NivelEvaluacion nivel) {
        switch (nivel) {
            case SIN_DATOS:
                return "La ciudad no tiene datos";
            case CRITICO:
                return "La ciudad no tiene actividad";
            case INESTABLE:
                return "La ciudad es inestable";
            case FUNCIONAL:
                return "La ciudad es operativa";
            case OPTIMO:
                return "La ciudad está en estado óptimo";
            default:
                return "Estado desconocido";
        }
    }

    public static String generarMensaje(NivelEvaluacion nivel,
                                        MetricaCiudad metrica,
                                        double score,
                                        Set<AlertaEvaluacion> alertas) {
        String base = mensajeBase(nivel, metrica);
        String detalle = detalleAlertas(alertas);
        String scoreFmt = String.format(java.util.Locale.ROOT, "%.1f", score);
        if (detalle.isEmpty()) {
            return base + " Score: " + scoreFmt + "/100.";
        }
        return base + " " + detalle + " Score: " + scoreFmt + "/100.";
    }

    private static String mensajeBase(NivelEvaluacion nivel, MetricaCiudad metrica) {
        switch (nivel) {
            case SIN_DATOS:
                return "La ciudad no tiene datos suficientes para ser evaluada.";
            case CRITICO:
                if (metrica.getBloquesActivos() == 0) {
                    return "La ciudad no tiene actividad operativa suficiente para sostener el sistema.";
                }
                return "La ciudad se encuentra en estado crítico por acumulación de desequilibrios estructurales.";
            case INESTABLE:
                return "La ciudad presenta viabilidad limitada con señales de inestabilidad internas.";
            case FUNCIONAL:
                return "La ciudad es operativa con cobertura y equilibrio aceptables.";
            case OPTIMO:
                return "La ciudad mantiene una viabilidad alta con indicadores equilibrados.";
            default:
                return "Estado desconocido.";
        }
    }

    private static String detalleAlertas(Set<AlertaEvaluacion> alertas) {
        if (alertas == null || alertas.isEmpty()) {
            return "Sin alertas relevantes.";
        }
        StringBuilder sb = new StringBuilder("Alertas (" + alertas.size() + "):");
        boolean primero = true;
        for (AlertaEvaluacion a : alertas) {
            sb.append(primero ? " " : ", ");
            sb.append(describir(a));
            primero = false;
        }
        sb.append(".");
        return sb.toString();
    }

    private static String describir(AlertaEvaluacion alerta) {
        switch (alerta) {
            case DEFICIT_ENERGETICO: return "déficit energético";
            case DEFICIT_SERVICIOS: return "déficit de servicios";
            case RIESGO_SATURACION: return "riesgo de saturación";
            case CONTAMINACION_ALTA: return "contaminación alta";
            case ACTIVIDAD_BAJA: return "actividad baja";
            case ESTABILIDAD_INSUFICIENTE: return "estabilidad insuficiente";
            case RIESGO_COLAPSO_POTENCIAL: return "riesgo de colapso potencial";
            default: return alerta.name();
        }
    }
}
