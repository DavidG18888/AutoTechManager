/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.ServicioRepuestoDAO;
import dao.impl.ServicioRepuestoDAOImpl;
import modelo.ServicioRepuesto;

import java.sql.SQLException;
import java.util.List;

public class ServicioRepuestoServicio {

    private final ServicioRepuestoDAO dao;

    public ServicioRepuestoServicio() {
        this.dao = new ServicioRepuestoDAOImpl();
    }

    public boolean guardar(ServicioRepuesto sr) throws SQLException {

        if (sr == null) {
            throw new IllegalArgumentException("La información es obligatoria.");
        }

        if (sr.getIdServicio() <= 0) {
            throw new IllegalArgumentException("Seleccione un servicio.");
        }

        if (sr.getIdRepuesto() <= 0) {
            throw new IllegalArgumentException("Seleccione un repuesto.");
        }

        if (sr.getCantidad() <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero.");
        }

        if (dao.existeRelacion(sr.getIdServicio(), sr.getIdRepuesto())) {
            throw new IllegalArgumentException(
                    "Este repuesto ya está asociado al servicio seleccionado.");
        }

        return dao.guardar(sr);
    }

    public List<ServicioRepuesto> listarPorServicio(int idServicio)
            throws SQLException {

        if (idServicio <= 0) {
            throw new IllegalArgumentException("Seleccione un servicio.");
        }

        return dao.listarPorServicio(idServicio);
    }
}