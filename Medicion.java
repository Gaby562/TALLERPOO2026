import java.time.LocalDateTime;


public class Medicion{
    private String idMedicion;
    private LocalDateTime fechaHora;
    private double milimetros;
    private boolean anomala;

    public Medicion(){

    }
    public Medicion(String idMedicion, LocalDateTime fechaHora, double milimetros, boolean anomala){
        this.idMedicion= idMedicion;
        this.fechaHora= fechaHora;
        this.milimetros= milimetros;
        this.anomala= anomala;
    }
    
    public void setIdMedicion(String idMedicion){
         this.idMedicion= idMedicion;
    }
    public String getIdMedicion(){
        return idMedicion;
    }
    public void setFechaHora(LocalDateTime fechaHora){
        this.fechaHora= fechaHora;   
    }
    public LocalDateTime getFechaHora(){
        return fechaHora;
    }
    public void setMilimetros(double milimetros){
        this.milimetros= milimetros;
    }
    public double getMilimetros(){
        return milimetros;
    }
    public void setAnomala(boolean anomala){
        this.anomala= anomala;
    }
    public boolean getAnomala(){
        return anomala;
    }

    @Override 
    public String toString(){
        return "Medicion{ Id Medicion: " + idMedicion + ", Fecha y Hora: " + fechaHora + ", Milimetros: " + milimetros + ", Anomala: " + anomala + "}";
    }
}