public abstract class Persona{
    private String dni;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String celular;

    public Persona(){

    }

    public Persona(String dni, String nombre, String apellido, String correo, String celular){
        this.dni=dni;
        this.nombre= nombre;
        this.apellido= apellido;
        this.correoElectronico=correo;
        this.celular=celular;
    }

    public String getNombreCompleto(){
        return nombre + " " + apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
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
        return "Persona [dni: " + dni + ", nombre: " + nombre + ", apellido: " + apellido + ", correoElectronico: " + correoElectronico + ", celular: " + celular + "]";
    }

    
}