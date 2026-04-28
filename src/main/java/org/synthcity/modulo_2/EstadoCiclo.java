package org.synthcity.modulo_2;

public final class EstadoCiclo {

    private final int numeroCiclo;
    private final int energiaProducida;
    private final int consumoEnergetico;
    private final int equilibrioEnergetico;
    private final int demandaServicios;
    private final int coberturaServicios;
    private final double coberturaServiciosPonderada;
    private final double eficienciaTransporte;
    private final int presionIndustrial;
    private final int soporteTransporte;
    private final int contaminacionCiclo;
    private final int contaminacionAcumulada;
    private final double bienestar;
    private final double estabilidad;
    private final double densidad;
    private final boolean necesidadExpansionDetectada;
    private final EstadoSimulacion estadoSimulacion;

    public EstadoCiclo(
            int numeroCiclo,
            int energiaProducida,
            int consumoEnergetico,
            int equilibrioEnergetico,
            int demandaServicios,
            int coberturaServicios,
            double coberturaServiciosPonderada,
            double eficienciaTransporte,
            int presionIndustrial,
            int soporteTransporte,
            int contaminacionCiclo,
            int contaminacionAcumulada,
            double bienestar,
            double estabilidad,
            double densidad,
            boolean necesidadExpansionDetectada,
            EstadoSimulacion estadoSimulacion) {

        if (numeroCiclo <= 0) {
            throw new ResultadoSimulacionInvalidoException("El numero de ciclo debe ser mayor que cero.");
        }
        if (energiaProducida < 0 || consumoEnergetico < 0 || demandaServicios < 0
                || coberturaServicios < 0 || presionIndustrial < 0 || soporteTransporte < 0
                || contaminacionCiclo < 0 || contaminacionAcumulada < 0) {
            throw new ResultadoSimulacionInvalidoException("Las metricas del ciclo no pueden ser negativas.");
        }
        if (equilibrioEnergetico != energiaProducida - consumoEnergetico) {
            throw new ResultadoSimulacionInvalidoException("El equilibrio energetico del ciclo no es coherente.");
        }
        if (contaminacionAcumulada < contaminacionCiclo) {
            throw new ResultadoSimulacionInvalidoException("La contaminacion acumulada no puede ser menor que la del ciclo.");
        }
        validarRango01(coberturaServiciosPonderada, "cobertura ponderada");
        validarRango01(eficienciaTransporte, "eficiencia de transporte");
        validarRango01(bienestar, "bienestar");
        validarRango01(estabilidad, "estabilidad");
        validarRango01(densidad, "densidad");
        if (estadoSimulacion == null) {
            throw new ResultadoSimulacionInvalidoException("El estado del ciclo no puede ser nulo.");
        }

        this.numeroCiclo = numeroCiclo;
        this.energiaProducida = energiaProducida;
        this.consumoEnergetico = consumoEnergetico;
        this.equilibrioEnergetico = equilibrioEnergetico;
        this.demandaServicios = demandaServicios;
        this.coberturaServicios = coberturaServicios;
        this.coberturaServiciosPonderada = coberturaServiciosPonderada;
        this.eficienciaTransporte = eficienciaTransporte;
        this.presionIndustrial = presionIndustrial;
        this.soporteTransporte = soporteTransporte;
        this.contaminacionCiclo = contaminacionCiclo;
        this.contaminacionAcumulada = contaminacionAcumulada;
        this.bienestar = bienestar;
        this.estabilidad = estabilidad;
        this.densidad = densidad;
        this.necesidadExpansionDetectada = necesidadExpansionDetectada;
        this.estadoSimulacion = estadoSimulacion;
    }

    private static void validarRango01(double valor, String campo) {
        if (valor < 0.0 || valor > 1.0) {
            throw new ResultadoSimulacionInvalidoException("El valor de " + campo + " debe estar entre 0.0 y 1.0.");
        }
    }

    public int getNumeroCiclo() {
        return numeroCiclo;
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

    public double getCoberturaServiciosPonderada() {
        return coberturaServiciosPonderada;
    }

    public double getEficienciaTransporte() {
        return eficienciaTransporte;
    }

    public int getPresionIndustrial() {
        return presionIndustrial;
    }

    public int getSoporteTransporte() {
        return soporteTransporte;
    }

    public int getContaminacionCiclo() {
        return contaminacionCiclo;
    }

    public int getContaminacionAcumulada() {
        return contaminacionAcumulada;
    }

    public double getBienestar() {
        return bienestar;
    }

    public double getEstabilidad() {
        return estabilidad;
    }

    public double getDensidad() {
        return densidad;
    }

    public boolean isNecesidadExpansionDetectada() {
        return necesidadExpansionDetectada;
    }

    public EstadoSimulacion getEstadoSimulacion() {
        return estadoSimulacion;
    }

    public boolean hayDeficitEnergetico() {
        return equilibrioEnergetico < 0;
    }

    public boolean hayDeficitServicios() {
        return coberturaServicios < demandaServicios;
    }

    @Override
    public String toString() {
        return "EstadoCiclo{" +
                "numeroCiclo=" + numeroCiclo +
                ", energiaProducida=" + energiaProducida +
                ", consumoEnergetico=" + consumoEnergetico +
                ", equilibrioEnergetico=" + equilibrioEnergetico +
                ", demandaServicios=" + demandaServicios +
                ", coberturaServicios=" + coberturaServicios +
                ", coberturaServiciosPonderada=" + coberturaServiciosPonderada +
                ", eficienciaTransporte=" + eficienciaTransporte +
                ", contaminacionCiclo=" + contaminacionCiclo +
                ", contaminacionAcumulada=" + contaminacionAcumulada +
                ", bienestar=" + bienestar +
                ", estabilidad=" + estabilidad +
                ", densidad=" + densidad +
                ", necesidadExpansionDetectada=" + necesidadExpansionDetectada +
                ", estadoSimulacion=" + estadoSimulacion +
                '}';
    }
}
