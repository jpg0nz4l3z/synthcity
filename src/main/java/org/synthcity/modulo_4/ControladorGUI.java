/*package SPRINT2;

public class ControladorGUI {

    // Atributos de Estado Actual
    private Ciudad ciudadActual;
    private ResultadoEvaluacion evaluacionActual;
    private PredictionResult prediccionActual;

    // Referencias
    private PanelCiudad panelCiudad;               // Persona 1
    private PanelResumenSistema panelResumen;      // Persona 2
    private ResultadoRepository resultadoRepository; // Persona 3

    // Constructor
    public ControladorGUI(PanelCiudad panelCiudad, PanelResumenSistema panelResumen, ResultadoRepository resultadoRepository) {
        this.panelCiudad = panelCiudad;
        this.panelResumen = panelResumen;
        this.resultadoRepository = resultadoRepository;
    }


    // Recibe los datos y los distribuye
    public void mostrarSistema(Ciudad ciudad, ResultadoEvaluacion evaluacion, PredictionResult prediccion) {
        this.ciudadActual = ciudad;
        this.evaluacionActual = evaluacion;
        this.prediccionActual = prediccion;

        //Persona 1
        if (this.panelCiudad != null && this.ciudadActual != null) {
            this.panelCiudad.mostrarCiudad(this.ciudadActual);
        }
        //Persona 2
        if (this.panelResumen != null && this.ciudadActual != null && this.evaluacionActual != null) {
            this.panelResumen.mostrarSistema(this.ciudadActual, this.evaluacionActual, this.prediccionActual);
        }
    }

    // Vuelve a pintar la interfaz con los datos ya guardados
    public void refrescarVista() {
        if (this.ciudadActual != null && this.evaluacionActual != null) {
            mostrarSistema(this.ciudadActual, this.evaluacionActual, this.prediccionActual);
    }}

    // Deja el sistema en blanco
    public void limpiarVista() {
        this.ciudadActual = null;
        this.evaluacionActual = null;
        this.prediccionActual = null;

        if (this.panelCiudad != null){
            this.panelCiudad.limpiar();
        }
        if (this.panelResumen != null){
            this.panelResumen.limpiar();
        }
    }

    // Conecta el botón de la interfaz con la base de datos
    public void guardarResultadoActual() {
        if (this.resultadoRepository != null && this.evaluacionActual != null && this.prediccionActual != null) {
            this.resultadoRepository.guardarResultado(this.ciudadActual, this.evaluacionActual, this.prediccionActual);
        }

    }
}
*/