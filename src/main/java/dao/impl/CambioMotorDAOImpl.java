/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.CambioMotorDAO;
import modelo.CambioMotor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CambioMotorDAOImpl implements CambioMotorDAO {

    @Override
    public boolean guardar(CambioMotor cambio) throws SQLException {

        String sql = "INSERT INTO CambiosMotor "
                + "(id_orden, id_motor_nuevo, numero_serie_anterior, "
                + "destino_motor_anterior, fecha_cambio, "
                + "autorizacion_cliente, pruebas_realizadas, "
                + "resultado_pruebas, garantia_meses) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, cambio.getIdOrden());
            ps.setInt(2, cambio.getIdMotorNuevo());
            ps.setString(3, cambio.getNumeroSerieAnterior());
            ps.setString(4, cambio.getDestinoMotorAnterior());
            ps.setTimestamp(5,
                    Timestamp.valueOf(cambio.getFechaCambio()));
            ps.setBoolean(6, cambio.isAutorizacionCliente());
            ps.setString(7, cambio.getPruebasRealizadas());
            ps.setString(8, cambio.getResultadoPruebas());
            ps.setInt(9, cambio.getGarantiaMeses());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public CambioMotor buscarPorOrden(int idOrden) throws SQLException {

        String sql = "SELECT TOP 1 * FROM CambiosMotor "
                + "WHERE id_orden = ? ORDER BY fecha_cambio DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    CambioMotor cambio = new CambioMotor();

                    cambio.setIdCambio(rs.getInt("id_cambio"));
                    cambio.setIdOrden(rs.getInt("id_orden"));
                    cambio.setIdMotorNuevo(rs.getInt("id_motor_nuevo"));
                    cambio.setNumeroSerieAnterior(
                            rs.getString("numero_serie_anterior"));
                    cambio.setDestinoMotorAnterior(
                            rs.getString("destino_motor_anterior"));

                    Timestamp fecha = rs.getTimestamp("fecha_cambio");

                    if (fecha != null) {
                        cambio.setFechaCambio(fecha.toLocalDateTime());
                    }

                    cambio.setAutorizacionCliente(
                            rs.getBoolean("autorizacion_cliente"));
                    cambio.setPruebasRealizadas(
                            rs.getString("pruebas_realizadas"));
                    cambio.setResultadoPruebas(
                            rs.getString("resultado_pruebas"));
                    cambio.setGarantiaMeses(rs.getInt("garantia_meses"));

                    return cambio;
                }
            }
        }

        return null;
    }
}