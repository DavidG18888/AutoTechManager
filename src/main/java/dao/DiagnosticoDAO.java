/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Diagnostico;
import java.sql.SQLException;
import java.util.List;

public interface DiagnosticoDAO {
    boolean guardar(Diagnostico diagnostico) throws SQLException;
    Diagnostico buscarPorOrden(int idOrden) throws SQLException;
    List<Diagnostico> listarPorOrden(int idOrden) throws SQLException;
}