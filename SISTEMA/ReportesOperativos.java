package SISTEMA;

import CLASES.*;
import enums.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportesOperativos {
    
    private SistemaMeteorologico sistema;

    public ReportesOperativos(SistemaMeteorologico sistema) {
        this.sistema = sistema;
    }

    // INFORME DE FALLAS Y MANTENIMIENTO
    // Genera un informe con las fallas y necesidades de mantenimiento de los sensores
    public String informeFallas() {

        // Guarda el título del informe
        String texto = "INFORME DE FALLAS Y MANTENIMIENTO\n";

        // Agrega el título de la primera sección
        texto += "\n1. Sensores fuera de servicio o en mantenimiento\n";

        // Contador de sensores encontrados
        int cantidad = 0;

        // Recorre todos los sensores del sistema
        for (Sensor s : sistema.getSensores()) {

            // Comprueba si el sensor no está operativo
            if (s.getEstado() != EstadoOperativo.OPERATIVO) {

                // Agrega al informe el ID, el estado y la ubicación del sensor
                texto += "- Sensor " + s.getIdSensor() + " (" + s.getEstado() + "), "
                    + ubicacion(s) + "\n";

                // Aumenta el contador
                cantidad++;
            }
        }

        // Si no encontró sensores, indica que no hay ninguno
        if (cantidad == 0) {
            texto += "Ninguno\n";
        }

        // Agrega el título de la segunda sección
        texto += "\n2. Lecturas anómalas\n";

        // Reinicia el contador
        cantidad = 0;

        // Recorre todos los sensores del sistema
        for (Sensor s : sistema.getSensores()) {

            // Recorre las mediciones del sensor actual
            for (Medicion m : s.getMediciones()) {

                // Comprueba si la medición está marcada como anómala
                if (m.getAnomala()) {

                    // Agrega al informe los datos de la medición
                    texto += "- Sensor " + s.getIdSensor() + ", medición " + m.getIdMedicion()
                        + ", " + m.getFechaHora() + ", " + m.getMilimetros() + " mm\n";

                    // Aumenta el contador
                    cantidad++;
                }
            }
        }

        // Si no encontró mediciones anómalas, indica que no hay ninguna
        if (cantidad == 0) {
            texto += "Ninguna\n";
        }

        // Agrega el título de la tercera sección
        texto += "\n3. Sensores que requieren calibración\n";

        // Reinicia el contador
        cantidad = 0;

        // Recorre todos los sensores del sistema
        for (Sensor s : sistema.getSensores()) {

            // Comprueba si el sensor necesita calibración
            if (s.requiereCalibracion()) {

                // Obtiene la última fecha de calibración o indica si nunca fue calibrado
                String ultima = (s.getUltimaCalibracion() != null)
                    ? s.getUltimaCalibracion().toString() : "nunca calibrado";

                // Agrega al informe el ID, la fecha de calibración y la ubicación
                texto += "- Sensor " + s.getIdSensor() + ", última calibración: " + ultima
                    + ", " + ubicacion(s) + "\n";

                // Aumenta el contador
                cantidad++;
            }
        }

        // Si no encontró sensores que necesiten calibración, indica que no hay ninguno
        if (cantidad == 0) {
            texto += "Ninguno\n";
        }

        // Devuelve el informe completo
        return texto;
    }

    // Busca en qué estación está instalado un sensor
    private String ubicacion(Sensor s) {

        // Busca la estación a la que pertenece el sensor
        EstacionMeteorologica e = sistema.buscarEstacionDeSensor(s);

        // Comprueba si encontró una estación
        if (e != null) {

            // Devuelve el nombre y el ID de la estación
            return "estación " + e.getNombre() + " (ID " + e.getIdEstacion() + ")";
        }

        // Si no encontró una estación, indica que no está asignado
        return "sin estación asignada";
    }

    //FICHA TECNICA DE UNA ESTACION
    // Cantidad de últimas lecturas que se mostrarán por sensor
    private static final int CANTIDAD_ULTIMAS = 3;

    // Método que genera la ficha técnica de una estación
    public String fichaTecnica(EstacionMeteorologica e) {

        // Guarda los datos generales de la estación
        String texto = "FICHA TÉCNICA DE LA ESTACIÓN\n" +
            "ID: " + e.getIdEstacion() + "\n" + // ID de la estación
            "Nombre: " + e.getNombre() + "\n" + // Nombre
            "Tipo: " + e.getTipo() + "\n" + // Tipo de estación
            "Estado: " + e.getEstado() + "\n" + // Estado
            "Ubicación: latitud " + e.getLatitud() + ", longitud " + e.getLongitud()
                + ", altitud " + e.getAltitud() + " m\n"; // Ubicación

        // Busca la zona de la estación
        ZonaGeografica zona = sistema.buscarZonaDeEstacion(e);

        // Muestra la zona o indica si no tiene una asignada
        texto += "Zona: " + (zona != null ? zona.getNombre() : "sin zona asignada") + "\n";

        // Agrega la sección del responsable
        texto += "\nRESPONSABLE TÉCNICO\n";

        // Obtiene el responsable de la estación
        ResponsableTecnico r = e.getResponsable();

        // Comprueba si tiene responsable
        if (r == null) {
            texto += "Sin responsable asignado\n"; // No tiene responsable
        } else {
            texto += "Nombre: " + r.getNombreCompleto() + "\n" + // Nombre
                "Especialidad: " + r.getEspecialidad() + "\n" + // Especialidad
                "Licencia: " + r.getNumeroLicencia() + "\n" + // Licencia
                "Contacto: " + r.getCorreoElectronico() + " / " + r.getCelular() + "\n"; // Contacto
        }

        // Agrega la sección de sensores
        texto += "\nSENSORES\n";

        // Comprueba si hay sensores
        if (e.getSensores().isEmpty()) {
            texto += "La estación no tiene sensores instalados\n";
        }

        // Recorre todos los sensores de la estación
        for (Sensor s : e.getSensores()) {

            // Muestra los datos del sensor
            texto += "- Sensor " + s.getIdSensor() + " (" + s.getMarca() + " " + s.getModelo() + ")\n" +
                "  Estado: " + s.getEstado() + "\n" +
                "  Última calibración: " + s.getUltimaCalibracion()
                    + (s.requiereCalibracion() ? " (REQUIERE CALIBRACIÓN)" : "") + "\n";

            // Obtiene las mediciones del sensor
            List<Medicion> mediciones = s.getMediciones();

            // Comprueba si tiene mediciones
            if (mediciones.isEmpty()) {
                texto += "  Sin lecturas registradas\n";
            } else {

                // Agrega el título de las lecturas
                texto += "  Últimas lecturas:\n";

                // Calcula desde dónde comenzar a mostrar las últimas 3 lecturas
                int limite = Math.max(0, mediciones.size() - CANTIDAD_ULTIMAS);

                // Recorre las mediciones desde la más reciente
                for (int i = mediciones.size() - 1; i >= limite; i--) {

                    // Obtiene una medición
                    Medicion m = mediciones.get(i);

                    // Muestra la fecha, los milímetros y si es anómala
                    texto += "    " + m.getFechaHora() + ": " + m.getMilimetros() + " mm"
                        + (m.getAnomala() ? " (ANÓMALA)" : "") + "\n";
                }
            }
        }

        // Devuelve toda la información de la estación
        return texto;
    }
    
    //HISTORIAL DE ALERTAS EMITIDAS

    // Busca alertas según el nivel, las fechas y la zona.
    // Si nivel o zona son null, no se aplica ese filtro.
    public List<Alerta> historialAlertas(NivelAlerta nivel, LocalDate desde, LocalDate hasta, ZonaGeografica zona) {

        // Creamos una lista vacía para guardar las alertas encontradas.
        List<Alerta> resultado = new ArrayList<>();

        // Recorremos todas las zonas del sistema.
        for (ZonaGeografica z : sistema.getZonas()) {

            // Si se eligió una zona y no coincide, la salteamos.
            if (zona != null && z != zona) {
                continue;
            }

            // Recorremos las alertas de cada zona.
            for (Alerta a : z.getAlertas()) {

                // Obtenemos la fecha en la que se emitió la alerta.
                LocalDate dia = a.getFechaHoraEmision().toLocalDate();

                // Verificamos si el nivel coincide con el solicitado.
                // Si nivel es null, acepta cualquier nivel.
                boolean cumpleNivel = (nivel == null || a.getNivel() == nivel);

                // Verificamos si la fecha está dentro del rango indicado.
                boolean cumpleFecha = !dia.isBefore(desde) && !dia.isAfter(hasta);

                // Si cumple con los dos filtros, agregamos la alerta.
                if (cumpleNivel && cumpleFecha) {
                    resultado.add(a);
                }
            }
        }

        // Ordenamos las alertas desde la más reciente a la más antigua.
        Collections.sort(resultado, new OrdenarAlertasPorFechaDesc());

        // Devolvemos la lista de alertas encontradas y ordenadas.
        return resultado;
    }
}
