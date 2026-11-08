/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.ClienteDAO;
import modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public boolean guardar(Cliente cliente) throws SQLException {

        String sql = "INSERT INTO Clientes "
                + "(nombre, dni, telefono, correo, direccion, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDni());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getDireccion());
            ps.setString(6, cliente.getEstado());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizar(Cliente cliente) throws SQLException {

        String sql = "UPDATE Clientes SET nombre = ?, dni = ?, "
                + "telefono = ?, correo = ?, direccion = ? "
                + "WHERE id_cliente = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDni());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getDireccion());
            ps.setInt(6, cliente.getIdCliente());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cambiarEstado(int idCliente, String estado)
            throws SQLException {

        String sql = "UPDATE Clientes SET estado = ? "
                + "WHERE id_cliente = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idCliente);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Cliente buscarPorId(int idCliente) throws SQLException {

        String sql = "SELECT * FROM Clientes WHERE id_cliente = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return obtenerCliente(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Cliente> listar() throws SQLException {

        List<Cliente> lista = new ArrayList<>();

        String sql = "SELECT * FROM Clientes ORDER BY id_cliente DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(obtenerCliente(rs));
            }
        }

        return lista;
    }

    private Cliente obtenerCliente(ResultSet rs) throws SQLException {

        Cliente cliente = new Cliente();

        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setDni(rs.getString("dni"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setCorreo(rs.getString("correo"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setEstado(rs.getString("estado"));

        return cliente;
    }
}