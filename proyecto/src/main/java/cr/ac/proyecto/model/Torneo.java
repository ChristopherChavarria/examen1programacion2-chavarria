package cr.ac.proyecto.model;

import cr.ac.proyecto.exception.EncuentroInvalidoException;
import cr.ac.proyecto.exception.JugadorDuplicadoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Torneo {

    private String nombre;
    private Disciplina disciplina;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<Equipo> equipos;
    private List<Encuentro> encuentros;

    public Torneo(String nombre, Disciplina disciplina, LocalDate fechaInicio, LocalDate fechaFin) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del torneo es obligatorio.");
        }
        if (fechaInicio == null || fechaFin == null || fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("Las fechas del torneo no son válidas.");
        }
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.equipos = new ArrayList<>();
        this.encuentros = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<Equipo> getEquipos() {
        return Collections.unmodifiableList(equipos);
    }

    public List<Encuentro> getEncuentros() {
        return Collections.unmodifiableList(encuentros);
    }

    public void inscribirEquipo(Equipo equipo) throws JugadorDuplicadoException {
        if (equipos.contains(equipo)) {
            throw new IllegalArgumentException("El equipo " + equipo.getNombre() + " ya está inscrito.");
        }
        if (equipo.getJugadores().isEmpty()) {
            throw new IllegalArgumentException("El equipo " + equipo.getNombre() + " no tiene jugadores.");
        }
        for (Jugador jugador : equipo.getJugadores()) {
            for (Equipo inscrito : equipos) {
                if (inscrito.tieneJugador(jugador.getIdentificacion())) {
                    throw new JugadorDuplicadoException(
                            "El jugador " + jugador.getNombre() + " ya está inscrito en este torneo con el equipo "
                            + inscrito.getNombre() + ".");
                }
            }
        }
        equipos.add(equipo);
    }

    public Encuentro programarEncuentro(Equipo local, Equipo visitante, LocalDate fecha, Arbitro arbitro)
            throws EncuentroInvalidoException {
        if (!equipos.contains(local) || !equipos.contains(visitante)) {
            throw new EncuentroInvalidoException("Los dos equipos deben estar inscritos en el torneo.");
        }
        if (fecha != null && (fecha.isBefore(fechaInicio) || fecha.isAfter(fechaFin))) {
            throw new EncuentroInvalidoException("La fecha del encuentro debe estar entre "
                    + fechaInicio + " y " + fechaFin + ".");
        }
        Encuentro encuentro = new Encuentro(local, visitante, fecha, arbitro);
        encuentros.add(encuentro);
        return encuentro;
    }

    public int calcularPuntos(Equipo equipo) {
        int puntos = 0;
        for (Encuentro e : encuentros) {
            if (e.getEstado() != EstadoEncuentro.JUGADO) {
                continue;
            }
            boolean esLocal = e.getEquipoLocal() == equipo;
            boolean esVisitante = e.getEquipoVisitante() == equipo;
            if (!esLocal && !esVisitante) {
                continue;
            }
            int aFavor = esLocal ? e.getMarcadorLocal() : e.getMarcadorVisitante();
            int enContra = esLocal ? e.getMarcadorVisitante() : e.getMarcadorLocal();
            if (aFavor > enContra) {
                puntos += 3;
            } else if (aFavor == enContra) {
                puntos += 1;
            }
        }
        return puntos;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
