package org.synthcity.modulo_3;

import org.synthcity.modulo_2.ResultadoSimulacion;

public class EvaluadorCiudad {

    // =========================================================================
    // MÉTODO EVALUAR (EL CORAZÓN DEL MÓDULO)
    // =========================================================================
    public ResultadoEvaluacion evaluar(ResultadoSimulacion resultado) {

        // 1. Validar la entrada (Tu responsabilidad)
        validarEntrada(resultado);

        // 2. Construir la métrica (Responsabilidad Persona 2)
        MetricaCiudad metrica = construirMetrica(resultado);

        // 3. Determinar el nivel de evaluación (Responsabilidad Persona 3 integrada aquí) [cite: 2434-2441]
        NivelEvaluacion nivel = determinarNivelEvaluacion(metrica);

        // 4. Generar un mensaje explicativo (Responsabilidad Persona 4)
        String mensaje = generarMensaje(metrica, nivel);

        // 5. Construir y devolver el resultado final (Responsabilidad Persona 4)
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
        // La Persona 2 recibe el objeto de simulación y calcula sus propios datos
        return new MetricaCiudad(resultado);
    }

    private NivelEvaluacion determinarNivelEvaluacion(MetricaCiudad metrica) {
        // Reglas de evaluación (ORDEN NO ALTERABLE) - PDF Pág 18

        // 5.1. Ciudad sin datos
        if (metrica.getTotalBloques() == 0) {
            return NivelEvaluacion.SIN_DATOS;
        }
        // 5.2. Ciudad crítica
        if (metrica.getBloquesActivos() == 0) {
            return NivelEvaluacion.CRITICO;
        }
        // 5.3. Ciudad óptima
        if (metrica.getBloquesActivos() == metrica.getTotalBloques()) {
            return NivelEvaluacion.OPTIMO;
        }
        // 5.4. Ciudad funcional (Umbral del 60% / 0.6)
        if (metrica.getPorcentajeActivos() >= 0.6) {
            return NivelEvaluacion.FUNCIONAL;
        }
        // 5.5. Ciudad inestable
        return NivelEvaluacion.INESTABLE;
    }

    private String generarMensaje(MetricaCiudad metrica, NivelEvaluacion nivel) {
        // Usamos la clase estática de la Persona 4
        return GeneradorMensajes.generarMensaje(nivel);
    }

    private ResultadoEvaluacion construirResultado(String nombre, MetricaCiudad metrica, NivelEvaluacion nivel, String mensaje) {
        // Ensamblamos el objeto final para el Módulo 4
        return new ResultadoEvaluacion(nombre, metrica, nivel, mensaje);
    }
}