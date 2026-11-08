/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Repuesto;
import modelo.ConsumoOrden;
import servicio.InventarioServicio;
import java.sql.SQLException;
import java.util.List;

public class InventarioControlador {

    private final InventarioServicio servicio;

    public InventarioControlador() {
        servicio = new InventarioServicio();
    }

    public boolean guardarRepuesto(Repuesto repuesto) throws SQLException {
        return servicio.guardarRepuesto(repuesto);
    }

    public boolean actualizarRepuesto(Repuesto repuesto) throws SQLException {
        return servicio.actualizarRepuesto(repuesto);
    }

    public Repuesto buscarPorId(int id) throws SQLException {
        return servicio.buscarPorId(id);
    }

    public List<Repuesto> listar() throws SQLException {
        return servicio.listar();
    }

    public List<Repuesto> listarBajoStock() throws SQLException {
        return servicio.listarBajoStock();
    }

    public boolean registrarEntrada(int idRepuesto, int cantidad)
            throws SQLException {
        return servicio.registrarEntrada(idRepuesto, cantidad);
    }

    public boolean registrarConsumo(int idOrden, int idRepuesto, int cantidad)
            throws SQLException {
        return servicio.registrarConsumo(idOrden, idRepuesto, cantidad);
    }

    public List<ConsumoOrden> listarConsumos(int idOrden)
            throws SQLException {
        return servicio.listarConsumos(idOrden);
    }
}