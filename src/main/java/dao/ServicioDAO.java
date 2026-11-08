/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Servicio;
import java.sql.SQLException;
import java.util.List;

public interface ServicioDAO {
    boolean guardar(Servicio servicio) throws SQLException;
    boolean actualizar(Servicio servicio) throws SQLException;
    boolean cambiarEstado(int idServicio, String estado) throws SQLException;
    Servicio buscarPorId(int idServicio) throws SQLException;
    List<Servicio> listar() throws SQLException;
}