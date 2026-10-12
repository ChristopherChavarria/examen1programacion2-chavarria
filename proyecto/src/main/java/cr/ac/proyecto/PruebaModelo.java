package cr.ac.proyecto;

import cr.ac.proyecto.exception.EncuentroInvalidoException;
import cr.ac.proyecto.exception.JugadorDuplicadoException;
import cr.ac.proyecto.model.Arbitro;
import cr.ac.proyecto.model.Cancelable;
import cr.ac.proyecto.model.Disciplina;
import cr.ac.proyecto.model.Encuentro;
import cr.ac.proyecto.model.Entrenador;
import cr.ac.proyecto.model.Equipo;
import cr.ac.proyecto.model.Jugador;
import cr.ac.proyecto.model.Persona;
import cr.ac.proyecto.model.Torneo;
import cr.ac.proyecto.service.GestorTorneos;
import java.time.LocalDate;

/**
 * Prueba manual del modelo por consola (Corte 2). No usa JavaFX.
 */
public class PruebaModelo {

    public static void main(String[] args) {
        GestorTorneos gestor = new GestorTorneos();

        try {
            // ----- 1. Registro de personas -----
            Entrenador entrenadorA = new Entrenador("1-0001", "Carlos Mora", 8);
            Entrenador entrenadorB = new Entrenador("1-0002", "Luis Vargas", 3);
            Entrenador entrenadorC = new Entrenador("1-0003", "Ana Rojas", 5);
            Arbitro arbitro = new Arbitro("2-0001", "Pedro Solano", "Nacional");
            Jugador j1 = new Jugador("3-0001", "Andres Castro", 10, "Delantero");
            Jugador j2 = new Jugador("3-0002", "Diego Arias", 1, "Portero");
            Jugador j3 = new Jugador("3-0003", "Mario Quesada", 9, "Delantero");
            Jugador j4 = new Jugador("3-0004", "Jose Brenes", 4, "Defensa");
            Jugador j5 = new Jugador("3-0005", "Kevin Salas", 7, "Volante");

            gestor.registrarPersona(entrenadorA);
            gestor.registrarPersona(entrenadorB);
            gestor.registrarPersona(entrenadorC);
            gestor.registrarPersona(arbitro);
            gestor.registrarPersona(j1);
            gestor.registrarPersona(j2);
            gestor.registrarPersona(j3);
            gestor.registrarPersona(j4);
            gestor.registrarPersona(j5);

            // ----- 2. Polimorfismo: misma llamada, respuesta distinta segun el tipo -----
            System.out.println("=== Personas registradas (polimorfismo) ===");
            for (Persona p : gestor.listarPersonas()) {
                System.out.println(p.getNombre() + " -> " + p.describirRol());
            }

            // ----- 3. Equipos -----
            Equipo tigres = new Equipo("Tigres", entrenadorA);
            tigres.agregarJugador(j1);
            tigres.agregarJugador(j2);
            Equipo leones = new Equipo("Leones", entrenadorB);
            leones.agregarJugador(j3);
            leones.agregarJugador(j4);
            Equipo pumas = new Equipo("Pumas", entrenadorC);
            pumas.agregarJugador(j5);

            gestor.registrarEquipo(tigres);
            gestor.registrarEquipo(leones);
            gestor.registrarEquipo(pumas);

            // ----- 4. Torneo, inscripciones y encuentros -----
            Torneo copa = new Torneo("Copa UNA", Disciplina.FUTBOL,
                    LocalDate.of(2026, 10, 15), LocalDate.of(2026, 11, 15));
            gestor.registrarTorneo(copa);
            copa.inscribirEquipo(tigres);
            copa.inscribirEquipo(leones);
            copa.inscribirEquipo(pumas);

            Encuentro e1 = copa.programarEncuentro(tigres, leones, LocalDate.of(2026, 10, 16), arbitro);
            Encuentro e2 = copa.programarEncuentro(leones, pumas, LocalDate.of(2026, 10, 18), arbitro);
            Encuentro e3 = copa.programarEncuentro(tigres, pumas, LocalDate.of(2026, 10, 20), arbitro);

            e1.registrarResultado(2, 1);
            e2.registrarResultado(0, 0);

            // ----- 5. Uso de la interface: se cancela a traves del tipo Cancelable -----
            Cancelable cancelable = e3;
            cancelable.cancelar();

            System.out.println();
            System.out.println("=== Encuentros de " + copa.getNombre() + " ===");
            for (Encuentro e : copa.getEncuentros()) {
                System.out.println(e.getFecha() + "  " + e.getEquipoLocal().getNombre() + " "
                        + e.getMarcadorLocal() + " - " + e.getMarcadorVisitante() + " "
                        + e.getEquipoVisitante().getNombre() + "  [" + e.getEstado() + "]");
            }

            // ----- 6. Busqueda y tabla de posiciones -----
            System.out.println();
            System.out.println("=== Busquedas ===");
            System.out.println("Equipo 'Leones': entrenador " + gestor.buscarEquipo("Leones").getEntrenador().getNombre());
            System.out.println("Persona 3-0001: " + gestor.buscarPersona("3-0001").getNombre());

            System.out.println();
            System.out.println("=== Tabla de posiciones ===");
            int posicion = 1;
            for (Equipo equipo : gestor.tablaDePosiciones(copa)) {
                System.out.println(posicion + ". " + equipo.getNombre() + " - " + copa.calcularPuntos(equipo) + " pts");
                posicion++;
            }

            // ----- 7. Excepciones controladas: reglas de negocio -----
            System.out.println();
            System.out.println("=== Reglas de negocio (excepciones controladas) ===");

            try {
                tigres.agregarJugador(j1);
            } catch (JugadorDuplicadoException ex) {
                System.out.println("JugadorDuplicadoException: " + ex.getMessage());
            }

            try {
                Equipo halcones = new Equipo("Halcones", entrenadorC);
                halcones.agregarJugador(j3);
                copa.inscribirEquipo(halcones);
            } catch (JugadorDuplicadoException ex) {
                System.out.println("JugadorDuplicadoException: " + ex.getMessage());
            }

            try {
                copa.programarEncuentro(tigres, tigres, LocalDate.of(2026, 10, 25), arbitro);
            } catch (EncuentroInvalidoException ex) {
                System.out.println("EncuentroInvalidoException: " + ex.getMessage());
            }

            try {
                Encuentro e4 = copa.programarEncuentro(leones, tigres, LocalDate.of(2026, 10, 27), arbitro);
                e4.registrarResultado(-1, 2);
            } catch (EncuentroInvalidoException ex) {
                System.out.println("EncuentroInvalidoException: " + ex.getMessage());
            }

            try {
                e3.registrarResultado(1, 0);
            } catch (EncuentroInvalidoException ex) {
                System.out.println("EncuentroInvalidoException: " + ex.getMessage());
            }

            try {
                gestor.registrarPersona(new Jugador("3-0001", "Otro Jugador", 11, "Defensa"));
            } catch (IllegalArgumentException ex) {
                System.out.println("IllegalArgumentException: " + ex.getMessage());
            }

        } catch (JugadorDuplicadoException | EncuentroInvalidoException ex) {
            System.out.println("Error inesperado en la prueba: " + ex.getMessage());
        }
    }
}
