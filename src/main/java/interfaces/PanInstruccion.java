/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import clases.Estrella;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Panel de instrucciones del juego.
 * Muestra el guion narrativo y los controles mediante un scroll vertical animado,
 * acompañado por un dirigible en movimiento, una llama espacial flotando y el botón de retorno.
 * 
 * Lo más complicado de esta clase:
 * 1. El cálculo del rebase y reciclado del texto (`textoY < -620`) para reiniciar el ciclo del scroll.
 * 2. La renderización dinámica del cartel de bandera peruana dentro del bucle de líneas del guion.
 * 3. La destrucción limpia de la ventana contenedora mediante `SwingUtilities.getWindowAncestor`.
 * 
 * @author Dell
 */
public class PanInstruccion extends JPanel implements ActionListener {

    /** Temporizador que controla la velocidad de refresco de la animación. */
    private Timer reloj;

    /** Ancho fijo del panel de instrucciones. */
    private final int ANCHO = 900;

    /** Alto fijo del panel de instrucciones. */
    private final int ALTO = 700;

    /** Botón para regresar al menú principal. */
    private JButton btnVolver;

    /** Lista que almacena las estrellas flotantes del fondo. */
    private ArrayList<Estrella> estrellas;

    /** Posición X actual del dirigible. */
    private int blimpX = 920;

    /** Posición Y fija del dirigible. */
    private int blimpY = 100;

    /** Velocidad de desplazamiento horizontal del dirigible. */
    private int blimpVel = 2;

    /** Posición X fija de la llama espacial. */
    private int llamaX = 730; 

    /** Posición Y base de la llama espacial. */
    private int llamaY = 300;

    /** Contador de tiempo para el movimiento senoidal de levitación de la llama. */
    private float tickLlama = 0;

    /** Arreglo de líneas de texto que forman el guion de la misión y controles. */
    private String[] guion = {
        "--- MISION: ADVINCULA CITY ---",
        "",
        "En el año 2099, la metropolis de Advincula City",
        "se ha quedado completamente sin aire.",
        "",
        "Para sobrevivir, la corporacion inicio el programa",
        "de colonizacion espacial para invadir mundos distantes,",
        "deforestarlos y extraer su atmosfera.",
        "",
        "Tu objetivo es pilotar la nave de recoleccion,",
        "defenderte de las amenazas locales y asegurar el recurso.",
        "",
        "--- CONTROLES ---",
        "[ FLECHA IZQ / DER ] : Mover Nave",
        "[ BARRA ESPACIADORA ] : Disparar",
        "",
        "TU PUEDES CAUSA"
    };

    /** Coordenada Y inicial de la primera línea del guion. */
    private int textoY = 680;

    /** Velocidad de subida del scroll de texto. */
    private int velTexto = 1;

    /**
     * Constructor del panel de instrucciones.
     * Configura el layout nulo, inicializa componentes gráficos, define los
     * eventos del botón volver e inicia el temporizador.
     */
    public PanInstruccion() {
        // Desactiva el layout manager para posicionado absoluto del botón
        setLayout(null); 
        setBackground(Color.BLACK);

        // Genera el fondo de estrellas
        estrellas = new ArrayList<>();
        crearEstrellas();

        // Configuración estética y de posicionamiento del botón Volver
        btnVolver = new JButton("VOLVER AL MENU");
        btnVolver.setBounds(20, 20, 160, 35);
        btnVolver.setFont(new Font("Consolas", Font.BOLD, 13));
        btnVolver.setForeground(Color.CYAN);
        btnVolver.setBackground(new Color(15, 23, 42));
        btnVolver.setBorder(javax.swing.BorderFactory.createLineBorder(Color.CYAN, 1));
        btnVolver.setFocusPainted(false);

        // Listener para detener el hilo y cambiar de ventana
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Detiene el temporizador para liberar recursos
                reloj.stop(); 
                
                // Abre la ventana del menú principal
                FrMenu menu = new FrMenu();
                menu.setVisible(true);

                // Obtiene la ventana contenedora principal (el JFrame) que alberga este panel
                Window ventanaActual = SwingUtilities.getWindowAncestor(PanInstruccion.this);
                
                // Valida que la ventana exista físicamente antes de intentar cerrarla
                if (ventanaActual != null) {
                    // Libera los recursos del sistema y destruye la ventana actual por completo
                    ventanaActual.dispose();
                }
                }
            }
        );

        // Agrega el botón al panel
        add(btnVolver);

        // Inicia el ciclo de animación cada 25 ms
        reloj = new Timer(25, this);
        reloj.start();
    }

    /**
     * Genera 100 estrellas en posiciones aleatorias para el fondo.
     */
    public void crearEstrellas() {
        // Popula la colección de estrellas con el tamaño del panel
        for (int i = 0; i < 100; i++) {
            estrellas.add(new Estrella(ANCHO, ALTO));
        }
    }

    /**
     * Renderiza todos los elementos gráficos del panel de instrucciones.
     * 
     * @param g Objeto Graphics sobre el cual se realiza el dibujo.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. DIBUJAR ESTRELLAS DE FONDO (Pinta las partículas del espacio)
        for (Estrella e : estrellas) {
            e.pintar(g);
        }

        // 2. DIBUJAR LLAMA ESPACIAL FLOTANTE (Renderiza el personaje con casco)
        dibujarLlamaEspacial(g);

        // 3. DIBUJAR DIRIGIBLE "PERU IS YOURS" (Renderiza el zeppelin superior)
        dibujarDirigible(g);

        // 4. DIBUJAR TITULO FIJO SUPERIOR (Encabezado estático)
        g.setColor(new Color(122, 92, 250));
        g.setFont(new Font("Agency FB", Font.BOLD, 42));
        g.drawString("INSTRUCCIONES DE MISION", ANCHO / 2 - 180, 50);

        // 5. DIBUJAR GUION CON DESPLAZAMIENTO ANIMADO (Bucle de renderizado de líneas)
        g.setFont(new Font("Consolas", Font.BOLD, 18));

        int separacion = 32;
        for (int i = 0; i < guion.length; i++) {
            int currentY = textoY + (i * separacion);

            // Filtra únicamente las líneas visibles dentro del área de lectura
            if (currentY > 180 && currentY < ALTO - 20) {

                // Dibuja el cartel especial para el mensaje final
                if (guion[i].equals("TU PUEDES CAUSA")) {
                    dibujarCartelBandera(g, currentY);
                } else {
                    // Aplica cian para secciones o blanco para texto estándar
                    if (guion[i].startsWith("---") || guion[i].startsWith("[")) {
                        g.setColor(Color.CYAN);
                    } else {
                        g.setColor(Color.WHITE);
                    }

                    // Centra horizontalmente cada línea según su ancho en píxeles
                    int largoTexto = g.getFontMetrics().stringWidth(guion[i]);
                    g.drawString(guion[i], (ANCHO - largoTexto) / 2, currentY);
                }
            }
        }
    }

    /**
     * Dibuja el cartel con la bandera peruana y el texto motivacional.
     * 
     * @param g Objeto Graphics sobre el cual se realiza el dibujo.
     * @param yPos Coordenada Y donde se dibujará el cartel.
     */
    private void dibujarCartelBandera(Graphics g, int yPos) {
        int anchoCartel = 280;
        int altoCartel = 45;
        int xPos = (ANCHO - anchoCartel) / 2;
        int yCartel = yPos - 30;

        int franjaAncho = anchoCartel / 3;

        // Franja 1: Roja (Izquierda)
        g.setColor(Color.RED);
        g.fillRect(xPos, yCartel, franjaAncho, altoCartel);

        // Franja 2: Blanca (Centro)
        g.setColor(Color.WHITE);
        g.fillRect(xPos + franjaAncho, yCartel, franjaAncho, altoCartel);

        // Franja 3: Roja (Derecha)
        g.setColor(Color.RED);
        g.fillRect(xPos + (franjaAncho * 2), yCartel, franjaAncho, altoCartel);

        // Borde dorado alrededor del cartel
        g.setColor(new Color(255, 215, 0));
        g.drawRect(xPos, yCartel, anchoCartel, altoCartel);

        // Texto "TU PUEDES CAUSA"
        g.setFont(new Font("Arial", Font.BOLD, 22));
        
        // Sombra proyectada del texto
        g.setColor(Color.BLACK);
        g.drawString("TU PUEDES CAUSA", xPos + 42, yCartel + 30);
        
        // Texto principal frontal
        g.setColor(Color.BLACK);
        g.drawString("TU PUEDES CAUSA", xPos + 40, yCartel + 28);
    }

    /**
     * Dibuja el dirigible volador con la leyenda promocional.
     * 
     * @param g Objeto Graphics sobre el cual se realiza el dibujo.
     */
    private void dibujarDirigible(Graphics g) {
        int x = blimpX;
        int y = blimpY;

        // Cuerpo principal elíptico
        g.setColor(new Color(15, 23, 42));
        g.fillOval(x, y, 270, 85);

        // Contorno exterior
        g.setColor(new Color(51, 65, 85));
        g.drawOval(x, y, 270, 85);

        // Aleta superior
        g.setColor(new Color(30, 41, 59));
        int[] xAletaArriba = { x + 235, x + 270, x + 250 };
        int[] yAletaArriba = { y + 10, y - 12, y + 25 };
        g.fillPolygon(xAletaArriba, yAletaArriba, 3);

        // Aleta inferior
        int[] xAletaAbajo = { x + 235, x + 270, x + 250 };
        int[] yAletaAbajo = { y + 75, y + 97, y + 60 };
        g.fillPolygon(xAletaAbajo, yAletaAbajo, 3);

        // Góndola inferior de pasajeros
        g.setColor(new Color(71, 85, 105));
        g.fillRect(x + 110, y + 80, 50, 12);

        // Texto luminoso sobre el dirigible
        g.setColor(new Color(255, 220, 80));
        g.setFont(new Font("Monospaced", Font.BOLD, 17));
        g.drawString("PERU IS YOURS...", x + 40, y + 49);
    }

    /**
     * Dibuja a la llama espacial flotando en el costado del panel.
     * 
     * @param g Objeto Graphics sobre el cual se realiza el dibujo.
     */
    private void dibujarLlamaEspacial(Graphics g) {
        // Calcula la variación senoidal de altura para flotar suavemente
        int offsetY = (int) (Math.sin(tickLlama) * 18);
        int x = llamaX;
        int y = llamaY + offsetY;

        // 1. Casco Espacial de Cristal con transparencia
        g.setColor(new Color(180, 230, 255, 110));
        g.fillOval(x - 10, y - 35, 75, 75);
        g.setColor(new Color(255, 255, 255, 190));
        g.drawOval(x - 10, y - 35, 75, 75);

        // 2. Cuerpo de la Llama
        Color lana = new Color(245, 242, 235);
        g.setColor(lana);
        g.fillOval(x + 10, y + 35, 95, 60);

        // Cuello alargado
        g.fillRect(x + 20, y, 26, 45);

        // Cabeza y hocico
        g.fillOval(x + 5, y - 20, 42, 32);
        g.fillOval(x - 2, y - 12, 22, 20);

        // Orejas alzadas
        int[] xOreja1 = { x + 22, x + 28, x + 16 };
        int[] yOreja1 = { y - 18, y - 42, y - 38 };
        g.fillPolygon(xOreja1, yOreja1, 3);

        int[] xOreja2 = { x + 35, x + 41, x + 29 };
        int[] yOreja2 = { y - 18, y - 42, y - 38 };
        g.fillPolygon(xOreja2, yOreja2, 3);

        // Patas
        g.fillRect(x + 20, y + 85, 10, 35);
        g.fillRect(x + 38, y + 85, 10, 35);
        g.fillRect(x + 68, y + 85, 10, 35);
        g.fillRect(x + 85, y + 85, 10, 35);

        // Cola
        g.fillOval(x + 98, y + 40, 16, 22);

        // 3. Cara y ojo
        g.setColor(new Color(220, 190, 170));
        g.fillOval(x - 2, y - 10, 15, 12);

        g.setColor(Color.BLACK);
        g.fillOval(x + 16, y - 12, 6, 6);

        // 4. Poncho peruano tradicional
        g.setColor(Color.RED);
        g.fillRect(x + 40, y + 38, 40, 28);
        g.setColor(Color.YELLOW);
        g.fillRect(x + 40, y + 46, 40, 8);
        g.setColor(Color.CYAN);
        g.fillRect(x + 40, y + 56, 40, 4);
    }

    /**
     * Evento periódico del temporizador.
     * Actualiza el movimiento de las estrellas, el avance del dirigible, 
     * el tiempo de flotación de la llama y la posición Y del scroll de texto.
     * 
     * @param e Evento de acción generado por el Timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Desplaza verticalmente las estrellas de fondo
        for (Estrella est : estrellas) {
            est.mover(ALTO);
        }

        // Mueve el dirigible hacia la izquierda y lo reubica al salir de pantalla
        blimpX -= blimpVel;
        if (blimpX < -320) {
            blimpX = ANCHO + 50;
        }

        // Avanza el tiempo de oscilación de la llama espacial
        tickLlama += 0.05f;

        // Desplaza el guion hacia arriba y lo reinicia al salir del límite
        textoY -= velTexto;
        if (textoY < -620) {
            textoY = 680;
        }

        // Solicita el redibujado de la escena
        repaint();
    }
}