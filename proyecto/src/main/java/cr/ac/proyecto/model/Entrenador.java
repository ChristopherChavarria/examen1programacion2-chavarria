package cr.ac.proyecto.model;

public class Entrenador extends Persona {

    private int aniosExperiencia;

    public Entrenador(String identificacion, String nombre, int aniosExperiencia) {
        super(identificacion, nombre);
        this.aniosExperiencia = aniosExperiencia;
    }

    public int getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(int aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    @Override
    public String describirRol() {
        return "Entrenador con " + aniosExperiencia + " años de experiencia";
    }
}
