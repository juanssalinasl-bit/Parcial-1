package co.edu.uniquindio.poo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Hotel {

    private String nombreComercial;
    private String nit;
    private String direccion;
    private String telefono;

    private List<Huesped> huespedes;
    private Habitacion[] habitaciones;
    private Reserva[] reservas;

    // Filas = habitaciones, columnas = lunes a domingo.
    // O = ocupada, D = disponible.
    private String[][] matrizOcupacion;

    public Hotel(String nombreComercial, String nit, String direccion,
                 String telefono, int cantidadHabitaciones,
                 int cantidadReservas) {

        this.nombreComercial = nombreComercial;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;

        huespedes = new ArrayList<>();
        habitaciones = new Habitacion[cantidadHabitaciones];
        reservas = new Reserva[cantidadReservas];

        matrizOcupacion = new String[cantidadHabitaciones][7];

        for (int i = 0; i < matrizOcupacion.length; i++) {
            for (int j = 0; j < 7; j++) {
                matrizOcupacion[i][j] = "D";
            }
        }
    }

    // ---------------- HUÉSPEDES ----------------

    public boolean agregarHuesped(Huesped huesped) {
        if (!validarHuesped(huesped)) {
            return false;
        }

        if (buscarHuesped(huesped.getDocumentoIdentidad()) != null) {
            return false;
        }

        if (buscarHuespedTelefono(huesped.getTelefono()) != null) {
            return false;
        }

        huespedes.add(huesped);
        return true;
    }

    private boolean validarHuesped(Huesped huesped) {
        return huesped != null
                && huesped.getDocumentoIdentidad() != null
                && !huesped.getDocumentoIdentidad().trim().isEmpty()
                && huesped.getNombreCompleto() != null
                && !huesped.getNombreCompleto().trim().isEmpty()
                && huesped.getEdad() > 0
                && huesped.getTelefono() != null
                && !huesped.getTelefono().trim().isEmpty()
                && huesped.getCiudadProcedencia() != null
                && !huesped.getCiudadProcedencia().trim().isEmpty();
    }

    public Huesped buscarHuesped(String documento) {
        for (Huesped huesped : huespedes) {
            if (huesped.getDocumentoIdentidad().equals(documento)) {
                return huesped;
            }
        }
        return null;
    }

    public Huesped buscarHuespedTelefono(String telefono) {
        for (Huesped huesped : huespedes) {
            if (huesped.getTelefono().equals(telefono)) {
                return huesped;
            }
        }
        return null;
    }

    // ---------------- HABITACIONES ----------------

    public boolean agregarHabitacion(Habitacion habitacion) {
        if (!validarHabitacion(habitacion)) {
            return false;
        }

        if (buscarHabitacion(habitacion.getNumero()) != null) {
            return false;
        }

        for (int i = 0; i < habitaciones.length; i++) {
            if (habitaciones[i] == null) {
                habitaciones[i] = habitacion;
                return true;
            }
        }

        return false;
    }

    private boolean validarHabitacion(Habitacion habitacion) {
        return habitacion != null
                && habitacion.getNumero() > 0
                && habitacion.getTipoHabitacion() != null
                && habitacion.getPiso() > 0
                && habitacion.getCapacidadMaxima() > 0
                && habitacion.getPrecioPorNoche() > 0
                && habitacion.getEstado() != null;
    }

    public Habitacion buscarHabitacion(int numero) {
        for (Habitacion habitacion : habitaciones) {
            if (habitacion != null && habitacion.getNumero() == numero) {
                return habitacion;
            }
        }
        return null;
    }

    // ---------------- RESERVAS ----------------

    public boolean agregarReserva(Reserva reserva) {
        if (!validarReserva(reserva)) {
            return false;
        }

        if (buscarReserva(reserva.getCodigoReserva()) != null) {
            return false;
        }

        if (reserva.getHabitaciones().isEmpty()) {
            return false;
        }

        // Una habitación no puede estar ocupada, reservada o en mantenimiento.
        for (Habitacion habitacion : reserva.getHabitaciones()) {
            if (habitacion == null
                    || habitacion.getEstado() != EstadoHabitacion.DISPONIBLE) {
                return false;
            }
        }

        for (int i = 0; i < reservas.length; i++) {
            if (reservas[i] == null) {
                reservas[i] = reserva;
                reserva.getHuesped().agregarReserva(reserva);

                if (reserva.getEstadoReserva() == EstadoReserva.CONFIRMADA) {
                    for (Habitacion habitacion : reserva.getHabitaciones()) {
                        habitacion.setEstado(EstadoHabitacion.RESERVADA);
                    }

                    actualizarMatrizConReserva(reserva);
                }

                return true;
            }
        }

        return false;
    }

    private boolean validarReserva(Reserva reserva) {
        return reserva != null
                && reserva.getCodigoReserva() > 0
                && reserva.getFechaReserva() != null
                && reserva.getNumeroNoches() > 0
                && reserva.getCantidadHuespedes() > 0
                && reserva.getEstadoReserva() != null
                && reserva.getMetodoPago() != null
                && reserva.getHuesped() != null
                && !reserva.getHabitaciones().isEmpty();
    }

    public Reserva buscarReserva(int codigo) {
        for (Reserva reserva : reservas) {
            if (reserva != null && reserva.getCodigoReserva() == codigo) {
                return reserva;
            }
        }
        return null;
    }

    // ---------------- VALIDAR Y MOSTRAR LISTAS ----------------

    public String listarHuespedes() {
        if (huespedes.isEmpty()) {
            return "No hay huéspedes registrados.";
        }

        String mensaje = "HUESPEDES REGISTRADOS: " + huespedes.size() + "\n\n";

        for (Huesped huesped : huespedes) {
            mensaje += huesped + "\n-------------------------\n";
        }

        return mensaje;
    }

    public String listarHabitaciones() {
        int cantidad = contarHabitaciones();

        if (cantidad == 0) {
            return "No hay habitaciones registradas.";
        }

        String mensaje = "HABITACIONES REGISTRADAS: " + cantidad
                + " de " + habitaciones.length + "\n\n";

        for (Habitacion habitacion : habitaciones) {
            if (habitacion != null) {
                mensaje += habitacion + "\n-------------------------\n";
            }
        }

        mensaje += "Espacios disponibles para habitaciones: "
                + (habitaciones.length - cantidad);

        return mensaje;
    }

    public String listarReservas() {
        int cantidad = contarReservas();

        if (cantidad == 0) {
            return "No hay reservas registradas.";
        }

        String mensaje = "RESERVAS REGISTRADAS: " + cantidad
                + " de " + reservas.length + "\n\n";

        for (Reserva reserva : reservas) {
            if (reserva != null) {
                mensaje += reserva + "\n-------------------------\n";
            }
        }

        mensaje += "Espacios disponibles para reservas: "
                + (reservas.length - cantidad);

        return mensaje;
    }

    public int contarHabitaciones() {
        int contador = 0;

        for (Habitacion habitacion : habitaciones) {
            if (habitacion != null) {
                contador++;
            }
        }

        return contador;
    }

    public int contarReservas() {
        int contador = 0;

        for (Reserva reserva : reservas) {
            if (reserva != null) {
                contador++;
            }
        }

        return contador;
    }

    public int contarHuespedes() {
        return huespedes.size();
    }

    // ---------------- CONSULTAR HUÉSPED ----------------

    public String consultarHuesped(String telefono) {
        Huesped huesped = buscarHuespedTelefono(telefono);

        if (huesped == null) {
            return "No existe un huésped con ese teléfono.";
        }

        String mensaje = "";
        mensaje += "Nombre: " + huesped.getNombreCompleto() + "\n";
        mensaje += "Documento: " + huesped.getDocumentoIdentidad() + "\n";
        mensaje += "Edad: " + huesped.getEdad() + "\n";
        mensaje += "Telefono: " + huesped.getTelefono() + "\n";
        mensaje += "Ciudad: " + huesped.getCiudadProcedencia() + "\n";
        mensaje += "\nReservas realizadas:\n";

        if (huesped.getReservas().isEmpty()) {
            mensaje += "No tiene reservas.";
        } else {
            for (Reserva reserva : huesped.getReservas()) {
                mensaje += "\n" + reserva + "\n";
            }
        }

        return mensaje;
    }

    // ---------------- DISPONIBILIDAD ----------------

    public String consultarDisponibilidad() {
        int disponibles = 0;
        int ocupadas = 0;
        int mantenimiento = 0;
        int reservadas = 0;

        Habitacion mayor = null;
        Habitacion menor = null;

        for (Habitacion habitacion : habitaciones) {
            if (habitacion == null) {
                continue;
            }

            if (habitacion.getEstado() == EstadoHabitacion.DISPONIBLE) {
                disponibles++;
            } else if (habitacion.getEstado() == EstadoHabitacion.OCUPADA) {
                ocupadas++;
            } else if (habitacion.getEstado() == EstadoHabitacion.MANTENIMIENTO) {
                mantenimiento++;
            } else if (habitacion.getEstado() == EstadoHabitacion.RESERVADA) {
                reservadas++;
            }

            if (mayor == null
                    || habitacion.getPrecioPorNoche() > mayor.getPrecioPorNoche()) {
                mayor = habitacion;
            }

            if (menor == null
                    || habitacion.getPrecioPorNoche() < menor.getPrecioPorNoche()) {
                menor = habitacion;
            }
        }

        String mensaje = "";
        mensaje += "Disponibles: " + disponibles + "\n";
        mensaje += "Ocupadas: " + ocupadas + "\n";
        mensaje += "Reservadas: " + reservadas + "\n";
        mensaje += "En mantenimiento: " + mantenimiento + "\n";

        if (mayor != null) {
            mensaje += "\nMayor precio por noche: habitación "
                    + mayor.getNumero() + " - $" + mayor.getPrecioPorNoche();
        }

        if (menor != null) {
            mensaje += "\nMenor precio por noche: habitación "
                    + menor.getNumero() + " - $" + menor.getPrecioPorNoche();
        }

        return mensaje;
    }

    // ---------------- MATRIZ ----------------

    public void cambiarOcupacion(int numeroHabitacion,
                                 DiaSemana dia,
                                 boolean ocupada) {

        if (dia == null) {
            return;
        }

        for (int i = 0; i < habitaciones.length; i++) {
            if (habitaciones[i] != null
                    && habitaciones[i].getNumero() == numeroHabitacion) {

                matrizOcupacion[i][dia.ordinal()] = ocupada ? "O" : "D";
                return;
            }
        }
    }

    private void actualizarMatrizConReserva(Reserva reserva) {
        for (Habitacion habitacion : reserva.getHabitaciones()) {

            int fila = buscarIndiceHabitacion(habitacion.getNumero());

            if (fila == -1) {
                continue;
            }

            for (int noche = 0; noche < reserva.getNumeroNoches(); noche++) {

                LocalDate fecha = reserva.getFechaReserva().plusDays(noche);
                int columna = fecha.getDayOfWeek().getValue() - 1;

                matrizOcupacion[fila][columna] = "O";
            }
        }
    }

    private int buscarIndiceHabitacion(int numero) {
        for (int i = 0; i < habitaciones.length; i++) {
            if (habitaciones[i] != null
                    && habitaciones[i].getNumero() == numero) {
                return i;
            }
        }
        return -1;
    }

    public String mostrarMatriz() {
        String mensaje = "";

        mensaje += "                 Lunes  Martes  Miercoles  Jueves  Viernes  Sabado  Domingo\n";

        for (int i = 0; i < habitaciones.length; i++) {
            if (habitaciones[i] != null) {

                mensaje += "Habitacion "
                        + habitaciones[i].getNumero()
                        + "          ";

                for (int j = 0; j < 7; j++) {
                    mensaje += matrizOcupacion[i][j] + "        ";
                }

                mensaje += "\n";
            }
        }

        mensaje += "\nO = Ocupada | D = Disponible";
        mensaje += "\n\n" + resumenOcupacion();

        return mensaje;
    }

    public String resumenOcupacion() {
        int mayor = diaMayorOcupacion();
        int menor = diaMenorOcupacion();

        return "Dia con mayor ocupacion: " + nombreDia(mayor)
                + "\nDia con menor ocupacion: " + nombreDia(menor)
                + "\nTotal de habitaciones ocupadas durante la semana: "
                + totalOcupaciones();
    }

    private int diaMayorOcupacion() {
        int mayor = -1;
        int diaMayor = 0;

        for (int j = 0; j < 7; j++) {
            int contador = contarOcupadasEnColumna(j);

            if (contador > mayor) {
                mayor = contador;
                diaMayor = j;
            }
        }

        return diaMayor;
    }

    private int diaMenorOcupacion() {
        int menor = Integer.MAX_VALUE;
        int diaMenor = 0;

        for (int j = 0; j < 7; j++) {
            int contador = contarOcupadasEnColumna(j);

            if (contador < menor) {
                menor = contador;
                diaMenor = j;
            }
        }

        return diaMenor;
    }

    private int contarOcupadasEnColumna(int columna) {
        int contador = 0;

        for (int i = 0; i < habitaciones.length; i++) {
            if (habitaciones[i] != null
                    && matrizOcupacion[i][columna].equals("O")) {
                contador++;
            }
        }

        return contador;
    }

    private int totalOcupaciones() {
        int total = 0;

        for (int i = 0; i < habitaciones.length; i++) {
            for (int j = 0; j < 7; j++) {
                if (matrizOcupacion[i][j].equals("O")) {
                    total++;
                }
            }
        }

        return total;
    }

    private String nombreDia(int dia) {
        String[] dias = {
                "Lunes", "Martes", "Miercoles", "Jueves",
                "Viernes", "Sabado", "Domingo"
        };

        return dias[dia];
    }

    // ---------------- RESERVAS CAPICÚA ----------------

    public String reservasEspeciales() {
        String mensaje = "";

        for (Reserva reserva : reservas) {
            if (reserva != null && reserva.esCapicua()) {
                mensaje += "Reserva capicua: "
                        + reserva.getCodigoReserva() + "\n";
            }
        }

        if (mensaje.isEmpty()) {
            mensaje = "No existen reservas con codigo capicua.";
        }

        return mensaje;
    }

    // ---------------- INGRESOS ----------------

    public double calcularIngresos(LocalDate fecha) {
        double total = 0;

        for (Reserva reserva : reservas) {
            if (reserva != null
                    && reserva.getFechaReserva().equals(fecha)) {
                total += reserva.getValorTotal();
            }
        }

        return total;
    }

    // Getters sencillos
    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getNit() {
        return nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }
}
