package org.synthcity.modulo_1;

public class ExcepcionesModulo1 {
}

class PosicionFueraDeLimitesException extends RuntimeException {
    public PosicionFueraDeLimitesException(String msg) {
        super(msg);
    }
}

class CeldaOcupadaException extends RuntimeException {
    public CeldaOcupadaException(String msg) {
        super(msg);
    }
}

class CeldaVaciaException extends RuntimeException {
    public CeldaVaciaException(String msg) {
        super(msg);
    }
}

class BloqueNuloException extends RuntimeException {
    public BloqueNuloException(String msg) {
        super(msg);
    }
}

class ExpansionCiudadException extends RuntimeException {
    public ExpansionCiudadException(String mensaje) {
        super(mensaje);
    }
}

class DimensionesInvalidasException extends RuntimeException {
    public DimensionesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
