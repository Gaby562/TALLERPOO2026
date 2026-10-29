public class ResponsableTecnico extends Persona{
    private String especialidad;
    private String numeroLicencia;

    public ResponsableTecnico(){

    }

    public ResponsableTecnico (int dni, String nombre, String apellido, String correo, String celular, String especialidad, String numeroLicencia){
        super(dni, nombre, apellido, correo, celular);
        this.especialidad= especialidad;
        this.numeroLicencia= numeroLicencia;
    }

    public void setEspecialidad(String especialidad){
        this.especialidad= especialidad;
    }
    public String getEspecialidad(){
        return especialidad;
    }

    public void setNumeroLicencia(String numeroLicencia){
        this.numeroLicencia= numeroLicencia;
    }
    public String getNumeroLicencia(){
        return numeroLicencia;
    }

    @Override
    public String toString() {
        return "RESPONSABLE TÉCNICO\n" +
            super.toString() + "\n" +
            "Especialidad: " + especialidad + "\n" +
            "Licencia: " + numeroLicencia;
    }

}