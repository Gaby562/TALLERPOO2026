package CLASES;

/**
 * Clase abstracta con los datos básicos de identificación de una persona.
 * Es la base de {@link ResponsableTecnico}.
 */
public abstract class Persona{
    private int dni;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String celular;

    /** Constructor sin parametros. Inicializa con valores por defecto.*/
    public Persona(){
        this.dni = 0;
        this.nombre = "";
        this.apellido = "";
        this.correoElectronico = "";
        this.celular = "";
    }

    /**
     * Crea una persona.
     *
     * @param dni documento de identidad
     * @param nombre nombre
     * @param apellido apellido
     * @param correo correo electrónico
     * @param celular número de celular
     */
    public Persona(int dni, String nombre, String apellido, String correo, String celular){
        this.dni=dni;
        this.nombre= nombre;
        this.apellido= apellido;
        this.correoElectronico=correo;
        this.celular=celular;
    }

    /**
     * Devuelve nombre y apellido juntos (no es un atributo, se arma al pedirlo).
     * @return el nombre seguido del apellido
     */
    public String getNombreCompleto(){
        return nombre + " " + apellido;
    }

    /** Devuelve el DNI. */
    public int getDni() {
        return dni;
    }

    /** Establece el DNI. */
    public void setDni(int dni) {
        this.dni = dni;
    }

    /** Devuelve el nombre. */
    public String getNombre() {
        return nombre;
    }

    /** Establece el nombre. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Devuelve el apellido. */
    public String getApellido() {
        return apellido;
    }

    /** Establece el apellido. */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /** Devuelve el correo electrónico. */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /** Establece el correo electrónico. */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    /** Devuelve el número de celular. */
    public String getCelular() {
        return celular;
    }

    /** Establece el número de celular. */
    public void setCelular(String celular) {
        this.celular = celular;
    }

    /** Devuelve los datos de la persona en formato de texto. */
    @Override
    public String toString() {
        return "DNI: " + dni + "\n" +
            "Nombre: " + getNombreCompleto() + "\n" +
            "Correo electrónico: " + correoElectronico + "\n" +
            "Celular: " + celular;
    }
    
}