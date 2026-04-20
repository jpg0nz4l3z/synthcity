package org.synthcity.modulo_1;

import org.synthcity.modulo_2.ReglasSimulacion;

public class BloqueIndustrial extends Bloque {

    public BloqueIndustrial(Posicion posicion) {
        super(TipoBloque.INDUSTRIAL, posicion, true);
    }

    @Override
    public int getProduccionEnergia() {
        return 0;
    }

    @Override
    public int getConsumoEnergetico() {
        return ReglasSimulacion.CONSUMO_INDUSTRIAL;
    }

    @Override
    public int getDemandaServicios() {
        return 0;
    }

    @Override
    public int getCoberturaServicios() {
        return 0;
    }

    @Override
    public int getPresionIndustrial() {
        return ReglasSimulacion.PRESION_POR_INDUSTRIAL;
    }

    @Override
    public int getSoporteTransporte() {
        return 0;
    }

    @Override
    public int getContaminacion() {
        return ReglasSimulacion.CONTAMINACION_POR_INDUSTRIAL;
    }

    @Override
    public String toString() {
        return "BloqueIndustrial [posicion=" + getPosicion() +
                ", estado=" + (estaActivo() ? "activo" : "inactivo") + "]";
    }
}