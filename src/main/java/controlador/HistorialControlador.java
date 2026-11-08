/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.sql.SQLException;
import java.util.List;

import modelo.OrdenTrabajo;
import modelo.DetalleOrden;
import modelo.ConsumoOrden;
import servicio.HistorialServicio;

public class HistorialControlador {

    private final HistorialServicio servicio;

    public HistorialControlador() {
        servicio = new HistorialServicio();
    }

    public List<OrdenTrabajo> listarPorVehiculo(int idVehiculo)
            throws SQLException {

        return servicio.listarPorVehiculo(idVehiculo);
    }

    public List<DetalleOrden> listarServiciosPorOrden(int idOrden)
            throws SQLException {

        return servicio.listarServiciosPorOrden(idOrden);
    }

    public List<ConsumoOrden> listarRepuestosPorOrden(int idOrden)
            throws SQLException {

        return servicio.listarRepuestosPorOrden(idOrden);
    }
}