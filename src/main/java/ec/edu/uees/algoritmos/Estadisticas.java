/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modelo.Proceso;

/**
 *
 * @author judit
 */
public class Estadisticas {

    /*public static String calcular(List<Proceso> procesos) {
        int totalEspera = 0;
        int tiempoActual = 0;
        List<Integer> tiemposEspera = new ArrayList<>();

        for (Proceso p : procesos) {
            tiemposEspera.add(Math.max(0, tiempoActual - p.llegada));
            totalEspera += Math.max(0, tiempoActual - p.llegada);
            tiempoActual = Math.max(tiempoActual, p.llegada) + p.duracion;
        }

        double promedio = (double) totalEspera / procesos.size();
        double varianza = tiemposEspera.stream().mapToDouble(t -> Math.pow(t - promedio, 2)).sum() / procesos.size();
        double desviacion = Math.sqrt(varianza);

        return String.format("Promedio: %.2f, σ: %.2f", promedio, desviacion);
    }*/
    public static String calcularFCFS(List<Proceso> procesos) {
    int totalEspera = 0;
    int tiempoActual = 0;
    List<Integer> tiemposEspera = new ArrayList<>();

    for (Proceso p : procesos) {
        tiemposEspera.add(Math.max(0, tiempoActual - p.getLlegada()));
        totalEspera += Math.max(0, tiempoActual - p.getLlegada());
        tiempoActual = Math.max(tiempoActual, p.getLlegada()) + p.getDuracion();
    }

    double promedio = (double) totalEspera / procesos.size();
    double varianza = tiemposEspera.stream().mapToDouble(t -> Math.pow(t - promedio, 2)).sum() / procesos.size();
    double desviacion = Math.sqrt(varianza);

    return String.format("FCFS -> Promedio: %.2f, σ: %.2f", promedio, desviacion);
}

public static String calcularSRTF(List<Proceso> ejecucion) {
     Map<String, Integer> tiemposFinalizacion = new LinkedHashMap<>();
    Map<String, Integer> tiemposInicio = new HashMap<>();
    Map<String, Integer> tiemposTotales = new HashMap<>();

    // Recorrer la lista de ejecución paso a paso
    for (Proceso p : ejecucion) {
        // Si es la primera vez que se ejecuta, guardamos su tiempo de inicio
        tiemposInicio.putIfAbsent(p.getId(), p.getLlegada());

        // Guardamos el último tiempo en el que se ejecutó (su finalización real)
        tiemposFinalizacion.put(p.getId(), p.getLlegada() + p.getDuracion());

        // Acumulamos el tiempo total ejecutado de cada proceso
        tiemposTotales.put(p.getId(), tiemposTotales.getOrDefault(p.getId(), 0) + p.getDuracion());
    }

    int totalEspera = 0;
    List<Integer> tiemposEspera = new ArrayList<>();

    // Calcular el tiempo de espera de cada proceso
    for (String id : tiemposInicio.keySet()) {
        int espera = tiemposFinalizacion.get(id) - tiemposInicio.get(id) - tiemposTotales.get(id);
        espera = Math.max(0, espera); // Evitar tiempos negativos
        tiemposEspera.add(espera);
        totalEspera += espera;
    }

    // Calcular estadísticas
    double promedio = (double) totalEspera / tiemposInicio.size();
    double varianza = tiemposEspera.stream().mapToDouble(t -> Math.pow(t - promedio, 2)).sum() / tiemposInicio.size();
    double desviacion = Math.sqrt(varianza);

    return String.format("SRTF -> Promedio: %.2f, σ: %.2f", promedio, desviacion);
}

    
   public static String calcular(List<Proceso> procesosOriginales, List<Proceso> procesosEjecutados) {
    Map<String, Integer> tiemposFinalizacion = new HashMap<>();
    Map<String, Integer> tiemposLlegada = new HashMap<>();
    Map<String, Integer> tiemposEjecucion = new HashMap<>();

    for (Proceso p : procesosOriginales) {
        tiemposLlegada.put(p.id, p.llegada);
        tiemposEjecucion.put(p.id, p.duracion);
    }

    // Obtener tiempos de finalización
    for (Proceso p : procesosEjecutados) {
        tiemposFinalizacion.put(p.id, p.llegada + p.duracion);
    }

    double totalRetorno = 0, totalEspera = 0;
    List<Double> tiemposRetorno = new ArrayList<>();
    int n = procesosOriginales.size();

    for (Proceso p : procesosOriginales) {
        int tf = tiemposFinalizacion.get(p.id);
        int tr = tf - tiemposLlegada.get(p.id);
        int te = tr - tiemposEjecucion.get(p.id);

        totalRetorno += tr;
        totalEspera += te;
        tiemposRetorno.add((double) tr);
    }

    double promRetorno = totalRetorno / n;
    double promEspera = totalEspera / n;

    // Cálculo de desviación estándar del tiempo de retorno
    double sumatoriaVarianza = 0;
    for (double tr : tiemposRetorno) {
        sumatoriaVarianza += Math.pow(tr - promRetorno, 2);
    }
    double desviacionRetorno = Math.sqrt(sumatoriaVarianza / n);

    return String.format("TR: %.2f | TE: %.2f | σ: %.2f", promRetorno, promEspera, desviacionRetorno);
}



}
