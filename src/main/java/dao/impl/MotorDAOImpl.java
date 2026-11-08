/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.MotorDAO;
import modelo.Motor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MotorDAOImpl implements MotorDAO {

    @Override
    public boolean guardar(Motor motor) throws SQLException {

        String sql = "INSERT INTO Motores "
                + "(numero_serie, marca, modelo, anio, tipo_motor, "
                + "cilindrada, compatible_marca, compatible_modelo, "
                + "compatible_anio, costo, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, motor.getNumeroSerie());
            ps.setString(2, motor.getMarca());
            ps.setString(3, motor.getModelo());
            ps.setInt(4, motor.getAnio());
            ps.setString(5, motor.getTipoMotor());
            ps.setDouble(6, motor.getCilindrada());
            ps.setString(7, motor.getCompatibleMarca());
            ps.setString(8, motor.getCompatibleModelo());
            ps.setInt(9, motor.getCompatibleAnio());
            ps.setDouble(10, motor.getCosto());
            ps.setString(11, motor.getEstado());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizar(Motor motor) throws SQLException {

        String sql = "UPDATE Motores SET numero_serie = ?, marca = ?, "
                + "modelo = ?, anio = ?, tipo_motor = ?, cilindrada = ?, "
                + "compatible_marca = ?, compatible_modelo = ?, "
                + "compatible_anio = ?, costo = ?, estado = ? "
                + "WHERE id_motor = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, motor.getNumeroSerie());
            ps.setString(2, motor.getMarca());
            ps.setString(3, motor.getModelo());
            ps.setInt(4, motor.getAnio());
            ps.setString(5, motor.getTipoMotor());
            ps.setDouble(6, motor.getCilindrada());
            ps.setString(7, motor.getCompatibleMarca());
            ps.setString(8, motor.getCompatibleModelo());
            ps.setInt(9, motor.getCompatibleAnio());
            ps.setDouble(10, motor.getCosto());
            ps.setString(11, motor.getEstado());
            ps.setInt(12, motor.getIdMotor());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizarEstado(int idMotor, String estado)
            throws SQLException {

        String sql = "UPDATE Motores SET estado = ? WHERE id_motor = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idMotor);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Motor buscarPorId(int idMotor) throws SQLException {

        String sql = "SELECT * FROM Motores WHERE id_motor = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idMotor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return obtenerMotor(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Motor> listar() throws SQLException {

        List<Motor> lista = new ArrayList<>();

        String sql = "SELECT * FROM Motores ORDER BY id_motor DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(obtenerMotor(rs));
            }
        }

        return lista;
    }

    @Override
    public List<Motor> listarDisponibles() throws SQLException {

        List<Motor> lista = new ArrayList<>();

        String sql = "SELECT * FROM Motores "
                + "WHERE estado = 'DISPONIBLE' "
                + "ORDER BY id_motor DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(obtenerMotor(rs));
            }
        }

        return lista;
    }

    private Motor obtenerMotor(ResultSet rs) throws SQLException {

        Motor motor = new Motor();

        motor.setIdMotor(rs.getInt("id_motor"));
        motor.setNumeroSerie(rs.getString("numero_serie"));
        motor.setMarca(rs.getString("marca"));
        motor.setModelo(rs.getString("modelo"));
        motor.setAnio(rs.getInt("anio"));
        motor.setTipoMotor(rs.getString("tipo_motor"));
        motor.setCilindrada(rs.getDouble("cilindrada"));
        motor.setCompatibleMarca(rs.getString("compatible_marca"));
        motor.setCompatibleModelo(rs.getString("compatible_modelo"));
        motor.setCompatibleAnio(rs.getInt("compatible_anio"));
        motor.setCosto(rs.getDouble("costo"));
        motor.setEstado(rs.getString("estado"));

        return motor;
    }
}