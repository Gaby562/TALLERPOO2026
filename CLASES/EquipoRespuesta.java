package CLASES;
import java.util.List;
import java.util.ArrayList;

/**
 * Equipo de respuesta técnica para emergencias. Está integrado por uno o más
 * {@link ResponsableTecnico}; cada técnico integra a lo sumo un equipo
 * (esa regla se controla en el sistema).
 */
public class EquipoRespuesta{
    private int idEquipo;
    private String nombreEquipo;
    private boolean disponible;
    private List<ResponsableTecnico> integrantes;

    /** Constructor sin parametros. Inicializa con valores por defecto.*/
    public EquipoRespuesta(){
        this.idEquipo = 0;
        this.nombreEquipo = "";
        this.disponible = true;
        this.integrantes= new ArrayList<ResponsableTecnico>();
    }
    /**
     * Crea un equipo sin integrantes.
     * @param idEquipo identificador del equipo
     * @param nombreEquipo nombre del equipo
     * @param disponible true si está disponible para atender emergencias
     */
    public EquipoRespuesta(int idEquipo, String nombreEquipo, boolean disponible){
        this.idEquipo=idEquipo;
        this.nombreEquipo=nombreEquipo;
        this.disponible=disponible;
        this.integrantes= new ArrayList<ResponsableTecnico>();
    }

    /**
     * Agrega un técnico a la lista de integrantes del equipo.
     * @param r técnico a agregar
     */
    public void agregarIntegrante(ResponsableTecnico r){
        this.integrantes.add(r);
    }
    /** Devuelve la lista de integrantes del equipo. */
    public List<ResponsableTecnico> getIntegrantes() {
        return integrantes;
    }
    /** Reemplaza la lista de integrantes del equipo. */
    public void setIntegrantes(List<ResponsableTecnico> integrantes) {
        this.integrantes = integrantes;
    }
    /** Devuelve el identificador del equipo. */
    public int getIdEquipo() {
        return idEquipo;
    }
    /** Establece el identificador del equipo. */
    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }
    /** Devuelve el nombre del equipo. */
    public String getNombreEquipo() {
        return nombreEquipo;
    }
    /** Establece el nombre del equipo. */
    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }
    /** Indica si el equipo está disponible para atender una emergencia. */
    public boolean isDisponible() {
        return disponible;
    }
    /** Establece si el equipo está disponible. */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    /** Devuelve los datos del equipo en formato de texto, mostrando solo los nombres de los integrantes. */
    @Override
    public String toString() {
        // Solo se muestran los nombres de los integrantes
        String nombres = "";
        for (ResponsableTecnico r : integrantes) {
            nombres += r.getNombreCompleto() + "; ";
        }
        return "EQUIPO DE RESPUESTA\n" +
            "ID del equipo: " + idEquipo + "\n" +
            "Nombre: " + nombreEquipo + "\n" +
            "Disponible: " + (disponible ? "Sí" : "No") + "\n" +
            "Integrantes: " + (integrantes.isEmpty() ? "ninguno" : nombres);
    }

}