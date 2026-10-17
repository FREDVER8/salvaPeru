/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Clase que representa proyectiles o disparos emitidos por la nave del jugador.
 * Gestiona el desplazamiento ascendente a lo largo del eje Y y su representación visual en pantalla.
 * 
 * Lo más complicado de esta clase:
 * 1. La sincronización de coordenadas iniciales con la posición exacta del cañón del jugador para evitar desfasamientos.
 * 2. El control de vida útil dentro de las listas de iteración del panel principal, eliminando instancias que rebasan $Y < 0$.
 * 
 * @author Dell
 */
public class Disparo {

    /** Posición X inicial y fija del disparo. */
    private int x;

    /** Posición Y dinámicamente actualizada durante el trayecto. */
    private int y;

    /** Velocidad de avance vertical hacia la parte superior. */
    private int vel;

    /** Ancho en píxeles del proyectil. */
    private int ancho;

    /** Alto en píxeles del proyectil. */
    private int alto;

    /**
     * Constructor de la clase `Disparo`.
     * Inicializa las coordenadas de salida del proyectil y define dimensiones predeterminadas (5x15) 
     * con una velocidad de desplazamiento fija de 6 píxeles por ciclo.
     * 
     * @param x Posición X de origen (alineada con la nave).
     * @param y Posición Y de origen (alineada con la punta de la nave).
     */
    public Disparo(int x, int y) {
        this.x = x;
        this.y = y;
        this.vel = 6;
        this.ancho = 5;
        this.alto = 15;
    }

    /**
     * Desplaza el proyectil hacia arriba restando la velocidad a la coordenada Y.
     */
    public void mover() {
        // Decrementa la coordenada Y para simular la trayectoria ascendente
        this.y -= vel;
    }

    /**
     * Renderiza el proyectil en pantalla como un rectángulo rojo.
     * 
     * @param g Objeto Graphics sobre el cual se realiza el dibujo.
     */
    public void dibujar(Graphics g) {
        // Establece el color rojo para el rayo o bala
        g.setColor(Color.RED);

        // Dibuja la figura rectangular del proyectil
        g.fillRect(x, y, ancho, alto);
    }

    /**
     * Obtiene la posición X actual del disparo.
     * 
     * @return La coordenada X.
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene la posición Y actual del disparo.
     * 
     * @return La coordenada Y.
     */
    public int getY() {
        return y;
    }
}