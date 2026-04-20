package org.synthcity.modulo_2;

/**
 * Excepción lanzada cuando el ResultadoSimulacion viola las invariantes matemáticas del contrato.
 */
public class ResultadoSimulacionInvalidoException extends RuntimeException {
    public ResultadoSimulacionInvalidoException(String mensaje) {
        super(mensaje);
    }
}
