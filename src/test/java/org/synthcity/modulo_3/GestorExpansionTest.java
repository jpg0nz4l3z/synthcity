package org.synthcity.modulo_3;

import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_2.SimuladorCiudad;

import static org.junit.jupiter.api.Assertions.*;

class GestorExpansionTest {

    private final GestorExpansion gestor = new GestorExpansion();
    private final SimuladorCiudad simulador = new SimuladorCiudad();

    @Test
    void necesitaExpansion_conMetricaNula_lanzaExcepcion() {
        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                gestor.necesitaExpansion(null)
        );
    }

    @Test
    void puedeExpandirseAhora_conCiudadNula_lanzaExcepcion() {
        MetricaCiudad metrica = crearMetricaCiudadNoSaturada();

        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                gestor.puedeExpandirseAhora(null, metrica)
        );
    }

    @Test
    void puedeExpandirseAhora_conMetricaNula_lanzaExcepcion() {
        Ciudad ciudad = new Ciudad("Test", 5, 5);

        assertThrows(ResultadoSimulacionInvalidoException.class, () ->
                gestor.puedeExpandirseAhora(ciudad, null)
        );
    }

    @Test
    void ciudadConDensidadAlta_necesitaExpansion() {
        Ciudad ciudad = new Ciudad("Densa", 2, 2);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 1)));

        ResultadoSimulacion resultado = simulador.simular(ciudad);
        MetricaCiudad metrica = new MetricaCiudad(resultado);

        assertTrue(gestor.necesitaExpansion(metrica));
    }

    @Test
    void ciudadNoSaturada_noNecesitaExpansion() {
        MetricaCiudad metrica = crearMetricaCiudadNoSaturada();

        assertFalse(gestor.necesitaExpansion(metrica));
    }

    @Test
    void puedeExpandirseAhora_siNecesitaExpansionYCiudadPuedeExpandirse() {
        Ciudad ciudad = new Ciudad("Expandible", 2, 2);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(1, 1)));

        MetricaCiudad metrica = new MetricaCiudad(simulador.simular(ciudad));

        assertTrue(gestor.puedeExpandirseAhora(ciudad, metrica));
    }

    @Test
    void noPuedeExpandirseAhora_siNoNecesitaExpansion() {
        Ciudad ciudad = new Ciudad("NoNecesita", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));

        MetricaCiudad metrica = new MetricaCiudad(simulador.simular(ciudad));

        assertFalse(gestor.puedeExpandirseAhora(ciudad, metrica));
    }

    private MetricaCiudad crearMetricaCiudadNoSaturada() {
        Ciudad ciudad = new Ciudad("Tranquila", 10, 10);
        ciudad.addBloque(new BloqueEnergia(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));

        return new MetricaCiudad(simulador.simular(ciudad));
    }
}