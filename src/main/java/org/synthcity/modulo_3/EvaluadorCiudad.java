package org.synthcity.modulo_3;

import org.synthcity.modulo_2.ResultadoSimulacion;

public class EvaluadorCiudad {

    //  Metodo principal y Secuencia Lógica Obligatoria
    public ResultadoEvaluacion evaluar(ResultadoSimulacion resultado) {

        // 1. Validar entrada
        validarEntrada(resultado);

        // 2. Construir la métrica
        MetricaCiudad metrica = construirMetrica(resultado);

        // 3. Determinar el nivel de evaluación
        NivelEvaluacion nivel = determinarNivelEvaluacion(metrica);

        // 4. Generar mensaje
        String mensaje = generarMensaje(metrica, nivel);

        // 5. Construir el resultado final
        ResultadoEvaluacion resultadoFinal = construirResultado(metrica, nivel, mensaje);

        // 6. Devolver el resultado
        return resultadoFinal;
    }

    // Orquestador limpio delegando en métodos auxiliares

    private void validarEntrada(ResultadoSimulacion resultado) {
        if (resultado == null) {
            throw new ResultadoSimulacionInvalidoException("El ResultadoSimulacion recibido es nulo.");
        }
        if (resultado.getTotalBloques() < 0 || resultado.getBloquesActivos() < 0 || resultado.getBloquesInactivos() < 0) {
            throw new ResultadoSimulacionInvalidoException("Los conteos de bloques no pueden ser negativos.");
        }
        if ((resultado.getBloquesActivos() + resultado.getBloquesInactivos()) != resultado.getTotalBloques()) {
            throw new ResultadoSimulacionInvalidoException("Incoherencia estructural: suma de bloques incorrecta.");
        }
    }

    private MetricaCiudad construirMetrica(ResultadoSimulacion resultado) {
        // TODO: La Persona 2 rellenará este método
        return new MetricaCiudad();
    }

    private NivelEvaluacion determinarNivelEvaluacion(MetricaCiudad metrica) {
        // TODO: La Persona 3 rellenará este método
        return NivelEvaluacion.SIN_DATOS;
    }

    private String generarMensaje(MetricaCiudad metrica, NivelEvaluacion nivel) {
        // TODO: La Persona 3 rellenará este método
        return "Mensaje temporal de evaluación.";
    }

    private ResultadoEvaluacion construirResultado(MetricaCiudad metrica, NivelEvaluacion nivel, String mensaje) {
        // TODO: La Persona 1/4 rellenará esto cuando la clase ResultadoEvaluacion esté lista
        return new ResultadoEvaluacion();
    }
}