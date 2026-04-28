package org.synthcity.modulo_3;


import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.util.Map;
import java.util.Collections;

public final class MetricaCiudad {
    private final int totalBloques;
    private final int bloquesActivos;
    private final int bloquesInactivos;
    private final double porcentajeActivos;
    private final double porcentajeInactivos;
    private final Map<TipoBloque, Integer> conteoPorTipo;

    //Atributos Sprint 3
    private final double tendenciaEstabilidad;
    private final double tendenciaContaminacion;
    private final double contaminacionAcumulada;
    private final int ciclosEjecutados;
    private final MotivoParadaSimulacion motivoParada;
    private final boolean necesidadExpansionDetectada;
    private final double coberturaServiciosPonderada;
    private final double eficienciaTransporte;

    //ultimo añadido
    private final double densidad;
    private final double indiceSaturacion;
    private final double ratioCoberturaServicios;
    private final double ratioEnergetico;
    private final double bienestar;
    private final double estabilidadMedia;

    public MetricaCiudad(ResultadoSimulacion resultado) {
        this.totalBloques = resultado.getBloquesTotales();
        this.bloquesActivos = resultado.getBloquesActivos();
        this.bloquesInactivos = this.totalBloques - this.bloquesActivos;

        if (this.totalBloques > 0) this.porcentajeActivos = (double) this.bloquesActivos / this.totalBloques;
        else this.porcentajeActivos = 0.0;

        this.porcentajeInactivos = 1.0 - this.porcentajeActivos;

        this.conteoPorTipo = Collections.unmodifiableMap(resultado.getConteoPorTipo());

        this.tendenciaEstabilidad = resultado.getTendenciaEstabilidad();
        this.tendenciaContaminacion = resultado.getTendenciaContaminacion();
        this.ciclosEjecutados = resultado.getCiclosEjecutados();
        this.motivoParada = resultado.getMotivoParada();
        this.necesidadExpansionDetectada = resultado.getNecesidadExpansionDetectada();
        this.coberturaServiciosPonderada = resultado.getCoberturaServiciosPonderada();
        this.eficienciaTransporte = resultado.getEficienciaTransporte();
        this.contaminacionAcumulada = resultado.getCiclos()
                .stream()
                .mapToDouble(c -> c.getContaminacionCiclo())
                .sum();
//ultimo añadido
        this.densidad = resultado.getDensidad();
        this.indiceSaturacion = resultado.getIndiceSaturacion();
        this.ratioCoberturaServicios = resultado.getRatioCoberturaServicios();

    }

    public int getTotalBloques() { return totalBloques; }
    public int getBloquesActivos() { return bloquesActivos; }
    public int getBloquesInactivos() { return bloquesInactivos; }
    public double getPorcentajeActivos() { return porcentajeActivos; }
    public double getPorcentajeInactivos() { return porcentajeInactivos; }
    public Map <TipoBloque, Integer> getConteoPorTipo() { return conteoPorTipo; }

    public boolean estaEnTendenciaNegativa(){
        return this.tendenciaEstabilidad < -0.1;
    }

    public boolean estaEnTendenciaPositiva(){
        return this.tendenciaEstabilidad > 0.1;
    }

    public boolean tieneContaminacionCreciente() {
        if (this.tendenciaContaminacion > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean colapsoDetectado() {
        return this.motivoParada == MotivoParadaSimulacion.COLAPSO_ENERGETICO;
    }

    public boolean saturacionDetectada(){
        return this.motivoParada == MotivoParadaSimulacion.SATURACION_CRITICA;
    }

    public double getTendenciaEstabilidad() {
        return tendenciaEstabilidad;
    }

    public double getTendenciaContaminacion() {
        return tendenciaContaminacion;
    }

    public double getContaminacionAcumulada() {
        return contaminacionAcumulada;
    }

    public int getCiclosEjecutados() {
        return ciclosEjecutados;
    }

    public MotivoParadaSimulacion getMotivoParada() {
        return motivoParada;
    }

    public boolean isNecesidadExpansionDetectada() {
        return necesidadExpansionDetectada;
    }

    public double getCoberturaServiciosPonderada() {
        return coberturaServiciosPonderada;
    }

    public double getEficienciaTransporte() {
        return eficienciaTransporte;
    }

    //ultimo añadidoge
    public double getDensidad() {
        return densidad;
    }

    public double getIndiceSaturacion() {
        return indiceSaturacion;
    }

    public double getRatioCoberturaServicios() {
        return ratioCoberturaServicios;
    }

    public double getRatioEnergetico() {
        return ratioEnergetico;
    }

    public double getBienestar() {
        return bienestar;
    }

    public double getEstabilidadMedia() {
        return estabilidadMedia;
    }
}