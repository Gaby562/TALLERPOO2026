package SISTEMA;

import CLASES.EstacionMeteorologica;
import CLASES.Medicion;
import CLASES.Sensor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reportes {
    private SistemaMeteorologico sistema;

    public Reportes(SistemaMeteorologico sistema) {
        this.sistema = sistema;
    }

    // Calcula los milímetros acumulados de una estación, entre dos fechas.
    public double acumuladoEstacion(EstacionMeteorologica e, LocalDate desde, LocalDate hasta) {
        // Variable donde sumamos los milímetros.
        double total = 0;
        // Recorremos los sensores de la estación.
        for (Sensor s : e.getSensores()) {
            // Recorremos las mediciones de cada sensor.
            for (Medicion m : s.getMediciones()) {
                // Obtenemos la fecha de la medición.
                LocalDate dia = m.getFechaHora().toLocalDate();
                // Verificamos que la fecha esté dentro del rango.
                if (!dia.isBefore(desde) && !dia.isAfter(hasta)) {
                    // Sumamos los milímetros de esa medición.
                    total += m.getMilimetros();
                }
            }
        }
        // Devolvemos el total acumulado.
        return total;
    }

    // Crea un ranking de estaciones de mayor a menor acumulado.
    public List<PuestoRanking> rankingEstaciones(LocalDate desde, LocalDate hasta) {
        // Creamos una lista vacía para guardar el ranking.
        List<PuestoRanking> ranking = new ArrayList<>();
        // Recorremos todas las estaciones del sistema.
        for (EstacionMeteorologica e : sistema.getEstaciones()) {
            // Calculamos el acumulado de cada estación y lo agregamos a la lista.
            ranking.add(new PuestoRanking(e, acumuladoEstacion(e, desde, hasta)));
        }
        // Ordenamos el ranking de mayor a menor acumulado.
        Collections.sort(ranking, new OrdenarPorAcumuladoDesc());
        // Devolvemos el ranking ordenado.
        return ranking;
    }
}

