/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.OrdenTrabajoDAO;
import dao.DetalleOrdenDAO;
import dao.ConsumoOrdenDAO;

import dao.impl.OrdenTrabajoDAOImpl;
import dao.impl.DetalleOrdenDAOImpl;
import dao.impl.ConsumoOrdenDAOImpl;

import modelo.OrdenTrabajo;
import modelo.DetalleOrden;
import modelo.ConsumoOrden;

import java.sql.SQLException;
import java.util.List;

public class HistorialServicio {

    private final OrdenTrabajoDAO ordenDAO;
    private final DetalleOrdenDAO detalleDAO;
    private final ConsumoOrdenDAO consumoDAO;

    public HistorialServicio() {
        this.ordenDAO = new OrdenTrabajoDAOImpl();
        this.detalleDAO = new DetalleOrdenDAOImpl();
        this.consumoDAO = new ConsumoOrdenDAOImpl();
    }

    public List<OrdenTrabajo> listarPorVehiculo(int idVehiculo)
            throws SQLException {

        if (idVehiculo <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un vehículo.");
        }

        return ordenDAO.listarPorVehiculo(idVehiculo);
    }

    public List<DetalleOrden> listarServiciosPorOrden(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException("Orden inválida.");
        }

        return detalleDAO.listarPorOrden(idOrden);
    }

    public List<ConsumoOrden> listarRepuestosPorOrden(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException("Orden inválida.");
        }

        return consumoDAO.listarPorOrden(idOrden);
    }
}