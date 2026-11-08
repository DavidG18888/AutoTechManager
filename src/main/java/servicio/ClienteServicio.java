/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.ClienteDAO;
import dao.impl.ClienteDAOImpl;
import modelo.Cliente;

import java.sql.SQLException;
import java.util.List;

public class ClienteServicio {

    private final ClienteDAO clienteDAO;

    public ClienteServicio() {
        this.clienteDAO = new ClienteDAOImpl();
    }

    public boolean guardar(Cliente cliente) throws SQLException {

        validarCliente(cliente);

        if (cliente.getEstado() == null
                || cliente.getEstado().isBlank()) {
            cliente.setEstado("ACTIVO");
        }

        return clienteDAO.guardar(cliente);
    }

    public boolean actualizar(Cliente cliente) throws SQLException {

        if (cliente.getIdCliente() <= 0) {
            throw new IllegalArgumentException("ID de cliente inválido.");
        }

        validarCliente(cliente);

        return clienteDAO.actualizar(cliente);
    }

    public boolean cambiarEstado(int idCliente, String estado)
            throws SQLException {

        if (idCliente <= 0) {
            throw new IllegalArgumentException("ID de cliente inválido.");
        }

        if (!estado.equalsIgnoreCase("ACTIVO")
                && !estado.equalsIgnoreCase("INACTIVO")) {
            throw new IllegalArgumentException("Estado no válido.");
        }

        return clienteDAO.cambiarEstado(idCliente, estado.toUpperCase());
    }

    public Cliente buscarPorId(int idCliente) throws SQLException {
        return clienteDAO.buscarPorId(idCliente);
    }

    public List<Cliente> listar() throws SQLException {
        return clienteDAO.listar();
    }

    private void validarCliente(Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio.");
        }

        if (cliente.getNombre() == null
                || cliente.getNombre().isBlank()) {
            throw new IllegalArgumentException("Ingrese el nombre.");
        }

        if (cliente.getDni() == null
                || cliente.getDni().isBlank()) {
            throw new IllegalArgumentException("Ingrese el DNI.");
        }

        if (cliente.getTelefono() == null
                || cliente.getTelefono().isBlank()) {
            throw new IllegalArgumentException("Ingrese el teléfono.");
        }
    }
}