package org.synthcity.modulo_2;

/**
 * Excepción lanzada cuando la ciudad de entrada es nula o estructuralmente inválida.
 */
class CiudadInvalidaParaSimulacionException extends RuntimeException {
    public CiudadInvalidaParaSimulacionException(String mensaje) {
        super(mensaje);
    }
}