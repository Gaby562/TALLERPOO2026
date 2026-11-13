package CLASES;

/**
 * Acumulado medio de precipitación (en mm) de una zona para un mes del año.
 * Se usa como referencia histórica para calcular la anomalía climática.
 */
public class ValorHistorico {
    private int mes;
    private double acumuladoMedio;

    /** Constructor sin parametros. Inicializa con valores por defecto.*/
    public ValorHistorico() {
        this.mes = 1;
        this.acumuladoMedio = 0;
    }

    /**
     * Crea un valor histórico validando mes y acumulado a través de los setters.
     * @param mes mes del año, de 1 (enero) a 12 (diciembre)
     * @param acumuladoMedio acumulado medio en mm, no negativo
     * @throws IllegalArgumentException si el mes o el acumulado no son válidos
     */
    public ValorHistorico(int mes, double acumuladoMedio) {
        setMes(mes);
        setAcumuladoMedio(acumuladoMedio);
    }

    /** Devuelve el mes (1 a 12). */
    public int getMes() {
        return mes;
    }

    /**
     * Establece el mes.
     * @param mes mes del año, de 1 (enero) a 12 (diciembre)
     * @throws IllegalArgumentException si el mes no está entre 1 y 12
     */
    public void setMes(int mes) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }
        this.mes = mes;
    }

    /** Devuelve el acumulado medio en mm. */
    public double getAcumuladoMedio() {
        return acumuladoMedio;
    }

    /**
     * Establece el acumulado medio.
     * @param acumuladoMedio acumulado medio en mm
     * @throws IllegalArgumentException si el valor es negativo
     */
    public void setAcumuladoMedio(double acumuladoMedio) {
        if (acumuladoMedio < 0) {
            throw new IllegalArgumentException("El acumulado medio no puede ser negativo.");
        }
        this.acumuladoMedio = acumuladoMedio;
    }

    /** Devuelve los datos del valor histórico en formato de texto. */
    @Override
    public String toString() {
        return "VALOR HISTÓRICO\n" +
            "Mes: " + mes + "\n" +
            "Acumulado medio: " + acumuladoMedio + " mm";
    }

}
