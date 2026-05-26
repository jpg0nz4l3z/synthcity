package org.synthcity.modulo_3.prediccion;

import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_1.TipoEstructuralCiudad;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.ResultadoSimulacionInvalidoException;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class PredictionInput {

    private final int totalBloques;
    private final int bloquesActivos;
    private final int bloquesInactivos;

    private final double porcentajeActivos;
    private final double densidad;
    private final double ratioEnergetico;
    private final double ratioCoberturaServicios;

    private final int contaminacion;
    private final double estabilidadBasica;
    private final double bienestar;

    private final double indiceSaturacion;
    private final double indiceViabilidadBase;

    private final EstadoSimulacion estadoSimulacion;
    private final TipoEstructuralCiudad tipoEstructural;
    private final Map<TipoBloque, Integer> conteoPorTipo;

    private final double contaminacionAcumulada;
    private final double tendenciaEstabilidad;
    private final double tendenciaContaminacion;
    private final int    ciclosEjecutados;
    private final boolean colapsoDetectado;
    private final boolean saturacionDetectada;

    public PredictionInput(
            int totalBloques,
            int bloquesActivos,
            int bloquesInactivos,
            double porcentajeActivos,
            double densidad,
            double ratioEnergetico,
            double ratioCoberturaServicios,
            int contaminacion,
            double estabilidadBasica,
            double bienestar,
            double indiceSaturacion,
            double indiceViabilidadBase,
            EstadoSimulacion estadoSimulacion,
            TipoEstructuralCiudad tipoEstructural,
            Map<TipoBloque, Integer> conteoPorTipo,

            double contaminacionAcumulada,
            double tendenciaEstabilidad,
            double tendenciaContaminacion,
            int ciclosEjecutados,
            boolean colapsoDetectado,
            boolean saturacionDetectada
    ) {
        if (totalBloques < 0) {
            throw new IllegalArgumentException("El total de bloques no puede ser negativo.");
        }
        if (bloquesActivos < 0 || bloquesInactivos < 0) {
            throw new IllegalArgumentException("Los bloques activos e inactivos no pueden ser negativos.");
        }
        if (bloquesActivos + bloquesInactivos != totalBloques) {
            throw new IllegalArgumentException("Activos + inactivos debe coincidir con el total.");
        }
        if (porcentajeActivos < 0.0 || porcentajeActivos > 1.0) {
            throw new IllegalArgumentException("porcentajeActivos debe estar entre 0.0 y 1.0.");
        }
        if (densidad < 0.0 || densidad > 1.0) {
            throw new IllegalArgumentException("densidad debe estar entre 0.0 y 1.0.");
        }
        if (ratioEnergetico < 0.0) {
            throw new IllegalArgumentException("ratioEnergetico no puede ser negativo.");
        }
        if (ratioCoberturaServicios < 0.0) {
            throw new IllegalArgumentException("ratioCoberturaServicios no puede ser negativo.");
        }
        if (contaminacion < 0) {
            throw new IllegalArgumentException("contaminacion no puede ser negativa.");
        }
        if (estabilidadBasica < 0.0 || estabilidadBasica > 1.0) {
            throw new IllegalArgumentException("estabilidadBasica debe estar entre 0.0 y 1.0.");
        }
        if (bienestar < 0.0 || bienestar > 1.0) {
            throw new IllegalArgumentException("bienestar debe estar entre 0.0 y 1.0.");
        }
        if (indiceSaturacion < 0.0 || indiceSaturacion > 1.0) {
            throw new IllegalArgumentException("indiceSaturacion debe estar entre 0.0 y 1.0.");
        }
        if (indiceViabilidadBase < 0.0 || indiceViabilidadBase > 1.0) {
            throw new IllegalArgumentException("indiceViabilidadBase debe estar entre 0.0 y 1.0.");
        }
        if (estadoSimulacion == null) {
            throw new IllegalArgumentException("estadoSimulacion no puede ser null.");
        }
        if (tipoEstructural == null) {
            throw new IllegalArgumentException("tipoEstructural no puede ser null.");
        }
        if (conteoPorTipo == null) {
            throw new IllegalArgumentException("conteoPorTipo no puede ser null.");
        }
        if (contaminacionAcumulada < 0.0) {
            throw new IllegalArgumentException("contaminacionAcumulada no puede ser negativa.");
        }
        if (ciclosEjecutados < 0) {
            throw new IllegalArgumentException("ciclosEjecutados no puede ser negativo.");
        }


        EnumMap<TipoBloque, Integer> copia = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            Integer cantidad = conteoPorTipo.get(tipo);
            if (cantidad == null || cantidad < 0) {
                throw new IllegalArgumentException("Conteo inválido para el tipo " + tipo + ".");
            }
            copia.put(tipo, cantidad);
        }

        this.totalBloques = totalBloques;
        this.bloquesActivos = bloquesActivos;
        this.bloquesInactivos = bloquesInactivos;
        this.porcentajeActivos = porcentajeActivos;
        this.densidad = densidad;
        this.ratioEnergetico = ratioEnergetico;
        this.ratioCoberturaServicios = ratioCoberturaServicios;
        this.contaminacion = contaminacion;
        this.estabilidadBasica = estabilidadBasica;
        this.bienestar = bienestar;
        this.indiceSaturacion = indiceSaturacion;
        this.indiceViabilidadBase = indiceViabilidadBase;
        this.estadoSimulacion = estadoSimulacion;
        this.tipoEstructural = tipoEstructural;
        this.conteoPorTipo = Collections.unmodifiableMap(copia);
        this.contaminacionAcumulada  = contaminacionAcumulada;
        this.tendenciaEstabilidad    = tendenciaEstabilidad;
        this.tendenciaContaminacion  = tendenciaContaminacion;
        this.ciclosEjecutados        = ciclosEjecutados;
        this.colapsoDetectado        = colapsoDetectado;
        this.saturacionDetectada     = saturacionDetectada;
    }

    public static PredictionInput desdeMetrica(MetricaCiudad metrica) {
        if (metrica == null) {
            throw new ResultadoSimulacionInvalidoException("La métrica no puede ser null.");
        }

        return new PredictionInput(
                metrica.getTotalBloques(),
                metrica.getBloquesActivos(),
                metrica.getBloquesInactivos(),
                metrica.getPorcentajeActivos(),
                metrica.getDensidad(),
                metrica.getRatioEnergetico(),
                metrica.getRatioCoberturaServicios(),
                metrica.getContaminacion(),
                metrica.getEstabilidadBasica(),
                metrica.getBienestar(),
                metrica.getIndiceSaturacion(),
                metrica.getIndiceViabilidadBase(),
                metrica.getEstadoSimulacion(),
                metrica.getTipoEstructural(),
                metrica.getConteoPorTipo(),
                metrica.getContaminacionAcumulada(),
                metrica.getTendenciaEstabilidad(),
                metrica.getTendenciaContaminacion(),
                metrica.getCiclosEjecutados(),
                metrica.colapsoDetectado(),
                metrica.saturacionDetectada()
        );
    }

    public String resumenEntrada() {
        return "PredictionInput{" +
                "totalBloques=" + totalBloques +
                ", bloquesActivos=" + bloquesActivos +
                ", porcentajeActivos=" + String.format(java.util.Locale.ROOT, "%.2f", porcentajeActivos) +
                ", densidad=" + String.format(java.util.Locale.ROOT, "%.2f", densidad) +
                ", ratioEnergetico=" + String.format(java.util.Locale.ROOT, "%.2f", ratioEnergetico) +
                ", ratioCoberturaServicios=" + String.format(java.util.Locale.ROOT, "%.2f", ratioCoberturaServicios) +
                ", contaminacion=" + contaminacion +
                ", estabilidadBasica=" + String.format(java.util.Locale.ROOT, "%.2f", estabilidadBasica) +
                ", tipoEstructural=" + tipoEstructural +
                '}';
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

    public double getDensidad() {
        return densidad;
    }

    public double getRatioEnergetico() {
        return ratioEnergetico;
    }

    public double getRatioCoberturaServicios() {
        return ratioCoberturaServicios;
    }

    public int getContaminacion() {
        return contaminacion;
    }

    public double getEstabilidadBasica() {
        return estabilidadBasica;
    }

    public double getBienestar() {
        return bienestar;
    }

    public double getIndiceSaturacion() {
        return indiceSaturacion;
    }

    public double getIndiceViabilidadBase() {
        return indiceViabilidadBase;
    }

    public EstadoSimulacion getEstadoSimulacion() {
        return estadoSimulacion;
    }

    public TipoEstructuralCiudad getTipoEstructural() {
        return tipoEstructural;
    }

    public Map<TipoBloque, Integer> getConteoPorTipo() {
        return conteoPorTipo;
    }

    public double getContaminacionAcumulada()  { return contaminacionAcumulada; }
    public double getTendenciaEstabilidad()    { return tendenciaEstabilidad; }
    public double getTendenciaContaminacion()  { return tendenciaContaminacion; }
    public int    getCiclosEjecutados()        { return ciclosEjecutados; }
    public boolean isColapsoDetectado()        { return colapsoDetectado; }
    public boolean isSaturacionDetectada()     { return saturacionDetectada; }
}
