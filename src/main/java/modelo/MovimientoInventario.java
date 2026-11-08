/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo;

import java.time.LocalDateTime;

public class MovimientoInventario {
    private int idMovimiento;
    private int idRepuesto;
    private String tipoMovimiento;
    private int cantidad;
    private LocalDateTime fecha;
    private String motivo;
    private Integer idOrden;
    private Integer idConsumo;

    public MovimientoInventario() {}

    public MovimientoInventario(int idMovimiento, int idRepuesto,
        String tipoMovimiento, int cantidad, LocalDateTime fecha,
        String motivo, Integer idOrden, Integer idConsumo) {
        this.idMovimiento = idMovimiento;
        this.idRepuesto = idRepuesto;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.motivo = motivo;
        this.idOrden = idOrden;
        this.idConsumo = idConsumo;
    }

    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }
    public int getIdRepuesto() { return idRepuesto; }
    public void setIdRepuesto(int idRepuesto) { this.idRepuesto = idRepuesto; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }
    public Integer getIdConsumo() { return idConsumo; }
    public void setIdConsumo(Integer idConsumo) { this.idConsumo = idConsumo; }
}
