/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao.impl;

import config.ConexionDB;
import dao.ActividadTrabajoDAO;
import modelo.ActividadTrabajo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ActividadTrabajoDAOImpl implements ActividadTrabajoDAO {

    @Override
    public boolean guardar(ActividadTrabajo actividad) throws SQLException {

        String sql = "INSERT INTO ActividadesTrabajo "
                + "(id_orden, id_mecanico, descripcion, fecha_inicio, "
                + "fecha_fin, estado, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, actividad.getIdOrden());
            ps.setInt(2, actividad.getIdMecanico());
            ps.setString(3, actividad.getDescripcion());

            if (actividad.getFechaInicio() != null) {
                ps.setTimestamp(4,
                        Timestamp.valueOf(actividad.getFechaInicio()));
            } else {
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            }

            if (actividad.getFechaFin() != null) {
                ps.setTimestamp(5,
                        Timestamp.valueOf(actividad.getFechaFin()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            ps.setString(6, actividad.getEstado());
            ps.setString(7, actividad.getObservaciones());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizarEstado(int idActividad, String estado)
            throws SQLException {

        String sql = "UPDATE ActividadesTrabajo "
                + "SET estado = ?, "
                + "fecha_fin = CASE "
                + "WHEN ? = 'COMPLETADA' THEN GETDATE() "
                + "ELSE NULL END "
                + "WHERE id_actividad = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setString(2, estado);
            ps.setInt(3, idActividad);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<ActividadTrabajo> listarPorOrden(int idOrden)
            throws SQLException {

        List<ActividadTrabajo> lista = new ArrayList<>();

        String sql = "SELECT * FROM ActividadesTrabajo "
                + "WHERE id_orden = ? "
                + "ORDER BY id_actividad";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ActividadTrabajo actividad = new ActividadTrabajo();

                    actividad.setIdActividad(
                            rs.getInt("id_actividad"));

                    actividad.setIdOrden(
                            rs.getInt("id_orden"));

                    actividad.setIdMecanico(
                            rs.getInt("id_mecanico"));

                    actividad.setDescripcion(
                            rs.getString("descripcion"));

                    Timestamp inicio = rs.getTimestamp("fecha_inicio");

                    if (inicio != null) {
                        actividad.setFechaInicio(
                                inicio.toLocalDateTime());
                    }

                    Timestamp fin = rs.getTimestamp("fecha_fin");

                    if (fin != null) {
                        actividad.setFechaFin(
                                fin.toLocalDateTime());
                    }

                    actividad.setEstado(
                            rs.getString("estado"));

                    actividad.setObservaciones(
                            rs.getString("observaciones"));

                    lista.add(actividad);
                }
            }
        }

        return lista;
    }

    @Override
    public boolean tienePendientes(int idOrden) throws SQLException {

        String sql = "SELECT COUNT(*) AS total "
                + "FROM ActividadesTrabajo "
                + "WHERE id_orden = ? "
                + "AND estado <> 'COMPLETADA'";

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
