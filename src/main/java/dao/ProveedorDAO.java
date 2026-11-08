/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Proveedor;
import java.sql.SQLException;
import java.util.List;

public interface ProveedorDAO {
    boolean guardar(Proveedor proveedor) throws SQLException;
    boolean actualizar(Proveedor proveedor) throws SQLException;
    boolean cambiarEstado(int idProveedor, String estado) throws SQLException;
    Proveedor buscarPorId(int idProveedor) throws SQLException;
    List<Proveedor> listar() throws SQLException;
}