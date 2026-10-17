/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representa al jugador principal del juego espacial.
 * Hereda de la clase abstracta {@link Personaje} e implementa el renderizado
 * visual de una nave triangular.
 * 
 * Lo más complicado de esta clase:
 * 1. El mapeo de coordenadas relativas (vértices X e Y) para construir la forma 
 *    triangular mediante `fillPolygon` alineada dinámicamente con la posición base.
 * 2. La invocación correcta del constructor superclase con los parámetros de 
 *    posición inicial y dimensiones exactas para evitar desfasamientos al colisionar.
 * 
 * @author Dell
 */
public class Jugador_1 extends Personaje {

    /**
     * Constructor por defecto del jugador.
     * Inicializa la posición de la nave centrada horizontalmente cerca de la base
     * de la pantalla (X=430, Y=620) con tamaño 30x30 y una velocidad de paso de 20 píxeles.
     */
    public Jugador_1() {
        super(430, 620, 30, 30, 20);
    }

    /**
     * Renderiza la nave espacial del jugador con forma de triángulo verde.
     * 
     * @param g Objeto Graphics sobre el cual se realiza el dibujo.
     */
    @Override
    public void dibujar(Graphics g) {
        // Establece el color de la nave
        g.setColor(Color.GREEN);

        // Puntos X para los 3 vértices (Punta central, Esquina izquierda, Esquina derecha)
        int[] xPuntos = { x + (ancho / 2), x, x + ancho };

        // Puntos Y para los 3 vértices (Arriba, Abajo izquierda, Abajo derecha)
        int[] yPuntos = { y, y + alto, y + alto };

        // Dibujamos el triángulo relleno de 3 puntos
        g.fillPolygon(xPuntos, yPuntos, 3);
    }
}