/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.OrdenTrabajoDAO;
import modelo.OrdenTrabajo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrdenTrabajoDAOImpl implements OrdenTrabajoDAO {

    @Override
    public int guardar(OrdenTrabajo orden) throws SQLException {

        String sql = "INSERT INTO OrdenesTrabajo "
                + "(id_cliente, id_vehiculo, id_mecanico_responsable, "
                + "fecha, kilometraje, sintomas, diagnostico_preliminar, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(
                sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, orden.getIdCliente());
            ps.setInt(2, orden.getIdVehiculo());

            if (orden.getIdMecanicoResponsable() > 0) {
                ps.setInt(3, orden.getIdMecanicoResponsable());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            ps.setTimestamp(4, Timestamp.valueOf(orden.getFecha()));
            ps.setDouble(5, orden.getKilometraje());
            ps.setString(6, orden.getSintomas());
            ps.setString(7, orden.getDiagnosticoPreliminar());
            ps.setString(8, orden.getEstado());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public boolean actualizar(OrdenTrabajo orden) throws SQLException {

        String sql = "UPDATE OrdenesTrabajo SET id_cliente = ?, "
                + "id_vehiculo = ?, id_mecanico_responsable = ?, "
                + "fecha = ?, kilometraje = ?, sintomas = ?, "
                + "diagnostico_preliminar = ?, estado = ? "
                + "WHERE id_orden = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, orden.getIdCliente());
            ps.setInt(2, orden.getIdVehiculo());

            if (orden.getIdMecanicoResponsable() > 0) {
                ps.setInt(3, orden.getIdMecanicoResponsable());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            ps.setTimestamp(4, Timestamp.valueOf(orden.getFecha()));
            ps.setDouble(5, orden.getKilometraje());
            ps.setString(6, orden.getSintomas());
            ps.setString(7, orden.getDiagnosticoPreliminar());
            ps.setString(8, orden.getEstado());
            ps.setInt(9, orden.getIdOrden());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizarEstado(int idOrden, String estado)
            throws SQLException {

        String sql = "UPDATE OrdenesTrabajo SET estado = ? "
                + "WHERE id_orden = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idOrden);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public OrdenTrabajo buscarPorId(int idOrden) throws SQLException {

        String sql = "SELECT * FROM OrdenesTrabajo WHERE id_orden = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return obtenerOrden(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<OrdenTrabajo> listar() throws SQLException {

        List<OrdenTrabajo> lista = new ArrayList<>();

        String sql = "SELECT * FROM OrdenesTrabajo ORDER BY id_orden DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(obtenerOrden(rs));
            }
        }

        return lista;
    }

    @Override
    public List<OrdenTrabajo> listarPorVehiculo(int idVehiculo)
            throws SQLException {

        List<OrdenTrabajo> lista = new ArrayList<>();

        String sql = "SELECT * FROM OrdenesTrabajo "
                + "WHERE id_vehiculo = ? ORDER BY fecha DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idVehiculo);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(obtenerOrden(rs));
                }
            }
        }

        return lista;
    }

    private OrdenTrabajo obtenerOrden(ResultSet rs) throws SQLException {

        OrdenTrabajo orden = new OrdenTrabajo();

        orden.setIdOrden(rs.getInt("id_orden"));
        orden.setIdCliente(rs.getInt("id_cliente"));
        orden.setIdVehiculo(rs.getInt("id_vehiculo"));
        orden.setIdMecanicoResponsable(
                rs.getInt("id_mecanico_responsable"));

        if (rs.wasNull()) {
            orden.setIdMecanicoResponsable(0);
        }

        Timestamp fecha = rs.getTimestamp("fecha");

        if (fecha != null) {
            orden.setFecha(fecha.toLocalDateTime());
        }

        orden.setKilometraje(rs.getDouble("kilometraje"));
        orden.setSintomas(rs.getString("sintomas"));
        orden.setDiagnosticoPreliminar(
                rs.getString("diagnostico_preliminar"));
        orden.setEstado(rs.getString("estado"));

        return orden;
    }
}