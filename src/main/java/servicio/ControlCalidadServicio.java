/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package servicio;

import dao.ControlCalidadDAO;
import dao.impl.ControlCalidadDAOImpl;
import modelo.ControlCalidad;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ControlCalidadServicio {

    private final ControlCalidadDAO controlDAO;

    public ControlCalidadServicio() {
        this.controlDAO = new ControlCalidadDAOImpl();
    }

    public boolean guardar(ControlCalidad control) throws SQLException {

        validar(control);

        if (control.getFecha() == null) {
            control.setFecha(LocalDateTime.now());
        }

        return controlDAO.guardar(control);
    }

    public boolean actualizar(ControlCalidad control)
            throws SQLException {

        validar(control);

        if (control.getIdControl() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un control de calidad.");
        }

        control.setFecha(LocalDateTime.now());

        return controlDAO.actualizar(control);
    }

    public List<ControlCalidad> listarPorOrden(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException("Orden inválida.");
        }

        return controlDAO.listarPorOrden(idOrden);
    }

    public boolean tieneControlesPendientes(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException("Orden inválida.");
        }

        return controlDAO.tieneControlesPendientes(idOrden);
    }

    private void validar(ControlCalidad control) {

        if (control == null) {
            throw new IllegalArgumentException(
                    "El control de calidad es obligatorio.");
        }

        if (control.getIdOrden() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione una orden.");
        }

        if (control.getIdMecanico() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione el mecánico responsable.");
        }

        if (control.getDescripcion() == null
                || control.getDescripcion().isBlank()) {
            throw new IllegalArgumentException(
                    "Describa la revisión realizada.");
        }

        if (control.getResultado() == null
                || control.getResultado().isBlank()) {
            throw new IllegalArgumentException(
                    "Seleccione el resultado.");
        }

        if (!control.getResultado().equals("PENDIENTE")
                && !control.getResultado().equals("APROBADO")
                && !control.getResultado().equals("RECHAZADO")) {

            throw new IllegalArgumentException(
                    "El resultado seleccionado no es válido.");
        }
    }
}
