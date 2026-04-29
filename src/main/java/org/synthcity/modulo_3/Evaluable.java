package org.synthcity.modulo_3;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_2.ResultadoSimulacion;

@FunctionalInterface
public interface Evaluable {
    ResultadoEvaluacion evaluar(ResultadoSimulacion resultadoSimulacion, Ciudad ciudad);
}
