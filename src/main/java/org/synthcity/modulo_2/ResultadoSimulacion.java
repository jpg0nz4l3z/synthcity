package org.synthcity.modulo_2;

import java.util.Map;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.stubs.TipoEstructuralCiudad;

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
    //Energía
    private final int energiaProducida;
    private final int consumoEnergetico;
    private final int equilibrioEnergetico;
    //Servicios e Impacto
    private final int demandaServicios;
    private final int coberturaServicios;
    private final int presionIndustrial;
    private final int soporteTransporte;
    private final int contaminacion;
    //Indices y Ratios
    private final double bienestar;
    private final double estabilidadBasica;
    private final double ratioEnergetico;
    private final double ratioCoberturaServicios;


    // Constructor con todos los atributos inicializados
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
            int demandaServicios ,
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
        this.conteoPorTipo = conteoPorTipo; // Java 10+; protege el mapa
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
    }

    // GETTERS
    public String getNombreCiudad() { return nombreCiudad; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public int getBloquesTotales() { return bloquesTotales; }
    public int getBloquesActivos() { return bloquesActivos; }
    public int getBloquesInactivos() { return bloquesInactivos; }
    public Map<TipoBloque, Integer> getConteoPorTipo() { return conteoPorTipo; }
    public EstadoSimulacion getEstadoSimulacion() { return estadoSimulacion; }

    public double getDensidad() {return densidad;}
    public TipoEstructuralCiudad getTipoEstructural() {return tipoEstructural;}
    public int getEnergiaProducida() {return energiaProducida;}
    public int getConsumoEnergetico() {return consumoEnergetico;}
    public int getEquilibrioEnergetico() {return equilibrioEnergetico;}
    public int getDemandaServicios() {return demandaServicios;}
    public int getCoberturaServicios() {return coberturaServicios;}
    public int getPresionIndustrial() {return presionIndustrial;}
    public int getSoporteTransporte() {return soporteTransporte;}
    public int getContaminacion() {return contaminacion;}
    public double getBienestar() {return bienestar;}
    public double getEstabilidadBasica() {return estabilidadBasica;}
    public double getRatioEnergetico() {return ratioEnergetico;}
    public double getRatioCoberturaServicios() {return ratioCoberturaServicios;}

    // MÉTODOS FUNCIONALES
    public int getCantidadPorTipo(TipoBloque tipo) {
        if (tipo == null) {
            return 0;
        }
        return conteoPorTipo.getOrDefault(tipo, 0);
    }

    public boolean ciudadEstaVacia() {
        return bloquesTotales == 0;
    }

    public boolean hayBloquesActivos() {return bloquesActivos > 0;}

    public boolean hayDeficitEnergetico(){return equilibrioEnergetico < 0;}

    public boolean hayDeficitServicios(){return coberturaServicios < demandaServicios;}

    // toString para depuración
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESULTADO DE SIMULACIÓN ===\n");
        sb.append("Ciudad:           ").append(nombreCiudad).append("\n");
        sb.append("Dimensiones:      ").append(filas).append("x").append(columnas).append("\n");
        sb.append("Capacidad maxima: ").append(capacidadMaxima).append("\n");
        sb.append("Bloques totales:  ").append(bloquesTotales).append("\n");
        sb.append("Bloques activos:  ").append(bloquesActivos).append("\n");
        sb.append("Bloques inactivos:").append(bloquesInactivos).append("\n");
        sb.append("Estado:           ").append(estadoSimulacion).append("\n");
        sb.append("Distribución por tipo:\n");

        for (TipoBloque tipo : TipoBloque.values()) {
            sb.append("  ").append(tipo).append(": ").append(getCantidadPorTipo(tipo)).append("\n");
        }

        return sb.toString();
    }
}
