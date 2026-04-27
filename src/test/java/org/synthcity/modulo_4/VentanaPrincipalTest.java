package org.synthcity.modulo_4;

import javafx.stage.Window;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VentanaPrincipalTest extends JavaFxTestBase {

    @Test
    void ventanaSeAbreCorrectamente() {
        ejecutarEnFxAndWait(() -> {
            PanelCiudad panelCiudad = new PanelCiudad();
            PanelResumenSistema panelResumen = new PanelResumenSistema();

            VentanaPrincipal ventana = new VentanaPrincipal(panelCiudad, panelResumen);

            assertDoesNotThrow(ventana::mostrar);
            assertFalse(Window.getWindows().isEmpty());

            new ArrayList<>(Window.getWindows()).forEach(Window::hide);
        });
    }
}