
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
    private double lluviaAcumulada24h;
    private List<ValorHistorico> valoresHistoricos;

    public ZonaGeografica(String idZona, String nombre,
            NivelVulnerabilidad nivelVulnerabilidad,
            double umbralAmarilla, double umbralNaranja,
            double umbralRoja) {

        this.idZona = idZona;
        this.nombre = nombre;
        this.nivelVulnerabilidad = nivelVulnerabilidad;
        this.umbralAmarilla = umbralAmarilla;
        this.umbralNaranja = umbralNaranja;
        this.umbralRoja = umbralRoja;
        this.nivelAlertaActual = NivelAlerta.VERDE;
        this.lluviaAcumulada24h = 0;
        this.valoresHistoricos = new ArrayList<>();
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

    public void setNivelVulnerabilidad(
            NivelVulnerabilidad nivelVulnerabilidad) {
        this.nivelVulnerabilidad = nivelVulnerabilidad;
    }

    public double getUmbralAmarilla() {
        return umbralAmarilla;
    }

    public void setUmbralAmarilla(double umbralAmarilla) {
        this.umbralAmarilla = umbralAmarilla;
    }

    public double getUmbralNaranja() {
        return umbralNaranja;
    }

    public void setUmbralNaranja(double umbralNaranja) {
        this.umbralNaranja = umbralNaranja;
    }

    public double getUmbralRoja() {
        return umbralRoja;
    }

    public void setUmbralRoja(double umbralRoja) {
        this.umbralRoja = umbralRoja;
    }

    public NivelAlerta getNivelAlertaActual() {
        return nivelAlertaActual;
    }

    public void setNivelAlertaActual(NivelAlerta nivelAlertaActual) {
        this.nivelAlertaActual = nivelAlertaActual;
    }

    public double getLluviaAcumulada24h() {
        return lluviaAcumulada24h;
    }

    public void setLluviaAcumulada24h(double lluviaAcumulada24h) {
        this.lluviaAcumulada24h = lluviaAcumulada24h;
    }

    public List<ValorHistorico> getValoresHistoricos() {
        return valoresHistoricos;
    }

    public void setValoresHistoricos(
            List<ValorHistorico> valoresHistoricos) {
        this.valoresHistoricos = valoresHistoricos;
    }
}
