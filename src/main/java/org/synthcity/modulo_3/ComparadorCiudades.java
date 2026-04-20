package org.synthcity.modulo_3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ComparadorCiudades {

    private ComparadorCiudades() {}

    // Orden: mayor score primero; desempate por menor número de alertas;
    // último desempate alfabético por nombre para que la ordenación sea determinista.
    public static int compararPorViabilidad(ResultadoEvaluacion a, ResultadoEvaluacion b) {
        if (a == null && b == null) return 0;
        if (a == null) return 1;
        if (b == null) return -1;

        int cmpScore = Double.compare(b.getScoreViabilidad(), a.getScoreViabilidad());
        if (cmpScore != 0) return cmpScore;

        int cmpAlertas = Integer.compare(a.getNumeroAlertas(), b.getNumeroAlertas());
        if (cmpAlertas != 0) return cmpAlertas;

        String nombreA = a.getNombreCiudad() == null ? "" : a.getNombreCiudad();
        String nombreB = b.getNombreCiudad() == null ? "" : b.getNombreCiudad();
        return nombreA.compareTo(nombreB);
    }

    public static List<ResultadoEvaluacion> ordenarPorViabilidad(List<ResultadoEvaluacion> evaluaciones) {
        if (evaluaciones == null) {
            throw new IllegalArgumentException("La lista de evaluaciones no puede ser nula.");
        }
        List<ResultadoEvaluacion> copia = new ArrayList<>(evaluaciones);
        copia.sort(ComparadorCiudades::compararPorViabilidad);
        return copia;
    }

    public static Comparator<ResultadoEvaluacion> porViabilidad() {
        return ComparadorCiudades::compararPorViabilidad;
    }
}
