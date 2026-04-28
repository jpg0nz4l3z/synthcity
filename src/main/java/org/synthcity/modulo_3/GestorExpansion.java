package org.synthcity.modulo_3;

import org.synthcity.modulo_1.Ciudad;

public class GestorExpansion {

    public boolean necesitaExpansion(MetricaCiudad metrica) {
        return
                metrica.getDensidad() >= 0.80 ||
                        metrica.getIndiceSaturacion() >= 0.75 ||
                        (metrica.getDensidad() >= 0.70 && metrica.getRatioCoberturaServicios() < 1.0) ||
                        metrica.isNecesidadExpansionDetectada();
    }

    public boolean puedeExpandirseAhora(Ciudad ciudad, MetricaCiudad metrica) {
        if (!ciudad.puedeExpandirse()) {
            return false;
        }

        int nuevasFilas = ciudad.getFilas() + 1;
        int nuevasColumnas = ciudad.getColumnas() + 1;

        if (nuevasFilas > Ciudad.MAX_FILAS || nuevasColumnas > Ciudad.MAX_COLUMNAS) {
            return false;
        }
        return true;
    }
}
