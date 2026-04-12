package synthcity.modulo_2;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import synthcity.modulo_1.Bloque;
import synthcity.modulo_1.Ciudad;
import synthcity.modulo_1.TipoBloque;

public class SimuladorCiudad {

    public ResultadoSimulacion simular(Ciudad ciudad) {

        // PASO 1: Protegerse ante ciudad nula
        validarEntrada(ciudad);

        // PASO 2: Leer información estructural por API pública de M1
        String nombreCiudad = ciudad.getNombre();
        int filas = ciudad.getFilas();
        int columnas = ciudad.getColumnas();
        int capacidadMaxima = ciudad.capacidadMaxima(); // Sin "get", así lo definió M1

        // PASO 3: Obtener todos los bloques — nunca tocamos el tablero interno
        List<Bloque> bloques = ciudad.listarBloques();

        // PASO 4: Obtener bloques activos por API pública
        List<Bloque> bloquesActivos = ciudad.listarBloquesActivos();

        // PASO 5: Calcular totales a partir de los tamaños de las listas
        int totalBloques = bloques.size();
        int totalActivos = bloquesActivos.size();
        int totalInactivos = totalBloques - totalActivos;

        // PASO 6: Delegar conteo por tipo
        Map<TipoBloque, Integer> conteoPorTipo = calcularConteosPorTipo(bloques);

        // PASO 7: Determinar estado técnico
        EstadoSimulacion estado = determinarEstadoSimulacion(totalBloques, totalActivos);

        // PASOS 8-9: Construir y devolver resultado
        return construirResultadoSimulacion(
                nombreCiudad, filas, columnas, capacidadMaxima,
                totalBloques, totalActivos, totalInactivos,
                conteoPorTipo, estado
        );
    }

    private void validarEntrada(Ciudad ciudad) {
        if (ciudad == null) {
            throw new CiudadNulaException("No se puede simular una ciudad nula.");
        }
    }

    private EstadoSimulacion determinarEstadoSimulacion(int bloquesTotales, int bloquesActivos) {
        if (bloquesTotales == 0) {
            return EstadoSimulacion.CIUDAD_VACIA;
        }
        if (bloquesActivos == 0) {
            return EstadoSimulacion.SIN_BLOQUES_ACTIVOS;
        }
        return EstadoSimulacion.EJECUTADA;
    }

    private ResultadoSimulacion construirResultadoSimulacion(
            String nombreCiudad, int filas, int columnas, int capacidadMaxima,
            int bloquesTotales, int bloquesActivos, int bloquesInactivos,
            Map<TipoBloque, Integer> conteoPorTipo, EstadoSimulacion estado) {

        return new ResultadoSimulacion(
                nombreCiudad, filas, columnas, capacidadMaxima,
                bloquesTotales, bloquesActivos, bloquesInactivos,
                conteoPorTipo, estado
        );
    }

    private Map<TipoBloque, Integer> calcularConteosPorTipo(List<Bloque> bloques) {
        Map<TipoBloque, Integer> conteos = new EnumMap<>(TipoBloque.class);

        // Inicializar todos los tipos a 0 — obligatorio para que M3 no reciba nulls
        for (TipoBloque tipo : TipoBloque.values()) {
            conteos.put(tipo, 0);
        }

        // Contar cada bloque por su tipo
        for (Bloque bloque : bloques) {
            TipoBloque tipo = bloque.getTipo();
            conteos.put(tipo, conteos.get(tipo) + 1);
        }

        return conteos;
    }
}