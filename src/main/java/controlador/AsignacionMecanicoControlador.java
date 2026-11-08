/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controlador;

import modelo.AsignacionMecanico;
import servicio.AsignacionMecanicoServicio;
import java.sql.SQLException;
import java.util.List;

public class AsignacionMecanicoControlador {

    private final AsignacionMecanicoServicio servicio;

    public AsignacionMecanicoControlador() {
        servicio = new AsignacionMecanicoServicio();
    }

    public boolean guardar(AsignacionMecanico asignacion)
            throws SQLException {
        return servicio.guardar(asignacion);
    }

    public List<AsignacionMecanico> listarPorOrden(int idOrden)
            throws SQLException {
        return servicio.listarPorOrden(idOrden);
    }

    public boolean existeAsignacion(int idOrden, int idMecanico)
            throws SQLException {
        return servicio.existeAsignacion(idOrden, idMecanico);
    }
}
