/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controlador;

import modelo.Proveedor;
import servicio.ProveedorServicio;
import java.sql.SQLException;
import java.util.List;

public class ProveedorControlador {

    private final ProveedorServicio servicio;

    public ProveedorControlador() {
        servicio = new ProveedorServicio();
    }

    public boolean guardar(Proveedor proveedor) throws SQLException {
        return servicio.guardar(proveedor);
    }

    public boolean actualizar(Proveedor proveedor) throws SQLException {
        return servicio.actualizar(proveedor);
    }

    public boolean cambiarEstado(int id, String estado) throws SQLException {
        return servicio.cambiarEstado(id, estado);
    }

    public Proveedor buscarPorId(int id) throws SQLException {
        return servicio.buscarPorId(id);
    }

    public List<Proveedor> listar() throws SQLException {
        return servicio.listar();
    }
}
