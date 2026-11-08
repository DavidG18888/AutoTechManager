/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Pago;
import servicio.PagoServicio;
import java.sql.SQLException;
import java.util.List;

public class PagoControlador {

    private final PagoServicio servicio = new PagoServicio();

    public boolean guardar(Pago pago) throws SQLException {
        return servicio.guardar(pago);
    }

    public List<Pago> listarPorPresupuesto(int idPresupuesto)
            throws SQLException {
        return servicio.listarPorPresupuesto(idPresupuesto);
    }

    public double calcularTotalPagado(int idPresupuesto)
            throws SQLException {
        return servicio.calcularTotalPagado(idPresupuesto);
    }

    public double calcularSaldoPendiente(int idPresupuesto)
            throws SQLException {
        return servicio.calcularSaldoPendiente(idPresupuesto);
    }
}