package org.synthcity.modulo_4.persistencia.entidades;

public class ModeloWekaEntidad {
    private Long id;
    private String rutaModelo;
    private String rutaEstructura;
    private Long datasetId; // Clave foránea apuntando al dataset de origen
    private String algoritmo;

    public ModeloWekaEntidad() {}

    public ModeloWekaEntidad(Long id, String rutaModelo, String rutaEstructura, Long datasetId, String algoritmo) {
        this.id = id;
        this.rutaModelo = rutaModelo;
        this.rutaEstructura = rutaEstructura;
        this.datasetId = datasetId;
        this.algoritmo = algoritmo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRutaModelo() { return rutaModelo; }
    public void setRutaModelo(String rutaModelo) { this.rutaModelo = rutaModelo; }

    public String getRutaEstructura() { return rutaEstructura; }
    public void setRutaEstructura(String rutaEstructura) { this.rutaEstructura = rutaEstructura; }

    public Long getDatasetId() { return datasetId; }
    public void setDatasetId(Long datasetId) { this.datasetId = datasetId; }

    public String getAlgoritmo() { return algoritmo; }
    public void setAlgoritmo(String algoritmo) { this.algoritmo = algoritmo; }
}