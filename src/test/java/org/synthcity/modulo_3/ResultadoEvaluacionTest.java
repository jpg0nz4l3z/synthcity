package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ResultadoEvaluacionTest {

    @Test
    public void testConstructorGuardaDatosCorrectamente() {
        NivelEvaluacion nivelDummy = NivelEvaluacion.OPTIMO;

        MetricaCiudad metricaDummy = new MetricaCiudad(new org.synthcity.modulo_2.ResultadoSimulacion() {
            @Override public int getBloquesTotales() { return 10; }
            @Override public int getBloquesActivos() { return 5; }
            @Override public java.util.Map getConteoPorTipo() { return java.util.Collections.emptyMap(); }
            @Override public java.util.List getCiclos() { return java.util.Collections.emptyList(); }
            @Override public double getTendenciaEstabilidad() { return 0; }
            @Override public double getTendenciaContaminacion() { return 0; }
            @Override public int getCiclosEjecutados() { return 1; }
            @Override public org.synthcity.modulo_2.MotivoParadaSimulacion getMotivoParada() { return null; }
            @Override public boolean getNecesidadExpansionDetectada() { return false; }
            @Override public double getCoberturaServiciosPonderada() { return 0; }
            @Override public double getEficienciaTransporte() { return 0; }
            @Override public double getDensidad() { return 0; }
            @Override public double getIndiceSaturacion() { return 0; }
            @Override public double getRatioCoberturaServicios() { return 0; }
        });

        List<AlertaEvaluacion> alertas = new ArrayList<>();
        alertas.add(AlertaEvaluacion.TENDENCIA_NEGATIVA);

        ResultadoEvaluacion resultado = new ResultadoEvaluacion(
                "CiudadTest", metricaDummy, nivelDummy, "Todo OK",
                85.5, TendenciaTemporal.MEJORANDO, alertas, true, 10
        );

        assertEquals("CiudadTest", resultado.getNombreCiudad());
        assertEquals(85.5, resultado.getScoreViabilidad());
        assertEquals(TendenciaTemporal.MEJORANDO, resultado.getTendenciaTemporal());
        assertTrue(resultado.fueExpandida());
        assertEquals(1, resultado.getAlertas().size());
    }

    @Test
    public void testConstructorLanzaExcepcionConDatosNulos() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ResultadoEvaluacion(null, null, null, null, 0, null, null, false, 0);
        }, "Debería lanzar IllegalArgumentException si los datos obligatorios son nulos");
    }
}
