/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo;

public class Repuesto {
    private int idRepuesto;
    private String codigo;
    private String nombre;
    private String descripcion;
    private int idProveedor;
    private int existencia;
    private int stockMinimo;
    private double costo;

    public Repuesto() {}

    public Repuesto(int idRepuesto, String codigo, String nombre,
        String descripcion, int idProveedor, int existencia,
        int stockMinimo, double costo) {
        this.idRepuesto = idRepuesto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idProveedor = idProveedor;
        this.existencia = existencia;
        this.stockMinimo = stockMinimo;
        this.costo = costo;
    }

    public int getIdRepuesto() { return idRepuesto; }
    public void setIdRepuesto(int idRepuesto) { this.idRepuesto = idRepuesto; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }
    public int getExistencia() { return existencia; }
    public void setExistencia(int existencia) { this.existencia = existencia; }
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
}
