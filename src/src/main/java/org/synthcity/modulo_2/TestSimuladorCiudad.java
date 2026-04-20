package org.synthcity.modulo_2;

import org.synthcity.modulo_1.BloqueEnergia;
import org.synthcity.modulo_1.BloqueIndustrial;
import org.synthcity.modulo_1.BloqueResidencial;
import org.synthcity.modulo_1.BloqueServicios;
import org.synthcity.modulo_1.BloqueTransporte;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.TipoBloque;

public class TestSimuladorCiudad {

    public static void main(String[] args) {

        SimuladorCiudad simulador = new SimuladorCiudad();

        // TEST 1: Ciudad nula
        try {
            simulador.simular(null);
            System.out.println("❌ ERROR: no lanzó excepción con ciudad nula");
        } catch (CiudadNulaExcepcion e) {
            System.out.println("✅ TEST 1 OK: excepción ciudad nula capturada");
        }

        // TEST 2: Ciudad vacía
        Ciudad ciudadVacia = new Ciudad("Vacia", 3, 3);
        ResultadoSimulacion r1 = simulador.simular(ciudadVacia);
        System.out.println("\n--- TEST 2: CIUDAD VACIA ---");
        System.out.println(r1);
        verificar("Estado CIUDAD_VACIA",        r1.getEstadoSimulacion() == EstadoSimulacion.CIUDAD_VACIA);
        verificar("ciudadEstaVacia() = true",    r1.ciudadEstaVacia());
        verificar("hayBloquesActivos() = false", !r1.hayBloquesActivos());

        // TEST 3: 1 bloque activo
        Ciudad ciudad1 = new Ciudad("Una", 3, 3);
        ciudad1.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ResultadoSimulacion r2 = simulador.simular(ciudad1);
        System.out.println("\n--- TEST 3: 1 BLOQUE ACTIVO ---");
        System.out.println(r2);
        verificar("Estado EJECUTADA",  r2.getEstadoSimulacion() == EstadoSimulacion.EJECUTADA);
        verificar("Total bloques = 1", r2.getBloquesTotales() == 1);
        verificar("Activos = 1",       r2.getBloquesActivos() == 1);
        verificar("Inactivos = 0",     r2.getBloquesInactivos() == 0);

        // TEST 4: Ciudad mixta con varios tipos
        Ciudad ciudad2 = new Ciudad("Mixta", 5, 5);
        ciudad2.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad2.addBloque(new BloqueIndustrial(new Posicion(0, 1)));
        ciudad2.addBloque(new BloqueEnergia(new Posicion(1, 0)));
        BloqueServicios bs = new BloqueServicios(new Posicion(1, 1));
        bs.desactivar();
        ciudad2.addBloque(bs);
        BloqueTransporte bt = new BloqueTransporte(new Posicion(2, 0));
        bt.desactivar();
        ciudad2.addBloque(bt);
        ResultadoSimulacion r3 = simulador.simular(ciudad2);
        System.out.println("\n--- TEST 4: CIUDAD MIXTA ---");
        System.out.println(r3);
        verificar("Total = 5",     r3.getBloquesTotales() == 5);
        verificar("Activos = 3",   r3.getBloquesActivos() == 3);
        verificar("Inactivos = 2", r3.getBloquesInactivos() == 2);
        verificar("Activos + Inactivos = Total",
                r3.getBloquesActivos() + r3.getBloquesInactivos() == r3.getBloquesTotales());
        verificar("RESIDENCIAL = 1", r3.getCantidadPorTipo(TipoBloque.RESIDENCIAL) == 1);
        verificar("INDUSTRIAL = 1",  r3.getCantidadPorTipo(TipoBloque.INDUSTRIAL)  == 1);
        verificar("ENERGIA = 1",     r3.getCantidadPorTipo(TipoBloque.ENERGIA)     == 1);
        verificar("SERVICIOS = 1",   r3.getCantidadPorTipo(TipoBloque.SERVICIOS)   == 1);
        verificar("TRANSPORTE = 1",  r3.getCantidadPorTipo(TipoBloque.TRANSPORTE)  == 1);

        // TEST 5: Todos los bloques inactivos
        Ciudad ciudad3 = new Ciudad("Inactiva", 3, 3);
        BloqueEnergia be1 = new BloqueEnergia(new Posicion(0, 0));
        BloqueEnergia be2 = new BloqueEnergia(new Posicion(0, 1));
        be1.desactivar();
        be2.desactivar();
        ciudad3.addBloque(be1);
        ciudad3.addBloque(be2);
        ResultadoSimulacion r4 = simulador.simular(ciudad3);
        System.out.println("\n--- TEST 5: TODOS INACTIVOS ---");
        System.out.println(r4);
        verificar("Estado SIN_BLOQUES_ACTIVOS",
                r4.getEstadoSimulacion() == EstadoSimulacion.SIN_BLOQUES_ACTIVOS);
        verificar("hayBloquesActivos() = false", !r4.hayBloquesActivos());

        System.out.println("\n✅ TODOS LOS TESTS COMPLETADOS");
    }

    private static void verificar(String descripcion, boolean condicion) {
        System.out.println(condicion ? "  ✅ " + descripcion : "  ❌ FALLO: " + descripcion);
    }
}