import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Ciudad {
    private String nombre;
    private int filas;
    private int columnas;
    private Bloque[][] tablero;


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
        return tablero[fila][columna];
    }
    //  Validación de dimensiones

    public boolean dentroLimites(Posicion posicion) {
        if (posicion == null) return false;

        int f = posicion.getFila();
        int c = posicion.getColumna();

        return f >= 0 && f < this.filas && c >= 0 && c < this.columnas;
    }

    public void validarPosicion(Posicion posicion) {
        if (posicion == null) {
            throw new IllegalArgumentException("La posición no puede ser nula.");
        }
        if (!dentroLimites(posicion)) {
            throw new IllegalArgumentException("Posición (" + posicion.getFila() + ", " + posicion.getColumna() + ") fuera del tablero.");
        }
    }

    // Gestión tablero

    public Bloque getBloque(Posicion posicion) {
        validarPosicion(posicion);
        return tablero[posicion.getFila()][posicion.getColumna()];
    }

    public boolean estaOcupada(Posicion posicion) {
        validarPosicion(posicion);
        return tablero[posicion.getFila()][posicion.getColumna()] != null;
    }

    public int capacidadMaxima() {
        return filas * columnas;
    }

    // Métodos complementarios

    public void addBloque(Bloque bloque){

        if (bloque == null){
            throw new IllegalArgumentException("El Bloque no existe");
        }
        int fila=bloque.getFilas();
        int columna= bloque.getColumnas();
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas ){
            throw new IllegalArgumentException("La posición no se encuentra dentro del tablero");
        }
        if (tablero[fila][columna]!= null){
            throw new IllegalArgumentException("La casilla ya está ocupada");
        }
        tablero[fila][columna] = bloque;
    }

    public void removeBloque(int fila, int columna){
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas ){
            throw new IllegalArgumentException("La posición no se encuentra dentro del tablero");
        }
        if (tablero[fila][columna] == null){
            throw new IllegalArgumentException("No Hay un bloque que eliminar en esta posición");
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

    @Override
    public String toString() {
        return "Ciudad{" +
                "nombre='" + nombre + '\'' +
                ", filas=" + filas +
                ", columnas=" + columnas +
                ", tablero=" + Arrays.toString(tablero) +
                ", capacidad máxima=" + capacidadMaxima() +
                ", tipo de bloque=" + TipoBloque() +
                ", número de bloques=" + contarBloques() +
                ", número de bloques por tipo=" + contarBloquesPorTipo() +
                '}';
    }
}

