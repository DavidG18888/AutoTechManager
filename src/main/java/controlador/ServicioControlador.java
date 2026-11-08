/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Servicio;
import servicio.ServicioServicio;
import java.sql.SQLException;
import java.util.List;

public class ServicioControlador {

    private final ServicioServicio servicio;

    public ServicioControlador() {
        servicio = new ServicioServicio();
    }

    public boolean guardar(Servicio s) throws SQLException {
        return servicio.guardar(s);
    }

    public boolean actualizar(Servicio s) throws SQLException {
        return servicio.actualizar(s);
    }

    public boolean cambiarEstado(int id, String estado) throws SQLException {
        return servicio.cambiarEstado(id, estado);
    }

    public Servicio buscarPorId(int id) throws SQLException {
        return servicio.buscarPorId(id);
    }

    public List<Servicio> listar() throws SQLException {
        return servicio.listar();
    }
}