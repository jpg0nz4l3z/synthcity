package org.synthcity.modulo_1;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_1.bloques.BloqueServicios;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContratoPersistenciaModulo1Test {

    @Test
    void validarCiudadParaPersistenciaAceptaCiudadConBloquesActivosEInactivos() {
        Ciudad ciudad = new Ciudad("Ciudad Persistible", 10, 10);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueServicios(new Posicion(2, 2)));
        ciudad.desactivarBloque(new Posicion(1, 1));

        assertDoesNotThrow(() -> ContratoPersistenciaModulo1.validarCiudadParaPersistencia(ciudad));
        assertTrue(ContratoPersistenciaModulo1.esCiudadPersistible(ciudad));
    }

    @Test
    void validarCiudadParaPersistenciaRechazaCiudadNula() {
        assertThrows(IllegalArgumentException.class, () ->
                ContratoPersistenciaModulo1.validarCiudadParaPersistencia(null));

        assertFalse(ContratoPersistenciaModulo1.esCiudadPersistible(null));
    }

    @Test
    void validarBloqueParaPersistenciaRechazaBloqueNulo() {
        Ciudad ciudad = new Ciudad("Ciudad", 5, 5);

        assertThrows(IllegalArgumentException.class, () ->
                ContratoPersistenciaModulo1.validarBloqueParaPersistencia(null, ciudad));
    }

    @Test
    void validarBloqueParaPersistenciaRechazaBloqueFueraDeLaCiudad() {
        Ciudad ciudad = new Ciudad("Ciudad", 5, 5);
        BloqueResidencial bloque = new BloqueResidencial(new Posicion(8, 8));

        assertThrows(IllegalArgumentException.class, () ->
                ContratoPersistenciaModulo1.validarBloqueParaPersistencia(bloque, ciudad));
    }
}
