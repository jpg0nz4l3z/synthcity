package org.synthcity.modulo_1.bloques;

import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.TipoBloque;
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
    public int getRadioInfluencia(){
        return 0;
    }

    @Override
    public boolean esGeneradorDemanda(){
        return false;
    }

    @Override
    public String toString() {
        return "BloqueIndustrial [posicion=" + getPosicion() +
                ", estado=" + (estaActivo() ? "activo" : "inactivo") + "]";
    }
    @Override
    public int getRadioInfluencia() {
        // TODO: Persona 2 - Asignar el radio correcto para la simulación
        return 0;
    }

    @Override
    public boolean esGeneradorDemanda() {
        // TODO: Persona 2 - Definir si este bloque genera demanda
        return false;
    }
}