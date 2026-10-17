/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * Clase que representa a los enemigos en el juego espacial.
 * Extiende de {@link Personaje} e incluye lógica para el manejo de ciclo de vida,
 * reaparición temporizada y desprendimiento fuera de pantalla.
 * 
 * Lo más complicado de esta clase:
 * 1. La gestión del temporizador asíncrono utilizando `System.currentTimeMillis()` 
 *    para coordinar el tiempo de reaparición (*respawn*) de forma aleatoria sin congelar el hilo principal.
 * 2. La reutilización eficiente del mismo objeto en memoria desactivándolo y reubicándolo 
 *    en lugar de instanciar nuevos objetos continuamente.
 * 
 * @author Dell
 */
public class Enemigo extends Personaje {

    /** Generador aleatorio para coordenadas y tiempos de reaparición. */
    private final Random ran = new Random();

    /** Tiempo de espera (en milisegundos) antes de reactivar al enemigo. */
    private long tiempoReaparicion;

    /** Marca de tiempo (ms) en la que el enemigo fue desactivado por última vez. */
    private long ultimoReinicio;

    /** Estado de visibilidad y actividad en la pantalla. */
    private boolean activo;

    /**
     * Constructor de la clase `Enemigo`.
     * Inicializa al enemigo fuera del área visible superior ($Y = -30$) con un tamaño 30x30,
     * marca su estado activo e inicia el temporizador de reaparición aleatorio.
     * 
     * @param velocidad Distancia en píxeles que recorre verticalmente por actualización.
     */
    public Enemigo(int velocidad) {
        super(0, -30, 30, 30, velocidad);
        this.activo = true;
        this.ultimoReinicio = System.currentTimeMillis();
        this.tiempoReaparicion = ran.nextInt(1000) + 500;
    }

    /**
     * Actualiza la posición o el estado del enemigo en cada ciclo de juego.
     * Si está activo, desciende verticalmente. Si está inactivo, evalúa si ha
     * transcurrido el tiempo necesario para hacerlo reaparecer en la parte superior.
     */
    public void actualizar() {
        long tiempoActual = System.currentTimeMillis();

        if (activo) {
            // Desplazamiento hacia abajo
            this.y = this.y + velocidad;
        } else {
            // Verificación para reaparición temporizada
            if (tiempoActual - ultimoReinicio >= tiempoReaparicion) {
                activo = true;
                this.y = -30;
                this.x = ran.nextInt(anchoPantalla - 30);
            }
        }
    }

    /**
     * Evalúa si el enemigo superó el borde inferior de la pantalla.
     * Si es así, lo desactiva e inicia el temporizador para su posterior reaparición.
     */
    public void revisarSalida() {
        if (this.activo && this.y > this.altoPantalla) {
            this.activo = false;
            this.ultimoReinicio = System.currentTimeMillis();
            this.tiempoReaparicion = ran.nextInt(1000) + 500;
        }
    }

    /**
     * Marca al enemigo como destruido, pausando su rendering y reiniciando 
     * su temporizador para el ciclo de reaparición.
     */
    public void morir() {
        activo = false;
        ultimoReinicio = System.currentTimeMillis();
        tiempoReaparicion = ran.nextInt(1000) + 500;
    }

    /**
     * Consulta el estado de actividad del enemigo.
     * 
     * @return true si el enemigo está activo en pantalla, false si está destruido o en espera.
     */
    public boolean estaActivo() {
        return this.activo;
    }

    /**
     * Renderiza al enemigo como un círculo rojo si se encuentra activo.
     * 
     * @param g Objeto Graphics sobre el cual se realiza el dibujo.
     */
    @Override
    public void dibujar(Graphics g) {
        if (activo) {
            g.setColor(Color.red);
            g.fillOval(x, y, alto, ancho);
        }
    }

    /**
     * Asigna directamente una nueva posición en el eje Y.
     * 
     * @param newY Nueva coordenada Y.
     */
    public void setY(int newY) {
        this.y = newY;
    }
}