package SISTEMA;

import java.util.Comparator;

/** Ordena los puestos del ranking de mayor a menor acumulado. */
public class OrdenarPorAcumuladoDesc implements Comparator<PuestoRanking> {
    /**
     * Compara dos puestos por milímetros acumulados, de forma que el mayor acumulado quede primero.
     * @param a primer puesto
     * @param b segundo puesto
     * @return un valor negativo si {@code a} acumuló más que {@code b}, positivo si acumuló menos, 0 si son iguales
     */
    @Override
    public int compare(PuestoRanking a, PuestoRanking b) {
        return Double.compare(b.getAcumulado(), a.getAcumulado());
    }
}