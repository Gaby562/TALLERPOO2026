public class ValorHistorico {
    
    private int mes;
    private double acumuladoMedio;

    public ValorHistorico(int mes, double acumuladoMedio) {
        this.mes = mes;
        this.acumuladoMedio = acumuladoMedio;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public double getAcumuladoMedio() {
        return acumuladoMedio;
    }

    public void setAcumuladoMedio(double acumuladoMedio) {
        this.acumuladoMedio = acumuladoMedio;
    }

}
