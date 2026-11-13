package CLASES;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import enums.*;

/**
 * Zona geográfica. Agrupa estaciones 
 * compone sus valores históricos mensuales, define tres umbrales de lluvia
 * acumulada en 24 horas y emite alertas según el riesgo.
 */
public class ZonaGeografica {
    private int idZona;
    private String nombre;
    private NivelVulnerabilidad nivelVulnerabilidad;
    private double umbralAmarilla;
    private double umbralNaranja;
    private double umbralRoja;
    private NivelAlerta nivelAlertaActual;

    /** Relaciones. */
    private List<EstacionMeteorologica> estaciones;   
    private List<ValorHistorico> valoresHistoricos;   
    private List<Alerta> alertas;                     

    /** Contador compartido por todas las zonas: cada alerta recibe un id único. */
    private static int proximoIdAlerta = 1;

    /** Umbral amarillo de referencia de la cátedra (50 mm en 24 h). */
    public static final double UMBRAL_AMARILLA_REF = 50;

    /** Umbral naranja de referencia de la cátedra (100 mm en 24 h). */
    public static final double UMBRAL_NARANJA_REF = 100;

    /** Umbral rojo de referencia de la cátedra (150 mm en 24 h). */
    public static final double UMBRAL_ROJA_REF = 150;

    /** Constructor sin parametros. Inicializa con valores por defecto.*/
    public ZonaGeografica() {
        this.idZona = 0;
        this.nombre = "";
        this.nivelVulnerabilidad = NivelVulnerabilidad.BAJO;
        setUmbrales(UMBRAL_AMARILLA_REF, UMBRAL_NARANJA_REF, UMBRAL_ROJA_REF);
        this.nivelAlertaActual = NivelAlerta.VERDE;
        this.estaciones = new ArrayList<>();
        this.valoresHistoricos = new ArrayList<>();
        this.alertas = new ArrayList<>();
    }

    /**
     * Crea una zona con nivel de alerta VERDE y sin estaciones.
     *
     * @param idZona identificador de la zona
     * @param nombre nombre de la zona
     * @param nivelVulnerabilidad nivel de vulnerabilidad climática
     * @param umbralAmarilla umbral amarillo en mm acumulados en 24 h
     * @param umbralNaranja umbral naranja en mm acumulados en 24 h
     * @param umbralRoja umbral rojo en mm acumulados en 24 h
     * @throws IllegalArgumentException si los umbrales no cumplen amarilla &lt; naranja &lt; roja
     */
    public ZonaGeografica(int idZona, String nombre, NivelVulnerabilidad nivelVulnerabilidad, double umbralAmarilla, double umbralNaranja, double umbralRoja) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.nivelVulnerabilidad = nivelVulnerabilidad;
        setUmbrales(umbralAmarilla, umbralNaranja, umbralRoja);
        this.nivelAlertaActual = NivelAlerta.VERDE;
        this.estaciones = new ArrayList<>();
        this.valoresHistoricos = new ArrayList<>();
        this.alertas = new ArrayList<>();
    }

    /** Devuelve el identificador de la zona. */
    public int getIdZona() {
        return idZona;
    }

    /** Establece el identificador de la zona. */
    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    /** Devuelve el nombre de la zona. */
    public String getNombre() {
        return nombre;
    }

    /** Establece el nombre de la zona. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Devuelve el nivel de vulnerabilidad climática. */
    public NivelVulnerabilidad getNivelVulnerabilidad() {
        return nivelVulnerabilidad;
    }

    /** Establece el nivel de vulnerabilidad climática. */
    public void setNivelVulnerabilidad(NivelVulnerabilidad nivelVulnerabilidad) {
        this.nivelVulnerabilidad = nivelVulnerabilidad;
    }

    /** Devuelve el umbral amarillo en mm acumulados en 24 h. */
    public double getUmbralAmarilla() {
        return umbralAmarilla;
    }

    /** Devuelve el umbral naranja en mm acumulados en 24 h. */
    public double getUmbralNaranja() {
        return umbralNaranja;
    }

    /** Devuelve el umbral rojo en mm acumulados en 24 h. */
    public double getUmbralRoja() {
        return umbralRoja;
    }

    /**
     * Cambia los tres umbrales a la vez, validando el orden.
     * @param amarilla umbral amarillo en mm
     * @param naranja umbral naranja en mm
     * @param roja umbral rojo en mm
     * @throws IllegalArgumentException si no se cumple amarilla &lt; naranja &lt; roja
     */
    public void setUmbrales(double amarilla, double naranja, double roja) throws IllegalArgumentException{
        if (!(amarilla < naranja && naranja < roja)) {
            throw new IllegalArgumentException(
                "Los umbrales deben cumplir: amarilla < naranja < roja.");
        }
        this.umbralAmarilla = amarilla;
        this.umbralNaranja = naranja;
        this.umbralRoja = roja;
    }

    /** Devuelve el nivel de alerta vigente de la zona. */
    public NivelAlerta getNivelAlertaActual() {
        return nivelAlertaActual;
    }

    /** Establece el nivel de alerta vigente de la zona. */
    public void setNivelAlertaActual(NivelAlerta nivelAlertaActual) {
        this.nivelAlertaActual = nivelAlertaActual;
    }

    /** Devuelve las estaciones que pertenecen a la zona. */
    public List<EstacionMeteorologica> getEstaciones() {
        return estaciones;
    }

    /** Reemplaza la lista de estaciones de la zona. */
    public void setEstaciones(List<EstacionMeteorologica> estaciones) {
        this.estaciones = estaciones;
    }

    /** Devuelve los valores históricos mensuales de la zona. */
    public List<ValorHistorico> getValoresHistoricos() {
        return valoresHistoricos;
    }

    /** Reemplaza la lista de valores históricos. */
    public void setValoresHistoricos(List<ValorHistorico> valoresHistoricos) {
        this.valoresHistoricos = valoresHistoricos;
    }

    /** Devuelve las alertas emitidas por la zona. */
    public List<Alerta> getAlertas() {
        return alertas;
    }

    /** Reemplaza la lista de alertas de la zona. */
    public void setAlertas(List<Alerta> alertas) {
        this.alertas = alertas;
    }

    /**
     * Agrega una estación a la zona (agregación: la estación se crea afuera).
     * No hace nada si es null o si ya pertenece a la zona.
     * @param e estación a agregar
     */
    public void agregarEstacion(EstacionMeteorologica e) {
        if (e != null && !estaciones.contains(e)) {
            estaciones.add(e);
        }
    }

    /**
     * Crea un valor histórico y lo agrega a la zona (composición).
     * @param mes mes del año, de 1 a 12
     * @param acumuladoMedio acumulado medio de precipitación en mm
     * @throws IllegalArgumentException si el mes o el acumulado no son válidos
     */
    public void agregarValorHistorico(int mes, double acumuladoMedio) {
        ValorHistorico v = new ValorHistorico(mes, acumuladoMedio);
        valoresHistoricos.add(v);
    }

    /**
     * Suma los milímetros registrados en las últimas 24 horas por todos los
     * sensores de todas las estaciones de la zona.
     * @return milímetros acumulados en las últimas 24 horas
     */
    public double lluviaAcumulada24h() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime desde = ahora.minusHours(24);
        double total = 0;

        for (EstacionMeteorologica e : estaciones) {
            for (Sensor s : e.getSensores()) {
                for (Medicion m : s.getMediciones()) {
                    if (!m.getFechaHora().isBefore(desde) && !m.getFechaHora().isAfter(ahora)) {
                        total += m.getMilimetros();
                    }
                }
            }
        }
        return total;
    }

    /**
     * Compara la lluvia acumulada en 24 horas con los umbrales de la zona,
     * actualiza su nivel de alerta vigente y, si el nivel cambió y no es
     * VERDE, emite una alerta automática de tipo LLUVIA_COPIOSA.
     *
     * @return el nivel de alerta vigente después de la evaluación
     */
    public NivelAlerta evaluarRiesgo() {
        double lluvia = lluviaAcumulada24h();
        NivelAlerta nuevo;

        if (lluvia >= umbralRoja) {
            nuevo = NivelAlerta.ROJA;

        } else if (lluvia >= umbralNaranja) {
            nuevo = NivelAlerta.NARANJA;

        } else if (lluvia >= umbralAmarilla) {
            nuevo = NivelAlerta.AMARILLA;

        } else {
            nuevo = NivelAlerta.VERDE;
        }
        boolean cambio = nuevo != nivelAlertaActual;
        nivelAlertaActual = nuevo;
        if (cambio && nuevo != NivelAlerta.VERDE) {

            // Emite una alerta de tipo LLUVIA_COPIOSA
            emitirAlerta(TipoEventoClimatico.LLUVIA_COPIOSA);
        }
        return nivelAlertaActual;
    }

    /**
     * Emite una alerta usando el nivel actual de la zona y una descripción
     * con la lluvia acumulada en 24 horas.
     * @param ev tipo de evento climático
     * @return la alerta creada
     */
    public Alerta emitirAlerta(TipoEventoClimatico ev) {
        return emitirAlerta(
            ev,
            nivelAlertaActual,
            "Lluvia acumulada en 24 h: " + lluviaAcumulada24h() + " mm"
        );
    }

    /**
     * Emite una alerta con el nivel y la descripción indicados y la agrega
     * a la lista de alertas de la zona. Cada alerta recibe un id único.
     * @param ev tipo de evento climático
     * @param nivel nivel de severidad
     * @param descripcion descripción de la alerta
     * @return la alerta creada
     */
    public Alerta emitirAlerta(TipoEventoClimatico ev, NivelAlerta nivel, String descripcion) {
        /**Obtiene el próximo identificador disponible y después incrementa el contador.*/
        int id = proximoIdAlerta++;
        Alerta a = new Alerta(id, LocalDateTime.now(), nivel, ev, descripcion);
        alertas.add(a);
        return a;
    }

    /** Devuelve los datos de la zona en formato de texto. */
    @Override
    public String toString() {
        return "ZONA GEOGRÁFICA\n" +
            "ID de la zona: " + idZona + "\n" +
            "Nombre: " + nombre + "\n" +
            "Vulnerabilidad: " + nivelVulnerabilidad + "\n" +
            "Umbral amarilla: " + umbralAmarilla + " mm\n" +
            "Umbral naranja: " + umbralNaranja + " mm\n" +
            "Umbral roja: " + umbralRoja + " mm\n" +
            "Nivel de alerta actual: " + nivelAlertaActual + "\n" +
            "Cantidad de estaciones: " + estaciones.size() + "\n" +
            "Cantidad de alertas emitidas: " + alertas.size();
    }
}
