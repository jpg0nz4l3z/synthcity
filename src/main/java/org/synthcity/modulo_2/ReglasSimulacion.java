package org.synthcity.modulo_2;

public final class ReglasSimulacion {

    public static final int ENERGIA_POR_BLOQUE_ENERGIA = 10;
    public static final int CONSUMO_RESIDENCIAL = 3;
    public static final int CONSUMO_INDUSTRIAL = 5;
    public static final int CONSUMO_SERVICIOS = 2;
    public static final int CONSUMO_TRANSPORTE = 2;
    public static final int CONSUMO_ENERGIA = 1; // Coste de mantenimiento

    public static final int DEMANDA_POR_RESIDENCIAL = 2;
    public static final int COBERTURA_POR_SERVICIO = 3;

    public static final int PRESION_POR_INDUSTRIAL = 4;
    public static final int TRANSPORTE_SOPORTE = 2;
    public static final int CONTAMINACION_POR_INDUSTRIAL = 5;

    public static final double PENALIZACION_DENSIDAD_ALTA = 0.80; // Umbral (80%)
    public static final int EXTRA_CONTAMINACION_DENSIDAD = 10; // Penalización

    private ReglasSimulacion() {
        throw new UnsupportedOperationException("Esta clase es un contenedor de constantes.");
    }
}
