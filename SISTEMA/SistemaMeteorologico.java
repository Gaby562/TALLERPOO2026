package SISTEMA;
import java.time.LocalDateTime;

import enums.NivelAlerta;
import enums.TipoEventoClimatico;


import java.util.ArrayList;
import java.util.List;

import CLASES.Alerta;
import CLASES.EquipoRespuesta;
import CLASES.EstacionMeteorologica;
import CLASES.Medicion;
import CLASES.ResponsableTecnico;
import CLASES.Sensor;
import CLASES.ZonaGeografica;
import EXCEPCIONES.AlertaSinEquipoException;
import EXCEPCIONES.EstacionNoOperativaException;
import EXCEPCIONES.LecturaInvalidaException;
import EXCEPCIONES.SensorSinEstacionException;
import EXCEPCIONES.SensorYaInstaladoException;
import EXCEPCIONES.TecnicoYaAsignadoException;

public class SistemaMeteorologico {
    private List<ZonaGeografica> zonas;
    private List<EstacionMeteorologica> estaciones;
    private List<Sensor> sensores;
    private List<ResponsableTecnico> tecnicos;
    private List<EquipoRespuesta> equipos;

    public SistemaMeteorologico() {
        this.zonas = new ArrayList<>();
        this.estaciones = new ArrayList<>();
        this.sensores = new ArrayList<>();
        this.tecnicos = new ArrayList<>();
        this.equipos = new ArrayList<>();
    }

    public List<ZonaGeografica> getZonas() {
        return zonas;
    }

    public List<EstacionMeteorologica> getEstaciones() {
        return estaciones;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public List<ResponsableTecnico> getTecnicos() {
        return tecnicos;
    }

    public List<EquipoRespuesta> getEquipos() {
        return equipos;
    }

    public void agregarZona(ZonaGeografica z){
        this.zonas.add(z);
    }

    public void agregarSensor(Sensor s){ 
        sensores.add(s);
    }

    public void agregarTecnico(ResponsableTecnico t){
        tecnicos.add(t);
    }

    public void agregarEquipo(EquipoRespuesta e){
        equipos.add(e);
    }

    // Agrega la estación al sistema y a su zona (agregación)
    public void agregarEstacion(EstacionMeteorologica e, ZonaGeografica z) {
        estaciones.add(e);
        z.agregarEstacion(e);
    }

    // se busca la zona por su id (devuelven null si no existe)
    public ZonaGeografica buscarZonaPorId(int id) {
        for (ZonaGeografica z : zonas) {
            if (z.getIdZona() == id) {
                return z;
            }
        }
        return null;
    }
    // buscamos la estacion por su id (devuelven null si no existe)
    public EstacionMeteorologica buscarEstacionPorId(int id) {
        for (EstacionMeteorologica e : estaciones) {
            if (e.getIdEstacion() == id) {
                return e;
            }
        }
        return null;
    }
    // buscamos un senson por su id (devuelven null si no existe)
    public Sensor buscarSensorPorId(int id) {
        for (Sensor s : sensores) {
            if (s.getIdSensor() == id) {
                return s;
            }
        }
        return null;
    }
    // se busca el responsable tecnico por su id (devuelven null si no existe)
    public ResponsableTecnico buscarTecnicoPorDni(int dni) {
        for (ResponsableTecnico t : tecnicos) {
            if (t.getDni() == dni) {
                return t;
            }
        }
        return null;
    }
    // buscamos el equipo de respuesta por su id (devuelven null si no existe)
    public EquipoRespuesta buscarEquipoPorId(int id) {
        for (EquipoRespuesta e : equipos) {
            if (e.getIdEquipo() == id) {
                return e;
            }
        }
        return null;
    }

    // Devuelve la estación donde está instalado el sensor, o null si no está en ninguna
    public EstacionMeteorologica buscarEstacionDeSensor(Sensor s) {
        for (EstacionMeteorologica e : estaciones) {
            if (e.getSensores().contains(s)) {
                return e;
            }
        }
        return null;
    }
    // Devuelve la zona que contiene a la estación, o null si no está en ninguna
    public ZonaGeografica buscarZonaDeEstacion(EstacionMeteorologica e) {
        for (ZonaGeografica z : zonas) {
            if (z.getEstaciones().contains(e)) {
                return z;
            }
        }
        return null;
    }

    // reglas de negocio
    // un sensor solo puede estar instalado en una estación a la vez
    public void instalarSensor(Sensor s, EstacionMeteorologica destino) throws SensorYaInstaladoException {
        // Busca en qué estación está instalado el sensor ahora (null si no está en ninguna)
        EstacionMeteorologica actual = buscarEstacionDeSensor(s);

        // Si el sensor ya está en otra estación, no se permite instalarlo
        if (actual != null && actual != destino) {
            // Se rechaza la operación avisando en qué estación está el sensor
            throw new SensorYaInstaladoException("El sensor " + s.getIdSensor()
                + " ya está instalado en la estación " + actual.getIdEstacion()
                + ". Hay que retirarlo primero.");
        }

        // Si no está en ninguna estación, lo instala
        destino.instalarSensor(s);
    }

    // Un técnico puede integrar a lo sumo un equipo
    public void agregarIntegrante(EquipoRespuesta equipo, ResponsableTecnico t)
            throws TecnicoYaAsignadoException {
        // Recorre todos los equipos del sistema, uno por uno
        for (EquipoRespuesta e : equipos) {
            // Pregunta si el técnico ya está en la lista de integrantes de ese equipo
            if (e.getIntegrantes().contains(t)) {
                // Si ya integra un equipo, se rechaza la operación indicando cuál es
                throw new TecnicoYaAsignadoException("El técnico " + t.getNombreCompleto()
                    + " ya integra el equipo " + e.getNombreEquipo() + ".");
            }
        }
        // Si el for terminó sin encontrarlo, el técnico está libre y se agrega al equipo
        equipo.agregarIntegrante(t);
    }

    // Registra una lectura: controla la estación, guarda la medición y evalúa el riesgo de la zona
    public Medicion registrarLectura(Sensor s, double mm, LocalDateTime fh)
            throws SensorSinEstacionException, EstacionNoOperativaException, LecturaInvalidaException {

        // Busca en qué estación está instalado el sensor (null si no está en ninguna)
        EstacionMeteorologica estacion = buscarEstacionDeSensor(s);

        // Si el sensor no está instalado en ninguna estación, no se puede registrar la lectura
        if (estacion == null) {
            throw new SensorSinEstacionException(
                "El sensor " + s.getIdSensor() + " no está instalado en ninguna estación.");
        }

        // Comprueba que la estación esté operativa antes de registrar la lectura (en Sensor.registrar())
        if (!estacion.estaOperativa()) {
            // Si está en mantenimiento o fuera de servicio, se rechaza la lectura
            throw new EstacionNoOperativaException("La estación " + estacion.getIdEstacion()
                + " no está operativa (estado: " + estacion.getEstado() + ").");
        }

        // El sensor valida su estado y el rango (0 a 500 mm), detecta si es anómala y crea la Medicion
        Medicion m = s.registrar(mm, fh);

        // Después de registrar la lectura, se actualiza el riesgo de la zona
        // Busca la zona a la que pertenece la estación
        ZonaGeografica zona = buscarZonaDeEstacion(estacion);
        // Si la estación pertenece a una zona, esa zona recalcula su nivel de alerta
        if (zona != null) {
            zona.evaluarRiesgo();
        }

        // Devuelve la medición creada, para que el menú pueda mostrarla
        return m;
    }

    // Alertas cargadas por un operador: solo CRECIDA_RIO y TORMENTA_SEVERA
    public Alerta registrarAlertaOperador(ZonaGeografica z, TipoEventoClimatico ev, NivelAlerta nivel, String descripcion) {
        // La consigna dice que LLUVIA_COPIOSA la genera el sistema, no el operador
        if (ev == TipoEventoClimatico.LLUVIA_COPIOSA) {
            // Se rechaza el uso incorrecto del método con una excepción
            throw new IllegalArgumentException("LLUVIA_COPIOSA la genera el sistema, no un operador.");
        }

        // La zona crea la alerta con el nivel y la descripción que indicó el operador,
        // la guarda en su lista de alertas y la devuelve
        return z.emitirAlerta(ev, nivel, descripcion);
    }

    // Asigna un equipo de respuesta a una alerta que lo necesita
    public void asignarEquipo(Alerta a, EquipoRespuesta e) throws AlertaSinEquipoException {

        // Verifica si la alerta necesita un equipo según su nivel
        if (!a.requiereEquipo()) {

            // Si no necesita equipo, muestra un error
            throw new AlertaSinEquipoException(
                "La alerta " + a.getIdAlerta() + " no requiere equipo (nivel " + a.getNivel() + ").");
        }

        // Verifica si el equipo tiene integrantes
        if (e.getIntegrantes().isEmpty()) {

            // Si el equipo no tiene integrantes, muestra un error
            throw new AlertaSinEquipoException(
                "El equipo " + e.getNombreEquipo() + " no tiene integrantes.");
        }

        // Si se cumplen las condiciones, asigna el equipo a la alerta
        a.asignarEquipo(e);
    }
    // Lista las alertas NARANJA o ROJA que todavía no tienen equipo asignado
    public List<Alerta> alertasPendientes() {

        // Crea una lista vacía para guardar las alertas pendientes
        List<Alerta> pendientes = new ArrayList<>();

        // Recorre todas las zonas geográficas
        for (ZonaGeografica z : zonas) {

            // Recorre todas las alertas de cada zona
            for (Alerta a : z.getAlertas()) {

                // Verifica si la alerta necesita un equipo y todavía no tiene uno asignado
                if (a.requiereEquipo() && a.getEquipo() == null) {

                    // Agrega la alerta a la lista de pendientes
                    pendientes.add(a);
                }
            }
        }

        // Devuelve la lista de alertas que necesitan un equipo
        return pendientes;
    }
}
