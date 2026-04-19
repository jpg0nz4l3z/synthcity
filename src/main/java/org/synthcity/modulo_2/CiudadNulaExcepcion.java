package org.synthcity.modulo_2;

public class CiudadNulaExcepcion extends RuntimeException {

    public CiudadNulaExcepcion() {
        super("La ciudad no puede ser nula.");
    }

    public CiudadNulaExcepcion(String mensaje) {
        super(mensaje);
    }
}
