package org.synthcity.modulo_1;

public enum TipoEstructuralCiudad {
    PEQUENA,   // capacidad <= 400
    MEDIANA,   // capacidad <= 1600
    GRANDE;    // capacidad > 1600

    public String descripcionCorta() {
        return switch (this) {
            case PEQUENA -> "Ciudad pequeña";
            case MEDIANA -> "Ciudad mediana";
            case GRANDE -> "Ciudad grande";
        };
    }
}
