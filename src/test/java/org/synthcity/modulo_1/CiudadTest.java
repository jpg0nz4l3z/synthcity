package org.synthcity.modulo_1;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.bloques.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CiudadTest {

    @Test
    void densidadCorrectaConCiudadVacia() {
        Ciudad ciudad = new Ciudad("CiudadA", 10, 10);

        assertEquals(0.0, ciudad.getDensidad(), 0.0001);
    }

    @Test
    void densidadCorrectaConVariosBloques() {
        Ciudad ciudad = new Ciudad("CiudadB", 5, 5); // capacidad = 25

        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueServicios(new Posicion(2, 2)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(3, 3)));

        assertEquals(4.0 / 25.0, ciudad.getDensidad(), 0.0001);
    }

    @Test
    void calculoDecimalCorrecto_noDivisionEntera() {
        Ciudad ciudad = new Ciudad("CiudadC", 2, 2); // capacidad = 4

        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));

        assertEquals(0.25, ciudad.getDensidad(), 0.0001);
        assertNotEquals(0.0, ciudad.getDensidad(), 0.0001);
    }

    @Test
    void estaProximaASaturacion_correctoEnElUmbral() {
        Ciudad ciudad = new Ciudad("CiudadD", 5, 5); // capacidad = 25
        // umbral = 0.80 -> 20 bloques

        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 3)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 4)));

        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 3)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 4)));

        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 3)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 4)));

        ciudad.addBloque(new BloqueResidencial(new Posicion(3, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(3, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(3, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(3, 3)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(3, 4)));

        assertEquals(20.0 / 25.0, ciudad.getDensidad(), 0.0001);
        assertTrue(ciudad.estaProximaASaturacion());
    }

    @Test
    void estaProximaASaturacion_correctoPorEncimaDelUmbral() {
        Ciudad ciudad = new Ciudad("CiudadE", 5, 5); // capacidad = 25
        // 21/25 = 0.84

        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 3)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 4)));

        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 3)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 4)));

        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 3)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 4)));

        ciudad.addBloque(new BloqueResidencial(new Posicion(3, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(3, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(3, 2)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(3, 3)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(3, 4)));

        ciudad.addBloque(new BloqueResidencial(new Posicion(4, 0)));

        assertEquals(21.0 / 25.0, ciudad.getDensidad(), 0.0001);
        assertTrue(ciudad.estaProximaASaturacion());
    }

    @Test
    void creacionDeCiudadValida() {
        Ciudad ciudad = new Ciudad("NeoCity", 10, 20);

        assertEquals("NeoCity", ciudad.getNombre());
        assertEquals(10, ciudad.getFilas());
        assertEquals(20, ciudad.getColumnas());
        assertNotNull(ciudad);
        assertTrue(ciudad.estaVacia());
        assertEquals(0, ciudad.contarBloques());
    }

    @Test
    void nombreNuloOVacio_error() {
        assertThrows(IllegalArgumentException.class, () ->
                new Ciudad(null, 10, 10));

        assertThrows(IllegalArgumentException.class, () ->
                new Ciudad("", 10, 10));

        assertThrows(IllegalArgumentException.class, () ->
                new Ciudad("   ", 10, 10));
    }

    @Test
    void filasOColumnasInvalidas_lanzaDimensionesInvalidasException() {
        assertThrows(DimensionesInvalidasException.class, () ->
                new Ciudad("CiudadA", 0, 10));

        assertThrows(DimensionesInvalidasException.class, () ->
                new Ciudad("CiudadB", 10, 0));

        assertThrows(DimensionesInvalidasException.class, () ->
                new Ciudad("CiudadC", -1, 10));

        assertThrows(DimensionesInvalidasException.class, () ->
                new Ciudad("CiudadD", 10, -1));
    }

    @Test
    void ciudadCreadaConTipoEstructuralCorrectoSegunCapacidad() {
        Ciudad pequena = new Ciudad("Pequena", 10, 10);   // 100
        Ciudad mediana = new Ciudad("Mediana", 30, 30);   // 900
        Ciudad grande = new Ciudad("Grande", 50, 50);     // 2500

        assertEquals(TipoEstructuralCiudad.PEQUENA, pequena.getTipoEstructural());
        assertEquals(TipoEstructuralCiudad.MEDIANA, mediana.getTipoEstructural());
        assertEquals(TipoEstructuralCiudad.GRANDE, grande.getTipoEstructural());
    }

    @Test
    void insercionDeBloqueEnPosicionValida() {
        Ciudad ciudad = new Ciudad("CiudadA", 5, 5);
        BloqueResidencial bloque = new BloqueResidencial(new Posicion(2, 3));

        ciudad.addBloque(bloque);

        assertTrue(ciudad.estaOcupada(2, 3));
        assertEquals(bloque, ciudad.getBloque(2, 3));
        assertEquals(1, ciudad.contarBloques());
    }

    @Test
    void insercionEnCeldaOcupada_lanzaCeldaOcupadaException() {
        Ciudad ciudad = new Ciudad("CiudadB", 5, 5);

        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 1)));

        assertThrows(CeldaOcupadaException.class, () ->
                ciudad.addBloque(new BloqueEnergia(new Posicion(1, 1))));
    }

    @Test
    void insercionFueraDeLimites_lanzaPosicionFueraDeLimitesException() {
        Ciudad ciudad = new Ciudad("CiudadC", 5, 5);

        assertThrows(PosicionFueraDeLimitesException.class, () ->
                ciudad.addBloque(new BloqueResidencial(new Posicion(7, 2))));

        assertThrows(PosicionFueraDeLimitesException.class, () ->
                ciudad.addBloque(new BloqueEnergia(new Posicion(2, 9))));
    }

    @Test
    void insercionDeBloqueNulo_lanzaBloqueNuloException() {
        Ciudad ciudad = new Ciudad("CiudadD", 5, 5);

        assertThrows(BloqueNuloException.class, () ->
                ciudad.addBloque(null));
    }

    @Test
    void listadoDeBloquesCorrecto_yConteoPorTipoCorrecto() {
        Ciudad ciudad = new Ciudad("CiudadE", 6, 6);

        BloqueResidencial residencial1 = new BloqueResidencial(new Posicion(0, 0));
        BloqueResidencial residencial2 = new BloqueResidencial(new Posicion(0, 1));
        BloqueEnergia energia = new BloqueEnergia(new Posicion(1, 1));
        BloqueIndustrial industrial = new BloqueIndustrial(new Posicion(2, 2));
        BloqueServicios servicios = new BloqueServicios(new Posicion(3, 3));

        ciudad.addBloque(residencial1);
        ciudad.addBloque(residencial2);
        ciudad.addBloque(energia);
        ciudad.addBloque(industrial);
        ciudad.addBloque(servicios);

        List<Bloque> bloques = ciudad.listarBloques();

        assertEquals(5, bloques.size());
        assertTrue(bloques.contains(residencial1));
        assertTrue(bloques.contains(residencial2));
        assertTrue(bloques.contains(energia));
        assertTrue(bloques.contains(industrial));
        assertTrue(bloques.contains(servicios));

        assertEquals(2, ciudad.contarBloquesPorTipo(TipoBloque.RESIDENCIAL));
        assertEquals(1, ciudad.contarBloquesPorTipo(TipoBloque.ENERGIA));
        assertEquals(1, ciudad.contarBloquesPorTipo(TipoBloque.INDUSTRIAL));
        assertEquals(1, ciudad.contarBloquesPorTipo(TipoBloque.SERVICIOS));
        assertEquals(0, ciudad.contarBloquesPorTipo(TipoBloque.TRANSPORTE));
    }

    @Test
    void activarBloqueExistenteFunciona() {
        Ciudad ciudad = new Ciudad("CiudadA", 5, 5);
        BloqueResidencial bloque = new BloqueResidencial(new Posicion(1, 1));

        ciudad.addBloque(bloque);
        ciudad.desactivarBloque(new Posicion(1, 1));

        assertFalse(ciudad.getBloque(1, 1).estaActivo());

        ciudad.activarBloque(new Posicion(1, 1));

        assertTrue(ciudad.getBloque(1, 1).estaActivo());
    }

    @Test
    void desactivarBloqueExistenteFunciona() {
        Ciudad ciudad = new Ciudad("CiudadB", 5, 5);
        BloqueEnergia bloque = new BloqueEnergia(new Posicion(2, 2));

        ciudad.addBloque(bloque);

        assertTrue(ciudad.getBloque(2, 2).estaActivo());

        ciudad.desactivarBloque(new Posicion(2, 2));

        assertFalse(ciudad.getBloque(2, 2).estaActivo());
    }

    @Test
    void activarEnCeldaVacia_lanzaCeldaVaciaException() {
        Ciudad ciudad = new Ciudad("CiudadC", 5, 5);

        assertThrows(CeldaVaciaException.class, () ->
                ciudad.activarBloque(new Posicion(0, 0)));
    }

    @Test
    void desactivarEnCeldaVacia_lanzaCeldaVaciaException() {
        Ciudad ciudad = new Ciudad("CiudadD", 5, 5);

        assertThrows(CeldaVaciaException.class, () ->
                ciudad.desactivarBloque(new Posicion(3, 3)));
    }

    @Test
    void hayBloquesActivos_correctoTrasVariosCambios() {
        Ciudad ciudad = new Ciudad("CiudadE", 5, 5);

        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(2, 2)));

        assertTrue(ciudad.hayBloquesActivos());

        ciudad.desactivarBloque(new Posicion(0, 0));
        ciudad.desactivarBloque(new Posicion(1, 1));
        ciudad.desactivarBloque(new Posicion(2, 2));

        assertFalse(ciudad.hayBloquesActivos());

        ciudad.activarBloque(new Posicion(1, 1));

        assertTrue(ciudad.hayBloquesActivos());
    }

    @Test
    void listadoDeBloquesActivos_correctoTrasCambios() {
        Ciudad ciudad = new Ciudad("CiudadF", 5, 5);

        BloqueResidencial residencial = new BloqueResidencial(new Posicion(0, 0));
        BloqueEnergia energia = new BloqueEnergia(new Posicion(1, 1));
        BloqueIndustrial industrial = new BloqueIndustrial(new Posicion(2, 2));

        ciudad.addBloque(residencial);
        ciudad.addBloque(energia);
        ciudad.addBloque(industrial);

        ciudad.desactivarBloque(new Posicion(1, 1));

        List<Bloque> activos = ciudad.listarBloquesActivos();

        assertEquals(2, activos.size());
        assertTrue(activos.contains(residencial));
        assertTrue(activos.contains(industrial));
        assertFalse(activos.contains(energia));

        ciudad.desactivarBloque(new Posicion(0, 0));
        ciudad.activarBloque(new Posicion(1, 1));

        activos = ciudad.listarBloquesActivos();

        assertEquals(2, activos.size());
        assertFalse(activos.contains(residencial));
        assertTrue(activos.contains(energia));
        assertTrue(activos.contains(industrial));
    }

    @Test
    void expansionValidaADimensionesMayores() {
        Ciudad ciudad = new Ciudad("CiudadA", 10, 10);

        ciudad.expandir(20, 20);

        assertEquals(20, ciudad.getFilas());
        assertEquals(20, ciudad.getColumnas());
    }

    @Test
    void expansionConDimensionesMenoresOIguales_lanzaExpansionCiudadException() {
        Ciudad ciudad = new Ciudad("CiudadB", 20, 20);

        assertThrows(ExpansionCiudadException.class, () ->
                ciudad.expandir(20, 25));

        assertThrows(ExpansionCiudadException.class, () ->
                ciudad.expandir(25, 20));

        assertThrows(ExpansionCiudadException.class, () ->
                ciudad.expandir(10, 10));
    }

    @Test
    void expansionPorEncimaDelLimiteGlobal_lanzaExpansionCiudadException() {
        Ciudad ciudad = new Ciudad("CiudadC", 20, 20);

        assertThrows(ExpansionCiudadException.class, () ->
                ciudad.expandir(101, 50));

        assertThrows(ExpansionCiudadException.class, () ->
                ciudad.expandir(50, 101));
    }

    @Test
    void expansionSuperandoElMaximoDeExpansiones_lanzaExpansionCiudadException() {
        Ciudad ciudad = new Ciudad("CiudadD", 10, 10);

        ciudad.expandir(20, 20);
        ciudad.expandir(30, 30);
        ciudad.expandir(40, 40);
        ciudad.expandir(50, 50);
        ciudad.expandir(60, 60);

        assertThrows(ExpansionCiudadException.class, () ->
                ciudad.expandir(70, 70));
    }

    @Test
    void trasExpandir_losBloquesAnterioresSeConservanEnSusPosiciones() {
        Ciudad ciudad = new Ciudad("CiudadE", 10, 10);

        BloqueResidencial b1 = new BloqueResidencial(new Posicion(0, 0));
        BloqueEnergia b2 = new BloqueEnergia(new Posicion(2, 3));
        BloqueIndustrial b3 = new BloqueIndustrial(new Posicion(9, 9));

        ciudad.addBloque(b1);
        ciudad.addBloque(b2);
        ciudad.addBloque(b3);

        ciudad.expandir(20, 20);

        assertSame(b1, ciudad.getBloque(0, 0));
        assertSame(b2, ciudad.getBloque(2, 3));
        assertSame(b3, ciudad.getBloque(9, 9));
    }

    @Test
    void trasExpandir_contarBloquesDevuelveElMismoValor() {
        Ciudad ciudad = new Ciudad("CiudadF", 10, 10);

        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(2, 2)));

        int bloquesAntes = ciudad.contarBloques();

        ciudad.expandir(20, 20);

        assertEquals(bloquesAntes, ciudad.contarBloques());
    }

    @Test
    void trasExpandir_capacidadMaximaEsMayor() {
        Ciudad ciudad = new Ciudad("CiudadG", 10, 10);

        int capacidadAntes = ciudad.getCapacidadMaxima();

        ciudad.expandir(20, 30);

        assertTrue(ciudad.getCapacidadMaxima() > capacidadAntes);
        assertEquals(600, ciudad.getCapacidadMaxima());
    }

    @Test
    void trasExpandir_tipoEstructuralRecalculadoCorrectamente() {
        Ciudad ciudad = new Ciudad("CiudadH", 10, 10);

        assertEquals(TipoEstructuralCiudad.PEQUENA, ciudad.getTipoEstructural());

        ciudad.expandir(30, 30);

        assertEquals(TipoEstructuralCiudad.MEDIANA, ciudad.getTipoEstructural());

        ciudad.expandir(50, 50);

        assertEquals(TipoEstructuralCiudad.GRANDE, ciudad.getTipoEstructural());
    }

    @Test
    void noApareceNingunBloqueDuplicadoTrasCopiarTablero() {
        Ciudad ciudad = new Ciudad("CiudadI", 10, 10);

        BloqueResidencial b1 = new BloqueResidencial(new Posicion(0, 0));
        BloqueEnergia b2 = new BloqueEnergia(new Posicion(1, 1));
        BloqueServicios b3 = new BloqueServicios(new Posicion(2, 2));

        ciudad.addBloque(b1);
        ciudad.addBloque(b2);
        ciudad.addBloque(b3);

        ciudad.expandir(20, 20);

        List<Bloque> bloques = ciudad.listarBloques();

        assertEquals(3, bloques.size());
        assertEquals(1, bloques.stream().filter(b -> b == b1).count());
        assertEquals(1, bloques.stream().filter(b -> b == b2).count());
        assertEquals(1, bloques.stream().filter(b -> b == b3).count());
    }

    @Test
    void elModeloSigueFuncionandoTrasExpansionEInsercionesPosteriores() {
        Ciudad ciudad = new Ciudad("CiudadJ", 10, 10);

        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 1)));

        ciudad.expandir(20, 20);

        ciudad.addBloque(new BloqueIndustrial(new Posicion(15, 15)));
        ciudad.addBloque(new BloqueTransporte(new Posicion(19, 19)));

        assertEquals(4, ciudad.contarBloques());
        assertTrue(ciudad.estaOcupada(0, 0));
        assertTrue(ciudad.estaOcupada(1, 1));
        assertTrue(ciudad.estaOcupada(15, 15));
        assertTrue(ciudad.estaOcupada(19, 19));

        assertNotNull(ciudad.getBloque(15, 15));
        assertNotNull(ciudad.getBloque(19, 19));
    }

    @Test
    void obtencionDeBloqueExistente() {
        Ciudad ciudad = new Ciudad("CiudadA", 5, 5);

        BloqueResidencial bloque = new BloqueResidencial(new Posicion(2, 3));
        ciudad.addBloque(bloque);

        assertNotNull(ciudad.getBloque(2, 3));
        assertSame(bloque, ciudad.getBloque(2, 3));
    }

    @Test
    void obtencionDePosicionVacia() {
        Ciudad ciudad = new Ciudad("CiudadB", 5, 5);

        assertNull(ciudad.getBloque(1, 1));
    }

    @Test
    void eliminacionDeBloqueExistente() {
        Ciudad ciudad = new Ciudad("CiudadC", 5, 5);

        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));

        ciudad.removeBloque(0, 0);

        assertNull(ciudad.getBloque(0, 0));
        assertEquals(0, ciudad.contarBloques());
    }

    @Test
    void eliminacionDePosicionVacia_lanzaCeldaVaciaException() {
        Ciudad ciudad = new Ciudad("CiudadD", 5, 5);

        assertThrows(CeldaVaciaException.class, () ->
                ciudad.removeBloque(2, 2));
    }

    @Test
    void conteoTotalCorrecto() {
        Ciudad ciudad = new Ciudad("CiudadE", 5, 5);

        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 2)));

        assertEquals(3, ciudad.contarBloques());
    }
}