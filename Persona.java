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

    
}