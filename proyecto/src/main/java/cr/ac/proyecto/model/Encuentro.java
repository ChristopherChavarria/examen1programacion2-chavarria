package cr.ac.proyecto.model;

import cr.ac.proyecto.exception.EncuentroInvalidoException;
import java.time.LocalDate;

public class Encuentro implements Cancelable {

    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private LocalDate fecha;
    private Arbitro arbitro;
    private int marcadorLocal;
    private int marcadorVisitante;
    private EstadoEncuentro estado;

    public Encuentro(Equipo equipoLocal, Equipo equipoVisitante, LocalDate fecha, Arbitro arbitro)
            throws EncuentroInvalidoException {
        if (equipoLocal == null || equipoVisitante == null) {
            throw new EncuentroInvalidoException("El encuentro necesita dos equipos.");
        }
        if (equipoLocal == equipoVisitante) {
            throw new EncuentroInvalidoException("Un equipo no puede enfrentarse a sí mismo.");
        }
        if (fecha == null) {
            throw new EncuentroInvalidoException("El encuentro debe tener una fecha.");
        }
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

    public void registrarResultado(int marcadorLocal, int marcadorVisitante) throws EncuentroInvalidoException {
        if (estado != EstadoEncuentro.PROGRAMADO) {
            throw new EncuentroInvalidoException(
                    "No se puede registrar el resultado: el encuentro está " + estado + ".");
        }
        if (marcadorLocal < 0 || marcadorVisitante < 0) {
            throw new EncuentroInvalidoException("Los marcadores no pueden ser negativos.");
        }
        this.marcadorLocal = marcadorLocal;
        this.marcadorVisitante = marcadorVisitante;
        this.estado = EstadoEncuentro.JUGADO;
    }

    @Override
    public void cancelar() {
        if (estado == EstadoEncuentro.JUGADO) {
            throw new IllegalStateException("No se puede cancelar un encuentro que ya se jugó.");
        }
        this.estado = EstadoEncuentro.CANCELADO;
    }
}
