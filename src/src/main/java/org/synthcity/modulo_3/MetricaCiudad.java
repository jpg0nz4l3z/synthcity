package org.synthcity.modulo_3;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_1.TipoEstructuralCiudad;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;

public final class MetricaCiudad {

    private static final double UMBRAL_DENSIDAD_RIESGO = 0.85;
    private static final double UMBRAL_CONTAMINACION_ALTA = 60.0;
    private static final double UMBRAL_RATIO_DEFICIT = 0.80;

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

    private final double densidad;
    private final TipoEstructuralCiudad tipoEstructural;
    private final int energiaProducida;
    private final int consumoEnergetico;
    private final int equilibrioEnergetico;
    private final int demandaServicios;
    private final int coberturaServicios;
    private final int presionIndustrialAbsoluta;
    private final int soporteTransporte;
    private final int contaminacion;
    private final double bienestar;
    private final double estabilidadBasica;
    private final double ratioCoberturaServicios;
    private final double ratioEnergetico;
    private final double indiceSaturacion;
    private final double indiceViabilidadBase;

    public MetricaCiudad(ResultadoSimulacion resultado) {
        validarResultado(resultado);

        this.totalBloques = resultado.getBloquesTotales();
        this.bloquesActivos = resultado.getBloquesActivos();
        this.bloquesInactivos = resultado.getBloquesInactivos();
        this.porcentajeActivos = totalBloques > 0 ? (double) bloquesActivos / totalBloques : 0.0;
        this.porcentajeInactivos = totalBloques > 0 ? (double) bloquesInactivos / totalBloques : 1.0;

        Map<TipoBloque, Integer> copia = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            copia.put(tipo, resultado.getConteoPorTipo().get(tipo));
        }
        this.conteoPorTipo = Collections.unmodifiableMap(copia);

        this.filas = resultado.getFilas();
        this.columnas = resultado.getColumnas();
        this.capacidadMaxima = resultado.getCapacidadMaxima();
        this.estadoSimulacion = resultado.getEstadoSimulacion();

        this.densidad = clamp01(resultado.getDensidad());
        this.densidadOcupacion = this.densidad;
        this.tipoEstructural = resultado.getTipoEstructural();
        this.energiaProducida = resultado.getEnergiaProducida();
        this.consumoEnergetico = resultado.getConsumoEnergetico();
        this.equilibrioEnergetico = resultado.getEquilibrioEnergetico();
        this.demandaServicios = resultado.getDemandaServicios();
        this.coberturaServicios = resultado.getCoberturaServicios();
        this.presionIndustrialAbsoluta = resultado.getPresionIndustrial();
        this.soporteTransporte = resultado.getSoporteTransporte();
        this.contaminacion = clampInt(resultado.getContaminacion(), 0, 100);
        this.bienestar = clamp01(resultado.getBienestar());
        this.estabilidadBasica = clamp01(resultado.getEstabilidadBasica());
        this.ratioEnergetico = normalizarRatio(resultado.getRatioEnergetico());
        this.ratioCoberturaServicios = normalizarRatio(resultado.getRatioCoberturaServicios());

        this.diversidadTipos = calcularDiversidadTipos(this.conteoPorTipo);
        this.ratioEnergia = ratioPorTipo(TipoBloque.ENERGIA);
        this.ratioServicios = ratioPorTipo(TipoBloque.SERVICIOS);
        this.ratioTransporte = ratioPorTipo(TipoBloque.TRANSPORTE);
        this.presionIndustrial = ratioPorTipo(TipoBloque.INDUSTRIAL);
        this.pesoResidencial = ratioPorTipo(TipoBloque.RESIDENCIAL);
        this.indiceEquilibrioBase = calcularIndiceEquilibrioBase();
        this.indiceSaturacion = calcularIndiceSaturacion();
        this.indiceViabilidadBase = calcularIndiceViabilidad();
    }

    private void validarResultado(ResultadoSimulacion resultado) {
        if (resultado == null) {
            throw new ResultadoSimulacionInvalidoException("No se puede construir MetricaCiudad con un resultado nulo.");
        }
        if (resultado.getConteoPorTipo() == null) {
            throw new ResultadoSimulacionInvalidoException("El conteo por tipo no puede ser nulo.");
        }
        if (resultado.getBloquesTotales() < 0 || resultado.getBloquesActivos() < 0 || resultado.getBloquesInactivos() < 0) {
            throw new ResultadoSimulacionInvalidoException("Los valores de bloques no pueden ser negativos.");
        }
        if (resultado.getBloquesActivos() + resultado.getBloquesInactivos() != resultado.getBloquesTotales()) {
            throw new ResultadoSimulacionInvalidoException("Activos + inactivos debe coincidir con el total.");
        }
        if (resultado.getBloquesActivos() > resultado.getBloquesTotales()
                || resultado.getBloquesInactivos() > resultado.getBloquesTotales()) {
            throw new ResultadoSimulacionInvalidoException("Los bloques activos o inactivos no pueden superar el total.");
        }
        if (resultado.getEquilibrioEnergetico() != resultado.getEnergiaProducida() - resultado.getConsumoEnergetico()) {
            throw new ResultadoSimulacionInvalidoException("El equilibrio energetico no coincide con produccion - consumo.");
        }
        if (resultado.getEnergiaProducida() < 0
                || resultado.getConsumoEnergetico() < 0
                || resultado.getDemandaServicios() < 0
                || resultado.getCoberturaServicios() < 0
                || resultado.getPresionIndustrial() < 0
                || resultado.getSoporteTransporte() < 0
                || resultado.getContaminacion() < 0) {
            throw new ResultadoSimulacionInvalidoException("Las variables urbanas no pueden ser negativas.");
        }
        if (resultado.getTipoEstructural() == null || resultado.getEstadoSimulacion() == null) {
            throw new ResultadoSimulacionInvalidoException("El resultado de simulacion no contiene estado estructural completo.");
        }
        for (TipoBloque tipo : TipoBloque.values()) {
            if (!resultado.getConteoPorTipo().containsKey(tipo)) {
                throw new ResultadoSimulacionInvalidoException("Contrato incompleto: falta el tipo de bloque " + tipo + ".");
            }
            Integer cantidad = resultado.getConteoPorTipo().get(tipo);
            if (cantidad == null || cantidad < 0) {
                throw new ResultadoSimulacionInvalidoException("Conteo invalido para el tipo de bloque " + tipo + ".");
            }
        }
    }

    private double ratioPorTipo(TipoBloque tipo) {
        if (totalBloques <= 0) {
            return 0.0;
        }
        return (double) conteoPorTipo.getOrDefault(tipo, 0) / totalBloques;
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

    private double calcularIndiceEquilibrioBase() {
        double score = 0.0;
        score += porcentajeActivos * 0.35;
        score += diversidadTipos * 0.20;
        score += Math.min(1.0, ratioEnergetico) * 0.15;
        score += Math.min(1.0, ratioCoberturaServicios) * 0.15;
        score += estabilidadBasica * 0.15;
        return clamp01(score);
    }

    private double calcularIndiceSaturacion() {
        double base = densidad;
        if (hayDeficitEnergetico()) {
            base += 0.05;
        }
        if (hayDeficitServicios()) {
            base += 0.05;
        }
        if (tieneContaminacionAlta()) {
            base += 0.05;
        }
        return clamp01(base);
    }

    private double calcularIndiceViabilidad() {
        if (totalBloques <= 0) {
            return 0.0;
        }
        double score = 0.0;
        score += porcentajeActivos * 0.25;
        score += Math.min(1.0, ratioEnergetico) * 0.20;
        score += Math.min(1.0, ratioCoberturaServicios) * 0.20;
        score += estabilidadBasica * 0.15;
        score += bienestar * 0.20;
        score -= (contaminacion / 100.0) * 0.10;
        score -= Math.max(0.0, densidad - UMBRAL_DENSIDAD_RIESGO) * 0.10;
        return clamp01(score);
    }

    private static double normalizarRatio(double valor) {
        if (!Double.isFinite(valor) || valor < 0.0) {
            return 0.0;
        }
        return valor;
    }

    private static int clampInt(int valor, int min, int max) {
        if (valor < min) {
            return min;
        }
        if (valor > max) {
            return max;
        }
        return valor;
    }

    private static double clamp01(double valor) {
        if (valor < 0.0) {
            return 0.0;
        }
        if (valor > 1.0) {
            return 1.0;
        }
        return valor;
    }

    public boolean hayDeficitEnergetico() {
        return totalBloques > 0 && ratioEnergetico < UMBRAL_RATIO_DEFICIT;
    }

    public boolean hayDeficitServicios() {
        return totalBloques > 0 && ratioCoberturaServicios < UMBRAL_RATIO_DEFICIT;
    }

    public boolean tieneRiesgoPorDensidad() {
        return densidad >= UMBRAL_DENSIDAD_RIESGO;
    }

    public boolean tieneContaminacionAlta() {
        return contaminacion >= UMBRAL_CONTAMINACION_ALTA;
    }

    public String resumenMetrico() {
        return "Total=" + totalBloques
                + ", activos=" + bloquesActivos
                + ", actividad=" + String.format(java.util.Locale.ROOT, "%.2f", porcentajeActivos)
                + ", densidad=" + String.format(java.util.Locale.ROOT, "%.2f", densidad)
                + ", ratioE=" + String.format(java.util.Locale.ROOT, "%.2f", ratioEnergetico)
                + ", ratioS=" + String.format(java.util.Locale.ROOT, "%.2f", ratioCoberturaServicios)
                + ", contaminacion=" + contaminacion
                + ", estabilidad=" + String.format(java.util.Locale.ROOT, "%.2f", estabilidadBasica)
                + ", tipo=" + tipoEstructural;
    }

    public int getTotalBloques() { return totalBloques; }
    public int getBloquesActivos() { return bloquesActivos; }
    public int getBloquesInactivos() { return bloquesInactivos; }
    public double getPorcentajeActivos() { return porcentajeActivos; }
    public double getPorcentajeInactivos() { return porcentajeInactivos; }
    public Map<TipoBloque, Integer> getConteoPorTipo() { return conteoPorTipo; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public EstadoSimulacion getEstadoSimulacion() { return estadoSimulacion; }
    public double getDensidadOcupacion() { return densidadOcupacion; }
    public double getDiversidadTipos() { return diversidadTipos; }
    public double getRatioEnergia() { return ratioEnergia; }
    public double getRatioServicios() { return ratioServicios; }
    public double getRatioTransporte() { return ratioTransporte; }
    public double getPresionIndustrial() { return presionIndustrial; }
    public double getPesoResidencial() { return pesoResidencial; }
    public double getIndiceEquilibrioBase() { return indiceEquilibrioBase; }
    public double getDensidad() { return densidad; }
    public TipoEstructuralCiudad getTipoEstructural() { return tipoEstructural; }
    public int getEnergiaProducida() { return energiaProducida; }
    public int getConsumoEnergetico() { return consumoEnergetico; }
    public int getEquilibrioEnergetico() { return equilibrioEnergetico; }
    public int getDemandaServicios() { return demandaServicios; }
    public int getCoberturaServicios() { return coberturaServicios; }
    public int getPresionIndustrialAbsoluta() { return presionIndustrialAbsoluta; }
    public int getSoporteTransporte() { return soporteTransporte; }
    public int getContaminacion() { return contaminacion; }
    public double getBienestar() { return bienestar; }
    public double getEstabilidadBasica() { return estabilidadBasica; }
    public double getRatioCoberturaServicios() { return ratioCoberturaServicios; }
    public double getRatioEnergetico() { return ratioEnergetico; }
    public double getIndiceSaturacion() { return indiceSaturacion; }
    public double getIndiceViabilidadBase() { return indiceViabilidadBase; }

    @Override
    public String toString() {
        return "MetricaCiudad{" + resumenMetrico() + "}";
    }
}
