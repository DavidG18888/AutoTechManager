/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package servicio;

import dao.PresupuestoDAO;
import dao.DetalleOrdenDAO;
import dao.ServicioRepuestoDAO;
import dao.RepuestoDAO;

import dao.impl.PresupuestoDAOImpl;
import dao.impl.DetalleOrdenDAOImpl;
import dao.impl.ServicioRepuestoDAOImpl;
import dao.impl.RepuestoDAOImpl;

import modelo.Presupuesto;
import modelo.DetalleOrden;
import modelo.ServicioRepuesto;
import modelo.Repuesto;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PresupuestoServicio {

    private final PresupuestoDAO presupuestoDAO;
    private final DetalleOrdenDAO detalleOrdenDAO;
    private final ServicioRepuestoDAO servicioRepuestoDAO;
    private final RepuestoDAO repuestoDAO;

    public PresupuestoServicio() {
        presupuestoDAO = new PresupuestoDAOImpl();
        detalleOrdenDAO = new DetalleOrdenDAOImpl();
        servicioRepuestoDAO = new ServicioRepuestoDAOImpl();
        repuestoDAO = new RepuestoDAOImpl();
    }

    public boolean guardar(Presupuesto p) throws SQLException {

        validarPresupuesto(p);

        if (presupuestoDAO.buscarPorOrden(p.getIdOrden()) != null) {
            throw new IllegalArgumentException(
                    "Esta orden ya tiene un presupuesto registrado.");
        }

        // Calcular automáticamente el costo de los repuestos.
        p.setRepuestos(
                calcularCostoRepuestosPorOrden(p.getIdOrden()));

        calcularTotal(p);

        p.setFecha(LocalDateTime.now());
        p.setEstado("PENDIENTE");
        p.setAutorizado(false);

        return presupuestoDAO.guardar(p);
    }

    public boolean actualizar(Presupuesto p) throws SQLException {

        if (p == null || p.getIdPresupuesto() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un presupuesto válido.");
        }

        Presupuesto existente =
                presupuestoDAO.buscarPorId(p.getIdPresupuesto());

        if (existente == null) {
            throw new IllegalArgumentException(
                    "El presupuesto no existe.");
        }

        if (!"PENDIENTE".equals(existente.getEstado())) {
            throw new IllegalArgumentException(
                    "Solo se pueden modificar presupuestos pendientes.");
        }

        // Mantener la orden original del presupuesto.
        p.setIdOrden(existente.getIdOrden());

        // Recalcular automáticamente el costo de los repuestos.
        p.setRepuestos(
                calcularCostoRepuestosPorOrden(existente.getIdOrden()));

        calcularTotal(p);

        return presupuestoDAO.actualizar(p);
    }

    public double calcularCostoRepuestosPorOrden(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione una orden válida.");
        }

        double totalRepuestos = 0;

        List<DetalleOrden> detalles =
                detalleOrdenDAO.listarPorOrden(idOrden);

        for (DetalleOrden detalle : detalles) {

            List<ServicioRepuesto> asociaciones =
                    servicioRepuestoDAO.listarPorServicio(
                            detalle.getIdServicio());

            for (ServicioRepuesto asociacion : asociaciones) {

                Repuesto repuesto = repuestoDAO.buscarPorId(
                        asociacion.getIdRepuesto());

                if (repuesto == null) {
                    throw new IllegalArgumentException(
                            "No se encontró el repuesto asociado al servicio.");
                }

                int cantidadNecesaria =
                        detalle.getCantidad() * asociacion.getCantidad();

                double costo =
                        cantidadNecesaria * repuesto.getCosto();

                totalRepuestos += costo;
            }
        }

        return totalRepuestos;
    }

    public boolean autorizar(int id) throws SQLException {

        Presupuesto p = presupuestoDAO.buscarPorId(id);

        if (p == null) {
            throw new IllegalArgumentException(
                    "El presupuesto no existe.");
        }

        if (!"PENDIENTE".equals(p.getEstado())) {
            throw new IllegalArgumentException(
                    "Solo se pueden aprobar presupuestos pendientes.");
        }

        return presupuestoDAO.actualizarEstado(
                id, "APROBADO", true);
    }

    public boolean rechazar(int id) throws SQLException {

        Presupuesto p = presupuestoDAO.buscarPorId(id);

        if (p == null) {
            throw new IllegalArgumentException(
                    "El presupuesto no existe.");
        }

        if (!"PENDIENTE".equals(p.getEstado())) {
            throw new IllegalArgumentException(
                    "Solo se pueden rechazar presupuestos pendientes.");
        }

        return presupuestoDAO.actualizarEstado(
                id, "RECHAZADO", false);
    }

    public Presupuesto buscarPorId(int id) throws SQLException {
        return presupuestoDAO.buscarPorId(id);
    }

    public Presupuesto buscarPorOrden(int idOrden) throws SQLException {
        return presupuestoDAO.buscarPorOrden(idOrden);
    }

    public List<Presupuesto> listar() throws SQLException {
        return presupuestoDAO.listar();
    }

    // Calcula subtotal, impuesto y total usando el porcentaje configurado.
    public void calcularTotal(Presupuesto p) {

        validarImportes(p);

        double subtotal = p.getManoObra() + p.getRepuestos();

        if (p.getDescuento() > subtotal) {
            throw new IllegalArgumentException(
                    "El descuento no puede superar el subtotal.");
        }

        double porcentaje = p.getPorcentajeImpuesto();

        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException(
                    "El impuesto debe estar entre 0 y 100 %.");
        }

        p.setSubtotal(subtotal);

        double baseImponible = subtotal - p.getDescuento();

        double impuesto =
                baseImponible * (porcentaje / 100.0);

        p.setImpuesto(impuesto);

        double total = baseImponible + impuesto;

        p.setTotal(total);
    }

    private void validarPresupuesto(Presupuesto p) {

        if (p == null) {
            throw new IllegalArgumentException(
                    "El presupuesto es obligatorio.");
        }

        if (p.getIdOrden() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione una orden de trabajo.");
        }

        validarImportes(p);
    }

    private void validarImportes(Presupuesto p) {

        if (p.getManoObra() < 0
                || p.getRepuestos() < 0
                || p.getDescuento() < 0
                || p.getImpuesto() < 0) {

            throw new IllegalArgumentException(
                    "Los importes no pueden ser negativos.");
        }

        if (p.getPorcentajeImpuesto() < 0
                || p.getPorcentajeImpuesto() > 100) {

            throw new IllegalArgumentException(
                    "El impuesto debe estar entre 0 y 100 %.");
        }
    }
}
