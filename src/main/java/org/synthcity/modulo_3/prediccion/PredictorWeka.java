package org.synthcity.modulo_3.prediccion;

import org.synthcity.modulo_3.ConversorClaseObjetivo;
import org.synthcity.modulo_3.RegistroDato;
import org.synthcity.modulo_3.TendenciaPredicha;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.Utils;
import weka.core.pmml.jaxbbindings.Predictor;

import java.util.ArrayList;
import java.util.List;

public class PredictorWeka implements Predictor {

    private J48 modelo;
    private Instances estructuraDataset;
    private boolean entrenado = false;

    public PredictorWeka() {
    }

    private Instances crearEstructura() {
        ArrayList<Attribute> atributos = new ArrayList<>();
        atributos.add(new Attribute("densidad"));
        atributos.add(new Attribute("ratio_energetico"));
        atributos.add(new Attribute("ratio_cobertura_servicios"));
        atributos.add(new Attribute("contaminacion"));
        atributos.add(new Attribute("contaminacion_acumulada"));
        atributos.add(new Attribute("estabilidad_media"));
        atributos.add(new Attribute("tendencia_estabilidad"));
        atributos.add(new Attribute("tendencia_contaminacion"));
        atributos.add(new Attribute("bienestar"));
        atributos.add(new Attribute("score_viabilidad"));
        atributos.add(new Attribute("ciclos_ejecutados"));
        atributos.add(new Attribute("colapso_detectado"));
        atributos.add(new Attribute("saturacion_detectada"));

        ArrayList<String> clases = new ArrayList<>();
        clases.add("CRITICO");
        clases.add("INESTABLE");
        clases.add("FUNCIONAL");
        clases.add("OPTIMO");
        atributos.add(new Attribute("objetivo", clases));

        Instances estructura = new Instances("Dataset", atributos, 0);
        estructura.setClassIndex(estructura.numAttributes() - 1);
        return estructura;
    }

    public void entrenar(List<RegistroDato> registros) throws Exception {
        if (registros == null || registros.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede entrenar: la lista de registros está vacía."
            );
        }

        estructuraDataset = crearEstructura();

        for (RegistroDato registro : registros) {
            if (registro.getObjetivo() == 0) continue;

            double[] features = registro.toArray();
            double[] valores = new double[estructuraDataset.numAttributes()];

            for (int i = 0; i < features.length; i++) {
                valores[i] = features[i];
            }

            String claseTexto = ConversorClaseObjetivo.convertir(registro.getObjetivo());
            double indiceClase = estructuraDataset.classAttribute()
                    .indexOfValue(claseTexto);
            valores[estructuraDataset.classIndex()] = indiceClase;

            DenseInstance instancia = new DenseInstance(1.0, valores);
            instancia.setDataset(estructuraDataset);
            estructuraDataset.add(instancia);
        }

        modelo = new J48();
        modelo.buildClassifier(estructuraDataset);
        entrenado = true;

        System.out.println("Modelo entrenado con " + estructuraDataset.size() + " registros.");
    }

    @Override
    public PredictionResult predecir(PredictionInput input) {
        if (!entrenado) {
            throw new IllegalStateException(
                    "El modelo no está entrenado. Llama a entrenar() o cargarModelo() primero."
            );
        }
        if (input == null) {
            throw new IllegalArgumentException("PredictionInput no puede ser null.");
        }

        try {
            double[] valores = new double[estructuraDataset.numAttributes()];

            valores[0]  = input.getDensidad();
            valores[1]  = input.getRatioEnergetico();
            valores[2]  = input.getRatioCoberturaServicios();
            valores[3]  = input.getContaminacion();
            valores[4]  = 0.0;
            valores[5]  = input.getEstabilidadBasica();
            valores[6]  = 0.0;
            valores[7]  = 0.0;
            valores[8]  = input.getBienestar();
            valores[9]  = input.getIndiceViabilidadBase() * 100.0;
            valores[10] = 0.0;
            valores[11] = 0.0;
            valores[12] = input.getIndiceSaturacion() > 0.8 ? 1.0 : 0.0;
            valores[estructuraDataset.classIndex()] = Utils.missingValue();

            DenseInstance instancia = new DenseInstance(1.0, valores);
            instancia.setDataset(estructuraDataset);

            double indiceClase = modelo.classifyInstance(instancia);
            String clasePredicha = estructuraDataset.classAttribute()
                    .value((int) indiceClase);

            return construirResultado(clasePredicha);

        } catch (Exception e) {
            throw new RuntimeException("Error al predecir: " + e.getMessage(), e);
        }
    }

    private PredictionResult construirResultado(String clasePredicha) {
        TendenciaPredicha tendencia;
        double scorePredicho;
        double confianza;
        String mensaje;

        switch (clasePredicha) {
            case "OPTIMO":
                tendencia     = TendenciaPredicha.MEJORA_PROBABLE;
                scorePredicho = 90.0;
                confianza     = 0.9;
                mensaje       = "El modelo Weka predice estado ÓPTIMO. La ciudad evoluciona favorablemente.";
                break;
            case "FUNCIONAL":
                tendencia     = TendenciaPredicha.ESTABLE;
                scorePredicho = 65.0;
                confianza     = 0.8;
                mensaje       = "El modelo Weka predice estado FUNCIONAL. La ciudad se mantiene estable.";
                break;
            case "INESTABLE":
                tendencia     = TendenciaPredicha.RIESGO_MODERADO;
                scorePredicho = 35.0;
                confianza     = 0.75;
                mensaje       = "El modelo Weka predice estado INESTABLE. La ciudad muestra signos de deterioro.";
                break;
            case "CRITICO":
                tendencia     = TendenciaPredicha.RIESGO_ALTO;
                scorePredicho = 10.0;
                confianza     = 0.85;
                mensaje       = "El modelo Weka predice estado CRÍTICO. Se requiere intervención urgente.";
                break;
            default:
                throw new IllegalArgumentException(
                        "Clase predicha no reconocida: " + clasePredicha
                );
        }

        return new PredictionResult(tendencia, scorePredicho, confianza, mensaje);
    }

    public void guardarModelo(String rutaModelo, String rutaEstructura) throws Exception {
        if (!entrenado) {
            throw new IllegalStateException(
                    "No se puede guardar: el modelo no está entrenado."
            );
        }
        SerializationHelper.write(rutaModelo, modelo);
        SerializationHelper.write(rutaEstructura, estructuraDataset);
        System.out.println("Modelo guardado en: " + rutaModelo);
    }

    public void cargarModelo(String rutaModelo, String rutaEstructura) throws Exception {
        this.modelo           = (J48) SerializationHelper.read(rutaModelo);
        this.estructuraDataset = (Instances) SerializationHelper.read(rutaEstructura);
        this.entrenado        = true;
        System.out.println("Modelo cargado desde: " + rutaModelo);
    }

    public boolean isEntrenado() {
        return entrenado;
    }
}