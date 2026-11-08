/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.ConsumoOrden;
import java.sql.SQLException;
import java.util.List;

public interface ConsumoOrdenDAO {
    boolean guardar(ConsumoOrden consumo) throws SQLException;
    List<ConsumoOrden> listarPorOrden(int idOrden) throws SQLException;
}