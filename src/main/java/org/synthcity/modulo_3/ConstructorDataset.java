package org.synthcity.modulo_3;

import java.util.ArrayList;
import java.util.List;

public class ConstructorDataset {

    // Los datos del Sprint 3
    private final List<RegistroDato> registros;

    public ConstructorDataset() {
        this.registros = new ArrayList<>();
    }

    public void agregarRegistro(RegistroDato dato) {
        if (dato != null) {
            this.registros.add(dato);
        }
    }

    public List<RegistroDato> getDataset() {
        return this.registros;
    }

    public int getTamano() {
        return this.registros.size();
    }

    public boolean estaVacio() {
        return this.registros.isEmpty();
    }

    public void limpiar() {
        this.registros.clear();
    }
}
