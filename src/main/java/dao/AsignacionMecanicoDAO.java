/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.AsignacionMecanico;
import java.sql.SQLException;
import java.util.List;

public interface AsignacionMecanicoDAO {

    boolean guardar(AsignacionMecanico asignacion) throws SQLException;

    List<AsignacionMecanico> listarPorOrden(int idOrden) throws SQLException;

    boolean existeAsignacion(int idOrden, int idMecanico) throws SQLException;

    boolean existeResponsable(int idOrden) throws SQLException;
}