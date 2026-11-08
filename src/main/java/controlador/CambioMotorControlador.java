/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import dao.CambioMotorDAO;
import dao.impl.CambioMotorDAOImpl;
import modelo.CambioMotor;
import modelo.Vehiculo;
import servicio.CambioMotorServicio;

import java.sql.SQLException;

public class CambioMotorControlador {

    private final CambioMotorServicio servicio;
    private final CambioMotorDAO cambioMotorDAO;

    public CambioMotorControlador() {
        servicio = new CambioMotorServicio();
        cambioMotorDAO = new CambioMotorDAOImpl();
    }

    public boolean registrarCambio(CambioMotor cambio, Vehiculo vehiculo)
            throws SQLException {

        return servicio.registrarCambio(cambio, vehiculo);
    }

    public CambioMotor buscarPorOrden(int idOrden)
            throws SQLException {

        return cambioMotorDAO.buscarPorOrden(idOrden);
    }
}