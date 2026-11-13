package EXCEPCIONES;
/**
 * Se lanza cuando no se puede asignar o liberar un equipo de respuesta:
 * la alerta no lo requiere, ya tiene uno, o el equipo no tiene integrantes
 * o no está disponible.
 */
public class AlertaSinEquipoException extends Exception {
    /**
     * Crea la excepción con un mensaje descriptivo.
     * @param mensaje motivo del error
     */
    public AlertaSinEquipoException(String mensaje) {
        super(mensaje);
    }
}