/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.ProveedorDAO;
import dao.impl.ProveedorDAOImpl;
import modelo.Proveedor;

import java.sql.SQLException;
import java.util.List;

public class ProveedorServicio {

    private final ProveedorDAO proveedorDAO;

    public ProveedorServicio() {
        this.proveedorDAO = new ProveedorDAOImpl();
    }

    public boolean guardar(Proveedor proveedor) throws SQLException {

        validarProveedor(proveedor);

        if (proveedor.getEstado() == null
                || proveedor.getEstado().isBlank()) {
            proveedor.setEstado("ACTIVO");
        }

        return proveedorDAO.guardar(proveedor);
    }

    public boolean actualizar(Proveedor proveedor) throws SQLException {

        if (proveedor == null || proveedor.getIdProveedor() <= 0) {
            throw new IllegalArgumentException(
                    "Proveedor inválido.");
        }

        validarProveedor(proveedor);

        return proveedorDAO.actualizar(proveedor);
    }

    public boolean cambiarEstado(int idProveedor, String estado)
            throws SQLException {

        if (idProveedor <= 0) {
            throw new IllegalArgumentException(
                    "ID de proveedor inválido.");
        }

        if (estado == null
                || (!estado.equalsIgnoreCase("ACTIVO")
                && !estado.equalsIgnoreCase("INACTIVO"))) {
            throw new IllegalArgumentException(
                    "Estado de proveedor no válido.");
        }

        return proveedorDAO.cambiarEstado(
                idProveedor, estado.toUpperCase());
    }

    public Proveedor buscarPorId(int idProveedor)
            throws SQLException {

        if (idProveedor <= 0) {
            throw new IllegalArgumentException(
                    "ID de proveedor inválido.");
        }

        return proveedorDAO.buscarPorId(idProveedor);
    }

    public List<Proveedor> listar() throws SQLException {
        return proveedorDAO.listar();
    }

    private void validarProveedor(Proveedor proveedor) {

        if (proveedor == null) {
            throw new IllegalArgumentException(
                    "El proveedor es obligatorio.");
        }

        if (proveedor.getNombre() == null
                || proveedor.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "Ingrese el nombre del proveedor.");
        }

        if (proveedor.getTelefono() == null
                || proveedor.getTelefono().isBlank()) {
            throw new IllegalArgumentException(
                    "Ingrese el teléfono del proveedor.");
        }
    }
}