/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {

        Connection cn = ConexionDB.getConexion();

        if (cn != null) {
            System.out.println("AutoTechManager conectado correctamente.");
        } else {
            System.out.println("Error de conexion.");
        }

        ConexionDB.cerrarConexion();
    }
}