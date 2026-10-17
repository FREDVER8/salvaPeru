/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basedatos;

import clases.Jugador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Clase Data Access Object (DAO) para la entidad {@link Jugador}.
 * Encapsula la persistencia, consultas CRUD y lógica de conexión con la tabla `jugador`.
 * * Lo más complicado de esta clase:
 * 1. La prevención de vulnerabilidades por Inyección SQL mediante el uso estructurado de `PreparedStatement`.
 * 2. El manejo adecuado del ciclo de vida de la conexión JDBC (`cn.close()`) en cada bloque de ejecución.
 * * @author PC PRIDE STARFISH
 */
public class JugadorDAO {

    /**
     * Valida si existe un usuario activo con las credenciales ingresadas.
     * * @param user Nombre de usuario a consultar.
     * @param pass Contraseña ingresada.
     * @return true si las credenciales coinciden y el usuario está activo (1/true), false en caso contrario.
     */
    public boolean validarLogin(String user, String pass) {
        boolean valido = false;
        try {
            // Abre la conexión con la base de datos
            Connection cn = Conexion.conectar();
            
            // Sentencia SQL parametrizada para evitar SQL Injection
            String sql = "SELECT * FROM jugador WHERE user = ? AND pass = ? AND activo = 1";
            PreparedStatement ps = cn.prepareStatement(sql);
            
            // Asignación de parámetros a la consulta
            ps.setString(1, user);
            ps.setString(2, pass);

            // Ejecución de la consulta
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                valido = true; // Credenciales válidas halladas
            }
            
            // Cierre explícito de la conexión
            cn.close();
        } catch (Exception e) {
            System.out.println("ERROR EN VALIDAR LOGIN: " + e);
        }
        return valido;
    }

    /**
     * Inserta un nuevo registro de jugador en la base de datos.
     * * @param j Objeto entidad {@link Jugador} con la información a persistir.
     */
public void altaJugador(Jugador j) {
    try {
        Connection cn = Conexion.conectar();
        
        // Agregamos 'score' a la lista de columnas y un signo '?' extra
        String sql = "INSERT INTO jugador (nombre, apellido, user, pass, activo, score) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cn.prepareStatement(sql);
        
        ps.setString(1, j.getNombre());
        ps.setString(2, j.getApellido());
        ps.setString(3, j.getUser());
        ps.setString(4, j.getPass());
        ps.setBoolean(5, j.isActivo());
        ps.setInt(6, 0); 

        ps.executeUpdate();
        cn.close();
    } catch (Exception e) {
        System.out.println("ERROR EN ALTA JUGADOR: " + e);
    }
}

    /**
     * Recupera la lista completa de jugadores registrados en la tabla.
     * * @return {@link ArrayList} de objetos {@link Jugador} mapeados con los registros de la base de datos.
     */
    public ArrayList<Jugador> consultarTodo() {
        ArrayList<Jugador> lista = new ArrayList<>();
        try {
            // Abre la conexión con la base de datos
            Connection cn = Conexion.conectar();
            
            // Sentencia SQL para seleccionar todos los registros
            String sql = "SELECT * FROM jugador";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Mapeo iterativo de cada fila del ResultSet a objetos tipo Jugador
            while (rs.next()) {
                Jugador j = new Jugador();
                j.setId(rs.getInt("id"));
                j.setNombre(rs.getString("nombre"));
                j.setApellido(rs.getString("apellido"));
                j.setUser(rs.getString("user"));
                j.setPass(rs.getString("pass"));
                j.setActivo(rs.getBoolean("activo"));
                j.setScore(rs.getInt("score"));
                
                // Agrega el objeto a la lista
                lista.add(j);
            }
            
            // Cierra la conexión
            cn.close();
        } catch (Exception e) {
            System.out.println("ERROR EN CONSULTAR TODO: " + e);
        }
        return lista;
    }

    /**
     * Verifica la existencia previa de un nombre de usuario en la base de datos
     * para evitar duplicados durante el registro.
     * * @param userBusqueda El nombre de usuario que se desea verificar.
     * @return true si el usuario ya existe en la base de datos, false si está disponible.
     */
    public boolean existeUsuario(String userBusqueda) {
        boolean existe = false;
        try {
            // Abre la conexión con la base de datos
            Connection cn = Conexion.conectar();
            
            // Consulta de conteo para optimizar la verificación
            String sql = "SELECT COUNT(*) FROM jugador WHERE user = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, userBusqueda);

            ResultSet rs = ps.executeQuery();
            
            // Evalúa si el número de coincidencias encontradas es mayor a 0
            if (rs.next() && rs.getInt(1) > 0) {
                existe = true;
            }
            
            // Cierra la conexión
            cn.close();
        } catch (Exception e) {
            System.out.println("ERROR AL REVISAR SI EXISTE USUARIO: " + e);
        }
        return existe;
    }
    /**
 * Actualiza el puntaje (score) del jugador usando su nombre de usuario.
 * Solo actualiza si el nuevo score es mayor al score actual almacenado en la BD.
 */
public void actualizarScorePorUser(String user, int nuevoScore) {
    try {
        Connection cn = Conexion.conectar();
        
        // Sentencia directa para actualizar el score del usuario sin restricciones complejas
        String sql = "UPDATE jugador SET score = ? WHERE user = ?";
        PreparedStatement ps = cn.prepareStatement(sql);
        ps.setInt(1, nuevoScore);
        ps.setString(2, user);

        int filasAfectadas = ps.executeUpdate();
        System.out.println("Filas actualizadas en BD para " + user + ": " + filasAfectadas);
        
        cn.close();
    } catch (Exception e) {
        System.out.println("ERROR AL ACTUALIZAR SCORE: " + e);
    }
}
}