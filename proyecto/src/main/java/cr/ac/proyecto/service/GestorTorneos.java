package cr.ac.proyecto.service;

import cr.ac.proyecto.model.Arbitro;
import cr.ac.proyecto.model.Equipo;
import cr.ac.proyecto.model.Persona;
import cr.ac.proyecto.model.Torneo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorTorneos {

    
    private static GestorTorneos instancia;

    public static GestorTorneos getInstancia() {
        if (instancia == null) {
            instancia = new GestorTorneos();
            DatosDePrueba.cargar(instancia);
        }
        return instancia;
    }

    private List<Torneo> torneos;
    private List<Persona> personas;
    private Map<String, Equipo> equiposPorNombre;

    public GestorTorneos() {
        this.torneos = new ArrayList<>();
        this.personas = new ArrayList<>();
        this.equiposPorNombre = new HashMap<>();
    }


    public void registrarPersona(Persona persona) {
        if (buscarPersona(persona.getIdentificacion()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una persona con la identificación " + persona.getIdentificacion() + ".");
        }
        personas.add(persona);
    }

    public Persona buscarPersona(String identificacion) {
        for (Persona p : personas) {
            if (p.getIdentificacion().equals(identificacion)) {
                return p;
            }
        }
        return null;
    }

    public List<Persona> listarPersonas() {
        return Collections.unmodifiableList(personas);
    }

    public List<Arbitro> listarArbitros() {
        List<Arbitro> arbitros = new ArrayList<>();
        for (Persona p : personas) {
            if (p instanceof Arbitro) {
                arbitros.add((Arbitro) p);
            }
        }
        return arbitros;
    }

 

    public void registrarEquipo(Equipo equipo) {
        if (equiposPorNombre.containsKey(equipo.getNombre())) {
            throw new IllegalArgumentException("Ya existe un equipo llamado " + equipo.getNombre() + ".");
        }
        equiposPorNombre.put(equipo.getNombre(), equipo);
    }

    public Equipo buscarEquipo(String nombre) {
        return equiposPorNombre.get(nombre);
    }

    public List<Equipo> listarEquipos() {
        return new ArrayList<>(equiposPorNombre.values());
    }


    public void registrarTorneo(Torneo torneo) {
        if (buscarTorneo(torneo.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe un torneo llamado " + torneo.getNombre() + ".");
        }
        torneos.add(torneo);
    }

    public Torneo buscarTorneo(String nombre) {
        for (Torneo t : torneos) {
            if (t.getNombre().equalsIgnoreCase(nombre)) {
                return t;
            }
        }
        return null;
    }

    public List<Torneo> listarTorneos() {
        return Collections.unmodifiableList(torneos);
    }

    public boolean eliminarTorneo(String nombre) {
        Torneo torneo = buscarTorneo(nombre);
        if (torneo == null) {
            return false;
        }
        return torneos.remove(torneo);
    }


    public List<Equipo> tablaDePosiciones(Torneo torneo) {
        List<Equipo> tabla = new ArrayList<>(torneo.getEquipos());
        tabla.sort((a, b) -> torneo.calcularPuntos(b) - torneo.calcularPuntos(a));
        return tabla;
    }
}
