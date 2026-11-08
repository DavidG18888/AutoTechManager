/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.DetalleOrden;
import servicio.DetalleOrdenServicio;
import java.sql.SQLException;
import java.util.List;

public class DetalleOrdenControlador {

    private final DetalleOrdenServicio servicio;

    public DetalleOrdenControlador() {
        servicio = new DetalleOrdenServicio();
    }

    public boolean guardar(DetalleOrden detalle) throws SQLException {
        return servicio.guardar(detalle);
    }

    public List<DetalleOrden> listarPorOrden(int idOrden)
            throws SQLException {
        return servicio.listarPorOrden(idOrden);
    }
}