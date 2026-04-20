package org.synthcity.modulo_4;

import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.ResultadoEvaluacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PresentadorCiudad {

    private final FormateadorResultado formateador;

    public PresentadorCiudad() {
        this.formateador = new FormateadorResultado();
    }

    public SalidaTexto presentar(ResultadoEvaluacion resultado) {
        validarEntrada(resultado);
        String contenido = formateador.formatear(resultado);
        return new SalidaTexto(contenido);
    }

    public SalidaTexto generarResumen(Ciudad ciudad, ResultadoEvaluacion evaluacion, PredictionResult prediccion) {

        StringBuilder sb = new StringBuilder();

        // 1. Ciudad
        sb.append("Ciudad: ").append(ciudad.getNombre()).append("\n");

        // 2. Evaluación
        sb.append("Evaluación: ").append(evaluacion.getNivelEvaluacion())
                .append(" (Score: ").append(evaluacion.getScoreViabilidad()).append(")\n");

        // 3. Predicción
        if (prediccion != null) {
            sb.append("Predicción: ").append(prediccion.getTendenciaPredicha());
        } else {
            sb.append("Predicción: No disponible");
        }

        // Fecha para la encapsulación
        String fechaRegistro = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Retorna la SalidaTexto
        return new SalidaTexto("Resumen Breve", sb.toString(), ciudad.getNombre(), fechaRegistro);
    }

    private void validarEntrada(ResultadoEvaluacion resultado) {
        if (resultado == null) {
            throw new FormatoSalidaException("ResultadoEvaluacion no puede ser null.");
        }

        if (resultado.getNombreCiudad() == null || resultado.getNombreCiudad().isBlank()) {
            throw new FormatoSalidaException("El nombre de la ciudad es obligatorio.");
        }

        if (resultado.getMetricaCiudad() == null) {
            throw new FormatoSalidaException("La metrica no puede ser null.");
        }

        if (resultado.getNivelEvaluacion() == null) {
            throw new FormatoSalidaException("El nivel de evaluacion no puede ser null.");
        }

        if (resultado.getMensaje() == null || resultado.getMensaje().isBlank()) {
            throw new FormatoSalidaException("El mensaje no puede ser null ni vacio.");
        }

        validarMetrica(resultado.getMetricaCiudad());
    }

    private void validarMetrica(MetricaCiudad metrica) {
        if (metrica.getConteoPorTipo() == null) {
            throw new FormatoSalidaException("El conteo por tipo no puede ser null.");
        }

        if (metrica.getTotalBloques() < 0) {
            throw new FormatoSalidaException("Total de bloques invalido.");
        }

        if (metrica.getBloquesActivos() < 0) {
            throw new FormatoSalidaException("Bloques activos invalidos.");
        }

        if (metrica.getBloquesInactivos() < 0) {
            throw new FormatoSalidaException("Bloques inactivos invalidos.");
        }
    }
}