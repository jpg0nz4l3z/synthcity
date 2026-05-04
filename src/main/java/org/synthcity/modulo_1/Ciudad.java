package org.synthcity.modulo_1;

import org.synthcity.modulo_1.bloques.Bloque;

import java.util.ArrayList;
import java.util.List;

public class Ciudad implements Expansionable {
    private String nombre;
    private int filas;
    private int columnas;
    private Bloque[][] tablero;
    private static final int MAX_FILAS = 100;
    private static final int MAX_COLUMNAS = 100;
    private TipoEstructuralCiudad tipoEstructural;
    private int expansionesRealizadas = 0;
    private final int maximoExpansiones = 5;
    private double umbralExpansion = 0.80;
    private static final int INCREMENTO_EXPANSION = 10;
    private boolean expandidaDesdeUltimaSimulacion;

    public Ciudad(String nombre, int filas, int columnas) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ciudad es obligatorio.");
        }

        if (filas <= 0 || columnas <= 0) {
            throw new DimensionesInvalidasException("Las dimensiones tienen que ser mayores que 0.");
        }

        if (filas > MAX_FILAS || columnas > MAX_COLUMNAS) {
            throw new DimensionesInvalidasException(
                    "Las dimensiones no pueden superar " + MAX_FILAS + " filas y " + MAX_COLUMNAS + " columnas."
            );
        }

        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.tablero = new Bloque[filas][columnas];
        this.tipoEstructural = calcularTipoEstructural();
        this.expansionesRealizadas = 0;
        this.expandidaDesdeUltimaSimulacion = false;
    }

    public static Ciudad reconstruir(String nombre, int filas, int columnas, int expansionesRealizadas) {
        if (expansionesRealizadas < 0) {
            throw new IllegalArgumentException("Las expansiones realizadas no pueden ser negativas.");
        }

        Ciudad ciudad = new Ciudad(nombre, filas, columnas);

        if (expansionesRealizadas > ciudad.maximoExpansiones) {
            throw new IllegalArgumentException("Las expansiones realizadas no pueden superar el maximo permitido.");
        }

        ciudad.expansionesRealizadas = expansionesRealizadas;
        ciudad.tipoEstructural = ciudad.calcularTipoEstructural();
        ciudad.expandidaDesdeUltimaSimulacion = false;

        return ciudad;
    }

    private TipoEstructuralCiudad calcularTipoEstructural() {
        int capacidad = capacidadMaxima();

        if (capacidad <= 400) return TipoEstructuralCiudad.PEQUENA;
        if (capacidad <= 1600) return TipoEstructuralCiudad.MEDIANA;
        return TipoEstructuralCiudad.GRANDE;
    }

    public TipoEstructuralCiudad getTipoEstructural() {
        return tipoEstructural;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public boolean dentroLimites(int fila, int columna) {
        boolean perteneceFilas = fila >= 0 && fila < this.filas;
        boolean perteneceColumnas = columna >= 0 && columna < this.columnas;
        return perteneceFilas && perteneceColumnas;
    }

    public void validarPosicion(int fila, int columna) {
        if (!dentroLimites(fila, columna)) {
            throw new PosicionFueraDeLimitesException("Posición (" + fila + ", " + columna + ") fuera del tablero.");
        }
    }

    public boolean estaOcupada(int fila, int columna) {
        validarPosicion(fila, columna);
        return tablero[fila][columna] != null;
    }

    public void addBloque(Bloque bloque) {
        if (bloque == null) {
            throw new BloqueNuloException("El bloque no puede ser nulo.");
        }

        Posicion pos = bloque.getPosicion();
        int fila = pos.getFila();
        int columna = pos.getColumna();

        if (!dentroLimites(fila, columna)) {
            throw new PosicionFueraDeLimitesException("La posición (" + fila + ", " + columna + ") está fuera del tablero.");
        }

        if (tablero[fila][columna] != null) {
            throw new CeldaOcupadaException("La celda (" + fila + ", " + columna + ") ya está ocupada.");
        }

        tablero[fila][columna] = bloque;
    }

    public void removeBloque(int fila, int columna) {
        if (!dentroLimites(fila, columna)) {
            throw new PosicionFueraDeLimitesException("La posición (" + fila + ", " + columna + ") no se encuentra dentro del tablero.");
        }

        if (tablero[fila][columna] == null) {
            throw new CeldaVaciaException("No hay un bloque que eliminar en la posición (" + fila + ", " + columna + ").");
        }

        tablero[fila][columna] = null;
    }

    public boolean estaVacia() {
        return contarBloques() == 0;
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
        int numBloquesTipo = 0;

        if (tipo == null) {
            throw new IllegalArgumentException("El tipo no existe");
        }

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] != null && tablero[i][j].getTipo() == tipo) {
                    numBloquesTipo++;
                }
            }
        }

        return numBloquesTipo;
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

        return listarBloques()
                .stream()
                .filter(Bloque::estaActivo)
                .toList();
    }

    public int capacidadMaxima() {
        return filas * columnas;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ciudad{");
        sb.append("nombre='").append(nombre).append('\'');
        sb.append(", dimensiones=").append(filas).append("x").append(columnas);
        sb.append(", capacidad máxima=").append(capacidadMaxima());
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


    public Bloque getBloque(int fila, int columna) {
        validarPosicion(fila, columna);
        return tablero[fila][columna];
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima();
    }

    public int getOcupacionActual() {
        return contarBloques();
    }


    public double getDensidad() {
        return (double) getOcupacionActual() / capacidadMaxima();
    }

    public boolean estaProximaASaturacion() {
        return getDensidad() >= umbralExpansion;
    }

    public boolean hayBloquesActivos() {
        return !listarBloquesActivos().isEmpty();
    }

    public void activarBloque(Posicion pos) {
        int fila = pos.getFila();
        int columna = pos.getColumna();

        validarPosicion(fila, columna);

        if (tablero[fila][columna] == null) {
            throw new CeldaVaciaException(
                    "No hay bloque que activar en la posición (" + fila + ", " + columna + ").");
        }
        tablero[fila][columna].activar();
    }

    public void desactivarBloque(Posicion pos) {
        int fila = pos.getFila();
        int columna = pos.getColumna();
        validarPosicion(fila, columna);

        if (tablero[fila][columna] == null) {
            throw new CeldaVaciaException(
                    "No hay bloque que desactivar en la posición (" + fila + ", " + columna + ").");
        }
        tablero[fila][columna].desactivar();
    }


    public boolean puedeExpandirse() {
        boolean cabenMasFilasColumnas =
                (filas + INCREMENTO_EXPANSION <= MAX_FILAS)
                        && (columnas + INCREMENTO_EXPANSION <= MAX_COLUMNAS);

        return expansionesRealizadas < maximoExpansiones && cabenMasFilasColumnas;
    }

    public ResultadoExpansion expandirSegunPolitica() {
        return expandir();
    }

    @Override
    public ResultadoExpansion expandir() {
        int filasAnteriores = this.filas;
        int columnasAnteriores = this.columnas;
        TipoEstructuralCiudad tipoAnterior = this.tipoEstructural;

        if (!puedeExpandirse()) {
            throw new ExpansionCiudadException(
                    "La ciudad no puede expandirse: se ha alcanzado el máximo de expansiones o los límites globales."
            );
        }

        int nuevasFilas = this.filas + INCREMENTO_EXPANSION;
        int nuevasColumnas = this.columnas + INCREMENTO_EXPANSION;

        if (nuevasFilas > MAX_FILAS || nuevasColumnas > MAX_COLUMNAS) {
            throw new ExpansionCiudadException(
                    "No se puede expandir porque supera los límites globales de "
                            + MAX_FILAS + " filas y " + MAX_COLUMNAS + " columnas."
            );
        }

        Bloque[][] nuevoTablero = new Bloque[nuevasFilas][nuevasColumnas];

        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                nuevoTablero[i][j] = this.tablero[i][j];
            }
        }

        this.tablero = nuevoTablero;
        this.filas = nuevasFilas;
        this.columnas = nuevasColumnas;
        this.tipoEstructural = calcularTipoEstructural();
        this.expansionesRealizadas++;
        this.expandidaDesdeUltimaSimulacion = true;

        return new ResultadoExpansion(
                true,
                filasAnteriores,
                columnasAnteriores,
                this.filas,
                this.columnas,
                tipoAnterior,
                this.tipoEstructural,
                null
        );
    }

    public ResumenEstructuralCiudad getResumenEstructural() {
        int bloquesActivos = listarBloquesActivos().size();
        int bloquesInactivos = getOcupacionActual() - bloquesActivos;

        return new ResumenEstructuralCiudad(
                this.nombre,
                this.filas,
                this.columnas,
                getCapacidadMaxima(),
                getOcupacionActual(),
                getDensidad(),
                getTipoEstructural(),
                bloquesActivos,
                bloquesInactivos
        );
    }
    // === API ESPACIAL (Requisito Sprint 3 para el Módulo 2) ===

    /**
     * Permite al simulador pedir un bloque concreto sabiendo su posición.
     */
    public Bloque getBloque(Posicion pos) {
        if (pos == null) {
            throw new IllegalArgumentException("La posición no puede ser nula.");
        }

        validarPosicion(pos.getFila(), pos.getColumna());
        return tablero[pos.getFila()][pos.getColumna()];
    }

    public boolean fueExpandidaDesdeUltimaSimulacion() {
        return expandidaDesdeUltimaSimulacion;
    }

    public void marcarSimulacionEjecutada() {
        this.expandidaDesdeUltimaSimulacion = false;
    }

    public int getExpansionesRealizadas() {
        return expansionesRealizadas;
    }

    public List<Bloque> getBloquesActivosConPosicion() {
        return listarBloquesActivos();
    }
}

