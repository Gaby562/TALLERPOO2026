package SISTEMA;
import CLASES.EstacionMeteorologica;
// Une una estación con su acumulado de lluvia en un rango de fechas
public class PuestoRanking {
    private EstacionMeteorologica estacion;
    private double acumulado;

    public PuestoRanking() {
        this.estacion=null;
        this.acumulado=0;
    }

    public PuestoRanking(EstacionMeteorologica estacion, double acumulado) {
        this.estacion = estacion;
        this.acumulado = acumulado;
    }

    public EstacionMeteorologica getEstacion() {
        return estacion;
    }

    public double getAcumulado() {
        return acumulado;
    }

    @Override
    public String toString() {
        return estacion.getNombre() + " (ID " + estacion.getIdEstacion() + "): " + acumulado + " mm";
    }
    
}
