package org.synthcity.modulo_2;

/**
 * Persona 3: Objeto intermedio del Módulo 2 (Sprint 2 - Fusión).
 * Agrupa los cálculos realizados por el motor antes de pasarlos a ResultadoSimulacion.
 */
public class MetricasSimulacionBasica {

    // --- Balance Energético ---
    private final int energiaProducida;
    private final int consumoEnergetico;
    private final int equilibrioEnergetico;

    // --- Servicios ---
    private final int demandaServicios;
    private final int coberturaServicios;

    // --- Impacto Ambiental y Social ---
    private final int presionIndustrial;
    private final int soporteTransporte;
    private final int contaminacion;
    private final double bienestar;

    // --- Índices Globales ---
    private final double estabilidadBasica;

    public MetricasSimulacionBasica(int energiaProducida, int consumoEnergetico, int equilibrioEnergetico,
                                    int demandaServicios, int coberturaServicios, int presionIndustrial,
                                    int soporteTransporte, int contaminacion, double bienestar,
                                    double estabilidadBasica) {
        this.energiaProducida = energiaProducida;
        this.consumoEnergetico = consumoEnergetico;
        this.equilibrioEnergetico = equilibrioEnergetico;
        this.demandaServicios = demandaServicios;
        this.coberturaServicios = coberturaServicios;
        this.presionIndustrial = presionIndustrial;
        this.soporteTransporte = soporteTransporte;
        this.contaminacion = contaminacion;
        this.bienestar = bienestar;
        this.estabilidadBasica = estabilidadBasica;
    }

    // --- GETTERS (Solo lectura) ---
    public int getEnergiaProducida() { return energiaProducida; }
    public int getConsumoEnergetico() { return consumoEnergetico; }
    public int getEquilibrioEnergetico() { return equilibrioEnergetico; }
    public int getDemandaServicios() { return demandaServicios; }
    public int getCoberturaServicios() { return coberturaServicios; }
    public int getPresionIndustrial() { return presionIndustrial; }
    public int getSoporteTransporte() { return soporteTransporte; }
    public int getContaminacion() { return contaminacion; }
    public double getBienestar() { return bienestar; }
    public double getEstabilidadBasica() { return estabilidadBasica; }
}