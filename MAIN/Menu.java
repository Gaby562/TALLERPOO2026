package MAIN;

import SISTEMA.*;
import CLASES.*;
import EXCEPCIONES.*;
import enums.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
 
public class Menu {
    private SistemaMeteorologico sistema;
    private ReportesPrecipitaciones precipitaciones;
    private ReportesOperativos operativos;
    private Scanner scanner;
 
    public Menu(SistemaMeteorologico sistema) {
        this.sistema = sistema;
        this.precipitaciones = new ReportesPrecipitaciones(sistema);
        this.operativos = new ReportesOperativos(sistema);
        this.scanner = new Scanner(System.in);
    }
 
 
    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
 
    // Repite la pregunta hasta que el usuario escriba un entero válido
    private int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Ingresá un número entero válido.");
            }
        }
    }
 
    // Acepta punto o coma como separador decimal
    private double leerDecimal(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ingresá un número válido.");
            }
        }
    }
 
    private LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje + " (aaaa-mm-dd): "));
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Formato: aaaa-mm-dd.");
            }
        }
    }
 
    // Muestra los valores de cualquier enum numerados y devuelve el elegido
    private <T extends Enum<T>> T leerEnum(Class<T> clase, String titulo) {
        T[] valores = clase.getEnumConstants();
        System.out.println(titulo);
        for (int i = 0; i < valores.length; i++) {
            System.out.println("  " + (i + 1) + ". " + valores[i]);
        }
        int opcion;
        do {
            opcion = leerEntero("Opción: ");
        } while (opcion < 1 || opcion > valores.length);
        return valores[opcion - 1];
    }
 
    // Pide un rango de fechas; devuelve null si es inválido
    private LocalDate[] leerRango() {
        LocalDate desde = leerFecha("Fecha desde");
        LocalDate hasta = leerFecha("Fecha hasta");
        if (desde.isAfter(hasta)) {
            System.out.println("La fecha desde no puede ser posterior a la fecha hasta.");
            return null;
        }
        return new LocalDate[] {desde, hasta};
    }
 
    // ==================== BÚSQUEDAS CON AVISO ====================
    // Devuelven null si no existe, y el que llama corta la operación
 
    private ZonaGeografica pedirZona() {
        ZonaGeografica z = sistema.buscarZonaPorId(leerEntero("ID de la zona: "));
        if (z == null) {
            System.out.println("No existe esa zona.");
        }
        return z;
    }
 
    private EstacionMeteorologica pedirEstacion() {
        EstacionMeteorologica e = sistema.buscarEstacionPorId(leerEntero("ID de la estación: "));
        if (e == null) {
            System.out.println("No existe esa estación.");
        }
        return e;
    }
 
    private Sensor pedirSensor() {
        Sensor s = sistema.buscarSensorPorId(leerEntero("ID del sensor: "));
        if (s == null) {
            System.out.println("No existe ese sensor.");
        }
        return s;
    }
 
    private ResponsableTecnico pedirTecnico() {
        ResponsableTecnico t = sistema.buscarTecnicoPorDni(leerEntero("DNI del técnico: "));
        if (t == null) {
            System.out.println("No existe ese técnico.");
        }
        return t;
    }
 
    private EquipoRespuesta pedirEquipo() {
        EquipoRespuesta e = sistema.buscarEquipoPorId(leerEntero("ID del equipo: "));
        if (e == null) {
            System.out.println("No existe ese equipo.");
        }
        return e;
    }
 
    // Busca una alerta por id recorriendo las alertas de todas las zonas
    private Alerta buscarAlerta(int id) {
        for (ZonaGeografica z : sistema.getZonas()) {
            for (Alerta a : z.getAlertas()) {
                if (a.getIdAlerta() == id) {
                    return a;
                }
            }
        }
        return null;
    }
 
    // Imprime cada elemento de la lista separado por una línea
    private void listar(List<?> lista, String vacio) {
        if (lista.isEmpty()) {
            System.out.println(vacio);
            return;
        }
        for (Object o : lista) {
            System.out.println(o);
            System.out.println("----------");
        }
    }
 
    // ==================== MENÚ PRINCIPAL ====================
 
    public void iniciar() {
        int opcion;
        do {
            System.out.println("\n===== SISTEMA DE ESTACIONES METEOROLÓGICAS =====");
            System.out.println("1. Infraestructura");
            System.out.println("2. Personal técnico");
            System.out.println("3. Registrar lectura de lluvia");
            System.out.println("4. Alertas");
            System.out.println("5. Reportes");
            System.out.println("0. Salir");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: menuInfraestructura(); break;
                case 2: menuPersonal(); break;
                case 3: registrarLectura(); break;
                case 4: menuAlertas(); break;
                case 5: menuReportes(); break;
                case 0: System.out.println("Hasta luego."); break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
 
    // ==================== INFRAESTRUCTURA ====================
 
    private void menuInfraestructura() {
        int opcion;
        do {
            System.out.println("\n--- INFRAESTRUCTURA ---");
            System.out.println("1. Listar zonas");
            System.out.println("2. Listar estaciones");
            System.out.println("3. Listar sensores");
            System.out.println("4. Agregar zona");
            System.out.println("5. Agregar estación");
            System.out.println("6. Agregar sensor");
            System.out.println("7. Instalar sensor en una estación");
            System.out.println("8. Retirar sensor de su estación");
            System.out.println("9. Cambiar estado de una estación");
            System.out.println("10. Cambiar estado de un sensor");
            System.out.println("11. Registrar mantenimiento");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: listar(sistema.getZonas(), "No hay zonas."); break;
                case 2: listar(sistema.getEstaciones(), "No hay estaciones."); break;
                case 3: listar(sistema.getSensores(), "No hay sensores."); break;
                case 4: agregarZona(); break;
                case 5: agregarEstacion(); break;
                case 6: agregarSensor(); break;
                case 7: instalarSensor(); break;
                case 8: retirarSensor(); break;
                case 9: cambiarEstadoEstacion(); break;
                case 10: cambiarEstadoSensor(); break;
                case 11: registrarMantenimiento(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
 
    
    private void agregarZona() {
        int id = leerEntero("ID: ");
        String nombre = leerTexto("Nombre: ");
        NivelVulnerabilidad nivel = leerEnum(NivelVulnerabilidad.class, "Nivel de vulnerabilidad:");

        double amarilla, naranja, roja;
        // Se ofrece la escala de referencia de la cátedra (50 / 100 / 150 mm)
        if (leerTexto("¿Usar la escala de referencia 50/100/150 mm? (s/n): ").equalsIgnoreCase("s")) {
            amarilla = ZonaGeografica.UMBRAL_AMARILLA_REF;
            naranja = ZonaGeografica.UMBRAL_NARANJA_REF;
            roja = ZonaGeografica.UMBRAL_ROJA_REF;
        } else {
            amarilla = leerDecimal("Umbral amarilla (mm en 24 h): ");
            naranja = leerDecimal("Umbral naranja (mm en 24 h): ");
            roja = leerDecimal("Umbral roja (mm en 24 h): ");
        }

        try {
            sistema.agregarZona(new ZonaGeografica(id, nombre, nivel, amarilla, naranja, roja));
            System.out.println("Zona agregada.");
        } catch (ElementoDuplicadoException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void agregarEstacion() {
        int id = leerEntero("ID: ");
        String nombre = leerTexto("Nombre: ");
        double latitud = leerDecimal("Latitud: ");
        double longitud = leerDecimal("Longitud: ");
        double altitud = leerDecimal("Altitud (m): ");
        EstadoOperativo estado = leerEnum(EstadoOperativo.class, "Estado:");
        TipoEstacion tipo = leerEnum(TipoEstacion.class, "Tipo de estación:");
        ResponsableTecnico responsable = pedirTecnico();
        if (responsable == null) {
            return;
        }
        ZonaGeografica zona = pedirZona();
        if (zona == null) {
            return;
        }
        try {
            sistema.agregarEstacion(new EstacionMeteorologica(id, nombre, latitud, longitud,
                altitud, estado, tipo, responsable), zona);
            System.out.println("Estación agregada.");
        } catch (ElementoDuplicadoException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void agregarSensor() {
        int id = leerEntero("ID: ");
        String marca = leerTexto("Marca: ");
        String modelo = leerTexto("Modelo: ");
        double precision = leerDecimal("Precisión: ");
        EstadoOperativo estado = leerEnum(EstadoOperativo.class, "Estado:");
        LocalDate calibracion = leerFecha("Fecha de última calibración");
        try {
            sistema.agregarSensor(new Sensor(id, marca, modelo, precision, estado, calibracion));
            System.out.println("Sensor agregado. Falta instalarlo en una estación.");
        } catch (ElementoDuplicadoException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void instalarSensor() {
        Sensor s = pedirSensor();
        if (s == null) {
            return;
        }
        EstacionMeteorologica e = pedirEstacion();
        if (e == null) {
            return;
        }
        try {
            sistema.instalarSensor(s, e);
            System.out.println("Sensor instalado.");
        } catch (SensorYaInstaladoException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
 
    private void retirarSensor() {
        Sensor s = pedirSensor();
        if (s == null) {
            return;
        }
        try {
            sistema.retirarSensor(s);
            System.out.println("Sensor retirado.");
        } catch (SensorSinEstacionException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
 
    private void cambiarEstadoEstacion() {
        EstacionMeteorologica e = pedirEstacion();
        if (e == null) {
            return;
        }
        EstadoOperativo estado = leerEnum(EstadoOperativo.class, "Nuevo estado:");
        System.out.println(sistema.cambiarEstadoEstacion(e, estado) ? "Estado actualizado." : "No se pudo cambiar el estado.");
    }
 
    private void cambiarEstadoSensor() {
        Sensor s = pedirSensor();
        if (s == null) {
            return;
        }
        EstadoOperativo estado = leerEnum(EstadoOperativo.class, "Nuevo estado:");
        System.out.println(sistema.cambiarEstadoSensor(s, estado) ? "Estado actualizado." : "No se pudo cambiar el estado.");
    }
 
    private void registrarMantenimiento() {
        EstacionMeteorologica e = pedirEstacion();
        if (e == null) {
            return;
        }
        int id = leerEntero("ID del mantenimiento: ");
        TipoMantenimiento tipo = leerEnum(TipoMantenimiento.class, "Tipo de mantenimiento:");
        LocalDate fecha = leerFecha("Fecha");
        String descripcion = leerTexto("Descripción: ");
 
        // El sensor es opcional: 0 significa mantenimiento general de la estación
        Sensor sensor = null;
        int idSensor = leerEntero("ID del sensor (0 si es de toda la estación): ");
        if (idSensor != 0) {
            sensor = sistema.buscarSensorPorId(idSensor);
            if (sensor == null) {
                System.out.println("No existe ese sensor.");
                return;
            }
        }
        ResponsableTecnico responsable = pedirTecnico();
        if (responsable == null) {
            return;
        }
        try {
            sistema.registrarMantenimiento(e, id, fecha, tipo, descripcion, sensor, responsable);
            System.out.println("Mantenimiento registrado.");
            if (tipo == TipoMantenimiento.CALIBRACION && sensor != null) {
                System.out.println("Se actualizó la fecha de calibración del sensor.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
 
    // ==================== PERSONAL TÉCNICO ====================
 
    private void menuPersonal() {
        int opcion;
        do {
            System.out.println("\n--- PERSONAL TÉCNICO ---");
            System.out.println("1. Listar técnicos");
            System.out.println("2. Listar equipos de respuesta");
            System.out.println("3. Agregar técnico");
            System.out.println("4. Agregar equipo de respuesta");
            System.out.println("5. Agregar integrante a un equipo");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: listar(sistema.getTecnicos(), "No hay técnicos."); break;
                case 2: listar(sistema.getEquipos(), "No hay equipos."); break;
                case 3: agregarTecnico(); break;
                case 4: agregarEquipo(); break;
                case 5: agregarIntegrante(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
 
    private void agregarTecnico() {
        int dni = leerEntero("DNI: ");
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String correo = leerTexto("Correo electrónico: ");
        String celular = leerTexto("Celular: ");
        String especialidad = leerTexto("Especialidad: ");
        String licencia = leerTexto("Número de licencia: ");
        try {
            sistema.agregarTecnico(new ResponsableTecnico(dni, nombre, apellido, correo,
                celular, especialidad, licencia));
            System.out.println("Técnico agregado.");
        } catch (ElementoDuplicadoException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void agregarEquipo() {
        int id = leerEntero("ID: ");
        String nombre = leerTexto("Nombre del equipo: ");
        try {
            // Un equipo nuevo está disponible; los integrantes se agregan después
            sistema.agregarEquipo(new EquipoRespuesta(id, nombre, true));
            System.out.println("Equipo agregado. Falta agregarle integrantes.");
        } catch (ElementoDuplicadoException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void agregarIntegrante() {
        EquipoRespuesta equipo = pedirEquipo();
        if (equipo == null) {
            return;
        }
        ResponsableTecnico tecnico = pedirTecnico();
        if (tecnico == null) {
            return;
        }
        try {
            sistema.agregarIntegrante(equipo, tecnico);
            System.out.println("Integrante agregado.");
        } catch (TecnicoYaAsignadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    // ==================== LECTURAS ====================
 
    private void registrarLectura() {
        Sensor sensor = pedirSensor();
        if (sensor == null) {
            return;
        }
        double mm = leerDecimal("Milímetros registrados: ");
        try {
            // La lectura se registra con la fecha y hora actuales
            Medicion m = sistema.registrarLectura(sensor, mm, LocalDateTime.now());
            System.out.println("Lectura registrada:\n" + m);
            if (m.getAnomala()) {
                System.out.println("ATENCIÓN: la lectura difiere más de 100 mm de la anterior y quedó marcada como anómala.");
            }
            // Avisa si el sistema generó alertas que todavía necesitan un equipo
            int pendientes = sistema.alertasPendientes().size();
            if (pendientes > 0) {
                System.out.println("Hay " + pendientes + " alerta(s) NARANJA o ROJA sin equipo asignado.");
            }
        } catch (SensorSinEstacionException | EstacionNoOperativaException | LecturaInvalidaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    // ==================== ALERTAS ====================
 
    private void menuAlertas() {
        int opcion;
        do {
            System.out.println("\n--- ALERTAS ---");
            System.out.println("1. Registrar alerta de operador (crecida de río o tormenta severa)");
            System.out.println("2. Ver alertas pendientes de equipo");
            System.out.println("3. Asignar equipo a una alerta");
            System.out.println("4. Liberar equipo (terminó la emergencia)");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: registrarAlertaOperador(); break;
                case 2: listar(sistema.alertasPendientes(), "No hay alertas pendientes."); break;
                case 3: asignarEquipo(); break;
                case 4: liberarEquipo(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
 
    private void registrarAlertaOperador() {
        ZonaGeografica zona = pedirZona();
        if (zona == null) {
            return;
        }
        // El operador solo puede cargar estos dos tipos (LLUVIA_COPIOSA la genera el sistema)
        System.out.println("Tipo de evento:\n  1. CRECIDA_RIO\n  2. TORMENTA_SEVERA");
        int tipo = leerEntero("Opción: ");
        if (tipo != 1 && tipo != 2) {
            System.out.println("Opción inválida.");
            return;
        }
        TipoEventoClimatico evento = (tipo == 1) ? TipoEventoClimatico.CRECIDA_RIO : TipoEventoClimatico.TORMENTA_SEVERA;
        NivelAlerta nivel = leerEnum(NivelAlerta.class, "Nivel de la alerta:");
        String descripcion = leerTexto("Descripción: ");
        try {
            Alerta a = sistema.registrarAlertaOperador(zona, evento, nivel, descripcion);
            System.out.println("Alerta registrada:\n" + a);
            if (a.requiereEquipo()) {
                System.out.println("Esta alerta requiere asignar un equipo de respuesta.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void asignarEquipo() {
        // Muestra las alertas que faltan y los equipos libres para facilitar la elección
        listar(sistema.alertasPendientes(), "No hay alertas pendientes.");
        if (sistema.alertasPendientes().isEmpty()) {
            return;
        }
        Alerta alerta = buscarAlerta(leerEntero("ID de la alerta: "));
        if (alerta == null) {
            System.out.println("No existe esa alerta.");
            return;
        }
        System.out.println("Equipos disponibles:");
        listar(sistema.equiposDisponibles(), "No hay equipos disponibles.");
        EquipoRespuesta equipo = pedirEquipo();
        if (equipo == null) {
            return;
        }
        try {
            sistema.asignarEquipo(alerta, equipo);
            System.out.println("Equipo asignado. El equipo queda no disponible hasta que se lo libere.");
        } catch (AlertaSinEquipoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void liberarEquipo() {
        Alerta alerta = buscarAlerta(leerEntero("ID de la alerta atendida: "));
        if (alerta == null) {
            System.out.println("No existe esa alerta.");
            return;
        }
        try {
            sistema.liberarEquipo(alerta);
            System.out.println("Equipo liberado.");
        } catch (AlertaSinEquipoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    // ==================== REPORTES ====================
 
    private void menuReportes() {
        int opcion;
        do {
            System.out.println("\n--- REPORTES ---");
            System.out.println("1. Histórico de precipitaciones (diario, semanal y mensual)");
            System.out.println("2. Ranking de estaciones por volumetría");
            System.out.println("3. Historial de alertas emitidas");
            System.out.println("4. Ficha técnica de una estación");
            System.out.println("5. Informe de fallas y mantenimiento");
            System.out.println("6. Estadística de anomalía climática");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: reporteHistorico(); break;
                case 2: reporteRanking(); break;
                case 3: reporteHistorialAlertas(); break;
                case 4: reporteFicha(); break;
                case 5: System.out.println("\n" + operativos.informeFallas()); break;
                case 6: reporteAnomalia(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
 
    private void reporteHistorico() {
        System.out.println("1. Por estación\n2. Por zona");
        int tipo = leerEntero("Opción: ");
        if (tipo != 1 && tipo != 2) {
            System.out.println("Opción inválida.");
            return;
        }
        LocalDate[] rango = leerRango();
        if (rango == null) {
            return;
        }
 
        Map<LocalDate, Double> diario;
        ZonaGeografica zona = null;
        if (tipo == 1) {
            EstacionMeteorologica e = pedirEstacion();
            if (e == null) {
                return;
            }
            diario = precipitaciones.historicoDiario(e, rango[0], rango[1]);
        } else {
            zona = pedirZona();
            if (zona == null) {
                return;
            }
            diario = precipitaciones.historicoDiario(zona, rango[0], rango[1]);
        }
 
        if (diario.isEmpty()) {
            System.out.println("No hay lecturas en ese rango.");
            return;
        }
        System.out.println("\n-- Diario --");
        for (Map.Entry<LocalDate, Double> d : diario.entrySet()) {
            System.out.println(d.getKey() + ": " + d.getValue() + " mm");
        }
        System.out.println("\n-- Semanal (la fecha es el lunes de la semana) --");
        for (Map.Entry<LocalDate, Double> s : precipitaciones.acumuladoSemanal(diario).entrySet()) {
            System.out.println(s.getKey() + ": " + s.getValue() + " mm");
        }
        System.out.println("\n-- Mensual --");
        for (Map.Entry<YearMonth, Double> m : precipitaciones.acumuladoMensual(diario).entrySet()) {
            System.out.println(m.getKey() + ": " + m.getValue() + " mm");
        }
        // Solo las zonas tienen valores históricos para comparar
        if (zona != null) {
            System.out.println("\n" + precipitaciones.compararConHistorico(zona, rango[0], rango[1]));
        }
    }
 
    private void reporteRanking() {
        LocalDate[] rango = leerRango();
        if (rango == null) {
            return;
        }
        List<PuestoRanking> ranking = precipitaciones.rankingEstaciones(rango[0], rango[1]);
        System.out.println("\nRANKING DE ESTACIONES POR VOLUMETRÍA");
        int puesto = 1;
        for (PuestoRanking p : ranking) {
            System.out.println(puesto + ". " + p);
            puesto++;
        }
    }
 
    private void reporteHistorialAlertas() {
        LocalDate[] rango = leerRango();
        if (rango == null) {
            return;
        }
        // Los filtros de nivel y zona son opcionales
        NivelAlerta nivel = null;
        if (leerTexto("¿Filtrar por nivel? (s/n): ").equalsIgnoreCase("s")) {
            nivel = leerEnum(NivelAlerta.class, "Nivel:");
        }
        ZonaGeografica zona = null;
        int idZona = leerEntero("ID de la zona (0 = todas): ");
        if (idZona != 0) {
            zona = sistema.buscarZonaPorId(idZona);
            if (zona == null) {
                System.out.println("No existe esa zona.");
                return;
            }
        }
        System.out.println("\nHISTORIAL DE ALERTAS");
        listar(operativos.historialAlertas(nivel, rango[0], rango[1], zona), "No hay alertas con esos filtros.");
    }
 
    private void reporteFicha() {
        EstacionMeteorologica e = pedirEstacion();
        if (e != null) {
            System.out.println("\n" + operativos.fichaTecnica(e));
        }
    }
 
    private void reporteAnomalia() {
        ZonaGeografica zona = pedirZona();
        if (zona == null) {
            return;
        }
        int anio = leerEntero("Año: ");
        int mes = leerEntero("Mes (1 a 12): ");
        try {
            System.out.println("\n" + precipitaciones.informeAnomalia(zona, anio, mes));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
