import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class PastelEnHuida extends JPanel {

    // =========================================================
    // CONFIGURACIÓN DE LA VENTANA
    // =========================================================

    private final int ANCHO = 900;
    private final int ALTO = 700;

    // =========================================================
    // POSICIÓN DEL PASTEL
    // =========================================================

    private int pastelX = 650;
    private int pastelY = 180;

    // Velocidad horizontal del pastel
    private int velocidadPastel = 0;

    // =========================================================
    // POSICIÓN DEL TEXTO
    // =========================================================

    // El texto empieza debajo de la pantalla
    private double textoY = ALTO + 100;

    // Velocidad con la que sube el texto
    private double velocidadTexto = 0.8;

    // =========================================================
    // ESTADOS DE LA ANIMACIÓN
    // =========================================================

    private boolean mostrandoHistoria = true;
    private boolean mostrandoInstrucciones = false;
    private boolean animacionTerminada = false;

    // =========================================================
    // TEXTO DE LA HISTORIA
    // =========================================================

    private final String[] historia = {

        "HACE MUCHO TIEMPO,",
        "EN UNA GALAXIA MUY LEJANA...",

        "",

        "EXISTÍA UN DELICIOSO",
        "PEDAZO DE PASTEL,",
        "CON UNA CEREZA ENCIMA,",
        "MUY FELIZ EN SU PLATO.",

        "",

        "PERO UN DÍA, DECIDIÓ QUE",
        "QUERÍA VER EL UNIVERSO",
        "Y TENER SU PROPIA AVENTURA.",

        "",

        "ASÍ QUE ESCAPÓ...",
        "DE UN HAMBRIENTO",
        "PERSEGUIDOR DE PASTEL",

        "",

        "QUE HARÍA LO QUE FUERA",
        "POR ATRAPARLO Y COMÉRSELO.",

        "",

        "Y ASÍ COMIENZA LA ÉPICA HISTORIA",
        "DEL PASTEL EN HUIDA..."
    };

    // =========================================================
    // TEXTO DE LAS INSTRUCCIONES
    // =========================================================

    private final String[] instrucciones = {

        "INSTRUCCIONES:",

        "",

        "USA LAS FLECHAS IZQUIERDA Y DERECHA",
        "PARA MOVER EL PASTEL.",

        "",

        "EVITA AL PERSEGUIDOR",

        "",

        "¡BUENA SUERTE!"
    };

    // =========================================================
    // FUENTES
    // =========================================================

    private Font fuentePixel;

    // =========================================================
    // ESTRELLAS
    // =========================================================

    private final int NUM_ESTRELLAS = 180;

    private int[] estrellasX = new int[NUM_ESTRELLAS];
    private int[] estrellasY = new int[NUM_ESTRELLAS];
    private int[] estrellasTamano = new int[NUM_ESTRELLAS];

    private Random random = new Random();

    // =========================================================
    // TIMER
    // =========================================================

    private Timer timer;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PastelEnHuida() {

        setPreferredSize(new Dimension(ANCHO, ALTO));

        setBackground(Color.BLACK);

        // -----------------------------------------------------
        // FUENTE
        // -----------------------------------------------------

        // Intentamos usar una fuente tipo pixel.
        // "Monospaced" existe prácticamente en todos los sistemas.
        fuentePixel = new Font(
                "Monospaced",
                Font.BOLD,
                22
        );

        // -----------------------------------------------------
        // CREAR ESTRELLAS
        // -----------------------------------------------------

        crearEstrellas();

        // -----------------------------------------------------
        // CONFIGURAR TECLADO
        // -----------------------------------------------------

        configurarTeclado();

        // -----------------------------------------------------
        // TIMER
        // -----------------------------------------------------

        timer = new Timer(16, e -> actualizarAnimacion());

        timer.start();
    }


    // =========================================================
    // CREAR ESTRELLAS
    // =========================================================

    private void crearEstrellas() {

        for (int i = 0; i < NUM_ESTRELLAS; i++) {

            estrellasX[i] = random.nextInt(ANCHO);
            estrellasY[i] = random.nextInt(ALTO);

            estrellasTamano[i] = random.nextInt(3) + 1;
        }
    }


    // =========================================================
    // CONFIGURAR TECLADO
    // =========================================================

    private void configurarTeclado() {

        setFocusable(true);

        // -----------------------------------------------------
        // FLECHA IZQUIERDA
        // -----------------------------------------------------

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("LEFT"),
                "moverIzquierda"
        );

        getActionMap().put(
                "moverIzquierda",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        velocidadPastel = -5;
                    }
                }
        );

        // -----------------------------------------------------
        // FLECHA DERECHA
        // -----------------------------------------------------

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("RIGHT"),
                "moverDerecha"
        );

        getActionMap().put(
                "moverDerecha",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        velocidadPastel = 5;
                    }
                }
        );

        // -----------------------------------------------------
        // SOLTAR FLECHAS
        // -----------------------------------------------------

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("released LEFT"),
                "detener"
        );

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("released RIGHT"),
                "detener"
        );

        getActionMap().put(
                "detener",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        velocidadPastel = 0;
                    }
                }
        );
    }


    // =========================================================
    // ACTUALIZAR ANIMACIÓN
    // =========================================================

    private void actualizarAnimacion() {

        // -----------------------------------------------------
        // MOVER PASTEL
        // -----------------------------------------------------

        pastelX += velocidadPastel;

        // Limitar el pastel a la pantalla

        if (pastelX < 50) {
            pastelX = 50;
        }

        if (pastelX > ANCHO - 150) {
            pastelX = ANCHO - 150;
        }


        // -----------------------------------------------------
        // MOVER TEXTO
        // -----------------------------------------------------

        if (mostrandoHistoria) {

            textoY -= velocidadTexto;

            // Altura aproximada de toda la historia

            int alturaHistoria = historia.length * 34;

            // Cuando toda la historia ya salió por arriba

            if (textoY < -alturaHistoria) {

                mostrandoHistoria = false;

                mostrandoInstrucciones = true;

                // Volver a colocar el texto debajo
                // para comenzar las instrucciones

                textoY = ALTO + 100;
            }
        }

        else if (mostrandoInstrucciones) {

            textoY -= velocidadTexto;

            int alturaInstrucciones =
                    instrucciones.length * 34;

            if (textoY < -alturaInstrucciones) {

                mostrandoInstrucciones = false;

                animacionTerminada = true;
            }
        }


        // -----------------------------------------------------
        // REDIBUJAR
        // -----------------------------------------------------

        repaint();
    }


    // =========================================================
    // PAINT COMPONENT
    // =========================================================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        // Mejorar calidad de dibujo

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // -----------------------------------------------------
        // FONDO
        // -----------------------------------------------------

        dibujarFondo(g2);

        // -----------------------------------------------------
        // PERSONA
        // -----------------------------------------------------

        dibujarPersona(g2);

        // -----------------------------------------------------
        // PASTEL
        // -----------------------------------------------------

        dibujarPastel(g2);

        // -----------------------------------------------------
        // TEXTO
        // -----------------------------------------------------

        if (mostrandoHistoria) {

            dibujarHistoria(g2);

        }
        else if (mostrandoInstrucciones) {

            dibujarInstrucciones(g2);
        }

        g2.dispose();
    }


    // =========================================================
    // FONDO ESPACIAL
    // =========================================================

    private void dibujarFondo(Graphics2D g2) {

        // Fondo negro

        g2.setColor(Color.BLACK);

        g2.fillRect(
                0,
                0,
                ANCHO,
                ALTO
        );

        // Estrellas

        for (int i = 0; i < NUM_ESTRELLAS; i++) {

            int brillo = 150 + random.nextInt(106);

            g2.setColor(
                    new Color(
                            brillo,
                            brillo,
                            brillo
                    )
            );

            g2.fillRect(
                    estrellasX[i],
                    estrellasY[i],
                    estrellasTamano[i],
                    estrellasTamano[i]
            );
        }
    }


    // =========================================================
    // DIBUJAR PERSONA
    // =========================================================

    private void dibujarPersona(Graphics2D g2) {

        // Posición base del personaje

        int x = 250;
        int y = 150;


        // -----------------------------------------------------
        // PIERNAS / SHORT
        // -----------------------------------------------------

        g2.setColor(new Color(25, 70, 150));

        // Pierna izquierda

        g2.fillRect(
                x + 45,
                y + 190,
                35,
                60
        );

        // Pierna derecha

        g2.fillRect(
                x + 100,
                y + 190,
                35,
                60
        );


        // -----------------------------------------------------
        // ZAPATOS
        // -----------------------------------------------------

        g2.setColor(Color.WHITE);

        g2.fillRect(
                x + 35,
                y + 240,
                50,
                18
        );

        g2.fillRect(
                x + 95,
                y + 240,
                50,
                18
        );


        // -----------------------------------------------------
        // CUERPO / PLAYERA AZUL
        // -----------------------------------------------------

        g2.setColor(
                new Color(25, 90, 190)
        );

        g2.fillOval(
                x,
                y + 70,
                180,
                145
        );


        // -----------------------------------------------------
        // FRANJA AMARILLA
        // -----------------------------------------------------

        g2.setColor(
                new Color(255, 210, 30)
        );

        g2.fillRect(
                x + 5,
                y + 120,
                170,
                38
        );


        // -----------------------------------------------------
        // BRAZO IZQUIERDO
        // -----------------------------------------------------

        g2.setColor(
                new Color(190, 125, 80)
        );

        g2.fillOval(
                x - 30,
                y + 90,
                55,
                90
        );


        // -----------------------------------------------------
        // BRAZO DERECHO
        // -----------------------------------------------------

        g2.fillOval(
                x + 155,
                y + 70,
                55,
                90
        );


        // -----------------------------------------------------
        // CABEZA
        // -----------------------------------------------------

        g2.setColor(
                new Color(190, 125, 80)
        );

        g2.fillOval(
                x + 40,
                y - 10,
                100,
                100
        );


        // -----------------------------------------------------
        // CABELLO
        // -----------------------------------------------------

        g2.setColor(
                new Color(45, 25, 15)
        );

        g2.fillOval(
                x + 42,
                y - 18,
                96,
                42
        );


        // -----------------------------------------------------
        // AUDÍFONOS
        // -----------------------------------------------------

        g2.setColor(Color.DARK_GRAY);

        // Audífono izquierdo

        g2.fillOval(
                x + 25,
                y + 10,
                30,
                55
        );

        // Audífono derecho

        g2.fillOval(
                x + 125,
                y + 10,
                30,
                55
        );

        // Parte superior

        g2.setColor(Color.GRAY);

        g2.drawArc(
                x + 30,
                y - 5,
                120,
                70,
                0,
                180
        );


        // -----------------------------------------------------
        // OJOS
        // -----------------------------------------------------

        g2.setColor(Color.BLACK);

        g2.fillRect(
                x + 65,
                y + 30,
                10,
                10
        );

        g2.fillRect(
                x + 105,
                y + 30,
                10,
                10
        );


        // -----------------------------------------------------
        // BOCA
        // -----------------------------------------------------

        g2.fillRect(
                x + 78,
                y + 55,
                35,
                10
        );


        // -----------------------------------------------------
        // DETALLE DE VELOCIDAD
        // -----------------------------------------------------

        g2.setColor(
                new Color(100, 180, 255)
        );

        g2.drawLine(
                x - 60,
                y + 100,
                x - 10,
                y + 100
        );

        g2.drawLine(
                x - 80,
                y + 130,
                x - 10,
                y + 130
        );

        g2.drawLine(
                x - 50,
                y + 160,
                x,
                y + 160
        );
    }


    // =========================================================
    // DIBUJAR PASTEL
    // =========================================================

    private void dibujarPastel(Graphics2D g2) {

        int x = pastelX;
        int y = pastelY;


        // -----------------------------------------------------
        // EFECTO DE VELOCIDAD
        // -----------------------------------------------------

        g2.setColor(
                new Color(80, 150, 255)
        );

        g2.fillRect(
                x - 80,
                y + 35,
                70,
                5
        );

        g2.fillRect(
                x - 60,
                y + 50,
                50,
                5
        );

        g2.fillRect(
                x - 100,
                y + 65,
                90,
                5
        );


        // -----------------------------------------------------
        // PARTE DEL PASTEL
        // -----------------------------------------------------

        // Base

        g2.setColor(
                new Color(220, 160, 90)
        );

        g2.fillRect(
                x,
                y + 30,
                120,
                70
        );


        // Parte superior

        g2.setColor(
                new Color(255, 245, 220)
        );

        g2.fillRect(
                x - 5,
                y + 15,
                130,
                30
        );


        // Relleno

        g2.setColor(
                new Color(230, 80, 120)
        );

        g2.fillRect(
                x,
                y + 55,
                120,
                15
        );


        // -----------------------------------------------------
        // CEREZA
        // -----------------------------------------------------

        g2.setColor(
                new Color(200, 30, 40)
        );

        g2.fillOval(
                x + 50,
                y - 5,
                25,
                25
        );


        // Tallo

        g2.setColor(
                new Color(50, 120, 50)
        );

        g2.drawLine(
                x + 63,
                y,
                x + 70,
                y - 15
        );


        // -----------------------------------------------------
        // BORDE
        // -----------------------------------------------------

        g2.setColor(Color.WHITE);

        g2.drawRect(
                x,
                y + 30,
                120,
                70
        );
    }


    // =========================================================
    // DIBUJAR HISTORIA
    // =========================================================

    private void dibujarHistoria(Graphics2D g2) {

        g2.setFont(fuentePixel);

        g2.setColor(Color.WHITE);

        int separacion = 34;

        for (int i = 0; i < historia.length; i++) {

            String linea = historia[i];

            if (linea.isEmpty()) {
                continue;
            }

            int anchoTexto =
                    g2.getFontMetrics().stringWidth(linea);

            int x =
                    (ANCHO - anchoTexto) / 2;

            int y =
                    (int) textoY + i * separacion;

            g2.drawString(
                    linea,
                    x,
                    y
            );
        }
    }


    // =========================================================
    // DIBUJAR INSTRUCCIONES
    // =========================================================

    private void dibujarInstrucciones(Graphics2D g2) {

        g2.setFont(fuentePixel);

        // VERDE

        g2.setColor(
                new Color(80, 255, 80)
        );

        int separacion = 34;

        for (int i = 0; i < instrucciones.length; i++) {

            String linea = instrucciones[i];

            if (linea.isEmpty()) {
                continue;
            }

            int anchoTexto =
                    g2.getFontMetrics().stringWidth(linea);

            int x =
                    (ANCHO - anchoTexto) / 2;

            int y =
                    (int) textoY + i * separacion;

            g2.drawString(
                    linea,
                    x,
                    y
            );
        }
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        JFrame ventana = new JFrame(
                "Pastel en Huida"
        );

        PastelEnHuida panel =
                new PastelEnHuida();

        ventana.add(panel);

        ventana.setSize(
                900,
                700
        );

        ventana.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        ventana.setLocationRelativeTo(null);

        ventana.setResizable(false);

        ventana.setVisible(true);

        panel.requestFocusInWindow();
    }
}