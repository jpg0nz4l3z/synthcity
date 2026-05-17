package com.synthcity.modulo4.persistencia.entidades;

public class CicloEntidad {
    private Long id;
    private Long simulacionId; // Clave foránea hacia SimulacionEntidad
    private int numero;
    private String estado; // Volcado estructurado del estado de las variables en ese instante

    public CicloEntidad() {}

    public CicloEntidad(Long id, Long simulacionId, int numero, String estado) {
        this.id = id;
        this.simulacionId = simulacionId;
        this.numero = numero;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSimulacionId() { return simulacionId; }
    public void setSimulacionId(Long simulacionId) { this.simulacionId = simulacionId; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}