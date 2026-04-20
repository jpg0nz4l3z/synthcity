package org.synthcity.modulo_2;

import java.util.Map;
import org.synthcity.modulo_1.TipoBloque;
import org.synthcity.modulo_1.TipoEstructuralCiudad;

public class ResultadoSimulacion {

    /** Nombre identificador de la ciudad. Heredado de Sprint 1. */
    private final String nombreCiudad;
    /** Número de filas del grid urbano. */
    private final int filas;
    /** Número de columnas del grid urbano. */
    private final int columnas;
    /** Capacidad total de bloques que admite la ciudad. */
    private final int capacidadMaxima;

    /** Total de bloques presentes en el tablero. */
    private final int bloquesTotales;
    /** Cantidad de bloques con estado activo. */
    private final int bloquesActivos;
    /** Cantidad de bloques con estado inactivo. */
    private final int bloquesInactivos;

    /** Mapa completo con la distribución de bloques por tipo. Todos los tipos del enum están presentes. */
    private final Map<TipoBloque, Integer> conteoPorTipo;
    /** Estado técnico resultante del ciclo de simulación. */
    private final EstadoSimulacion estadoSimulacion;

    /** Energía total generada por los bloques de tipo energía activos. */
    private final int energiaProducida;
    /** Consumo total de energía de todos los bloques activos. */
    private final int consumoEnergetico;
    /** Necesidad de servicios generada principalmente por bloques residenciales. */
    private final int demandaServicios;
    /** Capacidad de servicios proporcionada por bloques de servicios activos. */
    private final int coberturaServicios;
    /** Nivel de carga o presión introducida por la actividad industrial. */
    private final int presionIndustrial;
    /** Capacidad de conectividad o transporte instalada. */
    private final int soporteTransporte;
    /** Nivel de emisiones totales, incluyendo base industrial y penalización por densidad. */
    private final int contaminacion;

    // --- ATRIBUTOS RATIOS (Relaciones proporcionales) ---

    /** Relación entre bloques ocupados y dimensiones totales. Determina penalizaciones de contaminación. */
    private final double densidad;
    /** Clasificación estructural de la ciudad (Pueblo, Ciudad, Metrópolis). */
    private final TipoEstructuralCiudad tipoEstructural;
    /** Proporción entre producción y consumo energético. */
    private final double ratioEnergetico;
    /** Proporción entre cobertura real y demanda de servicios. */
    private final double ratioCoberturaServicios;

    // --- ATRIBUTOS DERIVADOS Y VARIABLES COMPUESTAS ---

    /** Diferencia neta entre energía producida y consumida (energiaProducida - consumoEnergetico). */
    private final int equilibrioEnergetico;
    /** Índice de calidad de vida ponderando servicios, transporte y contaminación. */
    private final double bienestar;
    /** Indicador de salud general del sistema basado en el equilibrio de todas las variables. */
    private final double estabilidadBasica;


    // Constructor con todos los atributos inicializados
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
            int demandaServicios ,
            int coberturaServicios,
            int presionIndustrial,
            int soporteTransporte,
            int contaminacion,
            double bienestar,
            double estabilidadBasica,
            double ratioEnergetico,
            double ratioCoberturaServicios) {

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

        // 1. Validación de Ratios: No pueden ser NaN ni Infinitos
        if (!Double.isFinite(this.ratioEnergetico) || !Double.isFinite(this.ratioCoberturaServicios)) {
            throw new ResultadoSimulacionInvalidoException("Ratios no válidos detectados (NaN o Infinity).");
        }
        // 2. Invariante de conteo de bloques: la suma debe ser exacta
        if (this.bloquesTotales != (this.bloquesActivos + this.bloquesInactivos)) {
            throw new ResultadoSimulacionInvalidoException("Inconsistencia en el conteo de bloques: la suma de activos e inactivos no coincide con el total.");
        }

        // 3. Invariante de energía: el equilibrio debe ser la diferencia exacta entre producción y consumo
        if (this.equilibrioEnergetico != (this.energiaProducida - this.consumoEnergetico)) {
            throw new ResultadoSimulacionInvalidoException("Inconsistencia energética: el equilibrio no corresponde a la diferencia entre producción y consumo.");
        }

        // 4. Invariante de densidad: debe estar en un rango lógico (ejemplo: entre 0 y 1 o según lo defina M1)
        if (this.densidad < 0) {
            throw new ResultadoSimulacionInvalidoException("La densidad no puede ser un valor negativo.");
        }

        // 5. Invariante de mapa: el conteo por tipo no puede ser nulo y debe ser completo
        for (TipoBloque tipo : TipoBloque.values()) {
            if (!this.conteoPorTipo.containsKey(tipo)) {
                throw new ResultadoSimulacionInvalidoException("Contrato incompleto: falta el tipo de bloque " + tipo + " en el mapa.");
            }
        }
    }

    // GETTERS
    public String getNombreCiudad() { return nombreCiudad; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public int getBloquesTotales() { return bloquesTotales; }
    public int getBloquesActivos() { return bloquesActivos; }
    public int getBloquesInactivos() { return bloquesInactivos; }
    public Map<TipoBloque, Integer> getConteoPorTipo() { return conteoPorTipo; }
    public EstadoSimulacion getEstadoSimulacion() { return estadoSimulacion; }

    public double getDensidad() {return densidad;}
    public TipoEstructuralCiudad getTipoEstructural() {return tipoEstructural;}
    public int getEnergiaProducida() {return energiaProducida;}
    public int getConsumoEnergetico() {return consumoEnergetico;}
    public int getEquilibrioEnergetico() {return equilibrioEnergetico;}
    public int getDemandaServicios() {return demandaServicios;}
    public int getCoberturaServicios() {return coberturaServicios;}
    public int getPresionIndustrial() {return presionIndustrial;}
    public int getSoporteTransporte() {return soporteTransporte;}
    public int getContaminacion() {return contaminacion;}
    public double getBienestar() {return bienestar;}
    public double getEstabilidadBasica() {return estabilidadBasica;}
    public double getRatioEnergetico() {return ratioEnergetico;}
    public double getRatioCoberturaServicios() {return ratioCoberturaServicios;}

    // MÉTODOS FUNCIONALES
    public int getCantidadPorTipo(TipoBloque tipo) {
        if (tipo == null) {
            return 0;
        }
        return conteoPorTipo.getOrDefault(tipo, 0);
    }

    public boolean ciudadEstaVacia() {
        return bloquesTotales == 0;
    }

    public boolean hayBloquesActivos() {return bloquesActivos > 0;}

    public boolean hayDeficitEnergetico(){return equilibrioEnergetico < 0;}

    public boolean hayDeficitServicios(){return ratioCoberturaServicios < 1.0;}

    /**
     * Devuelve una descripción compacta del resultado del ciclo.
     * Útil para trazabilidad rápida y depuración.
     */
    public String getResumenSimulacion() {
        return String.format(
                "Resumen de [%s]: Estado: %s | Bienestar: %.2f | Estabilidad: %.2f | Bal. Energía: %d",
                nombreCiudad, estadoSimulacion, bienestar, estabilidadBasica, equilibrioEnergetico
        );
    }

    // toString para depuración
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESULTADO DE SIMULACIÓN ===\n");
        sb.append("Ciudad:           ").append(nombreCiudad).append("\n");
        sb.append("Tipo Estructural: ").append(tipoEstructural).append("\n");
        sb.append("Dimensiones:      ").append(filas).append("x").append(columnas).append("\n");
        sb.append("Capacidad maxima: ").append(capacidadMaxima).append("\n");
        sb.append("Densidad:         ").append(densidad).append("\n");

        sb.append("Bloques totales:  ").append(bloquesTotales).append("\n");
        sb.append("Bloques activos:  ").append(bloquesActivos).append("\n");
        sb.append("Bloques inactivos:").append(bloquesInactivos).append("\n");

        // Sección de Energía
        sb.append("Energía Prod/Cons:").append(energiaProducida).append(" / ").append(consumoEnergetico).append("\n");
        sb.append("Balance Energético:").append(equilibrioEnergetico).append("\n");

        // Sección de Servicios e Impacto
        sb.append("Servicios (D/C):  ").append(demandaServicios).append(" / ").append(coberturaServicios).append("\n");
        sb.append("Presión Industrial:").append(presionIndustrial).append("\n");
        sb.append("Soporte Transporte:").append(soporteTransporte).append("\n");
        sb.append("Contaminación:    ").append(contaminacion).append("\n");

        // Sección de Índices
        sb.append("Bienestar:        ").append(bienestar).append("\n");
        sb.append("Estabilidad:      ").append(estabilidadBasica).append("\n");
        sb.append("Ratio Energético:  ").append(ratioEnergetico).append("\n");
        sb.append("Ratio Servicios:  ").append(ratioCoberturaServicios).append("\n");
        sb.append("Estado:           ").append(estadoSimulacion).append("\n");

        sb.append("Distribución por tipo:\n");
        for (TipoBloque tipo : TipoBloque.values()) {
            sb.append("  ").append(tipo).append(": ").append(getCantidadPorTipo(tipo)).append("\n");
        }

        return sb.toString();
    }
}
