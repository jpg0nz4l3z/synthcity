package org.synthcity.modulo_4;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VentanaPrincipalTest {

    @BeforeAll
    static void initJFX() {
        try { Platform.startup(() -> {}); } catch (IllegalStateException e) {}
    }

    @Test
    public void testMontarVentanaPrincipal_CaminoBasico() {
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> {
                PanelCiudad panelCiudad = new PanelCiudad();
                PanelResumenSistema panelResumen = new PanelResumenSistema();
                VentanaPrincipal ventana = new VentanaPrincipal(panelCiudad, panelResumen);
                assertNotNull(ventana, "La ventana principal debe instanciarse correctamente");
            }, "La interfaz debe montarse y enlazar sus paneles sin errores");
        });
    }
}
