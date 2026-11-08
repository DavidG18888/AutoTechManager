/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo;

import java.time.LocalDateTime;

public class AsignacionMecanico {
    private int idAsignacion;
    private int idOrden;
    private int idMecanico;
    private boolean responsable;
    private LocalDateTime fechaAsignacion;

    public AsignacionMecanico() {}

    public AsignacionMecanico(int idAsignacion, int idOrden,
        int idMecanico, boolean responsable,
        LocalDateTime fechaAsignacion) {
        this.idAsignacion = idAsignacion;
        this.idOrden = idOrden;
        this.idMecanico = idMecanico;
        this.responsable = responsable;
        this.fechaAsignacion = fechaAsignacion;
    }

    public int getIdAsignacion() { return idAsignacion; }
    public void setIdAsignacion(int idAsignacion) { this.idAsignacion = idAsignacion; }
    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }
    public int getIdMecanico() { return idMecanico; }
    public void setIdMecanico(int idMecanico) { this.idMecanico = idMecanico; }
    public boolean isResponsable() { return responsable; }
    public void setResponsable(boolean responsable) { this.responsable = responsable; }
    public LocalDateTime getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }
}
