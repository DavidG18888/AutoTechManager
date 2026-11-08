/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.UsuarioDAO;
import dao.impl.UsuarioDAOImpl;
import modelo.Usuario;
import utilidades.PasswordUtil;

import java.sql.SQLException;

public class UsuarioServicio {

    private final UsuarioDAO usuarioDAO;

    public UsuarioServicio() {
        usuarioDAO = new UsuarioDAOImpl();
    }

    public Usuario autenticar(String nombreUsuario, String contrasena)
            throws SQLException {

        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Ingrese su nombre de usuario.");
        }

        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException(
                    "Ingrese su contraseña.");
        }

        Usuario usuario = usuarioDAO.buscarPorUsuario(
                nombreUsuario.trim());

        if (usuario == null) {
            return null;
        }

        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            throw new IllegalArgumentException(
                    "Su cuenta está inactiva. Contacte al administrador.");
        }

        boolean correcta = PasswordUtil.verificarContrasena(
                contrasena,
                usuario.getContrasenaHash());

        if (!correcta) {
            return null;
        }

        return usuario;
    }
}