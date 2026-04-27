package org.synthcity.modulo_4;
import java.util.List;

public interface Persistible {

    void guardar(Object dato);
    List<?> listar();
}
