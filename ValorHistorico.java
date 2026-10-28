public class ValorHistorico {
    private int mes;
    private double acumuladoMedio;

    // Valida mes y acumulado a través de los setters
    public ValorHistorico(int mes, double acumuladoMedio) {
        setMes(mes);
        setAcumuladoMedio(acumuladoMedio);
    }

    public int getMes() {
        return mes;
    }

    // El mes debe estar entre 1 (enero) y 12 (diciembre)
    public void setMes(int mes) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }
        this.mes = mes;
    }

    public double getAcumuladoMedio() {
        return acumuladoMedio;
    }

    // El acumulado medio no puede ser negativo
    public void setAcumuladoMedio(double acumuladoMedio) {
        if (acumuladoMedio < 0) {
            throw new IllegalArgumentException("El acumulado medio no puede ser negativo.");
        }
        this.acumuladoMedio = acumuladoMedio;
    }

    @Override
    public String toString() {
        return "VALOR HISTÓRICO\n" +
            "Mes: " + mes + "\n" +
            "Acumulado medio: " + acumuladoMedio + " mm";
    }

}
