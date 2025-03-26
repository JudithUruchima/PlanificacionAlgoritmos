/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import modelo.Proceso;

/**
 *
 * @author judit
 */
public class ShortTermPredict {
//me rindo

    public static List<Proceso> planificar(List<Proceso> procesos) {
       // Ordenar por tiempo de llegada
    procesos.sort(Comparator.comparingInt(Proceso::getLlegada));

    // Mapa con el tiempo restante de cada proceso
    Map<String, Integer> tiempoRestante = new HashMap<>();
    for (Proceso p : procesos) {
        tiempoRestante.put(p.getId(), p.getDuracion());
    }

    PriorityQueue<Proceso> cola = new PriorityQueue<>(Comparator.comparingInt(p -> tiempoRestante.get(p.getId())));
    int tiempoActual = 0, index = 0;
    List<Proceso> ordenEjecucion = new ArrayList<>();
    Proceso procesoActual = null;

    while (!cola.isEmpty() || index < procesos.size()) {
        // Agregar procesos que han llegado
        while (index < procesos.size() && procesos.get(index).getLlegada() <= tiempoActual) {
            cola.add(procesos.get(index));
            index++;
        }

        if (!cola.isEmpty()) {
            // Seleccionamos el proceso con menor tiempo restante
            procesoActual = cola.poll();
            String idProceso = procesoActual.getId();

            // Ejecutamos durante 1 unidad de tiempo
            tiempoRestante.put(idProceso, tiempoRestante.get(idProceso) - 1);
            ordenEjecucion.add(new Proceso(idProceso, tiempoActual, 1)); // Guardar cada ejecución

            tiempoActual++; // Avanzar el tiempo

            // Si el proceso aún tiene tiempo restante, lo volvemos a agregar
            if (tiempoRestante.get(idProceso) > 0) {
                cola.add(procesoActual);
            } else {
                tiempoRestante.remove(idProceso);
            }
        } else {
            tiempoActual++; // No hay procesos listos, avanzamos el tiempo
        }
    }

    return ordenEjecucion; // Retornar toda la ejecución paso a paso
    }

}
