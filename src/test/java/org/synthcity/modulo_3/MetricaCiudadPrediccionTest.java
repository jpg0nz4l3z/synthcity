/*package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MetricaCiudadPrediccionTest {

    @Test
    void deberia_calcular_densidad_diversidad_y_ratios() {
        ResultadoSimulacion simulacion = crearSimulacionValida();
        MetricaCiudad metrica = new MetricaCiudad(simulacion);

        assertEquals(10, metrica.getTotalBloques());
        assertEquals(10, metrica.getCapacidadMaxima());
        assertEquals(1.0, metrica.getDensidadOcupacion(), 0.0001);
        assertEquals(1.0, metrica.getDiversidadTipos(), 0.0001);
        assertEquals(0.20, metrica.getRatioEnergia(), 0.0001);
        assertEquals(0.10, metrica.getRatioServicios(), 0.0001);
        assertEquals(0.10, metrica.getRatioTransporte(), 0.0001);
        assertEquals(0.10, metrica.getPresionIndustrial(), 0.0001);
        assertEquals(0.50, metrica.getPesoResidencial(), 0.0001);
    }

    @Test
    void deberia_mantener_compatibilidad_con_getters_antiguos() {
        ResultadoSimulacion simulacion = crearSimulacionValida();
        MetricaCiudad metrica = new MetricaCiudad(simulacion);

        assertEquals(10, metrica.getTotalBloques());
        assertEquals(7, metrica.getBloquesActivos());
        assertEquals(3, metrica.getBloquesInactivos());
        assertEquals(0.7, metrica.getPorcentajeActivos(), 0.0001);
        assertEquals(0.3, metrica.getPorcentajeInactivos(), 0.0001);
        assertNotNull(metrica.getConteoPorTipo());
    }

    @Test
    void ciudad_vacia_deberia_generar_metricas_seguras() {
        Map<TipoBloque, Integer> conteo = crearConteoBase();

        ResultadoSimulacion simulacion = new ResultadoSimulacion(
                "CiudadVacia",
                5,
                5,
                25,
                0,
                0,
                0,
                conteo,
                EstadoSimulacion.CIUDAD_VACIA
        );

        MetricaCiudad metrica = new MetricaCiudad(simulacion);

        assertEquals(0.0, metrica.getPorcentajeActivos(), 0.0001);
        assertEquals(1.0, metrica.getPorcentajeInactivos(), 0.0001);
        assertEquals(0.0, metrica.getDensidadOcupacion(), 0.0001);
        assertEquals(0.0, metrica.getDiversidadTipos(), 0.0001);
    }

    private ResultadoSimulacion crearSimulacionValida() {
        Map<TipoBloque, Integer> conteo = crearConteoBase();
        conteo.put(TipoBloque.RESIDENCIAL, 5);
        conteo.put(TipoBloque.ENERGIA, 2);
        conteo.put(TipoBloque.INDUSTRIAL, 1);
        conteo.put(TipoBloque.SERVICIOS, 1);
        conteo.put(TipoBloque.TRANSPORTE, 1);

        return new ResultadoSimulacion(
                "NeoMadrid",
                2,
                5,
                10,
                10,
                7,
                3,
                conteo,
                EstadoSimulacion.EJECUTADA
        );
    }

    private Map<TipoBloque, Integer> crearConteoBase() {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            conteo.put(tipo, 0);
        }
        return conteo;
    }
}*/
