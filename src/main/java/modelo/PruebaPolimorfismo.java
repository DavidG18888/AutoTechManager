/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.List;

public class PruebaPolimorfismo {

    public static void main(String[] args) {

        List<ServicioBase> servicios = new ArrayList<>();

        servicios.add(new ServicioMantenimiento());
        servicios.add(new ServicioReparacion());
        servicios.add(new ServicioDiagnostico());
        servicios.add(new ServicioCambioMotor());

        for (ServicioBase servicio : servicios) {

            System.out.println(
                "Tipo: " + servicio.obtenerTipoServicio()
            );

            System.out.println(
                "Costo: L " + servicio.calcularCostoEstimado()
            );

            System.out.println("----------------------");
        }
    }
}