package cr.ac.proyecto.model;

import cr.ac.proyecto.exception.JugadorDuplicadoException;
import java.util.ArrayList;
import java.util.List;

public class Equipo {

    private String nombre;
    private Entrenador entrenador;
    private List<Jugador> jugadores;

    public Equipo(String nombre, Entrenador entrenador) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo es obligatorio.");
        }
        if (entrenador == null) {
            throw new IllegalArgumentException("El equipo debe tener un entrenador.");
        }
        this.nombre = nombre;
        this.entrenador = entrenador;
        this.jugadores = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public boolean tieneJugador(String identificacion) {
        for (Jugador j : jugadores) {
            if (j.getIdentificacion().equals(identificacion)) {
                return true;
            }
        }
        return false;
    }

    public void agregarJugador(Jugador jugador) throws JugadorDuplicadoException {
        if (tieneJugador(jugador.getIdentificacion())) {
            throw new JugadorDuplicadoException(
                    "El jugador " + jugador.getNombre() + " ya pertenece al equipo " + nombre + ".");
        }
        jugadores.add(jugador);
    }
}
