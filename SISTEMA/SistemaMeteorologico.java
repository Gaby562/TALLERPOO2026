package SISTEMA;
import java.time.LocalDateTime;

import enums.NivelAlerta;
import enums.TipoEventoClimatico;


import java.util.ArrayList;
import java.util.List;

import CLASES.EquipoRespuesta;
import CLASES.EstacionMeteorologica;
import CLASES.ResponsableTecnico;
import CLASES.Sensor;
import CLASES.ZonaGeografica;

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

    // se busca la zona por su id
    public ZonaGeografica buscarZonaPorId(int id) {
        for (ZonaGeografica z : zonas) {
            if (z.getIdZona() == id) {
                return z;
            }
        }
        return null;
    }

    
}
