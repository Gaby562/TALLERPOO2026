package MAIN;

import SISTEMA.SistemaMeteorologico;

public class Main {
    public static void main(String[] args) {
        // Crea el sistema, le carga los datos de prueba y abre el menú
        SistemaMeteorologico sistema = new SistemaMeteorologico();
        CargadorDatos.cargar(sistema);
        new Menu(sistema).iniciar();
    }
}
