# Corte 2 — Modelo orientado a objetos funcional

## Cambios al diseño

El diseño del Corte 1 se mantiene: las mismas clases, la misma jerarquía de Persona, la interface Cancelable y los dos enums. Lo que cambió fue que las clases pasaron de ser solo la estructura a tener comportamiento y reglas:

- Se agregó el paquete `service` con la clase `GestorTorneos`, que guarda las colecciones de todo el sistema (torneos, equipos y personas).
- Se agregó el paquete `exception` con dos excepciones personalizadas: `JugadorDuplicadoException` y `EncuentroInvalidoException`.
- `Equipo` ganó el método `tieneJugador` y `agregarJugador` ahora valida duplicados.
- `Encuentro` ganó el método `registrarResultado`, y su constructor y `cancelar` ahora validan.
- `Torneo` ganó el método `calcularPuntos`, y `inscribirEquipo` y `programarEncuentro` ahora validan.
- `Persona`, `Equipo` y `Torneo` validan en el constructor los campos obligatorios.

El UML del Corte 1 sigue siendo válido en cuanto a clases y relaciones. Los métodos nuevos, el gestor y las excepciones se incorporarán en el UML final del Corte 3.

## Clases implementadas

| Clase | Paquete | Descripción |
|---|---|---|
| Persona | model | Clase abstracta con identificación y nombre. Declara el método abstracto describirRol(). |
| Jugador | model | Persona con número de camiseta y posición. |
| Entrenador | model | Persona con años de experiencia. |
| Arbitro | model | Persona con categoría. |
| Equipo | model | Tiene un entrenador y una lista de jugadores. No permite jugadores repetidos. |
| Encuentro | model | Partido entre dos equipos con árbitro, marcador y estado. Implementa Cancelable. |
| Torneo | model | Contiene los equipos inscritos y los encuentros. Calcula los puntos de cada equipo. |
| Cancelable | model | Interface con el método cancelar(). |
| EstadoEncuentro | model | Enum con los estados PROGRAMADO, JUGADO y CANCELADO. |
| Disciplina | model | Enum con las disciplinas FUTBOL, BALONCESTO y VOLEIBOL. |
| GestorTorneos | service | Administra las colecciones en memoria y las operaciones de registrar, buscar, listar y eliminar. |
| JugadorDuplicadoException | exception | Se lanza cuando un jugador ya está en el equipo o en otro equipo del mismo torneo. |
| EncuentroInvalidoException | exception | Se lanza cuando un encuentro o su resultado no cumple las reglas. |
| PruebaModelo | cr.ac.proyecto | Clase con main() que prueba el modelo por consola. |

## Colecciones utilizadas

| Colección | Tipo (`List`, `Map`, …) | Clase donde se encuentra | Propósito |
|---|---|---|---|
| torneos | `List<Torneo>` | GestorTorneos | Guardar todos los torneos registrados. |
| personas | `List<Persona>` | GestorTorneos | Guardar jugadores, entrenadores y árbitros en una sola lista del tipo de la superclase. |
| equiposPorNombre | `Map<String, Equipo>` | GestorTorneos | Buscar un equipo directamente por su nombre y evitar nombres repetidos. |
| jugadores | `List<Jugador>` | Equipo | Jugadores que pertenecen al equipo. |
| equipos | `List<Equipo>` | Torneo | Equipos inscritos en el torneo. |
| encuentros | `List<Encuentro>` | Torneo | Encuentros programados en el torneo. |

## Ejemplo de herencia

```java
public abstract class Persona {

    private String identificacion;
    private String nombre;

    public Persona(String identificacion, String nombre) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("La identificación es obligatoria.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.identificacion = identificacion;
        this.nombre = nombre;
    }

    public abstract String describirRol();
}

public class Jugador extends Persona {

    private int numeroCamiseta;
    private String posicion;

    public Jugador(String identificacion, String nombre, int numeroCamiseta, String posicion) {
        super(identificacion, nombre);
        this.numeroCamiseta = numeroCamiseta;
        this.posicion = posicion;
    }

    @Override
    public String describirRol() {
        return "Jugador, camiseta " + numeroCamiseta + ", posición " + posicion;
    }
}
```

Jugador hereda identificación y nombre de Persona y los inicializa con `super(...)`. Como la validación de campos obligatorios está en el constructor de Persona, también aplica para Entrenador y Arbitro sin repetir el código.

## Ejemplo de polimorfismo

```java
// PruebaModelo.java
for (Persona p : gestor.listarPersonas()) {
    System.out.println(p.getNombre() + " -> " + p.describirRol());
}
```

La lista es de tipo Persona, pero contiene jugadores, entrenadores y árbitros. La llamada `p.describirRol()` es la misma para todos y cada objeto responde con la versión de su propia clase:

```text
Carlos Mora -> Entrenador con 8 años de experiencia
Pedro Solano -> Árbitro, categoría Nacional
Andres Castro -> Jugador, camiseta 10, posición Delantero
```

## Interface

```java
public interface Cancelable {
    void cancelar();
}

public class Encuentro implements Cancelable {

    @Override
    public void cancelar() {
        if (estado == EstadoEncuentro.JUGADO) {
            throw new IllegalStateException("No se puede cancelar un encuentro que ya se jugó.");
        }
        this.estado = EstadoEncuentro.CANCELADO;
    }
}
```

Se utiliza a través de una variable del tipo de la interface:

```java
// PruebaModelo.java
Cancelable cancelable = e3;
cancelable.cancelar();
```

## Excepción personalizada

```java
package cr.ac.proyecto.exception;

public class JugadorDuplicadoException extends Exception {

    public JugadorDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
```

Se lanza desde el modelo, en `Equipo.agregarJugador`:

```java
public void agregarJugador(Jugador jugador) throws JugadorDuplicadoException {
    if (tieneJugador(jugador.getIdentificacion())) {
        throw new JugadorDuplicadoException(
                "El jugador " + jugador.getNombre() + " ya pertenece al equipo " + nombre + ".");
    }
    jugadores.add(jugador);
}
```

Y se captura en quien invoca la operación:

```java
// PruebaModelo.java
try {
    tigres.agregarJugador(j1);
} catch (JugadorDuplicadoException ex) {
    System.out.println("JugadorDuplicadoException: " + ex.getMessage());
}
```

La segunda excepción personalizada, `EncuentroInvalidoException`, tiene la misma estructura y se lanza desde `Encuentro` y `Torneo`.

## Reglas implementadas

| Regla | Clase / método | Excepción que se lanza |
|---|---|---|
| Un jugador no puede estar dos veces en el mismo equipo | Equipo.agregarJugador | JugadorDuplicadoException |
| Un jugador no puede pertenecer a dos equipos del mismo torneo (RF-09) | Torneo.inscribirEquipo | JugadorDuplicadoException |
| Un equipo no puede enfrentarse a sí mismo (RF-10) | Constructor de Encuentro | EncuentroInvalidoException |
| Los dos equipos de un encuentro deben estar inscritos en el torneo | Torneo.programarEncuentro | EncuentroInvalidoException |
| Los marcadores no pueden ser negativos (RF-11) | Encuentro.registrarResultado | EncuentroInvalidoException |
| Solo se registra resultado en un encuentro PROGRAMADO (RF-11) | Encuentro.registrarResultado | EncuentroInvalidoException |
| No se puede cancelar un encuentro que ya se jugó | Encuentro.cancelar | IllegalStateException |
| Un equipo sin jugadores o ya inscrito no se puede inscribir | Torneo.inscribirEquipo | IllegalArgumentException |
| Identificación y nombre de una persona son obligatorios | Constructor de Persona | IllegalArgumentException |
| El nombre y el entrenador del equipo son obligatorios | Constructor de Equipo | IllegalArgumentException |
| La fecha final del torneo no puede ser anterior a la inicial | Constructor de Torneo | IllegalArgumentException |
| No se repiten identificaciones de personas ni nombres de equipos o torneos | GestorTorneos.registrarPersona / registrarEquipo / registrarTorneo | IllegalArgumentException |

## Evidencia de ejecución de la prueba del modelo

Clase de prueba: `cr.ac.proyecto.PruebaModelo`

```text
=== Personas registradas (polimorfismo) ===
Carlos Mora -> Entrenador con 8 años de experiencia
Luis Vargas -> Entrenador con 3 años de experiencia
Ana Rojas -> Entrenador con 5 años de experiencia
Pedro Solano -> Árbitro, categoría Nacional
Andres Castro -> Jugador, camiseta 10, posición Delantero
Diego Arias -> Jugador, camiseta 1, posición Portero
Mario Quesada -> Jugador, camiseta 9, posición Delantero
Jose Brenes -> Jugador, camiseta 4, posición Defensa
Kevin Salas -> Jugador, camiseta 7, posición Volante

=== Encuentros de Copa UNA ===
2026-10-16  Tigres 2 - 1 Leones  [JUGADO]
2026-10-18  Leones 0 - 0 Pumas  [JUGADO]
2026-10-20  Tigres 0 - 0 Pumas  [CANCELADO]

=== Busquedas ===
Equipo 'Leones': entrenador Luis Vargas
Persona 3-0001: Andres Castro

=== Tabla de posiciones ===
1. Tigres - 3 pts
2. Leones - 1 pts
3. Pumas - 1 pts

=== Reglas de negocio (excepciones controladas) ===
JugadorDuplicadoException: El jugador Andres Castro ya pertenece al equipo Tigres.
JugadorDuplicadoException: El jugador Mario Quesada ya está inscrito en este torneo con el equipo Leones.
EncuentroInvalidoException: Un equipo no puede enfrentarse a sí mismo.
EncuentroInvalidoException: Los marcadores no pueden ser negativos.
EncuentroInvalidoException: No se puede registrar el resultado: el encuentro está CANCELADO.
IllegalArgumentException: Ya existe una persona con la identificación 3-0001.
```

## Distribución del trabajo

| Integrante | Aportes principales |
|---|---|
| Christopher Chavarria Solorzano | Trabajo individual: validaciones del modelo, excepciones personalizadas, clase GestorTorneos, prueba del modelo y documento del corte. |
