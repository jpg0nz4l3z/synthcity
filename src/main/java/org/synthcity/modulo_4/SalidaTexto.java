package org.synthcity.modulo_4;

public final class SalidaTexto {

    private final String contenido;

    public SalidaTexto(String contenido) {
        if (contenido == null) {
            throw new FormatoSalidaException("El contenido no puede ser null.");
        }
        if (contenido.isBlank()) {
            throw new FormatoSalidaException("El contenido no puede estar vacio.");
        }
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    @Override
    public String toString() {
        return contenido;
    }
}
