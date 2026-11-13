package CLASES;
import java.time.LocalDateTime;
import enums.*;

/**
 * Alerta emitida por una zona geográfica ante un evento climático.
 * Tiene un nivel de severidad y un tipo de evento. Si el nivel es NARANJA
 * o ROJA, requiere que se le asigne un {@link EquipoRespuesta}.
 */
public class Alerta {
    private int idAlerta;
    private LocalDateTime fechaHoraEmision;
    private NivelAlerta nivel;
    private TipoEventoClimatico evento;
    private String descripcion;
    private boolean atendida;
    /** Relacion. */
    private EquipoRespuesta equipo;

    /**Constructor sin parametros. Inicializa con valores por defecto.*/
    public Alerta(){
        this.idAlerta = 0;
        this.fechaHoraEmision = LocalDateTime.now();
        this.nivel = NivelAlerta.VERDE;
        this.evento = TipoEventoClimatico.LLUVIA_COPIOSA;
        this.descripcion = "";
        this.atendida = false;
        this.equipo = null;
    }
    
    /**
     * Crea una alerta sin equipo asignado y no atendida.
     *
     * @param idAlerta identificador de la alerta
     * @param fechaHoraEmision fecha y hora de emisión
     * @param nivel nivel de severidad
     * @param evento tipo de evento climático
     * @param descripcion descripción del evento
     */
    public Alerta(int idAlerta, LocalDateTime fechaHoraEmision, NivelAlerta nivel, TipoEventoClimatico evento, String descripcion) {
        this.idAlerta = idAlerta;
        this.fechaHoraEmision = fechaHoraEmision;
        this.nivel = nivel;
        this.evento = evento;
        this.descripcion = descripcion;
        this.atendida = false;
        this.equipo = null;
    }
    /** Devuelve el identificador de la alerta. */
    public int getIdAlerta() {
        return idAlerta;
    }
    /** Establece el identificador de la alerta. */
    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }
    /** Devuelve la fecha y hora de emisión. */
    public LocalDateTime getFechaHoraEmision() {
        return fechaHoraEmision;
    }
    /** Establece la fecha y hora de emisión. */
    public void setFechaHoraEmision(LocalDateTime fechaHoraEmision) {
        this.fechaHoraEmision = fechaHoraEmision;
    }
    /** Devuelve el nivel de severidad. */
    public NivelAlerta getNivel() {
        return nivel;
    }
    /** Establece el nivel de severidad. */
    public void setNivel(NivelAlerta nivel) {
        this.nivel = nivel;
    }
    /** Devuelve el tipo de evento climático. */
    public TipoEventoClimatico getEvento() {
        return evento;
    }
    /** Establece el tipo de evento climático. */
    public void setEvento(TipoEventoClimatico evento) {
        this.evento = evento;
    }
    /** Devuelve la descripción de la alerta. */
    public String getDescripcion() {
        return descripcion;
    }
    /** Establece la descripción de la alerta. */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /** Indica si la alerta ya fue atendida por un equipo de respuesta. */
    public boolean isAtendida() {
        return atendida;
    }
    /** Establece si la alerta fue atendida. */
    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }
    /** Devuelve el equipo asignado, o null si todavía no tiene. */
    public EquipoRespuesta getEquipo() {
        return equipo;
    }
     /** Establece el equipo asignado sin modificar el estado de atención. */
    public void setEquipo(EquipoRespuesta equipo) {
        this.equipo = equipo;
    }
    
    /** Asigna el equipo de respuesta y marca la alerta como atendida.
     * @param e equipo que atiende la alerta
     */
    public void asignarEquipo(EquipoRespuesta e) {
        this.equipo = e;
        this.atendida = true;
    }

    /** Indica si la alerta requiere un equipo de respuesta.
     * Solo las alertas de nivel NARANJA y ROJA lo requieren.
     * @return true si el nivel es NARANJA o ROJA
     */
    public boolean requiereEquipo() {
        return nivel == NivelAlerta.NARANJA || nivel == NivelAlerta.ROJA;
    }
    /** Devuelve los datos de la alerta en formato de texto. */
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
