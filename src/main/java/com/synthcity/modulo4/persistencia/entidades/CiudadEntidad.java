package com.synthcity.modulo4.persistencia.entidades;

public class CiudadEntidad {
    private Long id;
    private String nombre;
    private int filas;
    private int columnas;

    // Constructor vacío requerido para la reconstrucción en los mapeos del ResultSet
    public CiudadEntidad() {}

    // Constructor completo para inserciones y actualizaciones
    public CiudadEntidad(Long id, String nombre, int filas, int columnas) {
        this.id = id;
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
    }

    // Getters y Setters estrictos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getFilas() { return filas; }
    public void setFilas(int filas) { this.filas = filas; }

    public int getColumnas() { return columnas; }
    public void setColumnas(int columnas) { this.columnas = columnas; }
}