package org.synthcity.modulo_1;

/**
 * Interfaz obligatoria según el Sprint 3.
 * Define el comportamiento que debe tener cualquier entidad capaz de crecer.
 */

public interface Expansionable {
    ResultadoExpansion expandir(int nuevasFilas, int nuevasColumnas);
    boolean puedeExpandirse();
}