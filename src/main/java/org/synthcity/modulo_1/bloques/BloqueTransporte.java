package org.synthcity.modulo_1.bloques;

import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_2.ReglasSimulacion;

public class BloqueTransporte extends Bloque {

    public BloqueTransporte(Posicion posicion) {
        super(TipoBloque.TRANSPORTE, posicion, true);
    }

    @Override
    public int getProduccionEnergia() {
        return 0;
    }

    @Override
    public int getConsumoEnergetico() {
        return ReglasSimulacion.CONSUMO_TRANSPORTE;
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
        return ReglasSimulacion.TRANSPORTE_SOPORTE;
    }

    @Override
    public int getContaminacion() {
        return 0;
    }

    @Override
    public int getRadioInfluencia(){
        return ReglasSimulacion.RADIO_INFLUENCIA_TRANSPORTE;
    }

    @Override
    public boolean esGeneradorDemanda(){
        return false;
    }

    @Override
    public String toString() {
        return "BloqueTransporte [posicion=" + getPosicion() +
                ", estado=" + (estaActivo() ? "activo" : "inactivo") + "]";
    }
}