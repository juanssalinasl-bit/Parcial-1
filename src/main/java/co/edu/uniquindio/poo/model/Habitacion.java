package co.edu.uniquindio.poo.model;

public class Habitacion {

    private int numero;
    private TipoHabitacion tipoHabitacion;
    private int piso;
    private int capacidadMaxima;
    private double precioPorNoche;
    private EstadoHabitacion estado;

    public Habitacion(int numero, TipoHabitacion tipoHabitacion, int piso,
                      int capacidadMaxima, double precioPorNoche,
                      EstadoHabitacion estado) {
        this.numero = numero;
        this.tipoHabitacion = tipoHabitacion;
        this.piso = piso;
        this.capacidadMaxima = capacidadMaxima;
        this.precioPorNoche = precioPorNoche;
        this.estado = estado;
    }

    public int getNumero() {
        return numero;
    }

    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    public int getPiso() {
        return piso;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public double getPrecioPorNoche() {
        return precioPorNoche;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Habitacion " + numero
                + " | Tipo: " + tipoHabitacion
                + " | Piso: " + piso
                + " | Capacidad: " + capacidadMaxima
                + " | Precio: $" + precioPorNoche
                + " | Estado: " + estado;
    }
}
