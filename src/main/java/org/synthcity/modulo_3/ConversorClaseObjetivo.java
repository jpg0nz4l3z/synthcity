package org.synthcity.modulo_3;

public class ConversorClaseObjetivo {

    public static String convertir(int valor) {
        switch (valor) {
            case 1: return "CRITICO";
            case 2: return "INESTABLE";
            case 3: return "FUNCIONAL";
            case 4: return "OPTIMO";
            default: throw new IllegalArgumentException(
                    "Valor de objetivo no válido para Weka: " + valor
            );
        }
    }

    public static int convertirEntero(String etiqueta) {
        return switch (etiqueta.toUpperCase()) {
            case "CRITICO" -> 1;
            case "INESTABLE" -> 2;
            case "FUNCIONAL" -> 3;
            case "OPTIMO" -> 4;
            default -> throw new IllegalArgumentException(
                    "Etiqueta de objetivo no reconocida: " + etiqueta);
        };
    }
}