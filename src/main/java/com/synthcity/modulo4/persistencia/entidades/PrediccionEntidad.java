package com.synthcity.modulo4.persistencia.entidades;

public class PrediccionEntidad {
    private Long id;
    private Long evaluacionId; // Clave foránea obligatoria hacia EvaluacionEntidad
    private String tipo;       // 'HEURISTICA' o 'ML'
    private String tendencia;  // 'MEJORA', 'ESTABLE', 'DETERIORO'
    private double score;
    private String mensaje;
    private Long modeloId;     // Clave foránea opcional hacia ModeloWekaEntidad (puede ser null)

    public PrediccionEntidad() {}

    public PrediccionEntidad(Long id, Long evaluacionId, String tipo, String tendencia, double score, String mensaje, Long modeloId) {
        this.id = id;
        this.evaluacionId = evaluacionId;
        this.tipo = tipo;
        this.tendencia = tendencia;
        this.score = score;
        this.mensaje = mensaje;
        this.modeloId = modeloId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEvaluacionId() { return evaluacionId; }
    public void setEvaluacionId(Long evaluacionId) { this.evaluacionId = evaluacionId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTendencia() { return tendencia; }
    public void setTendencia(String tendencia) { this.tendencia = tendencia; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Long getModeloId() { return modeloId; }
    public void setModeloId(Long modeloId) { this.modeloId = modeloId; }
}