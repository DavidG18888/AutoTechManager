/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo;

import java.time.LocalDateTime;

public class CambioMotor {
    private int idCambio;
    private int idOrden;
    private int idMotorNuevo;
    private String numeroSerieAnterior;
    private String destinoMotorAnterior;
    private LocalDateTime fechaCambio;
    private boolean autorizacionCliente;
    private String pruebasRealizadas;
    private String resultadoPruebas;
    private int garantiaMeses;

    public CambioMotor() {}

    public CambioMotor(int idCambio, int idOrden, int idMotorNuevo,
            String numeroSerieAnterior, String destinoMotorAnterior,
            LocalDateTime fechaCambio, boolean autorizacionCliente,
            String pruebasRealizadas, String resultadoPruebas,
            int garantiaMeses) {
        this.idCambio = idCambio;
        this.idOrden = idOrden;
        this.idMotorNuevo = idMotorNuevo;
        this.numeroSerieAnterior = numeroSerieAnterior;
        this.destinoMotorAnterior = destinoMotorAnterior;
        this.fechaCambio = fechaCambio;
        this.autorizacionCliente = autorizacionCliente;
        this.pruebasRealizadas = pruebasRealizadas;
        this.resultadoPruebas = resultadoPruebas;
        this.garantiaMeses = garantiaMeses;
    }

    public int getIdCambio() { return idCambio; }
    public void setIdCambio(int idCambio) { this.idCambio = idCambio; }
    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }
    public int getIdMotorNuevo() { return idMotorNuevo; }
    public void setIdMotorNuevo(int idMotorNuevo) { this.idMotorNuevo = idMotorNuevo; }
    public String getNumeroSerieAnterior() { return numeroSerieAnterior; }
    public void setNumeroSerieAnterior(String numeroSerieAnterior) { this.numeroSerieAnterior = numeroSerieAnterior; }
    public String getDestinoMotorAnterior() { return destinoMotorAnterior; }
    public void setDestinoMotorAnterior(String destinoMotorAnterior) { this.destinoMotorAnterior = destinoMotorAnterior; }
    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
    public boolean isAutorizacionCliente() { return autorizacionCliente; }
    public void setAutorizacionCliente(boolean autorizacionCliente) { this.autorizacionCliente = autorizacionCliente; }
    public String getPruebasRealizadas() { return pruebasRealizadas; }
    public void setPruebasRealizadas(String pruebasRealizadas) { this.pruebasRealizadas = pruebasRealizadas; }
    public String getResultadoPruebas() { return resultadoPruebas; }
    public void setResultadoPruebas(String resultadoPruebas) { this.resultadoPruebas = resultadoPruebas; }
    public int getGarantiaMeses() { return garantiaMeses; }
    public void setGarantiaMeses(int garantiaMeses) { this.garantiaMeses = garantiaMeses; }
}
