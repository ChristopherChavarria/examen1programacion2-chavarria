package cr.ac.proyecto.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Torneo {

    private String nombre;
    private Disciplina disciplina;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<Equipo> equipos;
    private List<Encuentro> encuentros;

    public Torneo(String nombre, Disciplina disciplina, LocalDate fechaInicio, LocalDate fechaFin) {
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
        return equipos;
    }

    public List<Encuentro> getEncuentros() {
        return encuentros;
    }

    public void inscribirEquipo(Equipo equipo) {
        equipos.add(equipo);
    }

    public Encuentro programarEncuentro(Equipo local, Equipo visitante, LocalDate fecha, Arbitro arbitro) {
        Encuentro encuentro = new Encuentro(local, visitante, fecha, arbitro);
        encuentros.add(encuentro);
        return encuentro;
    }
}
