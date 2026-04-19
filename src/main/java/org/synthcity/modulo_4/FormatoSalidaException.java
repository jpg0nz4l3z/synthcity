package org.synthcity.modulo_4;

public class FormatoSalidaException extends RuntimeException {

    public FormatoSalidaException(String mensaje) {
        super(mensaje);
    }

    public FormatoSalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
