package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ComparadorCiudadesTest {

    @Test
    void compararPorViabilidad_deberia_priorizar_mejor_nivel() {
        ComparadorCiudades comparador = new ComparadorCiudades();

        ResultadoEvaluacion funcional = crearEvaluacion(
                "CiudadFuncional",
                10,
                7,
                3,
                crearConteo(5, 2, 1, 1, 1)
        );

        ResultadoEvaluacion critica = crearEvaluacion(
                "CiudadCritica",
                10,
                0,
                10,
                crearConteo(5, 1, 1, 2, 1)
        );

        int resultado = comparador.compararPorViabilidad(funcional, critica);

        assertTrue(resultado < 0);
    }

    @Test
    void compararPorViabilidad_deberia_desempatar_por_porcentaje_activo() {
        ComparadorCiudades comparador = new ComparadorCiudades();

        ResultadoEvaluacion funcionalMedia = crearEvaluacion(
                "FuncionalMedia",
                10,
                6,
                4,
                crearConteo(4, 2, 1, 2, 1)
        );

        ResultadoEvaluacion funcionalAlta = crearEvaluacion(
                "FuncionalAlta",
                10,
                8,
                2,
                crearConteo(4, 2, 1, 2, 1)
        );

        int resultado = comparador.compararPorViabilidad(funcionalAlta, funcionalMedia);

        assertTrue(resultado < 0);
    }

    @Test
    void ordenarPorViabilidad_deberia_ordenar_lista_completa() {
        ComparadorCiudades comparador = new ComparadorCiudades();

        ResultadoEvaluacion critica = crearEvaluacion(
                "CiudadCritica",
                10,
                0,
                10,
                crearConteo(5, 1, 1, 2, 1)
        );

        ResultadoEvaluacion inestable = crearEvaluacion(
                "CiudadInestable",
                10,
                5,
                5,
                crearConteo(5, 2, 1, 1, 1)
        );

        ResultadoEvaluacion funcional = crearEvaluacion(
                "CiudadFuncional",
                10,
                7,
                3,
                crearConteo(5, 2, 1, 1, 1)
        );

        List<ResultadoEvaluacion> lista = new ArrayList<>();
        lista.add(critica);
        lista.add(funcional);
        lista.add(inestable);

        List<ResultadoEvaluacion> ordenada = comparador.ordenarPorViabilidad(lista);

        assertEquals("CiudadFuncional", ordenada.get(0).getNombreCiudad());
        assertEquals("CiudadInestable", ordenada.get(1).getNombreCiudad());
        assertEquals("CiudadCritica", ordenada.get(2).getNombreCiudad());
    }

    @Test
    void esCoherenteConPrediccion_deberia_aceptar_sin_base_para_sin_datos() {
        ComparadorCiudades comparador = new ComparadorCiudades();

        ResultadoEvaluacion sinDatos = crearEvaluacion(
                "CiudadVacia",
                0,
                0,
                0,
                crearConteo(0, 0, 0, 0, 0)
        );

        PredictionResult prediccion = new PredictionResult(
                TendenciaPredicha.SIN_BASE,
                0.0,
                0.20,
                "No existe base suficiente para proyectar tendencia."
        );

        assertTrue(comparador.esCoherenteConPrediccion(sinDatos, prediccion));
    }

    @Test
    void esCoherenteConPrediccion_deberia_detectar_incoherencia_critico_vs_mejora() {
        ComparadorCiudades comparador = new ComparadorCiudades();

        ResultadoEvaluacion critica = crearEvaluacion(
                "CiudadCritica",
                10,
                0,
                10,
                crearConteo(5, 1, 1, 2, 1)
        );

        PredictionResult prediccion = new PredictionResult(
                TendenciaPredicha.MEJORA_PROBABLE,
                82.0,
                0.80,
                "La ciudad parece mejorar claramente."
        );

        assertFalse(comparador.esCoherenteConPrediccion(critica, prediccion));
    }

    @Test
    void esCoherenteConPrediccion_deberia_detectar_incoherencia_funcional_vs_riesgo_alto() {
        ComparadorCiudades comparador = new ComparadorCiudades();

        ResultadoEvaluacion funcional = crearEvaluacion(
                "CiudadFuncional",
                10,
                7,
                3,
                crearConteo(5, 2, 1, 1, 1)
        );

        PredictionResult prediccion = new PredictionResult(
                TendenciaPredicha.RIESGO_ALTO,
                20.0,
                0.75,
                "La ciudad entra en una fase de riesgo alto."
        );

        assertFalse(comparador.esCoherenteConPrediccion(funcional, prediccion));
    }

    @Test
    void esCoherenteConPrediccion_deberia_aceptar_funcional_con_estable() {
        ComparadorCiudades comparador = new ComparadorCiudades();

        ResultadoEvaluacion funcional = crearEvaluacion(
                "CiudadFuncional",
                10,
                7,
                3,
                crearConteo(5, 2, 1, 1, 1)
        );

        PredictionResult prediccion = new PredictionResult(
                TendenciaPredicha.ESTABLE,
                68.0,
                0.75,
                "La ciudad mantiene una evolucion estable."
        );

        assertTrue(comparador.esCoherenteConPrediccion(funcional, prediccion));
    }

    private ResultadoEvaluacion crearEvaluacion(
            String nombreCiudad,
            int totalBloques,
            int bloquesActivos,
            int bloquesInactivos,
            Map<TipoBloque, Integer> conteo
    ) {
        ResultadoSimulacion simulacion = new ResultadoSimulacion(
                nombreCiudad,
                2,
                5,
                10,
                totalBloques,
                bloquesActivos,
                bloquesInactivos,
                conteo,
                determinarEstado(totalBloques, bloquesActivos)
        );

        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        return evaluador.evaluar(simulacion);
    }

    private EstadoSimulacion determinarEstado(int totalBloques, int bloquesActivos) {
        if (totalBloques == 0) {
            return EstadoSimulacion.CIUDAD_VACIA;
        }
        if (bloquesActivos == 0) {
            return EstadoSimulacion.SIN_BLOQUES_ACTIVOS;
        }
        return EstadoSimulacion.EJECUTADA;
    }

    private Map<TipoBloque, Integer> crearConteo(
            int residenciales,
            int energia,
            int industrial,
            int servicios,
            int transporte
    ) {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            conteo.put(tipo, 0);
        }

        conteo.put(TipoBloque.RESIDENCIAL, residenciales);
        conteo.put(TipoBloque.ENERGIA, energia);
        conteo.put(TipoBloque.INDUSTRIAL, industrial);
        conteo.put(TipoBloque.SERVICIOS, servicios);
        conteo.put(TipoBloque.TRANSPORTE, transporte);

        return conteo;
    }
}