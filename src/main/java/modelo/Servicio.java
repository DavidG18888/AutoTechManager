/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package modelo;

public class Servicio extends ServicioBase {

    private int idServicio;
    private String codigo;
    private String nombre;
    private String descripcion;
    private int nivel;
    private double costoManoObra;
    private double tiempoEstimado;
    private String estado;

    public Servicio() {}

    public Servicio(int idServicio, String codigo, String nombre,
            String descripcion, int nivel, double costoManoObra,
            double tiempoEstimado, String estado) {

        this.idServicio = idServicio;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.costoManoObra = costoManoObra;
        this.tiempoEstimado = tiempoEstimado;
        this.estado = estado;
    }

    @Override
    public String obtenerTipoServicio() {
        return "GENERAL";
    }

    @Override
    public double calcularCostoEstimado() {
        return costoManoObra;
    }

    public int getIdServicio() { return idServicio; }
    public void setIdServicio(int idServicio) { this.idServicio = idServicio; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }

    public double getCostoManoObra() { return costoManoObra; }
    public void setCostoManoObra(double costoManoObra) {
        this.costoManoObra = costoManoObra;
    }

    public double getTiempoEstimado() { return tiempoEstimado; }
    public void setTiempoEstimado(double tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
