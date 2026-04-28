package org.synthcity.modulo_2;

import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.Bloque;

import java.util.List;

public class CalculadorEspacial {

    public double calcularCoberturaServiciosPonderada(List<Bloque> bloquesActivos) {
        if (bloquesActivos == null || bloquesActivos.isEmpty()) {
            return 1.0;
        }

        List<Bloque> generadoresDemanda = bloquesActivos.stream()
                .filter(Bloque::esGeneradorDemanda)
                .toList();

        if (generadoresDemanda.isEmpty()) {
            return 1.0;
        }

        List<Bloque> proveedoresCobertura = bloquesActivos.stream()
                .filter(bloque -> bloque.getCoberturaServicios() > 0)
                .filter(bloque -> bloque.getRadioInfluencia() > 0)
                .toList();

        if (proveedoresCobertura.isEmpty()) {
            return 0.0;
        }

        double coberturaPonderada = generadoresDemanda.stream()
                .mapToDouble(generador -> Math.max(1, generador.getDemandaServicios())
                        * mejorInfluencia(generador, proveedoresCobertura))
                .sum();

        double demandaTotal = generadoresDemanda.stream()
                .mapToDouble(generador -> Math.max(1, generador.getDemandaServicios()))
                .sum();

        if (demandaTotal <= 0.0) {
            return 1.0;
        }
        return clamp01(coberturaPonderada / demandaTotal);
    }

    public double calcularEficienciaTransporte(List<Bloque> bloquesActivos) {
        if (bloquesActivos == null || bloquesActivos.isEmpty()) {
            return 0.0;
        }

        List<Bloque> transportes = bloquesActivos.stream()
                .filter(bloque -> bloque.getSoporteTransporte() > 0)
                .filter(bloque -> bloque.getRadioInfluencia() > 0)
                .toList();

        if (transportes.isEmpty()) {
            return 0.0;
        }

        List<Bloque> objetivos = bloquesActivos.stream()
                .filter(bloque -> bloque.getSoporteTransporte() == 0)
                .toList();

        if (objetivos.isEmpty()) {
            return 1.0;
        }

        double cobertura = 0.0;
        for (Bloque objetivo : objetivos) {
            cobertura += mejorInfluencia(objetivo, transportes);
        }
        return clamp01(cobertura / objetivos.size());
    }

    private double mejorInfluencia(Bloque origen, List<Bloque> candidatos) {
        return candidatos.stream()
                .mapToDouble(candidato -> {
                    int distancia = distanciaManhattan(origen.getPosicion(), candidato.getPosicion());
                    int radio = candidato.getRadioInfluencia();

                    if (distancia <= radio) {
                        return (double) (radio - distancia + 1) / (radio + 1);
                    }

                    return 0.0;
                })
                .max()
                .orElse(0.0);
    }

    private int distanciaManhattan(Posicion a, Posicion b) {
        if (a == null || b == null) {
            return Integer.MAX_VALUE;
        }
        return Math.abs(a.getFila() - b.getFila()) + Math.abs(a.getColumna() - b.getColumna());
    }

    private double clamp01(double valor) {
        if (valor < 0.0) {
            return 0.0;
        }
        if (valor > 1.0) {
            return 1.0;
        }
        return valor;
    }

    public double calcularDistanciaMedia(List<Bloque> grupo1, List<Bloque> grupo2) {
        if (grupo1 == null || grupo2 == null || grupo1.isEmpty() || grupo2.isEmpty()) {
            return 0.0;
        }

        return grupo1.stream()
                .flatMapToInt(b1 -> grupo2.stream()
                        .mapToInt(b2 -> distanciaManhattan(b1.getPosicion(), b2.getPosicion())))
                .average()
                .orElse(0.0);
    }
}
