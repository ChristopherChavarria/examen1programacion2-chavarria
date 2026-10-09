package cr.ac.proyecto.model;

import java.time.LocalDate;

public class Encuentro implements Cancelable {

    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private LocalDate fecha;
    private Arbitro arbitro;
    private int marcadorLocal;
    private int marcadorVisitante;
    private EstadoEncuentro estado;

    public Encuentro(Equipo equipoLocal, Equipo equipoVisitante, LocalDate fecha, Arbitro arbitro) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.fecha = fecha;
        this.arbitro = arbitro;
        this.marcadorLocal = 0;
        this.marcadorVisitante = 0;
        this.estado = EstadoEncuentro.PROGRAMADO;
    }

    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    public Equipo getEquipoVisitante() {
        return equipoVisitante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Arbitro getArbitro() {
        return arbitro;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }

    public int getMarcadorLocal() {
        return marcadorLocal;
    }

    public int getMarcadorVisitante() {
        return marcadorVisitante;
    }

    public EstadoEncuentro getEstado() {
        return estado;
    }

    @Override
    public void cancelar() {
        this.estado = EstadoEncuentro.CANCELADO;
    }
}
