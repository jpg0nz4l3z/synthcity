package org.synthcity.modulo_4;

/*class PresentadorCiudadTest {

    @Test
    void presentar_deberia_devolver_salida_valida_si_resultado_es_correcto() {
        PresentadorCiudad presentador = new PresentadorCiudad();

        SalidaTexto salida = presentador.presentar(crearResultadoValido());

        assertNotNull(salida);
        assertTrue(salida.getContenido().contains("NeoMadrid"));
        assertTrue(salida.getContenido().contains("FUNCIONAL"));
        assertTrue(salida.getContenido().contains("Bloques totales: 10"));
    }

    @Test
    void presentar_deberia_lanzar_excepcion_si_resultado_es_null() {
        PresentadorCiudad presentador = new PresentadorCiudad();

        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> presentador.presentar(null)
        );

        assertEquals("ResultadoEvaluacion no puede ser null.", ex.getMessage());
    }

    @Test
    void presentar_deberia_lanzar_excepcion_si_nombre_es_null() {
        PresentadorCiudad presentador = new PresentadorCiudad();
        ResultadoEvaluacion resultado = new ResultadoEvaluacion(
                null,
                crearMetricaValida(),
                NivelEvaluacion.FUNCIONAL,
                "Mensaje valido"
        );

        FormatoSalidaException ex = assertThrows(
                FormatoSalidaException.class,
                () -> presentador.presentar(resultado)
        );

        assertEquals("El nombre de la ciudad es obligatorio.", ex.getMessage());
    }

    @Test
    void constructor_deberia_lanzar_excepcion_si_metrica_es_null() {
        ResultadoSimulacionInvalidoException ex = assertThrows(
                ResultadoSimulacionInvalidoException.class,
                () -> new ResultadoEvaluacion("NeoMadrid", null, NivelEvaluacion.FUNCIONAL, "Mensaje valido")
        );

        assertEquals("El resultado de evaluación no puede estar incompleto.", ex.getMessage());
    }

    @Test
    void generarResumen_deberia_unir_ciudad_evaluacion_y_prediccion() {
        PresentadorCiudad presentador = new PresentadorCiudad();
        Ciudad ciudad = new Ciudad("NeoTokyo", 10, 10);
        ResultadoEvaluacion evaluacion = crearResultadoValido("NeoTokyo");
        PredictionResult prediccion = new PredictionResult(
                TendenciaPredicha.MEJORA_PROBABLE,
                95.5,
                0.85,
                "Mejora constante"
        );

        SalidaTexto resultado = presentador.generarResumen(ciudad, evaluacion, prediccion);

        assertNotNull(resultado);
        assertEquals("NeoTokyo", resultado.getNombreCiudad());
        assertEquals("Resumen Breve", resultado.getTitulo());
        assertTrue(resultado.getContenido().contains("NeoTokyo"));
        assertTrue(resultado.getContenido().contains("FUNCIONAL"));
        assertTrue(resultado.getContenido().contains("95.5"));
        assertTrue(resultado.getContenido().contains("MEJORA_PROBABLE"));
    }

    private ResultadoEvaluacion crearResultadoValido() {
        return crearResultadoValido("NeoMadrid");
    }

    private ResultadoEvaluacion crearResultadoValido(String nombre) {
        return new ResultadoEvaluacion(
                nombre,
                crearMetricaValida(nombre),
                NivelEvaluacion.FUNCIONAL,
                "La ciudad presenta un nivel suficiente de actividad.",
                72.5,
                java.util.EnumSet.noneOf(org.synthcity.modulo_3.AlertaEvaluacion.class),
                "Sin riesgos relevantes detectados."
        );
    }

    private MetricaCiudad crearMetricaValida() {
        return crearMetricaValida("NeoMadrid");
    }

    private MetricaCiudad crearMetricaValida(String nombre) {
        return new MetricaCiudad(crearResultadoSimulacionValido(nombre));
    }

    private ResultadoSimulacion crearResultadoSimulacionValido(String nombre) {
        Map<TipoBloque, Integer> conteo = crearConteoBase();
        conteo.put(TipoBloque.RESIDENCIAL, 5);
        conteo.put(TipoBloque.ENERGIA, 2);
        conteo.put(TipoBloque.INDUSTRIAL, 1);
        conteo.put(TipoBloque.SERVICIOS, 1);
        conteo.put(TipoBloque.TRANSPORTE, 1);

        return new ResultadoSimulacion(
                nombre,
                2,
                5,
                10,
                10,
                7,
                3,
                conteo,
                EstadoSimulacion.EJECUTADA
        );
    }
    @Test
    public void testGenerarResumen_FlujoCompleto_CaminoBasico() {
        PresentadorCiudad presentador = new PresentadorCiudad();
        Ciudad ciudad = new Ciudad("NeoTokyo", 10, 10);

        ResultadoEvaluacion evaluacionFake = new ResultadoEvaluacion() {
            @Override public String getNivelEvaluacion() { return "Optimo"; }
            @Override public double getScoreViabilidad() { return 95.5; }
        };

        PredictionResult prediccionFake = new PredictionResult() {
            @Override public String getTendenciaPredicha() { return "Mejora Constante"; }
        };

        SalidaTexto resultado = presentador.generarResumen(ciudad, evaluacionFake, prediccionFake);

        assertNotNull(resultado);
        assertEquals("NeoTokyo", resultado.getNombreCiudad());
        assertEquals("Resumen Breve", resultado.getTitulo());
        assertTrue(resultado.getContenido().contains("NeoTokyo"));
        assertTrue(resultado.getContenido().contains("Optimo"));
        assertTrue(resultado.getContenido().contains("95.5"));
        assertTrue(resultado.getContenido().contains("Mejora Constante"));
    }

    private Map<TipoBloque, Integer> crearConteoBase() {
        Map<TipoBloque, Integer> conteo = new EnumMap<>(TipoBloque.class);
        for (TipoBloque tipo : TipoBloque.values()) {
            conteo.put(tipo, 0);
        }
        return conteo;
    }
}*/
