/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package servicio;

import config.ConexionDB;
import dao.RepuestoDAO;
import dao.ConsumoOrdenDAO;
import dao.MovimientoInventarioDAO;
import dao.impl.RepuestoDAOImpl;
import dao.impl.ConsumoOrdenDAOImpl;
import dao.impl.MovimientoInventarioDAOImpl;
import modelo.Repuesto;
import modelo.ConsumoOrden;
import modelo.MovimientoInventario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class InventarioServicio {

    private final RepuestoDAO repuestoDAO;
    private final ConsumoOrdenDAO consumoDAO;
    private final MovimientoInventarioDAO movimientoDAO;

    public InventarioServicio() {
        this.repuestoDAO = new RepuestoDAOImpl();
        this.consumoDAO = new ConsumoOrdenDAOImpl();
        this.movimientoDAO = new MovimientoInventarioDAOImpl();
    }

    public boolean guardarRepuesto(Repuesto repuesto)
            throws SQLException {

        validarRepuesto(repuesto);
        return repuestoDAO.guardar(repuesto);
    }

    public boolean actualizarRepuesto(Repuesto repuesto)
            throws SQLException {

        if (repuesto == null || repuesto.getIdRepuesto() <= 0) {
            throw new IllegalArgumentException(
                    "ID de repuesto inválido.");
        }

        validarRepuesto(repuesto);
        return repuestoDAO.actualizar(repuesto);
    }

    public Repuesto buscarPorId(int idRepuesto)
            throws SQLException {

        return repuestoDAO.buscarPorId(idRepuesto);
    }

    public List<Repuesto> listar() throws SQLException {
        return repuestoDAO.listar();
    }

    public List<Repuesto> listarBajoStock()
            throws SQLException {

        return repuestoDAO.listarBajoStock();
    }

    // REGISTRAR ENTRADA DE INVENTARIO
    public boolean registrarEntrada(int idRepuesto, int cantidad)
            throws SQLException {

        if (idRepuesto <= 0 || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad y el repuesto deben ser válidos.");
        }

        Repuesto repuesto = repuestoDAO.buscarPorId(idRepuesto);

        if (repuesto == null) {
            throw new IllegalArgumentException(
                    "El repuesto no existe.");
        }

        repuesto.setExistencia(
                repuesto.getExistencia() + cantidad);

        boolean actualizado = repuestoDAO.actualizar(repuesto);

        if (!actualizado) {
            return false;
        }

        MovimientoInventario movimiento =
                new MovimientoInventario();

        movimiento.setIdRepuesto(idRepuesto);
        movimiento.setTipoMovimiento("ENTRADA");
        movimiento.setCantidad(cantidad);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setMotivo("Entrada de inventario");

        return movimientoDAO.guardar(movimiento);
    }

    // REGISTRAR CONSUMO DE REPUESTO EN UNA ORDEN
    public boolean registrarConsumo(int idOrden, int idRepuesto,
            int cantidad) throws SQLException {

        if (idOrden <= 0 || idRepuesto <= 0 || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "Los datos del consumo no son válidos.");
        }

        Connection conexion = ConexionDB.getConexion();

        if (conexion == null || conexion.isClosed()) {
            throw new SQLException(
                    "No se pudo establecer la conexión con la base de datos.");
        }

        synchronized (conexion) {

            boolean autoCommitAnterior = conexion.getAutoCommit();

            try {
                conexion.setAutoCommit(false);

                double costoUnitario;
                int existencia;

                // Consultar el repuesto y bloquear su registro.
                String sqlConsulta =
                        "SELECT existencia, costo "
                        + "FROM Repuestos WITH (UPDLOCK, ROWLOCK) "
                        + "WHERE id_repuesto = ?";

                try (PreparedStatement ps =
                        conexion.prepareStatement(sqlConsulta)) {

                    ps.setInt(1, idRepuesto);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "El repuesto no existe.");
                        }

                        existencia = rs.getInt("existencia");
                        costoUnitario = rs.getDouble("costo");
                    }
                }

                // Verificar existencia disponible.
                if (existencia < cantidad) {
                    throw new IllegalArgumentException(
                            "Inventario insuficiente. Existencia actual: "
                            + existencia);
                }

                // Descontar existencias.
                String sqlDescuento =
                        "UPDATE Repuestos "
                        + "SET existencia = existencia - ? "
                        + "WHERE id_repuesto = ? "
                        + "AND existencia >= ?";

                try (PreparedStatement ps =
                        conexion.prepareStatement(sqlDescuento)) {

                    ps.setInt(1, cantidad);
                    ps.setInt(2, idRepuesto);
                    ps.setInt(3, cantidad);

                    if (ps.executeUpdate() == 0) {
                        throw new SQLException(
                                "No fue posible descontar el inventario.");
                    }
                }

                // Crear registro del consumo.
                LocalDateTime fechaActual = LocalDateTime.now();

                ConsumoOrden consumo = new ConsumoOrden();

                consumo.setIdOrden(idOrden);
                consumo.setIdRepuesto(idRepuesto);
                consumo.setCantidad(cantidad);
                consumo.setCostoUnitario(costoUnitario);
                consumo.setFecha(fechaActual);

                if (!consumoDAO.guardar(consumo)) {
                    throw new SQLException(
                            "No se pudo registrar el consumo.");
                }

                // Registrar movimiento de salida.
                MovimientoInventario movimiento =
                        new MovimientoInventario();

                movimiento.setIdRepuesto(idRepuesto);
                movimiento.setTipoMovimiento("SALIDA");
                movimiento.setCantidad(cantidad);
                movimiento.setFecha(fechaActual);

                movimiento.setMotivo(
                        "Consumo en orden de trabajo " + idOrden);

                movimiento.setIdOrden(idOrden);

                // Vincular el movimiento con el consumo generado.
                movimiento.setIdConsumo(
                        consumo.getIdConsumo());

                if (!movimientoDAO.guardar(movimiento)) {
                    throw new SQLException(
                            "No se pudo registrar el movimiento de inventario.");
                }

                // Confirmar todas las operaciones.
                conexion.commit();

                return true;

            } catch (SQLException | RuntimeException e) {

                try {
                    conexion.rollback();
                } catch (SQLException errorRollback) {
                    e.addSuppressed(errorRollback);
                }

                throw e;

            } finally {

                conexion.setAutoCommit(autoCommitAnterior);
            }
        }
    }

    // LISTAR CONSUMOS DE UNA ORDEN
    public List<ConsumoOrden> listarConsumos(int idOrden)
            throws SQLException {

        if (idOrden <= 0) {
            throw new IllegalArgumentException(
                    "El ID de la orden no es válido.");
        }

        return consumoDAO.listarPorOrden(idOrden);
    }

    // VALIDAR DATOS DEL REPUESTO
    private void validarRepuesto(Repuesto repuesto) {

        if (repuesto == null) {
            throw new IllegalArgumentException(
                    "El repuesto es obligatorio.");
        }

        if (repuesto.getCodigo() == null
                || repuesto.getCodigo().isBlank()) {

            throw new IllegalArgumentException(
                    "Ingrese el código.");
        }

        if (repuesto.getNombre() == null
                || repuesto.getNombre().isBlank()) {

            throw new IllegalArgumentException(
                    "Ingrese el nombre.");
        }

        if (repuesto.getExistencia() < 0) {
            throw new IllegalArgumentException(
                    "La existencia no puede ser negativa.");
        }

        if (repuesto.getStockMinimo() < 0) {
            throw new IllegalArgumentException(
                    "El stock mínimo no puede ser negativo.");
        }

        if (repuesto.getCosto() < 0) {
            throw new IllegalArgumentException(
                    "El costo no puede ser negativo.");
        }
    }
}
