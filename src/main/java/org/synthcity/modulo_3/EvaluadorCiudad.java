package org.synthcity.modulo_3;

import org.synthcity.modulo_2.ResultadoSimulacion;

/**
 * Persona 1: Orquestador del Módulo 3.
 * Transforma la simulación en una evaluación formal aplicando reglas deterministas.
 */
public class EvaluadorCiudad {

    // =========================================================================
    // MeTODO EVALUAR (EL CORAZÓN DEL MÓDULO)
    // =========================================================================
    public ResultadoEvaluacion evaluar(ResultadoSimulacion resultado) {

        // 1. Validar la entrada (Persona 1)
        validarEntrada(resultado);

        // 2. Construir la métrica (Persona 2)
        MetricaCiudad metrica = construirMetrica(resultado);

        // 3. Determinar el nivel de evaluación (Persona 3 integrada en Evaluador)
        NivelEvaluacion nivel = determinarNivelEvaluacion(metrica);

        // 4. Generar un mensaje explicativo (Persona 4)
        String mensaje = generarMensaje(metrica, nivel);

        // 5. Construir y devolver el resultado final (Persona 4)
        return construirResultado(resultado.getNombreCiudad(), metrica, nivel, mensaje);
    }

    // =========================================================================
    // MÉTODOS AUXILIARES OBLIGATORIOS
    // =========================================================================

    private void validarEntrada(ResultadoSimulacion resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("El resultado de simulación no puede ser nulo.");
        }
        if (resultado.getBloquesTotales() < 0 || resultado.getBloquesActivos() < 0 || resultado.getBloquesInactivos() < 0) {
            throw new ResultadoSimulacionInvalidoException("Los contadores de bloques no pueden ser negativos.");
        }
        if (resultado.getBloquesActivos() > resultado.getBloquesTotales() || resultado.getBloquesInactivos() > resultado.getBloquesTotales()) {
            throw new ResultadoSimulacionInvalidoException("Los bloques activos o inactivos no pueden superar el total.");
        }
        if ((resultado.getBloquesActivos() + resultado.getBloquesInactivos()) != resultado.getBloquesTotales()) {
            throw new ResultadoSimulacionInvalidoException("Incoherencia: Activos + Inactivos no suma el total.");
        }
        if (resultado.getConteoPorTipo() == null) {
            throw new ResultadoSimulacionInvalidoException("El conteo por tipo no puede ser nulo.");
        }
    }

    private MetricaCiudad construirMetrica(ResultadoSimulacion resultado) {
        // La Persona 2 calcula los porcentajes en su constructor [cite: 1025-1033]
        return new MetricaCiudad(resultado);
    }

    private NivelEvaluacion determinarNivelEvaluacion(MetricaCiudad metrica) {
        int total = metrica.getTotalBloques();
        int activos = metrica.getBloquesActivos();
        double porcentaje = metrica.getPorcentajeActivos();

        // Reglas de evaluación
        if (total == 0) {
            return NivelEvaluacion.SIN_DATOS;
        }
        if (activos == 0) {
            return NivelEvaluacion.CRITICO;
        }
        if (activos == total) {
            return NivelEvaluacion.OPTIMO;
        }
        if (porcentaje >= 0.6) {
            return NivelEvaluacion.FUNCIONAL;
        }

        return NivelEvaluacion.INESTABLE;
    }

    private String generarMensaje(MetricaCiudad metrica, NivelEvaluacion nivel) {
        // Conectamos con la clase Generar Mensaje
        return GeneradorMensajes.generarMensaje(nivel);
    }

    private ResultadoEvaluacion construirResultado(String nombre, MetricaCiudad metrica, NivelEvaluacion nivel, String mensaje) {
        // Ensamblamos el objeto final para el Módulo 4
        return new ResultadoEvaluacion(nombre, metrica, nivel, mensaje);
    }
}