package org.synthcity.modulo_3;

import org.synthcity.modulo_1.Ciudad;

public class GestorExpansion {

    public boolean necesitaExpansion(MetricaCiudad metrica) {
        if (metrica == null) {
            throw new ResultadoSimulacionInvalidoException("La métrica no puede ser nula.");
        }

        return metrica.getDensidad() >= 0.80
                || metrica.getIndiceSaturacion() >= 0.75
                || (metrica.getDensidad() >= 0.70 && metrica.getRatioCoberturaServicios() < 1.0)
                || metrica.isNecesidadExpansionDetectada()
                || metrica.saturacionDetectada();
    }

    public boolean puedeExpandirseAhora(Ciudad ciudad, MetricaCiudad metrica) {
        if (ciudad == null) {
            throw new ResultadoSimulacionInvalidoException("La ciudad no puede ser nula.");
        }
        if (metrica == null) {
            throw new ResultadoSimulacionInvalidoException("La métrica no puede ser nula.");
        }

        return necesitaExpansion(metrica) && ciudad.puedeExpandirse();
    }
}
