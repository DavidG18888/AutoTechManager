/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.MecanicoDAO;
import dao.impl.MecanicoDAOImpl;
import modelo.Mecanico;

import java.sql.SQLException;
import java.util.List;

public class MecanicoServicio {

    private final MecanicoDAO mecanicoDAO;

    public MecanicoServicio() {
        this.mecanicoDAO = new MecanicoDAOImpl();
    }

    public boolean guardar(Mecanico mecanico) throws SQLException {

        validarMecanico(mecanico);

        if (mecanico.getEstado() == null
                || mecanico.getEstado().isBlank()) {
            mecanico.setEstado("ACTIVO");
        }

        return mecanicoDAO.guardar(mecanico);
    }

    public boolean actualizar(Mecanico mecanico) throws SQLException {

        if (mecanico.getIdMecanico() <= 0) {
            throw new IllegalArgumentException("ID de mecánico inválido.");
        }

        validarMecanico(mecanico);

        return mecanicoDAO.actualizar(mecanico);
    }

    public boolean cambiarEstado(int idMecanico, String estado)
            throws SQLException {

        if (idMecanico <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        if (!estado.equalsIgnoreCase("ACTIVO")
                && !estado.equalsIgnoreCase("INACTIVO")) {
            throw new IllegalArgumentException("Estado no válido.");
        }

        return mecanicoDAO.cambiarEstado(idMecanico, estado.toUpperCase());
    }

    public Mecanico buscarPorId(int idMecanico) throws SQLException {
        return mecanicoDAO.buscarPorId(idMecanico);
    }

    public List<Mecanico> listar() throws SQLException {
        return mecanicoDAO.listar();
    }

    private void validarMecanico(Mecanico mecanico) {

        if (mecanico == null) {
            throw new IllegalArgumentException("El mecánico es obligatorio.");
        }

        if (mecanico.getNombre() == null
                || mecanico.getNombre().isBlank()) {
            throw new IllegalArgumentException("Ingrese el nombre.");
        }

        if (mecanico.getEspecialidad() == null
                || mecanico.getEspecialidad().isBlank()) {
            throw new IllegalArgumentException("Ingrese la especialidad.");
        }
    }
}