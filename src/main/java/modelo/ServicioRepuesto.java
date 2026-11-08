/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class ServicioRepuesto {

    private int idServicioRepuesto;
    private int idServicio;
    private int idRepuesto;
    private int cantidad;

    public int getIdServicioRepuesto() {
        return idServicioRepuesto;
    }

    public void setIdServicioRepuesto(int idServicioRepuesto) {
        this.idServicioRepuesto = idServicioRepuesto;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
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
}