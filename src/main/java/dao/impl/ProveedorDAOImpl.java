/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.ProveedorDAO;
import modelo.Proveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAOImpl implements ProveedorDAO {

    @Override
    public boolean guardar(Proveedor proveedor) throws SQLException {

        String sql = "INSERT INTO Proveedores "
                + "(nombre, telefono, correo, direccion, estado) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getTelefono());
            ps.setString(3, proveedor.getCorreo());
            ps.setString(4, proveedor.getDireccion());
            ps.setString(5, proveedor.getEstado());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizar(Proveedor proveedor) throws SQLException {

        String sql = "UPDATE Proveedores SET nombre = ?, telefono = ?, "
                + "correo = ?, direccion = ?, estado = ? "
                + "WHERE id_proveedor = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getTelefono());
            ps.setString(3, proveedor.getCorreo());
            ps.setString(4, proveedor.getDireccion());
            ps.setString(5, proveedor.getEstado());
            ps.setInt(6, proveedor.getIdProveedor());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cambiarEstado(int idProveedor, String estado)
            throws SQLException {

        String sql = "UPDATE Proveedores SET estado = ? "
                + "WHERE id_proveedor = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idProveedor);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Proveedor buscarPorId(int idProveedor) throws SQLException {

        String sql = "SELECT * FROM Proveedores WHERE id_proveedor = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return obtenerProveedor(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Proveedor> listar() throws SQLException {

        List<Proveedor> lista = new ArrayList<>();

        String sql = "SELECT * FROM Proveedores ORDER BY id_proveedor DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(obtenerProveedor(rs));
            }
        }

        return lista;
    }

    private Proveedor obtenerProveedor(ResultSet rs) throws SQLException {

        Proveedor proveedor = new Proveedor();

        proveedor.setIdProveedor(rs.getInt("id_proveedor"));
        proveedor.setNombre(rs.getString("nombre"));
        proveedor.setTelefono(rs.getString("telefono"));
        proveedor.setCorreo(rs.getString("correo"));
        proveedor.setDireccion(rs.getString("direccion"));
        proveedor.setEstado(rs.getString("estado"));

        return proveedor;
    }
}