import enums.EstadoOperativo;
import java.time.LocalDate; //una fecha sin hora para la ultima calibracion
import java.time.LocalDateTime; //fecha y hora para registrar la medicion
import java.util.List;
import java.util.ArrayList;

public class Sensor {
    private int idSensor;
    private String marca;
    private String modelo;
    private double precision;
    private EstadoOperativo estado;
    private LocalDate ultimaCalibracion;

    //relacion con medicion
    private List<Medicion> mediciones; //cada sensor puede tener 0 o muchas mediciones. Para almacenar el historial de las mediciones

    public Sensor() {
        this.mediciones = new ArrayList<>(); //lista vacia porq cada sensor puede tener cero
    }

    public Sensor(int idSensor, String marca, String modelo, double precision, EstadoOperativo estado, LocalDate ultimaCalibracion) {
        this.idSensor = idSensor;
        this.marca = marca;
        this.modelo = modelo;
        this.precision = precision;
        this.estado = estado;
        this.ultimaCalibracion = ultimaCalibracion;
        this.mediciones = new ArrayList<>();
    }
 
    public int getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public EstadoOperativo getEstado() {
        return estado;
    }

    public void setEstado(EstadoOperativo estado) {
        this.estado = estado;
    }

    public LocalDate getUltimaCalibracion() {
        return ultimaCalibracion;
    }

    public void setUltimaCalibracion(LocalDate ultimaCalibracion) {
        this.ultimaCalibracion = ultimaCalibracion;
    }

    public List<Medicion> getMediciones() {
        return mediciones;
    }

    public void setMediciones(List<Medicion> mediciones) {
        this.mediciones = mediciones;
    }

    //metodo que crea una medicion, comprueba q los mm sean validos,compara la nueva lectura con la anterior para detectar anomalias 
    //y la agrega a la lista donde esta el historial, con su fecha y hora

    //IMPORTANTE A REVISAR DESPUES: no se pueden registrar lecturas si el sensor o la estación estan en mantenimiento o fuera de servicio
    public Medicion registrar(double mm, LocalDateTime fh) throws LecturaInvalidaException {
       
        // Control operativo: el sensor tiene que estar OPERATIVO
        if (estado != EstadoOperativo.OPERATIVO) {
            throw new LecturaInvalidaException(
                "El sensor " + idSensor + " no está operativo (estado: " + estado + ").");
        }
        // Validación del rango físico (0 a 500 mm)
        if (!validar(mm)) {
            throw new LecturaInvalidaException(
                "Los milímetros deben estar entre 0 y 500."
            );
        }

        boolean anomala = false;

        if (!mediciones.isEmpty()) { //isEmpty comprueba si la lista esta vacia, !mediciones.isEmpty significa que la lista contiene al menos una medición
            Medicion anterior = mediciones.get(mediciones.size() - 1); //busca la ultima medicion de la lista

            if (Math.abs(mm - anterior.getMilimetros()) > 100) { //calcula la diferencia entre los milímetros nuevos y los anteriores
                anomala = true; //y si es anomala la registramos sin descartarla
            }
        }
        // El id incluye el sensor para que no se repita entre sensores distintos
        int id = mediciones.size() + 1;
        Medicion nueva = new Medicion(id, fh, mm, anomala);
        mediciones.add(nueva); //guardamos la medicion a la lista del historial
        return nueva; //y devolvemos la medicion que se creo
    }

    //metodo que compueba que una cantidad de lluvia sea valida (entre 0 y 500) devuelve true o false
    public boolean validar(double mm) {
        return mm >= 0 && mm <= 500;
    }

    //metodo que dice si el sensor necesita una calibracion y devuelve el resultado de la comparacion con la ult calibracion
    //plusMonths le suma 12 meses a la ult calibracion y isBefore comprueba si la fecha de vencimiento quedo antes de la fecha actual
    public boolean requiereCalibracion() {
        if (ultimaCalibracion == null) { //comprueba si no hay una fecha de calibración cargada, si la hay, devuelve false
            return false;
        }

        return ultimaCalibracion.plusMonths(12).isBefore(LocalDate.now());
    }

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
        
