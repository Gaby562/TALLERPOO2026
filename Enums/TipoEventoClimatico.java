package enums;
/** Tipo de evento climático que origina una alerta. */
public enum TipoEventoClimatico{
    /** Lluvia copiosa; la genera el sistema al superarse un umbral. */
    LLUVIA_COPIOSA, 
    /** Crecida de río; la registra un operador. */
    CRECIDA_RIO, 
    /** Tormenta severa; la registra un operador. */
    TORMENTA_SEVERA
}