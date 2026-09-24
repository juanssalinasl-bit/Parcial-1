/**
 * Este es el codigo del primer parcialde P1
 * @version 1.0
 * @author Juan Sebastian Salinas Luna
 * @fecha : 23/09/26
 */
package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.*;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) {

        Hotel hotel = new Hotel(
                "StayPlus",
                "900123456-7",
                "Armenia",
                "6060000000",
                20,
                30
        );

        inicializarDatosPrueba(hotel);

        JOptionPane.showMessageDialog(
                null,
                "Bienvenidos al sistema del Hotel StayPlus"
        );

        int opcion;

        do {
            opcion = leerOpcionMenu();

            switch (opcion) {

                case 1:
                    registrar(hotel);
                    break;

                case 2:
                    mostrarLista(hotel);
                    break;

                case 3:
                    consultarHuesped(hotel);
                    break;

                case 4:
                    JOptionPane.showMessageDialog(
                            null,
                            hotel.consultarDisponibilidad()
                    );
                    break;

                case 5:
                    JOptionPane.showMessageDialog(
                            null,
                            hotel.mostrarMatriz()
                    );
                    break;

                case 6:
                    cambiarOcupacion(hotel);
                    break;

                case 7:
                    JOptionPane.showMessageDialog(
                            null,
                            hotel.reservasEspeciales()
                    );
                    break;

                case 8:
                    consultarIngresos(hotel);
                    break;

                case 0:
                    JOptionPane.showMessageDialog(
                            null,
                            "Muchas gracias por utilizar StayPlus."
                    );
                    break;
            }

        } while (opcion != 0);
    }

    // ---------------- REGISTRO ----------------

    private static void registrar(Hotel hotel) {

        String tipo = seleccionarTipo("¿Qué desea registrar?");

        if (tipo == null) {
            return;
        }

        switch (tipo) {
            case "Huésped":
                crearHuesped(hotel);
                break;
            case "Habitación":
                crearHabitacion(hotel);
                break;
            case "Reserva":
                crearReserva(hotel);
                break;
        }
    }

    // ---------------- MOSTRAR LISTAS ----------------

    private static void mostrarLista(Hotel hotel) {

        String tipo = seleccionarTipo("¿Qué desea mostrar?");

        if (tipo == null) {
            return;
        }

        switch (tipo) {
            case "Huésped":
                JOptionPane.showMessageDialog(null, hotel.listarHuespedes());
                break;
            case "Habitación":
                JOptionPane.showMessageDialog(null, hotel.listarHabitaciones());
                break;
            case "Reserva":
                JOptionPane.showMessageDialog(null, hotel.listarReservas());
                break;
        }
    }

    private static String seleccionarTipo(String mensaje) {

        Object opcion = JOptionPane.showInputDialog(
                null,
                mensaje,
                "Seleccionar tipo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Huésped", "Habitación", "Reserva"},
                "Huésped"
        );

        if (opcion == null) {
            return null;
        }

        return opcion.toString();
    }

    // ---------------- HUÉSPED ----------------

    private static void crearHuesped(Hotel hotel) {

        String documento = leerTexto("Ingrese el documento:");
        String nombre = leerTexto("Ingrese el nombre completo:");
        int edad = leerEnteroPositivo("Ingrese la edad:");
        String telefono = leerTexto("Ingrese el teléfono:");
        String ciudad = leerTexto("Ingrese la ciudad de procedencia:");

        if (hotel.buscarHuesped(documento) != null) {
            mostrarError("Ya existe un huésped con ese documento.");
            return;
        }

        if (hotel.buscarHuespedTelefono(telefono) != null) {
            mostrarError("Ya existe un huésped con ese teléfono.");
            return;
        }

        Huesped huesped = new Huesped(
                documento,
                nombre,
                edad,
                telefono,
                ciudad
        );

        if (hotel.agregarHuesped(huesped)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Huésped registrado correctamente."
            );
        } else {
            mostrarError("No se pudo registrar el huésped.");
        }
    }

    // ---------------- HABITACIÓN ----------------

    private static void crearHabitacion(Hotel hotel) {

        int numero = leerEnteroPositivo(
                "Ingrese el número de habitación:"
        );

        if (hotel.buscarHabitacion(numero) != null) {
            mostrarError("Ya existe una habitación con ese número.");
            return;
        }

        TipoHabitacion tipo = seleccionarTipo();

        int piso = leerEnteroPositivo("Ingrese el piso:");
        int capacidad = leerEnteroPositivo("Ingrese la capacidad máxima:");
        double precio = leerDoublePositivo("Ingrese el precio por noche:");

        EstadoHabitacion estado = seleccionarEstadoHabitacion();

        Habitacion habitacion = new Habitacion(
                numero,
                tipo,
                piso,
                capacidad,
                precio,
                estado
        );

        if (hotel.agregarHabitacion(habitacion)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Habitación registrada correctamente."
            );
        } else {
            mostrarError(
                    "No se pudo registrar la habitación. "
                            + "Es posible que no haya espacio."
            );
        }
    }

    // ---------------- RESERVA ----------------

    private static void crearReserva(Hotel hotel) {

        int codigo = leerEnteroPositivo(
                "Ingrese el código de reserva:"
        );

        if (hotel.buscarReserva(codigo) != null) {
            mostrarError("Ya existe una reserva con ese código.");
            return;
        }

        LocalDate fecha = leerFecha();
        int noches = leerEnteroPositivo("Ingrese el número de noches:");
        int cantidadHuespedes = leerEnteroPositivo(
                "Ingrese la cantidad de huéspedes:"
        );

        String documento = leerTexto(
                "Ingrese el documento del huésped:"
        );

        Huesped huesped = hotel.buscarHuesped(documento);

        if (huesped == null) {
            mostrarError(
                    "El huésped no existe. Regístrelo primero."
            );
            return;
        }

        EstadoReserva estado = seleccionarEstadoReserva();
        MetodoPago metodo = seleccionarMetodoPago();

        Reserva reserva = new Reserva(
                codigo,
                fecha,
                noches,
                cantidadHuespedes,
                estado,
                metodo,
                huesped
        );

        int cantidadHabitaciones = leerEnteroPositivo(
                "¿Cuántas habitaciones desea agregar?"
        );

        for (int i = 0; i < cantidadHabitaciones; i++) {

            while (true) {

                int numero = leerEnteroPositivo(
                        "Ingrese el número de la habitación "
                                + (i + 1) + ":"
                );

                Habitacion habitacion =
                        hotel.buscarHabitacion(numero);

                if (habitacion == null) {
                    mostrarError("La habitación no existe.");
                    continue;
                }

                if (habitacion.getEstado()
                        != EstadoHabitacion.DISPONIBLE) {

                    mostrarError(
                            "La habitación no está disponible."
                    );
                    continue;
                }

                if (reserva.getHabitaciones().contains(habitacion)) {
                    mostrarError(
                            "Esa habitación ya fue agregada a la reserva."
                    );
                    continue;
                }

                reserva.agregarHabitacion(habitacion);
                break;
            }
        }

        int capacidadTotal = 0;

        for (Habitacion habitacion : reserva.getHabitaciones()) {
            capacidadTotal += habitacion.getCapacidadMaxima();
        }

        if (capacidadTotal < cantidadHuespedes) {
            mostrarError(
                    "La capacidad de las habitaciones seleccionadas "
                            + "no alcanza para todos los huéspedes."
            );
            return;
        }

        if (hotel.agregarReserva(reserva)) {

            String mensaje =
                    "Reserva registrada correctamente."
                            + "\n\nValor total: $"
                            + reserva.getValorTotal();

            if (estado == EstadoReserva.CONFIRMADA) {
                mensaje +=
                        "\n\nLa habitación quedó en estado RESERVADA."
                        + "\nLa matriz semanal también fue actualizada.";
            }

            JOptionPane.showMessageDialog(null, mensaje);

        } else {
            mostrarError(
                    "No se pudo registrar la reserva."
            );
        }
    }

    // ---------------- CONSULTA HUÉSPED ----------------

    private static void consultarHuesped(Hotel hotel) {

        String telefono = leerTexto(
                "Ingrese el teléfono del huésped:"
        );

        JOptionPane.showMessageDialog(
                null,
                hotel.consultarHuesped(telefono)
        );
    }

    // ---------------- MATRIZ ----------------

    private static void cambiarOcupacion(Hotel hotel) {

        int numero = leerEnteroPositivo(
                "Ingrese el número de habitación:"
        );

        if (hotel.buscarHabitacion(numero) == null) {
            mostrarError("La habitación no existe.");
            return;
        }

        DiaSemana dia = seleccionarDia();

        int opcion = leerOpcion(
                "¿Qué desea colocar?\n"
                        + "1. Ocupada\n"
                        + "2. Disponible",
                1,
                2
        );

        hotel.cambiarOcupacion(
                numero,
                dia,
                opcion == 1
        );

        JOptionPane.showMessageDialog(
                null,
                "Matriz actualizada correctamente."
        );
    }

    // ---------------- INGRESOS ----------------

    private static void consultarIngresos(Hotel hotel) {

        LocalDate fecha = leerFecha();

        double ingresos = hotel.calcularIngresos(fecha);

        JOptionPane.showMessageDialog(
                null,
                "Ingresos generados el " + fecha
                        + ": $" + ingresos
        );
    }

    // ---------------- SELECCIONES ----------------

    private static TipoHabitacion seleccionarTipo() {

        Object opcion = JOptionPane.showInputDialog(
                null,
                "Seleccione el tipo de habitación:",
                "Tipo de habitación",
                JOptionPane.QUESTION_MESSAGE,
                null,
                TipoHabitacion.values(),
                TipoHabitacion.INDIVIDUAL
        );

        if (opcion == null) {
            System.exit(0);
        }

        return (TipoHabitacion) opcion;
    }

    private static EstadoHabitacion seleccionarEstadoHabitacion() {

        Object opcion = JOptionPane.showInputDialog(
                null,
                "Seleccione el estado:",
                "Estado de habitación",
                JOptionPane.QUESTION_MESSAGE,
                null,
                EstadoHabitacion.values(),
                EstadoHabitacion.DISPONIBLE
        );

        if (opcion == null) {
            System.exit(0);
        }

        return (EstadoHabitacion) opcion;
    }

    private static EstadoReserva seleccionarEstadoReserva() {

        Object opcion = JOptionPane.showInputDialog(
                null,
                "Seleccione el estado:",
                "Estado de reserva",
                JOptionPane.QUESTION_MESSAGE,
                null,
                EstadoReserva.values(),
                EstadoReserva.PENDIENTE
        );

        if (opcion == null) {
            System.exit(0);
        }

        return (EstadoReserva) opcion;
    }

    private static MetodoPago seleccionarMetodoPago() {

        Object opcion = JOptionPane.showInputDialog(
                null,
                "Seleccione el método de pago:",
                "Método de pago",
                JOptionPane.QUESTION_MESSAGE,
                null,
                MetodoPago.values(),
                MetodoPago.EFECTIVO
        );

        if (opcion == null) {
            System.exit(0);
        }

        return (MetodoPago) opcion;
    }

    private static DiaSemana seleccionarDia() {

        Object opcion = JOptionPane.showInputDialog(
                null,
                "Seleccione el día:",
                "Día de la semana",
                JOptionPane.QUESTION_MESSAGE,
                null,
                DiaSemana.values(),
                DiaSemana.LUNES
        );

        if (opcion == null) {
            System.exit(0);
        }

        return (DiaSemana) opcion;
    }

    // ---------------- DATOS DE PRUEBA ----------------

    private static void inicializarDatosPrueba(Hotel hotel) {

        Huesped juan = new Huesped(
                "1001", "Juan Perez", 25, "3001111111", "Armenia");

        Huesped maria = new Huesped(
                "1002", "Maria Gomez", 30, "3002222222", "Pereira");

        Huesped carlos = new Huesped(
                "1003", "Carlos Lopez", 40, "3003333333", "Manizales");

        Huesped ana = new Huesped(
                "1004", "Ana Torres", 28, "3004444444", "Cali");

        hotel.agregarHuesped(juan);
        hotel.agregarHuesped(maria);
        hotel.agregarHuesped(carlos);
        hotel.agregarHuesped(ana);

        Habitacion h101 = new Habitacion(
                101, TipoHabitacion.INDIVIDUAL, 1, 1, 80000,
                EstadoHabitacion.DISPONIBLE);

        Habitacion h102 = new Habitacion(
                102, TipoHabitacion.DOBLE, 1, 2, 120000,
                EstadoHabitacion.DISPONIBLE);

        Habitacion h201 = new Habitacion(
                201, TipoHabitacion.DOBLE, 2, 2, 150000,
                EstadoHabitacion.DISPONIBLE);

        Habitacion h202 = new Habitacion(
                202, TipoHabitacion.SUITE, 2, 4, 250000,
                EstadoHabitacion.MANTENIMIENTO);

        Habitacion h301 = new Habitacion(
                301, TipoHabitacion.SUITE, 3, 4, 300000,
                EstadoHabitacion.OCUPADA);

        Habitacion h302 = new Habitacion(
                302, TipoHabitacion.DOBLE, 3, 2, 180000,
                EstadoHabitacion.DISPONIBLE);

        hotel.agregarHabitacion(h101);
        hotel.agregarHabitacion(h102);
        hotel.agregarHabitacion(h201);
        hotel.agregarHabitacion(h202);
        hotel.agregarHabitacion(h301);
        hotel.agregarHabitacion(h302);

        Reserva r1221 = new Reserva(
                1221, LocalDate.of(2026, 9, 21), 2, 2,
                EstadoReserva.CONFIRMADA, MetodoPago.TARJETA, juan);
        r1221.agregarHabitacion(h101);

        Reserva r1234 = new Reserva(
                1234, LocalDate.of(2026, 9, 23), 2, 2,
                EstadoReserva.PENDIENTE, MetodoPago.EFECTIVO, maria);
        r1234.agregarHabitacion(h102);

        Reserva r1331 = new Reserva(
                1331, LocalDate.of(2026, 9, 24), 1, 2,
                EstadoReserva.CONFIRMADA, MetodoPago.TRANSFERENCIA_BANCARIA, carlos);
        r1331.agregarHabitacion(h201);

        hotel.agregarReserva(r1221);
        hotel.agregarReserva(r1234);
        hotel.agregarReserva(r1331);
    }

    // ---------------- VALIDACIONES ----------------

    private static int leerOpcionMenu() {

        return leerOpcion(
                "----- MENU PRINCIPAL -----\n"
                        + "1. Registrar\n"
                        + "2. Mostrar lista\n"
                        + "3. Consultar huésped por teléfono\n"
                        + "4. Consultar disponibilidad\n"
                        + "5. Mostrar matriz semanal\n"
                        + "6. Actualizar matriz manualmente\n"
                        + "7. Mostrar reservas capicúa\n"
                        + "8. Calcular ingresos por fecha\n"
                        + "0. Salir",
                0,
                8
        );
    }

    private static int leerOpcion(
            String mensaje,
            int minimo,
            int maximo) {

        while (true) {

            String dato = JOptionPane.showInputDialog(
                    null,
                    mensaje
            );

            if (dato == null) {
                System.exit(0);
            }

            try {

                int opcion = Integer.parseInt(dato.trim());

                if (opcion >= minimo && opcion <= maximo) {
                    return opcion;
                }

            } catch (NumberFormatException e) {
                // Se muestra el mensaje de error abajo.
            }

            JOptionPane.showMessageDialog(
                    null,
                    "Opción inválida. Ingrese un número entre "
                            + minimo + " y " + maximo + "."
            );
        }
    }

    private static String leerTexto(String mensaje) {

        while (true) {

            String dato = JOptionPane.showInputDialog(
                    null,
                    mensaje
            );

            if (dato == null) {
                System.exit(0);
            }

            dato = dato.trim();

            if (!dato.isEmpty()) {
                return dato;
            }

            mostrarError("El campo no puede estar vacío.");
        }
    }

    private static int leerEntero(String mensaje) {

        while (true) {

            String dato = leerTexto(mensaje);

            try {
                return Integer.parseInt(dato);
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número entero.");
            }
        }
    }

    private static int leerEnteroPositivo(String mensaje) {

        while (true) {

            int numero = leerEntero(mensaje);

            if (numero > 0) {
                return numero;
            }

            mostrarError("El número debe ser mayor que cero.");
        }
    }

    private static double leerDoublePositivo(String mensaje) {

        while (true) {

            String dato = leerTexto(mensaje);

            try {

                double numero = Double.parseDouble(
                        dato.replace(",", ".")
                );

                if (numero > 0) {
                    return numero;
                }

            } catch (NumberFormatException e) {
                // Se muestra el mensaje de error.
            }

            mostrarError(
                    "Ingrese un número válido mayor que cero."
            );
        }
    }

    private static LocalDate leerFecha() {

        while (true) {

            String dato = leerTexto(
                    "Ingrese la fecha (AAAA-MM-DD):"
            );

            try {
                return LocalDate.parse(dato);
            } catch (DateTimeParseException e) {
                mostrarError(
                        "Fecha inválida. Ejemplo: 2026-09-24"
                );
            }
        }
    }

    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
