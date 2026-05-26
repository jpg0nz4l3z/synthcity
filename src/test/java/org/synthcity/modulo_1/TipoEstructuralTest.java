package org.synthcity.modulo_1;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueResidencial;

import static org.junit.jupiter.api.Assertions.*;

class TipoEstructuralTest {

    @Test
    void ciudad10x10_esPequena() {
        Ciudad ciudad = new Ciudad("CiudadA", 10, 10);

        assertEquals(TipoEstructuralCiudad.PEQUENA, ciudad.getTipoEstructural());
    }

    @Test
    void ciudad30x30_esMediana() {
        Ciudad ciudad = new Ciudad("CiudadB", 30, 30);

        assertEquals(TipoEstructuralCiudad.MEDIANA, ciudad.getTipoEstructural());
    }

    @Test
    void ciudad50x50_esGrande() {
        Ciudad ciudad = new Ciudad("CiudadC", 50, 50);

        assertEquals(TipoEstructuralCiudad.GRANDE, ciudad.getTipoEstructural());
    }

    @Test
    void elTipoNoCambiaAlAnadirBloquesSinExpandir() {
        Ciudad ciudad = new Ciudad("CiudadD", 10, 10);

        assertEquals(TipoEstructuralCiudad.PEQUENA, ciudad.getTipoEstructural());

        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(2, 2)));

        assertEquals(TipoEstructuralCiudad.PEQUENA, ciudad.getTipoEstructural());
    }

    @Test
    void elTipoSeRecalculaCorrectamenteTrasExpansion_dePequenaAMediana() {
        Ciudad ciudad = new Ciudad("CiudadE", 10, 10);

        assertEquals(TipoEstructuralCiudad.PEQUENA, ciudad.getTipoEstructural());

        ciudad.expandir(); // 20x20 = 400, sigue PEQUENA
        assertEquals(TipoEstructuralCiudad.PEQUENA, ciudad.getTipoEstructural());

        ciudad.expandir(); // 30x30 = 900, pasa a MEDIANA
        assertEquals(TipoEstructuralCiudad.MEDIANA, ciudad.getTipoEstructural());
    }

    @Test
    void elTipoSeRecalculaCorrectamenteTrasExpansion_deMedianaAGrande() {
        Ciudad ciudad = new Ciudad("CiudadF", 30, 30);

        assertEquals(TipoEstructuralCiudad.MEDIANA, ciudad.getTipoEstructural());

        ciudad.expandir(); // 40x40 = 1600, sigue MEDIANA
        assertEquals(TipoEstructuralCiudad.MEDIANA, ciudad.getTipoEstructural());

        ciudad.expandir(); // 50x50 = 2500, pasa a GRANDE
        assertEquals(TipoEstructuralCiudad.GRANDE, ciudad.getTipoEstructural());
    }
}
