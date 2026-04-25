package org.synthcity.modulo_1.bloques;

import org.synthcity.modulo_1.Posicion;
import org.synthcity.modulo_1.TipoBloque;
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
    public String toString() {
        return "BloqueServicios [posicion=" + getPosicion() +
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