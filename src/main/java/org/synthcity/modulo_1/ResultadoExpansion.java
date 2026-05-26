package org.synthcity.modulo_1;

public class ResultadoExpansion {

    private final boolean aprobada;
    private final int filasAnteriores;
    private final int columnasAnteriores;
    private final int filasNuevas;
    private final int columnasNuevas;
    private final TipoEstructuralCiudad tipoEstructuralAnterior;
    private final TipoEstructuralCiudad tipoEstructuralNuevo;
    private final String motivoRechazo;

    public ResultadoExpansion(boolean aprobada,
                              int filasAnteriores,
                              int columnasAnteriores,
                              int filasNuevas,
                              int columnasNuevas,
                              TipoEstructuralCiudad tipoEstructuralAnterior,
                              TipoEstructuralCiudad tipoEstructuralNuevo,
                              String motivoRechazo) {
        this.aprobada = aprobada;
        this.filasAnteriores = filasAnteriores;
        this.columnasAnteriores = columnasAnteriores;
        this.filasNuevas = filasNuevas;
        this.columnasNuevas = columnasNuevas;
        this.tipoEstructuralAnterior = tipoEstructuralAnterior;
        this.tipoEstructuralNuevo = tipoEstructuralNuevo;
        this.motivoRechazo = motivoRechazo;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public boolean isExitosa() {
        return aprobada;
    }

    public int getFilasAnteriores() {
        return filasAnteriores;
    }

    public int getColumnasAnteriores() {
        return columnasAnteriores;
    }

    public int getFilasNuevas() {
        return filasNuevas;
    }

    public int getColumnasNuevas() {
        return columnasNuevas;
    }

    public int getNuevasFilas() {
        return filasNuevas;
    }

    public int getNuevasColumnas() {
        return columnasNuevas;
    }

    public TipoEstructuralCiudad getTipoEstructuralAnterior() {
        return tipoEstructuralAnterior;
    }

    public TipoEstructuralCiudad getTipoEstructuralNuevo() {
        return tipoEstructuralNuevo;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public String getMensaje() {
        return motivoRechazo;
    }

    public boolean cambioTipoEstructural() {
        return tipoEstructuralAnterior != tipoEstructuralNuevo;
    }

    @Override
    public String toString() {
        return "ResultadoExpansion{" +
                "aprobada=" + aprobada +
                ", dimensiones=" + filasAnteriores + "x" + columnasAnteriores +
                " -> " + filasNuevas + "x" + columnasNuevas +
                ", tipo=" + tipoEstructuralAnterior + " -> " + tipoEstructuralNuevo +
                ", motivo='" + motivoRechazo + '\'' +
                '}';
    }
}