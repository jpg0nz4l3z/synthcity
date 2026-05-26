package org.synthcity.modulo_4;
import java.util.List;

public interface Persistible<T> {

    void guardar(T dato);
    List<T> listar();
}
