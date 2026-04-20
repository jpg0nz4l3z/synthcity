package org.synthcity.modulo_2;

import java.util.Map;

import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_1.TipoEstructuralCiudad;

public class ResultadoSimulacion {

    private final String nombreCiudad;
    private final int filas;
    private final int columnas;
    private final int capacidadMaxima;
    private final int bloquesTotales;
    private final int bloquesActivos;
    private final int bloquesInactivos;
    private final Map<TipoBloque, Integer> conteoPorTipo;
    private final EstadoSimulacion estadoSimulacion;
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
    private final double ratioEnergetico;
    private final double ratioCoberturaServicios;

    public ResultadoSimulacion(
            String nombreCiudad,
            int filas,
            int columnas,
            int capacidadMaxima,
            int bloquesTotales,
            int bloquesActivos,
            int bloquesInactivos,
            Map<TipoBloque, Integer> conteoPorTipo,
            EstadoSimulacion estadoSimulacion) {
        this(
                nombreCiudad,
                filas,
                columnas,
                capacidadMaxima,
                bloquesTotales,
                bloquesActivos,
                bloquesInactivos,
                conteoPorTipo,
                estadoSimulacion,
                calcularDensidad(bloquesTotales, capacidadMaxima),
                inferirTipoEstructural(capacidadMaxima),
                calcularEnergia(conteoPorTipo),
                calcularConsumo(conteoPorTipo),
                calcularEnergia(conteoPorTipo) - calcularConsumo(conteoPorTipo),
                calcularDemanda(conteoPorTipo),
                calcularCobertura(conteoPorTipo),
                calcularPresion(conteoPorTipo),
                calcularTransporte(conteoPorTipo),
                calcularContaminacion(conteoPorTipo, calcularDensidad(bloquesTotales, capacidadMaxima)),
                calcularBienestarDerivado(conteoPorTipo),
                calcularEstabilidadDerivada(conteoPorTipo, bloquesTotales, capacidadMaxima),
                calcularRatio(calcularEnergia(conteoPorTipo), calcularConsumo(conteoPorTipo)),
                calcularRatio(calcularCobertura(conteoPorTipo), calcularDemanda(conteoPorTipo))
        );
    }

    public ResultadoSimulacion(
            String nombreCiudad,
            int filas,
            int columnas,
            int capacidadMaxima,
            int bloquesTotales,
            int bloquesActivos,
            int bloquesInactivos,
            Map<TipoBloque, Integer> conteoPorTipo,
            EstadoSimulacion estadoSimulacion,
            double densidad,
            TipoEstructuralCiudad tipoEstructural,
            int energiaProducida,
            int consumoEnergetico,
            int equilibrioEnergetico,
            int demandaServicios,
            int coberturaServicios,
            int presionIndustrial,
            int soporteTransporte,
            int contaminacion,
            double bienestar,
            double estabilidadBasica,
            double ratioEnergetico,
            double ratioCoberturaServicios) {

        this.nombreCiudad = nombreCiudad;
        this.filas = filas;
        this.columnas = columnas;
        this.capacidadMaxima = capacidadMaxima;
        this.bloquesTotales = bloquesTotales;
        this.bloquesActivos = bloquesActivos;
        this.bloquesInactivos = bloquesInactivos;
        this.conteoPorTipo = conteoPorTipo == null ? null : Map.copyOf(conteoPorTipo);
        this.estadoSimulacion = estadoSimulacion;
        this.densidad = densidad;
        this.tipoEstructural = tipoEstructural;
        this.energiaProducida = energiaProducida;
        this.consumoEnergetico = consumoEnergetico;
        this.equilibrioEnergetico = equilibrioEnergetico;
        this.demandaServicios = demandaServicios;
        this.coberturaServicios = coberturaServicios;
        this.presionIndustrial = presionIndustrial;
        this.soporteTransporte = soporteTransporte;
        this.contaminacion = contaminacion;
        this.bienestar = bienestar;
        this.estabilidadBasica = estabilidadBasica;
        this.ratioEnergetico = ratioEnergetico;
        this.ratioCoberturaServicios = ratioCoberturaServicios;

        validarRatios();
        validarEquilibrio();
    }

    private void validarRatios() {
        if (!Double.isFinite(ratioEnergetico) || !Double.isFinite(ratioCoberturaServicios)) {
            throw new ResultadoSimulacionInvalidoException("Ratios no validos detectados.");
        }
    }

    private void validarEquilibrio() {
        if (equilibrioEnergetico != energiaProducida - consumoEnergetico) {
            throw new ResultadoSimulacionInvalidoException(
                    "Inconsistencia energetica: el equilibrio no corresponde a produccion - consumo.");
        }
    }

    private static int cantidad(Map<TipoBloque, Integer> conteo, TipoBloque tipo) {
        if (conteo == null) {
            return 0;
        }
        return Math.max(0, conteo.getOrDefault(tipo, 0));
    }

    private static double calcularDensidad(int bloquesTotales, int capacidadMaxima) {
        if (capacidadMaxima <= 0) {
            return 0.0;
        }
        return (double) bloquesTotales / capacidadMaxima;
    }

    private static TipoEstructuralCiudad inferirTipoEstructural(int capacidadMaxima) {
        if (capacidadMaxima <= 400) {
            return TipoEstructuralCiudad.PEQUENA;
        }
        if (capacidadMaxima <= 1600) {
            return TipoEstructuralCiudad.MEDIANA;
        }
        return TipoEstructuralCiudad.GRANDE;
    }

    private static int calcularEnergia(Map<TipoBloque, Integer> conteo) {
        return cantidad(conteo, TipoBloque.ENERGIA) * ReglasSimulacion.ENERGIA_POR_BLOQUE_ENERGIA;
    }

    private static int calcularConsumo(Map<TipoBloque, Integer> conteo) {
        return cantidad(conteo, TipoBloque.RESIDENCIAL) * ReglasSimulacion.CONSUMO_RESIDENCIAL
                + cantidad(conteo, TipoBloque.ENERGIA) * ReglasSimulacion.CONSUMO_ENERGIA
                + cantidad(conteo, TipoBloque.INDUSTRIAL) * ReglasSimulacion.CONSUMO_INDUSTRIAL
                + cantidad(conteo, TipoBloque.SERVICIOS) * ReglasSimulacion.CONSUMO_SERVICIOS
                + cantidad(conteo, TipoBloque.TRANSPORTE) * ReglasSimulacion.CONSUMO_TRANSPORTE;
    }

    private static int calcularDemanda(Map<TipoBloque, Integer> conteo) {
        return cantidad(conteo, TipoBloque.RESIDENCIAL) * ReglasSimulacion.DEMANDA_POR_RESIDENCIAL;
    }

    private static int calcularCobertura(Map<TipoBloque, Integer> conteo) {
        return cantidad(conteo, TipoBloque.SERVICIOS) * ReglasSimulacion.COBERTURA_POR_SERVICIO;
    }

    private static int calcularPresion(Map<TipoBloque, Integer> conteo) {
        return cantidad(conteo, TipoBloque.INDUSTRIAL) * ReglasSimulacion.PRESION_POR_INDUSTRIAL;
    }

    private static int calcularTransporte(Map<TipoBloque, Integer> conteo) {
        return cantidad(conteo, TipoBloque.TRANSPORTE) * ReglasSimulacion.TRANSPORTE_SOPORTE;
    }

    private static int calcularContaminacion(Map<TipoBloque, Integer> conteo, double densidad) {
        int contaminacion = cantidad(conteo, TipoBloque.INDUSTRIAL)
                * ReglasSimulacion.CONTAMINACION_POR_INDUSTRIAL;
        if (densidad >= ReglasSimulacion.PENALIZACION_DENSIDAD_ALTA) {
            contaminacion += ReglasSimulacion.EXTRA_CONTAMINACION_DENSIDAD;
        }
        return contaminacion;
    }

    private static double calcularBienestarDerivado(Map<TipoBloque, Integer> conteo) {
        int energia = calcularEnergia(conteo);
        int consumo = calcularConsumo(conteo);
        int demanda = calcularDemanda(conteo);
        int cobertura = calcularCobertura(conteo);
        int transporte = calcularTransporte(conteo);
        int contaminacion = calcularContaminacion(conteo, 0.0);

        double energiaOk = consumo <= 0 ? 1.0 : Math.min(1.0, (double) energia / consumo);
        double serviciosOk = demanda <= 0 ? 1.0 : Math.min(1.0, (double) cobertura / demanda);
        double transporteOk = Math.min(1.0, transporte / 10.0);
        double contaminacionOk = 1.0 - Math.min(1.0, contaminacion / 100.0);

        return clamp01(serviciosOk * 0.35 + energiaOk * 0.30 + transporteOk * 0.15 + contaminacionOk * 0.20);
    }

    private static double calcularEstabilidadDerivada(Map<TipoBloque, Integer> conteo, int total, int capacidad) {
        double energiaOk = Math.min(1.0, calcularRatio(calcularEnergia(conteo), calcularConsumo(conteo)));
        double serviciosOk = Math.min(1.0, calcularRatio(calcularCobertura(conteo), calcularDemanda(conteo)));
        double densidadOk = 1.0 - Math.min(1.0, Math.max(0.0, calcularDensidad(total, capacidad)));
        return clamp01(energiaOk * 0.40 + serviciosOk * 0.35 + densidadOk * 0.25);
    }

    private static double calcularRatio(int numerador, int denominador) {
        if (denominador == 0) {
            return numerador > 0 ? numerador : 1.0;
        }
        return (double) numerador / denominador;
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

    public String getNombreCiudad() { return nombreCiudad; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public int getBloquesTotales() { return bloquesTotales; }
    public int getBloquesActivos() { return bloquesActivos; }
    public int getBloquesInactivos() { return bloquesInactivos; }
    public Map<TipoBloque, Integer> getConteoPorTipo() { return conteoPorTipo; }
    public EstadoSimulacion getEstadoSimulacion() { return estadoSimulacion; }
    public double getDensidad() { return densidad; }
    public TipoEstructuralCiudad getTipoEstructural() { return tipoEstructural; }
    public int getEnergiaProducida() { return energiaProducida; }
    public int getConsumoEnergetico() { return consumoEnergetico; }
    public int getEquilibrioEnergetico() { return equilibrioEnergetico; }
    public int getDemandaServicios() { return demandaServicios; }
    public int getCoberturaServicios() { return coberturaServicios; }
    public int getPresionIndustrial() { return presionIndustrial; }
    public int getSoporteTransporte() { return soporteTransporte; }
    public int getContaminacion() { return contaminacion; }
    public double getBienestar() { return bienestar; }
    public double getEstabilidadBasica() { return estabilidadBasica; }
    public double getRatioEnergetico() { return ratioEnergetico; }
    public double getRatioCoberturaServicios() { return ratioCoberturaServicios; }

    public int getCantidadPorTipo(TipoBloque tipo) {
        if (tipo == null || conteoPorTipo == null) {
            return 0;
        }
        return conteoPorTipo.getOrDefault(tipo, 0);
    }

    public boolean ciudadEstaVacia() {
        return bloquesTotales == 0;
    }

    public boolean hayBloquesActivos() {
        return bloquesActivos > 0;
    }

    public boolean hayDeficitEnergetico() {
        return equilibrioEnergetico < 0;
    }

    public boolean hayDeficitServicios() {
        return ratioCoberturaServicios < 1.0;
    }

    public String getResumenSimulacion() {
        return String.format(
                java.util.Locale.ROOT,
                "Resumen de [%s]: Estado=%s | Bienestar=%.2f | Estabilidad=%.2f | BalanceEnergia=%d",
                nombreCiudad,
                estadoSimulacion,
                bienestar,
                estabilidadBasica,
                equilibrioEnergetico
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESULTADO DE SIMULACION ===\n");
        sb.append("Ciudad: ").append(nombreCiudad).append("\n");
        sb.append("Tipo estructural: ").append(tipoEstructural).append("\n");
        sb.append("Dimensiones: ").append(filas).append("x").append(columnas).append("\n");
        sb.append("Capacidad maxima: ").append(capacidadMaxima).append("\n");
        sb.append("Densidad: ").append(densidad).append("\n");
        sb.append("Bloques totales: ").append(bloquesTotales).append("\n");
        sb.append("Bloques activos: ").append(bloquesActivos).append("\n");
        sb.append("Bloques inactivos: ").append(bloquesInactivos).append("\n");
        sb.append("Energia producida/consumida: ").append(energiaProducida).append(" / ").append(consumoEnergetico).append("\n");
        sb.append("Equilibrio energetico: ").append(equilibrioEnergetico).append("\n");
        sb.append("Servicios demanda/cobertura: ").append(demandaServicios).append(" / ").append(coberturaServicios).append("\n");
        sb.append("Presion industrial: ").append(presionIndustrial).append("\n");
        sb.append("Soporte transporte: ").append(soporteTransporte).append("\n");
        sb.append("Contaminacion: ").append(contaminacion).append("\n");
        sb.append("Bienestar: ").append(bienestar).append("\n");
        sb.append("Estabilidad: ").append(estabilidadBasica).append("\n");
        sb.append("Ratio energetico: ").append(ratioEnergetico).append("\n");
        sb.append("Ratio servicios: ").append(ratioCoberturaServicios).append("\n");
        sb.append("Estado: ").append(estadoSimulacion).append("\n");
        return sb.toString();
    }
}
