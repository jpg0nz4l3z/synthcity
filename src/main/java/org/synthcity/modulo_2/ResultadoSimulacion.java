package org.synthcity.modulo_2;

import java.util.Map;
import org.synthcity.modulo_1.TipoBloque;

public class ResultadoSimulacion {

    private int bloquesTotales;
    private int bloquesActivos;
    private int bloquesInactivos;
    private String nombreCiudad;
    private Map<TipoBloque, Integer> conteoPorTipo;
    private EstadoSimulacion estadoSimulacion;

    public ResultadoSimulacion(
            String nombreCiudad,
            int filas,
            int columnas,
            int capacidadMaxima,
            int bloquesTotales,
            int bloquesActivos,
            int bloquesInactivos,
            Map<TipoBloque, Integer> conteoPorTipo,
            EstadoSimulacion estadoSimulacion
    ) {
        this.nombreCiudad = nombreCiudad;
        this.bloquesTotales = bloquesTotales;
        this.bloquesActivos = bloquesActivos;
        this.bloquesInactivos = bloquesInactivos;
        this.conteoPorTipo = conteoPorTipo;
        this.estadoSimulacion = estadoSimulacion;
    }

    public int getBloquesTotales() {
        return bloquesTotales;
    }

    public int getBloquesActivos() {
        return bloquesActivos;
    }

    public int getBloquesInactivos() {
        return bloquesInactivos;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public Map<TipoBloque, Integer> getConteoPorTipo() {
        return conteoPorTipo;
    }

    public EstadoSimulacion getEstadoSimulacion() {
        return estadoSimulacion;
    }

    public boolean ciudadEstaVacia() {
        return bloquesTotales == 0;
    }

    public boolean hayBloquesActivos() {
        return bloquesActivos > 0;
    }

    public int getCantidadPorTipo(TipoBloque tipo) {
        return conteoPorTipo.getOrDefault(tipo, 0);
    }
}