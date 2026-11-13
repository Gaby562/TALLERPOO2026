package enums;
/** Estado operativo de una estación o de un sensor. */
public enum EstadoOperativo {
    /** En funcionamiento normal; admite lecturas. */
    OPERATIVO,
    /** En mantenimiento; no admite lecturas. */
    EN_MANTENIMIENTO,
    /** Fuera de servicio; no admite lecturas. */
    FUERA_DE_SERVICIO
}
