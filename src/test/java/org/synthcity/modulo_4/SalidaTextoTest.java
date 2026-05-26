package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalidaTextoTest {

    @Test
    void contenidoNulo_lanzaFormatoSalidaException() {
        assertThrows(FormatoSalidaException.class, () -> new SalidaTexto(null));
    }

    @Test
    void contenidoVacio_lanzaFormatoSalidaException() {
        assertThrows(FormatoSalidaException.class, () -> new SalidaTexto("   "));
    }

    @Test
    void getContenido_devuelveContenidoOriginal() {
        SalidaTexto salida = new SalidaTexto("Informe generado");

        assertEquals("Informe generado", salida.getContenido());
    }

    @Test
    void toString_devuelveExactamenteElContenido() {
        SalidaTexto salida = new SalidaTexto("Texto final");

        assertEquals("Texto final", salida.toString());
    }

    @Test
    void constructorCompleto_guardaMetadatos() {
        SalidaTexto salida = new SalidaTexto(
                "Resumen",
                "Contenido del resumen",
                "NeoMadrid",
                "2026-04-20 10:00:00"
        );

        assertEquals("Resumen", salida.getTitulo());
        assertEquals("Contenido del resumen", salida.getContenido());
        assertEquals("NeoMadrid", salida.getNombreCiudad());
        assertEquals("2026-04-20 10:00:00", salida.getFechaGeneracion());
    }
}