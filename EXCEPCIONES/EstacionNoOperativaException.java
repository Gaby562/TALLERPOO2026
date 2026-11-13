package EXCEPCIONES;
/**
 * Se lanza cuando se intenta registrar una lectura en una estación que
 * está EN_MANTENIMIENTO o FUERA_DE_SERVICIO.
 */
public class EstacionNoOperativaException extends Exception {
    /**
     * Crea la excepción con un mensaje descriptivo.
     * @param mensaje motivo del error
     */
    public EstacionNoOperativaException(String mensaje) {
        super(mensaje);
    }
}