package com.synthcity.modulo4.persistencia.entidades;

public class DatasetReferenciaEntidad {
    private Long id;
    private String rutaCsv;
    private int columnas;
    private int registros;
    private String objetivo;

    public DatasetReferenciaEntidad() {}

    public DatasetReferenciaEntidad(Long id, String rutaCsv, int columnas, int registros, String objetivo) {
        this.id = id;
        this.rutaCsv = rutaCsv;
        this.columnas = columnas;
        this.registros = registros;
        this.objetivo = objetivo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRutaCsv() { return rutaCsv; }
    public void setRutaCsv(String rutaCsv) { this.rutaCsv = rutaCsv; }

    public int getColumnas() { return columnas; }
    public void setColumnas(int columnas) { this.columnas = columnas; }

    public int getRegistros() { return registros; }
    public void setRegistros(int registros) { this.registros = registros; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
}
