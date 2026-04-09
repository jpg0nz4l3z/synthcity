import java.util.Arrays;

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

