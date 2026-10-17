/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import javax.swing.JFrame;

/**
 * Ventana principal contenedora del juego espacial.
 * Se encarga de gestionar la dimensión del marco y alternar dinámicamente los
 * paneles de juego (`PanJuego`), victoria (`PanWin`) y derrota (`PanLose`).
 * 
 * Lo más complicado de esta clase:
 * 1. El reemplazo y refresco de paneles en tiempo de ejecución mediante 
 *    `removeAll()`, `revalidate()` y `repaint()` para asegurar la fluidez visual sin fugas.
 * 2. La integración del identificador de jugador para persistencia o trazabilidad durante la partida.
 * 
 * @author Dell
 */
public class FrJuego extends JFrame {

    /** Identificador único del jugador actual en la sesión de juego. */
    private int idJugador;

    /**
     * Constructor por defecto de la ventana de juego.
     * Configura el título, dimensiones, comportamiento de cierre y posicionamiento
     * centralizado, e inicializa el panel base `PanJuego`.
     */
    public FrJuego() {
        // Establece el título de la ventana
        setTitle("Space");
        
        // Define las dimensiones fijas del marco en píxeles (ancho, alto)
        setSize(900, 700);
        
        // Centra la ventana en la pantalla
        setLocationRelativeTo(null);
        
        // Finaliza el proceso al cerrar la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Inhabilita el redimensionado para mantener la proporción de los elementos renderizados
        setResizable(false);
        
        // Carga e inserta el panel de juego principal al iniciar la interfaz
        this.add(new PanJuego());
    }

    /**
     * Remueve el contenido actual del contenedor y despliega la pantalla de victoria (`PanWin`).
     * Revalida y redibuja la interfaz para aplicar los cambios en tiempo de ejecución.
     */
    public void mostrarWin() {
        // Limpia los paneles cargados previamente en el ContentPane
        this.getContentPane().removeAll();
        
        // Agrega el panel de estado ganado
        this.add(new PanWin());
        
        // Recalcula la jerarquía de componentes gráficos
        this.revalidate();
        
        // Solicita el redibujado inmediato del marco
        this.repaint();
    }

    /**
     * Remueve el contenido actual del contenedor y despliega la pantalla de derrota (`PanLose`).
     * Revalida y redibuja la interfaz para aplicar los cambios en tiempo de ejecución.
     */
    public void mostrarLose() {
        // Limpia los paneles cargados previamente en el ContentPane
        this.getContentPane().removeAll();
        
        // Agrega el panel de estado perdido
        this.add(new PanLose());
        
        // Recalcula la jerarquía de componentes gráficos
        this.revalidate();
        
        // Solicita el redibujado inmediato del marco
        this.repaint();
    }
}