package org.synthcity.modulo_1;

import org.synthcity.modulo_2.ReglasSimulacion;

public class BloqueServicios extends Bloque {

    public BloqueServicios(Posicion posicion) {
        super(TipoBloque.SERVICIOS, posicion, true);
    }

    @Override
    public int getProduccionEnergia() {
        return 0;
    }

    @Override
    public int getConsumoEnergetico() {
        return ReglasSimulacion.CONSUMO_SERVICIOS;
    }

    @Override
    public int getDemandaServicios() {
        return 0;
    }

    @Override
    public int getCoberturaServicios() {
        return ReglasSimulacion.COBERTURA_POR_SERVICIO;
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
        return ReglasSimulacion.RADIO_COBERTURA_SERVICIOS;
    }

    @Override
    public boolean esGeneradorDemanda(){
        return false;
    }

    @Override
    public String toString() {
        return "BloqueServicios [posicion=" + getPosicion() +
                ", estado=" + (estaActivo() ? "activo" : "inactivo") + "]";
    }
}