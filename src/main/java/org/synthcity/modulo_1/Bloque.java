package org.synthcity.modulo_1;

public abstract class Bloque {

    private TipoBloque tipoBloque;
    private Posicion posicion;
    private boolean activo;

    public Bloque(TipoBloque tipoBloque, Posicion posicion, boolean activo) {
        this.tipoBloque = tipoBloque;
        this.posicion = posicion;
        this.activo = activo;
    }

    public TipoBloque getTipo() {
        return tipoBloque;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public boolean estaActivo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }

    public void desactivar() {
        activo = false;
    }

    @Override
    public String toString() {
        return "Bloque [" +
                "tipo=" + tipoBloque +
                ", posicion=" + posicion +
                ", estado=" + (activo ? "activo" : "inactivo") +
                "]";
    }
}
