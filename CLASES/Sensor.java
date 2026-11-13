package CLASES;
import java.time.LocalDate; //una fecha sin hora para la ultima calibracion
import java.time.LocalDateTime; //fecha y hora para registrar la medicion
import java.util.List;
import EXCEPCIONES.*;
import enums.*;
import java.util.ArrayList;

/**
 * Pluviómetro instalado en una estación. Compone su historial de
 * {@link Medicion}: valida cada lectura, detecta las anómalas y
 * determina si necesita calibración.
 */
public class Sensor {
    private int idSensor;
    private String marca;
    private String modelo;
    private double precision;
    private EstadoOperativo estado;
    private LocalDate ultimaCalibracion;

    /**relacion con medicion.*/
    private List<Medicion> mediciones; 

    /**Contador compartido por todos los sensores: cada medición recibe un id único.*/
    private static int proximoIdMedicion = 1;

    /** Constructor sin parametros. Inicializa con valores por defecto.*/
    public Sensor() {
        this.idSensor = 0;
        this.marca = "";
        this.modelo = "";
        this.precision = 0;
        this.estado = EstadoOperativo.OPERATIVO;
        this.ultimaCalibracion = LocalDate.now();
        this.mediciones = new ArrayList<>(); //lista vacia porq cada sensor puede tener cero
    }

    /**
     * Crea un sensor sin mediciones.
     *
     * @param idSensor identificador del sensor
     * @param marca marca del sensor
     * @param modelo modelo del sensor
     * @param precision precisión del sensor
     * @param estado estado operativo inicial
     * @param ultimaCalibracion fecha de la última calibración
     */
    public Sensor(int idSensor, String marca, String modelo, double precision, EstadoOperativo estado, LocalDate ultimaCalibracion) {
        this.idSensor = idSensor;
        this.marca = marca;
        this.modelo = modelo;
        this.precision = precision;
        this.estado = estado;
        this.ultimaCalibracion = ultimaCalibracion;
        this.mediciones = new ArrayList<>();
    }

    /** Devuelve el identificador del sensor. */
    public int getIdSensor() {
        return idSensor;
    }

    /** Establece el identificador del sensor. */
    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    /** Devuelve la marca del sensor. */
    public String getMarca() {
        return marca;
    }

    /** Establece la marca del sensor. */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /** Devuelve el modelo del sensor. */
    public String getModelo() {
        return modelo;
    }

    /** Establece el modelo del sensor. */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /** Devuelve la precisión del sensor. */
    public double getPrecision() {
        return precision;
    }

    /** Establece la precisión del sensor. */
    public void setPrecision(double precision) {
        this.precision = precision;
    }

    /** Devuelve el estado operativo del sensor. */
    public EstadoOperativo getEstado() {
        return estado;
    }

    /** Establece el estado operativo del sensor. */
    public void setEstado(EstadoOperativo estado) {
        this.estado = estado;
    }

    /** Devuelve la fecha de la última calibración. */
    public LocalDate getUltimaCalibracion() {
        return ultimaCalibracion;
    }

    /** Establece la fecha de la última calibración. */
    public void setUltimaCalibracion(LocalDate ultimaCalibracion) {
        this.ultimaCalibracion = ultimaCalibracion;
    }

    /** Devuelve el historial de mediciones del sensor. */
    public List<Medicion> getMediciones() {
        return mediciones;
    }

    /** Reemplaza el historial de mediciones del sensor. */
    public void setMediciones(List<Medicion> mediciones) {
        this.mediciones = mediciones;
    }

    
    /**
     * Registra una lectura de lluvia. Comprueba que el sensor esté operativo
     * y que los milímetros sean válidos. Si la lectura difiere en más de
     * 100 mm de la anterior del mismo sensor, se registra igual pero
     * queda marcada como anómala. El control del estado de la estación
     * se hace en el sistema.
     *
     * @param mm milímetros registrados (entre 0 y 500)
     * @param fh fecha y hora de la lectura
     * @return la medición creada y agregada al historial
     * @throws LecturaInvalidaException si el sensor no está operativo o los mm están fuera de rango
     */
    public Medicion registrar(double mm, LocalDateTime fh) throws LecturaInvalidaException {
       
        if (estado != EstadoOperativo.OPERATIVO) {
            throw new LecturaInvalidaException(
                "El sensor " + idSensor + " no está operativo (estado: " + estado + ").");
        }
        if (!validar(mm)) {
            throw new LecturaInvalidaException(
                "Los milímetros deben estar entre 0 y 500."
            );
        }
        boolean anomala = false;
        if (!mediciones.isEmpty()) { 
            Medicion anterior = mediciones.get(mediciones.size() - 1); 
            if (Math.abs(mm - anterior.getMilimetros()) > 100) { 
                anomala = true; 
            }
        }
        int id = proximoIdMedicion++;
        Medicion nueva = new Medicion(id, fh, mm, anomala);
        mediciones.add(nueva); 
        return nueva; 
    }

    /**
     * Comprueba que una cantidad de lluvia esté dentro del rango físico admitido.
     * @param mm milímetros a validar
     * @return true si está entre 0 y 500 (inclusive)
     */
    public boolean validar(double mm) {
        return mm >= 0 && mm <= 500;
    }

    /**
     * Indica si el sensor necesita calibración: pasaron más de 12 meses
     * desde la última, o nunca fue calibrado.
     * @return true si requiere calibración
     */
    public boolean requiereCalibracion() {
        if (ultimaCalibracion == null) {
            return true;
        }
        return ultimaCalibracion.plusMonths(12).isBefore(LocalDate.now());
    }

    /** Devuelve los datos del sensor en formato de texto. */
    @Override
    public String toString() {
        return "SENSOR\n" +
            "ID del sensor: " + idSensor + "\n" +
            "Marca: " + marca + "\n" +
            "Modelo: " + modelo + "\n" +
            "Precisión: " + precision + "\n" +
            "Estado operativo: " + estado + "\n" +
            "Última calibración: " + ultimaCalibracion + "\n" +
            "Cantidad de mediciones: " + mediciones.size();
    }
}
        
