package org.synthcity.modulo_3;

/**
 * Excepción propia del Módulo 3.
 * Se lanza cuando el Módulo 2 envía un ResultadoSimulacion nulo o con datos matemáticamente incoherentes.
 */
public class ResultadoSimulacionInvalidoException extends RuntimeException {
    public ResultadoSimulacionInvalidoException(String mensaje) {
        super(mensaje);
    }
}