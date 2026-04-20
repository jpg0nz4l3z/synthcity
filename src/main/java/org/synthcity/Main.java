package org.synthcity;

import java.util.EnumMap;
import java.util.Map;

import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_3.MetricaCiudad;
import org.synthcity.modulo_3.NivelEvaluacion;
import org.synthcity.modulo_3.ResultadoEvaluacion;
/*import org.synthcity.modulo_4.PresentadorCiudad;
import org.synthcity.modulo_4.SalidaTexto;

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

        ResultadoSimulacion simulacion = new ResultadoSimulacion(
                "NeoMadrid",
                2,
                5,
                10,
                10,
                7,
                3,
                conteo,
                EstadoSimulacion.EJECUTADA
        );

        MetricaCiudad metrica = new MetricaCiudad(simulacion);

        return new ResultadoEvaluacion(
                "NeoMadrid",
                metrica,
                NivelEvaluacion.FUNCIONAL,
                "La ciudad presenta un nivel suficiente de actividad para considerarse operativa dentro del alcance actual del sistema."
        );
    }

    private static ResultadoEvaluacion crearResultadoSinDatos() {
        Map<TipoBloque, Integer> conteo = crearMapaBase();

        ResultadoSimulacion simulacion = new ResultadoSimulacion(
                "CiudadVacia",
                0,
                0,
                0,
                0,
                0,
                0,
                conteo,
                EstadoSimulacion.EJECUTADA
        );

        MetricaCiudad metrica = new MetricaCiudad(simulacion);

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

        ResultadoSimulacion simulacion = new ResultadoSimulacion(
                "CiudadCritica",
                2,
                3,
                6,
                5,
                0,
                5,
                conteo,
                EstadoSimulacion.EJECUTADA
        );

        MetricaCiudad metrica = new MetricaCiudad(simulacion);

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
}*/
