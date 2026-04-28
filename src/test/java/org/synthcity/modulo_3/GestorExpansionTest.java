package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

public class GestorExpansionTest {

    private MetricaCiudad crearMetricaFalsa(double densidad, double saturacion, double ratioServicios) {
        return new MetricaCiudad(new ResultadoSimulacion() {
            @Override public int getBloquesTotales() { return 100; }
            @Override public int getBloquesActivos() { return 50; }
            @Override public java.util.Map getConteoPorTipo() { return Collections.emptyMap(); }
            @Override public double getTendenciaEstabilidad() { return 0.0; }
            @Override public double getTendenciaContaminacion() { return 0.0; }
            @Override public int getCiclosEjecutados() { return 10; }
            @Override public MotivoParadaSimulacion getMotivoParada() { return null; }
            @Override public double getCoberturaServiciosPonderada() { return 0.0; }
            @Override public double getEficienciaTransporte() { return 0.0; }
            @Override public java.util.List getCiclos() { return Collections.emptyList(); }
            @Override public double getDensidad() { return densidad; }
            @Override public double getIndiceSaturacion() { return saturacion; }
            @Override public double getRatioCoberturaServicios() { return ratioServicios; }
            @Override public boolean getNecesidadExpansionDetectada() { return false; }
        });
    }

    @Test
    public void testNecesitaExpansion_PorDensidadCritica() {
        GestorExpansion gestor = new GestorExpansion();
        assertTrue(gestor.necesitaExpansion(crearMetricaFalsa(0.85, 0.50, 1.0)));
    }

    @Test
    public void testPuedeExpandirseAhora_CiudadBloqueada() {
        GestorExpansion gestor = new GestorExpansion();
        Ciudad ciudadBloqueada = new Ciudad() {
            @Override public boolean puedeExpandirse() { return false; }
        };
        assertFalse(gestor.puedeExpandirseAhora(ciudadBloqueada, crearMetricaFalsa(0.5, 0.5, 1.0)));
    }
}
