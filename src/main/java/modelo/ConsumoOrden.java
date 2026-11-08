/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;

public class ConsumoOrden {

    private int idConsumo;
    private int idOrden;
    private int idRepuesto;
    private int cantidad;
    private double costoUnitario;
    private LocalDateTime fecha;
    private String nombreRepuesto;

    public ConsumoOrden() {
    }

    public ConsumoOrden(int idConsumo, int idOrden, int idRepuesto,
            int cantidad, double costoUnitario, LocalDateTime fecha) {

        this.idConsumo = idConsumo;
        this.idOrden = idOrden;
        this.idRepuesto = idRepuesto;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.fecha = fecha;
    }

    public int getIdConsumo() {
        return idConsumo;
    }

    public void setIdConsumo(int idConsumo) {
        this.idConsumo = idConsumo;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public int getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(int idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getNombreRepuesto() {
        return nombreRepuesto;
    }

    public void setNombreRepuesto(String nombreRepuesto) {
        this.nombreRepuesto = nombreRepuesto;
    }
}