package org.synthcity.modulo_3;

import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_1.TipoEstructuralCiudad;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.MotivoParadaSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class MetricaCiudad {

    private static final double UMBRAL_DENSIDAD_RIESGO = 0.85;
    private static final double UMBRAL_CONTAMINACION_ALTA = 60.0;
    private static final double UMBRAL_RATIO_DEFICIT = 0.80;

    // Heredados / básicos
    private final int totalBloques;
    private final int bloquesActivos;
    private final int bloquesInactivos;
    private final double porcentajeActivos;
    private final double porcentajeInactivos;
    private final Map<TipoBloque, Integer> conteoPorTipo;

    // Estructura y estado de simulación
    private final int filas;
    private final int columnas;
    private final int capacidadMaxima;
    private final EstadoSimulacion estadoSimulacion;

    // Métricas estructurales / analíticas
    private final double densidadOcupacion;
    private final double diversidadTipos;
    private final double ratioEnergia;
    private final double ratioServicios;
    private final double ratioTransporte;
    private final double pesoResidencial;
    private final double indiceEquilibrioBase;

    // Datos ricos procedentes de ResultadoSimulacion
    private final double densidad;
    private final TipoEstructuralCiudad tipoEstructural;
    private final int energiaProducida;
    private final int consumoEnergetico;
    private final int equilibrioEnergetico;
    private final int demandaServicios;
    private final int coberturaServicios;
    private final int presionIndustrial;
    private final int soporteTransporte;
    private final int contaminacion;
    private final double bienestar;
    private final double estabilidadBasica;

    // Derivados
    private final double ratioCoberturaServicios;
    private final double ratioEnergetico;
    private final double indiceSaturacion;
    private final double indiceViabilidadBase;

    // Sprint 3 - métricas temporales y dinámicas
    private final double tendenciaEstabilidad;
    private final double tendenciaContaminacion;
    private final double contaminacionAcumulada;
    private final int ciclosEjecutados;
    private final MotivoParadaSimulacion motivoParada;
    private final boolean necesidadExpansionDetectada;
    private final double coberturaServiciosPonderada;
    private final double eficienciaTransporte;
    private final double estabilidadMedia;

    public MetricaCiudad(ResultadoSimulacion resultado) {
        if (resultado == null) {
            throw new ResultadoSimulacionInvalidoException(
                    "No se puede construir MetricaCiudad con un resultado nulo."
            );
        }
        if (resultado.getConteoPorTipo() == null) {
            throw new ResultadoSimulacionInvalidoException(
                    "El conteo por tipo no puede ser nulo."
            );
        }

        this.totalBloques = resultado.getBloquesTotales();
        this.bloquesActivos = resultado.getBloquesActivos();
        this.bloquesInactivos = resultado.getBloquesInactivos();

        if (this.totalBloques < 0 || this.bloquesActivos < 0 || this.bloquesInactivos < 0) {
            throw new ResultadoSimulacionInvalidoException(
                    "Los valores de bloques no pueden ser negativos."
            );
        }
        if (this.bloquesActivos + this.bloquesInactivos != this.totalBloques) {
            throw new ResultadoSimulacionInvalidoException(
                    "Activos + inactivos debe coincidir con el total."
            );
        }

        this.porcentajeActivos = this.totalBloques == 0
                ? 0.0
                : (double) this.bloquesActivos / this.totalBloques;
        this.porcentajeInactivos = this.totalBloques == 0
                ? 0.0
                : 1.0 - this.porcentajeActivos;

        Map<TipoBloque, Integer> copia = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            Integer cantidad = resultado.getConteoPorTipo().get(tipo);
            if (cantidad == null || cantidad < 0) {
                throw new ResultadoSimulacionInvalidoException(
                        "Conteo inválido para el tipo " + tipo + "."
                );
            }
            copia.put(tipo, cantidad);
        }
        this.conteoPorTipo = Collections.unmodifiableMap(copia);

        this.filas = resultado.getFilas();
        this.columnas = resultado.getColumnas();
        this.capacidadMaxima = resultado.getCapacidadMaxima();
        this.estadoSimulacion = resultado.getEstadoSimulacion();

        if (this.filas <= 0 || this.columnas <= 0 || this.capacidadMaxima <= 0) {
            throw new ResultadoSimulacionInvalidoException(
                    "Las dimensiones o la capacidad máxima son inválidas."
            );
        }
        if (this.estadoSimulacion == null) {
            throw new ResultadoSimulacionInvalidoException(
                    "El estado de simulación no puede ser nulo."
            );
        }

        // Copia de métricas del Módulo 2
        this.densidad = resultado.getDensidad();
        this.densidadOcupacion = this.densidad;
        this.tipoEstructural = resultado.getTipoEstructural();
        this.energiaProducida = resultado.getEnergiaProducida();
        this.consumoEnergetico = resultado.getConsumoEnergetico();
        this.equilibrioEnergetico = resultado.getEquilibrioEnergetico();
        this.demandaServicios = resultado.getDemandaServicios();
        this.coberturaServicios = resultado.getCoberturaServicios();
        this.presionIndustrial = resultado.getPresionIndustrial();
        this.soporteTransporte = resultado.getSoporteTransporte();
        this.contaminacion = resultado.getContaminacion();
        this.bienestar = resultado.getBienestar();
        this.estabilidadBasica = resultado.getEstabilidadBasica();
        this.estabilidadMedia = resultado.getEstabilidadMedia();

        if (this.estabilidadMedia < 0.0 || this.estabilidadMedia > 1.0) {
            throw new ResultadoSimulacionInvalidoException(
                    "La estabilidad media debe estar entre 0.0 y 1.0."
            );
        }

        if (this.densidad < 0.0 || this.densidad > 1.0) {
            throw new ResultadoSimulacionInvalidoException(
                    "La densidad debe estar entre 0.0 y 1.0."
            );
        }
        if (this.tipoEstructural == null) {
            throw new ResultadoSimulacionInvalidoException(
                    "El tipo estructural no puede ser nulo."
            );
        }
        if (this.energiaProducida < 0 || this.consumoEnergetico < 0 || this.demandaServicios < 0
                || this.coberturaServicios < 0 || this.presionIndustrial < 0
                || this.soporteTransporte < 0 || this.contaminacion < 0) {
            throw new ResultadoSimulacionInvalidoException(
                    "Las métricas de simulación no pueden ser negativas."
            );
        }
        if (this.equilibrioEnergetico != this.energiaProducida - this.consumoEnergetico) {
            throw new ResultadoSimulacionInvalidoException(
                    "El equilibrio energético no coincide con energiaProducida - consumoEnergetico."
            );
        }
        if (this.bienestar < 0.0 || this.bienestar > 1.0) {
            throw new ResultadoSimulacionInvalidoException(
                    "El bienestar debe estar entre 0.0 y 1.0."
            );
        }
        if (this.estabilidadBasica < 0.0 || this.estabilidadBasica > 1.0) {
            throw new ResultadoSimulacionInvalidoException(
                    "La estabilidad básica debe estar entre 0.0 y 1.0."
            );
        }

        // Derivados propios de Módulo 3
        this.diversidadTipos = calcularDiversidadTipos(this.conteoPorTipo);
        this.ratioEnergetico = calcularRatioEnergetico(this.energiaProducida, this.consumoEnergetico);
        this.ratioCoberturaServicios = calcularRatioCobertura(this.coberturaServicios, this.demandaServicios);

        // Estos dos son indicadores analíticos complementarios.
        // Usan datos ya agregados; no reconstruyen la simulación.
        this.ratioEnergia = this.ratioEnergetico;
        this.ratioServicios = this.ratioCoberturaServicios;
        this.ratioTransporte = ratioPorTipo(TipoBloque.TRANSPORTE);
        this.pesoResidencial = ratioPorTipo(TipoBloque.RESIDENCIAL);

        this.indiceEquilibrioBase = calcularIndiceEquilibrioBase();
        this.indiceSaturacion = calcularIndiceSaturacion();
        this.indiceViabilidadBase = calcularIndiceViabilidad();

        this.tendenciaEstabilidad = resultado.getTendenciaEstabilidad();
        this.tendenciaContaminacion = resultado.getTendenciaContaminacion();
        this.contaminacionAcumulada = resultado.getContaminacionAcumulada();
        this.ciclosEjecutados = resultado.getCiclosEjecutados();
        this.motivoParada = resultado.getMotivoParada();
        this.necesidadExpansionDetectada = resultado.isNecesidadExpansionDetectada();
        this.coberturaServiciosPonderada = resultado.getCoberturaServiciosPonderada();
        this.eficienciaTransporte = resultado.getEficienciaTransporte();

        if (this.contaminacionAcumulada < 0.0 || this.ciclosEjecutados < 0) {
            throw new ResultadoSimulacionInvalidoException(
                    "Las métricas temporales no pueden ser negativas."
            );
        }

        if (this.motivoParada == null) {
            throw new ResultadoSimulacionInvalidoException(
                    "El motivo de parada no puede ser nulo."
            );
        }

        if (this.coberturaServiciosPonderada < 0.0 || this.coberturaServiciosPonderada > 1.0
                || this.eficienciaTransporte < 0.0 || this.eficienciaTransporte > 1.0) {
            throw new ResultadoSimulacionInvalidoException(
                    "Las métricas espaciales deben estar entre 0.0 y 1.0."
            );
        }
    }

    private double ratioPorTipo(TipoBloque tipo) {
        if (this.totalBloques <= 0) {
            return 0.0;
        }
        return (double) this.conteoPorTipo.getOrDefault(tipo, 0) / this.totalBloques;
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

    private double calcularIndiceSaturacion() {
        double base = this.densidad;
        if (hayDeficitEnergetico()) base += 0.05;
        if (hayDeficitServicios()) base += 0.05;
        if (tieneContaminacionAlta()) base += 0.05;
        return clamp01(base);
    }

    private double calcularIndiceEquilibrioBase() {
        double score = 0.0;
        score += this.porcentajeActivos * 0.40;
        score += this.diversidadTipos * 0.20;
        score += Math.min(1.0, this.ratioEnergia) * 0.10;
        score += Math.min(1.0, this.ratioServicios) * 0.10;
        score += this.ratioTransporte * 0.10;

        // Presión industrial absoluta: penalización simple si es alta
        if (this.presionIndustrial <= 10.0) {
            score += 0.05;
        }
        if (this.densidadOcupacion <= 0.80) {
            score += 0.05;
        }
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

    public boolean estaEnTendenciaNegativa() {
        return this.tendenciaEstabilidad < 0.0;
    }

    public boolean estaEnTendenciaPositiva() {
        return this.tendenciaEstabilidad > 0.0;
    }

    public boolean tieneContaminacionCreciente() {
        return this.tendenciaContaminacion > 0.0;
    }

    public boolean colapsoDetectado() {
        return this.motivoParada == MotivoParadaSimulacion.COLAPSO_ENERGETICO;
    }

    public boolean saturacionDetectada() {
        return this.motivoParada == MotivoParadaSimulacion.SATURACION_CRITICA;
    }

    public boolean isNecesidadExpansionDetectada() {
        return necesidadExpansionDetectada;
    }

    public String resumenMetrico() {
        return "Total=" + totalBloques
                + ", activos=" + bloquesActivos
                + ", %act=" + String.format(java.util.Locale.ROOT, "%.2f", porcentajeActivos)
                + ", densidad=" + String.format(java.util.Locale.ROOT, "%.2f", densidad)
                + ", ratioE=" + String.format(java.util.Locale.ROOT, "%.2f", ratioEnergetico)
                + ", ratioS=" + String.format(java.util.Locale.ROOT, "%.2f", ratioCoberturaServicios)
                + ", contaminacion=" + contaminacion
                + ", estabilidad=" + String.format(java.util.Locale.ROOT, "%.2f", estabilidadBasica)
                + ", tipo=" + tipoEstructural;
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

    public double getPesoResidencial() {
        return pesoResidencial;
    }

    public double getIndiceEquilibrioBase() {
        return indiceEquilibrioBase;
    }

    public double getDensidad() {
        return densidad;
    }

    public TipoEstructuralCiudad getTipoEstructural() {
        return tipoEstructural;
    }

    public int getEnergiaProducida() {
        return energiaProducida;
    }

    public int getConsumoEnergetico() {
        return consumoEnergetico;
    }

    public int getEquilibrioEnergetico() {
        return equilibrioEnergetico;
    }

    public int getDemandaServicios() {
        return demandaServicios;
    }

    public int getCoberturaServicios() {
        return coberturaServicios;
    }

    public int getPresionIndustrial() {
        return presionIndustrial;
    }

    public int getSoporteTransporte() {
        return soporteTransporte;
    }

    public int getContaminacion() {
        return contaminacion;
    }

    public double getBienestar() {
        return bienestar;
    }

    public double getEstabilidadBasica() {
        return estabilidadBasica;
    }

    public double getRatioCoberturaServicios() {
        return ratioCoberturaServicios;
    }

    public double getRatioEnergetico() {
        return ratioEnergetico;
    }

    public double getIndiceSaturacion() {
        return indiceSaturacion;
    }

    public double getIndiceViabilidadBase() {
        return indiceViabilidadBase;
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

    public double getCoberturaServiciosPonderada() {
        return coberturaServiciosPonderada;
    }

    public double getEficienciaTransporte() {
        return eficienciaTransporte;
    }

    public double getEstabilidadMedia() {
        return estabilidadMedia;
    }

    @Override
    public String toString() {
        return "MetricaCiudad{" + resumenMetrico() + "}";
    }
}