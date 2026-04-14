package org.synthcity.modulo_3;

import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class MetricaCiudad {

    private final int totalBloques;
    private final int bloquesActivos;
    private final int bloquesInactivos;
    private final double porcentajeActivos;
    private final double porcentajeInactivos;
    private final Map<TipoBloque, Integer> conteoPorTipo;

    private final int filas;
    private final int columnas;
    private final int capacidadMaxima;
    private final EstadoSimulacion estadoSimulacion;

    private final double densidadOcupacion;
    private final double diversidadTipos;
    private final double ratioEnergia;
    private final double ratioServicios;
    private final double ratioTransporte;
    private final double presionIndustrial;
    private final double pesoResidencial;
    private final double indiceEquilibrioBase;

    public MetricaCiudad(ResultadoSimulacion resultado) {
        if (resultado == null) {
            throw new ResultadoSimulacionInvalidoException("No se puede construir MetricaCiudad con un resultado nulo.");
        }
        if (resultado.getConteoPorTipo() == null) {
            throw new ResultadoSimulacionInvalidoException("El conteo por tipo no puede ser nulo.");
        }

        this.totalBloques = resultado.getBloquesTotales();
        this.bloquesActivos = resultado.getBloquesActivos();
        this.bloquesInactivos = resultado.getBloquesInactivos();

        if (this.totalBloques < 0 || this.bloquesActivos < 0 || this.bloquesInactivos < 0) {
            throw new ResultadoSimulacionInvalidoException("Los valores de bloques no pueden ser negativos.");
        }

        if ((this.bloquesActivos + this.bloquesInactivos) != this.totalBloques) {
            throw new ResultadoSimulacionInvalidoException("Activos + inactivos debe coincidir con el total.");
        }

        if (this.totalBloques > 0) {
            this.porcentajeActivos = (double) this.bloquesActivos / this.totalBloques;
        } else {
            this.porcentajeActivos = 0.0;
        }

        this.porcentajeInactivos = 1.0 - this.porcentajeActivos;

        Map<TipoBloque, Integer> copia = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            copia.put(tipo, resultado.getConteoPorTipo().getOrDefault(tipo, 0));
        }
        this.conteoPorTipo = Collections.unmodifiableMap(copia);

        this.filas = resultado.getFilas();
        this.columnas = resultado.getColumnas();
        this.capacidadMaxima = resultado.getCapacidadMaxima();
        this.estadoSimulacion = resultado.getEstadoSimulacion();

        this.densidadOcupacion = calcularDensidadOcupacion(this.totalBloques, this.capacidadMaxima);
        this.diversidadTipos = calcularDiversidadTipos(this.conteoPorTipo);
        this.ratioEnergia = calcularRatioPorTipo(this.conteoPorTipo, TipoBloque.ENERGIA, this.totalBloques);
        this.ratioServicios = calcularRatioPorTipo(this.conteoPorTipo, TipoBloque.SERVICIOS, this.totalBloques);
        this.ratioTransporte = calcularRatioPorTipo(this.conteoPorTipo, TipoBloque.TRANSPORTE, this.totalBloques);
        this.presionIndustrial = calcularRatioPorTipo(this.conteoPorTipo, TipoBloque.INDUSTRIAL, this.totalBloques);
        this.pesoResidencial = calcularRatioPorTipo(this.conteoPorTipo, TipoBloque.RESIDENCIAL, this.totalBloques);
        this.indiceEquilibrioBase = calcularIndiceEquilibrioBase();
    }

    private double calcularDensidadOcupacion(int totalBloques, int capacidadMaxima) {
        if (capacidadMaxima <= 0) {
            return 0.0;
        }
        return (double) totalBloques / capacidadMaxima;
    }

    private double calcularDiversidadTipos(Map<TipoBloque, Integer> conteo) {
        int tiposPresentes = 0;
        for (TipoBloque tipo : TipoBloque.values()) {
            if (conteo.getOrDefault(tipo, 0) > 0) {
                tiposPresentes++;
            }
        }
        return (double) tiposPresentes / TipoBloque.values().length;
    }

    private double calcularRatioPorTipo(Map<TipoBloque, Integer> conteo, TipoBloque tipo, int total) {
        if (total <= 0) {
            return 0.0;
        }
        return (double) conteo.getOrDefault(tipo, 0) / total;
    }

    private double calcularIndiceEquilibrioBase() {
        double score = 0.0;

        score += porcentajeActivos * 0.40;
        score += diversidadTipos * 0.20;
        score += ratioEnergia * 0.10;
        score += ratioServicios * 0.10;
        score += ratioTransporte * 0.10;

        if (presionIndustrial <= 0.30) {
            score += 0.05;
        }

        if (densidadOcupacion <= 0.80) {
            score += 0.05;
        }

        if (score < 0.0) {
            return 0.0;
        }
        if (score > 1.0) {
            return 1.0;
        }
        return score;
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

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public EstadoSimulacion getEstadoSimulacion() {
        return estadoSimulacion;
    }

    public double getDensidadOcupacion() {
        return densidadOcupacion;
    }

    public double getDiversidadTipos() {
        return diversidadTipos;
    }

    public double getRatioEnergia() {
        return ratioEnergia;
    }

    public double getRatioServicios() {
        return ratioServicios;
    }

    public double getRatioTransporte() {
        return ratioTransporte;
    }

    public double getPresionIndustrial() {
        return presionIndustrial;
    }

    public double getPesoResidencial() {
        return pesoResidencial;
    }

    public double getIndiceEquilibrioBase() {
        return indiceEquilibrioBase;
    }
}