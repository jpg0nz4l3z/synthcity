package org.synthcity.modulo_4;

import org.synthcity.modulo_2.EstadoCiclo;
import org.synthcity.modulo_2.EstadoSimulacion;
import org.synthcity.modulo_2.MotivoParadaSimulacion;
import org.synthcity.modulo_2.ResultadoSimulacion;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_1.TipoEstructuralCiudad;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class PruebasPanelEvolucion {

    public static void main(String[] args) {
        pruebaA_cincoCirclos();
        pruebaB_colapsoEnergetico();
        pruebaC_historialVacio();
        pruebaD_tendenciaNegativa();
        System.out.println("\n Todas las pruebas ejecutadas sin errores de compilación.");
    }


    static void pruebaA_cincoCirclos() {
        System.out.println(" PRUEBA A: 5 ciclos normales ");

        List<EstadoCiclo> ciclos = List.of(
                ciclo(1, 100, 80, 10,  10,  0.8, 0.7, 0.6, 0.5, false),
                ciclo(2,  95, 82, 20,  30,  0.78, 0.68, 0.6, 0.5, false),
                ciclo(3,  90, 84, 30,  60,  0.76, 0.66, 0.59, 0.5, false),
                ciclo(4,  88, 86, 40,  95,  0.74, 0.64, 0.58, 0.5, false),
                ciclo(5,  85, 88, 50, 135,  0.72, 0.62, 0.57, 0.5, false)
        );

        ResultadoSimulacion historial = construirResultado(
                "CiudadTest", 10, 10, ciclos,
                MotivoParadaSimulacion.CICLOS_COMPLETADOS, false
        );

        System.out.println("Ciclos ejecutados: " + historial.getCiclosEjecutados());
        assert historial.getCiclosEjecutados() == 5 : "ERROR: debe tener 5 ciclos";

        System.out.println("Motivo parada: " + historial.getMotivoParada());
        assert historial.getMotivoParada() == MotivoParadaSimulacion.CICLOS_COMPLETADOS;

        System.out.println("Estabilidad media: " + historial.getEstabilidadMedia());
        System.out.println(" Prueba A OK");
    }


    static void pruebaB_colapsoEnergetico() {
        System.out.println(" PRUEBA B: colapso energético ");

        List<EstadoCiclo> ciclos = List.of(
                ciclo(1, 50, 80, 10,  10,  0.5, 0.4, 0.4, 0.3, false),
                ciclo(2, 40, 85, 20,  30,  0.4, 0.3, 0.35, 0.25, false),
                ciclo(3, 30, 90, 30,  60,  0.3, 0.2, 0.3, 0.2, true)
        );

        ResultadoSimulacion historial = construirResultado(
                "CiudadColapsada", 8, 8, ciclos,
                MotivoParadaSimulacion.COLAPSO_ENERGETICO, false
        );

        System.out.println("Motivo parada: " + historial.getMotivoParada());
        assert historial.getMotivoParada() == MotivoParadaSimulacion.COLAPSO_ENERGETICO;
        assert historial.isNecesidadExpansionDetectada();

        System.out.println("Necesidad expansión detectada: " + historial.isNecesidadExpansionDetectada());
        System.out.println(" Prueba B OK");
    }


    static void pruebaC_historialVacio() {
        System.out.println(" PRUEBA C: ciudad vacía ");


        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque t : TipoBloque.values()) conteo.put(t, 0);

        ResultadoSimulacion historial = new ResultadoSimulacion(
                "CiudadVacia", 5, 5, 25,
                0, 0, 0,
                conteo,
                EstadoSimulacion.CIUDAD_VACIA,
                0.0,
                TipoEstructuralCiudad.PEQUENA,
                0, 0, 0, 0, 0, 0, 0, 0,
                0.0, 0.0, 0.0, 0.0
        );

        System.out.println("Ciudad vacía: " + historial.ciudadEstaVacia());
        assert historial.ciudadEstaVacia();
        assert historial.getCiclos().isEmpty();

        System.out.println("Ciclos: " + historial.getCiclosEjecutados());
        System.out.println(" Prueba C OK — sin errores con historial vacío");
    }



    static void pruebaD_tendenciaNegativa() {
        System.out.println(" PRUEBA D: tendencia negativa (deterioro) ");

        List<EstadoCiclo> ciclos = List.of(
                ciclo(1, 80, 70, 5,   5,  0.9, 0.85, 0.8, 0.7, false),
                ciclo(2, 75, 72, 10,  15, 0.75, 0.7, 0.7, 0.6, false),
                ciclo(3, 65, 74, 20,  35, 0.6,  0.55, 0.6, 0.5, false),
                ciclo(4, 55, 76, 30,  65, 0.45, 0.4, 0.5, 0.4, false)
        );

        ResultadoSimulacion historial = construirResultado(
                "CiudadDeteriorada", 10, 10, ciclos,
                MotivoParadaSimulacion.CICLOS_COMPLETADOS, false
        );

        double tendencia = historial.getTendenciaEstabilidad();
        System.out.println("Tendencia estabilidad: " + tendencia);
        assert tendencia < 0 : "ERROR: la tendencia debe ser negativa";

        System.out.println(" Prueba D OK — tendencia negativa confirmada");
    }



    private static EstadoCiclo ciclo(int num,
                                     int energProd, int consumo,
                                     int contamCiclo, int contamAcum,
                                     double estabilidad, double bienestar,
                                     double cobertura, double eficiencia,
                                     boolean expansion) {
        return new EstadoCiclo(
                num,
                energProd,
                consumo,
                energProd - consumo,
                50, (int)(cobertura * 50),
                cobertura,
                eficiencia,
                10,
                5,
                contamCiclo,
                contamAcum,
                bienestar,
                estabilidad,
                0.5,
                expansion,
                EstadoSimulacion.EJECUTADA
        );
    }

    private static ResultadoSimulacion construirResultado(
            String nombre, int filas, int columnas,
            List<EstadoCiclo> ciclos,
            MotivoParadaSimulacion motivo,
            boolean historialReiniciado) {

        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque t : TipoBloque.values()) conteo.put(t, 1);

        int totalBloques = TipoBloque.values().length;

        return new ResultadoSimulacion(
                nombre, filas, columnas, filas * columnas,
                totalBloques, totalBloques, 0,
                conteo,
                EstadoSimulacion.EJECUTADA,
                0.3,
                TipoEstructuralCiudad.PEQUENA,
                80, 70, 10,
                50, 40, 10, 5, 15,
                0.7, 0.75, 1.1, 0.8,
                ciclos, motivo, false, historialReiniciado
        );
    }
}