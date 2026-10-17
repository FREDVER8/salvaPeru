/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import clases.Estrella;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Panel de derrota del juego.
 * Muestra la animacion de la nave explotando antes de tocar el planeta, 
 * junto con una llama triste llorando por el impacto fallido y botones de control.
 * 
 * @author Dell
 */
public class PanLose extends JPanel implements ActionListener {

    private Timer reloj;
    private final int ANCHO = 900;
    private final int ALTO = 700;
    private ArrayList<Estrella> estrellas;

    private int naveX = 750;
    private int naveY = 550;
    private int naveVel = 3;

    private int planetaX = 60;
    private int planetaY = -60;
    private float planetaTamano = 180;
    private int planetaVel = 2;

    private boolean explotado = false;
    private int radioExplosion = 10;
    private int maxRadio = 90;
    private int explosionX;
    private int explosionY;
    private int tickExplosion = 0;

    private int llamaX = 670;
    private int llamaY = 320;
    private float tickLlama = 0;

    // Botones de navegacion
    private JButton btnReintentar;
    private JButton btnSalir;

    /**
     * Constructor del panel de derrota.
     * Configura el fondo, genera las estrellas, inicializa los botones y arranca el temporizador.
     */
    public PanLose() {
        // Configuramos el panel sin layout predefinido para posicionar los elementos libremente
        setLayout(null);
        setBackground(Color.BLACK);

        estrellas = new ArrayList<>();
        crearEstrellas();
        
        inicializarBotones();

        // Creamos y arrancamos el temporizador que controla la animacion general cada 20 milisegundos
        reloj = new Timer(20, this);
        reloj.start();
    }

    /**
     * Inicializa y configura los botones de "Volver a Jugar" y "Salir / Cerrar".
     */
    private void inicializarBotones() {
        // Configuracion visual y de posicion del boton para reintentar la partida
        btnReintentar = new JButton("Volver a Jugar");
        btnReintentar.setBounds(250, 600, 180, 40);
        btnReintentar.setBackground(new Color(40, 167, 69));
        btnReintentar.setForeground(Color.WHITE);
        btnReintentar.setFont(new Font("Arial", Font.BOLD, 14));
        btnReintentar.setFocusPainted(false);
        
        // Registra el escuchador de eventos para reiniciar el juego al hacer clic
        btnReintentar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Detiene el temporizador actual para evitar ejecuciones en segundo plano
                reloj.stop();
                
                // Busca la ventana principal que contiene este panel
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(PanLose.this);
                
                // Valida que la ventana exista antes de cambiar el panel principal por uno nuevo del juego
                if (topFrame != null) {
                    topFrame.setContentPane(new PanJuego());
                    topFrame.revalidate();
                    topFrame.repaint();
                }
            }
        });

        // Configuracion visual y de posicion del boton para salir de la aplicacion
        btnSalir = new JButton("Salir / Cerrar");
        btnSalir.setBounds(470, 600, 180, 40);
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setFocusPainted(false);
        
        // Registra el escuchador de eventos para cerrar completamente el programa al hacer clic
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Termina por completo la ejecucion de la aplicacion de Java cerrando todos los procesos
                System.exit(0);
            }
        });

        // Agrega los botones configurados al panel
        add(btnReintentar);
        add(btnSalir);
    }

    public void crearEstrellas() {
        // Rellena la lista con 100 objetos estrella distribuidos segun las dimensiones de la pantalla
        for (int i = 0; i < 100; i++) {
            estrellas.add(new Estrella(ANCHO, ALTO));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Recorre y pinta cada una de las estrellas en el fondo del panel
        for (Estrella e : estrellas) {
            e.pintar(g);
        }

        // Dibuja la estructura base del planeta antes de la derrota usando proporciones dinamicas
        int tamInt = (int) planetaTamano;
        g.setColor(new Color(0, 119, 182));
        g.fillOval(planetaX, planetaY, tamInt, tamInt);

        // Dibuja los continentes verdes adaptados proporcionalmente al tamaño actual de la esfera
        g.setColor(new Color(40, 167, 69));
        g.fillOval(planetaX + (int)(tamInt * 0.2), planetaY + (int)(tamInt * 0.25), (int)(tamInt * 0.4), (int)(tamInt * 0.35));
        g.fillOval(planetaX + (int)(tamInt * 0.5), planetaY + (int)(tamInt * 0.5), (int)(tamInt * 0.35), (int)(tamInt * 0.3));

        // Dibuja una sombra semitransparente para dar efecto tridimensional al planeta
        g.setColor(new Color(3, 4, 94, 150));
        g.fillOval(planetaX + (int)(tamInt * 0.3), planetaY + (int)(tamInt * 0.3), (int)(tamInt * 0.65), (int)(tamInt * 0.65));

        // Evalua si la nave sigue volando o si ya ocurrio la colision para mostrar la explosion y la llama
        if (!explotado) {
            g.setColor(Color.GREEN);
            int[] xNave = { naveX, naveX + 40, naveX + 15 };
            int[] yNave = { naveY, naveY + 15, naveY + 40 };
            g.fillPolygon(xNave, yNave, 3);
        } else {
            // Calcula variaciones usando funciones trigonometricas para que la explosion parpadee y se anime
            int variacion = (int) (Math.sin(tickExplosion * 0.4) * 6);
            int radioActual = radioExplosion + variacion;

            // Dibuja multiples capas de fuego superpuestas para simular el efecto de la detonacion
            dibujarFuego(g, explosionX, explosionY, radioActual);
            dibujarFuego(g, explosionX - 25, explosionY - 20, (int)(radioActual * 0.85));
            dibujarFuego(g, explosionX + 25, explosionY + 20, (int)(radioActual * 0.9));

            // Dibuja al personaje de la llama triste en pantalla tras el impacto fallido
            dibujarLlamitaLlorando(g);
        }

        // Configura y dibuja el texto principal de derrota en la parte superior del panel
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 70));
        g.drawString("¡PELDISTE!", ANCHO / 2 - 170, 110);
    }

    private void dibujarFuego(Graphics g, int x, int y, int radio) {
        // Dibuja la capa exterior roja del fuego de la explosion
        g.setColor(Color.RED);
        g.fillOval(x - radio / 2, y - radio / 2, radio, radio);

        // Dibuja la capa intermedia de color naranja con proporciones reducidas
        g.setColor(Color.ORANGE);
        int r2 = (int) (radio * 0.7);
        g.fillOval(x - r2 / 2, y - r2 / 2, r2, r2);

        // Dibuja el nucleo amarillo del fuego
        g.setColor(Color.YELLOW);
        int r3 = (int) (radio * 0.4);
        g.fillOval(x - r3 / 2, y - r3 / 2, r3, r3);

        // Dibuja el centro blanco brillante de la explosion
        g.setColor(Color.WHITE);
        int r4 = (int) (radio * 0.2);
        g.fillOval(x - r4 / 2, y - r4 / 2, r4, r4);
    }

    private void dibujarLlamitaLlorando(Graphics g) {
        // Calcula un ligero movimiento flotante vertical usando la funcion seno
        int offsetY = (int) (Math.sin(tickLlama) * 10);
        int x = llamaX;
        int y = llamaY + offsetY;

        // Dibuja un brillo o aura semitransparente alrededor del personaje triste
        g.setColor(new Color(180, 230, 255, 110));
        g.fillOval(x - 10, y - 35, 75, 75);
        g.setColor(new Color(255, 255, 255, 190));
        g.drawOval(x - 10, y - 35, 75, 75);

        // Dibuja el cuerpo y la estructura principal de la lana de la llama
        Color lana = new Color(245, 242, 235);
        g.setColor(lana);
        g.fillOval(x + 10, y + 35, 95, 60);
        g.fillRect(x + 20, y, 26, 45);
        g.fillOval(x + 5, y - 20, 42, 32);
        g.fillOval(x - 2, y - 12, 22, 20);

        // Dibuja las orejas del personaje utilizando poligonos triangulares
        int[] xOreja1 = { x + 18, x + 10, x + 24 };
        int[] yOreja1 = { y - 18, y - 32, y - 28 };
        g.fillPolygon(xOreja1, yOreja1, 3);

        int[] xOreja2 = { x + 35, x + 42, x + 30 };
        int[] yOreja2 = { y - 18, y - 32, y - 28 };
        g.fillPolygon(xOreja2, yOreja2, 3);

        // Dibuja las patas de la llama y un pequeño detalle de la cola
        g.fillRect(x + 20, y + 85, 10, 35);
        g.fillRect(x + 38, y + 85, 10, 35);
        g.fillRect(x + 68, y + 85, 10, 35);
        g.fillRect(x + 85, y + 85, 10, 35);
        g.fillOval(x + 98, y + 40, 16, 22);

        // Dibuja el hocico y el ojo cerrado característico de tristeza
        g.setColor(new Color(220, 190, 170));
        g.fillOval(x - 2, y - 10, 15, 12);
        g.setColor(Color.BLACK);
        g.fillOval(x + 16, y - 12, 6, 6);

        // Dibuja una playera con franjas coloridas sobre el personaje
        g.setColor(Color.RED);
        g.fillRect(x + 40, y + 38, 40, 28);
        g.setColor(Color.YELLOW);
        g.fillRect(x + 40, y + 46, 40, 8);
        g.setColor(Color.CYAN);
        g.fillRect(x + 40, y + 56, 40, 4);

        // Simula la caída de lagrimas fluidas desde el rostro de la llama
        g.setColor(new Color(0, 180, 255));
        int gotaY1 = (int) ((tickExplosion * 3) % 28);
        g.fillOval(x + 14, y - 4 + gotaY1, 5, 8);

        int gotaY2 = (int) (((tickExplosion * 3) + 14) % 28);
        g.fillOval(x + 18, y - 4 + gotaY2, 5, 8);

        // Define las coordenadas para la caja de texto flotante del dialogo
        int txtX = x - 10;
        int txtY = y + 145;

        // Dibuja el fondo y el borde redondeado del bocadillo de texto
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRoundRect(txtX - 5, txtY - 22, 165, 30, 10, 10);
        g.setColor(Color.RED);
        g.drawRoundRect(txtX - 5, txtY - 22, 165, 30, 10, 10);

        // Dibuja el mensaje de derrota con un efecto de sombra para darle relieve
        g.setFont(new Font("Impact", Font.PLAIN, 22));
        g.setColor(Color.BLACK);
        g.drawString("NOOO CAUSA!!!", txtX + 2, txtY + 2);

        g.setColor(Color.YELLOW);
        g.drawString("NOOO CAUSA!!!", txtX, txtY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Mueve cada una de las estrellas del fondo en cada fotograma del temporizador
        for (Estrella est : estrellas) {
            est.mover(ALTO);
        }

        // Controla la aproximacion de la nave hacia el planeta mientras no haya ocurrido una explosion
        if (!explotado) {
            naveX -= naveVel;
            naveY -= naveVel;

            planetaX += planetaVel;
            planetaY += planetaVel;
            planetaTamano += 2.4f; // Incrementa el tamaño de la esfera de manera decimal usando sufijo f

            // Calcula el centro y los radios para la deteccion geometrica de colision
            float radioPlaneta = planetaTamano / 2f;
            float centroPlanetaX = planetaX + radioPlaneta;
            float centroPlanetaY = planetaY + radioPlaneta;

            float centroNaveX = naveX + 20;
            float centroNaveY = naveY + 20;

            // Mide la distancia euclidiana exacta entre el centro de la nave y el centro del planeta
            double distancia = Math.sqrt(Math.pow(centroPlanetaX - centroNaveX, 2) + Math.pow(centroPlanetaY - centroNaveY, 2));
            int distanciaSeguridad = 75; 

            // Si la distancia cruza el margen de seguridad e impacto, activa la bandera de explosion
            if (distancia <= (radioPlaneta + distanciaSeguridad)) {
                explotado = true;
                explosionX = (int) centroNaveX;
                explosionY = (int) centroNaveY;
            }
        } else {
            // Expande gradualmente el radio de la explosion hasta llegar al limite maximo permitido
            if (radioExplosion < maxRadio) {
                radioExplosion += 5;
            }

            // Incrementa los contadores de tiempo para animar las explosiones y los saltos/llanto de la llama
            tickExplosion++;
            tickLlama += 0.05f;
        }

        // Ordena a la interfaz redibujar todos los elementos actualizados en pantalla
        repaint();
    }
}