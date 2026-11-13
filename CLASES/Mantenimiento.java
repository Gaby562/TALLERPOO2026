package CLASES;
import java.time.LocalDate;
import enums.*; 

/**
 * Mantenimiento realizado en una estación. Puede aplicarse a un sensor
 * puntual (o a toda la estación si no tiene sensor) y es realizado por
 * un {@link ResponsableTecnico}.
 */
public class Mantenimiento{
    private int idMantenimiento;
    private LocalDate fecha;
    private TipoMantenimiento tipo;
    private String descripcion;
    // relaciones
    private Sensor sensor;  // 0..1
    private ResponsableTecnico responsable; // 1

    /** Constructor sin parametros. Inicializa con valores por defecto.*/
    public Mantenimiento(){
        this.idMantenimiento = 0;
        this.fecha = LocalDate.now();
        this.tipo = TipoMantenimiento.PREVENTIVO;
        this.descripcion = "";
        this.sensor = null;        
        this.responsable = null;
    }

    /**
     * Crea un mantenimiento.
     *
     * @param idMantenimiento identificador del mantenimiento
     * @param fecha fecha en que se realizó
     * @param tipo tipo de mantenimiento
     * @param descripcion descripción de la tarea
     * @param sensor sensor intervenido, o null si fue sobre toda la estación
     * @param responsable técnico que lo realizó
     */
    public Mantenimiento(int idMantenimiento, LocalDate fecha, TipoMantenimiento tipo, String descripcion, Sensor sensor, ResponsableTecnico responsable) {
        this.idMantenimiento = idMantenimiento;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.sensor = sensor;
        this.responsable = responsable;
    }

    /** Devuelve el identificador del mantenimiento. */
    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    /** Establece el identificador del mantenimiento. */
    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento; 
    }

    /** Devuelve la fecha del mantenimiento. */
    public LocalDate getFecha() { 
        return fecha; 
    }

    /** Establece la fecha del mantenimiento. */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha; 
    }

    /** Devuelve el tipo de mantenimiento. */
    public TipoMantenimiento getTipo() {
        return tipo;
    }

    /** Establece el tipo de mantenimiento. */
    public void setTipo(TipoMantenimiento tipo) {
        this.tipo = tipo; 
    }

    /** Devuelve la descripción de la tarea realizada. */
    public String getDescripcion() {
        return descripcion;
    }

    /** Establece la descripción de la tarea realizada. */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /** Devuelve el sensor intervenido. */
    public Sensor getSensor() { 
        return sensor; 
    }

    /** Establece el sensor intervenido. */
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    /** Devuelve el técnico que realizó el mantenimiento. */
    public ResponsableTecnico getResponsable() { 
        return responsable; 
    }

    /** Establece el técnico que realizó el mantenimiento. */
    public void setResponsable(ResponsableTecnico responsable) { 
        this.responsable = responsable; 
    }

    /** Devuelve los datos del mantenimiento en formato de texto. */
    @Override
    public String toString() {
        return "MANTENIMIENTO\n" +
            "ID del mantenimiento: " + idMantenimiento + "\n" +
            "Fecha: " + fecha + "\n" +
            "Tipo: " + tipo + "\n" +
            "Descripción: " + descripcion + "\n" +
            "Sensor: " + (sensor != null ? String.valueOf(sensor.getIdSensor()) : "sin sensor") + "\n" +
            "Responsable: " + (responsable != null ? responsable.getNombreCompleto() : "sin asignar");
    }
    
}