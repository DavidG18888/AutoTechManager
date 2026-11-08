/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.ConexionDB;
import dao.MecanicoDAO;
import modelo.Mecanico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MecanicoDAOImpl implements MecanicoDAO {

    @Override
    public boolean guardar(Mecanico mecanico) throws SQLException {

        String sql = "INSERT INTO Mecanicos "
                + "(nombre, telefono, especialidad, estado) "
                + "VALUES (?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, mecanico.getNombre());
            ps.setString(2, mecanico.getTelefono());
            ps.setString(3, mecanico.getEspecialidad());
            ps.setString(4, mecanico.getEstado());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizar(Mecanico mecanico) throws SQLException {

        String sql = "UPDATE Mecanicos SET nombre = ?, telefono = ?, "
                + "especialidad = ?, estado = ? WHERE id_mecanico = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, mecanico.getNombre());
            ps.setString(2, mecanico.getTelefono());
            ps.setString(3, mecanico.getEspecialidad());
            ps.setString(4, mecanico.getEstado());
            ps.setInt(5, mecanico.getIdMecanico());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cambiarEstado(int idMecanico, String estado)
            throws SQLException {

        String sql = "UPDATE Mecanicos SET estado = ? "
                + "WHERE id_mecanico = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idMecanico);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Mecanico buscarPorId(int idMecanico) throws SQLException {

        String sql = "SELECT * FROM Mecanicos WHERE id_mecanico = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idMecanico);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return obtenerMecanico(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Mecanico> listar() throws SQLException {

        List<Mecanico> lista = new ArrayList<>();

        String sql = "SELECT * FROM Mecanicos ORDER BY id_mecanico DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(obtenerMecanico(rs));
            }
        }

        return lista;
    }

    private Mecanico obtenerMecanico(ResultSet rs) throws SQLException {

        Mecanico mecanico = new Mecanico();

        mecanico.setIdMecanico(rs.getInt("id_mecanico"));
        mecanico.setNombre(rs.getString("nombre"));
        mecanico.setTelefono(rs.getString("telefono"));
        mecanico.setEspecialidad(rs.getString("especialidad"));
        mecanico.setEstado(rs.getString("estado"));

        return mecanico;
    }
}