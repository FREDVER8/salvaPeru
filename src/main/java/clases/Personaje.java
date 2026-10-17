/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.awt.Graphics;

/**
 * Clase abstracta base que representa a un personaje dentro del entorno gráfico.
 * Define las coordenadas, dimensiones, velocidad y comportamientos comunes de
 * desplazamiento en el eje horizontal, sirviendo como molde para clases hijas.
 * 
 * Lo más complicado de esta clase:
 * 1. La delimitación de colisiones contra las bordes de la pantalla en los métodos
 *    `MoverIzquierda` y `MoverDerecha` para evitar desbordamientos visuales.
 * 2. La definición del método abstracto `dibujar`, que fuerza a cada subclase a implementar
 *    su propia lógica de renderizado gráfico mediante el objeto `Graphics`.
 * 
 * @author Dell
 */
public abstract class Personaje {

    /** Coordenada X actual del personaje. */
    protected int x;

    /** Coordenada Y actual del personaje. */
    protected int y;

    /** Ancho del personaje en píxeles. */
    protected int ancho;

    /** Alto del personaje en píxeles. */
    protected int alto;

    /** Velocidad de desplazamiento en píxeles por actualización. */
    protected int velocidad;

    /** Ancho estático por defecto de la pantalla del juego. */
    protected static final int anchoPantalla = 900;

    /** Alto estático por defecto de la pantalla del juego. */
    protected static final int altoPantalla = 700;

    /**
     * Constructor de la clase abstracta `Personaje`.
     * Inicializa los atributos de posición, tamaño y velocidad del elemento en pantalla.
     * 
     * @param x Posición inicial en el eje X.
     * @param y Posición inicial en el eje Y.
     * @param ancho Ancho en píxeles.
     * @param alto Alto en píxeles.
     * @param velocidad Distancia de desplazamiento por movimiento.
     */
    public Personaje(int x, int y, int ancho, int alto, int velocidad) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.velocidad = velocidad;
    }

    /**
     * Desplaza al personaje hacia la izquierda restando la velocidad a la coordenada X.
     * Limita el movimiento para evitar que la entidad sobrepase el borde izquierdo (X = 0).
     */
    public void MoverIzquierda() {
        // Decrementa la posición X según la velocidad actual
        this.x = this.x - this.velocidad;
        
        // Evita que el personaje salga por el límite izquierdo
        if (this.x < 0) {
            x = 0;
        }
    }

    /**
     * Desplaza al personaje hacia la derecha sumando la velocidad a la coordenada X.
     * Limita el movimiento considerando el ancho del personaje y el ancho de la pantalla.
     * 
     * @param anchoPantalla Ancho del contenedor donde se desplaza el personaje.
     */
    public void MoverDerecha(int anchoPantalla) {
        // Incrementa la posición X según la velocidad actual
        this.x = this.x + this.velocidad;
        
        // Ajusta la posición si excede el límite del margen derecho
        if (x > anchoPantalla - ancho - 20) {
            x = anchoPantalla - ancho - 16;
        }
    }

    /**
     * Obtiene la posición X actual del personaje.
     * 
     * @return La coordenada X.
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene la posición Y actual del personaje.
     * 
     * @return La coordenada Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Método abstracto para el renderizado visual del personaje.
     * Debe ser implementado obligatoriamente por las subclases.
     * 
     * @param g Objeto Graphics sobre el cual se realiza el dibujo.
     */
    public abstract void dibujar(Graphics g);
}