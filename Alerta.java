import java.time.LocalDateTime;
import enums.NivelAlerta;
import enums.TipoEventoClimatico;

public class Alerta {
    private String idAlerta;
    private LocalDateTime fechaHoraEmision;
    private NivelAlerta nivel;
    private TipoEventoClimatico evento;
    private String descripcion;
    private boolean atendida;
    //Relacion
    private EquipoRespuesta equipo;

    public Alerta(){

    }
    public Alerta(String idAlerta, LocalDateTime fechaHoraEmision, NivelAlerta nivel, TipoEventoClimatico evento, String descripcion) {
        this.idAlerta = idAlerta;
        this.fechaHoraEmision = fechaHoraEmision;
        this.nivel = nivel;
        this.evento = evento;
        this.descripcion = descripcion;
        this.atendida = false;
        this.equipo = null;
    }
    public String getIdAlerta() {
        return idAlerta;
    }
    public void setIdAlerta(String idAlerta) {
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
    
    public void asignarEquipo(EquipoRespuesta e) {
        this.equipo = e;
        this.atendida = true;
    }
    public boolean requiereEquipo() {
        return nivel == NivelAlerta.NARANJA || nivel == NivelAlerta.ROJA;
    }
    @Override
    public String toString() {
        return "Alerta [idAlerta=" + idAlerta + ", fechaHoraEmision=" + fechaHoraEmision + ", nivel=" + nivel + ", evento=" + evento + ", descripcion=" + descripcion + ", atendida=" + atendida + ", equipo=" + equipo + "]";
    }
    
}
