/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Presupuesto;
import servicio.PresupuestoServicio;

import java.sql.SQLException;
import java.util.List;

public class PresupuestoControlador {

    private final PresupuestoServicio servicio;

    public PresupuestoControlador() {
        servicio = new PresupuestoServicio();
    }

    public boolean guardar(Presupuesto p) throws SQLException {
        return servicio.guardar(p);
    }

    public boolean actualizar(Presupuesto p) throws SQLException {
        return servicio.actualizar(p);
    }

    public boolean autorizar(int id) throws SQLException {
        return servicio.autorizar(id);
    }

    public boolean rechazar(int id) throws SQLException {
        return servicio.rechazar(id);
    }

    public Presupuesto buscarPorId(int id) throws SQLException {
        return servicio.buscarPorId(id);
    }

    public Presupuesto buscarPorOrden(int idOrden) throws SQLException {
        return servicio.buscarPorOrden(idOrden);
    }

    public List<Presupuesto> listar() throws SQLException {
        return servicio.listar();
    }

    public double calcularCostoRepuestosPorOrden(int idOrden)
            throws SQLException {
        return servicio.calcularCostoRepuestosPorOrden(idOrden);
    }
}