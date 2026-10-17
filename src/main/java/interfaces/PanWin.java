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
 * Panel de victoria del juego.
 * Muestra la animación de una nave espacial viajando hacia un planeta, 
 * y al llegar, despliega una bandera, una llamita festiva celebrando y confeti.
 * 
 * Lo más complicado de esta clase:
 * 1. El cálculo geométrico de colisión por distancia euclidiana entre el centro 
 *    de la nave y el radio variable del planeta en crecimiento (`distancia <= radioPlaneta`).
 * 2. El dibujado mediante primitivas de {@link Graphics} (polígonos, arcos, degradados y formas complejas) 
 *    para construir la ilustración vectorial de la llama y la bandera sin usar imágenes externas.
 * 3. La sincronización de múltiples animaciones matemáticas independientes 
 *    (ondas senoidales para el flameo de la bandera, los saltos de la llama y la caída del confeti).
 * 
 * @author Dell
 */
public class PanWin extends JPanel implements ActionListener {

    /** Temporizador que controla la velocidad de actualización de la animación. */
    private Timer reloj;

    /** Ancho fijo de la pantalla del panel. */
    private final int ANCHO = 900;

    /** Alto fijo de la pantalla del panel. */
    private final int ALTO = 700;

    /** Lista que almacena las estrellas flotantes del fondo. */
    private ArrayList<Estrella> estrellas;

    /** Posición X actual de la nave espacial. */
    private int naveX = 750;

    /** Posición Y actual de la nave espacial. */
    private int naveY = 550;

    /** Velocidad con la que se desplaza la nave. */
    private int naveVel = 3;

    /** Posición X actual del planeta. */
    private int planetaX = 60;

    /** Posición Y actual del planeta. */
    private int planetaY = -60;

    /** Tamaño o diámetro actual del planeta. */
    private float planetaTamano = 180;

    /** Velocidad con la que el planeta se acerca a la pantalla. */
    private int planetaVel = 2;

    /** Indica si la nave ya hizo contacto/colisión con el planeta. */
    private boolean enContacto = false;

    /** Posición X donde se plantará la bandera al colisionar. */
    private int banderaX;

    /** Posición Y donde se plantará la bandera al colisionar. */
    private int banderaY;

    /** Contador de tiempo para animar el movimiento de onda en la bandera. */
    private int tickOnda = 0;

    /** Posición X fija de la llama celebrando. */
    private int llamaX = 670;

    /** Posición Y base de la llama celebrando. */
    private int llamaY = 320;

    /** Contador de tiempo para calcular los saltos de la llama. */
    private float tickSalto = 0;

    /** Lista de partículas de confeti para la celebración. */
    private ArrayList<Confeti> listaConfeti;

    // Botones de navegación
    private JButton btnReintentar;
    private JButton btnSalir;

    /**
     * Constructor del panel de victoria.
     * Configura el fondo negro, inicializa las listas de elementos gráficos,
     * los botones de control e inicia el temporizador de la animación.
     */
    public PanWin() {
        setLayout(null);
        setBackground(Color.BLACK);

        // Inicialización de colecciones para elementos dinámicos
        estrellas = new ArrayList<>();
        crearEstrellas();

        listaConfeti = new ArrayList<>();
        crearConfeti();

        inicializarBotones();

        // Inicia el bucle de animación a 50 fotogramas por segundo (cada 20 ms)
        reloj = new Timer(20, this);
        reloj.start();
    }

    /**
     * Inicializa y configura los botones de "Volver a Jugar" y "Salir / Cerrar".
     */
    private void inicializarBotones() {
        // Botón para volver a jugar (reinicia PanJuego)
        btnReintentar = new JButton("Volver a Jugar");
        btnReintentar.setBounds(250, 600, 180, 40);
        btnReintentar.setBackground(new Color(40, 167, 69));
        btnReintentar.setForeground(Color.WHITE);
        btnReintentar.setFont(new Font("Arial", Font.BOLD, 14));
        btnReintentar.setFocusPainted(false);
        
        btnReintentar.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Detiene el temporizador actual para evitar que siga ejecutando animaciones en segundo plano
        reloj.stop();
        
        // Busca hacia arriba en la jerarquía de componentes la ventana principal (JFrame) que contiene este panel
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(PanWin.this);
        
        // Valida que la ventana principal exista antes de intentar modificarla
        if (topFrame != null) {
            // Reemplaza el panel actual de victoria/derrota por una nueva instancia del panel del juego principal
            topFrame.setContentPane(new PanJuego());
            
            // Le avisa al contenedor que la estructura de componentes cambió para que recalcule el diseño
            topFrame.revalidate();
            
            // Fuerza a la ventana a redibujar sus gráficos inmediatamente para mostrar el nuevo panel sin retrasos
            topFrame.repaint();
        }
    }
});

        // Botón para salir por completo de la aplicación
        btnSalir = new JButton("Salir / Cerrar");
        btnSalir.setBounds(470, 600, 180, 40);
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setFocusPainted(false);
        
        btnSalir.addActionListener(new ActionListener() {
            @Override
            //  Termina por completo la ejecución de la aplicación y cierra todos los procesos y ventanas abiertas
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Agregamos los botones al panel
        add(btnReintentar);
        add(btnSalir);
    }

    /**
     * Genera 100 estrellas aleatorias para llenar el fondo del espacio.
     */
    public void crearEstrellas() {
        for (int i = 0; i < 100; i++) {
            estrellas.add(new Estrella(ANCHO, ALTO));
        }
    }

    /**
     * Genera 60 piezas de confeti con posiciones, velocidades y colores aleatorios.
     */
    /**
 * Genera una lluvia inicial de 60 partículas de confeti con posiciones, 
 * velocidades de caída y colores completamente aleatorios.
 */
public void crearConfeti() {
    // Definimos un arreglo con los colores disponibles para darle variedad visual al festejo
    Color[] colores = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.ORANGE};
    
    // Ciclo que se repite 60 veces para crear exactamente 60 piezas de confeti independientes
    for (int i = 0; i < 60; i++) {
        // Calcula una posición horizontal (X) aleatoria a lo largo de todo el ancho del panel (0 hasta ANCHO)
        int x = (int) (Math.random() * ANCHO);
        
        // Calcula una posición vertical (Y) aleatoria a lo largo de toda la altura del panel (0 hasta ALTO)
        int y = (int) (Math.random() * ALTO);
        
        // Asigna una velocidad de caída aleatoria entre 2 y 5 píxeles por fotograma 
        // (Math.random() * 4 da de 0 a 3.99, al sumarle 2 queda en un rango de 2 a 5)
        int vel = 2 + (int) (Math.random() * 4);
        
        // Selecciona de forma aleatoria un color del arreglo creado arriba usando el tamaño del arreglo como límite
        Color c = colores[(int) (Math.random() * colores.length)];
        
        // Instancia un nuevo objeto 'Confeti' con los valores aleatorios generados y lo añade a la lista principal
        listaConfeti.add(new Confeti(x, y, vel, c));
    }
}

    /**
     * Dibuja los gráficos del panel en cada fotograma.
     * 
     * @param g Objeto Graphics que permite dibujar los componentes visuales.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // DIBUJAR ESTRELLAS (Renderiza el fondo espacial)
        for (Estrella e : estrellas) {
            e.pintar(g);
        }

        // Renderiza las partículas de confeti solo si ya ocurrió el impacto
        if (enContacto) {
            // Es la forma más limpia y eficiente en Java para recorrer cada uno de los 60
            for (Confeti c : listaConfeti) {
                c.dibujar(g);
            }
        }

        // 2. DIBUJAR PLANETA (Cambia de verde/azul a café al tocarlo)
        int tamInt = (int) planetaTamano;

        if (!enContacto) {
            // BLOQUE 1: Dibuja el planeta antes del impacto (con aspecto vivo, tonos azules y continentes verdes)
            g.setColor(new Color(0, 119, 182)); // Color azul brillante para los océanos del planeta
            g.fillOval(planetaX, planetaY, tamInt, tamInt); // Dibuja la esfera base del planeta usando el tamaño actual (que va creciendo)

            g.setColor(new Color(40, 167, 69)); // Color verde para representar la tierra/continentes
            g.fillOval(planetaX + (int)(tamInt * 0.2), planetaY + (int)(tamInt * 0.25), (int)(tamInt * 0.4), (int)(tamInt * 0.35)); // Primer continente adaptado proporcionalmente al tamaño del planeta
            g.fillOval(planetaX + (int)(tamInt * 0.5), planetaY + (int)(tamInt * 0.5), (int)(tamInt * 0.35), (int)(tamInt * 0.3)); // Segundo continente proporcional

            g.setColor(new Color(3, 4, 94, 150)); // Azul oscuro semi-transparente para simular una sombra de profundidad
            g.fillOval(planetaX + (int)(tamInt * 0.3), planetaY + (int)(tamInt * 0.3), (int)(tamInt * 0.65), (int)(tamInt * 0.65)); // Sombra circular que da efecto tridimensional (esfera)

            } else { // BLOQUE 2: Se ejecuta al ocurrir el contacto (cambia el planeta a tonos tierra/secos por la llegada)
    
            g.setColor(new Color(120, 70, 30)); // Color café/marrón para la base del planeta alcanzado
            g.fillOval(planetaX, planetaY, tamInt, tamInt); // Dibuja la esfera base con el nuevo tono

            g.setColor(new Color(85, 45, 15)); // Color café más oscuro para los detalles continentales secos
            g.fillOval(planetaX + (int)(tamInt * 0.2), planetaY + (int)(tamInt * 0.25), (int)(tamInt * 0.4), (int)(tamInt * 0.35)); // Detalle de continente seco 1
            g.fillOval(planetaX + (int)(tamInt * 0.5), planetaY + (int)(tamInt * 0.5), (int)(tamInt * 0.35), (int)(tamInt * 0.3)); // Detalle de continente seco 2

            g.setColor(new Color(45, 20, 5, 170)); // Sombra oscura semi-transparente para el planeta transformado
            g.fillOval(planetaX + (int)(tamInt * 0.3), planetaY + (int)(tamInt * 0.3), (int)(tamInt * 0.65), (int)(tamInt * 0.65)); // Sombra 3D adaptada al nuevo estado
            }

        // 3. DIBUJAR NAVE O BANDERITA + LLAMITA FELIZ
        if (!enContacto) {
            g.setColor(Color.GREEN);
            int[] xNave = { naveX, naveX + 40, naveX + 15 };
            int[] yNave = { naveY, naveY + 15, naveY + 40 };
            g.fillPolygon(xNave, yNave, 3);
        } else {
            dibujarBanderita(g);
            dibujarLlamitaFeliz(g);
        }

        // 4. TEXTO DE VICTORIA SUPERIOR
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 55));
        g.drawString("¡VICTORIA!", ANCHO / 2 - 140, 110);
    }

/**
     * Dibuja la bandera animada en las coordenadas de impacto.
     * 
     * @param g Objeto Graphics sobre el cual se realiza el dibujo.
     */
    private void dibujarBanderita(Graphics g) {
        // Dibuja el asta o palo vertical blanco de la bandera
        g.setColor(Color.WHITE);
        g.drawLine(banderaX, banderaY, banderaX, banderaY - 45);

        // Variables de posicionamiento y medidas para las franjas de la bandera
        int xIni = banderaX;
        int yIni = banderaY - 45;
        int alto = 22;
        int anchoFranja = 11;
        
        // Calcula un efecto de onda usando funciones trigonométricas (seno) para que la bandera se mueva con el viento
        int onda = (int) (Math.sin(tickOnda * 0.2) * 5);

        // Dibuja la primera franja roja de la bandera
        g.setColor(Color.RED);
        g.fillRect(xIni, yIni, anchoFranja, alto);

        // Dibuja la segunda franja blanca de la bandera
        g.setColor(Color.WHITE);
        g.fillRect(xIni + anchoFranja, yIni, anchoFranja, alto);

        // Dibuja la tercera franja aplicando el movimiento de onda dinámico con un polígono
        g.setColor(Color.RED);
        // Define los 4 puntos (X, Y) para dibujar un polígono personalizado en lugar de un rectángulo normal
        int[] xFranja3 = { 
            xIni + (anchoFranja * 2),          // 1. Esquina superior izq. (fija al lado de la franja anterior)
            xIni + (anchoFranja * 3) + onda,   // 2. Esquina superior der. (se mueve con el viento gracias a 'onda')
            xIni + (anchoFranja * 3) + onda,   // 3. Esquina inferior der. (mantiene el movimiento de onda abajo)
            xIni + (anchoFranja * 2)           // 4. Esquina inferior izq. (cierra la figura en la base)
        };
        int[] yFranja3 = { 
            yIni, yIni + 2, yIni + alto + 2, yIni + alto 
        };
        
        // ¿Por qué se hizo así? Para sumar la variable 'onda' solo al lado derecho, 
        // logrando el efecto visual de que la bandera se está ondeando con el viento.
        g.fillPolygon(xFranja3, yFranja3, 4);

        // Dibuja un contorno translúcido alrededor de la bandera para darle mejor definición visual
        g.setColor(new Color(0, 0, 0, 80));
        g.drawRect(xIni, yIni, anchoFranja * 3 + onda, alto);
    }

    /**
     * Dibuja al personaje de la llama dando saltos de celebración.
     * 
     * @param g Objeto Graphics sobre el cual se realiza el dibujo.
     */
    private void dibujarLlamitaFeliz(Graphics g) {
        // Calcula el salto vertical de la llama usando el valor absoluto del seno para un efecto de rebote constante
        int saltoY = (int) (Math.abs(Math.sin(tickSalto)) * 30);
        int x = llamaX;
        int y = llamaY - saltoY;

        // Dibuja un brillo o aura semitransparente detrás del personaje
        g.setColor(new Color(180, 230, 255, 110));
        g.fillOval(x - 10, y - 35, 75, 75);
        g.setColor(new Color(255, 255, 255, 190));
        g.drawOval(x - 10, y - 35, 75, 75);

        // Cuerpo y extremidades de la llama con el color característico de su lana
        Color lana = new Color(245, 242, 235);
        g.setColor(lana);
        g.fillOval(x + 10, y + 35, 95, 60);
        g.fillRect(x + 20, y, 26, 45);
        g.fillOval(x + 5, y - 20, 42, 32);
        g.fillOval(x - 2, y - 12, 22, 20);

        // Orejas de la llama usando polígonos triangulares
        int[] xOreja1 = { x + 18, x + 12, x + 24 };
        int[] yOreja1 = { y - 18, y - 35, y - 28 };
        g.fillPolygon(xOreja1, yOreja1, 3);

        int[] xOreja2 = { x + 35, x + 41, x + 29 };
        int[] yOreja2 = { y - 18, y - 35, y - 28 };
        g.fillPolygon(xOreja2, yOreja2, 3);

        // Patas de la llama
        g.fillRect(x + 20, y + 85, 10, 35);
        g.fillRect(x + 38, y + 85, 10, 35);
        g.fillRect(x + 68, y + 85, 10, 35);
        g.fillRect(x + 85, y + 85, 10, 35);

        // Detalle de la cola
        g.fillOval(x + 98, y + 35, 18, 18);

        // Detalle del hocico y sonrisa
        g.setColor(new Color(220, 190, 170));
        g.fillOval(x - 2, y - 10, 15, 12);
        g.setColor(Color.BLACK);
        g.drawArc(x + 14, y - 14, 8, 8, 0, 180);

        // Dibuja una pequeña playera decorativa en la llama con franjas de colores
        g.setColor(Color.RED);
        g.fillRect(x + 40, y + 38, 40, 28);
        g.setColor(Color.YELLOW);
        g.fillRect(x + 40, y + 46, 40, 8);
        g.setColor(Color.CYAN);
        g.fillRect(x + 40, y + 56, 40, 4);

        // Coordenadas para el cuadro de diálogo flotante del mensaje de victoria
        int txtX = llamaX - 15;
        int txtY = llamaY + 150;

        // Fondo oscuro semitransparente con borde verde para la caja de texto
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRoundRect(txtX - 5, txtY - 22, 195, 32, 10, 10);
        g.setColor(Color.GREEN);
        g.drawRoundRect(txtX - 5, txtY - 22, 195, 32, 10, 10);

        // Dibuja el texto de celebración con sombra para darle efecto de relieve
        g.setFont(new Font("Impact", Font.PLAIN, 22));
        g.setColor(Color.BLACK);
        g.drawString("LO LOGRASTE PE !!!", txtX + 2, txtY + 2);
        g.setColor(Color.GREEN);
        g.drawString("LO LOGRASTE PE !!!", txtX, txtY);
    }

    /**
     * Evento ejecutado periódicamente por el temporizador.
     * 
     * @param e Evento de acción generado por el Timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Mueve cada una de las estrellas de fondo en cada fotograma
        for (Estrella est : estrellas) {
            est.mover(ALTO);
        }

        // Si la nave aún no choca con el planeta, continúa el movimiento de aproximación
        if (!enContacto) {
            naveX -= naveVel;
            naveY -= naveVel;

            planetaX += planetaVel;
            planetaY += planetaVel;
            planetaTamano += 2.4f; // El planeta va creciendo conforme se acerca

            // Calcula el centro y los radios para la detección matemática de colisión
            float radioPlaneta = planetaTamano / 2f;
            float centroPlanetaX = planetaX + radioPlaneta;
            float centroPlanetaY = planetaY + radioPlaneta;

            float centroNaveX = naveX + 20;
            float centroNaveY = naveY + 20;

            // Distancia euclidiana entre la nave y el planeta
            double distancia = Math.sqrt(Math.pow(centroPlanetaX - centroNaveX, 2) + Math.pow(centroPlanetaY - centroNaveY, 2));

            // Si la distancia es menor o igual al radio, ocurre el impacto (victoria)
            if (distancia <= radioPlaneta) {
                enContacto = true;
                banderaX = (int) centroNaveX;
                banderaY = (int) centroNaveY;
            }
        } else {
            // Si ya hubo contacto, actualiza las animaciones del festejo (onda y saltos de la llama)
            tickOnda++;
            tickSalto += 0.12f;

            // Mueve cada partícula de confeti hacia abajo en cada fotograma
            for (Confeti c : listaConfeti) {
                c.mover(ALTO);
            }
        }

        // Fuerza a la pantalla a redibujar todos los elementos actualizados
        repaint();
    }

    /**
     * Clase interna que representa una partícula individual de confeti.
     */
    private class Confeti {
        int x;
        int y;
        int vel;
        Color color;

        public Confeti(int x, int y, int vel, Color color) {
            this.x = x;
            this.y = y;
            this.vel = vel;
            this.color = color;
        }

        // Mueve el confeti hacia abajo y lo reinicia arriba si sale de la pantalla
        public void mover(int altoMax) {
            y += vel;
            if (y > altoMax) {
                y = -10;
                x = (int) (Math.random() * ANCHO);
            }
        }

        // Dibuja la partícula de confeti en su posición actual con su respectivo color
        public void dibujar(Graphics g) {
            g.setColor(color);
            g.fillRect(x, y, 6, 6);
        }
    }
}