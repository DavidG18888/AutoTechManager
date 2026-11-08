/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import modelo.ControlCalidad;
import java.sql.SQLException;
import java.util.List;

public interface ControlCalidadDAO {

    boolean guardar(ControlCalidad control) throws SQLException;

    boolean actualizar(ControlCalidad control) throws SQLException;

    List<ControlCalidad> listarPorOrden(int idOrden)
            throws SQLException;

    boolean tieneControlesPendientes(int idOrden)
            throws SQLException;
}
    