/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Mecanico;
import servicio.MecanicoServicio;
import java.sql.SQLException;
import java.util.List;

public class MecanicoControlador {

    private final MecanicoServicio servicio;

    public MecanicoControlador() {
        servicio = new MecanicoServicio();
    }

    public boolean guardar(Mecanico mecanico) throws SQLException {
        return servicio.guardar(mecanico);
    }

    public boolean actualizar(Mecanico mecanico) throws SQLException {
        return servicio.actualizar(mecanico);
    }

    public boolean cambiarEstado(int id, String estado) throws SQLException {
        return servicio.cambiarEstado(id, estado);
    }

    public Mecanico buscarPorId(int id) throws SQLException {
        return servicio.buscarPorId(id);
    }

    public List<Mecanico> listar() throws SQLException {
        return servicio.listar();
    }
}