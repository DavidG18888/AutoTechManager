/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package servicio;

import dao.OrdenTrabajoDAO;
import dao.ActividadTrabajoDAO;
import dao.ControlCalidadDAO;
import dao.ConsumoOrdenDAO;
import dao.DetalleOrdenDAO;
import dao.ServicioRepuestoDAO;

import dao.impl.OrdenTrabajoDAOImpl;
import dao.impl.ActividadTrabajoDAOImpl;
import dao.impl.ControlCalidadDAOImpl;
import dao.impl.ConsumoOrdenDAOImpl;
import dao.impl.DetalleOrdenDAOImpl;
import dao.impl.ServicioRepuestoDAOImpl;

import modelo.OrdenTrabajo;
import modelo.ControlCalidad;
import modelo.ActividadTrabajo;
import modelo.ConsumoOrden;
import modelo.DetalleOrden;
import modelo.ServicioRepuesto;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class OrdenTrabajoServicio {

    private final OrdenTrabajoDAO ordenDAO;
    private final ActividadTrabajoDAO actividadDAO;
    private final ControlCalidadDAO controlCalidadDAO;
    private final ConsumoOrdenDAO consumoDAO;
    private final DetalleOrdenDAO detalleDAO;
    private final ServicioRepuestoDAO servicioRepuestoDAO;

    public OrdenTrabajoServicio() {
        this.ordenDAO = new OrdenTrabajoDAOImpl();
        this.actividadDAO = new ActividadTrabajoDAOImpl();
        this.controlCalidadDAO = new ControlCalidadDAOImpl();
        this.consumoDAO = new ConsumoOrdenDAOImpl();
        this.detalleDAO = new DetalleOrdenDAOImpl();
        this.servicioRepuestoDAO = new ServicioRepuestoDAOImpl();
    }

    // REGISTRAR UNA ORDEN
    public int guardar(OrdenTrabajo orden) throws SQLException {

        validarOrden(orden);

        if (orden.getFecha() == null) {
            orden.setFecha(LocalDateTime.now());
        }

        if (orden.getEstado() == null
                || orden.getEstado().isBlank()) {
            orden.setEstado("RECIBIDA");
        } else {
            orden.setEstado(normalizarEstado(orden.getEstado()));
        }

        return ordenDAO.guardar(orden);
    }

    // ACTUALIZAR ESTADO
    public boolean actualizarEstado(int idOrden, String nuevoEstado)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException(
                    "ID de orden inválido.");
        }

        nuevoEstado = normalizarEstado(nuevoEstado);

        if (!esEstadoValido(nuevoEstado)) {
            throw new IllegalArgumentException(
                    "Estado de orden no válido.");
        }

        OrdenTrabajo orden = ordenDAO.buscarPorId(idOrden);

        if (orden == null) {
            throw new IllegalArgumentException(
                    "La orden no existe.");
        }

        String estadoActual = normalizarEstado(orden.getEstado());

        if (!puedeCambiarEstado(estadoActual, nuevoEstado)) {
            throw new IllegalArgumentException(
                    "No se permite pasar de " + estadoActual
                    + " a " + nuevoEstado + ".");
        }

        // VALIDAR ANTES DE FINALIZAR
        if (nuevoEstado.equals("FINALIZADA_Y_ENTREGADA")) {
            validarFinalizacion(idOrden);
        }

        return ordenDAO.actualizarEstado(idOrden, nuevoEstado);
    }

    // VALIDACIONES PARA FINALIZAR
    private void validarFinalizacion(int idOrden)
            throws SQLException {

        // VALIDAR ACTIVIDADES
        List<ActividadTrabajo> actividades =
                actividadDAO.listarPorOrden(idOrden);

        if (actividades.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede finalizar: no existen actividades "
                    + "de trabajo registradas.");
        }

        if (actividadDAO.tienePendientes(idOrden)) {
            throw new IllegalArgumentException(
                    "No se puede finalizar: existen actividades "
                    + "pendientes o sin completar.");
        }

        // VALIDAR CONSUMO DE REPUESTOS
        validarConsumosRepuestos(idOrden);

        // VALIDAR CONTROLES DE CALIDAD
        List<ControlCalidad> controles =
                controlCalidadDAO.listarPorOrden(idOrden);

        if (controles.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede finalizar: no existen controles "
                    + "de calidad registrados.");
        }

        if (controlCalidadDAO.tieneControlesPendientes(idOrden)) {
            throw new IllegalArgumentException(
                    "No se puede finalizar: existen controles "
                    + "de calidad sin aprobar.");
        }
    }

    // COMPARAR REPUESTOS REQUERIDOS CONTRA LOS CONSUMIDOS
    private void validarConsumosRepuestos(int idOrden)
            throws SQLException {

        List<DetalleOrden> detalles =
                detalleDAO.listarPorOrden(idOrden);

        // Cantidad requerida por cada repuesto
        Map<Integer, Double> requeridos = new HashMap<>();

        for (DetalleOrden detalle : detalles) {

            List<ServicioRepuesto> asociaciones =
                    servicioRepuestoDAO.listarPorServicio(
                            detalle.getIdServicio());

            for (ServicioRepuesto asociacion : asociaciones) {

                int idRepuesto = asociacion.getIdRepuesto();

                double cantidadRequerida =
                        detalle.getCantidad()
                        * asociacion.getCantidad();

                requeridos.merge(
                        idRepuesto,
                        cantidadRequerida,
                        Double::sum
                );
            }
        }

        // Si los servicios no requieren repuestos,
        // no hay cantidades pendientes que validar.
        if (requeridos.isEmpty()) {
            return;
        }

        // Cantidad realmente registrada como consumo
        Map<Integer, Double> consumidos = new HashMap<>();

        List<ConsumoOrden> listaConsumos =
                consumoDAO.listarPorOrden(idOrden);

        for (ConsumoOrden consumo : listaConsumos) {

            consumidos.merge(
                    consumo.getIdRepuesto(),
                    (double) consumo.getCantidad(),
                    Double::sum
            );
        }

        // Comparar cantidades
        for (Map.Entry<Integer, Double> entrada
                : requeridos.entrySet()) {

            int idRepuesto = entrada.getKey();
            double cantidadRequerida = entrada.getValue();

            double cantidadConsumida =
                    consumidos.getOrDefault(idRepuesto, 0.0);

            if (cantidadConsumida < cantidadRequerida) {

                double faltante =
                        cantidadRequerida - cantidadConsumida;

                throw new IllegalArgumentException(
                        "No se puede finalizar la orden. "
                        + "Falta registrar el consumo del repuesto ID "
                        + idRepuesto + ". Cantidad pendiente: "
                        + faltante);
            }
        }
    }

    // BUSCAR ORDEN
    public OrdenTrabajo buscarPorId(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException(
                    "ID de orden inválido.");
        }

        return ordenDAO.buscarPorId(idOrden);
    }

    // LISTAR ÓRDENES
    public List<OrdenTrabajo> listar() throws SQLException {
        return ordenDAO.listar();
    }

    // LISTAR POR VEHÍCULO
    public List<OrdenTrabajo> listarPorVehiculo(int idVehiculo)
            throws SQLException {

        if (idVehiculo <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un vehículo.");
        }

        return ordenDAO.listarPorVehiculo(idVehiculo);
    }

    // VALIDAR DATOS
    private void validarOrden(OrdenTrabajo orden) {

        if (orden == null) {
            throw new IllegalArgumentException(
                    "La orden es obligatoria.");
        }

        if (orden.getIdCliente() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un cliente.");
        }

        if (orden.getIdVehiculo() <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un vehículo.");
        }

        if (orden.getKilometraje() < 0) {
            throw new IllegalArgumentException(
                    "El kilometraje no puede ser negativo.");
        }

        if (orden.getSintomas() == null
                || orden.getSintomas().isBlank()) {
            throw new IllegalArgumentException(
                    "Describa los síntomas del vehículo.");
        }
    }

    // NORMALIZAR ESTADOS
    private String normalizarEstado(String estado) {

        if (estado == null) {
            return "";
        }

        return estado.trim()
                .toUpperCase()
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U")
                .replace(" ", "_")
                .replace("DE_", "");
    }

    // ESTADOS PERMITIDOS
    private boolean esEstadoValido(String estado) {

        return switch (estado) {
            case "RECIBIDA",
                 "EN_DIAGNOSTICO",
                 "PENDIENTE_APROBACION",
                 "EN_REPARACION",
                 "EN_CONTROL_CALIDAD",
                 "FINALIZADA_Y_ENTREGADA",
                 "RECHAZADA",
                 "CANCELADA" -> true;

            default -> false;
        };
    }

    // FLUJO DE ESTADOS
    private boolean puedeCambiarEstado(
            String actual, String nuevo) {

        if (actual == null || nuevo == null) {
            return false;
        }

        actual = normalizarEstado(actual);
        nuevo = normalizarEstado(nuevo);

        if (actual.equals(nuevo)) {
            return true;
        }

        return switch (actual) {

            case "RECIBIDA" ->
                    nuevo.equals("EN_DIAGNOSTICO")
                    || nuevo.equals("CANCELADA");

            case "EN_DIAGNOSTICO" ->
                    nuevo.equals("PENDIENTE_APROBACION")
                    || nuevo.equals("CANCELADA");

            case "PENDIENTE_APROBACION" ->
                    nuevo.equals("EN_REPARACION")
                    || nuevo.equals("RECHAZADA")
                    || nuevo.equals("CANCELADA");

            case "EN_REPARACION" ->
                    nuevo.equals("EN_CONTROL_CALIDAD")
                    || nuevo.equals("CANCELADA");

            case "EN_CONTROL_CALIDAD" ->
                    nuevo.equals("FINALIZADA_Y_ENTREGADA")
                    || nuevo.equals("EN_REPARACION");

            default -> false;
        };
    }
}
