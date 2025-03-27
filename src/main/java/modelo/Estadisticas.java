package modelo;

import java.util.*;

/**
 * Clase para calcular estadísticas de planificación de procesos.
 */
public class Estadisticas {

    /**
     * Calcula el tiempo de espera promedio y la desviación estándar usando FCFS.
     */
    public static String calcularFCFS(List<Proceso> procesos) {
        int totalEspera = 0;
        int tiempoActual = 0;
        List<Integer> tiemposEspera = new ArrayList<>();

        // Ordenamos los procesos por orden de llegada
        procesos.sort(Comparator.comparingInt(Proceso::getLlegada));

        for (Proceso p : procesos) {
            int espera = Math.max(0, tiempoActual - p.getLlegada());
            tiemposEspera.add(espera);
            totalEspera += espera;
            tiempoActual = Math.max(tiempoActual, p.getLlegada()) + p.getDuracion();
        }

        return calcularEstadisticas("FCFS", tiemposEspera, totalEspera, procesos.size());
    }

    /**
     * Calcula las estadísticas usando SRTF.
     */
    public static String calcularSRTF(List<Proceso> ejecucion) {
        Map<String, Integer> tiemposFinalizacion = new LinkedHashMap<>();
        Map<String, Integer> tiemposInicio = new HashMap<>();
        Map<String, Integer> tiemposTotales = new HashMap<>();

        // Recorrer la ejecución paso a paso
        for (Proceso p : ejecucion) {
            tiemposInicio.putIfAbsent(p.getId(), p.getLlegada()); // Guardamos el primer inicio
            tiemposFinalizacion.put(p.getId(), p.getLlegada() + p.getDuracion());
            tiemposTotales.put(p.getId(), tiemposTotales.getOrDefault(p.getId(), 0) + p.getDuracion());
        }

        int totalEspera = 0;
        List<Integer> tiemposEspera = new ArrayList<>();

        // Calcular el tiempo de espera de cada proceso
        for (String id : tiemposInicio.keySet()) {
            int espera = tiemposFinalizacion.get(id) - tiemposInicio.get(id) - tiemposTotales.get(id);
            espera = Math.max(0, espera);
            tiemposEspera.add(espera);
            totalEspera += espera;
        }

        return calcularEstadisticas("STP", tiemposEspera, totalEspera, tiemposInicio.size());
    }

    /**
     * Calcula estadísticas generales de tiempo de retorno y espera.
     */
    public static String calcular(List<Proceso> procesosOriginales, List<Proceso> procesosEjecutados) {
        Map<String, Integer> tiemposFinalizacion = new HashMap<>();
        Map<String, Integer> tiemposLlegada = new HashMap<>();
        Map<String, Integer> tiemposEjecucion = new HashMap<>();

        // Guardar datos de procesos originales
        for (Proceso p : procesosOriginales) {
            tiemposLlegada.put(p.getId(), p.getLlegada());
            tiemposEjecucion.put(p.getId(), p.getDuracion());
        }

        // Obtener tiempos de finalización (última ejecución de cada proceso)
        for (Proceso p : procesosEjecutados) {
            tiemposFinalizacion.put(p.getId(), p.getLlegada() + p.getDuracion());
        }

        double totalRetorno = 0, totalEspera = 0;
        List<Double> tiemposRetorno = new ArrayList<>();
        int n = procesosOriginales.size();

        for (Proceso p : procesosOriginales) {
            int tf = tiemposFinalizacion.getOrDefault(p.getId(), 0);
            int tr = tf - tiemposLlegada.get(p.getId());
            int te = tr - tiemposEjecucion.get(p.getId());

            totalRetorno += tr;
            totalEspera += te;
            tiemposRetorno.add((double) tr);
        }

        return calcularEstadisticasRetorno(totalRetorno, totalEspera, tiemposRetorno, n);
    }

    /**
     * Método auxiliar para calcular estadísticas (Promedio y Desviación Estándar).
     */
    private static String calcularEstadisticas(String metodo, List<Integer> tiempos, int total, int n) {
        double promedio = (double) total / n;
        double varianza = tiempos.stream().mapToDouble(t -> Math.pow(t - promedio, 2)).sum() / n;
        double desviacion = Math.sqrt(varianza);

        return String.format("%s -> Promedio: %.2f, σ: %.2f", metodo, promedio, desviacion);
    }

    /**
     * Método auxiliar para calcular estadísticas de tiempo de retorno.
     */
    private static String calcularEstadisticasRetorno(double totalRetorno, double totalEspera, List<Double> tiemposRetorno, int n) {
        double promRetorno = totalRetorno / n;
        double promEspera = totalEspera / n;

        // Cálculo de desviación estándar del tiempo de retorno
        double sumatoriaVarianza = tiemposRetorno.stream().mapToDouble(tr -> Math.pow(tr - promRetorno, 2)).sum();
        double desviacionRetorno = Math.sqrt(sumatoriaVarianza / n);

        return String.format("TR: %.2f | TE: %.2f | σ: %.2f", promRetorno, promEspera, desviacionRetorno);
    }
}