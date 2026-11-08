/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Cliente;
import servicio.ClienteServicio;
import java.sql.SQLException;
import java.util.List;

public class ClienteControlador {

    private final ClienteServicio servicio;

    public ClienteControlador() {
        servicio = new ClienteServicio();
    }

    public boolean guardar(Cliente cliente) throws SQLException {
        return servicio.guardar(cliente);
    }

    public boolean actualizar(Cliente cliente) throws SQLException {
        return servicio.actualizar(cliente);
    }

    public boolean cambiarEstado(int idCliente, String estado)
            throws SQLException {
        return servicio.cambiarEstado(idCliente, estado);
    }

    public Cliente buscarPorId(int idCliente) throws SQLException {
        return servicio.buscarPorId(idCliente);
    }

    public List<Cliente> listar() throws SQLException {
        return servicio.listar();
    }
}