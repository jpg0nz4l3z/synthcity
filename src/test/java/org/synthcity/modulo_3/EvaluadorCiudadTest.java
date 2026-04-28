package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.EstadoSimulacion;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class EvaluadorCiudadTest {

    private ResultadoSimulacion crearResultadoValido(double densidad, boolean forzarExpansion) {
        return new ResultadoSimulacion() {
            @Override public int getBloquesTotales() { return 100; }
            @Override public int getBloquesActivos() { return 100; }
            @Override public int getBloquesInactivos() { return 0; }
            @Override public Map getConteoPorTipo() { Map<Object, Integer> m = new HashMap<>(); m.put("T", 100); return m; }
            @Override public double getTendenciaEstabilidad() { return 0.5; }
            @Override public double getTendenciaContaminacion() { return -0.1; }
            @Override public int getCiclosEjecutados() { return 10; }
            @Override public MotivoParadaSimulacion getMotivoParada() { return null; }
            @Override public double getCoberturaServiciosPonderada() { return 0.9; }
            @Override public double getEficienciaTransporte() { return 0.8; }
            @Override public double getDensidad() { return densidad; }
            @Override public double getIndiceSaturacion() { return forzarExpansion ? 0.9 : 0.4; }
            @Override public double getRatioCoberturaServicios() { return 1.0; }
            @Override public boolean getNecesidadExpansionDetectada() { return false; }
            @Override public java.util.List getCiclos() { return Collections.emptyList(); }
            @Override public String getNombreCiudad() { return "TestCity"; }
            @Override public EstadoSimulacion getEstado() { return new EstadoSimulacion() { public boolean esVacioLegal() { return false; } }; }
        };
    }

    @Test
    public void testEvaluarFlujoNormalSinExpansion() {
        ConstructorDataset miDataset = new ConstructorDataset();
        GestorExpansion gestor = new GestorExpansion();
        Simulable simulableFalso = c -> fail("No debería llamar a simular()");

        EvaluadorCiudad evaluador = new EvaluadorCiudad(simulableFalso, gestor, miDataset);
        ResultadoEvaluacion evaluacion = evaluador.evaluar(crearResultadoValido(0.6, false), new Ciudad());

        assertNotNull(evaluacion);
        assertFalse(evaluacion.fueExpandida(), "No debería haberse expandido");
    }

    @Test
    public void testEvaluarFlujoConExpansion() {
        ConstructorDataset miDataset = new ConstructorDataset();
        Simulable simulableFalso = c -> crearResultadoValido(0.5, false);

        Ciudad ciudadSaturada = new Ciudad() {
            @Override public boolean puedeExpandirse() { return true; }
            @Override public int getFilas() { return 10; }
            @Override public int getColumnas() { return 10; }
            @Override public void expandir() {}
        };

        EvaluadorCiudad evaluador = new EvaluadorCiudad(simulableFalso, new GestorExpansion(), miDataset);
        ResultadoEvaluacion evaluacion = evaluador.evaluar(crearResultadoValido(0.95, true), ciudadSaturada);

        assertTrue(evaluacion.fueExpandida(), "El evaluador debería haber ordenado la expansión");
    }
}
