package org.synthcity.modulo_2;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.synthcity.modulo_1.Bloque;
import org.synthcity.modulo_1.TipoBloque;

public class AnalizadorBloques {

    public Map<TipoBloque, Integer> calcularConteosPorTipo(List<Bloque> bloques) {
        Map<TipoBloque, Integer> conteos = new EnumMap<>(TipoBloque.class);

        // Inicializar todos los tipos a 0
        // Garantiza que M3 nunca recibe null al consultar un tipo
        for (TipoBloque tipo : TipoBloque.values()) {
            conteos.put(tipo, 0);
        }

        // Recorrer y contar con defensas ante nulos
        if (bloques != null) {
            for (Bloque bloque : bloques) {
                if (bloque != null) {
                    TipoBloque tipo = bloque.getTipo();
                    if (tipo != null) {
                        conteos.put(tipo, conteos.get(tipo) + 1);
                    }
                }
            }
        }

        return conteos;
    }
}