/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

import config.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrearUsuarioAdmin {

    public static void main(String[] args) {

        String usuario = "Administrador";
        String contrasena = "Admin";

        String hash = PasswordUtil.generarHash(contrasena);

        String sql = "INSERT INTO Usuarios "
                + "(usuario, contrasena_hash, rol, estado) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, hash);
            ps.setString(3, "ADMINISTRADOR");
            ps.setString(4, "ACTIVO");

            ps.executeUpdate();

            System.out.println("Usuario administrador creado.");
            System.out.println("Usuario: " + usuario);
            System.out.println("Contraseña: " + contrasena);

        } catch (SQLException e) {
            System.out.println("No se pudo crear el usuario: "
                    + e.getMessage());
        }
    }
}