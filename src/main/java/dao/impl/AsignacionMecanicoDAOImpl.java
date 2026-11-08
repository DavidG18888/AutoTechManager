/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao.impl;

import config.ConexionDB;
import dao.AsignacionMecanicoDAO;
import modelo.AsignacionMecanico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AsignacionMecanicoDAOImpl
        implements AsignacionMecanicoDAO {

    @Override
    public boolean guardar(AsignacionMecanico asignacion)
            throws SQLException {

        String sql = "INSERT INTO AsignacionesMecanicos "
                + "(id_orden, id_mecanico, es_responsable, fecha_asignacion) "
                + "VALUES (?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        if (conexion == null || conexion.isClosed()) {
            throw new SQLException(
                    "No hay conexión disponible con la base de datos.");
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, asignacion.getIdOrden());
            ps.setInt(2, asignacion.getIdMecanico());
            ps.setBoolean(3, asignacion.isResponsable());

            if (asignacion.getFechaAsignacion() != null) {
                ps.setTimestamp(4,
                        Timestamp.valueOf(asignacion.getFechaAsignacion()));
            } else {
                ps.setTimestamp(4, Timestamp.valueOf(
                        java.time.LocalDateTime.now()));
            }

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<AsignacionMecanico> listarPorOrden(int idOrden)
            throws SQLException {

        List<AsignacionMecanico> lista = new ArrayList<>();

        String sql = "SELECT id_asignacion, id_orden, id_mecanico, "
                + "es_responsable, fecha_asignacion "
                + "FROM AsignacionesMecanicos "
                + "WHERE id_orden = ? "
                + "ORDER BY id_asignacion";

        Connection conexion = ConexionDB.getConexion();

        if (conexion == null || conexion.isClosed()) {
            throw new SQLException(
                    "No hay conexión disponible con la base de datos.");
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    AsignacionMecanico asignacion =
                            new AsignacionMecanico();

                    asignacion.setIdAsignacion(
                            rs.getInt("id_asignacion"));

                    asignacion.setIdOrden(
                            rs.getInt("id_orden"));

                    asignacion.setIdMecanico(
                            rs.getInt("id_mecanico"));

                    asignacion.setResponsable(
                            rs.getBoolean("es_responsable"));

                    Timestamp fecha =
                            rs.getTimestamp("fecha_asignacion");

                    if (fecha != null) {
                        asignacion.setFechaAsignacion(
                                fecha.toLocalDateTime());
                    }

                    lista.add(asignacion);
                }
            }
        }

        return lista;
    }

    @Override
    public boolean existeAsignacion(int idOrden, int idMecanico)
            throws SQLException {

        String sql = "SELECT COUNT(*) AS total "
                + "FROM AsignacionesMecanicos "
                + "WHERE id_orden = ? AND id_mecanico = ?";

        Connection conexion = ConexionDB.getConexion();

        if (conexion == null || conexion.isClosed()) {
            throw new SQLException(
                    "No hay conexión disponible con la base de datos.");
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);
            ps.setInt(2, idMecanico);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        }

        return false;
    }

    @Override
    public boolean existeResponsable(int idOrden)
            throws SQLException {

        String sql = "SELECT COUNT(*) AS total "
                + "FROM AsignacionesMecanicos "
                + "WHERE id_orden = ? AND es_responsable = 1";

        Connection conexion = ConexionDB.getConexion();

        if (conexion == null || conexion.isClosed()) {
            throw new SQLException(
                    "No hay conexión disponible con la base de datos.");
        }

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
