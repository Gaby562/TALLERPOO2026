package SISTEMA;
import CLASES.*;

/** Une una estación con su acumulado de lluvia en un rango de fechas. */
public class PuestoRanking {
    private EstacionMeteorologica estacion;
    private double acumulado;

    /**Constructor sin parametros. Inicializa con valores por defecto.*/
    public PuestoRanking() {
        this.estacion=null;
        this.acumulado=0;
    }

    /**
     * Crea un puesto del ranking.
     *
     * @param estacion estación del puesto
     * @param acumulado milímetros acumulados por la estación
     */
    public PuestoRanking(EstacionMeteorologica estacion, double acumulado) {
        this.estacion = estacion;
        this.acumulado = acumulado;
    }

    /** Devuelve la estación del puesto. */
    public EstacionMeteorologica getEstacion() {
        return estacion;
    }

    /** Devuelve los milímetros acumulados por la estación. */
    public double getAcumulado() {
        return acumulado;
    }

    /** Devuelve el nombre, el ID y el acumulado de la estación en formato de texto. */
    @Override
    public String toString() {
        return estacion.getNombre() + " (ID " + estacion.getIdEstacion() + "): " + acumulado + " mm";
    }
    
}
