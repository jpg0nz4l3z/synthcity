// Ubicación: src/main/java/org/synthcity/modulo_3/EvaluadorCiudad.java
package org.synthcity.modulo_3;

import org.synthcity.modulo_2.ResultadoSimulacion;
// Nota: Cuando tengas las clases de la Persona 3 y 4, descomenta estos imports si están en otro paquete

public class EvaluadorCiudad {

    // =========================================================================
    // METODO EVALUAR
    // =========================================================================
    public Object evaluar(ResultadoSimulacion resultado) { // Devuelve Object temporalmente hasta tener ResultadoEvaluacion

        // 1. Validar la entrada
        validarEntrada(resultado);

        // 2. Construir la métrica (Usando la clase REAL de la Persona 2)
        MetricaCiudad metrica = construirMetrica(resultado);

        /* *  TODO COMENTADO A LA ESPERA DE LA PERSONA 3 Y 4
         * Cuando te den sus clases, descomenta este bloque y cambia
         * el "Object" del metodo por "ResultadoEvaluacion".
         *
        // 3. Determinar el nivel de evaluación (Persona 3)
        NivelEvaluacion nivel = determinarNivelEvaluacion(metrica);

        // 4. Generar un mensaje explicativo (Persona 4)
        String mensaje = generarMensaje(metrica, nivel);

        // 5 y 6. Construir y devolver el resultado final (Persona 4)
        return construirResultado(resultado.getNombreCiudad(), metrica, nivel, mensaje);
        */

        System.out.println("Evaluación temporal completada. MetricaCiudad generada con éxito.");
        return null; // Retorno temporal
    }

    // =========================================================================
    // MÉTODOS AUXILIARES OBLIGATORIOS
    // =========================================================================

    private void validarEntrada(ResultadoSimulacion resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("El resultado de simulación no puede ser nulo.");
        }
        //  CAMBIO CLAVE: Usamos getBloquesTotales() para coincidir con tu compañero
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
        // Aquí ya estamos usando el código real que te pasó tu compañero
        return new MetricaCiudad(resultado);
    }

    /*
     * MÉTODOS DE LA PERSONA 3 Y 4 COMENTADOS HASTA QUE ENTREGUEN SUS PARTES
     *
    private NivelEvaluacion determinarNivelEvaluacion(MetricaCiudad metrica) { ... }
    private String generarMensaje(MetricaCiudad metrica, NivelEvaluacion nivel) { ... }
    private ResultadoEvaluacion construirResultado(String nombre, MetricaCiudad metrica, NivelEvaluacion nivel, String mensaje) { ... }
    */
}