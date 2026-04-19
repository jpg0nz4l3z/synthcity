package org.synthcity.modulo_3;

import org.synthcity.modulo_2.EstadoSimulacion;

public class PredictionInput {

    private final int totalBloques;
    private final int bloquesActivos;
    private final int bloquesInactivos;
    private final int capacidadMaxima;
    private final double porcentajeActivos;
    private final double porcentajeInactivos;
    private final double densidadOcupacion;
    private final double diversidadTipos;
    private final double ratioEnergia;
    private final double ratioServicios;
    private final double ratioTransporte;
    private final double presionIndustrial;
    private final double pesoResidencial;
    private final double indiceEquilibrioBase;
    private final EstadoSimulacion estadoSimulacion;

    private final double densidad;
    private final double ratioEnergetico;
    private final double ratioCoberturaServicios;
    private final int contaminacion;
    private final double estabilidadBasica;
    private final double bienestar;
    private final double indiceSaturacion;
    private final double indiceViabilidadBase;
    private final TipoEstructuralCiudad tipoEstructural;

    public PredictionInput(
            int totalBloques,
            int bloquesActivos,
            int bloquesInactivos,
            int capacidadMaxima,
            double porcentajeActivos,
            double porcentajeInactivos,
            double densidadOcupacion,
            double diversidadTipos,
            double ratioEnergia,
            double ratioServicios,
            double ratioTransporte,
            double presionIndustrial,
            double pesoResidencial,
            double indiceEquilibrioBase,
            EstadoSimulacion estadoSimulacion,
            double densidad,
            double ratioEnergetico,
            double ratioCoberturaServicios,
            int contaminacion,
            double estabilidadBasica,
            double bienestar,
            double indiceSaturacion,
            double indiceViabilidadBase,
            TipoEstructuralCiudad tipoEstructural
    ) {
        if (totalBloques < 0) {
            throw new IllegalArgumentException("El total de bloques no puede ser negativo.");
        }
        if (bloquesActivos < 0 || bloquesInactivos < 0) {
            throw new IllegalArgumentException("Los bloques activos e inactivos no pueden ser negativos.");
        }
        if (capacidadMaxima < 0) {
            throw new IllegalArgumentException("La capacidad maxima no puede ser negativa.");
        }
        if (porcentajeActivos < 0.0 || porcentajeActivos > 1.0) {
            throw new IllegalArgumentException("porcentajeActivos debe estar entre 0.0 y 1.0.");
        }
        if (porcentajeInactivos < 0.0 || porcentajeInactivos > 1.0) {
            throw new IllegalArgumentException("porcentajeInactivos debe estar entre 0.0 y 1.0.");
        }
        if (densidadOcupacion < 0.0 || densidadOcupacion > 1.0) {
            throw new IllegalArgumentException("densidadOcupacion debe estar entre 0.0 y 1.0.");
        }
        if (diversidadTipos < 0.0 || diversidadTipos > 1.0) {
            throw new IllegalArgumentException("diversidadTipos debe estar entre 0.0 y 1.0.");
        }
        if (ratioEnergia < 0.0 || ratioEnergia > 1.0) {
            throw new IllegalArgumentException("ratioEnergia debe estar entre 0.0 y 1.0.");
        }
        if (ratioServicios < 0.0 || ratioServicios > 1.0) {
            throw new IllegalArgumentException("ratioServicios debe estar entre 0.0 y 1.0.");
        }
        if (ratioTransporte < 0.0 || ratioTransporte > 1.0) {
            throw new IllegalArgumentException("ratioTransporte debe estar entre 0.0 y 1.0.");
        }
        if (presionIndustrial < 0.0 || presionIndustrial > 1.0) {
            throw new IllegalArgumentException("presionIndustrial debe estar entre 0.0 y 1.0.");
        }
        if (pesoResidencial < 0.0 || pesoResidencial > 1.0) {
            throw new IllegalArgumentException("pesoResidencial debe estar entre 0.0 y 1.0.");
        }
        if (indiceEquilibrioBase < 0.0 || indiceEquilibrioBase > 1.0) {
            throw new IllegalArgumentException("indiceEquilibrioBase debe estar entre 0.0 y 1.0.");
        }
        if (estadoSimulacion == null) {
            throw new IllegalArgumentException("estadoSimulacion no puede ser null.");
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
        if (contaminacion < 0 || contaminacion > 100) {
            throw new IllegalArgumentException("contaminacion debe estar entre 0 y 100.");
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
        if (tipoEstructural == null) {
            throw new IllegalArgumentException("tipoEstructural no puede ser null.");
        }

        this.totalBloques = totalBloques;
        this.bloquesActivos = bloquesActivos;
        this.bloquesInactivos = bloquesInactivos;
        this.capacidadMaxima = capacidadMaxima;
        this.porcentajeActivos = porcentajeActivos;
        this.porcentajeInactivos = porcentajeInactivos;
        this.densidadOcupacion = densidadOcupacion;
        this.diversidadTipos = diversidadTipos;
        this.ratioEnergia = ratioEnergia;
        this.ratioServicios = ratioServicios;
        this.ratioTransporte = ratioTransporte;
        this.presionIndustrial = presionIndustrial;
        this.pesoResidencial = pesoResidencial;
        this.indiceEquilibrioBase = indiceEquilibrioBase;
        this.estadoSimulacion = estadoSimulacion;
        this.densidad = densidad;
        this.ratioEnergetico = ratioEnergetico;
        this.ratioCoberturaServicios = ratioCoberturaServicios;
        this.contaminacion = contaminacion;
        this.estabilidadBasica = estabilidadBasica;
        this.bienestar = bienestar;
        this.indiceSaturacion = indiceSaturacion;
        this.indiceViabilidadBase = indiceViabilidadBase;
        this.tipoEstructural = tipoEstructural;
    }

    public static PredictionInput desdeMetrica(MetricaCiudad metrica) {
        if (metrica == null) {
            throw new IllegalArgumentException("La metrica no puede ser null.");
        }

        return new PredictionInput(
                metrica.getTotalBloques(),
                metrica.getBloquesActivos(),
                metrica.getBloquesInactivos(),
                metrica.getCapacidadMaxima(),
                metrica.getPorcentajeActivos(),
                metrica.getPorcentajeInactivos(),
                metrica.getDensidadOcupacion(),
                metrica.getDiversidadTipos(),
                metrica.getRatioEnergia(),
                metrica.getRatioServicios(),
                metrica.getRatioTransporte(),
                metrica.getPresionIndustrial(),
                metrica.getPesoResidencial(),
                metrica.getIndiceEquilibrioBase(),
                metrica.getEstadoSimulacion(),
                metrica.getDensidad(),
                Math.min(1.0, metrica.getRatioEnergetico()),
                Math.min(1.0, metrica.getRatioCoberturaServicios()),
                metrica.getContaminacion(),
                metrica.getEstabilidadBasica(),
                metrica.getBienestar(),
                metrica.getIndiceSaturacion(),
                metrica.getIndiceViabilidadBase(),
                metrica.getTipoEstructural()
        );
    }

    public int getTotalBloques() { return totalBloques; }
    public int getBloquesActivos() { return bloquesActivos; }
    public int getBloquesInactivos() { return bloquesInactivos; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public double getPorcentajeActivos() { return porcentajeActivos; }
    public double getPorcentajeInactivos() { return porcentajeInactivos; }
    public double getDensidadOcupacion() { return densidadOcupacion; }
    public double getDiversidadTipos() { return diversidadTipos; }
    public double getRatioEnergia() { return ratioEnergia; }
    public double getRatioServicios() { return ratioServicios; }
    public double getRatioTransporte() { return ratioTransporte; }
    public double getPresionIndustrial() { return presionIndustrial; }
    public double getPesoResidencial() { return pesoResidencial; }
    public double getIndiceEquilibrioBase() { return indiceEquilibrioBase; }
    public EstadoSimulacion getEstadoSimulacion() { return estadoSimulacion; }

    public double getDensidad() { return densidad; }
    public double getRatioEnergetico() { return ratioEnergetico; }
    public double getRatioCoberturaServicios() { return ratioCoberturaServicios; }
    public int getContaminacion() { return contaminacion; }
    public double getEstabilidadBasica() { return estabilidadBasica; }
    public double getBienestar() { return bienestar; }
    public double getIndiceSaturacion() { return indiceSaturacion; }
    public double getIndiceViabilidadBase() { return indiceViabilidadBase; }
    public TipoEstructuralCiudad getTipoEstructural() { return tipoEstructural; }
}
