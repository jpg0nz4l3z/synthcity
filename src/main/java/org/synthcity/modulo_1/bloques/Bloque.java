package org.synthcity.modulo_1.bloques;


import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.TipoBloque;

public abstract class Bloque {

    private TipoBloque tipoBloque;
    private Posicion posicion;
    private boolean activo;


    public Bloque(TipoBloque tipoBloque, Posicion posicion, boolean activo) {
        this.tipoBloque = tipoBloque;
        this.posicion = posicion;
        this.activo = activo;
    }

    public static Bloque crearBloque(TipoBloque tipo, Posicion posicion) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de bloque no puede ser nulo.");
        }

        if (posicion == null) {
            throw new IllegalArgumentException("La posicion del bloque no puede ser nula.");
        }

        switch (tipo) {
            case RESIDENCIAL:
                return new BloqueResidencial(posicion);
            case ENERGIA:
                return new BloqueEnergia(posicion);
            case INDUSTRIAL:
                return new BloqueIndustrial(posicion);
            case SERVICIOS:
                return new BloqueServicios(posicion);
            case TRANSPORTE:
                return new BloqueTransporte(posicion);
            default:
                throw new IllegalArgumentException("Tipo de bloque no soportado: " + tipo);
        }
    }

    public static Bloque crearBloque(TipoBloque tipo, Posicion posicion, boolean activo) {
        Bloque bloque = crearBloque(tipo, posicion);
        bloque.cambiarEstado(activo);
        return bloque;
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

    public void cambiarEstado(boolean estado) {
        this.activo = estado;
    }



    @Override
    public String toString() {
        return "Bloque [" +
                "tipo=" + tipoBloque +
                ", posicion=" + posicion +
                ", estado=" + (activo ? "activo" : "inactivo") +
                "]";
    }

    // METODOS ABSTRACTOS
    public abstract int getRadioInfluencia();

    public abstract boolean esGeneradorDemanda();


    public abstract int getProduccionEnergia();


    public abstract int getConsumoEnergetico();


    public abstract int getDemandaServicios();


    public abstract int getCoberturaServicios();


    public abstract int getPresionIndustrial();


    public abstract int getSoporteTransporte();


    public abstract int getContaminacion();

}
