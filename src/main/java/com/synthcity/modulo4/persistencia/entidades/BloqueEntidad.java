package com.synthcity.modulo4.persistencia.entidades;

public class BloqueEntidad {
    private Long id;
    private Long ciudadId; // Clave foránea hacia CiudadEntidad
    private String tipo;
    private int x;
    private int y;
    private boolean activo;

    public BloqueEntidad() {}

    public BloqueEntidad(Long id, Long ciudadId, String tipo, int x, int y, boolean activo) {
        this.id = id;
        this.ciudadId = ciudadId;
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCiudadId() { return ciudadId; }
    public void setCiudadId(Long ciudadId) { this.ciudadId = ciudadId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
