/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.PresupuestoDAO;
import modelo.Presupuesto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PresupuestoDAOImpl implements PresupuestoDAO {

    @Override
    public boolean guardar(Presupuesto p) throws SQLException {

        String sql = """
            INSERT INTO Presupuestos
            (id_orden, fecha, mano_obra, repuestos, subtotal,
             descuento, porcentaje_impuesto, impuesto, total,
             estado, autorizado)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, p.getIdOrden());
            ps.setTimestamp(2, Timestamp.valueOf(p.getFecha()));
            ps.setDouble(3, p.getManoObra());
            ps.setDouble(4, p.getRepuestos());
            ps.setDouble(5, p.getSubtotal());
            ps.setDouble(6, p.getDescuento());
            ps.setDouble(7, p.getPorcentajeImpuesto());
            ps.setDouble(8, p.getImpuesto());
            ps.setDouble(9, p.getTotal());
            ps.setString(10, p.getEstado());
            ps.setBoolean(11, p.isAutorizado());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizar(Presupuesto p) throws SQLException {

        String sql = """
            UPDATE Presupuestos
            SET mano_obra = ?, repuestos = ?, subtotal = ?,
                descuento = ?, porcentaje_impuesto = ?,
                impuesto = ?, total = ?
            WHERE id_presupuesto = ?
        """;

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, p.getManoObra());
            ps.setDouble(2, p.getRepuestos());
            ps.setDouble(3, p.getSubtotal());
            ps.setDouble(4, p.getDescuento());
            ps.setDouble(5, p.getPorcentajeImpuesto());
            ps.setDouble(6, p.getImpuesto());
            ps.setDouble(7, p.getTotal());
            ps.setInt(8, p.getIdPresupuesto());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizarEstado(
            int idPresupuesto,
            String estado,
            boolean autorizado) throws SQLException {

        String sql = """
            UPDATE Presupuestos
            SET estado = ?, autorizado = ?
            WHERE id_presupuesto = ?
        """;

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setBoolean(2, autorizado);
            ps.setInt(3, idPresupuesto);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Presupuesto buscarPorId(int idPresupuesto)
            throws SQLException {

        String sql = """
            SELECT * FROM Presupuestos
            WHERE id_presupuesto = ?
        """;

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPresupuesto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return obtenerPresupuesto(rs);
                }
            }
        }

        return null;
    }

    @Override
    public Presupuesto buscarPorOrden(int idOrden) throws SQLException {

        String sql = """
            SELECT TOP 1 * FROM Presupuestos
            WHERE id_orden = ?
            ORDER BY fecha DESC
        """;

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return obtenerPresupuesto(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Presupuesto> listar() throws SQLException {

        List<Presupuesto> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM Presupuestos
            ORDER BY fecha DESC
        """;

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(obtenerPresupuesto(rs));
            }
        }

        return lista;
    }

    private Presupuesto obtenerPresupuesto(ResultSet rs)
            throws SQLException {

        Presupuesto p = new Presupuesto();

        p.setIdPresupuesto(rs.getInt("id_presupuesto"));
        p.setIdOrden(rs.getInt("id_orden"));

        Timestamp fecha = rs.getTimestamp("fecha");

        if (fecha != null) {
            p.setFecha(fecha.toLocalDateTime());
        }

        p.setManoObra(rs.getDouble("mano_obra"));
        p.setRepuestos(rs.getDouble("repuestos"));
        p.setSubtotal(rs.getDouble("subtotal"));
        p.setDescuento(rs.getDouble("descuento"));
        p.setPorcentajeImpuesto(
                rs.getDouble("porcentaje_impuesto"));
        p.setImpuesto(rs.getDouble("impuesto"));
        p.setTotal(rs.getDouble("total"));
        p.setEstado(rs.getString("estado"));
        p.setAutorizado(rs.getBoolean("autorizado"));

        return p;
    }
}