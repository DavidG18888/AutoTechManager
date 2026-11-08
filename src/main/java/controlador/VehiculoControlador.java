/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Vehiculo;
import servicio.VehiculoServicio;
import java.sql.SQLException;
import java.util.List;

public class VehiculoControlador {

    private final VehiculoServicio servicio;

    public VehiculoControlador() {
        servicio = new VehiculoServicio();
    }

    public boolean guardar(Vehiculo vehiculo) throws SQLException {
        return servicio.guardar(vehiculo);
    }

    public boolean actualizar(Vehiculo vehiculo) throws SQLException {
        return servicio.actualizar(vehiculo);
    }

    public Vehiculo buscarPorId(int id) throws SQLException {
        return servicio.buscarPorId(id);
    }

    public List<Vehiculo> listar() throws SQLException {
        return servicio.listar();
    }

    public List<Vehiculo> listarPorCliente(int idCliente)
            throws SQLException {
        return servicio.listarPorCliente(idCliente);
    }
}