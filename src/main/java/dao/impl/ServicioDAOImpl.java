/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao.impl;

import config.ConexionDB;
import dao.ServicioDAO;
import modelo.Servicio;
import modelo.ServicioMantenimiento;
import modelo.ServicioReparacion;
import modelo.ServicioDiagnostico;
import modelo.ServicioCambioMotor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAOImpl implements ServicioDAO {

    @Override
    public boolean guardar(Servicio servicio) throws SQLException {

        String sql = "INSERT INTO Servicios "
                + "(codigo, nombre, descripcion, nivel, costo_mano_obra, "
                + "tiempo_estimado, estado, tipo_servicio) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, servicio.getCodigo());
            ps.setString(2, servicio.getNombre());
            ps.setString(3, servicio.getDescripcion());
            ps.setInt(4, servicio.getNivel());
            ps.setDouble(5, servicio.getCostoManoObra());
            ps.setDouble(6, servicio.getTiempoEstimado());
            ps.setString(7, servicio.getEstado());
            ps.setString(8, servicio.obtenerTipoServicio());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizar(Servicio servicio) throws SQLException {

        String sql = "UPDATE Servicios SET codigo = ?, nombre = ?, "
                + "descripcion = ?, nivel = ?, costo_mano_obra = ?, "
                + "tiempo_estimado = ?, estado = ?, tipo_servicio = ? "
                + "WHERE id_servicio = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, servicio.getCodigo());
            ps.setString(2, servicio.getNombre());
            ps.setString(3, servicio.getDescripcion());
            ps.setInt(4, servicio.getNivel());
            ps.setDouble(5, servicio.getCostoManoObra());
            ps.setDouble(6, servicio.getTiempoEstimado());
            ps.setString(7, servicio.getEstado());
            ps.setString(8, servicio.obtenerTipoServicio());
            ps.setInt(9, servicio.getIdServicio());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cambiarEstado(int idServicio, String estado)
            throws SQLException {

        String sql = "UPDATE Servicios SET estado = ? "
                + "WHERE id_servicio = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idServicio);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Servicio buscarPorId(int idServicio) throws SQLException {

        String sql = "SELECT * FROM Servicios WHERE id_servicio = ?";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idServicio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return obtenerServicio(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Servicio> listar() throws SQLException {

        List<Servicio> lista = new ArrayList<>();

        String sql = "SELECT * FROM Servicios ORDER BY id_servicio DESC";

        Connection conexion = ConexionDB.getConexion();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(obtenerServicio(rs));
            }
        }

        return lista;
    }

    private Servicio obtenerServicio(ResultSet rs) throws SQLException {

        String tipo = rs.getString("tipo_servicio");

        Servicio servicio;

        if (tipo == null) {
            tipo = "GENERAL";
        }

        switch (tipo.toUpperCase()) {

            case "MANTENIMIENTO":
                servicio = new ServicioMantenimiento();
                break;

            case "REPARACION":
                servicio = new ServicioReparacion();
                break;

            case "DIAGNOSTICO":
                servicio = new ServicioDiagnostico();
                break;

            case "CAMBIO_MOTOR":
                servicio = new ServicioCambioMotor();
                break;

            default:
                servicio = new Servicio();
                break;
        }

        servicio.setIdServicio(rs.getInt("id_servicio"));
        servicio.setCodigo(rs.getString("codigo"));
        servicio.setNombre(rs.getString("nombre"));
        servicio.setDescripcion(rs.getString("descripcion"));
        servicio.setNivel(rs.getInt("nivel"));
        servicio.setCostoManoObra(rs.getDouble("costo_mano_obra"));
        servicio.setTiempoEstimado(rs.getDouble("tiempo_estimado"));
        servicio.setEstado(rs.getString("estado"));

        return servicio;
    }
}
