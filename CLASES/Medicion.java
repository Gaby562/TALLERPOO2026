package CLASES;
import java.time.LocalDateTime;

/**
 * Lectura de precipitación registrada por un {@link Sensor}.
 * Guarda la fecha y hora, los milímetros medidos y si fue marcada como
 * anómala{
 */
public class Medicion{
    private int idMedicion;
    private LocalDateTime fechaHora;
    private double milimetros;
    private boolean anomala;

    /** Constructor sin parametros. Inicializa con valores por defecto.*/
    public Medicion(){
        this.idMedicion = 0;
        this.fechaHora = LocalDateTime.now();
        this.milimetros = 0;
        this.anomala = false;
    }

    /**
     * Crea una medición.
     *
     * @param idMedicion identificador de la medición
     * @param fechaHora fecha y hora de la lectura
     * @param milimetros milímetros de lluvia registrados
     * @param anomala true si la lectura fue marcada como anómala
     */
    public Medicion(int idMedicion, LocalDateTime fechaHora, double milimetros, boolean anomala){
        this.idMedicion= idMedicion;
        this.fechaHora= fechaHora;
        this.milimetros= milimetros;
        this.anomala= anomala;
    }

    /** Establece el identificador de la medición. */
    public void setIdMedicion(int idMedicion){
         this.idMedicion= idMedicion;
    }

    /** Devuelve el identificador de la medición. */
    public int getIdMedicion(){
        return idMedicion;
    }

    /** Establece la fecha y hora de la lectura. */
    public void setFechaHora(LocalDateTime fechaHora){
        this.fechaHora= fechaHora;   
    }

    /** Devuelve la fecha y hora de la lectura. */
    public LocalDateTime getFechaHora(){
        return fechaHora;
    }

    /** Establece los milímetros registrados. */
    public void setMilimetros(double milimetros){
        this.milimetros= milimetros;
    }

    /** Devuelve los milímetros registrados. */
    public double getMilimetros(){
        return milimetros;
    }

    /** Establece si la lectura es anómala. */
    public void setAnomala(boolean anomala){
        this.anomala= anomala;
    }

    /** Indica si la lectura fue marcada como anómala. */
    public boolean getAnomala(){
        return anomala;
    }

    /** Devuelve los datos de la medición en formato de texto. */
    @Override
    public String toString() {
        return "MEDICIÓN\n" +
            "ID de la medición: " + idMedicion + "\n" +
            "Fecha y hora: " + fechaHora + "\n" +
            "Milímetros: " + milimetros + " mm\n" +
            "Anómala: " + (anomala ? "Sí" : "No");
    }
}