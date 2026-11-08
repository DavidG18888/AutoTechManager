/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.RepuestoDAO;
import dao.impl.RepuestoDAOImpl;
import modelo.Repuesto;

import java.sql.SQLException;
import java.util.List;

public class RepuestoServicio {

    private final RepuestoDAO dao;

    public RepuestoServicio() {
        this.dao = new RepuestoDAOImpl();
    }

    public boolean guardar(Repuesto repuesto) throws SQLException {

        if (repuesto == null) {
            throw new IllegalArgumentException(
                    "Los datos del repuesto son obligatorios.");
        }

        if (repuesto.getCodigo() == null
                || repuesto.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Ingrese el código del repuesto.");
        }

        if (repuesto.getNombre() == null
                || repuesto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Ingrese el nombre del repuesto.");
        }

        if (repuesto.getExistencia() < 0
                || repuesto.getStockMinimo() < 0
                || repuesto.getCosto() < 0) {
            throw new IllegalArgumentException(
                    "La existencia, el stock mínimo y el costo no pueden ser negativos.");
        }

        if (repuesto.getIdProveedor() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un proveedor.");
        }

        return dao.guardar(repuesto);
    }

    public boolean actualizar(Repuesto repuesto) throws SQLException {

        if (repuesto == null || repuesto.getIdRepuesto() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un repuesto válido.");
        }

        if (repuesto.getNombre() == null
                || repuesto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del repuesto es obligatorio.");
        }

        if (repuesto.getExistencia() < 0
                || repuesto.getStockMinimo() < 0
                || repuesto.getCosto() < 0) {
            throw new IllegalArgumentException(
                    "Los valores numéricos no pueden ser negativos.");
        }

        return dao.actualizar(repuesto);
    }

    public Repuesto buscarPorId(int idRepuesto) throws SQLException {

        if (idRepuesto <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un repuesto válido.");
        }

        return dao.buscarPorId(idRepuesto);
    }

    public List<Repuesto> listar() throws SQLException {
        return dao.listar();
    }

    public List<Repuesto> listarBajoStock() throws SQLException {
        return dao.listarBajoStock();
    }
}