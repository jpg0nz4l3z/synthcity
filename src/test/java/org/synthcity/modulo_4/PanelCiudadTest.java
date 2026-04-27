package org.synthcity.modulo_4;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.bloques.BloqueEnergia;
import org.synthcity.modulo_1.bloques.BloqueIndustrial;
import org.synthcity.modulo_1.bloques.BloqueResidencial;
import org.synthcity.modulo_1.bloques.BloqueServicios;
import org.synthcity.modulo_1.bloques.BloqueTransporte;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PanelCiudadTest extends JavaFxTestBase {

    @Test
    void gridSeDibujaConDimensionesCorrectas() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("Grid", 3, 4);
            PanelCiudad panel = new PanelCiudad();

            panel.mostrarCiudad(ciudad);

            assertEquals(12, panel.getChildren().size());
        });
    }

    @Test
    void celdasVaciasYOcupadasSeDistinguen() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("Celdas", 2, 2);
            ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));

            PanelCiudad panel = new PanelCiudad();
            panel.mostrarCiudad(ciudad);

            long celdasConTexto = panel.getChildren().stream()
                    .filter(n -> n instanceof StackPane)
                    .map(n -> (StackPane) n)
                    .filter(sp -> contieneTexto(sp))
                    .count();

            assertEquals(1, celdasConTexto);
            assertEquals(4, panel.getChildren().size());
        });
    }

    @Test
    void tiposDeBloqueSeRepresentanConInicialCorrecta() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("Tipos", 1, 5);
            ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
            ciudad.addBloque(new BloqueEnergia(new Posicion(0, 1)));
            ciudad.addBloque(new BloqueIndustrial(new Posicion(0, 2)));
            ciudad.addBloque(new BloqueServicios(new Posicion(0, 3)));
            ciudad.addBloque(new BloqueTransporte(new Posicion(0, 4)));

            PanelCiudad panel = new PanelCiudad();
            panel.mostrarCiudad(ciudad);

            List<String> textos = panel.getChildren().stream()
                    .filter(n -> n instanceof StackPane)
                    .flatMap(n -> ((StackPane) n).getChildren().stream())
                    .filter(n -> n instanceof Text)
                    .map(n -> ((Text) n).getText())
                    .toList();

            assertTrue(textos.contains("R"));
            assertTrue(textos.contains("E"));
            assertTrue(textos.contains("I"));
            assertTrue(textos.contains("S"));
            assertTrue(textos.contains("T"));
        });
    }

    @Test
    void bloqueInactivoSeMuestraConOpacidadReducida() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("Inactivo", 1, 1);
            ciudad.addBloque(new BloqueResidencial(new Posicion(0, 0)));
            ciudad.desactivarBloque(new Posicion(0, 0));

            PanelCiudad panel = new PanelCiudad();
            panel.mostrarCiudad(ciudad);

            StackPane celda = (StackPane) panel.getChildren().get(0);

            assertEquals(0.6, celda.getOpacity(), 0.0001);
        });
    }

    @Test
    void limpiarVaciaElGrid() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("Limpiar", 2, 2);
            PanelCiudad panel = new PanelCiudad();

            panel.mostrarCiudad(ciudad);
            assertFalse(panel.getChildren().isEmpty());

            panel.limpiar();

            assertTrue(panel.getChildren().isEmpty());
        });
    }

    @Test
    void refrescarRedibujaCiudadActual() {
        ejecutarEnFxAndWait(() -> {
            Ciudad ciudad = new Ciudad("Refrescar", 2, 3);
            PanelCiudad panel = new PanelCiudad();

            panel.mostrarCiudad(ciudad);
            panel.limpiar();

            assertEquals(0, panel.getChildren().size());

            panel.refrescar();

            assertEquals(6, panel.getChildren().size());
        });
    }

    private boolean contieneTexto(StackPane stackPane) {
        return stackPane.getChildren().stream().anyMatch(n -> n instanceof Text);
    }
}