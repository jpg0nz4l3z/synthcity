package org.synthcity.modulo_4;


public class SalidaTexto {
    private String titulo;
    private String contenido;
    private String nombreCiudad;
    private String fechaGeneracion;

    public SalidaTexto(String contenido) {
        this("Salida SynthCity", contenido, null, null);
    }

    public SalidaTexto(String titulo, String contenido, String nombreCiudad, String fechaGeneracion) {
        this.titulo = titulo;
        if (contenido == null) {
            throw new FormatoSalidaException("El contenido no puede ser null.");
        }
        if (contenido.isBlank()) {
            throw new FormatoSalidaException("El contenido no puede estar vacio.");
        }
        this.contenido = contenido;
        this.nombreCiudad = nombreCiudad;
        this.fechaGeneracion = fechaGeneracion;
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public String getFechaGeneracion() {
        return fechaGeneracion;
    }

    @Override
    public String toString() {
        return contenido;
    }
}