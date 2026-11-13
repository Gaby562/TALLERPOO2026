package EXCEPCIONES;
/**
 * Se lanza cuando se intenta instalar un sensor que ya está instalado
 * en otra estación. Un sensor solo puede estar en una estación a la vez.
 */
public class SensorYaInstaladoException extends Exception {
    /**
     * Crea la excepción con un mensaje descriptivo.
     * @param mensaje motivo del error
     */
    public SensorYaInstaladoException(String mensaje) {
        super(mensaje);
    }
}