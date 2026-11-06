package MAIN;

import CLASES.*;
import SISTEMA.*;
import enums.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
 
// Carga el juego de datos de prueba que se usa en la defensa
// Las fechas son relativas a "ahora" para que siempre caigan dentro de las últimas 24 h
public class CargadorDatos {
 
    public static void cargar(SistemaMeteorologico sistema) {
        try {
            //Tecnicos
            ResponsableTecnico t1 = new ResponsableTecnico(47520908, "Pablo", "Yañez", "pablo.yanez@mail.com", "3456346564", "Hidrología", "HG-101");
            ResponsableTecnico t2 = new ResponsableTecnico(46251338, "Gabriela", "Mascimiani", "gabriela.mascimiani@mail.com", "3456497258", "Meteorología", "MT-202");
            ResponsableTecnico t3 = new ResponsableTecnico(30111222, "Melissa", "Fernandez", "melissa.fernandez@mail.com", "3454555666", "Instrumental", "IN-303");
            ResponsableTecnico t4 = new ResponsableTecnico(31777888, "Pedro", "Benitez", "pedro.benitez@mail.com", "3454777888", "Telemetría", "TL-404");
            sistema.agregarTecnico(t1);
            sistema.agregarTecnico(t2);
            sistema.agregarTecnico(t3);
            sistema.agregarTecnico(t4);
 
            // Zonas con sus umbrales (amarilla < naranja < roja)
            ZonaGeografica z1 = new ZonaGeografica(1, "Cuenca Río Uruguay", NivelVulnerabilidad.ALTO, 60, 120, 200);

            ZonaGeografica z2 = new ZonaGeografica(2, "Cuenca Río Gualeguay", NivelVulnerabilidad.MEDIO,
            ZonaGeografica.UMBRAL_AMARILLA_REF,
            ZonaGeografica.UMBRAL_NARANJA_REF,
            ZonaGeografica.UMBRAL_ROJA_REF);
            
            // Valores históricos: un acumulado medio por cada mes del año
            double[] historico1 = {150, 130, 140, 160, 110, 90, 80, 70, 100, 130, 150, 160};
            double[] historico2 = {120, 110, 120, 140, 100, 80, 70, 60, 90, 110, 120, 130};
            for (int mes = 1; mes <= 12; mes++) {
                z1.agregarValorHistorico(mes, historico1[mes - 1]);
                z2.agregarValorHistorico(mes, historico2[mes - 1]);
            }
            sistema.agregarZona(z1);
            sistema.agregarZona(z2);
 
            // Estaciones (cada una con su zona; la estación 4 está fuera de servicio)
            EstacionMeteorologica e1 = new EstacionMeteorologica(1, "Concordia Norte", -31.37, -58.02, 35, EstadoOperativo.OPERATIVO, TipoEstacion.AUTOMATICA, t1);
            EstacionMeteorologica e2 = new EstacionMeteorologica(2, "Salto Grande", -31.27, -57.94, 40, EstadoOperativo.OPERATIVO, TipoEstacion.CONVENCIONAL, t2);
            EstacionMeteorologica e3 = new EstacionMeteorologica(3, "Federación", -31.00, -57.90, 30, EstadoOperativo.OPERATIVO, TipoEstacion.AUTOMATICA, t3);
            EstacionMeteorologica e4 = new EstacionMeteorologica(4, "Chajarí", -30.75, -57.98, 60, EstadoOperativo.FUERA_DE_SERVICIO, TipoEstacion.CONVENCIONAL, t4);
            sistema.agregarEstacion(e1, z1);
            sistema.agregarEstacion(e2, z1);
            sistema.agregarEstacion(e3, z2);
            sistema.agregarEstacion(e4, z2);
 
            // Sensores 
            LocalDate hoy = LocalDate.now();
            Sensor s1 = new Sensor(1, "Davis", "VantagePro2", 0.2, EstadoOperativo.OPERATIVO, hoy.minusMonths(3));
            Sensor s2 = new Sensor(2, "Davis", "Rain Collector", 0.2, EstadoOperativo.OPERATIVO, hoy.minusMonths(14)); // calibración vencida
            Sensor s3 = new Sensor(3, "Onset", "RG3", 0.2, EstadoOperativo.OPERATIVO, hoy.minusMonths(5));
            Sensor s4 = new Sensor(4, "Texas", "TR-525", 0.25, EstadoOperativo.OPERATIVO, hoy.minusMonths(2));
            Sensor s5 = new Sensor(5, "Texas", "TR-525", 0.25, EstadoOperativo.EN_MANTENIMIENTO, hoy.minusMonths(8)); // en mantenimiento
            Sensor s6 = new Sensor(6, "Davis", "Rain Collector", 0.2, EstadoOperativo.OPERATIVO, hoy.minusMonths(1));
            sistema.agregarSensor(s1);
            sistema.agregarSensor(s2);
            sistema.agregarSensor(s3);
            sistema.agregarSensor(s4);
            sistema.agregarSensor(s5);
            sistema.agregarSensor(s6);
 
            // Instalación de los sensores en las estaciones
            sistema.instalarSensor(s1, e1);
            sistema.instalarSensor(s2, e1);
            sistema.instalarSensor(s3, e2);
            sistema.instalarSensor(s4, e3);
            sistema.instalarSensor(s5, e3);
            sistema.instalarSensor(s6, e4);
 
            // Equipos de respuesta 
            EquipoRespuesta alfa = new EquipoRespuesta(1, "Equipo Alfa", true);
            EquipoRespuesta beta = new EquipoRespuesta(2, "Equipo Beta", true);
            sistema.agregarEquipo(alfa);
            sistema.agregarEquipo(beta);
            sistema.agregarIntegrante(alfa, t1);
            sistema.agregarIntegrante(alfa, t2);
            sistema.agregarIntegrante(beta, t3);
            sistema.agregarIntegrante(beta, t4);
 
            // Mantenimientos 
            sistema.registrarMantenimiento(e1, 1, hoy.minusDays(20), TipoMantenimiento.PREVENTIVO, "Revisión general", s1, t1);
            sistema.registrarMantenimiento(e2, 2, hoy.minusDays(10), TipoMantenimiento.CORRECTIVO, "Cambio de pluviómetro", s3, t2);
            sistema.registrarMantenimiento(e3, 3, hoy.minusDays(2), TipoMantenimiento.CORRECTIVO, "Falla del sensor", s5, t3);
 
            // Lecturas de la zona 1 (se cargan en orden cronológico)
            // Acumulado: 30 (verde) -> 55 (AMARILLA) -> 100 (NARANJA) -> 120 -> 250 (ROJA)
            LocalDateTime ahora = LocalDateTime.now();
            sistema.registrarLectura(s1, 30, ahora.minusHours(10));
            sistema.registrarLectura(s2, 25, ahora.minusHours(9));
            sistema.registrarLectura(s1, 45, ahora.minusHours(7));
            sistema.registrarLectura(s3, 20, ahora.minusHours(5));
            // Esta lectura difiere más de 100 mm de la anterior del sensor: queda marcada como anómala
            sistema.registrarLectura(s2, 130, ahora.minusHours(3));
 
            // Lecturas de la zona 2 (no alcanzan el umbral amarillo) 
            sistema.registrarLectura(s4, 10, ahora.minusHours(6));
            sistema.registrarLectura(s4, 20, ahora.minusHours(2));
 
            // Alerta cargada por un operador 
            sistema.registrarAlertaOperador(z2, TipoEventoClimatico.CRECIDA_RIO, NivelAlerta.AMARILLA, "Crecida leve del río Gualeguay");
 
            // Las alertas NARANJA y ROJA de la zona 1 quedan sin equipo, para mostrar la asignación
 
        } catch (Exception ex) {
            // Un error acá casi siempre es un id repetido o un dato mal cargado
            System.out.println("Error al cargar los datos de prueba: " + ex.getMessage());
        }
    }
}