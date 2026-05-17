package com.synthcity.modulo4.persistencia.entidades;

public class EvaluacionEntidad {
    private Long id;
    private Long simulacionId; // Clave foránea: una evaluación pertenece a una simulación
    private String nivel;
    private double score;
    private String mensaje;

    public EvaluacionEntidad() {}

    public EvaluacionEntidad(Long id, Long simulacionId, String nivel, double score, String mensaje) {
        this.id = id;
        this.simulacionId = simulacionId;
        this.nivel = nivel;
        this.score = score;
        this.mensaje = mensaje;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSimulacionId() { return simulacionId; }
    public void setSimulacionId(Long simulacionId) { this.simulacionId = simulacionId; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
