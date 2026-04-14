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

    private double densidad;
    private TipoEstructuralCiudad tipoEstructural;
    //Energía
    private int energiaProducida;
    private int consumoEnergetico;
    private int equilibrioEnergetico;
    //Servicios e Impacto
    private int demandaServicios;
    private int coberturaServicios;
    private int presionIndustrial;
    private int soporteTransporte;
    private int contaminacion;
    //Indices y Ratios
    private double bienestar;
    private double estabilidadBasica;
    private double ratioEnergetico;
    private double ratioCoberturaServicios;


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
            EstadoSimulacion estadoSimulacion) {

        this.nombreCiudad = nombreCiudad;
        this.filas = filas;
        this.columnas = columnas;
        this.capacidadMaxima = capacidadMaxima;
        this.bloquesTotales = bloquesTotales;
        this.bloquesActivos = bloquesActivos;
        this.bloquesInactivos = bloquesInactivos;
        this.conteoPorTipo = conteoPorTipo; // Java 10+; protege el mapa
        this.estadoSimulacion = estadoSimulacion;
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

    public boolean hayBloquesActivos() {

        return bloquesActivos > 0;
    }

    // toString para depuración
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESULTADO DE SIMULACION ===\n");
        sb.append("Ciudad:           ").append(nombreCiudad).append("\n");
        sb.append("Dimensiones:      ").append(filas).append("x").append(columnas).append("\n");
        sb.append("Capacidad maxima: ").append(capacidadMaxima).append("\n");
        sb.append("Bloques totales:  ").append(bloquesTotales).append("\n");
        sb.append("Bloques activos:  ").append(bloquesActivos).append("\n");
        sb.append("Bloques inactivos:").append(bloquesInactivos).append("\n");
        sb.append("Estado:           ").append(estadoSimulacion).append("\n");
        sb.append("Distribucion por tipo:\n");

        for (TipoBloque tipo : TipoBloque.values()) {
            sb.append("  ").append(tipo).append(": ").append(getCantidadPorTipo(tipo)).append("\n");
        }

        return sb.toString();
    }
}
