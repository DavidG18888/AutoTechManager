/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao.impl;

import config.ConexionDB;
import dao.ControlCalidadDAO;
import modelo.ControlCalidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ControlCalidadDAOImpl implements ControlCalidadDAO {

    @Override
    public boolean guardar(ControlCalidad control) throws SQLException {

        String sql = "INSERT INTO ControlesCalidad "
                + "(id_orden, id_mecanico, fecha, descripcion, "
                + "resultado, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, control.getIdOrden());
            ps.setInt(2, control.getIdMecanico());
            ps.setTimestamp(3, Timestamp.valueOf(control.getFecha()));
            ps.setString(4, control.getDescripcion());
            ps.setString(5, control.getResultado());
            ps.setString(6, control.getObservaciones());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizar(ControlCalidad control) throws SQLException {

        String sql = "UPDATE ControlesCalidad "
                + "SET id_orden = ?, id_mecanico = ?, fecha = ?, "
                + "descripcion = ?, resultado = ?, observaciones = ? "
                + "WHERE id_control = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, control.getIdOrden());
            ps.setInt(2, control.getIdMecanico());
            ps.setTimestamp(3, Timestamp.valueOf(control.getFecha()));
            ps.setString(4, control.getDescripcion());
            ps.setString(5, control.getResultado());
            ps.setString(6, control.getObservaciones());
            ps.setInt(7, control.getIdControl());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<ControlCalidad> listarPorOrden(int idOrden)
            throws SQLException {

        List<ControlCalidad> lista = new ArrayList<>();

        String sql = "SELECT * FROM ControlesCalidad "
                + "WHERE id_orden = ? ORDER BY fecha DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ControlCalidad control = new ControlCalidad();

                    control.setIdControl(rs.getInt("id_control"));
                    control.setIdOrden(rs.getInt("id_orden"));
                    control.setIdMecanico(rs.getInt("id_mecanico"));

                    Timestamp fecha = rs.getTimestamp("fecha");

                    if (fecha != null) {
                        control.setFecha(fecha.toLocalDateTime());
                    }

                    control.setDescripcion(rs.getString("descripcion"));
                    control.setResultado(rs.getString("resultado"));
                    control.setObservaciones(
                            rs.getString("observaciones"));

                    lista.add(control);
                }
            }
        }

        return lista;
    }

    @Override
    public boolean tieneControlesPendientes(int idOrden)
            throws SQLException {

        String sql = "SELECT COUNT(*) AS total "
                + "FROM ControlesCalidad "
                + "WHERE id_orden = ? AND resultado <> 'APROBADO'";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        }

        return false;
    }
}
