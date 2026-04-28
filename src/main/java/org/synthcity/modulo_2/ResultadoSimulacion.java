package org.synthcity.modulo_2;

import java.util.Collections;
import java.util.List;
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
    private final List<EstadoCiclo> ciclos;
    private final int ciclosEjecutados;
    private final MotivoParadaSimulacion motivoParada;
    private final int contaminacionAcumulada;
    private final double coberturaServiciosPonderada;
    private final double eficienciaTransporte;
    private final double estabilidadMedia;
    private final double bienestarMinimo;
    private final double bienestarMaximo;
    private final double bienestarUltimo;
    private final double tendenciaEstabilidad;
    private final double tendenciaContaminacion;
    private final boolean necesidadExpansionDetectada;
    private final boolean historialReiniciadoPorExpansion;

    private final EstadoCiclo estadoAgregado;


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
            double ratioCoberturaServicios,
            List<EstadoCiclo> ciclos,
            MotivoParadaSimulacion motivoParada,
            boolean necesidadExpansionDetectada,
            boolean historialReiniciadoPorExpansion) {

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
        if (ciclos == null) {
            throw new ResultadoSimulacionInvalidoException("La secuencia de ciclos no puede ser nula.");
        }
        if (motivoParada == null) {
            throw new ResultadoSimulacionInvalidoException("El motivo de parada no puede ser nulo.");
        }
        validarCiclos(ciclos);

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
        this.ciclos = List.copyOf(ciclos);
        this.ciclosEjecutados = ciclos.size();
        this.motivoParada = motivoParada;
        this.contaminacionAcumulada = calcularContaminacionAcumulada(ciclos, contaminacion);
        this.coberturaServiciosPonderada = obtenerCoberturaPonderada(ciclos, ratioCoberturaServicios);
        this.eficienciaTransporte = obtenerEficienciaTransporte(ciclos);
        this.estabilidadMedia = calcularEstabilidadMedia(ciclos, estabilidadBasica);
        this.bienestarMinimo = calcularBienestarMinimo(ciclos, bienestar);
        this.bienestarMaximo = calcularBienestarMaximo(ciclos, bienestar);
        this.bienestarUltimo = obtenerBienestarUltimo(ciclos, bienestar);
        this.tendenciaEstabilidad = calcularTendenciaEstabilidad(ciclos);
        this.tendenciaContaminacion = calcularTendenciaContaminacion(ciclos);
        this.necesidadExpansionDetectada = necesidadExpansionDetectada || ciclos.stream().anyMatch(EstadoCiclo::isNecesidadExpansionDetectada);
        this.historialReiniciadoPorExpansion = historialReiniciadoPorExpansion;
        this.estadoAgregado = ciclos.isEmpty() ? null : ciclos.getLast();
    }

    private static List<EstadoCiclo> crearHistorialCompatibilidad(
            int bloquesTotales,
            int bloquesActivos,
            EstadoSimulacion estadoSimulacion,
            double densidad,
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
            double ratioCoberturaServicios) {
        if (bloquesTotales == 0 || bloquesActivos == 0) {
            return List.of();
        }
        return List.of(new EstadoCiclo(
                1,
                energiaProducida,
                consumoEnergetico,
                equilibrioEnergetico,
                demandaServicios,
                coberturaServicios,
                clamp01(ratioCoberturaServicios),
                soporteTransporte > 0 ? 1.0 : 0.0,
                presionIndustrial,
                soporteTransporte,
                contaminacion,
                contaminacion,
                bienestar,
                estabilidadBasica,
                densidad,
                false,
                estadoSimulacion
        ));
    }

    private static MotivoParadaSimulacion inferirMotivoParada(int bloquesTotales, int bloquesActivos) {
        if (bloquesTotales == 0) {
            return MotivoParadaSimulacion.CIUDAD_VACIA;
        }
        if (bloquesActivos == 0) {
            return MotivoParadaSimulacion.SIN_BLOQUES_ACTIVOS;
        }
        return MotivoParadaSimulacion.CICLOS_COMPLETADOS;
    }

    private static void validarCiclos(List<EstadoCiclo> ciclos) {
        int contaminacionAnterior = -1;
        for (int i = 0; i < ciclos.size(); i++) {
            EstadoCiclo ciclo = ciclos.get(i);
            if (ciclo == null) {
                throw new ResultadoSimulacionInvalidoException("La secuencia de ciclos no puede contener nulos.");
            }
            if (ciclo.getNumeroCiclo() != i + 1) {
                throw new ResultadoSimulacionInvalidoException("Los ciclos deben estar numerados desde 1 sin saltos.");
            }
            if (ciclo.getContaminacionAcumulada() < contaminacionAnterior) {
                throw new ResultadoSimulacionInvalidoException("La contaminacion acumulada debe ser creciente.");
            }
            contaminacionAnterior = ciclo.getContaminacionAcumulada();
        }
    }

    private static int calcularContaminacionAcumulada(List<EstadoCiclo> ciclos, int valorCompatibilidad) {
        if (ciclos.isEmpty()) {
            return valorCompatibilidad;
        }
        return ciclos.get(ciclos.size() - 1).getContaminacionAcumulada();
    }

    private static double obtenerCoberturaPonderada(List<EstadoCiclo> ciclos, double ratioCoberturaServicios) {
        if (ciclos.isEmpty()) {
            return clamp01(ratioCoberturaServicios);
        }
        return ciclos.get(ciclos.size() - 1).getCoberturaServiciosPonderada();
    }

    private static double obtenerEficienciaTransporte(List<EstadoCiclo> ciclos) {
        if (ciclos.isEmpty()) {
            return 0.0;
        }
        return ciclos.get(ciclos.size() - 1).getEficienciaTransporte();
    }

    private static double calcularEstabilidadMedia(List<EstadoCiclo> ciclos, double valorCompatibilidad) {
        if (ciclos.isEmpty()) {
            return valorCompatibilidad;
        }
        return ciclos.stream().mapToDouble(EstadoCiclo::getEstabilidad).average().orElse(valorCompatibilidad);
    }

    private static double calcularBienestarMinimo(List<EstadoCiclo> ciclos, double valorCompatibilidad) {
        if (ciclos.isEmpty()) {
            return valorCompatibilidad;
        }
        return ciclos.stream().mapToDouble(EstadoCiclo::getBienestar).min().orElse(valorCompatibilidad);
    }

    private static double calcularBienestarMaximo(List<EstadoCiclo> ciclos, double valorCompatibilidad) {
        if (ciclos.isEmpty()) {
            return valorCompatibilidad;
        }
        return ciclos.stream().mapToDouble(EstadoCiclo::getBienestar).max().orElse(valorCompatibilidad);
    }

    private static double obtenerBienestarUltimo(List<EstadoCiclo> ciclos, double valorCompatibilidad) {
        if (ciclos.isEmpty()) {
            return valorCompatibilidad;
        }
        return ciclos.get(ciclos.size() - 1).getBienestar();
    }

    private static double calcularTendenciaEstabilidad(List<EstadoCiclo> ciclos) {
        if (ciclos.size() < 2) {
            return 0.0;
        }
        return ciclos.get(ciclos.size() - 1).getEstabilidad() - ciclos.get(0).getEstabilidad();
    }

    private static double calcularTendenciaContaminacion(List<EstadoCiclo> ciclos) {
        if (ciclos.size() < 2) {
            return 0.0;
        }

        return ciclos.get(ciclos.size() - 1).getContaminacionAcumulada()
                - ciclos.get(0).getContaminacionAcumulada();
    }

    private static double clamp01(double valor) {
        if (valor < 0.0) {
            return 0.0;
        }
        if (valor > 1.0) {
            return 1.0;
        }
        return valor;
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
                "Resumen de [%s]: Estado=%s | Ciclos=%d | Parada=%s | Bienestar=%.2f | Estabilidad=%.2f | BalanceEnergia=%d",
                nombreCiudad,
                estadoSimulacion,
                ciclosEjecutados,
                motivoParada,
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
        sb.append("Contaminacion acumulada: ").append(contaminacionAcumulada).append("\n");
        sb.append("Bienestar: ").append(bienestar).append("\n");
        sb.append("Estabilidad: ").append(estabilidadBasica).append("\n");
        sb.append("Ciclos ejecutados: ").append(ciclosEjecutados).append("\n");
        sb.append("Motivo de parada: ").append(motivoParada).append("\n");
        sb.append("Cobertura ponderada: ").append(coberturaServiciosPonderada).append("\n");
        sb.append("Eficiencia transporte: ").append(eficienciaTransporte).append("\n");
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

    public List<EstadoCiclo> getCiclos() {
        return Collections.unmodifiableList(ciclos);
    }

    public int getCiclosEjecutados() {
        return ciclosEjecutados;
    }

    public MotivoParadaSimulacion getMotivoParada() {
        return motivoParada;
    }

    public int getContaminacionAcumulada() {
        return contaminacionAcumulada;
    }

    public double getCoberturaServiciosPonderada() {
        return coberturaServiciosPonderada;
    }

    public double getEficienciaTransporte() {
        return eficienciaTransporte;
    }

    public double getEstabilidadMedia() {
        return estabilidadMedia;
    }

    public double getBienestarMinimo() {
        return bienestarMinimo;
    }

    public double getBienestarMaximo() {
        return bienestarMaximo;
    }

    public double getBienestarUltimo() {
        return bienestarUltimo;
    }

    public double getTendenciaEstabilidad() {
        return tendenciaEstabilidad;
    }

    public EstadoCiclo getEstadoAgregado() {
        return estadoAgregado;
    }

    public double getTendenciaContaminacion() {
        return tendenciaContaminacion;
    }

    public boolean isNecesidadExpansionDetectada() {
        return necesidadExpansionDetectada;
    }

    public boolean isHistorialReiniciadoPorExpansion() {
        return historialReiniciadoPorExpansion;
    }
}
