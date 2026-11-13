package CLASES;

/**
 * Técnico responsable de la supervisión de estaciones y del mantenimiento
 * y calibración de sus sensores. Hereda los datos personales de {@link Persona}
 * y también puede integrar un {@link EquipoRespuesta}.
 */
public class ResponsableTecnico extends Persona{
    private String especialidad;
    private String numeroLicencia;

    /** Constructor sin parametros. Inicializa con valores por defecto.*/
    public ResponsableTecnico(){
        super();
        this.especialidad = "";
        this.numeroLicencia = "";
    }

    /**
     * Crea un responsable técnico.
     *
     * @param dni documento de identidad
     * @param nombre nombre
     * @param apellido apellido
     * @param correo correo electrónico
     * @param celular número de celular
     * @param especialidad especialidad técnica
     * @param numeroLicencia número de licencia profesional
     */
    public ResponsableTecnico (int dni, String nombre, String apellido, String correo, String celular, String especialidad, String numeroLicencia){
        super(dni, nombre, apellido, correo, celular);
        this.especialidad= especialidad;
        this.numeroLicencia= numeroLicencia;
    }

    /** Establece la especialidad técnica. */
    public void setEspecialidad(String especialidad){
        this.especialidad= especialidad;
    }

    /** Devuelve la especialidad técnica. */
    public String getEspecialidad(){
        return especialidad;
    }

    /** Establece el número de licencia. */
    public void setNumeroLicencia(String numeroLicencia){
        this.numeroLicencia= numeroLicencia;
    }

    /** Devuelve el número de licencia. */
    public String getNumeroLicencia(){
        return numeroLicencia;
    }

    /** Devuelve los datos del responsable técnico en formato de texto. */
    @Override
    public String toString() {
        return "RESPONSABLE TÉCNICO\n" +
            super.toString() + "\n" +
            "Especialidad: " + especialidad + "\n" +
            "Licencia: " + numeroLicencia;
    }

}