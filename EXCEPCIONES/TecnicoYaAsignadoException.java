package EXCEPCIONES;
/**
 * Se lanza cuando se intenta agregar a un equipo de respuesta un técnico
 * que ya integra otro equipo. Cada técnico integra a lo sumo un equipo.
 */
public class TecnicoYaAsignadoException extends Exception {
    /**
     * Crea la excepción con un mensaje descriptivo.
     * @param mensaje motivo del error
     */
    public TecnicoYaAsignadoException(String mensaje) {
        super(mensaje);
    }
}