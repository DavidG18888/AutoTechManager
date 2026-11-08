/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dao.VehiculoDAO;
import dao.impl.VehiculoDAOImpl;
import modelo.Vehiculo;

import java.sql.SQLException;
import java.util.List;

public class VehiculoServicio {

    private final VehiculoDAO vehiculoDAO;

    public VehiculoServicio() {
        this.vehiculoDAO = new VehiculoDAOImpl();
    }

    public boolean guardar(Vehiculo vehiculo) throws SQLException {

        validarVehiculo(vehiculo);

        return vehiculoDAO.guardar(vehiculo);
    }

    public boolean actualizar(Vehiculo vehiculo) throws SQLException {

        if (vehiculo.getIdVehiculo() <= 0) {
            throw new IllegalArgumentException("ID de vehículo inválido.");
        }

        validarVehiculo(vehiculo);

        return vehiculoDAO.actualizar(vehiculo);
    }

    public Vehiculo buscarPorId(int idVehiculo) throws SQLException {
        return vehiculoDAO.buscarPorId(idVehiculo);
    }

    public List<Vehiculo> listar() throws SQLException {
        return vehiculoDAO.listar();
    }

    public List<Vehiculo> listarPorCliente(int idCliente)
            throws SQLException {

        if (idCliente <= 0) {
            throw new IllegalArgumentException("Seleccione un cliente.");
        }

        return vehiculoDAO.listarPorCliente(idCliente);
    }

    private void validarVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo es obligatorio.");
        }

        if (vehiculo.getIdCliente() <= 0) {
            throw new IllegalArgumentException("Seleccione un cliente.");
        }

        if (vehiculo.getPlaca() == null
                || vehiculo.getPlaca().isBlank()) {
            throw new IllegalArgumentException("Ingrese la placa.");
        }

        if (vehiculo.getVin() == null
                || vehiculo.getVin().isBlank()) {
            throw new IllegalArgumentException("Ingrese el VIN.");
        }

        if (vehiculo.getMarca() == null
                || vehiculo.getMarca().isBlank()) {
            throw new IllegalArgumentException("Ingrese la marca.");
        }

        if (vehiculo.getModelo() == null
                || vehiculo.getModelo().isBlank()) {
            throw new IllegalArgumentException("Ingrese el modelo.");
        }

        if (vehiculo.getAnio() <= 0) {
            throw new IllegalArgumentException("El año no es válido.");
        }

        if (vehiculo.getKilometraje() < 0) {
            throw new IllegalArgumentException(
                    "El kilometraje no puede ser negativo.");
        }
    }
}