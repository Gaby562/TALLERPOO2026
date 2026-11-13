package CLASES;
import enums.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Estación meteorológica. Tiene sensores instalados
 * (agregación), un responsable técnico a cargo y un historial de
 * mantenimientos que ella misma crea (composición).
 */
public class EstacionMeteorologica {
    private int idEstacion;
    private String nombre;
    private double latitud;
    private double longitud;
    private double altitud;
    private EstadoOperativo estado;
    private TipoEstacion tipo;

    /** relaciones.*/
    private List<Sensor> sensores; 
    private ResponsableTecnico responsable; 
    private List<Mantenimiento> mantenimientos; 

    /** Constructor sin parametros. Inicializa con valores por defecto.*/
    public EstacionMeteorologica() {
        this.idEstacion = 0;
        this.nombre = "";
        this.latitud = 0;
        this.longitud = 0;
        this.altitud = 0;
        this.estado = EstadoOperativo.OPERATIVO;
        this.tipo = TipoEstacion.AUTOMATICA;
        this.responsable = null;
        this.sensores = new ArrayList<>(); //lista vacia porq puede no tener ninguno
        this.mantenimientos = new ArrayList<>(); //lo mismo que sensores
    }
    /**
     * Crea una estación sin sensores ni mantenimientos.
     *
     * @param idEstacion identificador de la estación
     * @param nombre nombre de la estación
     * @param latitud latitud en grados
     * @param longitud longitud en grados
     * @param altitud altitud en metros
     * @param estado estado operativo inicial
     * @param tipo tipo de estación (automática o convencional)
     * @param responsable técnico responsable de la estación
     */
    public EstacionMeteorologica(int idEstacion, String nombre, double latitud, double longitud, double altitud, EstadoOperativo estado, TipoEstacion tipo, ResponsableTecnico responsable) {
        this.idEstacion = idEstacion;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.altitud = altitud;
        this.estado = estado;
        this.tipo = tipo;
        this.responsable = responsable;
        this.sensores = new ArrayList<>();
        this.mantenimientos = new ArrayList<>();
    }
    /** Devuelve el identificador de la estación. */
    public int getIdEstacion (){
        return idEstacion;
    }
    /** Establece el identificador de la estación. */
    public void setIdEstacion(int idEstacion){
        this.idEstacion = idEstacion;
    }
    /** Devuelve el nombre de la estación. */
    public String getNombre() {
        return nombre;
    }
    /** Establece el nombre de la estación. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /** Devuelve la latitud en grados. */
    public double getLatitud() {
        return latitud;
    }
    /** Establece la latitud en grados. */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
    /** Devuelve la longitud en grados. */
    public double getLongitud() {
        return longitud;
    }
    /** Establece la longitud en grados. */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
    /** Devuelve la altitud en metros. */
    public double getAltitud() {
        return altitud;
    }
    /** Establece la altitud en metros. */
    public void setAltitud(double altitud) {
        this.altitud = altitud;
    }
    /** Devuelve el estado operativo de la estación. */
    public EstadoOperativo getEstado() {
        return estado;
    }
    /** Establece el estado operativo de la estación. */
    public void setEstado(EstadoOperativo estado) {
        this.estado = estado;
    }
    /** Devuelve el tipo de estación. */
    public TipoEstacion getTipo() {
        return tipo;
    }

    /** Establece el tipo de estación. */
    public void setTipo(TipoEstacion tipo) {
        this.tipo = tipo;
    }

    /** Devuelve la lista de sensores instalados en la estación. */
    public List<Sensor> getSensores() {
        return sensores;
    }

    /** Reemplaza la lista de sensores instalados. */
    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    /** Devuelve el responsable técnico de la estación. */
    public ResponsableTecnico getResponsable() {
        return responsable;
    }

    /** Establece el responsable técnico de la estación. */
    public void setResponsable(ResponsableTecnico responsable) {
        this.responsable = responsable;
    }

    /** Devuelve la lista de mantenimientos registrados en la estación. */
    public List<Mantenimiento> getMantenimientos() {
        return mantenimientos;
    }

    /** Reemplaza la lista de mantenimientos. */
    public void setMantenimientos(List<Mantenimiento> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    /**
     * Instala un sensor en la estación. No hace nada si el sensor es null
     * o si ya está instalado en esta estación. 
     * La regla de que un sensor esté en una sola estación se controla en el sistema.
     * @param s sensor a instalar
     */
    public void instalarSensor(Sensor s) {
        if (s != null && !sensores.contains(s)) {
            sensores.add(s);
        }
    }

    /**
     * Retira un sensor de la estación. No hace nada si el sensor es null o si no está instalado.
     * @param s sensor a retirar
     */
    public void retirarSensor(Sensor s) {
        if (s != null) {
            sensores.remove(s);
        }
    }

    /**
     * Indica si la estación está operativa.
     * @return true si su estado es OPERATIVO
     */
    public boolean estaOperativa() {
        return estado == EstadoOperativo.OPERATIVO;
    }

    /**
     * Crea un mantenimiento y lo agrega a la lista de la estación
     * (composición: la estación es quien lo crea).
     *
     * @param id identificador del mantenimiento
     * @param fecha fecha en que se realizó
     * @param tipo tipo de mantenimiento
     * @param descripcion descripción de la tarea
     * @param sensor sensor intervenido, o null si fue sobre toda la estación
     * @param responsable técnico que lo realizó
     * @return el mantenimiento creado
     */
    public Mantenimiento registrarMantenimiento(int id, LocalDate fecha, TipoMantenimiento tipo, String descripcion, Sensor sensor, ResponsableTecnico responsable) {
        // La estación es quien crea el mantenimiento
        Mantenimiento m = new Mantenimiento(id, fecha, tipo, descripcion, sensor, responsable);
        // Lo guarda en su lista de mantenimientos
        this.mantenimientos.add(m);
        // Devuelve el mantenimiento creado
        return m;
    }

    /** Devuelve los datos de la estación en formato de texto. */
    @Override
    public String toString() {
        return "ESTACIÓN METEOROLÓGICA\n" +
            "ID de la estación: " + idEstacion + "\n" +
            "Nombre: " + nombre + "\n" +
            "Latitud: " + latitud + "\n" +
            "Longitud: " + longitud + "\n" +
            "Altitud: " + altitud + " metros\n" +
            "Estado operativo: " + estado + "\n" +
            "Tipo de estación: " + tipo + "\n" +
            "Responsable técnico: " + (responsable != null ? responsable.getNombreCompleto() : "sin asignar") + "\n" +
            "Cantidad de sensores: " + sensores.size() + "\n" +
            "Cantidad de mantenimientos: " + mantenimientos.size();
    }

}
