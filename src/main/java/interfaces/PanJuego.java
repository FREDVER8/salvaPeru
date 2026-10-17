/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import clases.Disparo;
import clases.Enemigo;
import clases.Estrella;
import clases.Jugador_1; 
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Panel principal de gameplay en el motor gráfico de Swing.
 * Administra el bucle de juego (Game Loop), la gestión de entradas por teclado mediante banderas,
 * la detección de colisiones geométricas y las transiciones progresivas de dificultad entre niveles.
 * 
 * @author Dell
 */
public class PanJuego extends JPanel implements ActionListener, KeyListener {

    /** Temporizador que controla la tasa de refresco (loop de juego) cada 20 milisegundos (50 FPS). */
    private Timer reloj;

    /** Ancho predefinido del panel de juego en píxeles. */
    private final int ANCHO = 900;

    /** Alto predefinido del panel de juego en píxeles. */
    private final int ALTO = 700;

    /** Colección dinámica que almacena las entidades estelares para el fondo en movimiento. */
    private ArrayList<Estrella> estrellas;

    /** Instancia de la nave principal controlada por el usuario. */
    private Jugador_1 jugador; 

    /** Lista dinámica que almacena los objetos enemigos activos en el nivel actual. */
    private ArrayList<Enemigo> enemigos;

    /** Velocidades escalonadas predefinidas para los diferentes grados de dificultad de los enemigos. */
    private int vel1 = 4, vel2 = 6, vel3 = 8;

    /** Lista dinámica que almacena los proyectiles activos disparados por el jugador. */
    private ArrayList<Disparo> balas;

    /** Bandera lógica para el movimiento continuo de la nave hacia la izquierda. */
    private boolean tecla_izq = false;

    /** Bandera lógica para el movimiento continuo de la nave hacia la derecha. */
    private boolean tecla_der = false;

    /** Puntuación acumulada actual del jugador durante la partida. */
    private int puntos = 0;

    /** Vidas restantes disponibles del jugador antes de la derrota. */
    private int vidas = 3;

    /** Identificador numérico del nivel actual (1, 2 o 3). */
    private int nivelActual = 1;

    /**
     * Constructor principal del panel de juego.
     * Configura el color de fondo, inicializa las estructuras de datos, vincula los escuchadores 
     * de eventos de teclado y arranca el temporizador principal del juego.
     */
    public PanJuego() {
        setBackground(Color.BLACK);
        estrellas = new ArrayList<>();
        jugador = new Jugador_1(); 
        crearEstrellas();
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        
        // Inicia el bucle de actualización del juego a 50 FPS (cada 20 milisegundos)
        reloj = new Timer(20, this);
        reloj.start();
        
        enemigos = new ArrayList<>();
        enemigosNivel1();
        balas = new ArrayList<>();
        
        // Solicita y asegura el foco del teclado en la ventana tras renderizar la interfaz gráfica
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    /**
     * Renderiza todos los elementos gráficos del juego en cada fotograma.
     * Dibuja secuencialmente el fondo de estrellas, los enemigos, los proyectiles,
     * la nave del jugador y el panel de información en pantalla (HUD).
     * 
     * @param g Objeto Graphics encargado de las operaciones de dibujo en 2D.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibuja el fondo espacial dinámico recorriendo la lista de estrellas
        for (Estrella e : estrellas) {
            e.pintar(g);
        }
        
        // Renderiza cada uno de los enemigos activos en la pantalla de juego
        for (Enemigo e : enemigos) {
            e.dibujar(g);
        }
        
        // Renderiza los proyectiles activos disparados por el usuario
        for (Disparo d : balas) {
            d.dibujar(g);
        }

        // Dibuja la nave del jugador en su coordenada actual de posición
        jugador.dibujar(g);

        // Despliega los marcadores informativos (HUD: Puntos, Vidas y Nivel) en la parte superior
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("PUNTOS " + puntos, 700, 30);
        g.drawString("VIDAS " + vidas, 50, 30);
        g.drawString("NIVEL " + nivelActual, 400, 30);
    }

    /**
     * Ejecuta la lógica general de actualización para mover las entidades del escenario.
     */
    public void actualizar() {
        moverEstrellas();
        moverEnemigos();
        disparar();
    }

    /**
     * Inicializa y llena la colección con 100 objetos estrella con posiciones aleatorias.
     */
    public void crearEstrellas() {
        for (int i = 0; i < 100; i++) {
            estrellas.add(new Estrella(ANCHO, ALTO));
        }
    }

    /**
     * Desplaza de manera cíclica cada objeto estrella para simular la profundidad espacial.
     */
    public void moverEstrellas() {
        for (Estrella e : estrellas) {
            e.mover(ALTO);
        }
    }

    /**
     * Carga y configura el patrón inicial de enemigos correspondiente al Nivel 1.
     * Instancia dos enemigos con velocidades básicas y desfasa la altura del primero 
     * para que aparezca de manera progresiva fuera de la pantalla.
     */
    public void enemigosNivel1() {
        enemigos.add(new Enemigo(vel1));
        enemigos.add(new Enemigo(vel2));
        
        // Escalona la aparición vertical del primer enemigo fuera del límite superior
        enemigos.get(0).setY(-150);
    }

    /**
     * Carga y configura el patrón de enemigos correspondiente al Nivel 2.
     * Limpia la lista anterior de entidades, instancia tres enemigos con mayor velocidad 
     * y distribuye sus coordenadas en Y para evitar que aparezcan agrupados al mismo tiempo.
     */
    public void enemigosNivel2() {
        enemigos.clear(); 
        enemigos.add(new Enemigo(vel2));
        enemigos.add(new Enemigo(vel3));
        enemigos.add(new Enemigo(vel2));

        // Desfasa las alturas iniciales de forma escalonada fuera de la pantalla
        enemigos.get(0).setY(-100);
        enemigos.get(1).setY(-250);
        enemigos.get(2).setY(-400);
    }

    /**
     * Carga y configura el patrón de alta dificultad correspondiente al Nivel 3.
     * Limpia la lista previa e instancia cuatro enemigos con velocidades avanzadas (hasta 12 píxeles),
     * espaciando sus posiciones verticales para generar una mayor densidad de obstáculos.
     */
    public void enemigosNivel3() {
        enemigos.clear(); 
        enemigos.add(new Enemigo(vel3));
        enemigos.add(new Enemigo(10));
        enemigos.add(new Enemigo(10));
        enemigos.add(new Enemigo(12));

        // Aplica rangos de aparición más amplios para lidiar con el flujo acelerado de enemigos
        enemigos.get(0).setY(-100);
        enemigos.get(1).setY(-200);
        enemigos.get(2).setY(-350);
        enemigos.get(3).setY(-500);
    }

    /**
     * Almacena el puntaje final obtenido por el usuario autenticado comunicándose con la capa de datos (DAO).
     * 
     * @param puntajeObtenido Cantidad total de puntos acumulados al finalizar la partida.
     */
    public void guardarPartida(int puntajeObtenido) {
        String usuarioActual = interfaces.FrLogin.usuarioLogueado;
    
        System.out.println("Intentando guardar score " + puntajeObtenido + " para el usuario: [" + usuarioActual + "]");
    
        if (usuarioActual != null && !usuarioActual.isEmpty()) {
            basedatos.JugadorDAO dao = new basedatos.JugadorDAO();
            dao.actualizarScorePorUser(usuarioActual, puntajeObtenido);
        } else {
            System.out.println("¡ERROR CRÍTICO! El usuario actual es nulo o vacío.");
        }
    }

    /**
     * Monitorea constantemente la puntuación acumulada para gestionar las reglas de progresión.
     * Controla el ascenso de nivel ejecutando los métodos de generación de enemigos correspondientes
     * o activa la pantalla de victoria (PanWin) al completar el objetivo máximo de puntos.
     */
    public void comprobarNivel() {
        // Transición hacia el Nivel 2 al alcanzar o superar los 200 puntos
        if (puntos >= 200 && nivelActual == 1) {
            nivelActual = 2;
            enemigosNivel2();
            System.out.println("--- ¡INICIA NIVEL 2! ---");
            
        // Transición hacia el Nivel 3 al alcanzar o superar los 400 puntos
        } else if (puntos >= 400 && nivelActual == 2) {
            nivelActual = 3;
            enemigosNivel3();
            System.out.println("--- ¡INICIA NIVEL 3! ---");
            
        // Transición a la pantalla de victoria (PanWin) al acumular 600 puntos en el nivel final
        } else if (puntos >= 600 && nivelActual == 3) {
            nivelActual = 4;
            System.out.println("--- ¡JUEGO GANADO! ---");
            reloj.stop(); // Detiene el bucle principal del juego
            
            guardarPartida(puntos); 
            
            // Localiza la ventana principal para reemplazar el panel actual por PanWin
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.setContentPane(new PanWin());
                topFrame.revalidate(); 
                topFrame.repaint();
            }
        }
    }

    /**
     * Actualiza la posición de cada enemigo activo en pantalla y evalúa si han rebasado los límites.
     */
    public void moverEnemigos() {
        for (Enemigo e : enemigos) {
            e.actualizar();
            e.revisarSalida();
        }
    }

    /**
     * Actualiza la trayectoria de las balas activas y elimina de la lista aquellos proyectiles
     * que rebasan el borde superior de la pantalla para optimizar recursos.
     */
    public void disparar() {
        for (int i = 0; i < balas.size(); i++) {
            balas.get(i).mover();

            // Remueve la bala si supera el límite superior del panel
            if (balas.get(i).getY() < 0) {
                balas.remove(i);
                i--;
            }
        }
    }

    /**
     * Método ejecutado de forma cíclica por el temporizador en cada fotograma.
     * Gestiona las entradas de movimiento continuo, procesa colisiones, evalúa niveles y repinta la interfaz.
     * 
     * @param e Objeto ActionEvent disparado por el Timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Aplica desplazamiento continuo a la izquierda si la bandera está activa
        if (tecla_izq) {
            jugador.MoverIzquierda();
        }
        
        // Aplica desplazamiento continuo a la derecha respetando los límites si la bandera está activa
        if (tecla_der) {
            jugador.MoverDerecha(ANCHO);
        }

        colisionesBalas(); 
        colisionJugador(); 
        comprobarNivel();  
        actualizar();      
        repaint();         
    }

    /**
     * Detecta y calcula las colisiones geométricas entre los proyectiles del jugador y los enemigos activos
     * utilizando la fórmula de distancia euclidiana (Teorema de Pitágoras). Si ocurre un impacto,
     * destruye al enemigo, incrementa la puntuación y remueve la bala.
     */
    public void colisionesBalas() {
        for (int i = 0; i < balas.size(); i++) {
            Disparo dis = balas.get(i);
            for (Enemigo e : enemigos) {
                if (e.estaActivo()) {
                    // Calcula la distancia euclidiana exacta entre las coordenadas de la bala y el enemigo
                    double distancia = Math.sqrt(
                            Math.pow(dis.getX() - e.getX(), 2) +
                            Math.pow(dis.getY() - e.getY(), 2));
                    
                    // Valida si la distancia de proximidad es menor al umbral de impacto (30 píxeles)
                    if (distancia < 30) {
                        balas.remove(i);
                        i--; 
                        e.morir();
                        puntos += 50;
                        System.out.println("PUNTOS: " + puntos);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Detecta colisiones físicas entre la nave del jugador y los enemigos activos.
     * Reduce las vidas disponibles en caso de impacto y, si se agotan por completo,
     * detiene el juego, guarda la puntuación y realiza la transición hacia la pantalla de derrota (PanLose).
     */
    public void colisionJugador() {
        for (Enemigo en : enemigos) {
            if (en.estaActivo()) {
                // Calcula la distancia euclidiana entre el jugador y el enemigo
                double distancia = Math.sqrt(
                        Math.pow(jugador.getX() - en.getX(), 2) +
                        Math.pow(jugador.getY() - en.getY(), 2));

                // Umbral de choque menor a 30 píxeles
                if (distancia < 30) {
                    vidas -= 1;
                    en.morir();

                    // Dispara la transición a la pantalla de derrota si las vidas llegan a cero
                    if (vidas < 1) {
                        reloj.stop(); 
                        
                        guardarPartida(puntos); 
                        
                        // Reemplaza el panel de juego actual por el panel de derrota (PanLose)
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                        if (topFrame != null) {
                            topFrame.setContentPane(new PanLose());
                            topFrame.revalidate(); 
                            topFrame.repaint();
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Captura el evento de presión de teclas para activar las banderas de movimiento horizontal 
     * (flechas direccionales) o instanciar un nuevo proyectil (barra espaciadora).
     * 
     * @param e Evento de teclado generado.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Activa la bandera de movimiento hacia la izquierda
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            tecla_izq = true;
        }
        
        // Activa la bandera de movimiento hacia la derecha
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            tecla_der = true;
        }
        
        // Crea e instancia un nuevo proyectil al presionar la barra espaciadora
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            balas.add(new Disparo(jugador.getX() + 10, jugador.getY()));
        }
    }

    /**
     * Captura el evento de liberación de teclas para desactivar las banderas de movimiento horizontal,
     * permitiendo un control fluido y sin interrupciones de la nave.
     * 
     * @param e Evento de teclado generado.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // Desactiva la bandera de movimiento izquierdo al soltar la tecla
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            tecla_izq = false;
        }
        
        // Desactiva la bandera de movimiento derecho al soltar la tecla
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            tecla_der = false;
        }
    }
}