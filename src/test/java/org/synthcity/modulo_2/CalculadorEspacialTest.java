package org.synthcity.modulo_2;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculadorEspacialTest {

    private final CalculadorEspacial calculador = new CalculadorEspacial();

    @Test
    void coberturaServiciosPonderada_esUnoSiNoHayBloquesActivos() {
        assertEquals(1.0, calculador.calcularCoberturaServiciosPonderada(List.of()), 0.0001);
    }

    @Test
    void coberturaServiciosPonderada_esUnoSiNoHayGeneradoresDemanda() {
        List<Bloque> bloques = List.of(
                new BloqueEnergia(new Posicion(0, 0)),
                new BloqueServicios(new Posicion(0, 1))
        );

        assertEquals(1.0, calculador.calcularCoberturaServiciosPonderada(bloques), 0.0001);
    }

    @Test
    void coberturaServiciosPonderada_esCeroSiHayDemandaPeroNoServicios() {
        List<Bloque> bloques = List.of(
                new BloqueResidencial(new Posicion(0, 0)),
                new BloqueEnergia(new Posicion(0, 1))
        );

        assertEquals(0.0, calculador.calcularCoberturaServiciosPonderada(bloques), 0.0001);
    }

    @Test
    void coberturaServiciosPonderada_esAltaSiServicioEstaCerca() {
        List<Bloque> bloques = List.of(
                new BloqueResidencial(new Posicion(0, 0)),
                new BloqueServicios(new Posicion(0, 1))
        );

        assertTrue(calculador.calcularCoberturaServiciosPonderada(bloques) > 0.0);
    }

    @Test
    void coberturaServiciosPonderada_esCeroSiServicioEstaFueraDeRadio() {
        List<Bloque> bloques = List.of(
                new BloqueResidencial(new Posicion(0, 0)),
                new BloqueServicios(new Posicion(9, 9))
        );

        assertEquals(0.0, calculador.calcularCoberturaServiciosPonderada(bloques), 0.0001);
    }

    @Test
    void eficienciaTransporte_esCeroSiNoHayBloquesActivos() {
        assertEquals(0.0, calculador.calcularEficienciaTransporte(List.of()), 0.0001);
    }

    @Test
    void eficienciaTransporte_esCeroSiNoHayTransporte() {
        List<Bloque> bloques = List.of(
                new BloqueResidencial(new Posicion(0, 0)),
                new BloqueServicios(new Posicion(0, 1))
        );

        assertEquals(0.0, calculador.calcularEficienciaTransporte(bloques), 0.0001);
    }

    @Test
    void eficienciaTransporte_esMayorQueCeroSiTransporteEstaCerca() {
        List<Bloque> bloques = List.of(
                new BloqueResidencial(new Posicion(0, 0)),
                new BloqueServicios(new Posicion(0, 1)),
                new BloqueTransporte(new Posicion(1, 0))
        );

        assertTrue(calculador.calcularEficienciaTransporte(bloques) > 0.0);
    }

    @Test
    void eficienciaTransporte_esCeroSiTransporteEstaLejos() {
        List<Bloque> bloques = List.of(
                new BloqueResidencial(new Posicion(0, 0)),
                new BloqueTransporte(new Posicion(9, 9))
        );

        assertEquals(0.0, calculador.calcularEficienciaTransporte(bloques), 0.0001);
    }

    @Test
    void calcularDistanciaMedia_devuelveCeroConListasVaciasONulas() {
        assertEquals(0.0, calculador.calcularDistanciaMedia(null, List.of()), 0.0001);
        assertEquals(0.0, calculador.calcularDistanciaMedia(List.of(), null), 0.0001);
        assertEquals(0.0, calculador.calcularDistanciaMedia(List.of(), List.of()), 0.0001);
    }

    @Test
    void calcularDistanciaMedia_devuelveMediaCorrecta() {
        List<Bloque> grupo1 = List.of(
                new BloqueResidencial(new Posicion(0, 0))
        );

        List<Bloque> grupo2 = List.of(
                new BloqueServicios(new Posicion(0, 2)),
                new BloqueServicios(new Posicion(2, 0))
        );

        assertEquals(2.0, calculador.calcularDistanciaMedia(grupo1, grupo2), 0.0001);
    }
}
