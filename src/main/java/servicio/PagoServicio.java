/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.PagoDAO;
import dao.PresupuestoDAO;
import dao.impl.PagoDAOImpl;
import dao.impl.PresupuestoDAOImpl;
import modelo.Pago;
import modelo.Presupuesto;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PagoServicio {

    private final PagoDAO pagoDAO;
    private final PresupuestoDAO presupuestoDAO;

    public PagoServicio() {
        this.pagoDAO = new PagoDAOImpl();
        this.presupuestoDAO = new PresupuestoDAOImpl();
    }

    public boolean guardar(Pago pago) throws SQLException {

        if (pago == null) {
            throw new IllegalArgumentException("El pago es obligatorio.");
        }

        if (pago.getIdPresupuesto() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un presupuesto.");
        }

        if (pago.getMonto() <= 0) {
            throw new IllegalArgumentException(
                    "El monto debe ser mayor que cero.");
        }

        Presupuesto presupuesto =
                presupuestoDAO.buscarPorId(pago.getIdPresupuesto());

        if (presupuesto == null) {
            throw new IllegalArgumentException(
                    "El presupuesto no existe.");
        }

        if (!presupuesto.isAutorizado()) {
            throw new IllegalArgumentException(
                    "El presupuesto debe estar autorizado.");
        }

        double pagado = pagoDAO.calcularTotalPagado(
                pago.getIdPresupuesto());

        double saldo = presupuesto.getTotal() - pagado;

        if (pago.getMonto() > saldo) {
            throw new IllegalArgumentException(
                    "El pago supera el saldo pendiente: " + saldo);
        }

        if (pago.getMetodoPago() == null
                || pago.getMetodoPago().isBlank()) {
            throw new IllegalArgumentException(
                    "Seleccione el método de pago.");
        }

        if (pago.getFecha() == null) {
            pago.setFecha(LocalDateTime.now());
        }

        return pagoDAO.guardar(pago);
    }

    public List<Pago> listarPorPresupuesto(int idPresupuesto)
            throws SQLException {
        return pagoDAO.listarPorPresupuesto(idPresupuesto);
    }

    public double calcularTotalPagado(int idPresupuesto)
            throws SQLException {
        return pagoDAO.calcularTotalPagado(idPresupuesto);
    }

    public double calcularSaldoPendiente(int idPresupuesto)
            throws SQLException {

        Presupuesto presupuesto =
                presupuestoDAO.buscarPorId(idPresupuesto);

        if (presupuesto == null) {
            throw new IllegalArgumentException(
                    "El presupuesto no existe.");
        }

        double pagado = pagoDAO.calcularTotalPagado(idPresupuesto);

        return Math.max(0, presupuesto.getTotal() - pagado);
    }
}