package org.synthcity.modulo_1;

public enum TipoEstructuralCiudad {
    PEQUENA,   // capacidad <= 400
    MEDIANA,   // capacidad <= 1600
    GRANDE;    // capacidad > 1600

    public static String descripcionCorta(int capacidad) {
        if (capacidad <= 400) {
            return "Ciudad pequeña";
        } else if (capacidad <= 1600) {
            return "Ciudad mediana";
        } else if (capacidad > 1600) {
            return "Ciudad grande";
        }

        return null;
    }
}