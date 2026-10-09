package cr.ac.proyecto.model;

import java.util.ArrayList;
import java.util.List;

public class Equipo {

    private String nombre;
    private Entrenador entrenador;
    private List<Jugador> jugadores;

    public Equipo(String nombre, Entrenador entrenador) {
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

    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
    }
}
