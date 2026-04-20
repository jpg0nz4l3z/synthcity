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
        formatearNotaDeAlcance(sb);

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

    private void formatearNotaDeAlcance(StringBuilder sb) {
        sb.append("----------------------------------------\n");
        sb.append("NOTA DE ALCANCE\n");
        sb.append("----------------------------------------\n");
        sb.append("Este informe se basa exclusivamente en metricas estructurales\n");
        sb.append("basicas del sistema.\n");
        sb.append("No incluye evaluacion de recursos, energia, poblacion ni\n");
        sb.append("predicciones, que seran incorporadas en fases posteriores.\n");
    }

    private String formatearPorcentaje(double valor) {
        return String.format(Locale.ROOT, "%.2f%%", valor * 100);
    }

    private String formatearNombreTipo(TipoBloque tipo) {
        String nombre = tipo.name().toLowerCase(Locale.ROOT);
        return Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1);
    }
}
