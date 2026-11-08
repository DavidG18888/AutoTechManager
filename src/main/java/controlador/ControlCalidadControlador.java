/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package controlador;

import modelo.ControlCalidad;
import servicio.ControlCalidadServicio;

import java.sql.SQLException;
import java.util.List;

public class ControlCalidadControlador {

    private final ControlCalidadServicio servicio;

    public ControlCalidadControlador() {
        servicio = new ControlCalidadServicio();
    }

    public boolean guardar(ControlCalidad control) throws SQLException {
        return servicio.guardar(control);
    }

    public boolean actualizar(ControlCalidad control)
            throws SQLException {
        return servicio.actualizar(control);
    }

    public List<ControlCalidad> listarPorOrden(int idOrden)
            throws SQLException {
        return servicio.listarPorOrden(idOrden);
    }

    public boolean tieneControlesPendientes(int idOrden)
            throws SQLException {
        return servicio.tieneControlesPendientes(idOrden);
    }
}
