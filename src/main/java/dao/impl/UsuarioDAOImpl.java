/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.UsuarioDAO;
import modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario buscarPorUsuario(String usuario) throws SQLException {

        String sql = "SELECT id_usuario, usuario, contrasena_hash, "
                   + "rol, estado FROM Usuarios WHERE usuario = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Usuario u = new Usuario();

                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setContrasenaHash(rs.getString("contrasena_hash"));
                    u.setRol(rs.getString("rol"));
                    u.setEstado(rs.getString("estado"));

                    return u;
                }
            }
        }

        return null;
    }
}