package org.synthcity.modulo_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Importamos los stubs que crearemos en la carpeta del Módulo 1
import org.synthcity.modulo_1.TipoBloque;

class SimuladorCiudadTest {

    private SimuladorCiudad simulador;

    @BeforeEach
    void setUp() {
        simulador = new SimuladorCiudad();
    }

    // =========================================================
    // A. CASOS ESTRUCTURALES BÁSICOS
    // =========================================================

    @Test
    void testSimular_CiudadNula_LanzaExcepcion() {
        assertThrows(CiudadInvalidaParaSimulacionException.class, () -> {
            simulador.simular(null);
        });
    }

    @Test
    void testSimular_CiudadVacia_EstadoCorrecto() {
        // Usamos nuestro Stub de ciudad (simulando que está vacía)
        CiudadStub ciudadVacia = new CiudadStub("Ciudad Fantasma", 10, 10, 0.1, TipoEstructuralCiudad.PUEBLO);

        ResultadoSimulacion resultado = simulador.simular(ciudadVacia);

        assertEquals(EstadoSimulacion.CIUDAD_VACIA, resultado.getEstadoSimulacion());
        assertEquals(0, resultado.getBloquesTotales());
    }

    // =========================================================
    // B. COMPROBACIÓN DEL POLIMORFISMO
    // =========================================================

    @Test
    void testSimular_Polimorfismo_UnBloqueEnergia() {
        CiudadStub ciudad = new CiudadStub("Central City", 10, 10, 0.5, TipoEstructuralCiudad.CIUDAD);

        // Creamos un bloque de energía que devuelve 10 (sin usar ifs ni switches)
        BloqueStub central = new BloqueStub(TipoBloque.ENERGIA);
        central.setProduccionEnergia(ReglasSimulacion.ENERGIA_POR_BLOQUE_ENERGIA);

        ciudad.agregarBloque(central, true); // Lo añadimos como activo

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(10, resultado.getEnergiaProducida());
        assertEquals(1, resultado.getCantidadPorTipo(TipoBloque.ENERGIA));
        assertTrue(resultado.getEquilibrioEnergetico() > 0);
    }

    // =========================================================
    // C. DÉFICIT ENERGÉTICO
    // =========================================================

    @Test
    void testSimular_DeficitEnergetico_Detectado() {
        CiudadStub ciudad = new CiudadStub("Industrial City", 10, 10, 0.5, TipoEstructuralCiudad.CIUDAD);

        // Bloque industrial que consume 5 pero no produce nada
        BloqueStub industrial = new BloqueStub(TipoBloque.INDUSTRIAL);
        industrial.setConsumoEnergetico(ReglasSimulacion.CONSUMO_INDUSTRIAL);

        ciudad.agregarBloque(industrial, true);

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertTrue(resultado.hayDeficitEnergetico());
        assertEquals(EstadoSimulacion.DEFICIT_ENERGETICO, resultado.getEstadoSimulacion());
    }

    // =========================================================
    // D. REGLAS DEL SISTEMA (DENSIDAD)
    // =========================================================

    @Test
    void testSimular_DensidadAlta_AplicaPenalizacionContaminacion() {
        // Ciudad con densidad del 90% (Supera el umbral de 0.80)
        CiudadStub ciudad = new CiudadStub("Kowloon", 10, 10, 0.90, TipoEstructuralCiudad.METROPOLIS);

        BloqueStub industrial = new BloqueStub(TipoBloque.INDUSTRIAL);
        industrial.setContaminacionGenerada(ReglasSimulacion.CONTAMINACION_POR_INDUSTRIAL);

        ciudad.agregarBloque(industrial, true);

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        // Contaminación base (5) + Extra por hacinamiento (10) = 15
        assertEquals(15, resultado.getContaminacion());
    }
}