/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.ServicioRepuestoDAO;
import modelo.ServicioRepuesto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioRepuestoDAOImpl implements ServicioRepuestoDAO {

    @Override
    public boolean guardar(ServicioRepuesto sr) throws SQLException {

        String sql = """
            INSERT INTO ServiciosRepuestos
            (id_servicio, id_repuesto, cantidad)
            VALUES (?, ?, ?)
        """;

        Connection con = ConexionDB.getConexion();

        if (con == null || con.isClosed()) {
            throw new SQLException("No hay conexión con la base de datos.");
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sr.getIdServicio());
            ps.setInt(2, sr.getIdRepuesto());
            ps.setInt(3, sr.getCantidad());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<ServicioRepuesto> listarPorServicio(int idServicio)
            throws SQLException {

        List<ServicioRepuesto> lista = new ArrayList<>();

        String sql = """
            SELECT sr.id_servicio_repuesto,
                   sr.id_servicio,
                   sr.id_repuesto,
                   sr.cantidad
            FROM ServiciosRepuestos sr
            WHERE sr.id_servicio = ?
            ORDER BY sr.id_servicio_repuesto
        """;

        Connection con = ConexionDB.getConexion();

        if (con == null || con.isClosed()) {
            throw new SQLException("No hay conexión con la base de datos.");
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idServicio);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ServicioRepuesto sr = new ServicioRepuesto();

                    sr.setIdServicioRepuesto(
                            rs.getInt("id_servicio_repuesto"));

                    sr.setIdServicio(rs.getInt("id_servicio"));
                    sr.setIdRepuesto(rs.getInt("id_repuesto"));
                    sr.setCantidad(rs.getInt("cantidad"));

                    lista.add(sr);
                }
            }
        }

        return lista;
    }

    @Override
    public boolean existeRelacion(int idServicio, int idRepuesto)
            throws SQLException {

        String sql = """
            SELECT COUNT(*)
            FROM ServiciosRepuestos
            WHERE id_servicio = ? AND id_repuesto = ?
        """;

        Connection con = ConexionDB.getConexion();

        if (con == null || con.isClosed()) {
            throw new SQLException("No hay conexión con la base de datos.");
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idServicio);
            ps.setInt(2, idRepuesto);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}