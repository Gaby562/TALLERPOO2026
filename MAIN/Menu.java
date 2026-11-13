package MAIN;

import SISTEMA.*;
import CLASES.*;
import EXCEPCIONES.*;
import enums.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Interfaz de consola del sistema. Muestra menús numerados, listas de
 * opciones antes de pedir cada ID, permite cancelar con 0 y presenta los
 * reportes en forma de tabla.
 */
public class Menu {
    private static final String LINEA = "============================================================";
    private static final String SUBLINEA = "------------------------------------------------------------";
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private SistemaMeteorologico sistema;
    private ReportesPrecipitaciones precipitaciones;
    private ReportesOperativos operativos;
    private Scanner scanner;

    /**
     * Crea el menú sobre un sistema ya cargado.
     * @param sistema sistema con los datos
     */
    public Menu(SistemaMeteorologico sistema) {
        this.sistema = sistema;
        this.precipitaciones = new ReportesPrecipitaciones(sistema);
        this.operativos = new ReportesOperativos(sistema);
        this.scanner = new Scanner(System.in);
    }

    // ==================== MENSAJES ====================

    private void titulo(String texto) {
        System.out.println("\n" + LINEA);
        System.out.println("  " + texto.toUpperCase());
        System.out.println(LINEA);
    }

    private void ok(String mensaje) {
        System.out.println("[OK] " + mensaje);
    }

    private void error(String mensaje) {
        System.out.println("[!] " + mensaje);
    }

    private void cancelado() {
        System.out.println("Operación cancelada.");
    }

    private void esperarEnter() {
        System.out.print("\nPresioná Enter para continuar...");
        scanner.nextLine();
    }

    // ==================== LECTURA DE DATOS ====================

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    // No acepta texto vacío
    private String leerTextoObligatorio(String mensaje) {
        while (true) {
            String t = leerTexto(mensaje);
            if (!t.isEmpty()) {
                return t;
            }
            error("Este dato no puede quedar vacío.");
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                error("Ingresá un número entero válido.");
            }
        }
    }

    private int leerOpcion() {
        return leerEntero("\nElegí una opción: ");
    }

    // Acepta punto o coma como separador decimal
    private double leerDecimal(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
            } catch (NumberFormatException e) {
                error("Ingresá un número válido (ej: 12.5).");
            }
        }
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            String t = leerTexto(mensaje + " (s/n): ").toLowerCase();
            if (t.equals("s") || t.equals("si") || t.equals("sí")) {
                return true;
            }
            if (t.equals("n") || t.equals("no")) {
                return false;
            }
            error("Respondé s o n.");
        }
    }

    // Si hay valor por defecto, con Enter se usa ese valor
    private LocalDate leerFecha(String mensaje, LocalDate porDefecto) {
        String sugerencia = (porDefecto != null) ? ", Enter = " + porDefecto : "";
        while (true) {
            String t = leerTexto(mensaje + " (aaaa-mm-dd" + sugerencia + "): ");
            if (t.isEmpty() && porDefecto != null) {
                return porDefecto;
            }
            try {
                return LocalDate.parse(t);
            } catch (DateTimeParseException e) {
                error("Fecha inválida. Ejemplo: 2026-11-09");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) {
        return leerFecha(mensaje, null);
    }

    // Con Enter se usa la fecha y hora actuales
    private LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            String t = leerTexto(mensaje + " (aaaa-mm-dd HH:mm, Enter = ahora): ");
            if (t.isEmpty()) {
                return LocalDateTime.now();
            }
            try {
                return LocalDateTime.parse(t, FORMATO_FECHA_HORA);
            } catch (DateTimeParseException e) {
                error("Formato inválido. Ejemplo: 2026-11-09 14:30");
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
        while (true) {
            opcion = leerEntero("Opción: ");
            if (opcion >= 1 && opcion <= valores.length) {
                return valores[opcion - 1];
            }
            error("Elegí un número entre 1 y " + valores.length + ".");
        }
    }

    // Pide un rango de fechas; por defecto, los últimos 7 días. Devuelve null si es inválido
    private LocalDate[] leerRango() {
        LocalDate hoy = LocalDate.now();
        LocalDate desde = leerFecha("Fecha desde", hoy.minusDays(7));
        LocalDate hasta = leerFecha("Fecha hasta", hoy);
        if (desde.isAfter(hasta)) {
            error("La fecha desde no puede ser posterior a la fecha hasta.");
            return null;
        }
        return new LocalDate[] {desde, hasta};
    }

    // ==================== LISTAS DE AYUDA Y BÚSQUEDAS ====================
    // Cada pedirX muestra las opciones disponibles, acepta 0 para cancelar
    // y devuelve null si se cancela o no existe.

    private void mostrarZonas() {
        System.out.println("Zonas disponibles:");
        for (ZonaGeografica z : sistema.getZonas()) {
            System.out.printf("  [%d] %s - vulnerabilidad %s - alerta %s%n",
                z.getIdZona(), z.getNombre(), z.getNivelVulnerabilidad(), z.getNivelAlertaActual());
        }
    }

    private void mostrarEstaciones() {
        System.out.println("Estaciones disponibles:");
        for (EstacionMeteorologica e : sistema.getEstaciones()) {
            System.out.printf("  [%d] %s - %s%n", e.getIdEstacion(), e.getNombre(), e.getEstado());
        }
    }

    private void mostrarSensores() {
        System.out.println("Sensores disponibles:");
        for (Sensor s : sistema.getSensores()) {
            EstacionMeteorologica e = sistema.buscarEstacionDeSensor(s);
            System.out.printf("  [%d] %s %s - %s - %s%n", s.getIdSensor(), s.getMarca(), s.getModelo(),
                s.getEstado(), (e != null ? "estación " + e.getNombre() : "sin instalar"));
        }
    }

    private void mostrarTecnicos() {
        System.out.println("Técnicos disponibles:");
        for (ResponsableTecnico t : sistema.getTecnicos()) {
            System.out.printf("  [%d] %s - %s%n", t.getDni(), t.getNombreCompleto(), t.getEspecialidad());
        }
    }

    private void mostrarEquipos(List<EquipoRespuesta> lista) {
        for (EquipoRespuesta e : lista) {
            System.out.printf("  [%d] %s - %d integrante(s) - %s%n", e.getIdEquipo(), e.getNombreEquipo(),
                e.getIntegrantes().size(), e.isDisponible() ? "disponible" : "ocupado");
        }
    }

    private ZonaGeografica pedirZona() {
        if (sistema.getZonas().isEmpty()) {
            error("No hay zonas cargadas.");
            return null;
        }
        mostrarZonas();
        int id = leerEntero("ID de la zona (0 para cancelar): ");
        if (id == 0) {
            cancelado();
            return null;
        }
        ZonaGeografica z = sistema.buscarZonaPorId(id);
        if (z == null) {
            error("No existe una zona con ese ID.");
        }
        return z;
    }

    private EstacionMeteorologica pedirEstacion() {
        if (sistema.getEstaciones().isEmpty()) {
            error("No hay estaciones cargadas.");
            return null;
        }
        mostrarEstaciones();
        int id = leerEntero("ID de la estación (0 para cancelar): ");
        if (id == 0) {
            cancelado();
            return null;
        }
        EstacionMeteorologica e = sistema.buscarEstacionPorId(id);
        if (e == null) {
            error("No existe una estación con ese ID.");
        }
        return e;
    }

    private Sensor pedirSensor() {
        if (sistema.getSensores().isEmpty()) {
            error("No hay sensores cargados.");
            return null;
        }
        mostrarSensores();
        int id = leerEntero("ID del sensor (0 para cancelar): ");
        if (id == 0) {
            cancelado();
            return null;
        }
        Sensor s = sistema.buscarSensorPorId(id);
        if (s == null) {
            error("No existe un sensor con ese ID.");
        }
        return s;
    }

    private ResponsableTecnico pedirTecnico() {
        if (sistema.getTecnicos().isEmpty()) {
            error("No hay técnicos cargados.");
            return null;
        }
        mostrarTecnicos();
        int dni = leerEntero("DNI del técnico (0 para cancelar): ");
        if (dni == 0) {
            cancelado();
            return null;
        }
        ResponsableTecnico t = sistema.buscarTecnicoPorDni(dni);
        if (t == null) {
            error("No existe un técnico con ese DNI.");
        }
        return t;
    }

    private EquipoRespuesta pedirEquipo(List<EquipoRespuesta> opciones) {
        if (opciones.isEmpty()) {
            error("No hay equipos para elegir.");
            return null;
        }
        System.out.println("Equipos:");
        mostrarEquipos(opciones);
        int id = leerEntero("ID del equipo (0 para cancelar): ");
        if (id == 0) {
            cancelado();
            return null;
        }
        EquipoRespuesta e = sistema.buscarEquipoPorId(id);
        if (e == null || !opciones.contains(e)) {
            error("Ese equipo no está entre las opciones.");
            return null;
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

    // Línea resumida de una alerta, con su zona
    private void mostrarAlertaResumen(ZonaGeografica z, Alerta a) {
        System.out.printf("  [%d] %s - %s - zona %s - %s%n", a.getIdAlerta(), a.getNivel(),
            a.getEvento(), z.getNombre(), a.getDescripcion());
    }

    // Muestra las alertas NARANJA/ROJA sin equipo; devuelve false si no hay ninguna
    private boolean mostrarPendientes() {
        boolean hay = false;
        for (ZonaGeografica z : sistema.getZonas()) {
            for (Alerta a : z.getAlertas()) {
                if (a.requiereEquipo() && a.getEquipo() == null) {
                    if (!hay) {
                        System.out.println("Alertas pendientes de equipo:");
                    }
                    mostrarAlertaResumen(z, a);
                    hay = true;
                }
            }
        }
        if (!hay) {
            System.out.println("No hay alertas pendientes de equipo.");
        }
        return hay;
    }

    // Imprime cada elemento de la lista con todos sus datos, separado por una línea
    private void listar(List<?> lista, String vacio) {
        if (lista.isEmpty()) {
            System.out.println(vacio);
        } else {
            for (Object o : lista) {
                System.out.println(o);
                System.out.println(SUBLINEA);
            }
        }
        esperarEnter();
    }

    // ==================== MENÚ PRINCIPAL ====================

    // Resumen del estado del sistema que se ve al entrar al menú principal
    private void mostrarTablero() {
        System.out.println("Zonas: " + sistema.getZonas().size()
            + "  |  Estaciones: " + sistema.getEstaciones().size()
            + "  |  Sensores: " + sistema.getSensores().size()
            + "  |  Técnicos: " + sistema.getTecnicos().size());
        System.out.println(SUBLINEA);
        System.out.println("Nivel de alerta por zona:");
        for (ZonaGeografica z : sistema.getZonas()) {
            System.out.printf("  %-30s %s%n", z.getNombre(), z.getNivelAlertaActual());
        }
        int pendientes = sistema.alertasPendientes().size();
        if (pendientes > 0) {
            System.out.println(SUBLINEA);
            System.out.println(">> ATENCIÓN: " + pendientes + " alerta(s) NARANJA/ROJA sin equipo asignado (opción 4).");
        }
    }

    /** Muestra el menú principal y atiende las opciones hasta que se elige salir. */
    public void iniciar() {
        int opcion;
        do {
            titulo("Sistema de estaciones meteorológicas");
            mostrarTablero();
            System.out.println(SUBLINEA);
            System.out.println("  1. Infraestructura (zonas, estaciones, sensores)");
            System.out.println("  2. Personal técnico y equipos");
            System.out.println("  3. Registrar lectura de lluvia");
            System.out.println("  4. Alertas");
            System.out.println("  5. Reportes");
            System.out.println("  0. Salir");
            opcion = leerOpcion();
            switch (opcion) {
                case 1: menuInfraestructura(); break;
                case 2: menuPersonal(); break;
                case 3: registrarLectura(); break;
                case 4: menuAlertas(); break;
                case 5: menuReportes(); break;
                case 0: System.out.println("\nHasta luego."); break;
                default: error("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // ==================== INFRAESTRUCTURA ====================

    private void menuInfraestructura() {
        int opcion;
        do {
            titulo("Infraestructura");
            System.out.println("  VER");
            System.out.println("    1. Listar zonas");
            System.out.println("    2. Listar estaciones");
            System.out.println("    3. Listar sensores");
            System.out.println("  AGREGAR");
            System.out.println("    4. Agregar zona");
            System.out.println("    5. Agregar estación");
            System.out.println("    6. Agregar sensor");
            System.out.println("  SENSORES");
            System.out.println("    7. Instalar sensor en una estación");
            System.out.println("    8. Retirar sensor de su estación");
            System.out.println("  ESTADOS Y MANTENIMIENTO");
            System.out.println("    9. Cambiar estado de una estación");
            System.out.println("   10. Cambiar estado de un sensor");
            System.out.println("   11. Registrar mantenimiento");
            System.out.println("  CONFIGURACIÓN DE ZONAS");
            System.out.println("   12. Modificar umbrales de una zona");
            System.out.println("   13. Cargar valor histórico mensual de una zona");
            System.out.println("    0. Volver");
            opcion = leerOpcion();
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
                case 12: modificarUmbrales(); break;
                case 13: cargarValorHistorico(); break;
                case 0: break;
                default: error("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void agregarZona() {
        titulo("Agregar zona");
        int id = leerEntero("ID (número positivo): ");
        String nombre = leerTextoObligatorio("Nombre: ");
        NivelVulnerabilidad nivel = leerEnum(NivelVulnerabilidad.class, "Nivel de vulnerabilidad:");

        double amarilla, naranja, roja;
        // Se ofrece la escala de referencia de la cátedra (50 / 100 / 150 mm)
        if (leerSiNo("¿Usar la escala de referencia 50/100/150 mm?")) {
            amarilla = ZonaGeografica.UMBRAL_AMARILLA_REF;
            naranja = ZonaGeografica.UMBRAL_NARANJA_REF;
            roja = ZonaGeografica.UMBRAL_ROJA_REF;
        } else {
            System.out.println("Los umbrales deben cumplir: amarilla < naranja < roja.");
            amarilla = leerDecimal("Umbral amarilla (mm en 24 h): ");
            naranja = leerDecimal("Umbral naranja (mm en 24 h): ");
            roja = leerDecimal("Umbral roja (mm en 24 h): ");
        }

        try {
            sistema.agregarZona(new ZonaGeografica(id, nombre, nivel, amarilla, naranja, roja));
            ok("Zona agregada. Cargá sus valores históricos desde la opción 13 para poder comparar con la media.");
        } catch (ElementoDuplicadoException | IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    private void agregarEstacion() {
        titulo("Agregar estación");
        int id = leerEntero("ID (número positivo): ");
        String nombre = leerTextoObligatorio("Nombre: ");
        double latitud = leerDecimal("Latitud (-90 a 90): ");
        double longitud = leerDecimal("Longitud (-180 a 180): ");
        double altitud = leerDecimal("Altitud (m): ");
        EstadoOperativo estado = leerEnum(EstadoOperativo.class, "Estado:");
        TipoEstacion tipo = leerEnum(TipoEstacion.class, "Tipo de estación:");
        System.out.println("\nResponsable técnico de la estación:");
        ResponsableTecnico responsable = pedirTecnico();
        if (responsable == null) {
            return;
        }
        System.out.println("\nZona a la que pertenece:");
        ZonaGeografica zona = pedirZona();
        if (zona == null) {
            return;
        }
        try {
            sistema.agregarEstacion(new EstacionMeteorologica(id, nombre, latitud, longitud,
                altitud, estado, tipo, responsable), zona);
            ok("Estación agregada a la zona " + zona.getNombre() + ".");
        } catch (ElementoDuplicadoException | IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    private void agregarSensor() {
        titulo("Agregar sensor");
        int id = leerEntero("ID (número positivo): ");
        String marca = leerTextoObligatorio("Marca: ");
        String modelo = leerTextoObligatorio("Modelo: ");
        double precision = leerDecimal("Precisión: ");
        EstadoOperativo estado = leerEnum(EstadoOperativo.class, "Estado:");
        LocalDate calibracion = leerFecha("Fecha de última calibración", LocalDate.now());
        try {
            sistema.agregarSensor(new Sensor(id, marca, modelo, precision, estado, calibracion));
            ok("Sensor agregado. Falta instalarlo en una estación (opción 7).");
        } catch (ElementoDuplicadoException | IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    private void instalarSensor() {
        titulo("Instalar sensor");
        Sensor s = pedirSensor();
        if (s == null) {
            return;
        }
        System.out.println();
        EstacionMeteorologica e = pedirEstacion();
        if (e == null) {
            return;
        }
        try {
            sistema.instalarSensor(s, e);
            ok("Sensor " + s.getIdSensor() + " instalado en " + e.getNombre() + ".");
        } catch (SensorYaInstaladoException ex) {
            error(ex.getMessage());
        }
    }

    private void retirarSensor() {
        titulo("Retirar sensor");
        Sensor s = pedirSensor();
        if (s == null) {
            return;
        }
        try {
            sistema.retirarSensor(s);
            ok("Sensor " + s.getIdSensor() + " retirado. Ahora puede instalarse en otra estación.");
        } catch (SensorSinEstacionException ex) {
            error(ex.getMessage());
        }
    }

    private void cambiarEstadoEstacion() {
        titulo("Cambiar estado de una estación");
        EstacionMeteorologica e = pedirEstacion();
        if (e == null) {
            return;
        }
        System.out.println("Estado actual: " + e.getEstado());
        EstadoOperativo estado = leerEnum(EstadoOperativo.class, "Nuevo estado:");
        if (sistema.cambiarEstadoEstacion(e, estado)) {
            ok("Estado actualizado a " + estado + ".");
        } else {
            error("No se pudo cambiar el estado.");
        }
    }

    private void cambiarEstadoSensor() {
        titulo("Cambiar estado de un sensor");
        Sensor s = pedirSensor();
        if (s == null) {
            return;
        }
        System.out.println("Estado actual: " + s.getEstado());
        EstadoOperativo estado = leerEnum(EstadoOperativo.class, "Nuevo estado:");
        if (sistema.cambiarEstadoSensor(s, estado)) {
            ok("Estado actualizado a " + estado + ".");
        } else {
            error("No se pudo cambiar el estado.");
        }
    }

    private void registrarMantenimiento() {
        titulo("Registrar mantenimiento");
        EstacionMeteorologica e = pedirEstacion();
        if (e == null) {
            return;
        }
        int id = leerEntero("ID del mantenimiento (número positivo): ");
        TipoMantenimiento tipo = leerEnum(TipoMantenimiento.class, "Tipo de mantenimiento:");
        LocalDate fecha = leerFecha("Fecha", LocalDate.now());
        String descripcion = leerTextoObligatorio("Descripción: ");

        // El sensor es opcional: solo se ofrecen los sensores de esa estación
        Sensor sensor = null;
        if (e.getSensores().isEmpty()) {
            System.out.println("La estación no tiene sensores: el mantenimiento será general.");
        } else {
            System.out.println("Sensores de la estación:");
            for (Sensor s : e.getSensores()) {
                System.out.printf("  [%d] %s %s - %s%n", s.getIdSensor(), s.getMarca(), s.getModelo(), s.getEstado());
            }
            while (true) {
                int idSensor = leerEntero("ID del sensor (0 = toda la estación): ");
                if (idSensor == 0) {
                    break;
                }
                Sensor candidato = sistema.buscarSensorPorId(idSensor);
                if (candidato != null && e.getSensores().contains(candidato)) {
                    sensor = candidato;
                    break;
                }
                error("Ese sensor no está instalado en esta estación.");
            }
        }
        System.out.println("\nTécnico que realizó el mantenimiento:");
        ResponsableTecnico responsable = pedirTecnico();
        if (responsable == null) {
            return;
        }
        try {
            sistema.registrarMantenimiento(e, id, fecha, tipo, descripcion, sensor, responsable);
            ok("Mantenimiento registrado.");
            if (tipo == TipoMantenimiento.CALIBRACION && sensor != null) {
                System.out.println("    Se actualizó la fecha de calibración del sensor.");
            }
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }

    private void modificarUmbrales() {
        titulo("Modificar umbrales de una zona");
        ZonaGeografica z = pedirZona();
        if (z == null) {
            return;
        }
        System.out.printf("Umbrales actuales: amarilla %.1f - naranja %.1f - roja %.1f mm%n",
            z.getUmbralAmarilla(), z.getUmbralNaranja(), z.getUmbralRoja());
        double amarilla, naranja, roja;
        if (leerSiNo("¿Usar la escala de referencia 50/100/150 mm?")) {
            amarilla = ZonaGeografica.UMBRAL_AMARILLA_REF;
            naranja = ZonaGeografica.UMBRAL_NARANJA_REF;
            roja = ZonaGeografica.UMBRAL_ROJA_REF;
        } else {
            System.out.println("Los umbrales deben cumplir: amarilla < naranja < roja.");
            amarilla = leerDecimal("Umbral amarilla (mm en 24 h): ");
            naranja = leerDecimal("Umbral naranja (mm en 24 h): ");
            roja = leerDecimal("Umbral roja (mm en 24 h): ");
        }
        try {
            z.setUmbrales(amarilla, naranja, roja);
            ok("Umbrales actualizados. Se aplican desde la próxima lectura registrada.");
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    private void cargarValorHistorico() {
        titulo("Cargar valor histórico mensual");
        ZonaGeografica z = pedirZona();
        if (z == null) {
            return;
        }
        System.out.println("Valores cargados:");
        if (z.getValoresHistoricos().isEmpty()) {
            System.out.println("  (ninguno)");
        }
        for (ValorHistorico v : z.getValoresHistoricos()) {
            System.out.printf("  mes %2d: %.1f mm%n", v.getMes(), v.getAcumuladoMedio());
        }
        int mes;
        while (true) {
            mes = leerEntero("Mes (1 a 12, 0 para cancelar): ");
            if (mes == 0) {
                cancelado();
                return;
            }
            if (mes >= 1 && mes <= 12) {
                break;
            }
            error("El mes debe estar entre 1 y 12.");
        }
        double acumulado;
        while (true) {
            acumulado = leerDecimal("Acumulado medio del mes (mm): ");
            if (acumulado >= 0) {
                break;
            }
            error("El acumulado no puede ser negativo.");
        }
        try {
            // Si el mes ya tiene valor, se actualiza en vez de duplicarlo
            ValorHistorico existente = precipitaciones.buscarValorHistorico(z, mes);
            if (existente != null) {
                existente.setAcumuladoMedio(acumulado);
                ok("Se actualizó el valor del mes " + mes + ".");
            } else {
                z.agregarValorHistorico(mes, acumulado);
                ok("Valor histórico del mes " + mes + " cargado.");
            }
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    // ==================== PERSONAL TÉCNICO ====================

    private void menuPersonal() {
        int opcion;
        do {
            titulo("Personal técnico y equipos");
            System.out.println("  1. Listar técnicos");
            System.out.println("  2. Listar equipos de respuesta");
            System.out.println("  3. Agregar técnico");
            System.out.println("  4. Agregar equipo de respuesta");
            System.out.println("  5. Agregar integrante a un equipo");
            System.out.println("  0. Volver");
            opcion = leerOpcion();
            switch (opcion) {
                case 1: listar(sistema.getTecnicos(), "No hay técnicos."); break;
                case 2: listar(sistema.getEquipos(), "No hay equipos."); break;
                case 3: agregarTecnico(); break;
                case 4: agregarEquipo(); break;
                case 5: agregarIntegrante(); break;
                case 0: break;
                default: error("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void agregarTecnico() {
        titulo("Agregar técnico");
        int dni = leerEntero("DNI (sin puntos): ");
        String nombre = leerTextoObligatorio("Nombre: ");
        String apellido = leerTextoObligatorio("Apellido: ");
        String correo = leerTexto("Correo electrónico: ");
        String celular = leerTexto("Celular: ");
        String especialidad = leerTextoObligatorio("Especialidad: ");
        String licencia = leerTextoObligatorio("Número de licencia: ");
        try {
            sistema.agregarTecnico(new ResponsableTecnico(dni, nombre, apellido, correo,
                celular, especialidad, licencia));
            ok("Técnico agregado.");
        } catch (ElementoDuplicadoException | IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    private void agregarEquipo() {
        titulo("Agregar equipo de respuesta");
        int id = leerEntero("ID (número positivo): ");
        String nombre = leerTextoObligatorio("Nombre del equipo: ");
        try {
            // Un equipo nuevo está disponible; los integrantes se agregan después
            sistema.agregarEquipo(new EquipoRespuesta(id, nombre, true));
            ok("Equipo agregado. Falta agregarle integrantes (opción 5).");
        } catch (ElementoDuplicadoException | IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    private void agregarIntegrante() {
        titulo("Agregar integrante a un equipo");
        EquipoRespuesta equipo = pedirEquipo(sistema.getEquipos());
        if (equipo == null) {
            return;
        }
        System.out.println();
        ResponsableTecnico tecnico = pedirTecnico();
        if (tecnico == null) {
            return;
        }
        try {
            sistema.agregarIntegrante(equipo, tecnico);
            ok(tecnico.getNombreCompleto() + " ahora integra el " + equipo.getNombreEquipo() + ".");
        } catch (TecnicoYaAsignadoException e) {
            error(e.getMessage());
        }
    }

    // ==================== LECTURAS ====================

    private void registrarLectura() {
        titulo("Registrar lectura de lluvia");
        Sensor sensor = pedirSensor();
        if (sensor == null) {
            return;
        }
        double mm = leerDecimal("Milímetros registrados (0 a 500): ");
        LocalDateTime fh = leerFechaHora("Fecha y hora de la lectura");

        if (fh.isAfter(LocalDateTime.now())) {
            error("La fecha y hora no pueden ser futuras.");
            return;
        }
        // Avisa si la lectura es anterior a la última del sensor (afecta la detección de anomalías)
        List<Medicion> previas = sensor.getMediciones();
        if (!previas.isEmpty() && fh.isBefore(previas.get(previas.size() - 1).getFechaHora())) {
            System.out.println("Esta lectura es anterior a la última registrada por el sensor.");
            if (!leerSiNo("¿Registrarla igual?")) {
                cancelado();
                return;
            }
        }
        try {
            Medicion m = sistema.registrarLectura(sensor, mm, fh);
            ok("Lectura registrada:");
            System.out.println(m);
            if (m.getAnomala()) {
                System.out.println("\n>> ATENCIÓN: difiere más de 100 mm de la lectura anterior y quedó marcada como anómala.");
            }
            ZonaGeografica zona = sistema.buscarZonaDeEstacion(sistema.buscarEstacionDeSensor(sensor));
            if (zona != null) {
                System.out.println("\nNivel de alerta de la zona " + zona.getNombre() + ": " + zona.getNivelAlertaActual()
                    + " (" + String.format("%.1f", zona.lluviaAcumulada24h()) + " mm en 24 h)");
            }
            int pendientes = sistema.alertasPendientes().size();
            if (pendientes > 0) {
                System.out.println(">> Hay " + pendientes + " alerta(s) NARANJA/ROJA sin equipo. Asignalas desde el menú Alertas.");
            }
        } catch (SensorSinEstacionException | EstacionNoOperativaException | LecturaInvalidaException e) {
            error(e.getMessage());
        }
        esperarEnter();
    }

    // ==================== ALERTAS ====================

    private void menuAlertas() {
        int opcion;
        do {
            titulo("Alertas");
            int pendientes = sistema.alertasPendientes().size();
            if (pendientes > 0) {
                System.out.println(">> " + pendientes + " alerta(s) pendiente(s) de equipo\n");
            }
            System.out.println("  1. Registrar alerta de operador (crecida de río o tormenta severa)");
            System.out.println("  2. Ver alertas pendientes de equipo");
            System.out.println("  3. Asignar equipo a una alerta");
            System.out.println("  4. Liberar equipo (terminó la emergencia)");
            System.out.println("  0. Volver");
            opcion = leerOpcion();
            switch (opcion) {
                case 1: registrarAlertaOperador(); break;
                case 2: mostrarPendientes(); esperarEnter(); break;
                case 3: asignarEquipo(); break;
                case 4: liberarEquipo(); break;
                case 0: break;
                default: error("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void registrarAlertaOperador() {
        titulo("Registrar alerta de operador");
        ZonaGeografica zona = pedirZona();
        if (zona == null) {
            return;
        }
        // El operador solo puede cargar estos dos tipos (LLUVIA_COPIOSA la genera el sistema)
        System.out.println("\nTipo de evento:\n  1. CRECIDA_RIO\n  2. TORMENTA_SEVERA\n  0. Cancelar");
        int tipo;
        while (true) {
            tipo = leerEntero("Opción: ");
            if (tipo >= 0 && tipo <= 2) {
                break;
            }
            error("Elegí 1, 2 o 0.");
        }
        if (tipo == 0) {
            cancelado();
            return;
        }
        TipoEventoClimatico evento = (tipo == 1) ? TipoEventoClimatico.CRECIDA_RIO : TipoEventoClimatico.TORMENTA_SEVERA;
        NivelAlerta nivel = leerEnum(NivelAlerta.class, "Nivel de la alerta:");
        String descripcion = leerTextoObligatorio("Descripción: ");
        try {
            Alerta a = sistema.registrarAlertaOperador(zona, evento, nivel, descripcion);
            ok("Alerta registrada:");
            System.out.println(a);
            if (a.requiereEquipo()) {
                System.out.println("\n>> Esta alerta requiere asignar un equipo de respuesta (opción 3).");
            }
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        }
        esperarEnter();
    }

    private void asignarEquipo() {
        titulo("Asignar equipo a una alerta");
        if (!mostrarPendientes()) {
            esperarEnter();
            return;
        }
        Alerta alerta = buscarAlerta(leerEntero("ID de la alerta (0 para cancelar): "));
        if (alerta == null) {
            error("No existe esa alerta (o se canceló la operación).");
            return;
        }
        System.out.println();
        EquipoRespuesta equipo = pedirEquipo(sistema.equiposDisponibles());
        if (equipo == null) {
            return;
        }
        try {
            sistema.asignarEquipo(alerta, equipo);
            ok("Equipo " + equipo.getNombreEquipo() + " asignado. Queda ocupado hasta que se lo libere.");
        } catch (AlertaSinEquipoException e) {
            error(e.getMessage());
        }
    }

    private void liberarEquipo() {
        titulo("Liberar equipo");
        // Solo se muestran las alertas cuyo equipo sigue ocupado
        boolean hay = false;
        for (ZonaGeografica z : sistema.getZonas()) {
            for (Alerta a : z.getAlertas()) {
                if (a.getEquipo() != null && !a.getEquipo().isDisponible()) {
                    if (!hay) {
                        System.out.println("Alertas con equipo en emergencia:");
                    }
                    System.out.printf("  [%d] %s - zona %s - atendida por %s%n", a.getIdAlerta(), a.getNivel(),
                        z.getNombre(), a.getEquipo().getNombreEquipo());
                    hay = true;
                }
            }
        }
        if (!hay) {
            System.out.println("No hay equipos ocupados en este momento.");
            esperarEnter();
            return;
        }
        Alerta alerta = buscarAlerta(leerEntero("ID de la alerta atendida (0 para cancelar): "));
        if (alerta == null) {
            error("No existe esa alerta (o se canceló la operación).");
            return;
        }
        try {
            sistema.liberarEquipo(alerta);
            ok("Equipo liberado.");
        } catch (AlertaSinEquipoException e) {
            error(e.getMessage());
        }
    }

    // ==================== REPORTES ====================

    private void menuReportes() {
        int opcion;
        do {
            titulo("Reportes");
            System.out.println("  1. Histórico de precipitaciones (diario, semanal y mensual)");
            System.out.println("  2. Ranking de estaciones por volumetría");
            System.out.println("  3. Historial de alertas emitidas");
            System.out.println("  4. Ficha técnica de una estación");
            System.out.println("  5. Informe de fallas y mantenimiento");
            System.out.println("  6. Estadística de anomalía climática");
            System.out.println("  0. Volver");
            opcion = leerOpcion();
            switch (opcion) {
                case 1: reporteHistorico(); break;
                case 2: reporteRanking(); break;
                case 3: reporteHistorialAlertas(); break;
                case 4: reporteFicha(); break;
                case 5: System.out.println("\n" + operativos.informeFallas()); esperarEnter(); break;
                case 6: reporteAnomalia(); break;
                case 0: break;
                default: error("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void reporteHistorico() {
        titulo("Histórico de precipitaciones");
        System.out.println("  1. Por estación\n  2. Por zona\n  0. Cancelar");
        int tipo = leerEntero("Opción: ");
        if (tipo == 0) {
            cancelado();
            return;
        }
        if (tipo != 1 && tipo != 2) {
            error("Opción inválida.");
            return;
        }
        System.out.println();
        LocalDate[] rango = leerRango();
        if (rango == null) {
            return;
        }

        Map<LocalDate, Double> diario;
        ZonaGeografica zona;
        if (tipo == 1) {
            EstacionMeteorologica e = pedirEstacion();
            if (e == null) {
                return;
            }
            diario = precipitaciones.historicoDiario(e, rango[0], rango[1]);
            // Para comparar con la media histórica se usa la zona de la estación
            zona = sistema.buscarZonaDeEstacion(e);
        } else {
            zona = pedirZona();
            if (zona == null) {
                return;
            }
            diario = precipitaciones.historicoDiario(zona, rango[0], rango[1]);
        }

        if (diario.isEmpty()) {
            System.out.println("\nNo hay lecturas en ese rango.");
            esperarEnter();
            return;
        }
        System.out.println("\n-- Diario --");
        for (Map.Entry<LocalDate, Double> d : diario.entrySet()) {
            System.out.printf("  %s  %8.1f mm%n", d.getKey(), d.getValue());
        }
        System.out.println("\n-- Semanal (fecha = lunes de la semana) --");
        for (Map.Entry<LocalDate, Double> s : precipitaciones.acumuladoSemanal(diario).entrySet()) {
            System.out.printf("  %s  %8.1f mm%n", s.getKey(), s.getValue());
        }
        System.out.println("\n-- Mensual --");
        for (Map.Entry<YearMonth, Double> m : precipitaciones.acumuladoMensual(diario).entrySet()) {
            System.out.printf("  %s  %8.1f mm%n", m.getKey(), m.getValue());
        }
        if (zona != null) {
            System.out.println("\n" + precipitaciones.compararConHistorico(zona, rango[0], rango[1]));
        }
        esperarEnter();
    }

    private void reporteRanking() {
        titulo("Ranking de estaciones por volumetría");
        LocalDate[] rango = leerRango();
        if (rango == null) {
            return;
        }
        List<PuestoRanking> ranking = precipitaciones.rankingEstaciones(rango[0], rango[1]);
        System.out.println("\nDel " + rango[0] + " al " + rango[1]);
        System.out.println(SUBLINEA);
        System.out.printf("  %-4s %-28s %12s%n", "N°", "Estación", "Acumulado");
        System.out.println(SUBLINEA);
        int puesto = 1;
        for (PuestoRanking p : ranking) {
            System.out.printf("  %-4d %-28s %9.1f mm%n", puesto,
                p.getEstacion().getNombre() + " (" + p.getEstacion().getIdEstacion() + ")", p.getAcumulado());
            puesto++;
        }
        esperarEnter();
    }

    private void reporteHistorialAlertas() {
        titulo("Historial de alertas emitidas");
        LocalDate[] rango = leerRango();
        if (rango == null) {
            return;
        }
        // Los filtros de nivel y zona son opcionales
        NivelAlerta nivel = null;
        if (leerSiNo("¿Filtrar por nivel?")) {
            nivel = leerEnum(NivelAlerta.class, "Nivel:");
        }
        ZonaGeografica zona = null;
        mostrarZonas();
        int idZona = leerEntero("ID de la zona (0 = todas): ");
        if (idZona != 0) {
            zona = sistema.buscarZonaPorId(idZona);
            if (zona == null) {
                error("No existe esa zona.");
                return;
            }
        }
        System.out.println();
        listar(operativos.historialAlertas(nivel, rango[0], rango[1], zona), "No hay alertas con esos filtros.");
    }

    private void reporteFicha() {
        titulo("Ficha técnica de una estación");
        EstacionMeteorologica e = pedirEstacion();
        if (e != null) {
            System.out.println("\n" + operativos.fichaTecnica(e));
            esperarEnter();
        }
    }

    private void reporteAnomalia() {
        titulo("Anomalía climática");
        ZonaGeografica zona = pedirZona();
        if (zona == null) {
            return;
        }
        int anio = leerEntero("Año (ej: " + LocalDate.now().getYear() + "): ");
        int mes = leerEntero("Mes (1 a 12): ");
        try {
            System.out.println("\n" + precipitaciones.informeAnomalia(zona, anio, mes));
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        }
        esperarEnter();
    }
}
