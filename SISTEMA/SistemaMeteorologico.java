package SISTEMA;
import CLASES.*;
import EXCEPCIONES.*;
import enums.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * sistema: guarda en memoria las zonas, estaciones, sensores,
 * técnicos y equipos de respuesta, y aplica las reglas de negocio
 * (asignación única de sensores, control operativo, disparo de alertas y asignación de emergencias).
 */
public class SistemaMeteorologico {
    private List<ZonaGeografica> zonas;
    private List<EstacionMeteorologica> estaciones;
    private List<Sensor> sensores;
    private List<ResponsableTecnico> tecnicos;
    private List<EquipoRespuesta> equipos;

    /** Crea un sistema vacío, sin datos cargados. */
    public SistemaMeteorologico() {
        this.zonas = new ArrayList<>();
        this.estaciones = new ArrayList<>();
        this.sensores = new ArrayList<>();
        this.tecnicos = new ArrayList<>();
        this.equipos = new ArrayList<>();
    }

    /** Devuelve las zonas registradas. */
    public List<ZonaGeografica> getZonas() {
        return zonas;
    }

    /** Devuelve las estaciones registradas. */
    public List<EstacionMeteorologica> getEstaciones() {
        return estaciones;
    }

    /** Devuelve los sensores registrados. */
    public List<Sensor> getSensores() {
        return sensores;
    }

    /** Devuelve los técnicos registrados. */
    public List<ResponsableTecnico> getTecnicos() {
        return tecnicos;
    }

    /** Devuelve los equipos de respuesta registrados. */
    public List<EquipoRespuesta> getEquipos() {
        return equipos;
    }

    /**
     * Registra una zona en el sistema.
     *
     * @param z zona a registrar
     * @throws ElementoDuplicadoException si ya existe una zona con el mismo id
     * @throws IllegalArgumentException si la zona es null o su id no es positivo
     */
    public void agregarZona(ZonaGeografica z) throws ElementoDuplicadoException {
        // La zona no puede ser null
        if (z == null) {
            throw new IllegalArgumentException("La zona no puede ser null.");
        }
        // El id tiene que ser positivo (el 0 se usa como "todas las zonas" en los reportes)
        if (z.getIdZona() <= 0) {
            throw new IllegalArgumentException("El id de la zona debe ser mayor que 0.");
        }
        // No puede haber dos zonas con el mismo id
        if (buscarZonaPorId(z.getIdZona()) != null) {
            throw new ElementoDuplicadoException("Ya existe una zona con id " + z.getIdZona() + ".");
        }
        zonas.add(z);
    }

    /**
     * Registra un sensor en el sistema. Todavía no queda instalado en ninguna estación.
     *
     * @param s sensor a registrar
     * @throws ElementoDuplicadoException si ya existe un sensor con el mismo id
     * @throws IllegalArgumentException si el sensor es null o su id no es positivo
     */
    public void agregarSensor(Sensor s) throws ElementoDuplicadoException {
        // El sensor no puede ser null
        if (s == null) {
            throw new IllegalArgumentException("El sensor no puede ser null.");
        }
        // El id tiene que ser positivo (el 0 se usa como "toda la estación" en mantenimientos)
        if (s.getIdSensor() <= 0) {
            throw new IllegalArgumentException("El id del sensor debe ser mayor que 0.");
        }
        // No puede haber dos sensores con el mismo id
        if (buscarSensorPorId(s.getIdSensor()) != null) {
            throw new ElementoDuplicadoException("Ya existe un sensor con id " + s.getIdSensor() + ".");
        }
        sensores.add(s);
    }

    /**
     * Registra un técnico en el sistema. Su identificador es el DNI.
     *
     * @param t técnico a registrar
     * @throws ElementoDuplicadoException si ya existe un técnico con el mismo DNI
     * @throws IllegalArgumentException si el técnico es null o su DNI no es positivo
     */
    public void agregarTecnico(ResponsableTecnico t) throws ElementoDuplicadoException {
        // El técnico no puede ser null
        if (t == null) {
            throw new IllegalArgumentException("El técnico no puede ser null.");
        }
        // El DNI tiene que ser positivo
        if (t.getDni() <= 0) {
            throw new IllegalArgumentException("El DNI del técnico debe ser mayor que 0.");
        }
        // El identificador del técnico es su DNI
        if (buscarTecnicoPorDni(t.getDni()) != null) {
            throw new ElementoDuplicadoException("Ya existe un técnico con DNI " + t.getDni() + ".");
        }
        tecnicos.add(t);
    }

    /**
     * Registra un equipo de respuesta en el sistema.
     *
     * @param e equipo a registrar
     * @throws ElementoDuplicadoException si ya existe un equipo con el mismo id
     * @throws IllegalArgumentException si el equipo es null o su id no es positivo
     */
    public void agregarEquipo(EquipoRespuesta e) throws ElementoDuplicadoException {
        // El equipo no puede ser null
        if (e == null) {
            throw new IllegalArgumentException("El equipo no puede ser null.");
        }
        // El id tiene que ser positivo
        if (e.getIdEquipo() <= 0) {
            throw new IllegalArgumentException("El id del equipo debe ser mayor que 0.");
        }
        // No puede haber dos equipos con el mismo id
        if (buscarEquipoPorId(e.getIdEquipo()) != null) {
            throw new ElementoDuplicadoException("Ya existe un equipo con id " + e.getIdEquipo() + ".");
        }
        equipos.add(e);
    }

    /**
     * Registra una estación en el sistema y la agrega a su zona.
     * Cada estación pertenece a exactamente una zona.
     *
     * @param e estación a registrar
     * @param z zona a la que pertenece, que debe estar registrada
     * @throws ElementoDuplicadoException si ya existe una estación con el mismo id
     * @throws IllegalArgumentException si algún dato es null o inválido (id, latitud, longitud o zona no registrada)
     */
    public void agregarEstacion(EstacionMeteorologica e, ZonaGeografica z) throws ElementoDuplicadoException {
        // La estación y la zona no pueden ser null
        if (e == null || z == null) {
            throw new IllegalArgumentException("La estación y la zona no pueden ser null.");
        }
        // El id tiene que ser positivo
        if (e.getIdEstacion() <= 0) {
            throw new IllegalArgumentException("El id de la estación debe ser mayor que 0.");
        }
        // La latitud tiene que estar entre -90 y 90 grados
        if (e.getLatitud() < -90 || e.getLatitud() > 90) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90.");
        }
        // La longitud tiene que estar entre -180 y 180 grados
        if (e.getLongitud() < -180 || e.getLongitud() > 180) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180.");
        }
        // La zona tiene que estar registrada en el sistema
        if (!zonas.contains(z)) {
            throw new IllegalArgumentException("La zona " + z.getIdZona() + " no está registrada en el sistema.");
        }
        // No puede haber dos estaciones con el mismo id
        if (buscarEstacionPorId(e.getIdEstacion()) != null) {
            throw new ElementoDuplicadoException("Ya existe una estación con id " + e.getIdEstacion() + ".");
        }
        // Recién ahora se agrega, al sistema y a su zona
        estaciones.add(e);
        z.agregarEstacion(e);
    }
 
    
 

    /**
     * Busca una zona por su id.
     *
     * @param id identificador de la zona
     * @return la zona, o null si no existe
     */
    public ZonaGeografica buscarZonaPorId(int id) {
        for (ZonaGeografica z : zonas) {
            if (z.getIdZona() == id) {
                return z;
            }
        }
        return null;
    }
 
    /**
     * Busca una estación por su id.
     *
     * @param id identificador de la estación
     * @return la estación, o null si no existe
     */
    public EstacionMeteorologica buscarEstacionPorId(int id) {
        for (EstacionMeteorologica e : estaciones) {
            if (e.getIdEstacion() == id) {
                return e;
            }
        }
        return null;
    }
 

    /**
     * Busca un sensor por su id.
     *
     * @param id identificador del sensor
     * @return el sensor, o null si no existe
     */
    public Sensor buscarSensorPorId(int id) {
        for (Sensor s : sensores) {
            if (s.getIdSensor() == id) {
                return s;
            }
        }
        return null;
    }
 
    /**
     * Busca un responsable técnico por su DNI.
     *
     * @param dni documento del técnico
     * @return el técnico, o null si no existe
     */
    public ResponsableTecnico buscarTecnicoPorDni(int dni) {
        for (ResponsableTecnico t : tecnicos) {
            if (t.getDni() == dni) {
                return t;
            }
        }
        return null;
    }
 
    /**
     * Busca un equipo de respuesta por su id.
     *
     * @param id identificador del equipo
     * @return el equipo, o null si no existe
     */
    public EquipoRespuesta buscarEquipoPorId(int id) {
        for (EquipoRespuesta e : equipos) {
            if (e.getIdEquipo() == id) {
                return e;
            }
        }
        return null;
    }
    
    /**
     * Busca la estación donde está instalado un sensor.
     * @param s sensor a ubicar
     * @return la estación, o null si el sensor no está instalado en ninguna
     */
    public EstacionMeteorologica buscarEstacionDeSensor(Sensor s) {
        for (EstacionMeteorologica e : estaciones) {
            if (e.getSensores().contains(s)) {
                return e;
            }
        }
        return null;
    }
 
    /**
     * Busca la zona a la que pertenece una estación.
     *
     * @param e estación a ubicar
     * @return la zona, o null si la estación no pertenece a ninguna
     */
    public ZonaGeografica buscarZonaDeEstacion(EstacionMeteorologica e) {
        for (ZonaGeografica z : zonas) {
            if (z.getEstaciones().contains(e)) {
                return z;
            }
        }
        return null;
    }
 
    // REGLAS DE NEGOCIO
 
    /**
     * Instala un sensor en una estación. Un sensor solo puede estar
     * instalado en una estación a la vez.
     *
     * @param s sensor a instalar
     * @param destino estación donde se instala
     * @throws SensorYaInstaladoException si el sensor ya está instalado en otra estación
     */
    public void instalarSensor(Sensor s, EstacionMeteorologica destino) throws SensorYaInstaladoException {
        // Busca en qué estación está instalado el sensor ahora (null si no está en ninguna)
        EstacionMeteorologica actual = buscarEstacionDeSensor(s);
 
        // Si el sensor ya está en otra estación, no deja que se instale 
        if (actual != null && actual != destino) {
            // Se rechaza la operación avisando en qué estación está el sensor
            throw new SensorYaInstaladoException("El sensor " + s.getIdSensor()
                + " ya está instalado en la estación " + actual.getIdEstacion()
                + ". Hay que retirarlo primero.");
        }
 
        // Si no está en ninguna estación, lo instala
        destino.instalarSensor(s);
    }
 
    
    /**
     * Retira un sensor de la estación donde está instalado. Es el primer
     * paso para trasladarlo a otra estación.
     *
     * @param s sensor a retirar
     * @throws SensorSinEstacionException si el sensor no está instalado en ninguna estación
     */
    public void retirarSensor(Sensor s) throws SensorSinEstacionException {
        // Busca en qué estación está instalado el sensor
        EstacionMeteorologica actual = buscarEstacionDeSensor(s);
 
        // Si no está instalado en ninguna estación, lanza una excepción
        if (actual == null) {
            throw new SensorSinEstacionException("El sensor " + s.getIdSensor() + " no está instalado en ninguna estación.");
        }
        // Si encuentra la estación, retira el sensor de ella
        actual.retirarSensor(s);
    }
 
    
    /**
     * Cambia el estado operativo de una estación.
     *
     * @param e estación a modificar
     * @param nuevo nuevo estado
     * @return true si se cambió; false si falta algún dato o la estación no está registrada
     */
    public boolean cambiarEstadoEstacion(EstacionMeteorologica e, EstadoOperativo nuevo) {
        if (e == null || nuevo == null || !estaciones.contains(e)) {
            return false;
        }
        e.setEstado(nuevo);
        return true;
    }
 
    /**
     * Cambia el estado operativo de un sensor.
     * @param s sensor a modificar
     * @param nuevo nuevo estado
     * @return true si se cambió; false si falta algún dato o el sensor no está registrado
     */
    public boolean cambiarEstadoSensor(Sensor s, EstadoOperativo nuevo) {
        if (s == null || nuevo == null || !sensores.contains(s)) {
            return false;
        }
        s.setEstado(nuevo);
        return true;
    }
 
    
    /**
     * Registra un mantenimiento en una estación. Si es de tipo CALIBRACION
     * y tiene sensor, actualiza la fecha de última calibración del sensor.
     * No cambia el estado de la estación ni del sensor; eso se hace con
     * {@link #cambiarEstadoEstacion} y {@link #cambiarEstadoSensor}.
     *
     * @param est estación donde se registra
     * @param id identificador del mantenimiento (positivo)
     * @param fecha fecha en que se realizó
     * @param tipo tipo de mantenimiento
     * @param descripcion descripción de la tarea
     * @param s sensor intervenido, o null si fue sobre toda la estación
     * @param r técnico que lo realizó
     * @return el mantenimiento registrado
     * @throws IllegalArgumentException si el id no es positivo o el sensor no pertenece a la estación
     */
    public Mantenimiento registrarMantenimiento(EstacionMeteorologica est, int id, LocalDate fecha, TipoMantenimiento tipo, String descripcion, Sensor s, ResponsableTecnico r) {
 
        // El id del mantenimiento tiene que ser positivo
        if (id <= 0) {
            throw new IllegalArgumentException("El id del mantenimiento debe ser mayor que 0.");
        }
        // Si hay un sensor y no pertenece a esa estación, se rechaza
        if (s != null && !est.getSensores().contains(s)) {
            throw new IllegalArgumentException("El sensor " + s.getIdSensor() + " no pertenece a la estación " + est.getNombre() + ".");
        }
 
        // Registra el mantenimiento en la estación y guarda el resultado en m
        Mantenimiento m = est.registrarMantenimiento(id, fecha, tipo, descripcion, s, r);
 
        // Si el tipo es calibración y hay sensor, actualiza la fecha de su última calibración
        if (tipo == TipoMantenimiento.CALIBRACION && s != null) {
            s.setUltimaCalibracion(fecha);
        }
 
        // Devuelve el mantenimiento registrado
        return m;
    }
 
    /**
     * Agrega un técnico a un equipo de respuesta. Un técnico puede
     * integrar a lo sumo un equipo.
     *
     * @param equipo equipo al que se agrega
     * @param t técnico a agregar
     * @throws TecnicoYaAsignadoException si el técnico ya integra un equipo
     */
    public void agregarIntegrante(EquipoRespuesta equipo, ResponsableTecnico t)throws TecnicoYaAsignadoException {
        // Recorre todos los equipos del sistema
        for (EquipoRespuesta e : equipos) {
            // Pregunta si el técnico ya está en la lista de integrantes de ese equipo
            if (e.getIntegrantes().contains(t)) {
                // Si ya integra un equipo, se rechaza la operación indicando cuál es
                throw new TecnicoYaAsignadoException("El técnico " + t.getNombreCompleto() + " ya integra el equipo " + e.getNombreEquipo() + ".");
            }
        }
        // Si el for terminó sin encontrarlo, el técnico está libre y se agrega al equipo
        equipo.agregarIntegrante(t);
    }
 
    
    /**
     * Registra una lectura de lluvia: controla que el sensor esté instalado
     * y que la estación esté operativa, guarda la medición y evalúa el riesgo
     * de la zona (lo que puede generar una alerta).
     *
     * @param s sensor que realiza la lectura
     * @param mm milímetros registrados
     * @param fh fecha y hora de la lectura
     * @return la medición creada
     * @throws SensorSinEstacionException si el sensor no está instalado en ninguna estación
     * @throws EstacionNoOperativaException si la estación está en mantenimiento o fuera de servicio
     * @throws LecturaInvalidaException si el sensor no está operativo o los mm están fuera de rango
     */
    public Medicion registrarLectura(Sensor s, double mm, LocalDateTime fh)
            throws SensorSinEstacionException, EstacionNoOperativaException, LecturaInvalidaException {
 
        // Busca en qué estación está instalado el sensor (null si no está en ninguna)
        EstacionMeteorologica estacion = buscarEstacionDeSensor(s);
 
        // Si el sensor no está instalado en ninguna estación, no se puede registrar la lectura
        if (estacion == null) {
            throw new SensorSinEstacionException("El sensor " + s.getIdSensor() + " no está instalado en ninguna estación.");
        }
 
        // Control operativo de la estación (el del sensor lo hace Sensor.registrar)
        if (!estacion.estaOperativa()) {
            // Si está en mantenimiento o fuera de servicio, se rechaza la lectura
            throw new EstacionNoOperativaException("La estación " + estacion.getIdEstacion() + " no está operativa (estado: " + estacion.getEstado() + ").");
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
 
    /**
     * Registra una alerta cargada por un operador. Solo se admiten los
     * eventos CRECIDA_RIO y TORMENTA_SEVERA; LLUVIA_COPIOSA la genera el sistema.
     *
     * @param z zona afectada
     * @param ev tipo de evento (CRECIDA_RIO o TORMENTA_SEVERA)
     * @param nivel nivel de severidad indicado por el operador
     * @param descripcion descripción de la alerta
     * @return la alerta creada
     * @throws IllegalArgumentException si el evento no es CRECIDA_RIO ni TORMENTA_SEVERA
     */
    public Alerta registrarAlertaOperador(ZonaGeografica z, TipoEventoClimatico ev, NivelAlerta nivel, String descripcion) {
        // Solo se permite registrar CRECIDA_RIO o TORMENTA_SEVERA
        if (ev != TipoEventoClimatico.CRECIDA_RIO && ev != TipoEventoClimatico.TORMENTA_SEVERA) {
            throw new IllegalArgumentException("El operador solo puede registrar CRECIDA_RIO o TORMENTA_SEVERA.");
        }
 
        // La zona crea la alerta con el nivel y la descripción que indicó el operador,
        // la guarda en su lista de alertas y la devuelve
        return z.emitirAlerta(ev, nivel, descripcion);
    }
 
    
    /**
     * Asigna un equipo de respuesta a una alerta NARANJA o ROJA y lo marca
     * como ocupado.
     *
     * @param a alerta que requiere equipo
     * @param e equipo a asignar
     * @throws AlertaSinEquipoException si la alerta no requiere equipo o ya tiene uno,
     *         o si el equipo no tiene integrantes o no está disponible
     */
    public void asignarEquipo(Alerta a, EquipoRespuesta e) throws AlertaSinEquipoException {
 
        // La alerta tiene que requerir equipo (NARANJA o ROJA)
        if (!a.requiereEquipo()) {
            throw new AlertaSinEquipoException("La alerta " + a.getIdAlerta() + " no requiere equipo (nivel " + a.getNivel() + ").");
        }
 
        // La alerta no puede tener ya un equipo asignado
        if (a.getEquipo() != null) {
            throw new AlertaSinEquipoException("La alerta " + a.getIdAlerta() + " ya tiene el equipo " + a.getEquipo().getNombreEquipo() + ".");
        }
 
        // El equipo tiene que tener integrantes
        if (e.getIntegrantes().isEmpty()) {
            throw new AlertaSinEquipoException("El equipo " + e.getNombreEquipo() + " no tiene integrantes.");
        }
 
        // El equipo tiene que estar disponible
        if (!e.isDisponible()) {
            throw new AlertaSinEquipoException("El equipo " + e.getNombreEquipo() + " no está disponible.");
        }
 
        // Asigna el equipo a la alerta y lo marca como ocupado
        a.asignarEquipo(e);
        e.setDisponible(false);
    }
 
    /**
     * Libera al equipo cuando termina la emergencia. La alerta queda como
     * atendida y conserva el equipo que la atendió.
     *
     * @param a alerta atendida
     * @throws AlertaSinEquipoException si la alerta no tiene equipo asignado
     */
    public void liberarEquipo(Alerta a) throws AlertaSinEquipoException {
        if (a.getEquipo() == null) {
            throw new AlertaSinEquipoException(
                "La alerta " + a.getIdAlerta() + " no tiene equipo asignado.");
        }
        a.getEquipo().setDisponible(true);
    }
 
    /**
     * Lista los equipos que están disponibles y tienen al menos un integrante.
     * @return los equipos que pueden atender una emergencia
     */
    public List<EquipoRespuesta> equiposDisponibles() {
        List<EquipoRespuesta> libres = new ArrayList<>();
        for (EquipoRespuesta e : equipos) {
            if (e.isDisponible() && !e.getIntegrantes().isEmpty()) {
                libres.add(e);
            }
        }
        return libres;
    }
 
    
    /**
     * Lista las alertas NARANJA o ROJA que todavía no tienen equipo asignado.
     * @return las alertas pendientes de asignación de equipo
     */
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
