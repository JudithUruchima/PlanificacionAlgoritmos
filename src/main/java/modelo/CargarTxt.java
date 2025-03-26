/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelo.Proceso;

/**
 *
 * @author judit
 */
public class CargarTxt {

    public static List<Proceso> cargarProcesos(File file) throws IOException {
        List<Proceso> procesos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        String id = parts[0].trim();
                        int llegada = Integer.parseInt(parts[1].trim());
                        int duracion = Integer.parseInt(parts[2].trim());
                        procesos.add(new Proceso(id, llegada, duracion));
                    } catch (NumberFormatException e) {
                        System.out.println("Error en línea: " + line + " (valores no numéricos)");
                    }
                } else {
                    System.out.println("Línea ignorada: " + line + " (formato incorrecto)");
                }
            }
        }
        return procesos;
    }

}
