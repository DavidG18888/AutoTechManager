/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Motor;
import java.sql.SQLException;
import java.util.List;

public interface MotorDAO {
    boolean guardar(Motor motor) throws SQLException;
    boolean actualizar(Motor motor) throws SQLException;
    boolean actualizarEstado(int idMotor, String estado) throws SQLException;
    Motor buscarPorId(int idMotor) throws SQLException;
    List<Motor> listar() throws SQLException;
    List<Motor> listarDisponibles() throws SQLException;
}