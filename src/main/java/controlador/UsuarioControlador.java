/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Usuario;
import servicio.UsuarioServicio;
import java.sql.SQLException;

public class UsuarioControlador {

    private final UsuarioServicio servicio;

    public UsuarioControlador() {
        servicio = new UsuarioServicio();
    }

    public Usuario autenticar(String usuario, String contrasena)
            throws SQLException {

        return servicio.autenticar(usuario, contrasena);
    }
}