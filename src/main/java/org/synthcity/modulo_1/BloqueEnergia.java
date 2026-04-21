package org.synthcity.modulo_1;

import org.synthcity.modulo_2.ReglasSimulacion;

public class BloqueEnergia extends Bloque {

    public BloqueEnergia(Posicion posicion) {
        super(TipoBloque.ENERGIA, posicion, true);
    }

    @Override
    public int getProduccionEnergia() {
        return ReglasSimulacion.ENERGIA_POR_BLOQUE_ENERGIA;
    }

    @Override
    public int getConsumoEnergetico() {
        return ReglasSimulacion.CONSUMO_ENERGIA;
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
    public int getRadioInfluencia(){
        return 0;
    }

    @Override
    public boolean esGeneradorDemanda(){
        return false;
    }

    @Override
    public String toString() {
        return "BloqueEnergia [posicion=" + getPosicion() +
                ", estado=" + (estaActivo() ? "activo" : "inactivo") + "]";
    }
}