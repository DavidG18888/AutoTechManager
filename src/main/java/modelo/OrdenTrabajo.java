/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo;

import java.time.LocalDateTime;

public class OrdenTrabajo {
    private int idOrden;
    private int idCliente;
    private int idVehiculo;
    private int idMecanicoResponsable;
    private LocalDateTime fecha;
    private double kilometraje;
    private String sintomas;
    private String diagnosticoPreliminar;
    private String estado;

    public OrdenTrabajo() {}

    public OrdenTrabajo(int idOrden, int idCliente, int idVehiculo,
        int idMecanicoResponsable, LocalDateTime fecha,
        double kilometraje, String sintomas,
        String diagnosticoPreliminar, String estado) {
        this.idOrden = idOrden;
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.idMecanicoResponsable = idMecanicoResponsable;
        this.fecha = fecha;
        this.kilometraje = kilometraje;
        this.sintomas = sintomas;
        this.diagnosticoPreliminar = diagnosticoPreliminar;
        this.estado = estado;
    }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(int idVehiculo) { this.idVehiculo = idVehiculo; }
    public int getIdMecanicoResponsable() { return idMecanicoResponsable; }
    public void setIdMecanicoResponsable(int idMecanicoResponsable) { this.idMecanicoResponsable = idMecanicoResponsable; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public double getKilometraje() { return kilometraje; }
    public void setKilometraje(double kilometraje) { this.kilometraje = kilometraje; }
    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public String getDiagnosticoPreliminar() { return diagnosticoPreliminar; }
    public void setDiagnosticoPreliminar(String diagnosticoPreliminar) { this.diagnosticoPreliminar = diagnosticoPreliminar; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
