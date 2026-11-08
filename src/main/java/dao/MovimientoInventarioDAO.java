/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.MovimientoInventario;
import java.sql.SQLException;
import java.util.List;

public interface MovimientoInventarioDAO {
    boolean guardar(MovimientoInventario movimiento) throws SQLException;
    List<MovimientoInventario> listarPorRepuesto(int idRepuesto) throws SQLException;
    List<MovimientoInventario> listarPorOrden(int idOrden) throws SQLException;
}