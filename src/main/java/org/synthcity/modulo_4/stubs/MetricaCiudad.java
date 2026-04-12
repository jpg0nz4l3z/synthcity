package org.synthcity.modulo_4.stubs;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class MetricaCiudad {

    private final int totalBloques;
    private final int bloquesActivos;
    private final int bloquesInactivos;
    private final double porcentajeActivos;
    private final double porcentajeInactivos;
    private final Map<TipoBloque, Integer> conteoPorTipo;

    public MetricaCiudad(
            int totalBloques,
            int bloquesActivos,
            int bloquesInactivos,
            double porcentajeActivos,
            double porcentajeInactivos,
            Map<TipoBloque, Integer> conteoPorTipo
    ) {
        this.totalBloques = totalBloques;
        this.bloquesActivos = bloquesActivos;
        this.bloquesInactivos = bloquesInactivos;
        this.porcentajeActivos = porcentajeActivos;
        this.porcentajeInactivos = porcentajeInactivos;
        this.conteoPorTipo = Collections.unmodifiableMap(new EnumMap<>(conteoPorTipo));
    }

    public int getTotalBloques() {
        return totalBloques;
    }

    public int getBloquesActivos() {
        return bloquesActivos;
    }

    public int getBloquesInactivos() {
        return bloquesInactivos;
    }

    public double getPorcentajeActivos() {
        return porcentajeActivos;
    }

    public double getPorcentajeInactivos() {
        return porcentajeInactivos;
    }

    public Map<TipoBloque, Integer> getConteoPorTipo() {
        return conteoPorTipo;
    }
}
