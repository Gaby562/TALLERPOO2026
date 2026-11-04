package SISTEMA;

import CLASES.Alerta;
import java.util.Comparator;

// Ordena las alertas de la más reciente a la más antigua
public class OrdenarAlertasPorFechaDesc implements Comparator<Alerta> {
    @Override
    public int compare(Alerta a, Alerta b) {
        // b primero y a después: invierte el orden (más reciente primero)
        return b.getFechaHoraEmision().compareTo(a.getFechaHoraEmision());
    }
}