/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo;

public class Motor {
    private int idMotor;
    private String numeroSerie;
    private String marca;
    private String modelo;
    private int anio;
    private String tipoMotor;
    private double cilindrada;
    private String compatibleMarca;
    private String compatibleModelo;
    private int compatibleAnio;
    private double costo;
    private String estado;

    public Motor() {}

    public Motor(int idMotor, String numeroSerie, String marca,
        String modelo, int anio, String tipoMotor,
        double cilindrada, String compatibleMarca,
        String compatibleModelo, int compatibleAnio,
        double costo, String estado) {
        this.idMotor = idMotor;
        this.numeroSerie = numeroSerie;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.tipoMotor = tipoMotor;
        this.cilindrada = cilindrada;
        this.compatibleMarca = compatibleMarca;
        this.compatibleModelo = compatibleModelo;
        this.compatibleAnio = compatibleAnio;
        this.costo = costo;
        this.estado = estado;
    }

    public int getIdMotor() { return idMotor; }
    public void setIdMotor(int idMotor) { this.idMotor = idMotor; }
    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public String getTipoMotor() { return tipoMotor; }
    public void setTipoMotor(String tipoMotor) { this.tipoMotor = tipoMotor; }
    public double getCilindrada() { return cilindrada;}
    public void setCilindrada(double cilindrada) { this.cilindrada = cilindrada; }
    public String getCompatibleMarca() { return compatibleMarca; }
    public void setCompatibleMarca(String compatibleMarca) { this.compatibleMarca = compatibleMarca; }
    public String getCompatibleModelo() { return compatibleModelo; }
    public void setCompatibleModelo(String compatibleModelo) { this.compatibleModelo = compatibleModelo; }
    public int getCompatibleAnio() { return compatibleAnio; }
    public void setCompatibleAnio(int compatibleAnio) { this.compatibleAnio = compatibleAnio; }
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
