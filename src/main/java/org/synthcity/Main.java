package org.synthcity;

import java.util.EnumMap;
import java.util.Map;

import org.synthcity.modulo_4.PresentadorCiudad;
import org.synthcity.modulo_4.SalidaTexto;
import org.synthcity.modulo_4.stubs.MetricaCiudad;
import org.synthcity.modulo_4.stubs.NivelEvaluacion;
import org.synthcity.modulo_4.stubs.ResultadoEvaluacion;
import org.synthcity.modulo_4.stubs.TipoBloque;

public class Main {

    public static void main(String[] args) {
        probarEscenario("FUNCIONAL", crearResultadoFuncional());
        probarEscenario("SIN_DATOS", crearResultadoSinDatos());
        probarEscenario("CRITICO", crearResultadoCritico());
        probarErrorControlado();
    }

    private static void probarEscenario(String nombreEscenario, ResultadoEvaluacion resultado) {
        System.out.println();
        System.out.println("========== ESCENARIO " + nombreEscenario + " ==========");
        System.out.println();

        PresentadorCiudad presentador = new PresentadorCiudad();
        SalidaTexto salida = presentador.presentar(resultado);

        System.out.println(salida);
    }

    private static void probarErrorControlado() {
        System.out.println();
        System.out.println("========== ESCENARIO ERROR CONTROLADO ==========");
        System.out.println();

        try {
            PresentadorCiudad presentador = new PresentadorCiudad();
            presentador.presentar(null);
        } catch (Exception e) {
            System.out.println("Error controlado capturado: " + e.getMessage());
        }
    }

    private static ResultadoEvaluacion crearResultadoFuncional() {
        Map<TipoBloque, Integer> conteo = crearMapaBase();

        conteo.put(TipoBloque.RESIDENCIAL, 5);
        conteo.put(TipoBloque.ENERGIA, 2);
        conteo.put(TipoBloque.INDUSTRIAL, 1);
        conteo.put(TipoBloque.SERVICIOS, 1);
        conteo.put(TipoBloque.TRANSPORTE, 1);

        MetricaCiudad metrica = new MetricaCiudad(
                10,
                7,
                3,
                0.70,
                0.30,
                conteo
        );

        return new ResultadoEvaluacion(
                "NeoMadrid",
                metrica,
                NivelEvaluacion.FUNCIONAL,
                "La ciudad presenta un nivel suficiente de actividad para considerarse operativa dentro del alcance actual del sistema."
        );
    }

    private static ResultadoEvaluacion crearResultadoSinDatos() {
        Map<TipoBloque, Integer> conteo = crearMapaBase();

        MetricaCiudad metrica = new MetricaCiudad(
                0,
                0,
                0,
                0.0,
                0.0,
                conteo
        );

        return new ResultadoEvaluacion(
                "CiudadVacia",
                metrica,
                NivelEvaluacion.SIN_DATOS,
                "La ciudad no contiene bloques suficientes para ser evaluada."
        );
    }

    private static ResultadoEvaluacion crearResultadoCritico() {
        Map<TipoBloque, Integer> conteo = crearMapaBase();

        conteo.put(TipoBloque.RESIDENCIAL, 3);
        conteo.put(TipoBloque.ENERGIA, 1);
        conteo.put(TipoBloque.SERVICIOS, 1);

        MetricaCiudad metrica = new MetricaCiudad(
                5,
                0,
                5,
                0.0,
                1.0,
                conteo
        );

        return new ResultadoEvaluacion(
                "CiudadCritica",
                metrica,
                NivelEvaluacion.CRITICO,
                "La ciudad contiene bloques, pero ninguno se encuentra activo."
        );
    }

    private static Map<TipoBloque, Integer> crearMapaBase() {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);

        for (TipoBloque tipo : TipoBloque.values()) {
            conteo.put(tipo, 0);
        }

        return conteo;
    }
}
