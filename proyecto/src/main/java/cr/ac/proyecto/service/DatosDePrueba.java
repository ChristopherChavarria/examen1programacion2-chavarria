package cr.ac.proyecto.service;

import cr.ac.proyecto.exception.EncuentroInvalidoException;
import cr.ac.proyecto.exception.JugadorDuplicadoException;
import cr.ac.proyecto.model.Arbitro;
import cr.ac.proyecto.model.Disciplina;
import cr.ac.proyecto.model.Encuentro;
import cr.ac.proyecto.model.Entrenador;
import cr.ac.proyecto.model.Equipo;
import cr.ac.proyecto.model.Jugador;
import cr.ac.proyecto.model.Torneo;
import java.time.LocalDate;

/**
 * Carga datos iniciales en memoria para que la aplicación
 * tenga información desde el arranque (útil para la demostración).
 */
public class DatosDePrueba {

    public static void cargar(GestorTorneos gestor) {
        try {
            Entrenador e1 = new Entrenador("1-0001", "Carlos Mora", 8);
            Entrenador e2 = new Entrenador("1-0002", "Luis Vargas", 3);
            Entrenador e3 = new Entrenador("1-0003", "Ana Rojas", 5);
            Entrenador e4 = new Entrenador("1-0004", "Sofia Leon", 10);
            Arbitro a1 = new Arbitro("2-0001", "Pedro Solano", "Nacional");
            Arbitro a2 = new Arbitro("2-0002", "Marta Ulate", "Regional");

            Jugador j1 = new Jugador("3-0001", "Andres Castro", 10, "Delantero");
            Jugador j2 = new Jugador("3-0002", "Diego Arias", 1, "Portero");
            Jugador j3 = new Jugador("3-0003", "Mario Quesada", 9, "Delantero");
            Jugador j4 = new Jugador("3-0004", "Jose Brenes", 4, "Defensa");
            Jugador j5 = new Jugador("3-0005", "Kevin Salas", 7, "Volante");
            Jugador j6 = new Jugador("3-0006", "Pablo Mena", 5, "Defensa");
            Jugador j7 = new Jugador("3-0007", "Ivan Soto", 11, "Delantero");
            Jugador j8 = new Jugador("3-0008", "Raul Cordero", 2, "Defensa");

            gestor.registrarPersona(e1);
            gestor.registrarPersona(e2);
            gestor.registrarPersona(e3);
            gestor.registrarPersona(e4);
            gestor.registrarPersona(a1);
            gestor.registrarPersona(a2);
            gestor.registrarPersona(j1);
            gestor.registrarPersona(j2);
            gestor.registrarPersona(j3);
            gestor.registrarPersona(j4);
            gestor.registrarPersona(j5);
            gestor.registrarPersona(j6);
            gestor.registrarPersona(j7);
            gestor.registrarPersona(j8);

            Equipo tigres = new Equipo("Tigres", e1);
            tigres.agregarJugador(j1);
            tigres.agregarJugador(j2);
            Equipo leones = new Equipo("Leones", e2);
            leones.agregarJugador(j3);
            leones.agregarJugador(j4);
            Equipo pumas = new Equipo("Pumas", e3);
            pumas.agregarJugador(j5);
            pumas.agregarJugador(j6);
            Equipo halcones = new Equipo("Halcones", e4);
            halcones.agregarJugador(j7);
            halcones.agregarJugador(j8);

            gestor.registrarEquipo(tigres);
            gestor.registrarEquipo(leones);
            gestor.registrarEquipo(pumas);
            gestor.registrarEquipo(halcones);

            Torneo copa = new Torneo("Copa UNA", Disciplina.FUTBOL,
                    LocalDate.of(2026, 10, 15), LocalDate.of(2026, 11, 30));
            gestor.registrarTorneo(copa);
            copa.inscribirEquipo(tigres);
            copa.inscribirEquipo(leones);
            copa.inscribirEquipo(pumas);
            copa.inscribirEquipo(halcones);

            Encuentro p1 = copa.programarEncuentro(tigres, leones, LocalDate.of(2026, 10, 16), a1);
            Encuentro p2 = copa.programarEncuentro(pumas, halcones, LocalDate.of(2026, 10, 17), a2);
            copa.programarEncuentro(tigres, pumas, LocalDate.of(2026, 10, 23), a1);
            copa.programarEncuentro(leones, halcones, LocalDate.of(2026, 10, 24), a2);
            p1.registrarResultado(2, 1);
            p2.registrarResultado(1, 1);

            Torneo liga = new Torneo("Liga Recreativa", Disciplina.BALONCESTO,
                    LocalDate.of(2026, 11, 1), LocalDate.of(2026, 12, 15));
            gestor.registrarTorneo(liga);

        } catch (JugadorDuplicadoException | EncuentroInvalidoException e) {
            System.out.println("No se pudieron cargar los datos de prueba: " + e.getMessage());
        }
    }
}
