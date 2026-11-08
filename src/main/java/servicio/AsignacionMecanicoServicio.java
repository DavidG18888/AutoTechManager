/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package servicio;

import dao.AsignacionMecanicoDAO;
import dao.MecanicoDAO;
import dao.OrdenTrabajoDAO;

import dao.impl.AsignacionMecanicoDAOImpl;
import dao.impl.MecanicoDAOImpl;
import dao.impl.OrdenTrabajoDAOImpl;

import modelo.AsignacionMecanico;
import modelo.Mecanico;
import modelo.OrdenTrabajo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AsignacionMecanicoServicio {

    private final AsignacionMecanicoDAO asignacionDAO;
    private final MecanicoDAO mecanicoDAO;
    private final OrdenTrabajoDAO ordenDAO;

    public AsignacionMecanicoServicio() {

        this.asignacionDAO = new AsignacionMecanicoDAOImpl();
        this.mecanicoDAO = new MecanicoDAOImpl();
        this.ordenDAO = new OrdenTrabajoDAOImpl();
    }

    // Registrar una asignación de mecánico
    public boolean guardar(AsignacionMecanico asignacion)
            throws SQLException {

        if (asignacion == null) {
            throw new IllegalArgumentException(
                    "La asignación es obligatoria.");
        }

        if (asignacion.getIdOrden() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione una orden de trabajo.");
        }

        if (asignacion.getIdMecanico() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un mecánico.");
        }

        // Verificar que la orden exista
        OrdenTrabajo orden =
                ordenDAO.buscarPorId(asignacion.getIdOrden());

        if (orden == null) {
            throw new IllegalArgumentException(
                    "La orden de trabajo no existe.");
        }

        // Verificar que el mecánico exista
        Mecanico mecanico =
                mecanicoDAO.buscarPorId(asignacion.getIdMecanico());

        if (mecanico == null) {
            throw new IllegalArgumentException(
                    "El mecánico seleccionado no existe.");
        }

        // Verificar que el mecánico esté activo
        if (!"ACTIVO".equalsIgnoreCase(mecanico.getEstado())) {
            throw new IllegalArgumentException(
                    "No se puede asignar un mecánico inactivo.");
        }

        // Evitar asignaciones duplicadas
        if (asignacionDAO.existeAsignacion(
                asignacion.getIdOrden(),
                asignacion.getIdMecanico())) {

            throw new IllegalArgumentException(
                    "El mecánico ya está asignado a esta orden.");
        }

        // Verificar que no exista otro responsable
        if (asignacion.isResponsable()
                && asignacionDAO.existeResponsable(
                        asignacion.getIdOrden())) {

            throw new IllegalArgumentException(
                    "Esta orden ya tiene un mecánico responsable principal.");
        }

        // Asignar fecha automáticamente
        if (asignacion.getFechaAsignacion() == null) {
            asignacion.setFechaAsignacion(LocalDateTime.now());
        }

        return asignacionDAO.guardar(asignacion);
    }

    // Consultar mecánicos asignados a una orden
    public List<AsignacionMecanico> listarPorOrden(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione una orden.");
        }

        return asignacionDAO.listarPorOrden(idOrden);
    }

    // Verificar si un mecánico ya está asignado
    public boolean existeAsignacion(int idOrden, int idMecanico)
            throws SQLException {

        if (idOrden <= 0 || idMecanico <= 0) {
            throw new IllegalArgumentException(
                    "Los datos de la asignación no son válidos.");
        }

        return asignacionDAO.existeAsignacion(
                idOrden, idMecanico);
    }

    // Verificar si una orden ya tiene responsable
    public boolean existeResponsable(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione una orden válida.");
        }

        return asignacionDAO.existeResponsable(idOrden);
    }
}
