package org.synthcity.modulo_2;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.synthcity.modulo_1.bloques.*;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.TipoBloque;

public class SimuladorCiudad {

    public ResultadoSimulacion simular(Ciudad ciudad) {
        validarEntrada(ciudad);

        List<Bloque> bloques = ciudad.listarBloques();
        List<Bloque> activos = ciudad.listarBloquesActivos();
        double densidad = ciudad.getDensidad();

        Map<TipoBloque, Integer> conteos = calcularConteoPorTipo(bloques);
        MetricasSimulacionBasica metricas = calcularMetricas(activos, bloques.size(), densidad);
        EstadoSimulacion estado = determinarEstadoSimulacion(conteos, metricas, densidad);

        return construirResultadoSimulacion(ciudad, conteos, metricas, estado);
    }

    private void validarEntrada(Ciudad ciudad) {
        if (ciudad == null) {
            throw new CiudadInvalidaParaSimulacionException("La ciudad no puede ser nula.");
        }

        if (ciudad.getFilas() <= 0 || ciudad.getColumnas() <= 0) {
            throw new CiudadInvalidaParaSimulacionException("La ciudad tiene dimensiones inválidas.");
        }

        if (ciudad.getCapacidadMaxima() <= 0) {
            throw new CiudadInvalidaParaSimulacionException("La ciudad tiene capacidad máxima inválida.");
        }

        if (ciudad.listarBloques() == null || ciudad.listarBloquesActivos() == null) {
            throw new CiudadInvalidaParaSimulacionException("La API pública de la ciudad devuelve datos incoherentes.");
        }

        if (ciudad.getDensidad() < 0.0 || ciudad.getDensidad() > 1.0) {
            throw new CiudadInvalidaParaSimulacionException("La densidad de la ciudad es incoherente.");
        }

        if (ciudad.getTipoEstructural() == null) {
            throw new CiudadInvalidaParaSimulacionException("La ciudad no tiene tipo estructural válido.");
        }
    }

    public Map<TipoBloque, Integer> calcularConteoPorTipo(List<Bloque> bloques) {
        Map<TipoBloque, Integer> conteos = new EnumMap<>(TipoBloque.class);

        for (TipoBloque tipo : TipoBloque.values()) {
            conteos.put(tipo, 0);
        }

        if (bloques != null) {
            for (Bloque bloque : bloques) {
                if (bloque != null && bloque.getTipo() != null) {
                    conteos.put(bloque.getTipo(), conteos.get(bloque.getTipo()) + 1);
                }
            }
        }
        return conteos;
    }

    private EstadoSimulacion determinarEstadoSimulacion(Map<TipoBloque, Integer> conteos, MetricasSimulacionBasica metricas, double densidad) {
        int totales = conteos.values().stream().mapToInt(Integer::intValue).sum();

        if (totales == 0) {
            return EstadoSimulacion.CIUDAD_VACIA;
        }
        if (metricas.getConsumoEnergetico() == 0 && metricas.getEnergiaProducida() == 0 && metricas.getCoberturaServicios() == 0 && metricas.getDemandaServicios() == 0) {
            return EstadoSimulacion.SIN_BLOQUES_ACTIVOS;
        }
        if (metricas.getEnergiaProducida() < metricas.getConsumoEnergetico()) {
            return EstadoSimulacion.DEFICIT_ENERGETICO;
        }
        if (metricas.getCoberturaServicios() < metricas.getDemandaServicios()) {
            return EstadoSimulacion.DEFICIT_SERVICIOS;
        }
        if (densidad >= ReglasSimulacion.PENALIZACION_DENSIDAD_ALTA) {
            return EstadoSimulacion.DESEQUILIBRIO_ESTRUCTURAL;
        }

        return metricas.getEstabilidadBasica() >= 0.65 ? EstadoSimulacion.SIMULACION_ESTABLE : EstadoSimulacion.SIMULACION_INESTABLE;
    }

    private ResultadoSimulacion construirResultadoSimulacion(Ciudad ciudad, Map<TipoBloque, Integer> conteos, MetricasSimulacionBasica metricas, EstadoSimulacion estado) {
        int bloquesTotales = ciudad.listarBloques().size();
        int bloquesActivos = ciudad.listarBloquesActivos().size();
        int bloquesInactivos = bloquesTotales - bloquesActivos;

        double ratioEnergetico = calcularRatio(metricas.getEnergiaProducida(), metricas.getConsumoEnergetico());
        double ratioCobertura = calcularRatio(metricas.getCoberturaServicios(), metricas.getDemandaServicios());

        return new ResultadoSimulacion(ciudad.getNombre(), ciudad.getFilas(), ciudad.getColumnas(), ciudad.getCapacidadMaxima(), bloquesTotales, bloquesActivos, bloquesInactivos, conteos, estado, ciudad.getDensidad(), ciudad.getTipoEstructural(), metricas.getEnergiaProducida(), metricas.getConsumoEnergetico(), metricas.getEquilibrioEnergetico(), metricas.getDemandaServicios(), metricas.getCoberturaServicios(), metricas.getPresionIndustrial(), metricas.getSoporteTransporte(), metricas.getContaminacion(), metricas.getBienestar(), metricas.getEstabilidadBasica(), ratioEnergetico, ratioCobertura);
    }


    private MetricasSimulacionBasica calcularMetricas(List<Bloque> activos, int bloquesTotales, double densidad) {
        int energia = activos.stream().mapToInt(Bloque::getProduccionEnergia).sum();
        int consumo = activos.stream().mapToInt(Bloque::getConsumoEnergetico).sum();
        int demanda = activos.stream().mapToInt(Bloque::getDemandaServicios).sum();
        int cobertura = activos.stream().mapToInt(Bloque::getCoberturaServicios).sum();
        int presion = activos.stream().mapToInt(Bloque::getPresionIndustrial).sum();
        int transporte = activos.stream().mapToInt(Bloque::getSoporteTransporte).sum();
        int contaminacion = activos.stream().mapToInt(Bloque::getContaminacion).sum();

        if (densidad >= ReglasSimulacion.PENALIZACION_DENSIDAD_ALTA) {
            contaminacion += ReglasSimulacion.EXTRA_CONTAMINACION_DENSIDAD;
        }

        double porcentajeActividad = bloquesTotales <= 0 ? 0.0 : (double) activos.size() / bloquesTotales;

        double bienestar = calcularBienestar(cobertura, transporte, contaminacion, energia, consumo, demanda, porcentajeActividad);

        double estabilidad = calcularEstabilidad(energia, consumo, cobertura, demanda, densidad, porcentajeActividad);

        return new MetricasSimulacionBasica(energia, consumo, demanda, cobertura, presion, transporte, contaminacion, bienestar, estabilidad);
    }


    private double calcularBienestar(int cobertura, int transporte, int contaminacion, int energia, int consumo, int demanda, double porcentajeActividad) {
        double energiaOk = consumo <= 0 ? 1.0 : Math.min(1.0, (double) energia / consumo);
        double serviciosOk = demanda <= 0 ? 1.0 : Math.min(1.0, (double) cobertura / demanda);
        double transporteOk = Math.min(1.0, transporte / 10.0);
        double actividadOk = clamp(porcentajeActividad);
        double contaminacionOk = 1.0 - Math.min(1.0, contaminacion / 100.0);

        return clamp(serviciosOk * 0.30 + energiaOk * 0.25 + transporteOk * 0.15 + actividadOk * 0.15 + contaminacionOk * 0.15);
    }

    private double calcularEstabilidad(int energia, int consumo, int cobertura, int demanda, double densidad, double porcentajeActividad) {
        double energiaOk = consumo <= 0 ? 1.0 : Math.min(1.0, (double) energia / consumo);
        double serviciosOk = demanda <= 0 ? 1.0 : Math.min(1.0, (double) cobertura / demanda);
        double densidadOk = 1.0 - Math.min(1.0, Math.max(0.0, densidad));
        double actividadOk = clamp(porcentajeActividad);

        return clamp(energiaOk * 0.35 + serviciosOk * 0.25 + densidadOk * 0.25 + actividadOk * 0.15);
    }

    private double clamp(double valor) {
        if (valor < 0.0) {
            return 0.0;
        }
        return Math.min(valor, 1.0);
    }

    private double calcularRatio(int numerador, int denominador) {
        if (denominador == 0) {
            return numerador > 0 ? numerador : 1.0;
        }
        return (double) numerador / denominador;
    }
}