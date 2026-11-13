package enums;
/** Tipo de mantenimiento realizado en una estación o sensor. */
public enum TipoMantenimiento{
    /** Mantenimiento programado para prevenir fallas. */
    PREVENTIVO,
    /** Mantenimiento para reparar una falla. */
    CORRECTIVO,
    /** Calibración de un sensor; actualiza su fecha de última calibración. */
    CALIBRACION
}