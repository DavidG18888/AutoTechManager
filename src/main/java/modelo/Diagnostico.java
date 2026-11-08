/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo;

import java.time.LocalDateTime;

public class Diagnostico {

    private int idDiagnostico;
    private int idOrden;
    private LocalDateTime fecha;
    private String descripcion;
    private String causa;
    private String recomendaciones;
    private boolean requiereCambioMotor;
    private String evidenciasTecnicas;

    public Diagnostico() {}

    public Diagnostico(int idDiagnostico, int idOrden,
            LocalDateTime fecha, String descripcion, String causa,
            String recomendaciones, boolean requiereCambioMotor) {

        this.idDiagnostico = idDiagnostico;
        this.idOrden = idOrden;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.causa = causa;
        this.recomendaciones = recomendaciones;
        this.requiereCambioMotor = requiereCambioMotor;
    }

    public int getIdDiagnostico() {
        return idDiagnostico;
    }

    public void setIdDiagnostico(int idDiagnostico) {
        this.idDiagnostico = idDiagnostico;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public boolean isRequiereCambioMotor() {
        return requiereCambioMotor;
    }

    public void setRequiereCambioMotor(boolean requiereCambioMotor) {
        this.requiereCambioMotor = requiereCambioMotor;
    }

    public String getEvidenciasTecnicas() {
        return evidenciasTecnicas;
    }

    public void setEvidenciasTecnicas(String evidenciasTecnicas) {
        this.evidenciasTecnicas = evidenciasTecnicas;
    }
}