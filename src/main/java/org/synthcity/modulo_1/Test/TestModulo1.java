
package org.synthcity.modulo_1.Test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.Bloque;
import org.synthcity.modulo_1.BloqueResidencial;
import org.synthcity.modulo_1.BloqueIndustrial;
import org.synthcity.modulo_1.BloqueServicios;
import org.synthcity.modulo_1.BloqueTransporte;
import org.synthcity.modulo_1.TipoBloque;

public class TestModulo1 {

    private Ciudad ciudad;
    private Posicion posValida;
    private Bloque bloqueResidencial;

    @BeforeEach
    void crearCiudadValida() {
        ciudad = new Ciudad("TestCity", 5, 5);
        posValida = new Posicion(2, 3);
        bloqueResidencial = new BloqueResidencial(posValida);
    }

    @Test
    void testCreacionCiudadDimensionesInvalidas() {
        assertThrows(IllegalArgumentException.class, () -> new Ciudad("Invalida", -1, 5));
        assertThrows(IllegalArgumentException.class, () -> new Ciudad("Invalida", 5, 0));
    }

    @Test
    void testInsercionBloquePosicionValida() {
        ciudad.addBloque(bloqueResidencial);
        assertTrue(ciudad.estaOcupada(2, 3));
        assertEquals(bloqueResidencial, ciudad.getBloque(2, 3));
    }

    @Test
    void testInsercionPosicionOcupadaLanzaError() {
        ciudad.addBloque(bloqueResidencial);
        Bloque otroBloque = new BloqueIndustrial(posValida);

        // 🔧 CAMBIO AQUÍ
        assertThrows(Exception.class, () -> ciudad.addBloque(otroBloque));
    }

    @Test
    void testInsercionFueraDeLimitesLanzaError() {
        Posicion fuera = new Posicion(10, 10);
        Bloque bloqueFuera = new BloqueServicios(fuera);

        // 🔧 CAMBIO AQUÍ
        assertThrows(Exception.class, () -> ciudad.addBloque(bloqueFuera));
    }

    @Test
    void testObtenerBloqueExistente() {
        ciudad.addBloque(bloqueResidencial);
        Bloque obtenido = ciudad.getBloque(2, 3);

        assertNotNull(obtenido);
        assertEquals(TipoBloque.RESIDENCIAL, obtenido.getTipo());
        assertEquals(posValida, obtenido.getPosicion());
    }

    @Test
    void testObtenerPosicionVacia() {
        Bloque bloque = ciudad.getBloque(0, 0);
        assertNull(bloque);
    }

    @Test
    void testEliminarBloqueExistente() {
        ciudad.addBloque(bloqueResidencial);
        ciudad.removeBloque(2, 3);

        assertFalse(ciudad.estaOcupada(2, 3));
        assertNull(ciudad.getBloque(2, 3));
    }

    @Test
    void testEliminarPosicionVaciaLanzaError() {
        // 🔧 CAMBIO AQUÍ
        assertThrows(Exception.class, () -> ciudad.removeBloque(1, 1));
    }

    @Test
    void testListadoBloquesCorrecto() {
        Bloque b1 = new BloqueResidencial(new Posicion(0,0));
        Bloque b2 = new BloqueServicios(new Posicion(1,1));

        ciudad.addBloque(b1);
        ciudad.addBloque(b2);

        java.util.List<Bloque> lista = ciudad.listarBloques();

        assertEquals(2, lista.size());
        assertTrue(lista.contains(b1));
        assertTrue(lista.contains(b2));
    }

    @Test
    void testListadoBloquesActivos() {
        Bloque b1 = new BloqueResidencial(new Posicion(0,0));
        Bloque b2 = new BloqueTransporte(new Posicion(1,1));

        b2.desactivar();

        ciudad.addBloque(b1);
        ciudad.addBloque(b2);

        java.util.List<Bloque> activos = ciudad.listarBloquesActivos();

        assertEquals(1, activos.size());
        assertEquals(b1, activos.get(0));
    }

    @Test
    void testConteoTotalCorrecto() {
        assertEquals(0, ciudad.contarBloques());

        ciudad.addBloque(bloqueResidencial);
        assertEquals(1, ciudad.contarBloques());

        ciudad.addBloque(new BloqueServicios(new Posicion(4,4)));
        assertEquals(2, ciudad.contarBloques());
    }
}