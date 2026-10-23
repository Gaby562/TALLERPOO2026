import java.util.List;
import java.util.ArrayList;

public class EquipoRespuesta{
    private String idEquipo;
    private String nombreEquipo;
    private boolean disponible;
    private List<ResponsableTecnico> integrantes;

    public EquipoRespuesta(){
        this.integrantes= new ArrayList<ResponsableTecnico>();
    }

    public EquipoRespuesta(String idEquipo, String nombreEquipo, boolean disponible){
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

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
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
        return "EquipoRespuesta{idEquipo: " + idEquipo + ", nombreEquipo: " + nombreEquipo + ", disponible: " + disponible + ", integrantes=" + integrantes + "}";
    }

    
}