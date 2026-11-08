/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Presupuesto;
import java.sql.SQLException;
import java.util.List;

public interface PresupuestoDAO {

    boolean guardar(Presupuesto presupuesto) throws SQLException;

    boolean actualizar(Presupuesto presupuesto) throws SQLException;

    boolean actualizarEstado(
            int idPresupuesto,
            String estado,
            boolean autorizado
    ) throws SQLException;

    Presupuesto buscarPorId(int idPresupuesto) throws SQLException;

    Presupuesto buscarPorOrden(int idOrden) throws SQLException;

    List<Presupuesto> listar() throws SQLException;
}