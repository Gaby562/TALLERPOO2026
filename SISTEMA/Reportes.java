package SISTEMA;

import CLASES.Alerta;
import CLASES.EstacionMeteorologica;
import CLASES.Medicion;
import CLASES.Sensor;
import CLASES.ZonaGeografica;
import enums.NivelAlerta;
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

    // Busca alertas según el nivel, las fechas y la zona.
    // Si nivel o zona son null, no se aplica ese filtro.
    public List<Alerta> historialAlertas(NivelAlerta nivel, LocalDate desde, LocalDate hasta, ZonaGeografica zona) {

        // Creamos una lista vacía para guardar las alertas encontradas.
        List<Alerta> resultado = new ArrayList<>();

        // Recorremos todas las zonas del sistema.
        for (ZonaGeografica z : sistema.getZonas()) {

            // Si se eligió una zona y no coincide, la salteamos.
            if (zona != null && z != zona) {
                continue;
            }

            // Recorremos las alertas de cada zona.
            for (Alerta a : z.getAlertas()) {

                // Obtenemos la fecha en la que se emitió la alerta.
                LocalDate dia = a.getFechaHoraEmision().toLocalDate();

                // Verificamos si el nivel coincide con el solicitado.
                // Si nivel es null, acepta cualquier nivel.
                boolean cumpleNivel = (nivel == null || a.getNivel() == nivel);

                // Verificamos si la fecha está dentro del rango indicado.
                boolean cumpleFecha = !dia.isBefore(desde) && !dia.isAfter(hasta);

                // Si cumple con los dos filtros, agregamos la alerta.
                if (cumpleNivel && cumpleFecha) {
                    resultado.add(a);
                }
            }
        }

        // Ordenamos las alertas desde la más reciente a la más antigua.
        Collections.sort(resultado, new OrdenarAlertasPorFechaDesc());

        // Devolvemos la lista de alertas encontradas y ordenadas.
        return resultado;
    }
}

