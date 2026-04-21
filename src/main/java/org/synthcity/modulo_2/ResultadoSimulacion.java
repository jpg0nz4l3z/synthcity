package org.synthcity.modulo_2;

import java.util.Map;

import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_1.TipoEstructuralCiudad;

public class ResultadoSimulacion {

    private final String nombreCiudad;
    private final int filas;
    private final int columnas;
    private final int capacidadMaxima;

    private final int bloquesTotales;
    private final int bloquesActivos;
    private final int bloquesInactivos;
    private final Map<TipoBloque, Integer> conteoPorTipo;
    private final EstadoSimulacion estadoSimulacion;
    private final double densidad;
    private final TipoEstructuralCiudad tipoEstructural;
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
    private final double ratioEnergetico;
    private final double ratioCoberturaServicios;

    public ResultadoSimulacion(
            String nombreCiudad,
            int filas,
            int columnas,
            int capacidadMaxima,
            int bloquesTotales,
            int bloquesActivos,
            int bloquesInactivos,
            Map<TipoBloque, Integer> conteoPorTipo,
            EstadoSimulacion estadoSimulacion,
            double densidad,
            TipoEstructuralCiudad tipoEstructural,
            int energiaProducida,
            int consumoEnergetico,
            int equilibrioEnergetico,
            int demandaServicios,
            int coberturaServicios,
            int presionIndustrial,
            int soporteTransporte,
            int contaminacion,
            double bienestar,
            double estabilidadBasica,
            double ratioEnergetico,
            double ratioCoberturaServicios) {

        if (nombreCiudad == null || nombreCiudad.isBlank()) {
            throw new ResultadoSimulacionInvalidoException("El nombre de la ciudad es obligatorio.");
        }

        if (filas <= 0 || columnas <= 0 || capacidadMaxima <= 0) {
            throw new ResultadoSimulacionInvalidoException("Las dimensiones o la capacidad máxima son inválidas.");
        }

        if (bloquesTotales < 0 || bloquesActivos < 0 || bloquesInactivos < 0) {
            throw new ResultadoSimulacionInvalidoException("Las cantidades de bloques no pueden ser negativas.");
        }

        if (bloquesActivos + bloquesInactivos != bloquesTotales) {
            throw new ResultadoSimulacionInvalidoException(
                    "La suma de bloques activos e inactivos debe coincidir con el total."
            );
        }

        if (conteoPorTipo == null) {
            throw new ResultadoSimulacionInvalidoException("El mapa de conteo por tipo no puede ser nulo.");
        }

        for (TipoBloque tipo : TipoBloque.values()) {
            if (!conteoPorTipo.containsKey(tipo)) {
                throw new ResultadoSimulacionInvalidoException(
                        "Falta el tipo " + tipo + " en el mapa de conteo."
                );
            }
            Integer cantidad = conteoPorTipo.get(tipo);
            if (cantidad == null || cantidad < 0) {
                throw new ResultadoSimulacionInvalidoException(
                        "El conteo del tipo " + tipo + " es inválido."
                );
            }
        }

        int sumaConteos = conteoPorTipo.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        if (sumaConteos != bloquesTotales) {
            throw new ResultadoSimulacionInvalidoException(
                    "La suma del conteo por tipo no coincide con el número total de bloques."
            );
        }

        if (estadoSimulacion == null) {
            throw new ResultadoSimulacionInvalidoException("El estado de simulación no puede ser nulo.");
        }

        if (tipoEstructural == null) {
            throw new ResultadoSimulacionInvalidoException("El tipo estructural no puede ser nulo.");
        }

        if (densidad < 0.0 || densidad > 1.0) {
            throw new ResultadoSimulacionInvalidoException("La densidad debe estar entre 0.0 y 1.0.");
        }

        if (energiaProducida < 0 || consumoEnergetico < 0 || demandaServicios < 0
                || coberturaServicios < 0 || presionIndustrial < 0
                || soporteTransporte < 0 || contaminacion < 0) {
            throw new ResultadoSimulacionInvalidoException(
                    "Las métricas absolutas no pueden ser negativas."
            );
        }

        if (equilibrioEnergetico != energiaProducida - consumoEnergetico) {
            throw new ResultadoSimulacionInvalidoException(
                    "El equilibrio energético no coincide con energiaProducida - consumoEnergetico."
            );
        }

        if (bienestar < 0.0 || bienestar > 1.0) {
            throw new ResultadoSimulacionInvalidoException("El bienestar debe estar entre 0.0 y 1.0.");
        }

        if (estabilidadBasica < 0.0 || estabilidadBasica > 1.0) {
            throw new ResultadoSimulacionInvalidoException("La estabilidad básica debe estar entre 0.0 y 1.0.");
        }

        if (ratioEnergetico < 0.0 || ratioCoberturaServicios < 0.0) {
            throw new ResultadoSimulacionInvalidoException("Los ratios no pueden ser negativos.");
        }

        this.nombreCiudad = nombreCiudad;
        this.filas = filas;
        this.columnas = columnas;
        this.capacidadMaxima = capacidadMaxima;
        this.bloquesTotales = bloquesTotales;
        this.bloquesActivos = bloquesActivos;
        this.bloquesInactivos = bloquesInactivos;
        this.conteoPorTipo = Map.copyOf(conteoPorTipo);
        this.estadoSimulacion = estadoSimulacion;
        this.densidad = densidad;
        this.tipoEstructural = tipoEstructural;
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
        this.ratioEnergetico = ratioEnergetico;
        this.ratioCoberturaServicios = ratioCoberturaServicios;
    }

    public int getCantidadPorTipo(TipoBloque tipo) {
        if (tipo == null || conteoPorTipo == null) {
            return 0;
        }
        return conteoPorTipo.getOrDefault(tipo, 0);
    }

    public boolean ciudadEstaVacia() {
        return bloquesTotales == 0;
    }

    public boolean hayBloquesActivos() {
        return bloquesActivos > 0;
    }

    public boolean hayDeficitEnergetico() {
        return equilibrioEnergetico < 0;
    }

    public boolean hayDeficitServicios() {
        return ratioCoberturaServicios < 1.0;
    }

    public String getResumenSimulacion() {
        return String.format(
                java.util.Locale.ROOT,
                "Resumen de [%s]: Estado=%s | Bienestar=%.2f | Estabilidad=%.2f | BalanceEnergia=%d",
                nombreCiudad,
                estadoSimulacion,
                bienestar,
                estabilidadBasica,
                equilibrioEnergetico
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESULTADO DE SIMULACION ===\n");
        sb.append("Ciudad: ").append(nombreCiudad).append("\n");
        sb.append("Tipo estructural: ").append(tipoEstructural).append("\n");
        sb.append("Dimensiones: ").append(filas).append("x").append(columnas).append("\n");
        sb.append("Capacidad maxima: ").append(capacidadMaxima).append("\n");
        sb.append("Densidad: ").append(densidad).append("\n");
        sb.append("Bloques totales: ").append(bloquesTotales).append("\n");
        sb.append("Bloques activos: ").append(bloquesActivos).append("\n");
        sb.append("Bloques inactivos: ").append(bloquesInactivos).append("\n");
        sb.append("Energia producida/consumida: ").append(energiaProducida).append(" / ").append(consumoEnergetico).append("\n");
        sb.append("Equilibrio energetico: ").append(equilibrioEnergetico).append("\n");
        sb.append("Servicios demanda/cobertura: ").append(demandaServicios).append(" / ").append(coberturaServicios).append("\n");
        sb.append("Presion industrial: ").append(presionIndustrial).append("\n");
        sb.append("Soporte transporte: ").append(soporteTransporte).append("\n");
        sb.append("Contaminacion: ").append(contaminacion).append("\n");
        sb.append("Bienestar: ").append(bienestar).append("\n");
        sb.append("Estabilidad: ").append(estabilidadBasica).append("\n");
        sb.append("Ratio energetico: ").append(ratioEnergetico).append("\n");
        sb.append("Ratio servicios: ").append(ratioCoberturaServicios).append("\n");
        sb.append("Estado: ").append(estadoSimulacion).append("\n");
        return sb.toString();
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public int getBloquesTotales() {
        return bloquesTotales;
    }

    public int getBloquesActivos() {
        return bloquesActivos;
    }

    public int getBloquesInactivos() {
        return bloquesInactivos;
    }

    public Map<TipoBloque, Integer> getConteoPorTipo() {
        return conteoPorTipo;
    }

    public EstadoSimulacion getEstadoSimulacion() {
        return estadoSimulacion;
    }

    public double getDensidad() {
        return densidad;
    }

    public TipoEstructuralCiudad getTipoEstructural() {
        return tipoEstructural;
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

    public double getRatioEnergetico() {
        return ratioEnergetico;
    }

    public double getRatioCoberturaServicios() {
        return ratioCoberturaServicios;
    }
}
