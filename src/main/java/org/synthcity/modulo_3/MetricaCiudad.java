package org.synthcity.modulo_3;

import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_1.TipoBloque;
import java.util.Map;
import java.util.Collections;

public final class MetricaCiudad {

    private final int totalBloques;
    private final int bloquesActivos;
    private final int bloquesInactivos;
    private final double porcentajeActivos;
    private final double porcentajeInactivos;
    private final Map<TipoBloque, Integer> conteoPorTipo;

    public MetricaCiudad(ResultadoSimulacion resultado) {
        if (resultado == null) {
            throw new NullPointerException();
        }

        this.totalBloques = resultado.getBloquesTotales();
        this.bloquesActivos = resultado.getBloquesActivos();
        this.bloquesInactivos = this.totalBloques - this.bloquesActivos;

        if (this.totalBloques > 0) {
            this.porcentajeActivos = (double) this.bloquesActivos / this.totalBloques;
        } else {
            this.porcentajeActivos = 0.0;
        }

        this.porcentajeInactivos = 1.0 - this.porcentajeActivos;

        this.conteoPorTipo = Collections.unmodifiableMap(resultado.getConteoPorTipo());
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