package MAIN;

import SISTEMA.SistemaMeteorologico;

/** Punto de entrada del programa. */
public class Main {
    /**
     * Crea el sistema, le carga los datos de prueba y abre el menú.
     *
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        // Crea el sistema, le carga los datos de prueba y abre el menú
        SistemaMeteorologico sistema = new SistemaMeteorologico();
        CargadorDatos.cargar(sistema);
        new Menu(sistema).iniciar();
    }
}
