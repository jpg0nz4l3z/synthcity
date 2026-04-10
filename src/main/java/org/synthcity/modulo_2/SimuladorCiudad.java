import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SimuladorCiudad {

    public ResultadoSimulacion simular(Ciudad ciudad) {

        validarEntrada(ciudad);

        String nombreCiudad = ciudad.getNombre();
        int filas = ciudad.getFilas();
        int columnas = ciudad.getColumnas();
        int capacidadMaxima = ciudad.capacidadMaxima();

        List<Bloque> bloques = ciudad.listarBloques();
        List<Bloque> bloquesActivos = ciudad.listarBloquesActivos();

        int totalBloques = bloques.size();
        int totalActivos = bloquesActivos.size();
        int totalInactivos = totalBloques - totalActivos;

        Map<TipoBloque, Integer> conteoPorTipo = calcularConteosPorTipo(bloques);

        EstadoSimulacion estado = determinarEstadoSimulacion(totalBloques, totalActivos);

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

        for (TipoBloque tipo : TipoBloque.values()) {
            conteos.put(tipo, 0);
        }

        for (Bloque bloque : bloques) {
            TipoBloque tipo = bloque.getTipo();
            conteos.put(tipo, conteos.get(tipo) + 1);
        }

        return conteos;
    }
}