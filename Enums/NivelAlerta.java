package enums;
/** Nivel de severidad de una alerta, de menor a mayor. */
public enum NivelAlerta {
    /** Sin riesgo: la lluvia no alcanza el umbral amarillo. */
    VERDE,
    /** Riesgo bajo: se alcanzó el umbral amarillo. */
    AMARILLA,
    /** Riesgo alto: se alcanzó el umbral naranja. Requiere equipo de respuesta. */
    NARANJA,
    /** Riesgo crítico: se alcanzó el umbral rojo. Requiere equipo de respuesta. */
    ROJA
}
