package org.synthcity.modulo_1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultadoExpansionTest {

    @Test
    void resultadoExpansionAprobadaContieneDatosCorrectos() {
        ResultadoExpansion r = new ResultadoExpansion(
                true,
                10, 10,
                20, 20,
                TipoEstructuralCiudad.PEQUENA,
                TipoEstructuralCiudad.MEDIANA,
                null
        );

        assertTrue(r.isAprobada());
        assertEquals(10, r.getFilasAnteriores());
        assertEquals(20, r.getFilasNuevas());
        assertEquals(TipoEstructuralCiudad.PEQUENA, r.getTipoEstructuralAnterior());
        assertEquals(TipoEstructuralCiudad.MEDIANA, r.getTipoEstructuralNuevo());
    }

    @Test
    void resultadoExpansionDetectaCambioTipoEstructural() {
        ResultadoExpansion r = new ResultadoExpansion(
                true,
                10, 10,
                30, 30,
                TipoEstructuralCiudad.PEQUENA,
                TipoEstructuralCiudad.MEDIANA,
                null
        );

        assertTrue(r.cambioTipoEstructural());
    }

    @Test
    void resultadoExpansionRechazadaContieneMotivo() {
        ResultadoExpansion r = new ResultadoExpansion(
                false,
                50, 50,
                50, 50,
                TipoEstructuralCiudad.GRANDE,
                TipoEstructuralCiudad.GRANDE,
                "No permitido"
        );

        assertFalse(r.isAprobada());
        assertEquals("No permitido", r.getMotivoRechazo());
    }

    @Test
    void resultadoExpansionExponeTodosLosDatosNecesariosParaPersistirHistorial() {
        ResultadoExpansion r = new ResultadoExpansion(
                true,
                10,
                20,
                30,
                40,
                TipoEstructuralCiudad.PEQUENA,
                TipoEstructuralCiudad.MEDIANA,
                null
        );

        assertTrue(r.isAprobada());
        assertTrue(r.isExitosa());

        assertEquals(10, r.getFilasAnteriores());
        assertEquals(20, r.getColumnasAnteriores());
        assertEquals(30, r.getFilasNuevas());
        assertEquals(40, r.getColumnasNuevas());

        assertEquals(30, r.getNuevasFilas());
        assertEquals(40, r.getNuevasColumnas());

        assertEquals(TipoEstructuralCiudad.PEQUENA, r.getTipoEstructuralAnterior());
        assertEquals(TipoEstructuralCiudad.MEDIANA, r.getTipoEstructuralNuevo());
        assertTrue(r.cambioTipoEstructural());
        assertNull(r.getMotivoRechazo());
    }
}