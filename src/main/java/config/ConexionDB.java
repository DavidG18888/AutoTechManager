/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL =
            "jdbc:sqlserver://127.0.0.1:1433;"
            + "databaseName=AutoTechDB;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    private static final String USER = "sa";
    private static final String PASSWORD = "Davidgomez188";

    private static Connection conexion = null;

    private ConexionDB() {
    }

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {

                Class.forName(
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver"
                );

                conexion = DriverManager.getConnection(
                    URL, USER, PASSWORD
                );

                System.out.println(
                    "--> Conexion exitosa a SQL Server"
                );
            }

        } catch (ClassNotFoundException e) {
            System.err.println(
                "No se encontro el driver de SQL Server: "
                + e.getMessage()
            );

        } catch (SQLException e) {
            System.err.println(
                "Error al conectar con SQL: "
                + e.getMessage()
            );
        }

        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                conexion = null;

                System.out.println(
                    "--> Conexion con SQL cerrada"
                );
            }

        } catch (SQLException e) {
            System.err.println(
                "Error al cerrar la conexion: "
                + e.getMessage()
            );
        }
    }
}
