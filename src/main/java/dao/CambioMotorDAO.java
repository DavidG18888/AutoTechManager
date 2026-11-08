/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.CambioMotor;
import java.sql.SQLException;

public interface CambioMotorDAO {
    boolean guardar(CambioMotor cambio) throws SQLException;
    CambioMotor buscarPorOrden(int idOrden) throws SQLException;
}