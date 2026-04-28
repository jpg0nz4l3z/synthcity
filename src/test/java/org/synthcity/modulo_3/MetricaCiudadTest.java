package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_2.ResultadoSimulacion;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

public class MetricaCiudadTest {

    @Test
    public void testTendenciasDeEstabilidadYContaminacion() {
        ResultadoSimulacion resultadoFalso = new ResultadoSimulacion() {
            @Override public double getTendenciaEstabilidad() { return -0.15; }
            @Override public double getTendenciaContaminacion() { return 0.5; }
            @Override public int getBloquesTotales() { return 10; }
            @Override public int getBloquesActivos() { return 5; }
            @Override public java.util.Map getConteoPorTipo() { return Collections.emptyMap(); }
            @Override public java.util.List getCiclos() { return Collections.emptyList(); }
            @Override public int getCiclosEjecutados() { return 10; }
            @Override public double getCoberturaServiciosPonderada() { return 0.0; }
            @Override public double getEficienciaTransporte() { return 0.0; }
            @Override public double getDensidad() { return 0.5; }
            @Override public double getIndiceSaturacion() { return 0.5; }
            @Override public double getRatioCoberturaServicios() { return 0.5; }
            @Override public boolean getNecesidadExpansionDetectada() { return false; }
            @Override public org.synthcity.modulo_2.MotivoParadaSimulacion getMotivoParada() { return null; }
        };

        MetricaCiudad metrica = new MetricaCiudad(resultadoFalso);

        assertTrue(metrica.estaEnTendenciaNegativa(), "Debería estar en tendencia negativa (-0.15)");
        assertTrue(metrica.tieneContaminacionCreciente(), "Debería tener contaminación creciente (0.5)");
    }
}
