// Ubicación: src/test/java/org/synthcity/modulo_3/EvaluadorCiudadTest.java
package org.synthcity.modulo_3;

import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_1.TipoBloque;
import java.util.HashMap;
import java.util.Map;

public class EvaluadorCiudadTest {

    public static void main(String[] args) {
        EvaluadorCiudad evaluador = new EvaluadorCiudad();
        Map<TipoBloque, Integer> mapaConteo = new HashMap<>();

        System.out.println("INICIANDO BATERÍA DE PRUEBAS DE SEGURIDAD (PERSONA 1 + 2)\n");

        // TEST 1: Protección contra nulos
        try {
            evaluador.evaluar(null);
            System.out.println("TEST 1 FALLADO: El evaluador aceptó un objeto nulo.");
        } catch (ResultadoSimulacionInvalidoException e) {
            System.out.println("TEST 1 PASADO: Simulación nula bloqueada (" + e.getMessage() + ")");
        }

        // TEST 2: Protección contra números negativos
        try {
            ResultadoSimulacion simNegativa = new ResultadoSimulacion("Ciudad", -5, 0, 0, 100, mapaConteo);
            evaluador.evaluar(simNegativa);
            System.out.println("TEST 2 FALLADO: El evaluador aceptó bloques negativos.");
        } catch (ResultadoSimulacionInvalidoException e) {
            System.out.println("TEST 2 PASADO: Negativos bloqueados (" + e.getMessage() + ")");
        }

        // TEST 3: Protección contra matemáticas rotas (Activos + Inactivos != Total)
        try {
            // Total 10, pero metemos 8 activos y 8 inactivos (imposible)
            ResultadoSimulacion simIncoherente = new ResultadoSimulacion("Ciudad", 10, 8, 8, 100, mapaConteo);
            evaluador.evaluar(simIncoherente);
            System.out.println("TEST 3 FALLADO: El evaluador aceptó matemáticas incoherentes.");
        } catch (ResultadoSimulacionInvalidoException e) {
            System.out.println("TEST 3 PASADO: Matemáticas incoherentes bloqueadas (" + e.getMessage() + ")");
        }

        // TEST 4: Protección contra mapa nulo
        try {
            ResultadoSimulacion simSinMapa = new ResultadoSimulacion("Ciudad", 10, 5, 5, 100, null);
            evaluador.evaluar(simSinMapa);
            System.out.println("TEST 4 FALLADO: El evaluador aceptó un mapa nulo.");
        } catch (ResultadoSimulacionInvalidoException e) {
            System.out.println("TEST 4 PASADO: Mapa nulo bloqueado (" + e.getMessage() + ")");
        }

        // TEST 5: Ejecución exitosa y conexión con MetricaCiudad
        try {
            System.out.println("\n--- Probando integración con MetricaCiudad ---");
            ResultadoSimulacion simCorrecta = new ResultadoSimulacion("Utopia", 100, 60, 40, 200, mapaConteo);
            evaluador.evaluar(simCorrecta);
            // Si el código llega aquí y se imprime el mensaje de la clase Evaluador, la integración es un éxito.
        } catch (Exception e) {
            System.out.println("TEST 5 FALLADO: Error inesperado con datos correctos -> " + e.getMessage());
        }
    }
}