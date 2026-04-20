package org.synthcity.modulo_3;

import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class MetricaCiudad {

    // Factores de derivación de indicadores urbanos a partir del conteo por tipo.
    // Centralizados aquí para que los valores sean únicos y reproducibles.
    private static final int FACTOR_PRODUCCION_ENERGIA = 10;
    private static final int FACTOR_CONSUMO_RESIDENCIAL = 2;
    private static final int FACTOR_CONSUMO_INDUSTRIAL = 4;
    private static final int FACTOR_CONSUMO_SERVICIOS = 1;
    private static final int FACTOR_CONSUMO_TRANSPORTE = 1;
    private static final int FACTOR_COBERTURA_SERVICIOS = 3;
    private static final int FACTOR_CONTAMINACION_INDUSTRIAL = 10;
    private static final int FACTOR_CONTAMINACION_RESIDENCIAL = 1;
    private static final int FACTOR_MITIGACION_SERVICIOS = 3;

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

    // Indicadores estructurales ya existentes (Sprint 1)
    private final double densidadOcupacion;
    private final double diversidadTipos;
    private final double ratioEnergia;
    private final double ratioServicios;
    private final double ratioTransporte;
    private final double presionIndustrial;
    private final double pesoResidencial;
    private final double indiceEquilibrioBase;

    // Nuevos indicadores urbanos (Sprint 2).
    // Al no exponerlos todavía ResultadoSimulacion, se derivan de conteoPorTipo
    // usando factores centralizados. Esto NO es recalcular la simulación:
    // es transformar la base cuantitativa ya entregada en indicadores analíticos.
    private final double densidad;
    private final TipoEstructuralCiudad tipoEstructural;
    private final int energiaProducida;
    private final int consumoEnergetico;
    private final int equilibrioEnergetico;
    private final int demandaServicios;
    private final int coberturaServicios;
    private final int soporteTransporte;
    private final int contaminacion;
    private final double bienestar;
    private final double estabilidadBasica;

    // Ratios e índices derivados del Sprint 2
    private final double ratioCoberturaServicios;
    private final double ratioEnergetico;
    private final double indiceSaturacion;
    private final double indiceViabilidadBase;

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
        this.densidad = this.densidadOcupacion;
        this.diversidadTipos = calcularDiversidadTipos(this.conteoPorTipo);
        this.ratioEnergia = ratioPorTipo(TipoBloque.ENERGIA);
        this.ratioServicios = ratioPorTipo(TipoBloque.SERVICIOS);
        this.ratioTransporte = ratioPorTipo(TipoBloque.TRANSPORTE);
        this.presionIndustrial = ratioPorTipo(TipoBloque.INDUSTRIAL);
        this.pesoResidencial = ratioPorTipo(TipoBloque.RESIDENCIAL);
        this.indiceEquilibrioBase = calcularIndiceEquilibrioBase();

        int res = this.conteoPorTipo.getOrDefault(TipoBloque.RESIDENCIAL, 0);
        int ene = this.conteoPorTipo.getOrDefault(TipoBloque.ENERGIA, 0);
        int ind = this.conteoPorTipo.getOrDefault(TipoBloque.INDUSTRIAL, 0);
        int ser = this.conteoPorTipo.getOrDefault(TipoBloque.SERVICIOS, 0);
        int tra = this.conteoPorTipo.getOrDefault(TipoBloque.TRANSPORTE, 0);

        this.energiaProducida = ene * FACTOR_PRODUCCION_ENERGIA;
        this.consumoEnergetico =
                res * FACTOR_CONSUMO_RESIDENCIAL
                        + ind * FACTOR_CONSUMO_INDUSTRIAL
                        + ser * FACTOR_CONSUMO_SERVICIOS
                        + tra * FACTOR_CONSUMO_TRANSPORTE;
        this.equilibrioEnergetico = this.energiaProducida - this.consumoEnergetico;
        this.demandaServicios = res;
        this.coberturaServicios = ser * FACTOR_COBERTURA_SERVICIOS;
        this.soporteTransporte = tra;
        this.contaminacion = calcularContaminacion(ind, res, ser);

        this.ratioEnergetico = calcularRatioEnergetico(this.energiaProducida, this.consumoEnergetico);
        this.ratioCoberturaServicios = calcularRatioCobertura(this.coberturaServicios, this.demandaServicios);

        this.tipoEstructural = inferirTipoEstructural(this.conteoPorTipo, this.diversidadTipos, this.totalBloques);
        this.estabilidadBasica = calcularEstabilidadBasica();
        this.bienestar = calcularBienestar();
        this.indiceSaturacion = calcularIndiceSaturacion();
        this.indiceViabilidadBase = calcularIndiceViabilidad();
    }

    private double ratioPorTipo(TipoBloque tipo) {
        if (this.totalBloques <= 0) {
            return 0.0;
        }
        return (double) this.conteoPorTipo.getOrDefault(tipo, 0) / this.totalBloques;
    }

    private double calcularDensidadOcupacion(int total, int capacidad) {
        if (capacidad <= 0) {
            return 0.0;
        }
        return (double) total / capacidad;
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

    private int calcularContaminacion(int ind, int res, int ser) {
        int base = ind * FACTOR_CONTAMINACION_INDUSTRIAL
                + res * FACTOR_CONTAMINACION_RESIDENCIAL
                - ser * FACTOR_MITIGACION_SERVICIOS;
        if (base < 0) return 0;
        if (base > 100) return 100;
        return base;
    }

    private double calcularRatioEnergetico(int producida, int consumo) {
        if (consumo <= 0) {
            return 1.0;
        }
        return (double) producida / consumo;
    }

    private double calcularRatioCobertura(int cobertura, int demanda) {
        if (demanda <= 0) {
            return 1.0;
        }
        return (double) cobertura / demanda;
    }

    private TipoEstructuralCiudad inferirTipoEstructural(Map<TipoBloque, Integer> conteo,
                                                         double diversidad,
                                                         int total) {
        if (total <= 0) {
            return TipoEstructuralCiudad.SIN_DATOS;
        }
        if (diversidad >= 0.80) {
            return TipoEstructuralCiudad.EQUILIBRADA;
        }
        TipoBloque dominante = null;
        int maximo = -1;
        for (TipoBloque tipo : TipoBloque.values()) {
            int count = conteo.getOrDefault(tipo, 0);
            if (count > maximo) {
                maximo = count;
                dominante = tipo;
            }
        }
        if (dominante == null) {
            return TipoEstructuralCiudad.SIN_DATOS;
        }
        return switch (dominante) {
            case RESIDENCIAL -> TipoEstructuralCiudad.RESIDENCIAL_DOMINANTE;
            case INDUSTRIAL -> TipoEstructuralCiudad.INDUSTRIAL_DOMINANTE;
            case ENERGIA -> TipoEstructuralCiudad.ENERGETICA_DOMINANTE;
            case SERVICIOS -> TipoEstructuralCiudad.SERVICIOS_DOMINANTE;
            case TRANSPORTE -> TipoEstructuralCiudad.TRANSPORTE_DOMINANTE;
        };
    }

    private double calcularEstabilidadBasica() {
        if (this.totalBloques <= 0) {
            return 0.0;
        }
        double cobertura = Math.min(1.0, this.ratioCoberturaServicios);
        double energia = Math.min(1.0, this.ratioEnergetico);
        double diversidad = this.diversidadTipos;
        double actividad = this.porcentajeActivos;
        double score = (cobertura * 0.25) + (energia * 0.25) + (diversidad * 0.25) + (actividad * 0.25);
        return clamp01(score);
    }

    private double calcularBienestar() {
        if (this.totalBloques <= 0) {
            return 0.0;
        }
        double cobertura = Math.min(1.0, this.ratioCoberturaServicios);
        double actividad = this.porcentajeActivos;
        double contaminacionNormalizada = this.contaminacion / 100.0;
        double score = (cobertura * 0.45) + (actividad * 0.40) + ((1.0 - contaminacionNormalizada) * 0.15);
        return clamp01(score);
    }

    private double calcularIndiceSaturacion() {
        double base = this.densidad;
        if (hayDeficitEnergetico()) base += 0.05;
        if (hayDeficitServicios()) base += 0.05;
        if (tieneContaminacionAlta()) base += 0.05;
        return clamp01(base);
    }

    private double calcularIndiceEquilibrioBase() {
        double score = 0.0;
        score += porcentajeActivos * 0.40;
        score += diversidadTipos * 0.20;
        score += ratioEnergia * 0.10;
        score += ratioServicios * 0.10;
        score += ratioTransporte * 0.10;
        if (presionIndustrial <= 0.30) score += 0.05;
        if (densidadOcupacion <= 0.80) score += 0.05;
        return clamp01(score);
    }

    private double calcularIndiceViabilidad() {
        if (this.totalBloques <= 0) {
            return 0.0;
        }
        double score = 0.0;
        score += this.porcentajeActivos * 0.30;
        score += Math.min(1.0, this.ratioEnergetico) * 0.20;
        score += Math.min(1.0, this.ratioCoberturaServicios) * 0.20;
        score += this.estabilidadBasica * 0.15;
        score += this.bienestar * 0.15;
        score -= (this.contaminacion / 100.0) * 0.10;
        score -= Math.max(0.0, this.densidad - UMBRAL_DENSIDAD_RIESGO) * 0.10;
        return clamp01(score);
    }

    private static double clamp01(double v) {
        if (v < 0.0) return 0.0;
        if (v > 1.0) return 1.0;
        return v;
    }

    public boolean hayDeficitEnergetico() {
        return this.totalBloques > 0 && this.ratioEnergetico < UMBRAL_RATIO_DEFICIT;
    }

    public boolean hayDeficitServicios() {
        return this.totalBloques > 0 && this.ratioCoberturaServicios < UMBRAL_RATIO_DEFICIT;
    }

    public boolean tieneRiesgoPorDensidad() {
        return this.densidad >= UMBRAL_DENSIDAD_RIESGO;
    }

    public boolean tieneContaminacionAlta() {
        return this.contaminacion >= UMBRAL_CONTAMINACION_ALTA;
    }

    public String resumenMetrico() {
        return "Total=" + totalBloques
                + ", activos=" + bloquesActivos
                + ", %act=" + String.format("%.2f", porcentajeActivos)
                + ", densidad=" + String.format("%.2f", densidad)
                + ", ratioE=" + String.format("%.2f", ratioEnergetico)
                + ", ratioS=" + String.format("%.2f", ratioCoberturaServicios)
                + ", contaminacion=" + contaminacion
                + ", estabilidad=" + String.format("%.2f", estabilidadBasica)
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
