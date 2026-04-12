package synthcity.modulo_2;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import synthcity.modulo_1.Bloque;
import synthcity.modulo_1.Ciudad;
import synthcity.modulo_1.TipoBloque;

public class SimuladorCiudad {

    //  Metodo principal
    public ResultadoSimulacion simular(Ciudad ciudad) {

        // PASO 1: Protegerse ante ciudad nula
        validarEntrada(ciudad);

        // PASO 2: Leer información estructural por API pública de M1
        String nombreCiudad = ciudad.getNombre();
        int filas            = ciudad.getFilas();
        int columnas         = ciudad.getColumnas();
        int capacidadMaxima  = ciudad.capacidadMaxima(); // M1 no usa "get"

        // PASO 3: Obtener todos los bloques — nunca tocamos el tablero interno
        List<Bloque> bloques = ciudad.listarBloques();

        // PASO 4: Obtener bloques activos por API pública de M1
        List<Bloque> bloquesActivos = ciudad.listarBloquesActivos();

        // PASO 5: Calcular totales a partir de los tamaños de las listas
        int totalBloques   = bloques.size();
        int totalActivos   = bloquesActivos.size();
        int totalInactivos = totalBloques - totalActivos;

        // PASO 6: Delegar conteo por tipo — implementado por P3
        Map<TipoBloque, Integer> conteoPorTipo = calcularConteosPorTipo(bloques);

        // PASO 7: Determinar estado técnico del ciclo
        EstadoSimulacion estado = determinarEstadoSimulacion(totalBloques, totalActivos);

        // PASOS 8-9: Construir y devolver resultado
        return construirResultadoSimulacion(
                nombreCiudad, filas, columnas, capacidadMaxima,
                totalBloques, totalActivos, totalInactivos,
                conteoPorTipo, estado
        );
    }

    // Validación de entrada
    private void validarEntrada(Ciudad ciudad) {
        if (ciudad == null) {
            throw new CiudadNulaException("No se puede simular una ciudad nula.");
        }
    }

    // ─── Determinar estado técnico del ciclo ──────────────────────────────────
    private EstadoSimulacion determinarEstadoSimulacion(int bloquesTotales, int bloquesActivos) {
        if (bloquesTotales == 0) {
            return EstadoSimulacion.CIUDAD_VACIA;
        }
        if (bloquesActivos == 0) {
            return EstadoSimulacion.SIN_BLOQUES_ACTIVOS;
        }
        return EstadoSimulacion.EJECUTADA;
    }

    // Ensamblar el resultado
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

    // Conteo por tipo — integrado de P3
    public Map<TipoBloque, Integer> calcularConteosPorTipo(List<Bloque> bloques) {
        Map<TipoBloque, Integer> conteos = new EnumMap<>(TipoBloque.class);

        // Inicializar todos los tipos a 0
        for (TipoBloque tipo : TipoBloque.values()) {
            conteos.put(tipo, 0);
        }

        // Recorrer y contar — defensivo ante bloques nulos dentro de la lista
        if (bloques != null) {
            for (Bloque bloque : bloques) {
                if (bloque != null) {
                    TipoBloque tipo = bloque.getTipo();
                    if (tipo != null) {
                        conteos.put(tipo, conteos.get(tipo) + 1);
                    }
                }
            }
        }

        return conteos;
    }
}