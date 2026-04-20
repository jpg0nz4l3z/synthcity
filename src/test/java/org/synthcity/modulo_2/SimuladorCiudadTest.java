package org.synthcity.modulo_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.synthcity.modulo_1.Bloque;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.TipoBloque;
import java.util.ArrayList;
import java.util.List;

class SimuladorCiudadTest {

    private SimuladorCiudad simulador;

    @BeforeEach
    void setUp() {
        simulador = new SimuladorCiudad();
    }

    // =========================================================
    // STUBS LOCALES (Ajustados al código real del Módulo 1)
    // =========================================================

    class CiudadStub extends Ciudad {
        private final List<Bloque> bloques = new ArrayList<>();
        private final List<Bloque> activos = new ArrayList<>();
        private double densidad = 0.5;

        public CiudadStub() {
            super("TestCity", 10, 10);
        }

        // Estos métodos ya existen en el padre o los creamos para el test
        @Override public String getNombre() { return "TestCity"; }
        @Override public int getFilas() { return 10; }
        @Override public int getColumnas() { return 10; }
        @Override public int capacidadMaxima() { return 100; }

        // Si el M1 aún no tiene getDensidad en Ciudad.java, quita el @Override
        public double getDensidad() { return densidad; }

        @Override public List<Bloque> listarBloques() { return bloques; }
        @Override public List<Bloque> listarBloquesActivos() { return activos; }

        public void setDensidad(double d) { this.densidad = d; }
        public void agregarBloque(Bloque b, boolean activo) {
            bloques.add(b);
            if (activo) activos.add(b);
        }
    }

    class BloqueStub extends Bloque {
        private int energia = 0, consumo = 0, contaminacion = 0;
        private int demanda = 0, cobertura = 0, transporte = 0;

        public BloqueStub(TipoBloque tipo) {
            // AJUSTE CRÍTICO: El constructor de Bloque pide (TipoBloque, Posicion, boolean)
            super(tipo, new Posicion(0, 0), true);
        }

        // IMPLEMENTACIÓN DE LOS 7 MÉTODOS ABSTRACTOS REALES
        @Override public int getProduccionEnergia() { return energia; }
        @Override public int getConsumoEnergetico() { return consumo; }
        @Override public int getDemandaServicios() { return demanda; }
        @Override public int getCoberturaServicios() { return cobertura; }
        @Override public int getPresionIndustrial() { return 0; }
        @Override public int getSoporteTransporte() { return transporte; }
        @Override public int getContaminacion() { return contaminacion; }

        // Métodos de ayuda para configurar el test
        public BloqueStub setValoresEnergia(int e, int c) { this.energia = e; this.consumo = c; return this; }
        public BloqueStub setValoresServicios(int d, int co) { this.demanda = d; this.cobertura = co; return this; }
        public BloqueStub setContaminacion(int cont) { this.contaminacion = cont; return this; }
    }

    // =========================================================
    // PRUEBAS UNITARIAS
    // =========================================================

    @Test
    void testSimular_Polimorfismo_ExtraccionCorrecta() {
        CiudadStub ciudad = new CiudadStub();

        // Bloque que produce 15 de energía
        ciudad.agregarBloque(new BloqueStub(TipoBloque.ENERGIA).setValoresEnergia(15, 0), true);

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertEquals(15, resultado.getEnergiaProducida());
    }

    @Test
    void testSimular_CiudadVacia_EstadoCorrecto() {
        ResultadoSimulacion resultado = simulador.simular(new CiudadStub());
        assertEquals(EstadoSimulacion.CIUDAD_VACIA, resultado.getEstadoSimulacion());
    }

    @Test
    void testSimular_DeficitEnergetico_Detectado() {
        CiudadStub ciudad = new CiudadStub();
        // Bloque que consume 20 sin producir nada
        ciudad.agregarBloque(new BloqueStub(TipoBloque.RESIDENCIAL).setValoresEnergia(0, 20), true);

        ResultadoSimulacion resultado = simulador.simular(ciudad);

        assertTrue(resultado.hayDeficitEnergetico());
        assertEquals(EstadoSimulacion.DEFICIT_ENERGETICO, resultado.getEstadoSimulacion());
    }
}
