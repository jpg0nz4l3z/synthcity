package org.synthcity.modulo_4.persistencia.entidades;

import java.sql.Timestamp;

public class SimulacionEntidad {
    private Long id;
    private Long ciudadId; // Clave foránea: una simulación pertenece a una ciudad
    private int ciclos;
    private Timestamp fecha;

    public SimulacionEntidad() {}

    public SimulacionEntidad(Long id, Long ciudadId, int ciclos, Timestamp fecha) {
        this.id = id;
        this.ciudadId = ciudadId;
        this.ciclos = ciclos;
        this.fecha = fecha;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCiudadId() { return ciudadId; }
    public void setCiudadId(Long ciudadId) { this.ciudadId = ciudadId; }

    public int getCiclos() { return ciclos; }
    public void setCiclos(int ciclos) { this.ciclos = ciclos; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
}