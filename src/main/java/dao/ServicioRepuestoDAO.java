/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.ServicioRepuesto;
import java.sql.SQLException;
import java.util.List;

public interface ServicioRepuestoDAO {

    boolean guardar(ServicioRepuesto servicioRepuesto) throws SQLException;

    List<ServicioRepuesto> listarPorServicio(int idServicio) throws SQLException;

    boolean existeRelacion(int idServicio, int idRepuesto) throws SQLException;
}