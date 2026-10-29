public abstract class Persona{
    private int dni;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String celular;

    public Persona(){
        this.dni = 0;
        this.nombre = "";
        this.apellido = "";
        this.correoElectronico = "";
        this.celular = "";
    }

    public Persona(int dni, String nombre, String apellido, String correo, String celular){
        this.dni=dni;
        this.nombre= nombre;
        this.apellido= apellido;
        this.correoElectronico=correo;
        this.celular=celular;
    }

    // Devuelve nombre y apellido juntos (no es un atributo, se arma al pedirlo)
    public String getNombreCompleto(){
        return nombre + " " + apellido;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Override
    public String toString() {
        return "DNI: " + dni + "\n" +
            "Nombre: " + getNombreCompleto() + "\n" +
            "Correo electrónico: " + correoElectronico + "\n" +
            "Celular: " + celular;
    }

    
}