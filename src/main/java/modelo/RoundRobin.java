/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import modelo.Proceso;

/**
 *
 * @author judit
 */
public class RoundRobin {

    public static List<Proceso> planificar(List<Proceso> procesos, int quantum) {
        Queue<Proceso> cola = new LinkedList<>();
        List<Proceso> resultado = new ArrayList<>();
        Map<String, Integer> tiempoFinalizacion = new HashMap<>();
        Map<String, Integer> tiempoEjecucion = new HashMap<>();
        int tiempoActual = 0;

        // Copiamos los procesos originales con su tiempo restante
        for (Proceso p : procesos) {
            cola.add(new Proceso(p.id, p.llegada, p.duracion));
            tiempoEjecucion.put(p.id, p.duracion); // Guardamos la duración original
        }

        while (!cola.isEmpty()) {
            Proceso p = cola.poll();

            if (p.duracion > quantum) {
                resultado.add(new Proceso(p.id, tiempoActual, quantum));
                tiempoActual += quantum;
                p.duracion -= quantum;
                cola.add(p); // Volver a meter en la cola con el tiempo restante
            } else {
                resultado.add(new Proceso(p.id, tiempoActual, p.duracion));
                tiempoActual += p.duracion;
                tiempoFinalizacion.put(p.id, tiempoActual); // Guardar tiempo de finalización
            }
        }

        return resultado;
    }

}
