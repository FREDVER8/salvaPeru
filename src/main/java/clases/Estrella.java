/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * Clase que representa una estrella individual en el fondo espacial animado.
 * Modela el efecto de desplazamiento vertical continuo (parallax) asignando
 * posiciones, tamaños y velocidades aleatorias a cada partícula.
 * 
 * Lo más complicado de esta clase:
 * 1. La simulación de profundidad visual mediante la generación aleatoria coordinada 
 *    de tamaño (`tam`) y velocidad (`vel`), permitiendo que estrellas más pequeñas o grandes
 *    se muevan a ritmos distintos.
 * 2. El reciclado infinito de la posición $Y$ al superar el límite inferior del panel (`y > alto`)
 *    para mantener un flujo continuo sin instanciar nuevos objetos en memoria.
 * 
 * @author Dell
 */
public class Estrella {

    /** Coordenada X fija de la estrella en el panel. */
    private int x;

    /** Coordenada Y actual de la estrella en el panel. */
    private int y;

    /** Tamaño (diámetro) de la estrella en píxeles. */
    private int tam;

    /** Velocidad de caída vertical de la estrella. */
    private int vel;

    /** Generador de números aleatorios para la inicialización de atributos. */
    private Random r = new Random();

    /**
     * Constructor de la clase `Estrella`.
     * Genera la posición inicial dentro de los límites del panel y asigna
     * aleatoriamente un tamaño y velocidad entre 1 y 3 píxeles.
     * 
     * @param ancho Ancho máximo del contenedor para posicionar en X.
     * @param alto Alto máximo del contenedor para posicionar en Y.
     */
    public Estrella(int ancho, int alto) {
        // Asigna coordenadas X e Y de forma aleatoria dentro de la pantalla
        this.x = r.nextInt(ancho);
        this.y = r.nextInt(alto);

        // Define un tamaño aleatorio entre 1 y 3 píxeles
        this.tam = r.nextInt(3) + 1;

        // Define una velocidad de caída aleatoria entre 1 y 3 píxeles por frame
        this.vel = r.nextInt(3) + 1;
    }

    /**
     * Desplaza la estrella hacia abajo según su velocidad.
     * Si la estrella rebasa el límite inferior de la pantalla, se reinicia en la parte superior (Y = 0).
     * 
     * @param alto Límite inferior de la pantalla en píxeles.
     */
    public void mover(int alto) {
        // Incrementa la coordenada Y para simular la caída
        this.y += this.vel;

        // Reinicia la posición arriba cuando sale del área visible
        if (this.y > alto) {
            this.y = 0;
        }
    }

    /**
     * Renderiza la estrella en pantalla como un círculo blanco relleno.
     * 
     * @param g Objeto Graphics sobre el cual se realiza el dibujo.
     */
    public void pintar(Graphics g) {
        // Asigna el color blanco para la luz de la estrella
        g.setColor(Color.WHITE);

        // Dibuja el punto o círculo con las dimensiones configuradas
        g.fillOval(x, y, tam, tam);
    }
}