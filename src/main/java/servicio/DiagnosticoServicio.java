/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.DiagnosticoDAO;
import dao.impl.DiagnosticoDAOImpl;
import modelo.Diagnostico;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class DiagnosticoServicio {

    private final DiagnosticoDAO diagnosticoDAO;

    public DiagnosticoServicio() {
        this.diagnosticoDAO = new DiagnosticoDAOImpl();
    }

    public boolean guardar(Diagnostico diagnostico)
            throws SQLException {

        validarDiagnostico(diagnostico);

        if (diagnostico.getFecha() == null) {
            diagnostico.setFecha(LocalDateTime.now());
        }

        return diagnosticoDAO.guardar(diagnostico);
    }

    public Diagnostico buscarPorOrden(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione una orden de trabajo.");
        }

        return diagnosticoDAO.buscarPorOrden(idOrden);
    }

    public List<Diagnostico> listarPorOrden(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException(
                    "ID de orden inválido.");
        }

        return diagnosticoDAO.listarPorOrden(idOrden);
    }

    private void validarDiagnostico(Diagnostico diagnostico) {

        if (diagnostico == null) {
            throw new IllegalArgumentException(
                    "El diagnóstico es obligatorio.");
        }

        if (diagnostico.getIdOrden() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione una orden.");
        }

        if (diagnostico.getDescripcion() == null
                || diagnostico.getDescripcion().isBlank()) {
            throw new IllegalArgumentException(
                    "Ingrese la descripción del diagnóstico.");
        }

        if (diagnostico.getCausa() == null
                || diagnostico.getCausa().isBlank()) {
            throw new IllegalArgumentException(
                    "Ingrese la causa del problema.");
        }

        if (diagnostico.getRecomendaciones() == null
                || diagnostico.getRecomendaciones().isBlank()) {
            throw new IllegalArgumentException(
                    "Ingrese las recomendaciones.");
        }
    }
}