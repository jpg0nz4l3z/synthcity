package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueIndustrial;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_1.bloques.BloqueServicios;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;

import static org.junit.jupiter.api.Assertions.*;

class MetricaCiudadTest {

    private final SimuladorCiudad simulador = new SimuladorCiudad();

    @Test
    void resultadoSimulacionNulo_lanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                new MetricaCiudad(null));
    }

    @Test
    void porcentajesCorrectos() {
        Ciudad ciudad = new Ciudad("CiudadA", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 2)));
        ciudad.desactivarBloque(new Posicion(0, 2));

        ResultadoSimulacion rs = simulador.simular(ciudad);
        MetricaCiudad m = new MetricaCiudad(rs);

        assertEquals(3, m.getTotalBloques());
        assertEquals(2, m.getBloquesActivos());
        assertEquals(1, m.getBloquesInactivos());
        assertEquals(2.0 / 3.0, m.getPorcentajeActivos(), 0.000001);
        assertEquals(1.0 / 3.0, m.getPorcentajeInactivos(), 0.000001);
    }

    @Test
    void ratioEnergeticoCorrecto_incluyendoConsumoCero() {
        Ciudad ciudad = new Ciudad("CiudadB", 10, 10);
        // solo energía activa -> produce y consume algo; ratio > 0
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));

        ResultadoSimulacion rs = simulador.simular(ciudad);
        MetricaCiudad m = new MetricaCiudad(rs);

        assertTrue(m.getRatioEnergetico() > 0.0);

        // ciudad vacía -> total 0, desde resultado y métrica el ratio debe quedar coherente
        Ciudad vacia = new Ciudad("Vacia", 10, 10);
        ResultadoSimulacion rsVacia = simulador.simular(vacia);
        MetricaCiudad mVacia = new MetricaCiudad(rsVacia);

        assertEquals(1.0, mVacia.getRatioEnergetico(), 0.000001);
    }

    @Test
    void ratioCoberturaCorrecto_incluyendoDemandaCero() {
        Ciudad ciudad = new Ciudad("CiudadC", 10, 10);
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 0)));

        ResultadoSimulacion rs = simulador.simular(ciudad);
        MetricaCiudad m = new MetricaCiudad(rs);

        assertEquals(1.0, m.getRatioCoberturaServicios(), 0.000001);
    }

    @Test
    void indiceSaturacionCoherente() {
        Ciudad ciudad = new Ciudad("CiudadD", 5, 5); // capacidad 25

        // 20 bloques para densidad 0.80
        for (int i = 0; i < 10; i++) {
            ciudad.addBloque(new BloqueEnergia(new Posicion(i / 5, i % 5)));
        }
        for (int i = 10; i < 20; i++) {
            ciudad.addBloque(new BloqueIndustrial(new Posicion(i / 5, i % 5)));
        }

        ResultadoSimulacion rs = simulador.simular(ciudad);
        MetricaCiudad m = new MetricaCiudad(rs);

        assertTrue(m.getIndiceSaturacion() >= m.getDensidad());
        assertTrue(m.getIndiceSaturacion() >= 0.0 && m.getIndiceSaturacion() <= 1.0);
    }

    @Test
    void indiceViabilidadBaseCoherente() {
        Ciudad ciudad = new Ciudad("CiudadE", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 2)));
        ciudad.addBloque(new BloqueServicios(new Posicion(0, 3)));

        ResultadoSimulacion rs = simulador.simular(ciudad);
        MetricaCiudad m = new MetricaCiudad(rs);

        assertTrue(m.getIndiceViabilidadBase() >= 0.0);
        assertTrue(m.getIndiceViabilidadBase() <= 1.0);
    }

    @Test
    void conteoPorTipoYDiversidadCorrectos() {
        Ciudad ciudad = new Ciudad("CiudadF", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 2)));

        ResultadoSimulacion rs = simulador.simular(ciudad);
        MetricaCiudad m = new MetricaCiudad(rs);

        assertEquals(1, m.getConteoPorTipo().get(TipoBloque.ENERGIA));
        assertEquals(1, m.getConteoPorTipo().get(TipoBloque.RESIDENCIAL));
        assertEquals(1, m.getConteoPorTipo().get(TipoBloque.INDUSTRIAL));
        assertEquals(0, m.getConteoPorTipo().get(TipoBloque.SERVICIOS));
        assertEquals(0, m.getConteoPorTipo().get(TipoBloque.TRANSPORTE));

        assertTrue(m.getDiversidadTipos() > 0.0);
        assertTrue(m.getDiversidadTipos() <= 1.0);
    }
}
