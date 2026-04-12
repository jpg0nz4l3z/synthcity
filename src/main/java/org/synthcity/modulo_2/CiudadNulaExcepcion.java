package org.synthcity.modulo_2;

public class CiudadNulaException extends RuntimeException {

    public CiudadNulaException() {
        super("La ciudad no puede ser nula.");
    }

    public CiudadNulaException(String mensaje) {
        super(mensaje);
    }
}
