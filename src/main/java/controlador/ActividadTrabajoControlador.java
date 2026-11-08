/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controlador;

import modelo.ActividadTrabajo;
import servicio.ActividadTrabajoServicio;
import java.sql.SQLException;
import java.util.List;

public class ActividadTrabajoControlador {

    private final ActividadTrabajoServicio servicio;

    public ActividadTrabajoControlador() {
        servicio = new ActividadTrabajoServicio();
    }

    public boolean guardar(ActividadTrabajo actividad) throws SQLException {
        return servicio.guardar(actividad);
    }

    public boolean actualizarEstado(int idActividad, String estado)
            throws SQLException {
        return servicio.actualizarEstado(idActividad, estado);
    }

    public List<ActividadTrabajo> listarPorOrden(int idOrden)
            throws SQLException {
        return servicio.listarPorOrden(idOrden);
    }

    public boolean tienePendientes(int idOrden) throws SQLException {
        return servicio.tienePendientes(idOrden);
    }
}
