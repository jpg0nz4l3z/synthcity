import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class TestModulo1 {

    private Ciudad ciudad;
    private Posicion posValida;
    private Bloque bloqueResidencial;

    //1. Crear Ciudad válida
    @BeforeEach
    void crearCiudadValida() {
        ciudad = new Ciudad("TestCity", 5, 5);
        posValida = new Posicion(2, 3);
        bloqueResidencial = new BloqueResidencial(posValida);
    }
    // 2. Creación de ciudad con dimensiones inválidas → error
    @Test
    void testCreacionCiudadDimensionesInvalidas() {
        assertThrows(IllegalArgumentException.class, () -> new Ciudad("Invalida", -1, 5));
        assertThrows(IllegalArgumentException.class, () -> new Ciudad("Invalida", 5, 0));
    }

    // 3. Inserción de bloque en posición válida
    @Test
    void testInsercionBloquePosicionValida() {
        ciudad.addBloque(bloqueResidencial);
        assertTrue(ciudad.estaOcupada(2, 3));
        assertEquals(bloqueResidencial, ciudad.getBloque(2, 3));
    }

    // 4. Inserción en posición ocupada → error (CeldaOcupadaException)
    @Test
    void testInsercionPosicionOcupadaLanzaError() {
        ciudad.addBloque(bloqueResidencial);
        Bloque otroBloque = new BloqueIndustrial(posValida);
        assertThrows(CeldaOcupadaException.class, () -> ciudad.addBloque(otroBloque));
    }

    // 5. Inserción fuera de límites → error (PosicionFueraDeLimitesException)
    @Test
    void testInsercionFueraDeLimitesLanzaError() {
        Posicion fuera = new Posicion(10, 10);
        Bloque bloqueFuera = new BloqueServicios(fuera);
        assertThrows(PosicionFueraDeLimitesException.class, () -> ciudad.addBloque(bloqueFuera));
    }

    // 6. Obtención de bloque existente
    @Test
    void testObtenerBloqueExistente() {
        ciudad.addBloque(bloqueResidencial);
        Bloque obtenido = ciudad.getBloque(2, 3);
        assertNotNull(obtenido);
        assertEquals(TipoBloque.RESIDENCIAL, obtenido.getTipo());
        assertEquals(posValida, obtenido.getPosicion());
    }

    // 7. Obtención de posición vacía (debe devolver null)
    @Test
    void testObtenerPosicionVacia() {
        Bloque bloque = ciudad.getBloque(0, 0);
        assertNull(bloque);
    }

    // 8. Eliminación de bloque existente
    @Test
    void testEliminarBloqueExistente() {
        ciudad.addBloque(bloqueResidencial);
        assertTrue(ciudad.estaOcupada(2, 3));
        ciudad.removeBloque(2, 3);
        assertFalse(ciudad.estaOcupada(2, 3));
        assertNull(ciudad.getBloque(2, 3));
    }

    // 9. Eliminación de posición vacía → error (CeldaVaciaException)
    @Test
    void testEliminarPosicionVaciaLanzaError() {
        assertThrows(CeldaVaciaException.class, () -> ciudad.removeBloque(1, 1));
    }

    // 10. Listado de bloques correcto
    @Test
    void testListadoBloquesCorrecto() {
        Bloque b1 = new BloqueResidencial(new Posicion(0,0));
        Bloque b2 = new BloqueEnergia(new Posicion(1,1));
        ciudad.addBloque(b1);
        ciudad.addBloque(b2);
        Bloque b3 = new BloqueIndustrial(new Posicion(2,2));
        ciudad.addBloque(b3);
        ciudad.removeBloque(2,2);

        java.util.List<Bloque> lista = ciudad.listarBloques();
        assertEquals(2, lista.size());
        assertTrue(lista.contains(b1));
        assertTrue(lista.contains(b2));
        assertFalse(lista.contains(b3));
    }

    // 11. Listado de bloques activos
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

    // 12. Conteo total correcto
    @Test
    void testConteoTotalCorrecto() {
        assertEquals(0, ciudad.contarBloques());
        ciudad.addBloque(bloqueResidencial);
        assertEquals(1, ciudad.contarBloques());
        ciudad.addBloque(new BloqueEnergia(new Posicion(4,4)));
        assertEquals(2, ciudad.contarBloques());
        ciudad.removeBloque(2,3);
        assertEquals(1, ciudad.contarBloques());
    }
}