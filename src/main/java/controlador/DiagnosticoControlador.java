/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controlador;

import modelo.Diagnostico;
import servicio.DiagnosticoServicio;
import java.sql.SQLException;
import java.util.List;

public class DiagnosticoControlador {

    private final DiagnosticoServicio servicio;

    public DiagnosticoControlador() {
        servicio = new DiagnosticoServicio();
    }

    public boolean guardar(Diagnostico diagnostico) throws SQLException {
        return servicio.guardar(diagnostico);
    }

    public Diagnostico buscarPorOrden(int idOrden) throws SQLException {
        return servicio.buscarPorOrden(idOrden);
    }

    public List<Diagnostico> listarPorOrden(int idOrden) throws SQLException {
        return servicio.listarPorOrden(idOrden);
    }
}
