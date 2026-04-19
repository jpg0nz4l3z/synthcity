package org.synthcity.modulo_1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class testmodulo1 {

    private Ciudad ciudad; // 5x5 = capacidad 25, tipo PEQUEÑA

    @BeforeEach
    void setUp() {
        ciudad = new Ciudad("TestCity", 5, 5);
    }


    //  COMPORTAMIENTO FUNCIONAL DE LAS SUBCLASES

    @Test
    void testBloqueEnergiaProduceEnergia() {
        Bloque b = new BloqueEnergia(new Posicion(0, 0));
        assertTrue(b.getProduccionEnergia() > 0,
                "BloqueEnergía debe producir energía positiva");
        assertEquals(ReglasSimulacion.ENERGIA_POR_BLOQUE_ENERGIA, b.getProduccionEnergia());
        assertEquals(0, b.getContaminacion());
        assertEquals(0, b.getDemandaServicios());
    }

    @Test
    void testBloqueResidencialGeneraDemanda() {
        Bloque b = new BloqueResidencial(new Posicion(0, 0));
        assertTrue(b.getDemandaServicios() > 0,
                "BloqueResidencial debe generar demanda de servicios");
        assertEquals(ReglasSimulacion.DEMANDA_POR_RESIDENCIAL, b.getDemandaServicios());
        assertEquals(0, b.getProduccionEnergia());
        assertEquals(0, b.getContaminacion());
    }

    @Test
    void testBloqueIndustrialContaminaYPresion() {
        Bloque b = new BloqueIndustrial(new Posicion(0, 0));
        assertTrue(b.getContaminacion() > 0,
                "BloqueIndustrial debe generar contaminación");
        assertTrue(b.getPresionIndustrial() > 0,
                "BloqueIndustrial debe generar presión industrial");
        assertEquals(ReglasSimulacion.CONTAMINACION_POR_INDUSTRIAL, b.getContaminacion());
        assertEquals(ReglasSimulacion.PRESION_POR_INDUSTRIAL,       b.getPresionIndustrial());
        assertEquals(0, b.getProduccionEnergia());
    }

    @Test
    void testBloqueServiciosCubreServicios() {
        Bloque b = new BloqueServicios(new Posicion(0, 0));
        assertTrue(b.getCoberturaServicios() > 0,
                "BloqueServicios debe aportar cobertura de servicios");
        assertEquals(ReglasSimulacion.COBERTURA_POR_SERVICIO, b.getCoberturaServicios());
        assertEquals(0, b.getDemandaServicios());
        assertEquals(0, b.getContaminacion());
    }

    @Test
    void testBloqueTransporteAportaSoporte() {
        Bloque b = new BloqueTransporte(new Posicion(0, 0));
        assertTrue(b.getSoporteTransporte() > 0,
                "BloqueTransporte debe aportar soporte de conectividad");
        assertEquals(ReglasSimulacion.TRANSPORTE_SOPORTE, b.getSoporteTransporte());
        assertEquals(0, b.getContaminacion());
    }

    @Test
    void testTodosLosBloquesTienenConsumoPositivo() {
        // Invariante del sistema: todos los bloques consumen energía
        List<Bloque> bloques = List.of(
                new BloqueResidencial(new Posicion(0, 0)),
                new BloqueEnergia(new Posicion(0, 1)),
                new BloqueIndustrial(new Posicion(0, 2)),
                new BloqueServicios(new Posicion(0, 3)),
                new BloqueTransporte(new Posicion(0, 4))
        );
        for (Bloque b : bloques) {
            assertTrue(b.getConsumoEnergetico() > 0,
                    b.getTipo() + " debe tener consumo energético > 0");
        }
    }

    @Test
    void testTodosLosValoresSonMayoresOIgualACero() {
        // Invariante: ningún método puede devolver valor negativo
        List<Bloque> bloques = List.of(
                new BloqueResidencial(new Posicion(0, 0)),
                new BloqueEnergia(new Posicion(0, 1)),
                new BloqueIndustrial(new Posicion(0, 2)),
                new BloqueServicios(new Posicion(0, 3)),
                new BloqueTransporte(new Posicion(0, 4))
        );
        for (Bloque b : bloques) {
            String t = b.getTipo().name();
            assertTrue(b.getProduccionEnergia()  >= 0, t + ": produccion >= 0");
            assertTrue(b.getConsumoEnergetico()  >= 0, t + ": consumo >= 0");
            assertTrue(b.getDemandaServicios()   >= 0, t + ": demanda >= 0");
            assertTrue(b.getCoberturaServicios() >= 0, t + ": cobertura >= 0");
            assertTrue(b.getPresionIndustrial()  >= 0, t + ": presion >= 0");
            assertTrue(b.getSoporteTransporte()  >= 0, t + ": soporte >= 0");
            assertTrue(b.getContaminacion()      >= 0, t + ": contaminacion >= 0");
        }
    }

    @Test
    void testValoresCoherentesConReglasSimulacion() {
        // Los bloques deben devolver exactamente las constantes acordadas
        assertEquals(ReglasSimulacion.CONSUMO_RESIDENCIAL,
                new BloqueResidencial(new Posicion(0,0)).getConsumoEnergetico());
        assertEquals(ReglasSimulacion.CONSUMO_ENERGIA,
                new BloqueEnergia(new Posicion(0,0)).getConsumoEnergetico());
        assertEquals(ReglasSimulacion.CONSUMO_INDUSTRIAL,
                new BloqueIndustrial(new Posicion(0,0)).getConsumoEnergetico());
        assertEquals(ReglasSimulacion.CONSUMO_SERVICIOS,
                new BloqueServicios(new Posicion(0,0)).getConsumoEnergetico());
        assertEquals(ReglasSimulacion.CONSUMO_TRANSPORTE,
                new BloqueTransporte(new Posicion(0,0)).getConsumoEnergetico());
    }

    @Test
    void testValoresSonConstantesEntrellamadas() {
        Bloque b = new BloqueIndustrial(new Posicion(0, 0));
        assertEquals(b.getContaminacion(), b.getContaminacion(),
                "Los valores deben ser constantes entre llamadas sucesivas");
    }

    // TIPO ESTRUCTURAL

    @Test
    void testTipoEstructuralPequena() {
        // 10x10 = 100 <= 400 → PEQUENA
        Ciudad c = new Ciudad("P", 10, 10);
        assertEquals(TipoEstructuralCiudad.PEQUENA, c.getTipoEstructural());
    }

    @Test
    void testTipoEstructuralMediana() {
        // 30x30 = 900, entre 400 y 1600 → MEDIANA
        Ciudad c = new Ciudad("M", 30, 30);
        assertEquals(TipoEstructuralCiudad.MEDIANA, c.getTipoEstructural());
    }

    @Test
    void testTipoEstructuralGrande() {
        // 50x50 = 2500 > 1600 → GRANDE
        Ciudad c = new Ciudad("G", 50, 50);
        assertEquals(TipoEstructuralCiudad.GRANDE, c.getTipoEstructural());
    }

    @Test
    void testTipoEstructuralNoCambiaPorBloques() {
        // Añadir bloques NO cambia el tipo estructural
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 1)));
        ciudad.addBloque(new BloqueIndustrial(new Posicion(2, 2)));
        assertEquals(TipoEstructuralCiudad.PEQUENA, ciudad.getTipoEstructural(),
                "El tipo estructural depende del grid, no de los bloques");
    }

    @Test
    void testTipoEstructuralSeRecalculaTrasExpansion() {
        assertEquals(TipoEstructuralCiudad.PEQUENA, ciudad.getTipoEstructural());
        ciudad.expandir(50, 50); // 2500 > 1600
        assertEquals(TipoEstructuralCiudad.GRANDE, ciudad.getTipoEstructural());
    }

    // DENSIDAD Y SATURACIÓN

    @Test
    void testDensidadCiudadVaciaEsCero() {
        assertEquals(0.0, ciudad.getDensidad(), 0.001);
        assertEquals(0, ciudad.getOcupacionActual());
    }

    @Test
    void testDensidadDecimalCorrectaNoDivisionEntera() {
        // Ciudad 10x10 = 100 celdas. 25 bloques = 0.25
        // Si hay división entera el resultado sería 0 en vez de 0.25
        Ciudad c = new Ciudad("D", 10, 10);
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                c.addBloque(new BloqueResidencial(new Posicion(i, j)));

        assertEquals(0.25, c.getDensidad(), 0.001,
                "Si obtienes 0.0 en lugar de 0.25 es el error de división entera");
    }

    @Test
    void testDensidadSiempreEntre0y1() {
        assertTrue(ciudad.getDensidad() >= 0.0 && ciudad.getDensidad() <= 1.0);
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        assertTrue(ciudad.getDensidad() >= 0.0 && ciudad.getDensidad() <= 1.0);
    }

    @Test
    void testDensidadSeActualizaAlEliminar() {
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        double antes = ciudad.getDensidad();
        assertTrue(antes > 0.0);
        ciudad.removeBloque(0, 0);
        assertEquals(0.0, ciudad.getDensidad(), 0.001);
    }

    @Test
    void testProximaASaturacionConUmbral80() {
        // 5x5 = 25. 80% = 20 bloques
        assertFalse(ciudad.estaProximaASaturacion());

        int fila = 0, col = 0;
        for (int n = 0; n < 20; n++) {
            ciudad.addBloque(new BloqueResidencial(new Posicion(fila, col)));
            col++;
            if (col >= 5) { col = 0; fila++; }
        }
        assertTrue(ciudad.estaProximaASaturacion(),
                "Con 20/25 bloques (80%) debe detectarse saturación");
    }


    // CREACIÓN Y TABLERO

    @Test
    void testCreacionCiudadValida() {
        assertEquals("TestCity", ciudad.getNombre());
        assertEquals(5, ciudad.getFilas());
        assertEquals(5, ciudad.getColumnas());
        assertEquals(25, ciudad.getCapacidadMaxima());
        assertTrue(ciudad.estaVacia());
    }

    @Test
    void testNombreNuloLanzaError() {
        assertThrows(IllegalArgumentException.class,
                () -> new Ciudad(null, 5, 5));
    }

    @Test
    void testNombreVacioLanzaError() {
        assertThrows(IllegalArgumentException.class,
                () -> new Ciudad("   ", 5, 5));
    }

    @Test
    void testDimensionesInvalidasLanzanError() {
        assertThrows(Exception.class, () -> new Ciudad("X", -1, 5));
        assertThrows(Exception.class, () -> new Ciudad("X",  5, 0));
        assertThrows(Exception.class, () -> new Ciudad("X",  0, 0));
    }

    @Test
    void testCiudadSeCreaConTipoEstructuralCorrecto() {
        Ciudad c10 = new Ciudad("A", 10, 10); // 100 → PEQUENA
        Ciudad c30 = new Ciudad("B", 30, 30); // 900 → MEDIANA
        Ciudad c50 = new Ciudad("C", 50, 50); // 2500 → GRANDE
        assertEquals(TipoEstructuralCiudad.PEQUENA, c10.getTipoEstructural());
        assertEquals(TipoEstructuralCiudad.MEDIANA, c30.getTipoEstructural());
        assertEquals(TipoEstructuralCiudad.GRANDE,  c50.getTipoEstructural());
    }

    @Test
    void testInsercionBloquePosicionValida() {
        Bloque b = new BloqueResidencial(new Posicion(2, 3));
        ciudad.addBloque(b);
        assertTrue(ciudad.estaOcupada(2, 3));
        assertEquals(b, ciudad.getBloque(2, 3));
    }

    @Test
    void testInsercionEnCeldaOcupadaLanzaError() {
        Posicion pos = new Posicion(0, 0);
        ciudad.addBloque(new BloqueResidencial(pos));
        assertThrows(CeldaOcupadaException.class,
                () -> ciudad.addBloque(new BloqueIndustrial(pos)));
    }

    @Test
    void testInsercionFueraDeLimitesLanzaError() {
        assertThrows(PosicionFueraDeLimitesException.class,
                () -> ciudad.addBloque(new BloqueEnergia(new Posicion(99, 99))));
    }

    @Test
    void testInsercionBloqueNuloLanzaError() {
        assertThrows(BloqueNuloException.class,
                () -> ciudad.addBloque(null));
    }

    @Test
    void testListadoBloquesCorrecto() {
        Bloque b1 = new BloqueResidencial(new Posicion(0, 0));
        Bloque b2 = new BloqueEnergia(new Posicion(1, 1));
        ciudad.addBloque(b1);
        ciudad.addBloque(b2);
        List<Bloque> lista = ciudad.listarBloques();
        assertEquals(2, lista.size());
        assertTrue(lista.contains(b1));
        assertTrue(lista.contains(b2));
    }

    @Test
    void testConteoPorTipoCorrecto() {
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 1)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 0)));
        assertEquals(2, ciudad.contarBloquesPorTipo(TipoBloque.RESIDENCIAL));
        assertEquals(1, ciudad.contarBloquesPorTipo(TipoBloque.ENERGIA));
        assertEquals(0, ciudad.contarBloquesPorTipo(TipoBloque.INDUSTRIAL));
    }

    // BLOQUE E: ACTIVACIÓN Y DESACTIVACIÓN

    @Test
    void testActivarBloqueExistente() {
        Posicion pos = new Posicion(0, 0);
        ciudad.addBloque(new BloqueResidencial(pos));
        ciudad.desactivarBloque(pos);
        assertFalse(ciudad.getBloque(0, 0).estaActivo());
        ciudad.activarBloque(pos);
        assertTrue(ciudad.getBloque(0, 0).estaActivo());
    }

    @Test
    void testActivarEnCeldaVaciaLanzaError() {
        assertThrows(CeldaVaciaException.class,
                () -> ciudad.activarBloque(new Posicion(0, 0)));
    }

    @Test
    void testDesactivarEnCeldaVaciaLanzaError() {
        assertThrows(CeldaVaciaException.class,
                () -> ciudad.desactivarBloque(new Posicion(0, 0)));
    }

    @Test
    void testHayBloquesActivosCorrecto() {
        assertFalse(ciudad.hayBloquesActivos());
        Posicion pos = new Posicion(0, 0);
        ciudad.addBloque(new BloqueResidencial(pos));
        assertTrue(ciudad.hayBloquesActivos());
        ciudad.desactivarBloque(pos);
        assertFalse(ciudad.hayBloquesActivos()); // hay bloque pero inactivo
    }

    @Test
    void testListadoBloquesActivosTrasVariosCambios() {
        Bloque b1 = new BloqueResidencial(new Posicion(0, 0));
        Bloque b2 = new BloqueEnergia(new Posicion(1, 1));
        Bloque b3 = new BloqueIndustrial(new Posicion(2, 2));
        ciudad.addBloque(b1);
        ciudad.addBloque(b2);
        ciudad.addBloque(b3);
        assertEquals(3, ciudad.listarBloquesActivos().size());

        ciudad.desactivarBloque(new Posicion(1, 1));
        List<Bloque> activos = ciudad.listarBloquesActivos();
        assertEquals(2, activos.size());
        assertFalse(activos.contains(b2));

        ciudad.activarBloque(new Posicion(1, 1));
        assertEquals(3, ciudad.listarBloquesActivos().size());
    }

    @Test
    void testActivarNoAlteraTipoNiPosicion() {
        Posicion pos = new Posicion(0, 0);
        ciudad.addBloque(new BloqueResidencial(pos));
        ciudad.desactivarBloque(pos);
        ciudad.activarBloque(pos);
        assertEquals(TipoBloque.RESIDENCIAL, ciudad.getBloque(0, 0).getTipo());
        assertEquals(pos, ciudad.getBloque(0, 0).getPosicion());
    }


    // BLOQUE F: EXPANSIÓN

    @Test
    void testExpansionValidaCambiaCapacidad() {
        ciudad.expandir(10, 10);
        assertEquals(10, ciudad.getFilas());
        assertEquals(10, ciudad.getColumnas());
        assertEquals(100, ciudad.getCapacidadMaxima());
    }

    @Test
    void testExpansionConservaBloques() {
        // Este es el test más crítico de la expansión
        Bloque b1 = new BloqueResidencial(new Posicion(0, 0));
        Bloque b2 = new BloqueEnergia(new Posicion(1, 1));
        Bloque b3 = new BloqueIndustrial(new Posicion(4, 4)); // esquina original
        ciudad.addBloque(b1);
        ciudad.addBloque(b2);
        ciudad.addBloque(b3);

        ciudad.expandir(10, 10);

        assertEquals(b1, ciudad.getBloque(0, 0), "b1 debe seguir en (0,0)");
        assertEquals(b2, ciudad.getBloque(1, 1), "b2 debe seguir en (1,1)");
        assertEquals(b3, ciudad.getBloque(4, 4), "b3 debe seguir en (4,4) — esquina del tablero original");
    }

    @Test
    void testExpansionNoAlteraConteo() {
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.addBloque(new BloqueEnergia(new Posicion(1, 1)));
        assertEquals(2, ciudad.contarBloques());
        ciudad.expandir(10, 10);
        assertEquals(2, ciudad.contarBloques(),
                "El número de bloques no cambia al expandir");
    }

    @Test
    void testExpansionNuevasCeldasVacias() {
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.expandir(10, 10);
        assertNull(ciudad.getBloque(8, 8), "Las nuevas celdas deben estar vacías");
        assertNull(ciudad.getBloque(9, 9));
        assertNull(ciudad.getBloque(5, 0));
    }

    @Test
    void testExpansionConDimensionesMenoresLanzaError() {
        assertThrows(ExpansionCiudadException.class,
                () -> ciudad.expandir(3, 3));
        assertThrows(ExpansionCiudadException.class,
                () -> ciudad.expandir(10, 3)); // una dimensión menor basta
    }

    @Test
    void testExpansionConDimensionesIgualesLanzaError() {
        assertThrows(ExpansionCiudadException.class,
                () -> ciudad.expandir(5, 5),
                "Expandir a las mismas dimensiones debe lanzar ExpansionCiudadException");
    }

    @Test
    void testExpansionPorEncimaDeLimiteGlobalLanzaError() {
        assertThrows(ExpansionCiudadException.class,
                () -> ciudad.expandir(200, 200));
    }

    @Test
    void testExpansionSuperandoMaximoExpansionesLanzaError() {
        // Hacemos 5 expansiones (el máximo)
        Ciudad c = new Ciudad("Expand", 5, 5);
        c.expandir(15, 15);
        c.expandir(25, 25);
        c.expandir(35, 35);
        c.expandir(45, 45);
        c.expandir(55, 55);
        // La 6ª debe fallar
        assertThrows(ExpansionCiudadException.class,
                () -> c.expandir(65, 65));
    }

    @Test
    void testExpansionRecalculaTipoEstructural() {
        assertEquals(TipoEstructuralCiudad.PEQUENA, ciudad.getTipoEstructural());
        ciudad.expandir(50, 50); // 2500 → GRANDE
        assertEquals(TipoEstructuralCiudad.GRANDE, ciudad.getTipoEstructural());
    }

    @Test
    void testExpansionMantieneEstadoActivacion() {
        Posicion p0 = new Posicion(0, 0);
        Posicion p1 = new Posicion(1, 1);
        ciudad.addBloque(new BloqueResidencial(p0)); // activo
        ciudad.addBloque(new BloqueEnergia(p1));     // lo desactivamos
        ciudad.desactivarBloque(p1);

        ciudad.expandir(10, 10);

        assertTrue(ciudad.getBloque(0, 0).estaActivo(),
                "Bloque activo debe seguir activo tras expansión");
        assertFalse(ciudad.getBloque(1, 1).estaActivo(),
                "Bloque inactivo debe seguir inactivo tras expansión");
    }

    @Test
    void testExpansionPermiteInsertarEnNuevasCeldas() {
        ciudad.expandir(10, 10);
        Bloque nuevo = new BloqueTransporte(new Posicion(8, 8));
        assertDoesNotThrow(() -> ciudad.addBloque(nuevo));
        assertEquals(nuevo, ciudad.getBloque(8, 8));
    }

    @Test
    void testNoBloquesDuplicadosTrasExpansion() {
        ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        ciudad.expandir(10, 10);
        // Intentar sobreescribir la misma celda debe seguir fallando
        assertThrows(CeldaOcupadaException.class,
                () -> ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 0))));
        assertEquals(1, ciudad.contarBloques());
    }

    @Test
    void testFlujoCompleto() {
        // 1. Ciudad pequeña
        Ciudad c = new Ciudad("Flujo", 5, 5);
        assertEquals(TipoEstructuralCiudad.PEQUENA, c.getTipoEstructural());

        // 2. Añadir bloques de distintos tipos
        c.addBloque(new BloqueResidencial(new Posicion(0, 0)));
        c.addBloque(new BloqueEnergia(new Posicion(1, 1)));
        assertEquals(2, c.contarBloques());

        // 3. Verificar que los métodos funcionales responden correctamente
        assertTrue(c.getBloque(0, 0).getConsumoEnergetico() > 0);
        assertTrue(c.getBloque(1, 1).getProduccionEnergia() > 0);
        assertTrue(c.getBloque(0, 0).getDemandaServicios()  > 0);

        // 4. Desactivar uno y comprobar lista de activos
        c.desactivarBloque(new Posicion(1, 1));
        assertEquals(1, c.listarBloquesActivos().size());

        // 5. Comprobar densidad: 2/25 = 0.08
        assertEquals(0.08, c.getDensidad(), 0.001);

        // 6. Expandir y verificar conservación
        c.expandir(10, 10);
        assertEquals(100, c.getCapacidadMaxima());
        assertEquals(2, c.contarBloques());

        // 7. Insertar en zona nueva y comprobar densidad: 3/100 = 0.03
        c.addBloque(new BloqueIndustrial(new Posicion(9, 9)));
        assertEquals(3, c.contarBloques());
        assertEquals(0.03, c.getDensidad(), 0.001);

        // 8. Resumen estructural funciona
        ResumenEstructuralCiudad resumen = c.getResumenEstructural();
        assertNotNull(resumen);
        assertEquals("Flujo", resumen.getNombreCiudad());
        assertEquals(3, resumen.getOcupacionActual());
    }
}
