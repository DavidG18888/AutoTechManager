/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.ServicioDAO;
import dao.impl.ServicioDAOImpl;
import modelo.Servicio;

import java.sql.SQLException;
import java.util.List;

public class ServicioServicio {

    private final ServicioDAO servicioDAO;

    public ServicioServicio() {
        this.servicioDAO = new ServicioDAOImpl();
    }

    public boolean guardar(Servicio servicio) throws SQLException {

        validarServicio(servicio);

        if (servicio.getEstado() == null
                || servicio.getEstado().isBlank()) {
            servicio.setEstado("ACTIVO");
        }

        return servicioDAO.guardar(servicio);
    }

    public boolean actualizar(Servicio servicio) throws SQLException {

        if (servicio == null || servicio.getIdServicio() <= 0) {
            throw new IllegalArgumentException(
                    "El servicio no es válido.");
        }

        validarServicio(servicio);

        return servicioDAO.actualizar(servicio);
    }

    public boolean cambiarEstado(int idServicio, String estado)
            throws SQLException {

        if (idServicio <= 0) {
            throw new IllegalArgumentException(
                    "ID de servicio inválido.");
        }

        validarEstado(estado);

        return servicioDAO.cambiarEstado(
                idServicio, estado.toUpperCase());
    }

    public Servicio buscarPorId(int idServicio) throws SQLException {

        if (idServicio <= 0) {
            throw new IllegalArgumentException(
                    "ID de servicio inválido.");
        }

        return servicioDAO.buscarPorId(idServicio);
    }

    public List<Servicio> listar() throws SQLException {
        return servicioDAO.listar();
    }

    private void validarServicio(Servicio servicio) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio es obligatorio.");
        }

        if (servicio.getCodigo() == null
                || servicio.getCodigo().isBlank()) {
            throw new IllegalArgumentException(
                    "Ingrese el código del servicio.");
        }

        if (servicio.getNombre() == null
                || servicio.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "Ingrese el nombre del servicio.");
        }

        if (servicio.getNivel() < 1
                || servicio.getNivel() > 5) {
            throw new IllegalArgumentException(
                    "El nivel debe estar entre 1 y 5.");
        }

        if (servicio.getCostoManoObra() < 0) {
            throw new IllegalArgumentException(
                    "El costo no puede ser negativo.");
        }

        if (servicio.getTiempoEstimado() < 0) {
            throw new IllegalArgumentException(
                    "El tiempo no puede ser negativo.");
        }
    }

    private void validarEstado(String estado) {

        if (estado == null
                || (!estado.equalsIgnoreCase("ACTIVO")
                && !estado.equalsIgnoreCase("INACTIVO"))) {
            throw new IllegalArgumentException(
                    "El estado debe ser ACTIVO o INACTIVO.");
        }
    }
}