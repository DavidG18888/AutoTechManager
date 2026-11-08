/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.DiagnosticoDAO;
import modelo.Diagnostico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DiagnosticoDAOImpl implements DiagnosticoDAO {

    @Override
    public boolean guardar(Diagnostico diagnostico) throws SQLException {

        String sql = "INSERT INTO Diagnosticos "
                + "(id_orden, fecha, descripcion, causa, "
                + "recomendaciones, requiere_cambio_motor, evidencias_tecnicas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, diagnostico.getIdOrden());
            ps.setTimestamp(2,
                    Timestamp.valueOf(diagnostico.getFecha()));
            ps.setString(3, diagnostico.getDescripcion());
            ps.setString(4, diagnostico.getCausa());
            ps.setString(5, diagnostico.getRecomendaciones());
            ps.setBoolean(6, diagnostico.isRequiereCambioMotor());
            ps.setString(7, diagnostico.getEvidenciasTecnicas());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Diagnostico buscarPorOrden(int idOrden) throws SQLException {

        String sql = "SELECT TOP 1 * FROM Diagnosticos "
                + "WHERE id_orden = ? ORDER BY fecha DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return obtenerDiagnostico(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Diagnostico> listarPorOrden(int idOrden)
            throws SQLException {

        List<Diagnostico> lista = new ArrayList<>();

        String sql = "SELECT * FROM Diagnosticos "
                + "WHERE id_orden = ? ORDER BY fecha DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(obtenerDiagnostico(rs));
                }
            }
        }

        return lista;
    }

    private Diagnostico obtenerDiagnostico(ResultSet rs)
            throws SQLException {

        Diagnostico diagnostico = new Diagnostico();

        diagnostico.setIdDiagnostico(rs.getInt("id_diagnostico"));
        diagnostico.setIdOrden(rs.getInt("id_orden"));

        Timestamp fecha = rs.getTimestamp("fecha");

        if (fecha != null) {
            diagnostico.setFecha(fecha.toLocalDateTime());
        }

        diagnostico.setDescripcion(rs.getString("descripcion"));
        diagnostico.setCausa(rs.getString("causa"));
        diagnostico.setRecomendaciones(rs.getString("recomendaciones"));
        diagnostico.setRequiereCambioMotor(
                rs.getBoolean("requiere_cambio_motor"));

        diagnostico.setEvidenciasTecnicas(
                rs.getString("evidencias_tecnicas"));

        return diagnostico;
    }
}