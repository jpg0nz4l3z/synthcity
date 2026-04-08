public class Bloque {

    TipoBloque TipoBloque;
    Posicion Posicion;
    boolean Activo;


    public Bloque(TipoBloque TipoBloque, Posicion Posicion, boolean Activo) {
        this.TipoBloque = TipoBloque;
        this.Posicion = Posicion;
        this.Activo = Activo;
    }

    public TipoBloque getTipo(){
        return TipoBloque;
    }

    public Posicion getPosicion(){
        return Posicion;
    }

    public boolean estaActivo(){
        return Activo;
    }

    public void activar(){
        if (!Activo) {
            Activo = true;
        }
    }

    public void desactivar(){
        if (Activo) {
            Activo = false;
        }
    }

    @Override
    public String toString() {
        return "Bloque{" +
                "TipoBloque=" + TipoBloque +
                ", Posicion=" + Posicion +
                ", Activo=" + Activo +
                '}';
    }
}
