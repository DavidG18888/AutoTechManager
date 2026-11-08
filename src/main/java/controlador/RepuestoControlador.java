/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Repuesto;
import servicio.RepuestoServicio;

import java.sql.SQLException;
import java.util.List;

public class RepuestoControlador {

    private final RepuestoServicio servicio;

    public RepuestoControlador() {
        this.servicio = new RepuestoServicio();
    }

    public boolean guardar(Repuesto repuesto) throws SQLException {
        return servicio.guardar(repuesto);
    }

    public boolean actualizar(Repuesto repuesto) throws SQLException {
        return servicio.actualizar(repuesto);
    }

    public Repuesto buscarPorId(int idRepuesto) throws SQLException {
        return servicio.buscarPorId(idRepuesto);
    }

    public List<Repuesto> listar() throws SQLException {
        return servicio.listar();
    }

    public List<Repuesto> listarBajoStock() throws SQLException {
        return servicio.listarBajoStock();
    }
}