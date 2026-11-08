/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo;

import java.time.LocalDateTime;

public class ControlCalidad {
    private int idControl;
    private int idOrden;
    private int idMecanico;
    private LocalDateTime fecha;
    private String descripcion;
    private String resultado;
    private String observaciones;

    public ControlCalidad() {}

    public ControlCalidad(int idControl, int idOrden, int idMecanico,
        LocalDateTime fecha, String descripcion,
        String resultado, String observaciones) {
        this.idControl = idControl;
        this.idOrden = idOrden;
        this.idMecanico = idMecanico;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.resultado = resultado;
        this.observaciones = observaciones;
    }

    public int getIdControl() { return idControl; }
    public void setIdControl(int idControl) { this.idControl = idControl; }
    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }
    public int getIdMecanico() { return idMecanico; }
    public void setIdMecanico(int idMecanico) { this.idMecanico = idMecanico; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
