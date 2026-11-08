/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Pago;
import java.sql.SQLException;
import java.util.List;

public interface PagoDAO {
    boolean guardar(Pago pago) throws SQLException;
    Pago buscarPorId(int idPago) throws SQLException;
    List<Pago> listarPorPresupuesto(int idPresupuesto) throws SQLException;
    double calcularTotalPagado(int idPresupuesto) throws SQLException;
}