/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Dell
 */
public class Conexion {
    public static Connection conectar(){
        try{
            
            String url="jdbc:mysql://localhost:3306/juego";
            String user="root", pass = "";
            Connection cn = DriverManager.getConnection(url,user,pass);
            System.out.println("Conexion exitosa");
            return cn;
        }
        catch(Exception e){
            System.out.println("Error Conexion; " + e);
            JOptionPane.showMessageDialog(null, "ERROR EN LA CONEXION...");
            System.exit(0);
            return null;
        }
    }   
}
