package SISTEMA;

import CLASES.Alerta;
import CLASES.EstacionMeteorologica;
import CLASES.Medicion;
import CLASES.Sensor;
import CLASES.ValorHistorico;
import CLASES.ZonaGeografica;
import enums.NivelAlerta;
import java.time.LocalDate;
import java.time.YearMonth;
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

    //ESTADISTICA DE ANOMALIA CLIMATICA

    
    // Calcula la suma de las precipitaciones de todas las estaciones de una zona durante un mes
    public double acumuladoMensualZona(ZonaGeografica z, int anio, int mes) {

        // Verifica que el mes ingresado sea válido
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }

        // Obtiene el primer y último día del mes para establecer el período de consulta
        YearMonth periodo = YearMonth.of(anio, mes);
        LocalDate desde = periodo.atDay(1);
        LocalDate hasta = periodo.atEndOfMonth();

        double total = 0;

        // Recorre las estaciones de la zona y suma las precipitaciones del período
        for (EstacionMeteorologica e : z.getEstaciones()) {
            total += acumuladoEstacion(e, desde, hasta);
        }

        return total;
    }

    // Busca el registro histórico correspondiente al mes indicado dentro de una zona
    public ValorHistorico buscarValorHistorico(ZonaGeografica z, int mes) {

        // Recorre los valores históricos hasta encontrar el mes solicitado
        for (ValorHistorico v : z.getValoresHistoricos()) {
            if (v.getMes() == mes) {
                return v;
            }
        }

        // Devuelve null si no existe un registro para ese mes
        return null;
    }

    // Calcula el porcentaje de desviación respecto al promedio histórico usando el acumulado recibido
    public Double anomaliaMensual(double acumulado, ValorHistorico historico) {

        // No se puede calcular la anomalía si no existe un valor histórico o si es cero
        if (historico == null || historico.getAcumuladoMedio() == 0) {
            return null;
        }

        // Compara el acumulado real con el promedio histórico y calcula la diferencia porcentual
        return (acumulado - historico.getAcumuladoMedio()) / historico.getAcumuladoMedio() * 100;
    }

    // Genera un informe con las precipitaciones acumuladas y su desviación respecto al promedio histórico
    public String informeAnomalia(ZonaGeografica z, int anio, int mes) {

        // Calcula las precipitaciones acumuladas de la zona durante el mes
        double acumulado = acumuladoMensualZona(z, anio, mes);

        // Busca el promedio histórico correspondiente al mes
        ValorHistorico historico = buscarValorHistorico(z, mes);

        // Calcula la desviación utilizando los datos obtenidos anteriormente
        Double anomalia = anomaliaMensual(acumulado, historico);

        // Construye la información inicial del informe
        String texto = "ANOMALÍA CLIMÁTICA\n" +
            "Zona: " + z.getNombre() + "\n" +
            "Período: " + mes + "/" + anio + "\n" +
            "Acumulado del mes: " + acumulado + " mm\n";

        // Si no existe un valor histórico, informa que no se puede realizar la comparación
        if (historico == null) {
            return texto + "Valor histórico: no cargado para este mes";
        }

        // Agrega el promedio histórico al informe
        texto += "Valor histórico: " + historico.getAcumuladoMedio() + " mm\n";

        // Informa que no se pudo calcular la desviación porque el valor histórico es cero
        if (anomalia == null) {
            return texto + "Desviación: no calculable (el valor histórico es 0)";
        }

        // Determina si las precipitaciones estuvieron por encima, por debajo o iguales al promedio histórico
        String lectura = anomalia > 0 ? "por encima de lo histórico"
                    : anomalia < 0 ? "por debajo de lo histórico"
                    : "igual a lo histórico";

        // Agrega el porcentaje de desviación con un decimal y su interpretación
        return texto + "Desviación: " + String.format("%.1f", anomalia) + " % (" + lectura + ")";
    }


    //HISTORICO DE PRECIPITACIONES(DIARIO, SEMANAL Y MENSUAL)

    

}

