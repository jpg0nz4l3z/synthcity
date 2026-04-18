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

    // Tipo Estructural y control de expansión

    private TipoEstructuralCiudad tipoEstructural;
    private int expansionesRealizadas = 0;
    private int maximoExpansiones = MAX_EXPANSIONES;
    private double umbralExpansion = 0.80;

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
        this.tipoEstructural = calcularTipoEstructural();
        this.expansionesRealizadas = 0;
    }

    // Tipo Estructural

    private TipoEstructuralCiudad calcularTipoEstructural() {

        int capacidad = capacidadMaxima();
        if (capacidad <= 400) return TipoEstructuralCiudad.PEQUENA;
        if (capacidad <= 1600) return TipoEstructuralCiudad.MEDIANA;
        return TipoEstructuralCiudad.GRANDE;
    }

    public TipoEstructuralCiudad getTipoEstructural() {
        return tipoEstructural;
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

    // Ocupación, densidad y saturación

    public int getOcupacionActual() {
        return contarBloques();
    }

    public double getDensidad() {
        int capacidad = capacidadMaxima();
        if (capacidad == 0) return 0.0;
        return (double) getOcupacionActual() / capacidad;
    }

    public boolean estaProximaASaturacion() {
        return getDensidad() >= umbralExpansion;
    }

    public boolean hayBloquesActivos() {
        return !listarBloquesActivos().isEmpty();
    }

    // Activación y desactivación por posición

    public void activarBloque(Posicion pos) {
        int fila    = pos.getFila();
        int columna = pos.getColumna();
        validarPosicion(fila, columna);
        if (tablero[fila][columna] == null) {
            throw new CeldaVaciaException(
                    "No hay bloque que activar en la posición (" + fila + ", " + columna + ").");
        }
        tablero[fila][columna].activar();
    }

    public void desactivarBloque(Posicion pos) {
        int fila    = pos.getFila();
        int columna = pos.getColumna();
        validarPosicion(fila, columna);
        if (tablero[fila][columna] == null) {
            throw new CeldaVaciaException(
                    "No hay bloque que desactivar en la posición (" + fila + ", " + columna + ").");
        }
        tablero[fila][columna].desactivar();
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

