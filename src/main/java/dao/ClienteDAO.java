/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    boolean guardar(Cliente cliente) throws SQLException;
    boolean actualizar(Cliente cliente) throws SQLException;
    boolean cambiarEstado(int idCliente, String estado) throws SQLException;
    Cliente buscarPorId(int idCliente) throws SQLException;
    List<Cliente> listar() throws SQLException;
}