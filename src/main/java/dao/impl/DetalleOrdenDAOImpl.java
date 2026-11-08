/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.DetalleOrdenDAO;
import modelo.DetalleOrden;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleOrdenDAOImpl implements DetalleOrdenDAO {

    @Override
    public boolean guardar(DetalleOrden detalle) throws SQLException {

        String sql = "INSERT INTO DetalleOrden "
                + "(id_orden, id_servicio, cantidad, costo_mano_obra, "
                + "tiempo_estimado, subtotal) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdOrden());
            ps.setInt(2, detalle.getIdServicio());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getCostoManoObra());
            ps.setDouble(5, detalle.getTiempoEstimado());
            ps.setDouble(6, detalle.getSubtotal());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizar(DetalleOrden detalle) throws SQLException {

        String sql = "UPDATE DetalleOrden SET id_servicio = ?, "
                + "cantidad = ?, costo_mano_obra = ?, "
                + "tiempo_estimado = ?, subtotal = ? "
                + "WHERE id_detalle = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdServicio());
            ps.setInt(2, detalle.getCantidad());
            ps.setDouble(3, detalle.getCostoManoObra());
            ps.setDouble(4, detalle.getTiempoEstimado());
            ps.setDouble(5, detalle.getSubtotal());
            ps.setInt(6, detalle.getIdDetalle());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<DetalleOrden> listarPorOrden(int idOrden)
            throws SQLException {

        List<DetalleOrden> lista = new ArrayList<>();

        String sql = "SELECT d.*, s.nombre AS nombre_servicio "
                + "FROM DetalleOrden d "
                + "INNER JOIN Servicios s "
                + "ON d.id_servicio = s.id_servicio "
                + "WHERE d.id_orden = ? "
                + "ORDER BY d.id_detalle";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    DetalleOrden detalle = obtenerDetalle(rs);

                    detalle.setNombreServicio(
                            rs.getString("nombre_servicio"));

                    lista.add(detalle);
                }
            }
        }

        return lista;
    }

    private DetalleOrden obtenerDetalle(ResultSet rs)
            throws SQLException {

        DetalleOrden detalle = new DetalleOrden();

        detalle.setIdDetalle(rs.getInt("id_detalle"));
        detalle.setIdOrden(rs.getInt("id_orden"));
        detalle.setIdServicio(rs.getInt("id_servicio"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setCostoManoObra(rs.getDouble("costo_mano_obra"));
        detalle.setTiempoEstimado(rs.getDouble("tiempo_estimado"));
        detalle.setSubtotal(rs.getDouble("subtotal"));

        return detalle;
    }
}