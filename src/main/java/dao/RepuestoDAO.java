/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Repuesto;
import java.sql.SQLException;
import java.util.List;

public interface RepuestoDAO {
    boolean guardar(Repuesto repuesto) throws SQLException;
    boolean actualizar(Repuesto repuesto) throws SQLException;
    Repuesto buscarPorId(int idRepuesto) throws SQLException;
    List<Repuesto> listar() throws SQLException;
    List<Repuesto> listarBajoStock() throws SQLException;
}