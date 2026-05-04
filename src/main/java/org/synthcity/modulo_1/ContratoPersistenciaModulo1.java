package org.synthcity.modulo_1;

import org.synthcity.modulo_1.bloques.Bloque;

import java.util.List;

public final class ContratoPersistenciaModulo1 {

    private ContratoPersistenciaModulo1() {
    }

    public static void validarCiudadParaPersistencia(Ciudad ciudad) {
        if (ciudad == null) {
            throw new IllegalArgumentException("La ciudad no puede ser nula.");
        }

        if (ciudad.getNombre() == null || ciudad.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad debe tener nombre para persistirse.");
        }

        if (ciudad.getFilas() <= 0 || ciudad.getColumnas() <= 0) {
            throw new IllegalArgumentException("La ciudad debe tener dimensiones validas.");
        }

        if (ciudad.getCapacidadMaxima() != ciudad.getFilas() * ciudad.getColumnas()) {
            throw new IllegalArgumentException("La capacidad de la ciudad no coincide con sus dimensiones.");
        }

        if (ciudad.getTipoEstructural() == null) {
            throw new IllegalArgumentException("La ciudad debe tener tipo estructural.");
        }

        if (ciudad.getExpansionesRealizadas() < 0) {
            throw new IllegalArgumentException("Las expansiones realizadas no pueden ser negativas.");
        }

        List<Bloque> bloques = ciudad.listarBloques();
        if (bloques == null) {
            throw new IllegalArgumentException("La lista de bloques no puede ser nula.");
        }

        for (Bloque bloque : bloques) {
            validarBloqueParaPersistencia(bloque, ciudad);
        }
    }

    public static void validarBloqueParaPersistencia(Bloque bloque, Ciudad ciudad) {
        if (bloque == null) {
            throw new IllegalArgumentException("El bloque no puede ser nulo.");
        }

        if (bloque.getTipo() == null) {
            throw new IllegalArgumentException("El bloque debe tener tipo.");
        }

        Posicion posicion = bloque.getPosicion();
        if (posicion == null) {
            throw new IllegalArgumentException("El bloque debe tener posicion.");
        }

        if (posicion.getFila() < 0 || posicion.getColumna() < 0) {
            throw new IllegalArgumentException("La posicion del bloque debe ser positiva.");
        }

        if (ciudad != null && !ciudad.dentroLimites(posicion.getFila(), posicion.getColumna())) {
            throw new IllegalArgumentException("El bloque debe estar dentro de los limites de la ciudad.");
        }
    }

    public static boolean esCiudadPersistible(Ciudad ciudad) {
        try {
            validarCiudadParaPersistencia(ciudad);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
