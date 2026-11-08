/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.ServicioRepuesto;
import servicio.ServicioRepuestoServicio;

import java.sql.SQLException;
import java.util.List;

public class ServicioRepuestoControlador {

    private final ServicioRepuestoServicio servicio;

    public ServicioRepuestoControlador() {
        this.servicio = new ServicioRepuestoServicio();
    }

    public boolean guardar(ServicioRepuesto sr) throws SQLException {
        return servicio.guardar(sr);
    }

    public List<ServicioRepuesto> listarPorServicio(int idServicio)
            throws SQLException {
        return servicio.listarPorServicio(idServicio);
    }
}