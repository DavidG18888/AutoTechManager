/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo;

import java.time.LocalDateTime;

public class Pago {

    private int idPago;
    private int idPresupuesto;
    private LocalDateTime fecha;
    private double monto;
    private String metodoPago;
    private String referencia;
    private String observaciones;

    public Pago() {
    }

    public Pago(int idPago, int idPresupuesto, LocalDateTime fecha,
            double monto, String metodoPago, String referencia,
            String observaciones) {
        this.idPago = idPago;
        this.idPresupuesto = idPresupuesto;
        this.fecha = fecha;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.referencia = referencia;
        this.observaciones = observaciones;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(int idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}