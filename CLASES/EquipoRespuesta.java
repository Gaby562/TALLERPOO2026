package CLASES;
import java.util.List;
import java.util.ArrayList;

public class EquipoRespuesta{
    private int idEquipo;
    private String nombreEquipo;
    private boolean disponible;
    private List<ResponsableTecnico> integrantes;

    public EquipoRespuesta(){
        this.idEquipo = 0;
        this.nombreEquipo = "";
        this.disponible = true;
        this.integrantes= new ArrayList<ResponsableTecnico>();
    }

    public EquipoRespuesta(int idEquipo, String nombreEquipo, boolean disponible){
        this.idEquipo=idEquipo;
        this.nombreEquipo=nombreEquipo;
        this.disponible=disponible;
        this.integrantes= new ArrayList<ResponsableTecnico>();
    }

    public void agregarResponsableTecnico(ResponsableTecnico r){
        this.integrantes.add(r);
    }

   public List<ResponsableTecnico> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<ResponsableTecnico> integrantes) {
        this.integrantes = integrantes;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

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