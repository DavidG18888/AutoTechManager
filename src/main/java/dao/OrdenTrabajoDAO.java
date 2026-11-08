/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.OrdenTrabajo;
import java.sql.SQLException;
import java.util.List;

public interface OrdenTrabajoDAO {
    int guardar(OrdenTrabajo orden) throws SQLException;
    boolean actualizar(OrdenTrabajo orden) throws SQLException;
    boolean actualizarEstado(int idOrden, String estado) throws SQLException;
    OrdenTrabajo buscarPorId(int idOrden) throws SQLException;
    List<OrdenTrabajo> listar() throws SQLException;
    List<OrdenTrabajo> listarPorVehiculo(int idVehiculo) throws SQLException;
}