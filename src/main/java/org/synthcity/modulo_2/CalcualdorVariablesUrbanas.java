package org.synthcity.modulo_2;

import java.util.List;
import org.synthcity.modulo_1.Bloque;

/**
 * Persona 3: Motor de cálculo matemático del Módulo 2 (Sprint 2 - Fusión).
 * Implementación optimizada mediante Java Streams y Polimorfismo.
 */
public class CalculadorVariablesUrbanas {

    // =========================================================
    // CÁLCULOS FUNCIONALES (STREAMS & POLIMORFISMO)
    // =========================================================

    public static int calcularEnergiaProducida(List<Bloque> activos) {
        return activos.stream()
                .mapToInt(Bloque::getProduccionEnergia)
                .sum();
    }

    public static int calcularConsumoEnergetico(List<Bloque> activos) {
        return activos.stream()
                .mapToInt(Bloque::getConsumoEnergetico)
                .sum();
    }

    public static int calcularDemandaServicios(List<Bloque> activos) {
        return activos.stream()
                .mapToInt(Bloque::getDemandaServicios)
                .sum();
    }

    public static int calcularCoberturaServicios(List<Bloque> activos) {
        return activos.stream()
                .mapToInt(Bloque::getCoberturaServicios)
                .sum();
    }

    public static int calcularPresionIndustrial(List<Bloque> activos) {
        return activos.stream()
                .mapToInt(Bloque::getPresionIndustrial)
                .sum();
    }

    public static int calcularSoporteTransporte(List<Bloque> activos) {
        return activos.stream()
                .mapToInt(Bloque::getSoporteTransporte)
                .sum();
    }

    public static int calcularContaminacion(List<Bloque> activos, double densidad, int soporteTransporte) {
        int contaminacionBase = activos.stream()
                .mapToInt(Bloque::getContaminacionGenerada)
                .sum();

        // Penalización por densidad alta (>80%) definida en ReglasSimulacion
        double multiplicador = (densidad > ReglasSimulacion.PENALIZACION_DENSIDAD_ALTA) ? 1.25 : 1.0;
        int contaminacionTotal = (int) (contaminacionBase * multiplicador) - soporteTransporte;

        return Math.max(0, contaminacionTotal);
    }

    // =========================================================
    // ÍNDICES SINTÉTICOS (LÓGICA DE NEGOCIO M2)
    // =========================================================

    public static double calcularBienestar(int coberturaServicios, int soporteTransporte,
                                           double porcentajeActividad, int contaminacion,
                                           boolean deficitEnergetico, boolean deficitServicios) {

        double partePositiva = coberturaServicios + soporteTransporte + (porcentajeActividad * 10.0);
        int penalizacion = (deficitEnergetico ? 5 : 0) + (deficitServicios ? 5 : 0);

        return Math.max(0.0, partePositiva - (contaminacion + penalizacion));
    }

    public static double calcularIndiceEstabilidadBasica(double porcentajeActividad, int equilibrio,
                                                         int cobertura, int contaminacion, double densidad) {

        double estabilidad = (porcentajeActividad * 50.0) + cobertura + equilibrio - contaminacion;
        if (densidad > ReglasSimulacion.PENALIZACION_DENSIDAD_ALTA) {
            estabilidad -= 10.0;
        }
        return estabilidad;
    }
}