package org.synthcity.modulo_1;

public class ResumenEstructuralCiudad {
    private final String nombreCiudad;
    private final int filas;
    private final int columnas;
    private final int capacidadMaxima;
    private final int ocupacionActual;
    private final double densidad;
    private final TipoEstructuralCiudad tipoEstructural;
    private final int bloquesActivos;
    private final int bloquesInactivos;

    public ResumenEstructuralCiudad (String nombreCiudad, int filas, int columnas, int capacidadMaxima, int ocupacionActual,
                                     double densidad, TipoEstructuralCiudad tipoEstructural, int bloquesActivos, int bloquesInactivos){
        this.nombreCiudad = nombreCiudad;
        this.filas = filas;
        this.columnas = columnas;
        this.capacidadMaxima = capacidadMaxima;
        this.ocupacionActual = ocupacionActual;
        this.densidad = densidad;
        this.tipoEstructural = tipoEstructural;
        this.bloquesActivos = bloquesActivos;
        this.bloquesInactivos = bloquesInactivos;
    }
    public String getNombreCiudad() {
        return nombreCiudad;
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
    public int getOcupacionActual() {
        return ocupacionActual;
    }
    public double getDensidad() {
        return densidad;
    }

    public TipoEstructuralCiudad getTipoEstructural() {
        return tipoEstructural;
    }
    public int getBloquesActivos() {
        return bloquesActivos;
    }
    public int getBloquesInactivos() {
        return bloquesInactivos;
    }
    @Override
    public String toString(){
        return "ResumenEstructuralCiudad{" +
                "nombre='" + nombreCiudad + '\'' +
                ", " + filas + "x" + columnas +
                ", capacidad=" + capacidadMaxima +
                ", ocupación=" + ocupacionActual +
                ", densidad=" + String.format("%.2f", densidad) +
                ", tipo=" + tipoEstructural +
                ", activos=" + bloquesActivos +
                ", inactivos=" + bloquesInactivos +
                '}';
    }
}
