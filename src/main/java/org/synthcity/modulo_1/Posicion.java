package org.synthcity.modulo_1;

import java.util.Objects;

public class Posicion {
    private int fila;
    private int columna;

    public Posicion(int fila, int columna) {
        if (fila < 0 || columna < 0) {
            throw new IllegalArgumentException("La fila y la columna deben ser >= 0");
        }

        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Posicion posicion = (Posicion) o;
        return fila == posicion.fila && columna == posicion.columna;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fila, columna);
    }

    @Override
    public String toString() {
        return "Fila -> " + fila + ". Columna -> " + columna;
    }
}
