/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.MovimientoInventarioDAO;
import modelo.MovimientoInventario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventarioDAOImpl
        implements MovimientoInventarioDAO {

    @Override
    public boolean guardar(MovimientoInventario movimiento)
            throws SQLException {

        String sql = "INSERT INTO MovimientosInventario "
                + "(id_repuesto, tipo_movimiento, cantidad, fecha, "
                + "motivo, id_orden, id_consumo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, movimiento.getIdRepuesto());
            ps.setString(2, movimiento.getTipoMovimiento());
            ps.setInt(3, movimiento.getCantidad());
            ps.setTimestamp(4,
                    Timestamp.valueOf(movimiento.getFecha()));
            ps.setString(5, movimiento.getMotivo());

            if (movimiento.getIdOrden() != null) {
                ps.setInt(6, movimiento.getIdOrden());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            if (movimiento.getIdConsumo() != null) {
                ps.setInt(7, movimiento.getIdConsumo());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<MovimientoInventario> listarPorRepuesto(int idRepuesto)
            throws SQLException {

        List<MovimientoInventario> lista = new ArrayList<>();

        String sql = "SELECT * FROM MovimientosInventario "
                + "WHERE id_repuesto = ? ORDER BY fecha DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idRepuesto);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(obtenerMovimiento(rs));
                }
            }
        }

        return lista;
    }

    @Override
    public List<MovimientoInventario> listarPorOrden(int idOrden)
            throws SQLException {

        List<MovimientoInventario> lista = new ArrayList<>();

        String sql = "SELECT * FROM MovimientosInventario "
                + "WHERE id_orden = ? ORDER BY fecha DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(obtenerMovimiento(rs));
                }
            }
        }

        return lista;
    }

    private MovimientoInventario obtenerMovimiento(ResultSet rs)
            throws SQLException {

        MovimientoInventario movimiento = new MovimientoInventario();

        movimiento.setIdMovimiento(rs.getInt("id_movimiento"));
        movimiento.setIdRepuesto(rs.getInt("id_repuesto"));
        movimiento.setTipoMovimiento(rs.getString("tipo_movimiento"));
        movimiento.setCantidad(rs.getInt("cantidad"));

        Timestamp fecha = rs.getTimestamp("fecha");

        if (fecha != null) {
            movimiento.setFecha(fecha.toLocalDateTime());
        }

        movimiento.setMotivo(rs.getString("motivo"));

        int idOrden = rs.getInt("id_orden");
        movimiento.setIdOrden(rs.wasNull() ? null : idOrden);

        int idConsumo = rs.getInt("id_consumo");
        movimiento.setIdConsumo(rs.wasNull() ? null : idConsumo);

        return movimiento;
    }
}