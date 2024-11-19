package Gestion;

import Archivos.FormatoIncorrectoException;
import Archivos.GestionJSON;
import Restaurante.Plato;
import Restaurante.Reserva;
import Restaurante.Ticket;
import Restaurante.TipoPago;
import Users.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.time.LocalDateTime;
import java.util.*;

/**
 * La clase Gestion.GestionTickets gestiona un conjunto de tickets de la aplicación.
 *
 * - Tiene como campos una List de Tickets (`ticketList`), un objeto `scanner` para la lectura de entradas del usuario
 *   y un objeto `GestionDeCliente` para manejar información de los clientes relacionados con los tickets.
 * - Tiene un constructor vacío.
 * - Interactúa con un archivo JSON para guardar y cargar datos.
 * - Incluye métodos para agregar, modificar, buscar y listar tickets.
 * - Permite modificar atributos de un ticket como la reserva, el empleado, la hora de emisión, los platos y el tipo de pago.
 *
 * @author Melina
 * @since 2024
 * @version 1
 */



public class GestionTickets{

    private List<Ticket> ticketList;
    private Scanner scanner;
    private final GestionDeCliente gestionDeCliente;

    public GestionTickets() {
        this.ticketList = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        GestionJSON.crearArchivoJSON("tickets.json");
        this.gestionDeCliente = new GestionDeCliente();
    }

    public void ingresarUsuario(){
        System.out.println();
        Ticket aux = new Ticket();
        aux=aux.ingresarTicket();
        agregarYguardar(aux);
        System.out.println("\nTicket cargado con exito!");
    }

    public List<Ticket> cargarArrayConArchivo(){
        JSONTokener aux = GestionJSON.leer("tickets.json");

        try {
            JSONArray arreglo = new JSONArray(aux);
            for(int i = 0; i < arreglo.length(); i++){
                JSONObject aux1 = arreglo.getJSONObject(i);
                Ticket ticket = new Ticket();
                ticket = ticket.jsonToTicket(aux1);
                ticketList.add(ticket);
            }
        } catch (JSONException e){
            System.out.println("Ocurrio un error al convertir JSONObject a Ticket.");
        }

        return ticketList;
    }

    public void agregarYguardar (Ticket ticket){
        cargarArrayConArchivo();
        ticketList.add(ticket);
        cargarArchivoConArreglo(ticketList);
    }

    public void cargarArchivoConArreglo(List<Ticket> ticketSet){
        JSONArray arreglo = new JSONArray();
        try {

            for (Ticket t : ticketSet){
                try {
                    JSONObject json = t.toJson(t);
                    arreglo.put(json);
                }
                catch (FormatoIncorrectoException e){
                    System.out.println(e.getMessage());
                }
            }
            GestionJSON.agregarElemento("tickets.json", arreglo);
        } catch (JSONException e){
            System.out.println("Hubo un problema al cargar el archivo con array.");
        }
    }

    public void mostrarDatosUsuario(Ticket ticket) {
        if (ticketList.isEmpty()){
            cargarArrayConArchivo();
        }

        for (Ticket t : ticketList){
            if (t.getId() == ticket.getId()){
                t.mostrarTicket(t);
            }
        }
    }

    public Ticket modificarUsuario (Ticket t) {

        ticketList = cargarArrayConArchivo();
        boolean salir = false;
        Ticket ticketEliminar = null;


        for (Ticket ticket : ticketList){
            if (t.getId()==ticket.getId()){
                ticketEliminar=ticket;
                t=ticket;
                break;
            }
        }

        if (ticketEliminar!=null){
            ticketList.remove(ticketEliminar);
            cargarArchivoConArreglo(ticketList);
            while (!salir) {
                System.out.println("\n Que desea modificar?");
                System.out.println("1. Reserva.");
                System.out.println("2. Empleado.");
                System.out.println("3. Hora emision.");
                System.out.println("4. Platos.");
                System.out.println("5. Tipo pago.");
                System.out.println("6. Salir.");
                int op = scanner.nextInt();
                scanner.nextLine();
                switch (op) {
                    case 1:

                        GestionReserva gestionReserva = new GestionReserva();
                        Reserva res = null;
                        int cliente = 0;
                        boolean resValida = false;
                        while (!resValida) {
                            System.out.println("Por favor, ingresa ID de la reserva:");
                            int id = scanner.nextInt();
                            res = gestionReserva.encontrarUsuario(id);
                            //cliente=res.getCliente();
                            gestionReserva.darDeBajaUsuario(res);
                            if(res != null && cliente!=0){
                                t.setReserva(res);
                                t.setCliente(cliente);
                                resValida = true;
                            }else {
                                System.out.println("No se encontro la reserva, intentelo nuevamente.");
                            }
                        }

                        break;
                    case 2:

                        Empleado empleado = null;
                        GestionEmpleados gestionEmpleados = new GestionEmpleados();
                        boolean empleadoValido = false;
                        while (!empleadoValido){
                            System.out.println("Ingrese el DNI del empleado: ");
                            String dni = scanner.nextLine();
                            empleado = gestionEmpleados.encontrarUsuario(dni);
                            if (empleado != null){
                                empleadoValido=true;
                                t.setEmpleado(empleado);
                            }else {
                                System.out.println("No se encontro el empleado.");
                            }
                        }

                        break;
                    case 3:

                        LocalDateTime horaNow = LocalDateTime.now();
                        t.setHoraEmision(horaNow);

                        break;
                    case 4:

                        List<Plato>platos = new ArrayList<>();
                        double precio = 0;
                        boolean salir2 = false;
                        while (!salir2) {
                            System.out.println("1. Agregar plato.");
                            System.out.println("2. Salir.");
                            int opcion = scanner.nextInt();
                            scanner.nextLine();
                            if (opcion == 1) {
                                MenuRestaurante menuRestaurante = new MenuRestaurante();
                                menuRestaurante.listarPlatosTicket();
                                System.out.println("Ingrese el ID del plato:");
                                int id = scanner.nextInt();
                                scanner.nextLine();
                                Plato plato = menuRestaurante.encontrarUsuario(id);
                                platos.add(plato);
                            } else if (opcion == 2) {
                                System.out.println("Platos cargados con exito.");
                                t.setPlatos(platos);
                                for (Plato p : platos) {
                                    precio += p.getPrecio();
                                }
                                if (precio != 0) {
                                    salir2 = true;
                                    t.setPrecio(precio);
                                } else {
                                    System.out.println("Ocurrio un problema, intentelo de nuevo.");
                                }
                            }
                        }
                        break;
                    case 5:

                        TipoPago tipoPago = null;
                        boolean tipoPagoValido = false;
                        while (!tipoPagoValido){
                            System.out.println("Seleccione el tipo de pago: ");
                            System.out.println("1. Efectivo.");
                            System.out.println("2. Debito.");
                            System.out.println("3. Credito");
                            int opTipoPago = scanner.nextInt();
                            scanner.nextLine();
                            if (opTipoPago==1){
                                tipoPago = TipoPago.EFECTIVO;
                                t.setTipoPago(tipoPago);
                                tipoPagoValido = true;
                            } else if (opTipoPago==2) {
                                tipoPago = TipoPago.DEBITO;
                                t.setTipoPago(tipoPago);
                                tipoPagoValido=true;
                            }else if (opTipoPago==3){
                                tipoPago = TipoPago.CREDITO;
                                t.setTipoPago(tipoPago);
                                tipoPagoValido=true;
                            }else {
                                System.out.println("Opcion invalida.");
                            }
                        }
                        break;
                    case 6:
                        System.out.println("Saliendo del menu de modificacion de usuario...");
                        salir = true;
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                        break;
                }
            }
            ticketList.add(t);
            cargarArchivoConArreglo(ticketList);
            System.out.println("¡Cambios guardados con exito!");
            return t;
        }

        System.out.println("No se encontro el ticket");
        return null;

    }

    public void mostrarColeccion() {
        if (ticketList.isEmpty()){
            cargarArrayConArchivo();
        }

        for (Ticket t : ticketList){
            System.out.println();
            t.mostrarTicket(t);
            System.out.println();
        }
    }

    public Ticket encontrarUsuario(String dni) {
        if (ticketList.isEmpty()){
            cargarArrayConArchivo();
        }

        for (Ticket t : ticketList){
            if (gestionDeCliente.encontrarUsuario(t.getCliente()).getDni().equals(dni)){
                return t;
            }
        }
        return null;
    }

    public Ticket encontrarUsuario(int id) {
        if (ticketList.isEmpty()){
            cargarArrayConArchivo();
        }

        for (Ticket t : ticketList){
            if (t.getId() == id){
                return t;
            }
        }
        return null;
    }

    public void listarUsuarios(String dni) {
        if (ticketList.isEmpty()) {
            cargarArrayConArchivo();
        }

        boolean encontrado = false;

        for (Ticket ticket : ticketList){
            if (gestionDeCliente.encontrarUsuario(ticket.getCliente()).getDni().equals(dni)){
                encontrado=true;
                ticket.mostrarTicket(ticket);
            }
        }

        if (!encontrado){
            System.out.println("No se encontraron tickets del cliente.");
        }
    }

    public void listarUsuarios(boolean aux) {
        if (ticketList.isEmpty()) {
            cargarArrayConArchivo();
        }
        for (Ticket ticket : ticketList){
            if (ticket.getReserva().getEstado() == aux){
                ticket.mostrarTicket(ticket);
            }
        }
    }
}
