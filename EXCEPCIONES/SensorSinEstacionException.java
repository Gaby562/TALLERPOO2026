package EXCEPCIONES;
/**
 * Se lanza cuando se opera con un sensor que no está instalado en
 * ninguna estación (por ejemplo, al registrar una lectura o al retirarlo).
 */
public class SensorSinEstacionException extends Exception {
    /**
     * Crea la excepción con un mensaje descriptivo.
     * @param mensaje motivo del error
     */
    public SensorSinEstacionException(String mensaje) {
        super(mensaje);
    }
}