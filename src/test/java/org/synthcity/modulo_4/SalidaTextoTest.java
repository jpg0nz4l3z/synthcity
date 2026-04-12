package org.synthcity.modulo_4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalidaTextoTest {

    @Test
    void constructor_deberia_guardar_contenido_correctamente() {
        SalidaTexto salida = new SalidaTexto("Hola mundo");

        assertEquals("Hola mundo", salida.getContenido());
    }

    @Test
    void toString_deberia_devolver_el_contenido() {
        SalidaTexto salida = new SalidaTexto("Informe");

        assertEquals("Informe", salida.toString());
    }

    @Test
    void constructor_deberia_lanzar_excepcion_si_contenido_es_null() {
        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> new SalidaTexto(null)
        );

        assertEquals("El contenido no puede ser null.", ex.getMessage());
    }

    @Test
    void constructor_deberia_lanzar_excepcion_si_contenido_esta_vacio() {
        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> new SalidaTexto("   ")
        );

        assertEquals("El contenido no puede estar vacio.", ex.getMessage());
    }
}
