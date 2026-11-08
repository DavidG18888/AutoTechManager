/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.ConsumoOrdenDAO;
import modelo.ConsumoOrden;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ConsumoOrdenDAOImpl implements ConsumoOrdenDAO {

    @Override
    public boolean guardar(ConsumoOrden consumo) throws SQLException {

        String sql = "INSERT INTO ConsumosOrden "
                + "(id_orden, id_repuesto, cantidad, costo_unitario, fecha) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(
                sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, consumo.getIdOrden());
            ps.setInt(2, consumo.getIdRepuesto());
            ps.setInt(3, consumo.getCantidad());
            ps.setDouble(4, consumo.getCostoUnitario());
            ps.setTimestamp(5, Timestamp.valueOf(consumo.getFecha()));

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return false;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    consumo.setIdConsumo(rs.getInt(1));
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<ConsumoOrden> listarPorOrden(int idOrden)
            throws SQLException {

        List<ConsumoOrden> lista = new ArrayList<>();

        String sql = "SELECT c.*, r.nombre AS nombre_repuesto "
                + "FROM ConsumosOrden c "
                + "INNER JOIN Repuestos r "
                + "ON c.id_repuesto = r.id_repuesto "
                + "WHERE c.id_orden = ? "
                + "ORDER BY c.id_consumo";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ConsumoOrden consumo = new ConsumoOrden();

                    consumo.setIdConsumo(rs.getInt("id_consumo"));
                    consumo.setIdOrden(rs.getInt("id_orden"));
                    consumo.setIdRepuesto(rs.getInt("id_repuesto"));
                    consumo.setCantidad(rs.getInt("cantidad"));

                    consumo.setCostoUnitario(
                            rs.getDouble("costo_unitario"));

                    consumo.setNombreRepuesto(
                            rs.getString("nombre_repuesto"));

                    Timestamp fecha = rs.getTimestamp("fecha");

                    if (fecha != null) {
                        consumo.setFecha(fecha.toLocalDateTime());
                    }

                    lista.add(consumo);
                }
            }
        }

        return lista;
    }
}