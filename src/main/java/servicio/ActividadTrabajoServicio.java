/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.ActividadTrabajoDAO;
import dao.impl.ActividadTrabajoDAOImpl;
import modelo.ActividadTrabajo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ActividadTrabajoServicio {

    private final ActividadTrabajoDAO actividadDAO;

    public ActividadTrabajoServicio() {
        this.actividadDAO = new ActividadTrabajoDAOImpl();
    }

    public boolean guardar(ActividadTrabajo actividad)
            throws SQLException {

        validarActividad(actividad);

        if (actividad.getFechaInicio() == null) {
            actividad.setFechaInicio(LocalDateTime.now());
        }

        if (actividad.getEstado() == null
                || actividad.getEstado().isBlank()) {
            actividad.setEstado("PENDIENTE");
        }

        return actividadDAO.guardar(actividad);
    }

    public boolean actualizarEstado(int idActividad, String estado)
            throws SQLException {

        if (idActividad <= 0) {
            throw new IllegalArgumentException(
                    "ID de actividad inválido.");
        }

        if (!estadoValido(estado)) {
            throw new IllegalArgumentException(
                    "El estado de la actividad no es válido.");
        }

        return actividadDAO.actualizarEstado(
                idActividad, estado.toUpperCase());
    }

    public List<ActividadTrabajo> listarPorOrden(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione una orden.");
        }

        return actividadDAO.listarPorOrden(idOrden);
    }

    public boolean tienePendientes(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException(
                    "ID de orden inválido.");
        }

        return actividadDAO.tienePendientes(idOrden);
    }

    private void validarActividad(ActividadTrabajo actividad) {

        if (actividad == null) {
            throw new IllegalArgumentException(
                    "La actividad es obligatoria.");
        }

        if (actividad.getIdOrden() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione una orden de trabajo.");
        }

        if (actividad.getIdMecanico() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un mecánico.");
        }

        if (actividad.getDescripcion() == null
                || actividad.getDescripcion().isBlank()) {
            throw new IllegalArgumentException(
                    "Describa la actividad.");
        }
    }

    private boolean estadoValido(String estado) {

        if (estado == null) {
            return false;
        }

        return switch (estado.toUpperCase()) {
            case "PENDIENTE", "EN_PROCESO", "COMPLETADA" -> true;
            default -> false;
        };
    }
}