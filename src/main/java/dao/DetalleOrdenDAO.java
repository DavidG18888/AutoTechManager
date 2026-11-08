/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.DetalleOrden;
import java.sql.SQLException;
import java.util.List;

public interface DetalleOrdenDAO {
    boolean guardar(DetalleOrden detalle) throws SQLException;
    boolean actualizar(DetalleOrden detalle) throws SQLException;
    List<DetalleOrden> listarPorOrden(int idOrden) throws SQLException;
}