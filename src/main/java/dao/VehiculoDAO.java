/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Vehiculo;
import java.sql.SQLException;
import java.util.List;

public interface VehiculoDAO {
    boolean guardar(Vehiculo vehiculo) throws SQLException;
    boolean actualizar(Vehiculo vehiculo) throws SQLException;
    Vehiculo buscarPorId(int idVehiculo) throws SQLException;
    List<Vehiculo> listar() throws SQLException;
    List<Vehiculo> listarPorCliente(int idCliente) throws SQLException;
}