package CLASES;
import java.time.LocalDateTime;
import enums.*;

public class Alerta {
    private int idAlerta;
    private LocalDateTime fechaHoraEmision;
    private NivelAlerta nivel;
    private TipoEventoClimatico evento;
    private String descripcion;
    private boolean atendida;
    //Relacion
    private EquipoRespuesta equipo;

    public Alerta(){
        this.idAlerta = 0;
        this.fechaHoraEmision = LocalDateTime.now();
        this.nivel = NivelAlerta.VERDE;
        this.evento = TipoEventoClimatico.LLUVIA_COPIOSA;
        this.descripcion = "";
        this.atendida = false;
        this.equipo = null;
    }
    
    public Alerta(int idAlerta, LocalDateTime fechaHoraEmision, NivelAlerta nivel, TipoEventoClimatico evento, String descripcion) {
        this.idAlerta = idAlerta;
        this.fechaHoraEmision = fechaHoraEmision;
        this.nivel = nivel;
        this.evento = evento;
        this.descripcion = descripcion;
        this.atendida = false;
        this.equipo = null;
    }
    public int getIdAlerta() {
        return idAlerta;
    }
    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }
    public LocalDateTime getFechaHoraEmision() {
        return fechaHoraEmision;
    }
    public void setFechaHoraEmision(LocalDateTime fechaHoraEmision) {
        this.fechaHoraEmision = fechaHoraEmision;
    }
    public NivelAlerta getNivel() {
        return nivel;
    }
    public void setNivel(NivelAlerta nivel) {
        this.nivel = nivel;
    }
    public TipoEventoClimatico getEvento() {
        return evento;
    }
    public void setEvento(TipoEventoClimatico evento) {
        this.evento = evento;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public boolean isAtendida() {
        return atendida;
    }
    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }
    public EquipoRespuesta getEquipo() {
        return equipo;
    }
    public void setEquipo(EquipoRespuesta equipo) {
        this.equipo = equipo;
    }
    
    // Asigna el equipo y marca la alerta como atendida
    public void asignarEquipo(EquipoRespuesta e) {
        this.equipo = e;
        this.atendida = true;
    }

    // Solo las alertas NARANJA y ROJA requieren equipo
    public boolean requiereEquipo() {
        return nivel == NivelAlerta.NARANJA || nivel == NivelAlerta.ROJA;
    }
    
    @Override
    public String toString() {
        return "ALERTA\n" +
            "ID de la alerta: " + idAlerta + "\n" +
            "Fecha y hora de emisión: " + fechaHoraEmision + "\n" +
            "Nivel: " + nivel + "\n" +
            "Evento: " + evento + "\n" +
            "Descripción: " + descripcion + "\n" +
            "Atendida: " + (atendida ? "Sí" : "No") + "\n" +
            "Equipo asignado: " + (equipo != null ? equipo.getNombreEquipo() : "sin asignar");
    }
    
}
