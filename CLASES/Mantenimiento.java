package CLASES;
import java.time.LocalDate;
import enums.*; 

public class Mantenimiento{
    private int idMantenimiento;
    private LocalDate fecha;
    private TipoMantenimiento tipo;
    private String descripcion;
    // relaciones
    private Sensor sensor;  // 0..1
    private ResponsableTecnico responsable; // 1

    public Mantenimiento(){
        this.idMantenimiento = 0;
        this.fecha = LocalDate.now();
        this.tipo = TipoMantenimiento.PREVENTIVO;
        this.descripcion = "";
        this.sensor = null;        
        this.responsable = null;
    }
    public Mantenimiento(int idMantenimiento, LocalDate fecha, TipoMantenimiento tipo, String descripcion, Sensor sensor, ResponsableTecnico responsable) {
        this.idMantenimiento = idMantenimiento;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.sensor = sensor;
        this.responsable = responsable;
    }
    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento; 
    }

    public LocalDate getFecha() { 
        return fecha; 
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha; 
    }

    public TipoMantenimiento getTipo() {
        return tipo;
    }
    public void setTipo(TipoMantenimiento tipo) {
        this.tipo = tipo; 
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Sensor getSensor() { 
        return sensor; 
    }
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public ResponsableTecnico getResponsable() { 
        return responsable; 
    }
    public void setResponsable(ResponsableTecnico responsable) { 
        this.responsable = responsable; 
    }
    
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