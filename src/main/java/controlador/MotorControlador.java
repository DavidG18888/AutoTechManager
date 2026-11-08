/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controlador;

import dao.MotorDAO;
import dao.impl.MotorDAOImpl;
import modelo.Motor;
import java.sql.SQLException;
import java.util.List;

public class MotorControlador {

    private final MotorDAO dao;

    public MotorControlador() {
        dao = new MotorDAOImpl();
    }

    public Motor buscarPorId(int idMotor) throws SQLException {
        return dao.buscarPorId(idMotor);
    }

    public List<Motor> listarDisponibles() throws SQLException {
        return dao.listarDisponibles();
    }

    public List<Motor> listar() throws SQLException {
        return dao.listar();
    }
}
