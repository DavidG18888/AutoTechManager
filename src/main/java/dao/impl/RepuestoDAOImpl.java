/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.RepuestoDAO;
import modelo.Repuesto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepuestoDAOImpl implements RepuestoDAO {

    @Override
    public boolean guardar(Repuesto repuesto) throws SQLException {

        String sql = "INSERT INTO Repuestos "
                + "(codigo, nombre, descripcion, id_proveedor, "
                + "existencia, stock_minimo, costo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, repuesto.getCodigo());
            ps.setString(2, repuesto.getNombre());
            ps.setString(3, repuesto.getDescripcion());
            ps.setInt(4, repuesto.getIdProveedor());
            ps.setInt(5, repuesto.getExistencia());
            ps.setInt(6, repuesto.getStockMinimo());
            ps.setDouble(7, repuesto.getCosto());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizar(Repuesto repuesto) throws SQLException {

        String sql = "UPDATE Repuestos SET codigo = ?, nombre = ?, "
                + "descripcion = ?, id_proveedor = ?, existencia = ?, "
                + "stock_minimo = ?, costo = ? WHERE id_repuesto = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, repuesto.getCodigo());
            ps.setString(2, repuesto.getNombre());
            ps.setString(3, repuesto.getDescripcion());
            ps.setInt(4, repuesto.getIdProveedor());
            ps.setInt(5, repuesto.getExistencia());
            ps.setInt(6, repuesto.getStockMinimo());
            ps.setDouble(7, repuesto.getCosto());
            ps.setInt(8, repuesto.getIdRepuesto());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Repuesto buscarPorId(int idRepuesto) throws SQLException {

        String sql = "SELECT * FROM Repuestos WHERE id_repuesto = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idRepuesto);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return obtenerRepuesto(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Repuesto> listar() throws SQLException {

        List<Repuesto> lista = new ArrayList<>();

        String sql = "SELECT * FROM Repuestos ORDER BY id_repuesto DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(obtenerRepuesto(rs));
            }
        }

        return lista;
    }

    @Override
    public List<Repuesto> listarBajoStock() throws SQLException {

        List<Repuesto> lista = new ArrayList<>();

        String sql = "SELECT * FROM Repuestos "
                + "WHERE existencia <= stock_minimo "
                + "ORDER BY existencia ASC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(obtenerRepuesto(rs));
            }
        }

        return lista;
    }

    private Repuesto obtenerRepuesto(ResultSet rs) throws SQLException {

        Repuesto repuesto = new Repuesto();

        repuesto.setIdRepuesto(rs.getInt("id_repuesto"));
        repuesto.setCodigo(rs.getString("codigo"));
        repuesto.setNombre(rs.getString("nombre"));
        repuesto.setDescripcion(rs.getString("descripcion"));
        repuesto.setIdProveedor(rs.getInt("id_proveedor"));
        repuesto.setExistencia(rs.getInt("existencia"));
        repuesto.setStockMinimo(rs.getInt("stock_minimo"));
        repuesto.setCosto(rs.getDouble("costo"));

        return repuesto;
    }
}