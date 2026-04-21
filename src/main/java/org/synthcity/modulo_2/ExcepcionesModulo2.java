package org.synthcity.modulo_2;

public class ExcepcionesModulo2 {
}

class CiudadInvalidaParaSimulacionException extends RuntimeException {
    public CiudadInvalidaParaSimulacionException(String mensaje) {
        super(mensaje);
    }
}

class ResultadoSimulacionInvalidoException extends RuntimeException {
    public ResultadoSimulacionInvalidoException(String mensaje) {
        super(mensaje);
    }
}

