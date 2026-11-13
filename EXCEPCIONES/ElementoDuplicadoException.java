package EXCEPCIONES;
/**
 * Se lanza cuando se intenta registrar en el sistema un elemento
 * (zona, estación, sensor, técnico o equipo) con un identificador que ya existe.
 */
public class ElementoDuplicadoException extends Exception {
    /**
     * Crea la excepción con un mensaje descriptivo.
     * @param mensaje motivo del error
     */
    public ElementoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
