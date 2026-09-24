package co.edu.uniquindio.poo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva {

    private int codigoReserva;
    private LocalDate fechaReserva;
    private int numeroNoches;
    private int cantidadHuespedes;
    private EstadoReserva estadoReserva;
    private MetodoPago metodoPago;
    private double valorTotal;
    private Huesped huesped;
    private List<Habitacion> habitaciones;

    public Reserva(int codigoReserva, LocalDate fechaReserva, int numeroNoches,
                   int cantidadHuespedes, EstadoReserva estadoReserva,
                   MetodoPago metodoPago, Huesped huesped) {
        this.codigoReserva = codigoReserva;
        this.fechaReserva = fechaReserva;
        this.numeroNoches = numeroNoches;
        this.cantidadHuespedes = cantidadHuespedes;
        this.estadoReserva = estadoReserva;
        this.metodoPago = metodoPago;
        this.huesped = huesped;
        this.habitaciones = new ArrayList<>();
        this.valorTotal = 0;
    }

    public boolean agregarHabitacion(Habitacion habitacion) {
        if (habitacion == null || habitaciones.contains(habitacion)) {
            return false;
        }

        habitaciones.add(habitacion);
        calcularValorTotal();
        return true;
    }

    public double calcularValorTotal() {
        double totalPorNoche = 0;

        for (Habitacion habitacion : habitaciones) {
            totalPorNoche += habitacion.getPrecioPorNoche();
        }

        valorTotal = totalPorNoche * numeroNoches;
        return valorTotal;
    }

    public boolean esCapicua() {
        String numero = String.valueOf(codigoReserva);
        String invertido = "";

        for (int i = numero.length() - 1; i >= 0; i--) {
            invertido += numero.charAt(i);
        }

        return numero.equals(invertido);
    }

    public int getCodigoReserva() {
        return codigoReserva;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public int getNumeroNoches() {
        return numeroNoches;
    }

    public int getCantidadHuespedes() {
        return cantidadHuespedes;
    }

    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigoReserva
                + "\nFecha: " + fechaReserva
                + "\nNoches: " + numeroNoches
                + "\nHuespedes: " + cantidadHuespedes
                + "\nEstado: " + estadoReserva
                + "\nMetodo de pago: " + metodoPago
                + "\nHabitaciones: " + habitaciones.size()
                + "\nValor total: $" + valorTotal;
    }
}
