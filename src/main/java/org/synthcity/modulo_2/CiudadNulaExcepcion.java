package simulador.excepciones;

public class CiudadNulaException extends RuntimeException {

    public CiudadNulaException() {
        super("La ciudad no puede ser nula.");
    }

    public CiudadNulaException(String mensaje) {
        super(mensaje);
    }
}
