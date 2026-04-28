package org.synthcity.modulo_2;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_1.bloques.Bloque;

public class SimuladorCiudad implements Simulable {

    private final CalculadorEspacial calculadorEspacial;

    public SimuladorCiudad() {
        this(new CalculadorEspacial());
    }

    SimuladorCiudad(CalculadorEspacial calculadorEspacial) {
        if (calculadorEspacial == null) {
            throw new CiudadInvalidaParaSimulacionException("El calculador espacial no puede ser nulo.");
        }
        this.calculadorEspacial = calculadorEspacial;
    }

    @Override
    public ResultadoSimulacion simular(Ciudad ciudad) {
        validarEntrada(ciudad);

        boolean historialReiniciadoPorExpansion = ciudad.fueExpandidaDesdeUltimaSimulacion();
        ciudad.marcarSimulacionEjecutada();

        List<Bloque> bloques = ciudad.listarBloques();
        List<Bloque> activos = ciudad.getBloquesActivosConPosicion();
        Map<TipoBloque, Integer> conteos = calcularConteoPorTipo(bloques);

        if (bloques.isEmpty()) {
            return construirResultadoSinCiclos(
                    ciudad,
                    conteos,
                    EstadoSimulacion.CIUDAD_VACIA,
                    MotivoParadaSimulacion.CIUDAD_VACIA,
                    historialReiniciadoPorExpansion
            );
        }

        if (activos.isEmpty()) {
            return construirResultadoSinCiclos(
                    ciudad,
                    conteos,
                    EstadoSimulacion.SIN_BLOQUES_ACTIVOS,
                    MotivoParadaSimulacion.SIN_BLOQUES_ACTIVOS,
                    historialReiniciadoPorExpansion
            );
        }

        return ejecutarSimulacionIterativa(ciudad, bloques, activos, conteos, historialReiniciadoPorExpansion);
    }

    private void validarEntrada(Ciudad ciudad) {
        if (ciudad == null) {
            throw new CiudadInvalidaParaSimulacionException("La ciudad no puede ser nula.");
        }

        if (ciudad.getFilas() <= 0 || ciudad.getColumnas() <= 0) {
            throw new CiudadInvalidaParaSimulacionException("La ciudad tiene dimensiones invalidas.");
        }

        if (ciudad.getCapacidadMaxima() <= 0) {
            throw new CiudadInvalidaParaSimulacionException("La ciudad tiene capacidad maxima invalida.");
        }

        if (ciudad.listarBloques() == null || ciudad.listarBloquesActivos() == null
                || ciudad.getBloquesActivosConPosicion() == null) {
            throw new CiudadInvalidaParaSimulacionException("La API publica de la ciudad devuelve datos incoherentes.");
        }

        if (ciudad.getDensidad() < 0.0 || ciudad.getDensidad() > 1.0) {
            throw new CiudadInvalidaParaSimulacionException("La densidad de la ciudad es incoherente.");
        }

        if (ciudad.getTipoEstructural() == null) {
            throw new CiudadInvalidaParaSimulacionException("La ciudad no tiene tipo estructural valido.");
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

    private ResultadoSimulacion ejecutarSimulacionIterativa(
            Ciudad ciudad,
            List<Bloque> bloques,
            List<Bloque> activos,
            Map<TipoBloque, Integer> conteos,
            boolean historialReiniciadoPorExpansion) {

        List<EstadoCiclo> ciclos = new ArrayList<>();
        int contaminacionAcumulada = 0;
        int ciclosConDeficitEnergetico = 0;
        int ciclosConSaturacionCritica = 0;
        MotivoParadaSimulacion motivoParada = MotivoParadaSimulacion.CICLOS_COMPLETADOS;

        for (int numeroCiclo = 1; numeroCiclo <= ReglasSimulacion.MAX_CICLOS; numeroCiclo++) {
            EstadoCiclo ciclo = ejecutarCiclo(ciudad, bloques, activos, conteos, numeroCiclo, contaminacionAcumulada);
            ciclos.add(ciclo);
            contaminacionAcumulada = ciclo.getContaminacionAcumulada();

            if (ciclo.getEquilibrioEnergetico() < 0) {
                ciclosConDeficitEnergetico++;
            } else {
                ciclosConDeficitEnergetico = 0;
            }

            if (ciclo.getDensidad() >= ReglasSimulacion.UMBRAL_SATURACION_CRITICA) {
                ciclosConSaturacionCritica++;
            } else {
                ciclosConSaturacionCritica = 0;
            }

            motivoParada = comprobarCondicionParada(ciclosConDeficitEnergetico, ciclosConSaturacionCritica);
            if (motivoParada != MotivoParadaSimulacion.CICLOS_COMPLETADOS) {
                break;
            }
        }

        return construirResultadoSimulacion(ciudad, bloques, activos, conteos, ciclos, motivoParada, historialReiniciadoPorExpansion);
    }

    private EstadoCiclo ejecutarCiclo(
            Ciudad ciudad,
            List<Bloque> bloques,
            List<Bloque> activos,
            Map<TipoBloque, Integer> conteos,
            int numeroCiclo,
            int contaminacionAcumuladaAnterior) {

        double densidad = ciudad.getDensidad();
        double coberturaPonderada = calculadorEspacial.calcularCoberturaServiciosPonderada(activos);
        double eficienciaTransporte = calculadorEspacial.calcularEficienciaTransporte(activos);

        MetricasSimulacionBasica metricas = calcularMetricas(
                activos,
                bloques.size(),
                densidad,
                contaminacionAcumuladaAnterior,
                coberturaPonderada,
                eficienciaTransporte
        );

        int contaminacionAcumulada = contaminacionAcumuladaAnterior + metricas.getContaminacion();
        boolean necesitaExpansion = densidad >= ReglasSimulacion.UMBRAL_NECESIDAD_EXPANSION;
        EstadoSimulacion estado = determinarEstadoSimulacion(conteos, metricas, densidad);

        return new EstadoCiclo(
                numeroCiclo,
                metricas.getEnergiaProducida(),
                metricas.getConsumoEnergetico(),
                metricas.getEquilibrioEnergetico(),
                metricas.getDemandaServicios(),
                metricas.getCoberturaServicios(),
                coberturaPonderada,
                eficienciaTransporte,
                metricas.getPresionIndustrial(),
                metricas.getSoporteTransporte(),
                metricas.getContaminacion(),
                contaminacionAcumulada,
                metricas.getBienestar(),
                metricas.getEstabilidadBasica(),
                densidad,
                necesitaExpansion,
                estado
        );
    }

    private MotivoParadaSimulacion comprobarCondicionParada(int ciclosConDeficitEnergetico, int ciclosConSaturacionCritica) {
        if (ciclosConDeficitEnergetico >= ReglasSimulacion.CICLOS_CONSECUTIVOS_COLAPSO) {
            return MotivoParadaSimulacion.COLAPSO_ENERGETICO;
        }
        if (ciclosConSaturacionCritica >= ReglasSimulacion.CICLOS_CONSECUTIVOS_SATURACION) {
            return MotivoParadaSimulacion.SATURACION_CRITICA;
        }
        return MotivoParadaSimulacion.CICLOS_COMPLETADOS;
    }

    private EstadoSimulacion determinarEstadoSimulacion(Map<TipoBloque, Integer> conteos, MetricasSimulacionBasica metricas, double densidad) {
        int totales = conteos.values().stream().mapToInt(Integer::intValue).sum();

        if (totales == 0) {
            return EstadoSimulacion.CIUDAD_VACIA;
        }
        if (metricas.getConsumoEnergetico() == 0 && metricas.getEnergiaProducida() == 0
                && metricas.getCoberturaServicios() == 0 && metricas.getDemandaServicios() == 0) {
            return EstadoSimulacion.SIN_BLOQUES_ACTIVOS;
        }
        if (metricas.getEnergiaProducida() < metricas.getConsumoEnergetico()) {
            return EstadoSimulacion.DEFICIT_ENERGETICO;
        }
        if (metricas.getCoberturaServicios() < metricas.getDemandaServicios()) {
            return EstadoSimulacion.DEFICIT_SERVICIOS;
        }
        if (densidad >= ReglasSimulacion.UMBRAL_SATURACION_CRITICA) {
            return EstadoSimulacion.SATURACION_CRITICA;
        }
        if (densidad >= ReglasSimulacion.PENALIZACION_DENSIDAD_ALTA) {
            return EstadoSimulacion.DESEQUILIBRIO_ESTRUCTURAL;
        }

        return metricas.getEstabilidadBasica() >= 0.65 ? EstadoSimulacion.SIMULACION_ESTABLE : EstadoSimulacion.SIMULACION_INESTABLE;
    }

    private ResultadoSimulacion construirResultadoSinCiclos(
            Ciudad ciudad,
            Map<TipoBloque, Integer> conteos,
            EstadoSimulacion estado,
            MotivoParadaSimulacion motivoParada,
            boolean historialReiniciadoPorExpansion) {

        int bloquesTotales = ciudad.listarBloques().size();
        int bloquesActivos = ciudad.listarBloquesActivos().size();
        int bloquesInactivos = bloquesTotales - bloquesActivos;

        return new ResultadoSimulacion(
                ciudad.getNombre(),
                ciudad.getFilas(),
                ciudad.getColumnas(),
                ciudad.getCapacidadMaxima(),
                bloquesTotales,
                bloquesActivos,
                bloquesInactivos,
                conteos,
                estado,
                ciudad.getDensidad(),
                ciudad.getTipoEstructural(),
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0.0,
                0.0,
                1.0,
                1.0,
                List.of(),
                motivoParada,
                false,
                historialReiniciadoPorExpansion
        );
    }

    private ResultadoSimulacion construirResultadoSimulacion(
            Ciudad ciudad,
            List<Bloque> bloques,
            List<Bloque> activos,
            Map<TipoBloque, Integer> conteos,
            List<EstadoCiclo> ciclos,
            MotivoParadaSimulacion motivoParada,
            boolean historialReiniciadoPorExpansion) {

        EstadoCiclo ultimo = ciclos.get(ciclos.size() - 1);
        int bloquesTotales = bloques.size();
        int bloquesActivos = activos.size();
        int bloquesInactivos = bloquesTotales - bloquesActivos;
        double estabilidadMedia = ciclos.stream().mapToDouble(EstadoCiclo::getEstabilidad).average().orElse(0.0);

        EstadoSimulacion estadoAgregado = determinarEstadoAgregado(ultimo.getEstadoSimulacion(), motivoParada);
        double ratioEnergetico = calcularRatio(ultimo.getEnergiaProducida(), ultimo.getConsumoEnergetico());
        double ratioCobertura = calcularRatio(ultimo.getCoberturaServicios(), ultimo.getDemandaServicios());

        return new ResultadoSimulacion(
                ciudad.getNombre(),
                ciudad.getFilas(),
                ciudad.getColumnas(),
                ciudad.getCapacidadMaxima(),
                bloquesTotales,
                bloquesActivos,
                bloquesInactivos,
                conteos,
                estadoAgregado,
                ciudad.getDensidad(),
                ciudad.getTipoEstructural(),
                ultimo.getEnergiaProducida(),
                ultimo.getConsumoEnergetico(),
                ultimo.getEquilibrioEnergetico(),
                ultimo.getDemandaServicios(),
                ultimo.getCoberturaServicios(),
                ultimo.getPresionIndustrial(),
                ultimo.getSoporteTransporte(),
                ultimo.getContaminacionAcumulada(),
                ultimo.getBienestar(),
                estabilidadMedia,
                ratioEnergetico,
                ratioCobertura,
                ciclos,
                motivoParada,
                ciclos.stream().anyMatch(EstadoCiclo::isNecesidadExpansionDetectada),
                historialReiniciadoPorExpansion
        );
    }

    private EstadoSimulacion determinarEstadoAgregado(EstadoSimulacion estadoUltimoCiclo, MotivoParadaSimulacion motivoParada) {
        if (motivoParada == MotivoParadaSimulacion.COLAPSO_ENERGETICO) {
            return EstadoSimulacion.COLAPSO_ENERGETICO;
        }
        if (motivoParada == MotivoParadaSimulacion.SATURACION_CRITICA) {
            return EstadoSimulacion.SATURACION_CRITICA;
        }
        return estadoUltimoCiclo;
    }

    private MetricasSimulacionBasica calcularMetricas(
            List<Bloque> activos,
            int bloquesTotales,
            double densidad,
            int contaminacionAcumuladaAnterior,
            double coberturaPonderada,
            double eficienciaTransporte) {

        int energia = activos.stream().mapToInt(Bloque::getProduccionEnergia).sum();
        int consumo = activos.stream().mapToInt(Bloque::getConsumoEnergetico).sum();
        int demanda = activos.stream().mapToInt(Bloque::getDemandaServicios).sum();
        int cobertura = activos.stream().mapToInt(Bloque::getCoberturaServicios).sum();
        int presion = activos.stream().mapToInt(Bloque::getPresionIndustrial).sum();
        int transporte = activos.stream().mapToInt(Bloque::getSoporteTransporte).sum();
        int contaminacionCiclo = activos.stream().mapToInt(Bloque::getContaminacion).sum();

        if (densidad >= ReglasSimulacion.PENALIZACION_DENSIDAD_ALTA) {
            contaminacionCiclo += ReglasSimulacion.EXTRA_CONTAMINACION_DENSIDAD;
        }

        int contaminacionAcumulada = contaminacionAcumuladaAnterior + contaminacionCiclo;
        double porcentajeActividad = bloquesTotales <= 0 ? 0.0 : (double) activos.size() / bloquesTotales;
        double bienestar = calcularBienestar(
                cobertura,
                contaminacionAcumulada,
                energia,
                consumo,
                demanda,
                porcentajeActividad,
                coberturaPonderada,
                eficienciaTransporte
        );

        double estabilidad = calcularEstabilidad(
                energia,
                consumo,
                cobertura,
                demanda,
                densidad,
                porcentajeActividad,
                contaminacionAcumulada,
                coberturaPonderada,
                eficienciaTransporte
        );

        return new MetricasSimulacionBasica(
                energia,
                consumo,
                demanda,
                cobertura,
                presion,
                transporte,
                contaminacionCiclo,
                bienestar,
                estabilidad
        );
    }

    private double calcularBienestar(
            int cobertura,
            int contaminacionAcumulada,
            int energia,
            int consumo,
            int demanda,
            double porcentajeActividad,
            double coberturaPonderada,
            double eficienciaTransporte) {

        double energiaOk = consumo <= 0 ? 1.0 : Math.min(1.0, (double) energia / consumo);
        double serviciosBaseOk = demanda <= 0 ? 1.0 : Math.min(1.0, (double) cobertura / demanda);
        double serviciosOk = serviciosBaseOk * 0.40 + coberturaPonderada * 0.60;
        double actividadOk = clamp(porcentajeActividad);
        double contaminacionOk = 1.0 - Math.min(1.0, contaminacionAcumulada / 150.0);

        return clamp(serviciosOk * 0.30
                + energiaOk * 0.25
                + eficienciaTransporte * 0.15
                + actividadOk * 0.15
                + contaminacionOk * 0.15);
    }

    private double calcularEstabilidad(
            int energia,
            int consumo,
            int cobertura,
            int demanda,
            double densidad,
            double porcentajeActividad,
            int contaminacionAcumulada,
            double coberturaPonderada,
            double eficienciaTransporte) {

        double energiaOk = consumo <= 0 ? 1.0 : Math.min(1.0, (double) energia / consumo);
        double serviciosBaseOk = demanda <= 0 ? 1.0 : Math.min(1.0, (double) cobertura / demanda);
        double serviciosOk = serviciosBaseOk * 0.50 + coberturaPonderada * 0.50;
        double densidadOk = 1.0 - Math.min(1.0, Math.max(0.0, densidad));
        double actividadOk = clamp(porcentajeActividad);
        double contaminacionOk = 1.0 - Math.min(1.0, contaminacionAcumulada / 200.0);

        return clamp(energiaOk * 0.30
                + serviciosOk * 0.25
                + densidadOk * 0.20
                + actividadOk * 0.10
                + eficienciaTransporte * 0.10
                + contaminacionOk * 0.05);
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
