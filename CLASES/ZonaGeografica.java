package CLASES;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import enums.NivelAlerta;
import enums.NivelVulnerabilidad;
import enums.TipoEventoClimatico;

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

    //constructor por defecto
    public ZonaGeografica() {
        this.idZona = 0;
        this.nombre = "";
        this.nivelVulnerabilidad = NivelVulnerabilidad.BAJO;
        // Umbrales por defecto que cumplen amarilla < naranja < roja
        setUmbrales(50, 100, 150);
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

    // Suma los mm de las últimas 24 h de todos los sensores de todas las estaciones de la zona    
    public double lluviaAcumulada24h() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime desde = ahora.minusHours(24);
        double total = 0;
        for (EstacionMeteorologica e : estaciones) {
            for (Sensor s : e.getSensores()) {
                for (Medicion m : s.getMediciones()) {
                    if (!m.getFechaHora().isBefore(desde)
                            && !m.getFechaHora().isAfter(ahora)) {
                        total += m.getMilimetros();
                    }
                }
            }
        }
        return total;
    }

    // Compara la lluvia acumulada con los umbrales y actualiza el nivel vigente
    // Si el nivel cambió y no es VERDE, emite una alerta de LLUVIA_COPIOSA
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

        // Solo se emite cuando el nivel cambia, para no repetir alertas
        if (cambio && nuevo != NivelAlerta.VERDE) {
            emitirAlerta(TipoEventoClimatico.LLUVIA_COPIOSA);
        }
        return nivelAlertaActual;
    }

    // Emite una alerta con el nivel vigente de la zona (caso LLUVIA_COPIOSA, automática)
    public Alerta emitirAlerta(TipoEventoClimatico ev) {
        return emitirAlerta(ev, nivelAlertaActual, "Lluvia acumulada en 24 h: " + lluviaAcumulada24h() + " mm");
    }

    // Emite una alerta con nivel y descripción indicados por un operador
    // (casos CRECIDA_RIO y TORMENTA_SEVERA)
    public Alerta emitirAlerta(TipoEventoClimatico ev, NivelAlerta nivel, String descripcion) {
        // El id es un número único: se usa el contador y después se incrementa
        int id = proximoIdAlerta++;
        Alerta a = new Alerta(id, LocalDateTime.now(), nivel, ev, descripcion);
        alertas.add(a);
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
