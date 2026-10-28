import enums.EstadoOperativo;
import enums.TipoEstacion;
import enums.TipoMantenimiento;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

public class EstacionMeteorologica {
    private String idEstacion;
    private String nombre;
    private double latitud;
    private double longitud;
    private double altitud;
    private EstadoOperativo estado;
    private TipoEstacion tipo;

    // relaciones 
    private List<Sensor> sensores; //la estacion puede tener 0 o varios. Tambien represento aca la agregacion entre estacion y sensor
    private ResponsableTecnico responsable; //cada estacion tiene 1 responsable. Representa la relacion unidireccional con estacion
    private ZonaGeografica zona; //porque cada estacion pertenece a 1 zona
    private List<Mantenimiento> mantenimientos; //la estacion puede tener ninguno o varios.Tambien aca se representa la composicion entre estacion y mantenimiento

    public EstacionMeteorologica() {
        this.sensores = new ArrayList<>(); //lista vacia porq puede no tener ninguno
        this.mantenimientos = new ArrayList<>(); //lo mismo que sensores
    }

    public EstacionMeteorologica(String idEstacion, String nombre, double latitud, double longitud, double altitud, EstadoOperativo estado, TipoEstacion tipo, ResponsableTecnico responsable, ZonaGeografica zona) {
        this.idEstacion = idEstacion;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.altitud = altitud;
        this.estado = estado;
        this.tipo = tipo;
        this.responsable = responsable;
        this.zona = zona;
        this.sensores = new ArrayList<>();
        this.mantenimientos = new ArrayList<>();
    }

    public String getIdEstacion (){
        return idEstacion;
    }

    public void setIdEstacion(String idEstacion){
        this.idEstacion = idEstacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getAltitud() {
        return altitud;
    }

    public void setAltitud(double altitud) {
        this.altitud = altitud;
    }

    public EstadoOperativo getEstado() {
        return estado;
    }

    public void setEstado(EstadoOperativo estado) {
        this.estado = estado;
    }

    public TipoEstacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoEstacion tipo) {
        this.tipo = tipo;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    public ResponsableTecnico getResponsable() {
        return responsable;
    }

    public void setResponsable(ResponsableTecnico responsable) {
        this.responsable = responsable;
    }

    public ZonaGeografica getZona() {
        return zona;
    }

    public void setZona(ZonaGeografica zona) {
        this.zona = zona;
    }

    public List<Mantenimiento> getMantenimientos() {
        return mantenimientos;
    }

    public void setMantenimientos(List<Mantenimiento> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    //metodo instalar que agrega el sensor a la lista si no es null y todavía no esta en ella
    public void instalarSensor(Sensor s) {
        if (s != null && !sensores.contains(s)) {
            sensores.add(s);
        }
    }

    //metodo retirar que elimina el sensor de la lista si es que esta
    public void retirarSensor(Sensor s) {
        if (s != null) {
            sensores.remove(s);
        }
    }

    //metodo estaOperativa? devuelve true o false
    public boolean estaOperativa() {
        return estado == EstadoOperativo.OPERATIVO;
    }

    // Crea un mantenimiento y lo agrega a la lista de la estación (composición)
    public Mantenimiento registrarMantenimiento(String id, LocalDate fecha, TipoMantenimiento tipo, String descripcion, Sensor sensor, ResponsableTecnico responsable) {
        // La estación es quien crea el mantenimiento
        Mantenimiento m = new Mantenimiento(id, fecha, tipo, descripcion, sensor, responsable);
        // Lo guarda en su lista de mantenimientos
        this.mantenimientos.add(m);
        // Devuelve el mantenimiento creado
        return m;
    }

    @Override 
    public String toString(){
        return "ESTACIÓN METEOROLÓGICA\n" +
            "ID de la estación: " + idEstacion + "\n" +
            "Nombre de la estación: " + nombre + "\n" +
            "Latitud: " + latitud + "\n" +
            "Longitud: " + longitud + "\n" +
            "Altitud: " + altitud + " metros\n" +
            "Estado operativo: " + estado + "\n" +
            "Tipo de estación: " + tipo + "\n" +
            "Responsable técnico: " + responsable + "\n" +
            "Zona geográfica: " + zona + "\n" +
            "Sensores: " + sensores + "\n" +
            "Mantenimientos registrados: " + mantenimientos;
    }

}
