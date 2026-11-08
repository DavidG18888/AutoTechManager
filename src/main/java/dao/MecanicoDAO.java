/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Mecanico;
import java.sql.SQLException;
import java.util.List;

public interface MecanicoDAO {
    boolean guardar(Mecanico mecanico) throws SQLException;
    boolean actualizar(Mecanico mecanico) throws SQLException;
    boolean cambiarEstado(int idMecanico, String estado) throws SQLException;
    Mecanico buscarPorId(int idMecanico) throws SQLException;
    List<Mecanico> listar() throws SQLException;
}