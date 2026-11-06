package CLASES;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import enums.*;

public class ZonaGeografica {
    private int idZona;
    private String nombre;
    private NivelVulnerabilidad nivelVulnerabilidad;
    private double umbralAmarilla;
    private double umbralNaranja;
    private double umbralRoja;
    private NivelAlerta nivelAlertaActual;

    // Relaciones
    private List<EstacionMeteorologica> estaciones;   // agregación, 0..*
    private List<ValorHistorico> valoresHistoricos;   // composición
    private List<Alerta> alertas;                     // asociación "emite", 0..*

    // Contador compartido por todas las zonas: cada alerta recibe un id único
    private static int proximoIdAlerta = 1;

    public static final double UMBRAL_AMARILLA_REF = 50;
    public static final double UMBRAL_NARANJA_REF = 100;
    public static final double UMBRAL_ROJA_REF = 150;

    //constructor por defecto
    public ZonaGeografica() {
        this.idZona = 0;
        this.nombre = "";
        this.nivelVulnerabilidad = NivelVulnerabilidad.BAJO;
        // Umbrales por defecto que cumplen amarilla < naranja < roja
        setUmbrales(UMBRAL_AMARILLA_REF, UMBRAL_NARANJA_REF, UMBRAL_ROJA_REF);
        this.nivelAlertaActual = NivelAlerta.VERDE;
        this.estaciones = new ArrayList<>();
        this.valoresHistoricos = new ArrayList<>();
        this.alertas = new ArrayList<>();
    }

    public ZonaGeografica(int idZona, String nombre, NivelVulnerabilidad nivelVulnerabilidad, double umbralAmarilla, double umbralNaranja, double umbralRoja) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.nivelVulnerabilidad = nivelVulnerabilidad;
        // La validación (amarilla < naranja < roja) la hace setUmbrales
        setUmbrales(umbralAmarilla, umbralNaranja, umbralRoja);
        this.nivelAlertaActual = NivelAlerta.VERDE;
        this.estaciones = new ArrayList<>();
        this.valoresHistoricos = new ArrayList<>();
        this.alertas = new ArrayList<>();
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public NivelVulnerabilidad getNivelVulnerabilidad() {
        return nivelVulnerabilidad;
    }

    public void setNivelVulnerabilidad(NivelVulnerabilidad nivelVulnerabilidad) {
        this.nivelVulnerabilidad = nivelVulnerabilidad;
    }

    public double getUmbralAmarilla() {
        return umbralAmarilla;
    }

    public double getUmbralNaranja() {
        return umbralNaranja;
    }

    public double getUmbralRoja() {
        return umbralRoja;
    }

    // Cambia los tres umbrales a la vez, validando el orden
    public void setUmbrales(double amarilla, double naranja, double roja) throws IllegalArgumentException{
        if (!(amarilla < naranja && naranja < roja)) {
            throw new IllegalArgumentException(
                "Los umbrales deben cumplir: amarilla < naranja < roja.");
        }
        this.umbralAmarilla = amarilla;
        this.umbralNaranja = naranja;
        this.umbralRoja = roja;
    }
    
    public NivelAlerta getNivelAlertaActual() {
        return nivelAlertaActual;
    }

    public void setNivelAlertaActual(NivelAlerta nivelAlertaActual) {
        this.nivelAlertaActual = nivelAlertaActual;
    }

    public List<EstacionMeteorologica> getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(List<EstacionMeteorologica> estaciones) {
        this.estaciones = estaciones;
    }

    public List<ValorHistorico> getValoresHistoricos() {
        return valoresHistoricos;
    }

    public void setValoresHistoricos(List<ValorHistorico> valoresHistoricos) {
        this.valoresHistoricos = valoresHistoricos;
    }

    public List<Alerta> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<Alerta> alertas) {
        this.alertas = alertas;
    }

    
    // Agregación: la estación se crea afuera y se recibe ya armada
    public void agregarEstacion(EstacionMeteorologica e) {
        if (e != null && !estaciones.contains(e)) {
            estaciones.add(e);
        }
    }

    // Composición: la zona crea el ValorHistorico adentro
    public void agregarValorHistorico(int mes, double acumuladoMedio) {
        ValorHistorico v = new ValorHistorico(mes, acumuladoMedio);
        valoresHistoricos.add(v);
    }

    // Suma los milímetros de lluvia registrados en las últimas 24 horas
    public double lluviaAcumulada24h() {

        // Obtiene la fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();

        // Calcula la fecha y hora de hace 24 horas
        LocalDateTime desde = ahora.minusHours(24);

        // Variable que acumula los milímetros de lluvia
        double total = 0;

        // Recorre todas las estaciones que pertenecen a la zona
        for (EstacionMeteorologica e : estaciones) {

            // Recorre todos los sensores instalados en cada estación
            for (Sensor s : e.getSensores()) {

                // Recorre todas las mediciones registradas por cada sensor
                for (Medicion m : s.getMediciones()) {

                    // Comprueba si la medición está dentro de las últimas 24 horas
                    if (!m.getFechaHora().isBefore(desde)
                            && !m.getFechaHora().isAfter(ahora)) {

                        // Si está dentro del período, suma sus milímetros al total
                        total += m.getMilimetros();
                    }
                }
            }
        }

        // Devuelve el total de milímetros acumulados en las últimas 24 horas
        return total;
    }

    // Compara la lluvia acumulada con los umbrales y actualiza el nivel de alerta
    public NivelAlerta evaluarRiesgo() {

        // Obtiene los milímetros acumulados durante las últimas 24 horas
        double lluvia = lluviaAcumulada24h();

        // Variable donde se guarda el nuevo nivel de alerta
        NivelAlerta nuevo;

        // Si la lluvia alcanza o supera el umbral rojo, el nivel será ROJO
        if (lluvia >= umbralRoja) {
            nuevo = NivelAlerta.ROJA;

        // Si no llegó al rojo, pero alcanza el umbral naranja, será NARANJA
        } else if (lluvia >= umbralNaranja) {
            nuevo = NivelAlerta.NARANJA;

        // Si no llegó al naranja, pero alcanza el umbral amarillo, será AMARILLO
        } else if (lluvia >= umbralAmarilla) {
            nuevo = NivelAlerta.AMARILLA;

        // Si no alcanza ninguno de los umbrales, el nivel será VERDE
        } else {
            nuevo = NivelAlerta.VERDE;
        }

        // Comprueba si el nuevo nivel es diferente al nivel de alerta anterior
        boolean cambio = nuevo != nivelAlertaActual;

        // Actualiza el nivel de alerta actual de la zona
        nivelAlertaActual = nuevo;

        // Si cambió el nivel y no es VERDE, genera una alerta automática
        if (cambio && nuevo != NivelAlerta.VERDE) {

            // Emite una alerta de tipo LLUVIA_COPIOSA
            emitirAlerta(TipoEventoClimatico.LLUVIA_COPIOSA);
        }

        // Devuelve el nivel de alerta actual de la zona
        return nivelAlertaActual;
    }

    // Emite una alerta utilizando el nivel actual de la zona
    public Alerta emitirAlerta(TipoEventoClimatico ev) {

        // Llama al otro método emitirAlerta enviándole:
        // el tipo de evento, el nivel actual y una descripción de la lluvia acumulada
        return emitirAlerta(
            ev,
            nivelAlertaActual,
            "Lluvia acumulada en 24 h: " + lluviaAcumulada24h() + " mm"
        );
    }

    // Emite una alerta con el nivel y la descripción indicados
    public Alerta emitirAlerta(TipoEventoClimatico ev, NivelAlerta nivel, String descripcion) {

        // Obtiene el próximo identificador disponible y después incrementa el contador
        int id = proximoIdAlerta++;

        // Crea una nueva alerta con su ID, fecha y hora, nivel, evento y descripción
        Alerta a = new Alerta(id, LocalDateTime.now(), nivel, ev, descripcion);

        // Agrega la alerta creada a la lista de alertas de la zona
        alertas.add(a);

        // Devuelve la alerta creada
        return a;
    }

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
