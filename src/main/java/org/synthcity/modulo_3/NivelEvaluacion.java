public enum NivelEvaluacion {
    SIN_DATOS,
    CRITICO,
    INESTABLE,
    FUNCIONAL,
    OPTIMO

    public void determinarNivelEvaluacion(MetricaCiudad m){
        NivelEvaluacion nivelEvaluacion ;
        switch (m){
            case m.total == 0; nivelEvaluacion = NivelEvaluacion.SIN_DATOS; break;
            case m.activos == 0; nivelEvaluacion = NivelEvaluacion.CRITICO; break;
            case % m.activos < 50;  nivelEvaluacion = NivelEvaluacion.INESTABLE; break;
            case % m.activos >= 50;  nivelEvaluacion = NivelEvaluacion.FUNCIONAL; break;
            case % m.activos == 100;  nivelEvaluacion = NivelEvaluacion.OPTIMO; break;
        }
    }

}