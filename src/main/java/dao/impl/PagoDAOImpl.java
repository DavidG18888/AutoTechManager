/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.PagoDAO;
import modelo.Pago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PagoDAOImpl implements PagoDAO {

    @Override
    public boolean guardar(Pago pago) throws SQLException {

        String sql = "INSERT INTO Pagos "
                + "(id_presupuesto, fecha, monto, metodo_pago, referencia, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, pago.getIdPresupuesto());
            ps.setTimestamp(2, Timestamp.valueOf(pago.getFecha()));
            ps.setDouble(3, pago.getMonto());
            ps.setString(4, pago.getMetodoPago());
            ps.setString(5, pago.getReferencia());
            ps.setString(6, pago.getObservaciones());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Pago buscarPorId(int idPago) throws SQLException {

        String sql = "SELECT * FROM Pagos WHERE id_pago = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPago);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return obtenerPago(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Pago> listarPorPresupuesto(int idPresupuesto)
            throws SQLException {

        List<Pago> lista = new ArrayList<>();

        String sql = "SELECT * FROM Pagos WHERE id_presupuesto = ? "
                + "ORDER BY fecha DESC";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPresupuesto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(obtenerPago(rs));
                }
            }
        }

        return lista;
    }

    @Override
    public double calcularTotalPagado(int idPresupuesto)
            throws SQLException {

        String sql = "SELECT COALESCE(SUM(monto), 0) AS total "
                + "FROM Pagos WHERE id_presupuesto = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPresupuesto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }

        return 0;
    }

    private Pago obtenerPago(ResultSet rs) throws SQLException {

        Pago pago = new Pago();

        pago.setIdPago(rs.getInt("id_pago"));
        pago.setIdPresupuesto(rs.getInt("id_presupuesto"));

        Timestamp fecha = rs.getTimestamp("fecha");

        if (fecha != null) {
            pago.setFecha(fecha.toLocalDateTime());
        }

        pago.setMonto(rs.getDouble("monto"));
        pago.setMetodoPago(rs.getString("metodo_pago"));
        pago.setReferencia(rs.getString("referencia"));
        pago.setObservaciones(rs.getString("observaciones"));

        return pago;
    }
}