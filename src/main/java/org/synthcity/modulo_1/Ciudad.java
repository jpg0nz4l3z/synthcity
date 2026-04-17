package org.synthcity.modulo_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Excepciones
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
    private String nombre;
    private int filas;
    private int columnas;
    private Bloque[][] tablero;
    // === CONSTANTES Y ATRIBUTOS DE EXPANSIÓN  ===
    private static final int MAX_FILAS = 100;
    private static final int MAX_COLUMNAS = 100;
    private static final int MAX_EXPANSIONES = 5;   // política del grupo

    private int expansionesRealizadas = 0;

    public Ciudad(String nombre, int filas, int columnas) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ciudad es obligatorio.");
        }
        if (filas <= 0 || columnas <= 0) {
            throw new IllegalArgumentException("Las dimensiones tienen que ser mayor que 0.");
        }

        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.tablero = new Bloque[filas][columnas];
    }

    // Getters
    public String getNombre() { return nombre; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public Bloque getBloque(int fila, int columna) {
        validarPosicion(fila, columna);
        return tablero[fila][columna];
    }
    //  Validación de dimensiones

    public boolean dentroLimites(int fila, int columna) {
        return fila >= 0 && fila < this.filas && columna >= 0 && columna < this.columnas;
    }

    public void validarPosicion(int fila, int columna) {
        if (!dentroLimites(fila, columna)) {
            throw new PosicionFueraDeLimitesException("Posición (" + fila + ", " + columna + ") fuera del tablero.");
        }
    }

    // Gestión tablero

    public boolean estaOcupada(int fila, int columna) {
        validarPosicion(fila, columna);
        return tablero[fila][columna] != null;  // Usa los parámetros recibidos
    }

    public int capacidadMaxima() {
        return filas * columnas;
    }

    // Métodos complementarios

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

        // Validar que la celda está libre
        if (tablero[fila][columna] != null) {
            throw new CeldaOcupadaException("La casilla (" + fila + ", " + columna + ") ya está ocupada.");
        }

        tablero[fila][columna] = bloque;
    }

    public void removeBloque(int fila, int columna) {
        if (!dentroLimites(fila,columna)) {
            throw new PosicionFueraDeLimitesException("La posición (" + fila + ", " + columna + ") no se encuentra dentro del tablero.");
        }
        if (tablero[fila][columna] == null) {
            throw new CeldaVaciaException("No hay un bloque que eliminar en la posición (" + fila + ", " + columna + ").");
        }
        tablero[fila][columna] = null;
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

    public List<Bloque> listarBloquesActivos(){
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

    public int contarBloques(){
        int contadorBloques=0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] != null) {
                    contadorBloques++;
                }
            }
        }
        return contadorBloques;
    }

    public int contarBloquesPorTipo(TipoBloque tipo){
        int numBloquesTipo=0;
        if (tipo == null){
            throw new IllegalArgumentException("El tipo no existe");
        }
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] != null && tablero[i][j].getTipo()==tipo) {
                    numBloquesTipo++;
                }
            }
        }
        return numBloquesTipo;

    }

    public boolean estaVacia(){
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] != null ) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean puedeExpandirse() {
        return expansionesRealizadas < MAX_EXPANSIONES &&
                (filas + 10 <= MAX_FILAS) && (columnas + 10 <= MAX_COLUMNAS);
    }
    public void expandir(int nuevasFilas, int nuevasColumnas) {
        if (nuevasFilas <= this.filas || nuevasColumnas <= this.columnas) {
            throw new ExpansionCiudadException(
                    "No se puede expandir la ciudad a " + nuevasFilas + "x" + nuevasColumnas +
                            " porque las nuevas dimensiones deben ser mayores que las actuales (" +
                            this.filas + "x" + this.columnas + ").");
        }
        if (nuevasFilas > MAX_FILAS || nuevasColumnas > MAX_COLUMNAS) {
            throw new ExpansionCiudadException(
                    "No se puede expandir la ciudad a " + nuevasFilas + "x" +
                            nuevasColumnas +
                            " porque supera los límites globales (max. " + MAX_FILAS + " filas y " +
                            MAX_COLUMNAS + " columnas).");
        }
        if (expansionesRealizadas > MAX_EXPANSIONES) {
            throw new ExpansionCiudadException(
                    "No se puede expandir: se ha alcanzado el máximo de expansiones permitidas (" +
                            MAX_EXPANSIONES + ").");
        }
        Bloque[][] nuevoTablero = new Bloque[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                nuevoTablero[i][j] = this.tablero[i][j];
            }
        }
        this.tablero = nuevoTablero;
        this.filas = nuevasFilas;
        this.columnas = nuevasColumnas;

        this.expansionesRealizadas++;
        }
        public void expandirSegunPolitica(){
        if(!puedeExpandirse()) {
            throw new ExpansionCiudadException("La ciudad no puede expandirse según la política actual.");
        }
        int nuevasFilas = this.filas + 10;
        int nuevasColumnas = this.columnas + 10;
        expandir(nuevasFilas, nuevasColumnas);
        }
        public ResumenEstructuralCiudad getResumenEstructural() {
        int ocupacion = contarBloques();
        int activos = listarBloquesActivos().size();
        int inactivos = ocupacion - activos;

        return new ResumenEstructuralCiudad(
                this.nombre,
                this.filas,
                this.columnas,
                capacidadMaxima(),
                ocupacion,
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

