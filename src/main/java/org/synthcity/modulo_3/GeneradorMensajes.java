package org.synthcity.modulo_3;

public class GeneradorMensajes {

    public static String generarMensaje(NivelEvaluacion nivel) {
        switch (nivel) {
            case SIN_DATOS:
                return "La ciudad no tiene datos";
            case CRITICO:
                return "La ciudad no tiene actividad";
            case INESTABLE:
                return "La ciudad es inestable";
            case FUNCIONAL:
                return "La ciudad es operativa";
            case OPTIMO:
                return "La ciudad está en estado óptimo";
            default:
                return "Estado desconocido";
        }
    }
}