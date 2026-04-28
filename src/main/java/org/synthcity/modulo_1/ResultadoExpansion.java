package org.synthcity.modulo_1;

/**
 * DTO que encapsula el resultado de una operación de expansión.
 * Requisito obligatorio del Sprint 3 para la integración.
 */
public class ResultadoExpansion {
    private final boolean exitosa;
    private final int filasAnteriores;
    private final int columnasAnteriores;
    private final int nuevasFilas;
    private final int nuevasColumnas;
    private final String mensaje;

    public ResultadoExpansion(boolean exitosa, int filasAnt, int colAnt,
                              int filasNuevas, int colNuevas, String mensaje) {
        this.exitosa = exitosa;
        this.filasAnteriores = filasAnt;
        this.columnasAnteriores = colAnt;
        this.nuevasFilas = filasNuevas;
        this.nuevasColumnas = colNuevas;
        this.mensaje = mensaje;
    }

    // Getters obligatorios para que M3 pueda leer el resultado
    public boolean isExitosa() { return exitosa; }
    public int getFilasAnteriores() { return filasAnteriores; }
    public int getColumnasAnteriores() { return columnasAnteriores; }
    public int getNuevasFilas() { return nuevasFilas; }
    public int getNuevasColumnas() { return nuevasColumnas; }
    public String getMensaje() { return mensaje; }
}