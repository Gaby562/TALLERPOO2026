package EXCEPCIONES;
/**
 * Se lanza cuando una lectura no puede registrarse: el sensor no está
 * operativo o los milímetros están fuera del rango físico admitido (0 a 500).
 */
public class LecturaInvalidaException extends Exception{
    /**
     * Crea la excepción con un mensaje descriptivo.
     * @param mensaje motivo del error
     */
    public LecturaInvalidaException(String mensaje){
        super(mensaje);
    }
}