package org.synthcity.modulo_3;

/**
 * Clase principal del Módulo 3.
 * Orquesta la transformación de datos en métricas y resultados evaluados.
 */
public class EvaluadorCiudad {

    /**
     * Secuencia lógica OBLIGATORIA. Este orden no debe alterarse
     */
    public Object evaluar(Object resultado) { // TODO: Cambiar Object a ResultadoSimulacion cuando M2 lo cree

        // 1. Validar entrada (Tu responsabilidad)
        validarEntrada(resultado);

        // 2. Construir la métrica (Responsabilidad de la Persona 2)
        // 3. Determinar el nivel de evaluación (Responsabilidad de la Persona 3)
        // 4. Generar mensaje (Responsabilidad de la Persona 3)
        // 5. Construir el resultado final
        // 6. Devolver el resultado final

        System.out.println("[LOG] Validación completada. Flujo en construcción.");
        return null;
    }

    /**
     * Validaciones estrictas exigidas.
     */
    private void validarEntrada(Object resultadoObj) {
        // Validación 1: resultado != null [cite: 1943]
        if (resultadoObj == null) {
            throw new ResultadoSimulacionInvalidoException("El ResultadoSimulacion recibido es nulo.");
        }

        /* * TODO: Cuando el Módulo 2 entregue su clase, descomentaremos estas validaciones exactas:
         * ResultadoSimulacion resultado = (ResultadoSimulacion) resultadoObj;
         * if (resultado.getTotalBloques() < 0) throw new ResultadoSimulacionInvalidoException("totalBloques < 0");
         * if (resultado.getBloquesActivos() < 0) throw new ResultadoSimulacionInvalidoException("bloquesActivos < 0");
         * if (resultado.getBloquesInactivos() < 0) throw new ResultadoSimulacionInvalidoException("bloquesInactivos < 0");
         * // ... y el resto de validaciones matemáticas[cite: 1944, 1945, 1947].
         */
    }
}