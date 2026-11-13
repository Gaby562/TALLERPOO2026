package SISTEMA;

import CLASES.*;
import java.util.Comparator;

/** Ordena las alertas de la más reciente a la más antigua. */
public class OrdenarAlertasPorFechaDesc implements Comparator<Alerta> {
    /**
     * Compara dos alertas por fecha y hora de emisión, de forma que la más reciente quede primero.
     * @param a primera alerta
     * @param b segunda alerta
     * @return un valor negativo si {@code a} es más reciente que {@code b}, positivo si es más antigua, 0 si son iguales
     */
    @Override
    public int compare(Alerta a, Alerta b) {
        return b.getFechaHoraEmision().compareTo(a.getFechaHoraEmision());
    }
}