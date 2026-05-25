package org.synthcity.modulo_3.prediccion;

import org.synthcity.modulo_3.RegistroDato;
import org.synthcity.modulo_3.ResultadoEvaluacion;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalisisDataset {
    public static Map<Integer, Long> calcularDistribucion(List<RegistroDato> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(
                        RegistroDato::getObjetivo,
                        Collectors.counting()
                ));
    }
    public static double calcularMediaFeature(List<RegistroDato> registros, int indiceFeature) {
        return registros.stream()
                .mapToDouble(r -> r.toArray()[indiceFeature])
                .average()
                .orElse(0.0);
    }
    public static List<ResultadoEvaluacion> construirRanking(List<ResultadoEvaluacion> evaluaciones) {
        return evaluaciones.stream()
                .sorted(Comparator
                        .comparingDouble(ResultadoEvaluacion::getScoreViabilidad)
                        .reversed())
                .collect(Collectors.toList());
    }
    public static List<RegistroDato> filtrarPorObjetivo(List<RegistroDato> registros, int objetivo) {
        return registros.stream()
                .filter(r -> r.getObjetivo() == objetivo)
                .collect(Collectors.toList());
    }
    public static List<RegistroDato> filtrarValidos(List<RegistroDato> registros) {
        return registros.stream()
                .filter(r -> r.getObjetivo() >= 1 && r.getObjetivo() <= 4)
                .collect(Collectors.toList());
    }
    public static boolean tieneClasesSuficientes(List<RegistroDato> registros) {
        long clasesDistintas = registros.stream()
                .filter(r -> r.getObjetivo() >= 1 && r.getObjetivo() <= 4)
                .map(RegistroDato::getObjetivo)
                .distinct()
                .count();

        return clasesDistintas >= 2;
    }
    public static boolean sinValoresInvalidos(List<RegistroDato> registros) {
        return registros.stream()
                .allMatch(r -> {
                    for (double v : r.toArray()) {
                        if (Double.isNaN(v) || Double.isInfinite(v)) return false;
                    }
                    return true;
                });
    }
    public static void imprimirResumen(List<RegistroDato> registros) {
        System.out.println("\n========== RESUMEN DEL DATASET ==========");
        System.out.println("Total de registros: " + registros.size());

        // Distribución por clase con stream
        System.out.println("\nDistribución por clase objetivo:");
        calcularDistribucion(registros).forEach((objetivo, cantidad) -> {
            String etiqueta = switch (objetivo) {
                case 0 -> "SIN_DATOS";
                case 1 -> "CRITICO";
                case 2 -> "INESTABLE";
                case 3 -> "FUNCIONAL";
                case 4 -> "OPTIMO";
                default -> "DESCONOCIDO";
            };
            double porcentaje = (cantidad * 100.0) / registros.size();
            System.out.printf("  %-12s (objetivo=%d): %d registros (%.1f%%)\n",
                    etiqueta, objetivo, cantidad, porcentaje);
        });

        // Medias de las 13 features con stream
        String[] nombres = {
                "densidad", "ratioEnergetico", "ratioCoberturaServicios",
                "contaminacion", "contaminacionAcumulada", "estabilidadMedia",
                "tendenciaEstabilidad", "tendenciaContaminacion", "bienestar",
                "scoreViabilidad", "colapsoDetectado", "ciclosEjecutados", "saturacionDetectada"
        };

        System.out.println("\nMedia de cada feature:");
        for (int i = 0; i < nombres.length; i++) {
            final int idx = i;
            double media = registros.stream()
                    .mapToDouble(r -> r.toArray()[idx])
                    .average()
                    .orElse(0.0);
            System.out.printf("  %-30s: %.4f\n", nombres[i], media);
        }

        // Conteo de válidos e inválidos con stream
        long validos = registros.stream()
                .filter(r -> r.getObjetivo() >= 1 && r.getObjetivo() <= 4)
                .count();

        long conNaN = registros.stream()
                .filter(r -> {
                    for (double v : r.toArray()) {
                        if (Double.isNaN(v) || Double.isInfinite(v)) return true;
                    }
                    return false;
                })
                .count();

        System.out.println("\nRegistros válidos para Weka: " + validos);
        System.out.println("Registros con NaN/Infinito:  " + conNaN);
        System.out.println("Clases suficientes para J48: " + tieneClasesSuficientes(registros));
        System.out.println("==========================================\n");
    }
}