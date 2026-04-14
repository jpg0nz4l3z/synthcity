package org.synthcity.modulo_2;

/**
 * Indica que los datos calculados para el resultado no cumplen
 * con las reglas de coherencia del sistema (invariantes).
 */
public class ResultadoSimulacionInvalidoException extends RuntimeException {
    public ResultadoSimulacionInvalidoException(String mensaje) {
        super(mensaje);
    }
}
