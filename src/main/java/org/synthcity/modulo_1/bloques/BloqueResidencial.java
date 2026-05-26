package org.synthcity.modulo_1.bloques;

import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.ReglasSimulacion;

public class BloqueResidencial extends Bloque {

    public BloqueResidencial(Posicion posicion) {
        super(TipoBloque.RESIDENCIAL, posicion, true);
    }

    @Override
    public int getProduccionEnergia() {
        return 0;
    }

    @Override
    public int getConsumoEnergetico() {
        return ReglasSimulacion.CONSUMO_RESIDENCIAL;
    }

    @Override
    public int getDemandaServicios() {
        return ReglasSimulacion.DEMANDA_POR_RESIDENCIAL;
    }

    @Override
    public int getCoberturaServicios() {
        return 0;
    }

    @Override
    public int getPresionIndustrial() {
        return 0;
    }

    @Override
    public int getSoporteTransporte() {
        return 0;
    }

    @Override
    public int getContaminacion() {
        return 0;
    }

    @Override
    public int getRadioInfluencia() {
        return 0;
    }

    @Override
    public boolean esGeneradorDemanda(){
        return true;
    }

    @Override
    public String toString() {
        return "BloqueResidencial [posicion=" + getPosicion() +
                ", estado=" + (estaActivo() ? "activo" : "inactivo") + "]";
    }
}