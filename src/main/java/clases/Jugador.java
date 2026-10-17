/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 * Clase modelo POJO que representa la entidad de un Jugador o usuario del sistema.
 * Contiene los datos personales, credenciales de acceso, estado de la cuenta
 * y puntaje (score) para su mapeo con la base de datos mediante el patrón DAO.
 * 
 * @author Dell
 */
public class Jugador {

    /** Identificador único autoincrementable del jugador en la base de datos. */
    private int id;

    /** Nombre de pila del jugador. */
    private String nombre;

    /** Apellido del jugador. */
    private String apellido;

    /** Nombre de usuario o nick para inicio de sesión. */
    private String user;

    /** Contraseña de acceso a la cuenta. */
    private String pass;

    /** Estado lógico del jugador (true si está habilitado, false si está desactivado). */
    private boolean activo;
    
    /** Puntaje acumulado o récord alcanzado por el jugador. */
    private int score;

    /**
     * Constructor por defecto.
     * Inicializa los campos de texto vacíos, el estado activo en false y el score en 0.
     */
    public Jugador() {
        this.nombre = "";
        this.apellido = "";
        this.user = "";
        this.pass = "";
        this.activo = false;
        this.score = 0;
    }

    /**
     * Constructor para registro de nuevos jugadores (sin ID asignado y score por defecto en 0).
     */
    public Jugador(String nombre, String apellido, String user, String pass, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.user = user;
        this.pass = pass;
        this.activo = activo;
        this.score = 0;
    }

    /**
     * Constructor para registro de nuevos jugadores asignando puntaje inicial.
     */
    public Jugador(String nombre, String apellido, String user, String pass, boolean activo, int score) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.user = user;
        this.pass = pass;
        this.activo = activo;
        this.score = score;
    }

    /**
     * Constructor completo con ID y Score.
     * Se utiliza al consultar y mapear registros existentes recuperados de la base de datos.
     */
    public Jugador(int id, String nombre, String apellido, String user, String pass, boolean activo, int score) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.user = user;
        this.pass = pass;
        this.activo = activo;
        this.score = score; // <-- SE AGREGÓ ESTA LÍNEA QUE FALTABA
    }

    // --- GETTERS Y SETTERS ---

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public boolean isActivo() {
        return activo;
    }

    // <-- GETTER Y SETTER DE SCORE AGREGADOS -->
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}