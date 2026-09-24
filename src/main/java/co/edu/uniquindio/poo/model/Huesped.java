package co.edu.uniquindio.poo.model;

import java.util.ArrayList;
import java.util.List;

public class Huesped {

    private String documentoIdentidad;
    private String nombreCompleto;
    private int edad;
    private String telefono;
    private String ciudadProcedencia;
    private List<Reserva> reservas;

    public Huesped(String documentoIdentidad, String nombreCompleto, int edad,
                   String telefono, String ciudadProcedencia) {
        this.documentoIdentidad = documentoIdentidad;
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
        this.telefono = telefono;
        this.ciudadProcedencia = ciudadProcedencia;
        this.reservas = new ArrayList<>();
    }

    public void agregarReserva(Reserva reserva) {
        if (reserva != null && !reservas.contains(reserva)) {
            reservas.add(reserva);
        }
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public int getEdad() {
        return edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCiudadProcedencia() {
        return ciudadProcedencia;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    @Override
    public String toString() {
        return "Documento: " + documentoIdentidad
                + "\nNombre: " + nombreCompleto
                + "\nEdad: " + edad
                + "\nTelefono: " + telefono
                + "\nCiudad: " + ciudadProcedencia;
    }
}
