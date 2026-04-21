package org.synthcity.modulo_2;

public class MetricasSimulacionBasica {
    private final int energiaProducida;
    private final int consumoEnergetico;
    private final int equilibrioEnergetico;

    private final int demandaServicios;
    private final int coberturaServicios;

    private final int presionIndustrial;
    private final int soporteTransporte;
    private final int contaminacion;
    private final double bienestar;

    private final double estabilidadBasica;

    public MetricasSimulacionBasica(int energiaProducida, int consumoEnergetico,
                                    int demandaServicios, int coberturaServicios, int presionIndustrial,
                                    int soporteTransporte, int contaminacion, double bienestar,
                                    double estabilidadBasica) {
        this.energiaProducida = energiaProducida;
        this.consumoEnergetico = consumoEnergetico;
        this.equilibrioEnergetico = energiaProducida - consumoEnergetico;
        this.demandaServicios = demandaServicios;
        this.coberturaServicios = coberturaServicios;
        this.presionIndustrial = presionIndustrial;
        this.soporteTransporte = soporteTransporte;
        this.contaminacion = contaminacion;
        this.bienestar = bienestar;
        this.estabilidadBasica = estabilidadBasica;
    }

    public int getEnergiaProducida() {
        return energiaProducida;
    }

    public int getConsumoEnergetico() {
        return consumoEnergetico;
    }

    public int getEquilibrioEnergetico() {
        return equilibrioEnergetico;
    }

    public int getDemandaServicios() {
        return demandaServicios;
    }

    public int getCoberturaServicios() {
        return coberturaServicios;
    }

    public int getPresionIndustrial() {
        return presionIndustrial;
    }

    public int getSoporteTransporte() {
        return soporteTransporte;
    }

    public int getContaminacion() {
        return contaminacion;
    }

    public double getBienestar() {
        return bienestar;
    }

    public double getEstabilidadBasica() {
        return estabilidadBasica;
    }
}
