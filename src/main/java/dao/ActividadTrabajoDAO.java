/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import modelo.ActividadTrabajo;
import java.sql.SQLException;
import java.util.List;

public interface ActividadTrabajoDAO {

    boolean guardar(ActividadTrabajo actividad) throws SQLException;

    boolean actualizarEstado(int idActividad, String estado)
            throws SQLException;

    List<ActividadTrabajo> listarPorOrden(int idOrden)
            throws SQLException;

    boolean tienePendientes(int idOrden) throws SQLException;
}
