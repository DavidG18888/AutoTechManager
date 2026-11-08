/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.OrdenTrabajo;
import servicio.OrdenTrabajoServicio;
import java.sql.SQLException;
import java.util.List;

public class OrdenTrabajoControlador {

    private final OrdenTrabajoServicio servicio;

    public OrdenTrabajoControlador() {
        servicio = new OrdenTrabajoServicio();
    }

    public int guardar(OrdenTrabajo orden) throws SQLException {
        return servicio.guardar(orden);
    }

    public boolean actualizarEstado(int idOrden, String estado) throws SQLException {
        return servicio.actualizarEstado(idOrden, estado);
    }

    public OrdenTrabajo buscarPorId(int idOrden) throws SQLException {
        return servicio.buscarPorId(idOrden);
    }

    public List<OrdenTrabajo> listar() throws SQLException {
        return servicio.listar();
    }

    public List<OrdenTrabajo> listarPorVehiculo(int idVehiculo) throws SQLException {
        return servicio.listarPorVehiculo(idVehiculo);
    }
}