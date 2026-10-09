package cr.ac.proyecto.model;

public class Jugador extends Persona {

    private int numeroCamiseta;
    private String posicion;

    public Jugador(String identificacion, String nombre, int numeroCamiseta, String posicion) {
        super(identificacion, nombre);
        this.numeroCamiseta = numeroCamiseta;
        this.posicion = posicion;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(int numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    @Override
    public String describirRol() {
        return "Jugador, camiseta " + numeroCamiseta + ", posición " + posicion;
    }
}
