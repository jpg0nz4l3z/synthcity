package org.synthcity.modulo_2;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.synthcity.modulo_1.Bloque;
import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.TipoBloque;
// Importamos el tipo estructural directamente del Módulo 1, nada de stubs inventados
import org.synthcity.modulo_1.TipoEstructuralCiudad;

public class SimuladorCiudad {

    // =========================================================
    // ESTRUCTURA EXACTA DEL METODO
    // =========================================================
    public ResultadoSimulacion simular(Ciudad ciudad) {

        validarEntrada(ciudad);

        // extraer estructura y bloques
        List<Bloque> bloques = ciudad.listarBloques();
        List<Bloque> activos = ciudad.listarBloquesActivos();
        double densidad = ciudad.getDensidad();

        // delegar cálculos
        Map<TipoBloque, Integer> conteos = calcularConteoPorTipo(bloques);
        MetricasSimulacionBasica metricas = calcularMetricas(activos, densidad);
        EstadoSimulacion estado = determinarEstado(conteos, metricas);

        return construirResultado(ciudad, conteos, metricas, estado);
    }

    // =========================================================
    // MÉTODOS AUXILIARES DE LA PERSONA 1
    // =========================================================

    private void validarEntrada(Ciudad ciudad) {
        if (ciudad == null) {
            throw new CiudadInvalidaParaSimulacionException("La ciudad no puede ser nula");
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

    // Polimorfismo puro con Streams (Exigencia Pág. 7 y 13)
    private MetricasSimulacionBasica calcularMetricas(List<Bloque> activos, double densidad) {

        int energia = activos.stream().mapToInt(Bloque::getProduccionEnergia).sum();
        int consumo = activos.stream().mapToInt(Bloque::getConsumoEnergetico).sum();
        int demanda = activos.stream().mapToInt(Bloque::getDemandaServicios).sum();
        int cobertura = activos.stream().mapToInt(Bloque::getCoberturaServicios).sum();
        int presion = activos.stream().mapToInt(Bloque::getPresionIndustrial).sum();
        int transporte = activos.stream().mapToInt(Bloque::getSoporteTransporte).sum();

        // Contaminación: híbrido entre stream y regla de sistema
        int contaminacion = activos.stream().mapToInt(Bloque::getContaminacion).sum();
        if (densidad >= ReglasSimulacion.PENALIZACION_DENSIDAD_ALTA) {
            contaminacion += ReglasSimulacion.EXTRA_CONTAMINACION_DENSIDAD;
        }

        int equilibrio = energia - consumo;

        double bienestar = calcularBienestar(cobertura, transporte, contaminacion, energia, consumo, demanda);
        double estabilidad = calcularEstabilidad(energia, consumo, cobertura, densidad);

        return new MetricasSimulacionBasica(
                energia, consumo, equilibrio, demanda, cobertura,
                presion, transporte, contaminacion, bienestar, estabilidad
        );
    }

    // Fórmulas compuestas (Reglas de negocio del sistema)
    private double calcularBienestar(int cobertura, int transporte, int contaminacion, int energia, int consumo, int demanda) {
        double partePositiva = cobertura + transporte;
        int penalizacion = 0;
        if (energia < consumo) penalizacion += 5;
        if (cobertura < demanda) penalizacion += 5;
        return Math.max(0.0, partePositiva - (contaminacion + penalizacion));
    }

    private double calcularEstabilidad(int energia, int consumo, int cobertura, double densidad) {
        double base = cobertura + (energia - consumo);
        if (densidad >= ReglasSimulacion.PENALIZACION_DENSIDAD_ALTA) {
            base -= 10.0;
        }
        return base;
    }

    private EstadoSimulacion determinarEstado(Map<TipoBloque, Integer> conteos, MetricasSimulacionBasica metricas) {
        int totales = conteos.values().stream().mapToInt(Integer::intValue).sum();

        if (totales == 0) return EstadoSimulacion.CIUDAD_VACIA;
        // Asumimos que si no hay consumo ni energía generada ni cobertura, no hay activos funcionales
        if (metricas.getEnergiaProducida() < metricas.getConsumoEnergetico()) return EstadoSimulacion.DEFICIT_ENERGETICO;
        if (metricas.getCoberturaServicios() < metricas.getDemandaServicios()) return EstadoSimulacion.DEFICIT_SERVICIOS;

        return metricas.getEstabilidadBasica() >= 50.0 ? EstadoSimulacion.SIMULACION_ESTABLE : EstadoSimulacion.SIMULACION_INESTABLE;
    }

    // Ensamblaje final conectando con el constructor de Diego
    private ResultadoSimulacion construirResultado(Ciudad ciudad, Map<TipoBloque, Integer> conteos,
                                                   MetricasSimulacionBasica metricas, EstadoSimulacion estado) {

        int totales = ciudad.listarBloques().size();
        int activos = ciudad.listarBloquesActivos().size();
        int inactivos = totales - activos;

        // Ratios exigidos por el constructor de Persona 2 (Diego)
        double ratioE = metricas.getConsumoEnergetico() > 0 ? (double) metricas.getEnergiaProducida() / metricas.getConsumoEnergetico() : (metricas.getEnergiaProducida() > 0 ? 999.0 : 1.0);
        double ratioS = metricas.getDemandaServicios() > 0 ? (double) metricas.getCoberturaServicios() / metricas.getDemandaServicios() : (metricas.getCoberturaServicios() > 0 ? 999.0 : 1.0);

        return new ResultadoSimulacion(
                ciudad.getNombre(),
                ciudad.getFilas(),
                ciudad.getColumnas(),
                ciudad.capacidadMaxima(),
                totales, activos, inactivos,
                conteos, estado,
                ciudad.getDensidad(),
                ciudad.getTipoEstructural(), // <--- Viene del Módulo 1
                metricas.getEnergiaProducida(), metricas.getConsumoEnergetico(), metricas.getEquilibrioEnergetico(),
                metricas.getDemandaServicios(), metricas.getCoberturaServicios(),
                metricas.getPresionIndustrial(), metricas.getSoporteTransporte(), metricas.getContaminacion(),
                metricas.getBienestar(), metricas.getEstabilidadBasica(),
                ratioE, ratioS
        );
    }
}