import org.synthcity.modulo_3.NivelEvaluacion;

private NivelEvaluacion determinarNivelEvaluacion(MetricaCiudad m) {

    int total = m.getTotalBloques();
    int activos = m.getBloquesActivos();
    double porcentaje = m.getPorcentajeActivos();

    // 1. SIN_DATOS
    if (total == 0) {
        return NivelEvaluacion.SIN_DATOS;
    }

    // 2. CRITICO
    if (activos == 0) {
        return NivelEvaluacion.CRITICO;
    }

    // 3. OPTIMO
    if (activos == total) {
        return NivelEvaluacion.OPTIMO;
    }

    // 4. FUNCIONAL (>= 60%)
    if (porcentaje >= 0.6) {
        return NivelEvaluacion.FUNCIONAL;
    }

    // 5. INESTABLE (resto)
    return NivelEvaluacion.INESTABLE;
}
