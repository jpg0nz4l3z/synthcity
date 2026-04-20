package org.synthcity.modulo_1;

import java.util.ArrayList;
import java.util.List;

class PosicionFueraDeLimitesException extends RuntimeException {
    public PosicionFueraDeLimitesException(String msg) { super(msg); }
}

class CeldaOcupadaException extends RuntimeException {
    public CeldaOcupadaException(String msg) { super(msg); }
}

class CeldaVaciaException extends RuntimeException {
    public CeldaVaciaException(String msg) { super(msg); }
}

class BloqueNuloException extends RuntimeException {
    public BloqueNuloException(String msg) { super(msg); }
}

public class Ciudad {
    private static final int MAX_FILAS = 100;
    private static final int MAX_COLUMNAS = 100;
    private static final int MAX_EXPANSIONES = 5;
    private static final int INCREMENTO_EXPANSION = 10;

    private final String nombre;
    private int filas;
    private int columnas;
    private Bloque[][] tablero;
    private TipoEstructuralCiudad tipoEstructural;
    private int expansionesRealizadas;
    private final int maximoExpansiones;
    private final double umbralExpansion;

    public Ciudad(String nombre, int filas, int columnas) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ciudad es obligatorio.");
        }
        validarDimensionesIniciales(filas, columnas);

        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.tablero = new Bloque[filas][columnas];
        this.tipoEstructural = calcularTipoEstructural();
        this.expansionesRealizadas = 0;
        this.maximoExpansiones = MAX_EXPANSIONES;
        this.umbralExpansion = 0.80;
    }

    private void validarDimensionesIniciales(int filas, int columnas) {
        if (filas <= 0 || columnas <= 0) {
            throw new DimensionesInvalidasException("Las dimensiones tienen que ser mayores que 0.");
        }
        if (filas > MAX_FILAS || columnas > MAX_COLUMNAS) {
            throw new DimensionesInvalidasException(
                    "Las dimensiones no pueden superar " + MAX_FILAS + "x" + MAX_COLUMNAS + ".");
        }
    }

    private TipoEstructuralCiudad calcularTipoEstructural() {
        int capacidad = capacidadMaxima();
        if (capacidad <= 400) {
            return TipoEstructuralCiudad.PEQUENA;
        }
        if (capacidad <= 1600) {
            return TipoEstructuralCiudad.MEDIANA;
        }
        return TipoEstructuralCiudad.GRANDE;
    }

    public String getNombre() { return nombre; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public TipoEstructuralCiudad getTipoEstructural() { return tipoEstructural; }
    public int getExpansionesRealizadas() { return expansionesRealizadas; }
    public int getMaximoExpansiones() { return maximoExpansiones; }
    public int getCapacidadMaxima() { return capacidadMaxima(); }

    public Bloque getBloque(int fila, int columna) {
        validarPosicion(fila, columna);
        return tablero[fila][columna];
    }

    public boolean dentroLimites(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    public void validarPosicion(int fila, int columna) {
        if (!dentroLimites(fila, columna)) {
            throw new PosicionFueraDeLimitesException(
                    "Posición (" + fila + ", " + columna + ") fuera del tablero.");
        }
    }

    public boolean estaOcupada(int fila, int columna) {
        validarPosicion(fila, columna);
        return tablero[fila][columna] != null;
    }

    public int capacidadMaxima() {
        return filas * columnas;
    }

    public void addBloque(Bloque bloque) {
        if (bloque == null) {
            throw new BloqueNuloException("El bloque no puede ser nulo.");
        }

        Posicion pos = bloque.getPosicion();
        int fila = pos.getFila();
        int columna = pos.getColumna();

        validarPosicion(fila, columna);
        if (tablero[fila][columna] != null) {
            throw new CeldaOcupadaException("La casilla (" + fila + ", " + columna + ") ya está ocupada.");
        }

        tablero[fila][columna] = bloque;
    }

    public void removeBloque(int fila, int columna) {
        validarPosicion(fila, columna);
        if (tablero[fila][columna] == null) {
            throw new CeldaVaciaException(
                    "No hay un bloque que eliminar en la posición (" + fila + ", " + columna + ").");
        }
        tablero[fila][columna] = null;
    }

    public int getOcupacionActual() {
        return contarBloques();
    }

    public double getDensidad() {
        int capacidad = capacidadMaxima();
        if (capacidad == 0) {
            return 0.0;
        }
        return (double) getOcupacionActual() / capacidad;
    }

    public boolean estaProximaASaturacion() {
        return getDensidad() >= umbralExpansion;
    }

    public boolean hayBloquesActivos() {
        return !listarBloquesActivos().isEmpty();
    }

    public void activarBloque(Posicion pos) {
        Bloque bloque = obtenerBloqueExistente(pos, "activar");
        bloque.activar();
    }

    public void desactivarBloque(Posicion pos) {
        Bloque bloque = obtenerBloqueExistente(pos, "desactivar");
        bloque.desactivar();
    }

    private Bloque obtenerBloqueExistente(Posicion pos, String accion) {
        if (pos == null) {
            throw new IllegalArgumentException("La posición no puede ser nula.");
        }
        int fila = pos.getFila();
        int columna = pos.getColumna();
        validarPosicion(fila, columna);
        Bloque bloque = tablero[fila][columna];
        if (bloque == null) {
            throw new CeldaVaciaException(
                    "No hay bloque que " + accion + " en la posición (" + fila + ", " + columna + ").");
        }
        return bloque;
    }

    public List<Bloque> listarBloques() {
        List<Bloque> listaBloques = new ArrayList<>();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] != null) {
                    listaBloques.add(tablero[i][j]);
                }
            }
        }
        return listaBloques;
    }

    public List<Bloque> listarBloquesActivos() {
        List<Bloque> listaBloquesActivos = new ArrayList<>();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] != null && tablero[i][j].estaActivo()) {
                    listaBloquesActivos.add(tablero[i][j]);
                }
            }
        }
        return listaBloquesActivos;
    }

    public int contarBloques() {
        int contadorBloques = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] != null) {
                    contadorBloques++;
                }
            }
        }
        return contadorBloques;
    }

    public int contarBloquesPorTipo(TipoBloque tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo no existe.");
        }
        int numBloquesTipo = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] != null && tablero[i][j].getTipo() == tipo) {
                    numBloquesTipo++;
                }
            }
        }
        return numBloquesTipo;
    }

    public boolean estaVacia() {
        return contarBloques() == 0;
    }

    public boolean puedeExpandirse() {
        return expansionesRealizadas < maximoExpansiones
                && filas + INCREMENTO_EXPANSION <= MAX_FILAS
                && columnas + INCREMENTO_EXPANSION <= MAX_COLUMNAS;
    }

    public void expandir(int nuevasFilas, int nuevasColumnas) {
        if (nuevasFilas <= filas || nuevasColumnas <= columnas) {
            throw new ExpansionCiudadException(
                    "No se puede expandir la ciudad a " + nuevasFilas + "x" + nuevasColumnas
                            + " porque las nuevas dimensiones deben ser mayores que las actuales ("
                            + filas + "x" + columnas + ").");
        }
        if (nuevasFilas > MAX_FILAS || nuevasColumnas > MAX_COLUMNAS) {
            throw new ExpansionCiudadException(
                    "No se puede expandir la ciudad a " + nuevasFilas + "x" + nuevasColumnas
                            + " porque supera los límites globales (max. "
                            + MAX_FILAS + " filas y " + MAX_COLUMNAS + " columnas).");
        }
        if (expansionesRealizadas >= maximoExpansiones) {
            throw new ExpansionCiudadException(
                    "No se puede expandir: se ha alcanzado el máximo de expansiones permitidas ("
                            + maximoExpansiones + ").");
        }

        Bloque[][] nuevoTablero = new Bloque[nuevasFilas][nuevasColumnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                nuevoTablero[i][j] = tablero[i][j];
            }
        }

        this.tablero = nuevoTablero;
        this.filas = nuevasFilas;
        this.columnas = nuevasColumnas;
        this.expansionesRealizadas++;
        this.tipoEstructural = calcularTipoEstructural();
    }

    public void expandirSegunPolitica() {
        if (!puedeExpandirse()) {
            throw new ExpansionCiudadException("La ciudad no puede expandirse según la política actual.");
        }
        expandir(filas + INCREMENTO_EXPANSION, columnas + INCREMENTO_EXPANSION);
    }

    public ResumenEstructuralCiudad getResumenEstructural() {
        int ocupacion = contarBloques();
        int activos = listarBloquesActivos().size();
        int inactivos = ocupacion - activos;

        return new ResumenEstructuralCiudad(
                nombre,
                filas,
                columnas,
                capacidadMaxima(),
                ocupacion,
                getDensidad(),
                tipoEstructural,
                activos,
                inactivos
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ciudad{");
        sb.append("nombre='").append(nombre).append('\'');
        sb.append(", dimensiones=").append(filas).append("x").append(columnas);
        sb.append(", capacidad máxima=").append(capacidadMaxima());
        sb.append(", tipo=").append(tipoEstructural);
        sb.append(", densidad=").append(String.format(java.util.Locale.ROOT, "%.2f", getDensidad()));
        sb.append(", bloques totales=").append(contarBloques());
        sb.append(", distribución por tipo={");

        TipoBloque[] tipos = TipoBloque.values();
        for (int i = 0; i < tipos.length; i++) {
            sb.append(tipos[i].name()).append(":").append(contarBloquesPorTipo(tipos[i]));
            if (i < tipos.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("}}");
        return sb.toString();
    }
}
