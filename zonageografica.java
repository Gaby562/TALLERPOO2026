import enums.NivelAlerta;
import enums.NivelVulnerabilidad;
import enums.TipoEventoClimatico;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ZonaGeografica {
    private String idZona;
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

    //constructor por defecto
    public ZonaGeografica() {
        this.nivelAlertaActual = NivelAlerta.VERDE;
        this.estaciones = new ArrayList<>();
        this.valoresHistoricos = new ArrayList<>();
        this.alertas = new ArrayList<>();
    }

    public ZonaGeografica(String idZona, String nombre, NivelVulnerabilidad nivelVulnerabilidad, double umbralAmarilla, double umbralNaranja, double umbralRoja)throws IllegalArgumentException{
        // Se valida antes de guardar: amarilla < naranja < roja
        if (!(umbralAmarilla < umbralNaranja && umbralNaranja < umbralRoja)) {
            throw new IllegalArgumentException("Los umbrales deben cumplir: amarilla < naranja < roja.");
        }
        this.idZona = idZona;
        this.nombre = nombre;
        this.nivelVulnerabilidad = nivelVulnerabilidad;
        this.umbralAmarilla = umbralAmarilla;
        this.umbralNaranja = umbralNaranja;
        this.umbralRoja = umbralRoja;
        this.nivelAlertaActual = NivelAlerta.VERDE;
        this.estaciones = new ArrayList<>();
        this.valoresHistoricos = new ArrayList<>();
        this.alertas = new ArrayList<>();
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
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

    // Cambia el umbral amarillo, validando que quede menor que el naranja
    public void setUmbralAmarilla(double umbralAmarilla) {
        if (umbralAmarilla >= this.umbralNaranja) {
            throw new IllegalArgumentException(
                "El umbral amarilla debe ser menor que el naranja (" + this.umbralNaranja + ").");
        }
        this.umbralAmarilla = umbralAmarilla;
    }

    public double getUmbralNaranja() {
        return umbralNaranja;
    }

    // Cambia el umbral naranja, validando que quede entre el amarillo y el rojo
    public void setUmbralNaranja(double umbralNaranja) {
        if (umbralNaranja <= this.umbralAmarilla || umbralNaranja >= this.umbralRoja) {
            throw new IllegalArgumentException(
                "El umbral naranja debe estar entre la amarilla (" + this.umbralAmarilla
                + ") y la roja (" + this.umbralRoja + ").");
        }
        this.umbralNaranja = umbralNaranja;
    }

    public double getUmbralRoja() {
        return umbralRoja;
    }

    // Cambia el umbral rojo, validando que quede mayor que el naranja
    public void setUmbralRoja(double umbralRoja) {
        if (umbralRoja <= this.umbralNaranja) {
            throw new IllegalArgumentException(
                "El umbral roja debe ser mayor que la naranja (" + this.umbralNaranja + ").");
        }
        this.umbralRoja = umbralRoja;
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
        String id = idZona + "-A" + (alertas.size() + 1);
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
