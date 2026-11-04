package SISTEMA;

import java.util.Comparator;

// Ordena de mayor a menor acumulado
public class OrdenarPorAcumuladoDesc implements Comparator<PuestoRanking> {
    @Override
    public int compare(PuestoRanking a, PuestoRanking b) {
        // b primero y a después: invierte el orden (mayor a menor)
        return Double.compare(b.getAcumulado(), a.getAcumulado());
    }
}
