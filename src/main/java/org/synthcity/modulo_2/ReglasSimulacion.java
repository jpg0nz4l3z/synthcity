package org.synthcity.modulo_2;

/**
 * Centraliza las reglas que usarán tanto el motor como las subclases de Bloque.
 */
public final class ReglasSimulacion {

    // --- REGLAS DE ENERGÍA ---
    public static final int ENERGIA_POR_BLOQUE_ENERGIA = 10;
    public static final int CONSUMO_RESIDENCIAL = 3;
    public static final int CONSUMO_INDUSTRIAL = 5;
    public static final int CONSUMO_SERVICIOS = 2;
    public static final int CONSUMO_TRANSPORTE = 2;
    public static final int CONSUMO_ENERGIA = 1; // Coste de mantenimiento

    // --- REGLAS DE SERVICIOS ---
    public static final int DEMANDA_POR_RESIDENCIAL = 2;
    public static final int COBERTURA_POR_SERVICIO = 3;

    // --- REGLAS DE INDUSTRIA Y TRANSPORTE ---
    public static final int PRESION_POR_INDUSTRIAL = 4;
    public static final int TRANSPORTE_SOPORTE = 2;
    public static final int CONTAMINACION_POR_INDUSTRIAL = 5;

    // --- REGLAS DEL SISTEMA (DENSIDAD) ---
    public static final double PENALIZACION_DENSIDAD_ALTA = 0.80; // Umbral (80%)
    public static final int EXTRA_CONTAMINACION_DENSIDAD = 10; // Penalización

    public static final int MAX_CICLOS = 10;
    public static final int CICLOS_CONSECUTIVOS_COLAPSO = 3;
    public static final int CICLOS_CONSECUTIVOS_SATURACION = 3;
    public static final double UMBRAL_SATURACION_CRITICA = 0.90;
    public static final double UMBRAL_NECESIDAD_EXPANSION = 0.80;

    public static final int RADIO_COBERTURA_SERVICIOS = 5;
    public static final int RADIO_INFLUENCIA_TRANSPORTE = 3;
    public static final double FACTOR_PENALIZACION_DISTANCIA = 0.1;

    // ========== NUEVAS CONSTANTES AÑADIDAS EN SPRINT 4 ==========

    // --- Pesos para el cálculo de bienestar ---
    public static final double PESO_SERVICIOS_BASE_BIENESTAR = 0.40;
    public static final double PESO_SERVICIOS_POND_BIENESTAR = 0.60;
    public static final double PESO_SERVICIOS_BIENESTAR = 0.30;
    public static final double PESO_ENERGIA_BIENESTAR = 0.25;
    public static final double PESO_TRANSPORTE_BIENESTAR = 0.15;
    public static final double PESO_ACTIVIDAD_BIENESTAR = 0.15;
    public static final double PESO_CONTAMINACION_BIENESTAR = 0.15;

    // --- Pesos para el cálculo de estabilidad ---
    public static final double PESO_SERVICIOS_BASE_ESTABILIDAD = 0.50;
    public static final double PESO_SERVICIOS_POND_ESTABILIDAD = 0.50;
    public static final double PESO_ENERGIA_ESTABILIDAD = 0.30;
    public static final double PESO_SERVICIOS_ESTABILIDAD = 0.25;
    public static final double PESO_DENSIDAD_ESTABILIDAD = 0.20;
    public static final double PESO_ACTIVIDAD_ESTABILIDAD = 0.10;
    public static final double PESO_TRANSPORTE_ESTABILIDAD = 0.10;
    public static final double PESO_CONTAMINACION_ESTABILIDAD = 0.05;

    // --- Factores de escala de contaminación ---
    public static final double ESCALA_CONTAMINACION_BIENESTAR = 150.0;
    public static final double ESCALA_CONTAMINACION_ESTABILIDAD = 200.0;

    // --- Umbral para considerar simulación estable ---
    public static final double UMBRAL_ESTABILIDAD_SIMULACION_ESTABLE = 0.65;

    // --- Valor por defecto para ratios si denominador es cero ---
    public static final double RATIO_POR_DEFECTO = 1.0;

    // --- Factor de suavizado para la fórmula de influencia espacial ---
    public static final double FACTOR_SUAVIZADO_INFLUENCIA = 1.0;

    private ReglasSimulacion() {
        throw new UnsupportedOperationException("Esta clase es un contenedor de constantes.");
    }
}