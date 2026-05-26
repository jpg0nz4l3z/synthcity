package org.synthcity.modulo_4;

import java.util.Locale;
import java.util.Map;

import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.ResultadoEvaluacion;
import org.synthcity.modulo_1.TipoBloque;

public class FormateadorResultado {

    public String formatear(ResultadoEvaluacion resultado) {
        if (resultado == null) {
            throw new FormatoSalidaException("No se puede formatear un resultado null.");
        }

        MetricaCiudad metrica = resultado.getMetricaCiudad();
        if (metrica == null) {
            throw new FormatoSalidaException("No se puede formatear un resultado sin metricas.");
        }

        StringBuilder sb = new StringBuilder();

        sb.append("========================================\n");
        sb.append(" INFORME DE ESTADO DE CIUDAD\n");
        sb.append("========================================\n");

        formatearCabecera(sb, resultado);
        formatearMetricas(sb, metrica);
        formatearDistribucion(sb, metrica.getConteoPorTipo());
        formatearAnaliticaSprint2(sb, metrica);

        sb.append("========================================\n");
        sb.append(" FIN DEL INFORME\n");
        sb.append("========================================\n");

        return sb.toString();
    }

    private void formatearCabecera(StringBuilder sb, ResultadoEvaluacion resultado) {
        sb.append("Nombre de la ciudad: ")
                .append(resultado.getNombreCiudad())
                .append("\n");

        sb.append("----------------------------------------\n");
        sb.append("EVALUACION DEL SISTEMA\n");
        sb.append("----------------------------------------\n");

        sb.append("Nivel de evaluacion: ")
                .append(resultado.getNivelEvaluacion())
                .append("\n");

        sb.append("Interpretacion:\n")
                .append(resultado.getMensaje())
                .append("\n");

        sb.append("Score de viabilidad: ")
                .append(String.format(Locale.ROOT, "%.2f", resultado.getScoreViabilidad()))
                .append("\n");

        sb.append("Alertas: ")
                .append(resultado.getNumeroAlertas())
                .append("\n");
    }

    private void formatearMetricas(StringBuilder sb, MetricaCiudad metrica) {
        sb.append("----------------------------------------\n");
        sb.append("RESUMEN ESTRUCTURAL\n");
        sb.append("----------------------------------------\n");

        sb.append("Bloques totales: ")
                .append(metrica.getTotalBloques())
                .append("\n");

        sb.append("Bloques activos: ")
                .append(metrica.getBloquesActivos())
                .append("\n");

        sb.append("Bloques inactivos: ")
                .append(metrica.getBloquesInactivos())
                .append("\n");

        sb.append("Porcentaje de actividad: ")
                .append(formatearPorcentaje(metrica.getPorcentajeActivos()))
                .append("\n");

        sb.append("Porcentaje de inactividad: ")
                .append(formatearPorcentaje(metrica.getPorcentajeInactivos()))
                .append("\n");
    }

    private void formatearDistribucion(StringBuilder sb, Map<TipoBloque, Integer> conteoPorTipo) {
        if (conteoPorTipo == null) {
            throw new FormatoSalidaException("El mapa de conteo por tipo no puede ser null.");
        }

        sb.append("----------------------------------------\n");
        sb.append("DISTRIBUCION POR TIPO\n");
        sb.append("----------------------------------------\n");

        for (TipoBloque tipo : TipoBloque.values()) {
            Integer cantidad = conteoPorTipo.get(tipo);
            if (cantidad == null) {
                throw new FormatoSalidaException("Falta el tipo " + tipo + " en el conteo por tipo.");
            }

            sb.append(formatearNombreTipo(tipo))
                    .append(": ")
                    .append(cantidad)
                    .append("\n");
        }
    }

    private void formatearAnaliticaSprint2(StringBuilder sb, MetricaCiudad metrica) {
        sb.append("----------------------------------------\n");
        sb.append("VARIABLES URBANAS\n");
        sb.append("----------------------------------------\n");
        sb.append("Densidad: ")
                .append(String.format(Locale.ROOT, "%.2f", metrica.getDensidad()))
                .append("\n");
        sb.append("Energia producida/consumida: ")
                .append(metrica.getEnergiaProducida())
                .append(" / ")
                .append(metrica.getConsumoEnergetico())
                .append("\n");
        sb.append("Ratio energetico: ")
                .append(String.format(Locale.ROOT, "%.2f", metrica.getRatioEnergetico()))
                .append("\n");
        sb.append("Servicios demanda/cobertura: ")
                .append(metrica.getDemandaServicios())
                .append(" / ")
                .append(metrica.getCoberturaServicios())
                .append("\n");
        sb.append("Ratio servicios: ")
                .append(String.format(Locale.ROOT, "%.2f", metrica.getRatioCoberturaServicios()))
                .append("\n");
        sb.append("Contaminacion: ")
                .append(metrica.getContaminacion())
                .append("\n");
        sb.append("Bienestar: ")
                .append(String.format(Locale.ROOT, "%.2f", metrica.getBienestar()))
                .append("\n");
        sb.append("Estabilidad: ")
                .append(String.format(Locale.ROOT, "%.2f", metrica.getEstabilidadBasica()))
                .append("\n");
    }

    private String formatearPorcentaje(double valor) {
        return String.format(Locale.ROOT, "%.2f%%", valor * 100);
    }

    private String formatearNombreTipo(TipoBloque tipo) {
        String nombre = tipo.name().toLowerCase(Locale.ROOT);
        return Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1);
    }
}
