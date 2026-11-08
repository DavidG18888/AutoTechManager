/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.VehiculoDAO;
import modelo.Vehiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAOImpl implements VehiculoDAO {

    @Override
    public boolean guardar(Vehiculo vehiculo) throws SQLException {

        String sql = "INSERT INTO Vehiculos "
                + "(id_cliente, placa, vin, marca, modelo, anio, "
                + "color, kilometraje, combustible) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, vehiculo.getIdCliente());
            ps.setString(2, vehiculo.getPlaca());
            ps.setString(3, vehiculo.getVin());
            ps.setString(4, vehiculo.getMarca());
            ps.setString(5, vehiculo.getModelo());
            ps.setInt(6, vehiculo.getAnio());
            ps.setString(7, vehiculo.getColor());
            ps.setDouble(8, vehiculo.getKilometraje());
            ps.setString(9, vehiculo.getCombustible());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizar(Vehiculo vehiculo) throws SQLException {

        String sql = "UPDATE Vehiculos SET id_cliente = ?, placa = ?, "
                + "vin = ?, marca = ?, modelo = ?, anio = ?, "
                + "color = ?, kilometraje = ?, combustible = ? "
                + "WHERE id_vehiculo = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, vehiculo.getIdCliente());
            ps.setString(2, vehiculo.getPlaca());
            ps.setString(3, vehiculo.getVin());
            ps.setString(4, vehiculo.getMarca());
            ps.setString(5, vehiculo.getModelo());
            ps.setInt(6, vehiculo.getAnio());
            ps.setString(7, vehiculo.getColor());
            ps.setDouble(8, vehiculo.getKilometraje());
            ps.setString(9, vehiculo.getCombustible());
            ps.setInt(10, vehiculo.getIdVehiculo());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Vehiculo buscarPorId(int idVehiculo) throws SQLException {

        String sql = "SELECT * FROM Vehiculos WHERE id_vehiculo = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idVehiculo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return obtenerVehiculo(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Vehiculo> listar() throws SQLException {

        List<Vehiculo> lista = new ArrayList<>();

        String sql = "SELECT * FROM Vehiculos ORDER BY id_vehiculo DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(obtenerVehiculo(rs));
            }
        }

        return lista;
    }

    @Override
    public List<Vehiculo> listarPorCliente(int idCliente)
            throws SQLException {

        List<Vehiculo> lista = new ArrayList<>();

        String sql = "SELECT * FROM Vehiculos WHERE id_cliente = ? "
                + "ORDER BY id_vehiculo DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(obtenerVehiculo(rs));
                }
            }
        }

        return lista;
    }

    private Vehiculo obtenerVehiculo(ResultSet rs) throws SQLException {

        Vehiculo vehiculo = new Vehiculo();

        vehiculo.setIdVehiculo(rs.getInt("id_vehiculo"));
        vehiculo.setIdCliente(rs.getInt("id_cliente"));
        vehiculo.setPlaca(rs.getString("placa"));
        vehiculo.setVin(rs.getString("vin"));
        vehiculo.setMarca(rs.getString("marca"));
        vehiculo.setModelo(rs.getString("modelo"));
        vehiculo.setAnio(rs.getInt("anio"));
        vehiculo.setColor(rs.getString("color"));
        vehiculo.setKilometraje(rs.getDouble("kilometraje"));
        vehiculo.setCombustible(rs.getString("combustible"));

        return vehiculo;
    }
}