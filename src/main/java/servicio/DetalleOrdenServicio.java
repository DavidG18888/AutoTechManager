/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.DetalleOrdenDAO;
import dao.impl.DetalleOrdenDAOImpl;
import modelo.DetalleOrden;
import java.sql.SQLException;
import java.util.List;

public class DetalleOrdenServicio {

    private final DetalleOrdenDAO dao;

    public DetalleOrdenServicio() {
        dao = new DetalleOrdenDAOImpl();
    }

    public boolean guardar(DetalleOrden detalle) throws SQLException {

        if (detalle == null) {
            throw new IllegalArgumentException("Detalle inválido.");
        }

        if (detalle.getIdOrden() <= 0) {
            throw new IllegalArgumentException("Seleccione una orden.");
        }

        if (detalle.getIdServicio() <= 0) {
            throw new IllegalArgumentException("Seleccione un servicio.");
        }

        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        if (detalle.getCostoManoObra() < 0
                || detalle.getTiempoEstimado() < 0) {
            throw new IllegalArgumentException("Los valores no pueden ser negativos.");
        }

        double subtotal = detalle.getCantidad()
                * detalle.getCostoManoObra();

        detalle.setSubtotal(subtotal);

        return dao.guardar(detalle);
    }

    public List<DetalleOrden> listarPorOrden(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException("Seleccione una orden.");
        }

        return dao.listarPorOrden(idOrden);
    }
}