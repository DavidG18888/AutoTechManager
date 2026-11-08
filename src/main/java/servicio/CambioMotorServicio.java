/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package servicio;

import config.ConexionDB;
import dao.MotorDAO;
import dao.CambioMotorDAO;
import dao.impl.MotorDAOImpl;
import dao.impl.CambioMotorDAOImpl;
import modelo.Motor;
import modelo.CambioMotor;
import modelo.Vehiculo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CambioMotorServicio {

    private final MotorDAO motorDAO;
    private final CambioMotorDAO cambioMotorDAO;

    public CambioMotorServicio() {
        this.motorDAO = new MotorDAOImpl();
        this.cambioMotorDAO = new CambioMotorDAOImpl();
    }

    public boolean registrarCambio(CambioMotor cambio, Vehiculo vehiculo)
            throws SQLException {

        if (cambio == null || vehiculo == null) {
            throw new IllegalArgumentException(
                    "Faltan datos para registrar el cambio.");
        }

        Connection conexion = ConexionDB.getConexion();

        if (conexion == null) {
            throw new SQLException("No hay conexión con la base de datos.");
        }

        boolean autoCommitAnterior = conexion.getAutoCommit();

        try {
            conexion.setAutoCommit(false);

            if (cambio.getIdOrden() <= 0
                    || cambio.getIdMotorNuevo() <= 0) {
                throw new IllegalArgumentException(
                        "Seleccione la orden y el motor.");
            }

            if (!cambio.isAutorizacionCliente()) {
                throw new IllegalArgumentException(
                        "El cliente debe autorizar el cambio.");
            }

            Motor motor = motorDAO.buscarPorId(cambio.getIdMotorNuevo());

            if (motor == null) {
                throw new IllegalArgumentException(
                        "El motor seleccionado no existe.");
            }

            if (!"DISPONIBLE".equalsIgnoreCase(motor.getEstado())) {
                throw new IllegalArgumentException(
                        "El motor no está disponible.");
            }

            boolean marcaCompatible = motor.getCompatibleMarca() != null
                    && motor.getCompatibleMarca()
                            .equalsIgnoreCase(vehiculo.getMarca());

            boolean modeloCompatible = motor.getCompatibleModelo() != null
                    && motor.getCompatibleModelo()
                            .equalsIgnoreCase(vehiculo.getModelo());

            boolean anioCompatible =
                    motor.getCompatibleAnio() == vehiculo.getAnio();

            if (!marcaCompatible || !modeloCompatible || !anioCompatible) {
                throw new IllegalArgumentException(
                        "El motor no es compatible con el vehículo.");
            }

            if (cambio.getNumeroSerieAnterior() == null
                    || cambio.getNumeroSerieAnterior().isBlank()) {
                throw new IllegalArgumentException(
                        "Ingrese el número de serie anterior.");
            }

            if (cambio.getDestinoMotorAnterior() == null
                    || cambio.getDestinoMotorAnterior().isBlank()) {
                throw new IllegalArgumentException(
                        "Indique el destino del motor anterior.");
            }

            cambio.setFechaCambio(LocalDateTime.now());

            if (!cambioMotorDAO.guardar(cambio)) {
                conexion.rollback();
                return false;
            }

            if (!motorDAO.actualizarEstado(
                    motor.getIdMotor(), "INSTALADO")) {
                conexion.rollback();
                return false;
            }

            conexion.commit();
            return true;

        } catch (SQLException | RuntimeException ex) {

            try {
                conexion.rollback();
            } catch (SQLException errorRollback) {
                ex.addSuppressed(errorRollback);
            }

            throw ex;

        } finally {
            conexion.setAutoCommit(autoCommitAnterior);
        }
    }
}
