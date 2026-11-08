/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;


public class Mecanico {
    private int idMecanico;
    private String nombre;
    private String telefono;
    private String especialidad;
    private String estado;

    public Mecanico() {}

    public Mecanico(int idMecanico, String nombre, String telefono,
        String especialidad, String estado) {
        this.idMecanico = idMecanico;
        this.nombre = nombre;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.estado = estado;
    }

    public int getIdMecanico() { return idMecanico; }
    public void setIdMecanico(int idMecanico) { this.idMecanico = idMecanico; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
