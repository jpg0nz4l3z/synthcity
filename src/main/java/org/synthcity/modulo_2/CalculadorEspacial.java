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

        double demandaTotal = 0.0;
        double coberturaPonderada = 0.0;

        for (Bloque generador : generadoresDemanda) {
            double demanda = Math.max(1, generador.getDemandaServicios());
            demandaTotal += demanda;
            coberturaPonderada += demanda * mejorInfluencia(generador, proveedoresCobertura);
        }

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
        double mejor = 0.0;
        for (Bloque candidato : candidatos) {
            int distancia = distanciaManhattan(origen.getPosicion(), candidato.getPosicion());
            int radio = candidato.getRadioInfluencia();
            if (distancia <= radio) {
                double influencia = (double) (radio - distancia + 1) / (radio + 1);
                mejor = Math.max(mejor, influencia);
            }
        }
        return clamp01(mejor);
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
}
