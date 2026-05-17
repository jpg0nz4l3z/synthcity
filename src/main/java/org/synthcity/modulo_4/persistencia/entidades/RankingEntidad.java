package org.synthcity.modulo_4.persistencia.entidades;

import java.sql.Timestamp;

public class RankingEntidad {
    private Long id;
    private String contenido; // Texto plano descriptivo del estado ordenado de las ciudades
    private Timestamp fecha;

    public RankingEntidad() {}

    public RankingEntidad(Long id, String contenido, Timestamp fecha) {
        this.id = id;
        this.contenido = contenido;
        this.fecha = fecha;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
}