// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

/**
 * Actúa como intermediario entre el Modelo (Juego) y la Vista (VistaJuego)[cite: 206].
 * (Esta clase DEBE estar en su propio archivo "ControladorJuego.java")
 */
public class ControladorJuego /* implements ActionListener */ {
    
    private Juego modelo;
    private VistaJuego vista;
    
    public ControladorJuego(Juego modelo, VistaJuego vista) {
        this.modelo = modelo;
        this.vista = vista;
        
        // Conectar los botones de la vista a este controlador
        // this.vista.agregarListeners(this);
        
        // TODO: Iniciar el juego
        // modelo.iniciarRondaGUI(); // (Necesitarás un nuevo método en Juego)
        // actualizarVistaCompleta();
    }
    
    // @Override
    // public void actionPerformed(ActionEvent e) {
    //     String comando = e.getActionCommand(); // (Ej. "Pedir", "Plantarse")
        
    //     // TODO: Traducir clics en llamadas al modelo
    //     // switch (comando) {
    //     //     case "Pedir":
    //     //         modelo.jugadorPedir();
    //     //         break;
    //     //     case "Plantarse":
    //     //         modelo.jugadorPlantarse();
    //     //         break;
    //     // }
        
    //     // TODO: Actualizar la vista después de cada acción
    //     // actualizarVistaCompleta();
        
    //     // TODO: Comprobar si la ronda terminó y ejecutar turno del crupier
    // }
    
    // private void actualizarVistaCompleta() {
    //     // TODO: Obtener el estado del modelo (manos, saldo, etc.)
    //     // EstadoJuego estadoActual = modelo.getEstado();
    //     // vista.actualizarVista(estadoActual);
    // }
    
    /**
     * Punto de entrada para la aplicación GUI (Sprint 5).
     */
    public static void main(String[] args) {
        // Juego modelo = new Juego(); // (Asegúrate de quitarle la lógica de consola)
        // VistaJuego vista = new VistaJuego();
        // ControladorJuego controlador = new ControladorJuego(modelo, vista);
        // vista.setVisible(true);
    }
}