package Gestion;

import Archivos.FormatoIncorrectoException;
import Archivos.GestionJSON;
import Restaurante.MesaYaReservadaException;
import Restaurante.Plato;
import Restaurante.Reserva;
import Users.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * La clase Gestion.GestionReserva maneja las reservas realizadas por los clientes.
 *
 * -Tiene como campos una List de reservas (`reservasPorCliente`), un objeto `registroUser` para gestionar el registro de nuevos administradores,
 * un `scanner` para la lectura de entradas del usuario, y un objeto `GestionDeCliente` para manejar los clientes.
 * - tiene un constructor vacio.
 * - incluye metodos para agregar reservas, actualizar el tipo de cliente en función de la cantidad
 * de reservas, obtener el tipo de cliente y la cantidad de reservas realizadas por cada cliente.
 *
 * @author Melina
 * @since 2024
 * @version 1
 */

public class GestionReserva implements MetodosBasicosGestion<Reserva>{
    private List<Reserva> reservasPorCliente;
    private Scanner scanner;
    private GestionDeCliente gestionDeCliente;
    private RegistroUser registroUser;

    public GestionReserva() {
        this.reservasPorCliente = new ArrayList<Reserva>();
        GestionJSON.crearArchivoJSON("reservas.json");
        this.scanner=new Scanner(System.in);
        this.gestionDeCliente=new GestionDeCliente();
        this.registroUser = new RegistroUser();
    }

    @Override
    public void ingresarUsuario() throws MesaYaReservadaException {
        System.out.println();
        boolean valido = false;
        while (!valido){
            Reserva aux = new Reserva();
            aux = registroUser.ingresarReserva();
            if (verificarDisponibilidad(aux.getMesa(), aux.getDia(), aux.getHora())){
                agregarYguardar(aux);
                Cliente cliente = gestionDeCliente.encontrarUsuario(aux.getCliente());
                System.out.println("\nReserva " + aux.getId() + " de " + cliente.getNombre() + " " + cliente.getApellido() + " agregada con exito!");
                valido=true;
            }else {
                throw new MesaYaReservadaException("Hubo un problema, la mesa seleccionada ya esta ocupada. Cargue nuevamente la reserva.");
            }
        }
    }

    /**
     * verificarDisponibilidad verifica si una mesa está disponible para una reserva en un día y hora específicos.
     *
     * @param mesa el número de mesa a verificar.
     * @param dia el día de la reserva.
     * @param hora la hora de la reserva.
     * @return true si la mesa está disponible, false si ya está reservada en ese momento.
     */
    public boolean verificarDisponibilidad(int mesa, LocalDate dia, LocalTime hora) {
        if(reservasPorCliente.isEmpty()){
            cargarArrayConArchivo();

        }
        for (Reserva reserva : reservasPorCliente) {
            if (reserva.getMesa() == mesa &&
                    reserva.getDia().equals(dia) &&
                    reserva.getHora().equals(hora) && reserva.getEstado()==true) {
                return false;
            }
        }
        return true;
    }


    /**
     * cargarArrayConArchivo carga las reservas desde un archivo JSON y las agrega a la lista de reservas.
     * Si el archivo contiene datos en un formato incorrecto, se captura la excepción y no se agregan las reservas inválidas.
     *
     * @return una lista de objetos Reserva cargada desde el archivo "reservas.json".
     */
    public List<Reserva> cargarArrayConArchivo(){

        reservasPorCliente.clear();

        JSONTokener aux = GestionJSON.leer("reservas.json");

        try {
            JSONArray arreglo = new JSONArray(aux);
            for(int i = 0; i < arreglo.length(); i++){
                JSONObject aux1 = arreglo.getJSONObject(i);
                Reserva reserva = new Reserva();
                try{
                    reserva = reserva.jsonToReserva(aux1);
                    if (reserva!=null){
                        reservasPorCliente.add(reserva);
                    }
                }catch (FormatoIncorrectoException e){
                    System.out.println("Ocurrio un error al convertir JSON a Reserva.");
                }
            }
        } catch (JSONException e){
            System.out.println("Ocurrio un error al convertir JSONObject a Reserva.");
        }

        return reservasPorCliente;
    }

    public void agregarYguardar (Reserva reserva){
        cargarArrayConArchivo();
        if (!reservasPorCliente.contains(reserva)) {
            reservasPorCliente.add(reserva);
            cargarArchivoConArreglo(reservasPorCliente);
        } else {
            System.out.println("La reserva ya existe.");
        }
    }

    /**
     * cargarArchivoConArreglo guarda las reservas de la lista proporcionada en un archivo JSON.
     * Convierte cada objeto Reserva en su formato JSON correspondiente antes de agregarlo al archivo.
     * Si alguna reserva no se puede convertir debido a un formato incorrecto, se captura la excepción y se muestra un mensaje de error.
     *
     * @param listaReservas la lista de objetos Reserva que se desea guardar en el archivo "reservas.json".
     */
    public void cargarArchivoConArreglo(List<Reserva>listaReservas){
        JSONArray arreglo = new JSONArray();
        try {
            for (Reserva reserva : listaReservas){
                if (reserva!=null){
                    try {
                        JSONObject json = reserva.toJson(reserva);
                        arreglo.put(json);
                    }
                    catch (FormatoIncorrectoException e){
                        System.out.println(e.getMessage());
                    }
                }

            }
            GestionJSON.agregarElemento("reservas.json", arreglo);
        } catch (JSONException e){
            System.out.println("Hubo un problema al cargar el archivo con array.");
        }
    }

    @Override
    public void mostrarDatosUsuario(Reserva a) {

        DateTimeFormatter diaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

                System.out.println();
                System.out.println("--------------------------------------------");
                System.out.println("RESERVA N°:" + a.getId());
                System.out.println("--------------------------------------------");
                System.out.println("ID: " + a.getId());
                System.out.println("Generado el: " + a.getMomento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                System.out.println("Fecha de reserva: " + a.getDia().format(diaFormatter));
                System.out.println("Hora de reserva: " + a.getHora().format(horaFormatter));
                Cliente cliente = gestionDeCliente.encontrarUsuario(a.getCliente());
                if (cliente != null) {
                    System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
                } else {
                    System.out.println("Error: Cliente no encontrado.");
                }
                System.out.println("Mesa: " + a.getMesa());
                System.out.println("Cantidad de personas: " + a.getCantPersonas());
                System.out.println("--------------------------------------------");

    }

    /**
     * El metodo le permite a los usuarios modificar los datos de las reservas cargadas.
     * Lanza una excepcion si la mesa que se quiere ingresar ya se encuentra ocupada.
     * @param c
     * @return
     * @throws MesaYaReservadaException
     */

    public Reserva modificarUsuario (Reserva c) throws MesaYaReservadaException {

        reservasPorCliente = cargarArrayConArchivo();
        boolean salir = false;

            for (Reserva reserva : reservasPorCliente){
                if (c.getId()==reserva.getId()){
                    reservasPorCliente.remove(reserva);
                    c=reserva;
                    while (!salir) {
                        System.out.println("\n Que desea modificar?");
                        System.out.println("1. Dia de reserva.");
                        System.out.println("2. Hora de reserva.");
                        System.out.println("3. Cliente.");
                        System.out.println("4. Mesa.");
                        System.out.println("5. Cantidad de personas.");
                        System.out.println("6. Salir.");
                        int op = scanner.nextInt();
                        scanner.nextLine();
                        switch (op) {
                            case 1:
                                LocalDate dia = null;
                                boolean diaValido = false;
                                while (!diaValido) {
                                    System.out.println("Por favor, ingresa el día (formato: dd/MM):");
                                    String diaInput = scanner.nextLine();
                                    DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("dd/MM");
                                    try {
                                        dia = LocalDate.parse(diaInput, formatoDia);
                                        c.setDia(dia);
                                        diaValido = true;
                                    } catch (DateTimeParseException e) {
                                        System.out.println("Error: El formato del día no es correcto.");
                                    }
                                }

                                break;
                            case 2:

                                LocalTime hora = null;
                                boolean horaValida = false;
                                while (!horaValida) {
                                    System.out.println("Ingrese la hora (formato: HH:mm):");
                                    String horaInput = scanner.nextLine();
                                    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
                                    try {
                                        hora = LocalTime.parse(horaInput, formatoHora);
                                        c.setHora(hora);
                                        horaValida = true;
                                    } catch (DateTimeParseException e) {
                                        System.out.println("Error: El formato de la hora no es correcto.");
                                    }
                                }

                                break;
                            case 3:

                                Cliente cliente = null;
                                boolean valido = false;
                                while (!valido) {
                                    System.out.println();
                                    System.out.println("¿Qué tipo de cliente desea ingresar?");
                                    System.out.println("1. Cliente existente.");
                                    System.out.println("2. Crear nuevo cliente.");
                                    int opcion = scanner.nextInt();
                                    scanner.nextLine();

                                    switch (opcion) {
                                        case 1:
                                            System.out.println("Ingrese el DNI del cliente: ");
                                            String dni = scanner.nextLine();
                                            cliente = gestionDeCliente.encontrarUsuario(dni);
                                            if (cliente != null) {
                                                c.setCliente(cliente.getId());
                                                valido = true;
                                            } else {
                                                System.out.println("No se encontró el cliente.");
                                            }
                                            break;
                                        case 2:
                                            gestionDeCliente.ingresarUsuario();
                                            System.out.println("Ingrese el DNI del cliente recién ingresado: ");
                                            String dni2 = scanner.nextLine();
                                            cliente = gestionDeCliente.encontrarUsuario(dni2);
                                            if (cliente != null) {
                                                c.setCliente(cliente.getId());
                                                valido = true;
                                            } else {
                                                System.out.println("No se encontró el cliente.");
                                            }
                                            break;
                                        default:
                                            System.out.println("Opción incorrecta, ingrese una opción válida.");
                                    }
                                }

                                break;
                            case 4:
                                boolean estado = false;
                                while (!estado){
                                    System.out.println("Ingrese la mesa:");
                                    int mesa = scanner.nextInt();
                                    estado = verificarDisponibilidad(mesa, c.getDia(), c.getHora());
                                    if (!estado){
                                        throw new MesaYaReservadaException("La mesa no esta disponible, ingrese otra.");
                                    }else {
                                        c.setMesa(mesa);
                                        estado=true;
                                    }
                                }
                                break;
                            case 5:
                                boolean cantPersonasValido = false;
                                while (!cantPersonasValido){
                                    System.out.println("Ingrese la cantidad de personas:");
                                    int cantPersonas = scanner.nextInt();
                                    if (cantPersonas<=0){
                                        System.out.println("La cantidad de personas no puede ser 0");
                                    }else {
                                        c.setCantPersonas(cantPersonas);
                                        cantPersonasValido=true;
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
                    reservasPorCliente.add(c);
                    cargarArchivoConArreglo(reservasPorCliente);
                    System.out.println("¡Cambios guardados con exito!");
                    return c;
                }
        }
        return null;
    }

    /**
     * El metodo permite dar de baja reservas.
     * @param a
     */

    @Override
    public void darDeBajaUsuario(Reserva a) {
        reservasPorCliente = cargarArrayConArchivo();

        boolean encontrado = false;
        String opcion = "";

        for (int i = 0; i<reservasPorCliente.size() ; i++) {
            Reserva reserva = reservasPorCliente.get(i);
            if (a.getId() == reserva.getId()) {
                encontrado=true;
                System.out.println("¿Esta seguro de eliminar la reserva? SI o NO.");
                opcion = scanner.nextLine();
                if (opcion.equalsIgnoreCase("si")) {
                    reserva.setEstado(false);
                    cargarArchivoConArreglo(reservasPorCliente);
                    System.out.println("Reserva eliminada con exito.");
                    return;
                } else if (opcion.equalsIgnoreCase("no")) {
                    System.out.println("Operacion cancelada.");
                    return;
                } else {
                    System.out.println("Opcion invalida.");
                }
            }
        }

        if (!encontrado){
            System.out.println("No se encontro la reserva con el ID ingresado.");
        }
    }

    /**
     * El metodo da de baja la reserva una vez que se genera el Ticket de esta.
     * @param a
     */

    public void darDeBajaReservaTicket (Reserva a) {
        reservasPorCliente = cargarArrayConArchivo();

        boolean encontrado = false;

        for (int i = 0; i<reservasPorCliente.size() ; i++) {
            Reserva reserva = reservasPorCliente.get(i);
            if (a.getId() == reserva.getId()) {
                encontrado=true;
                reserva.setEstado(false);
                cargarArchivoConArreglo(reservasPorCliente);
                return;
            }
        }

        if (!encontrado){
            System.out.println("No se encontro la reserva con el ID ingresado.");
        }
    }

    /**
     * El metodo le permite a los usuarios dar de alta reservas.
     * @param a
     */

    @Override
    public void darDeAltaUsuario(Reserva a) {
        reservasPorCliente = cargarArrayConArchivo();

        boolean encontrado = false;
        String opcion = "";

        for (int i = 0; i<reservasPorCliente.size() ; i++) {
            Reserva reserva = reservasPorCliente.get(i);
            if (a.getId() == reserva.getId()) {
                encontrado=true;
                System.out.println("¿Esta seguro de dar de alta la reserva? SI o NO.");
                opcion = scanner.nextLine();
                if (opcion.equalsIgnoreCase("si")) {
                    reserva.setEstado(true);
                    cargarArchivoConArreglo(reservasPorCliente);
                    System.out.println("Reserva dada de alta con exito.");
                    return;
                } else if (opcion.equalsIgnoreCase("no")) {
                    System.out.println("Operacion cancelada.");
                    return;
                } else {
                    System.out.println("Opcion invalida.");
                }
            }
        }

        if (!encontrado){
            System.out.println("No se encontro la reserva con el ID ingresado.");
        }
    }

    /**
     * El metodo muestra todas las reservas, sin importar su estado.
     */

    @Override
    public void mostrarColeccion() {

        if (reservasPorCliente.isEmpty()) {
            cargarArrayConArchivo();
        }

        for (Reserva reserva : reservasPorCliente) {
            if (reserva != null) {
                mostrarDatosUsuario(reserva);
            } else {
                System.out.println("Error: Se encontró una reserva nula.");
            }
        }
    }

    /**
     * El metodo le permite al usuario encontrar una reserva segun el DNI del cliente que la hizo.
     * @param dni
     * @return {@code Reserva} encontrada.
     */

    @Override
    public Reserva encontrarUsuario(String dni) {
        if (reservasPorCliente.isEmpty()) {
            cargarArrayConArchivo();
        }

        Cliente cliente = null;
        for (Reserva reserva : reservasPorCliente){
            if (gestionDeCliente.encontrarUsuario(reserva.getCliente()).getDni().equals(dni)){
                return reserva;
            }
        }

        System.out.println("No se encontro la reserva.");
        return null;
    }

    /**
     * El metodo le permite al usuario encontrar una reserva segun la informacion enviada a traves de
     * los parametros.
     * @param dni
     * @param dia
     * @param hs
     * @return
     */

    public Reserva encontrarUsuarioMuchos(String dni, LocalDate dia, LocalTime hs) {
        if (reservasPorCliente.isEmpty()) {
            cargarArrayConArchivo();
        }

        for (Reserva reserva : reservasPorCliente){
            if (gestionDeCliente.encontrarUsuario(reserva.getCliente()).getDni().equals(dni) && reserva.getDia().equals(dia)
                    && reserva.getHora().equals(hs)){
                return reserva;
            }
        }

        System.out.println("No se encontro la reserva.");
        return null;
    }

    /**
     * El metodo lista las reservas realizadas por un Cliente segun su DNI.
     * @param dni
     */

    @Override
    public void listarUsuarios(String dni) {
        if (reservasPorCliente.isEmpty()) {
            cargarArrayConArchivo();
        }
        boolean en = false;
        for (Reserva reserva : reservasPorCliente){
            if (gestionDeCliente.encontrarUsuario(reserva.getCliente()).getDni().equals(dni)){
                mostrarDatosUsuario(reserva);
                en = true;
            }
        }
        if(!en){
            System.out.println("No se encontraron reservas del cliente ");
        }
    }

    /**
     * El metodo lista las reservas segun el dia enviado por parametro.
     * @param dia
     */

    public void listarUsuarios(LocalDate dia) {
        if (reservasPorCliente.isEmpty()) {
            cargarArrayConArchivo();
        }
        boolean en = false;
        for (Reserva reserva : reservasPorCliente){
            if (reserva.getDia().equals(dia)){
                mostrarDatosUsuario(reserva);
                en = true;
            }
        }
        if(!en){
            System.out.println("No se encontraron reservas ese dia.");
        }
    }

    /**
     * El metodo lista las reservas segun la hora de estas.
     * @param hora
     */

    public void listarUsuarios(LocalTime hora) {
        if (reservasPorCliente.isEmpty()) {
            cargarArrayConArchivo();
        }
        boolean en = false;
        for (Reserva reserva : reservasPorCliente){
            if (reserva.getHora().equals(hora)){
                mostrarDatosUsuario(reserva);
                en = true;
            }
        }
        if(!en){
            System.out.println("No se encontraron reservas a esa hora.");
        }
    }

    /**
     * El metodo lista las reservas segun su dia y hora.
     * @param dia
     * @param hora
     */

    public void listarUsuarios(LocalDate dia, LocalTime hora) {
        if (reservasPorCliente.isEmpty()) {
            cargarArrayConArchivo();
        }
        boolean en = false;
        for (Reserva reserva : reservasPorCliente){
            if (reserva.getHora().equals(hora) && reserva.getDia().equals(dia)){
                mostrarDatosUsuario(reserva);
                en = true;
            }
        }
        if(!en){
            System.out.println("No se encontraron reservas el dia " + dia + " a la hora " + hora);
        }
    }

    /**
     * El metodo lista las reservas segun el estado de estas (activo o inactivo).
     * @param aux
     */

    @Override
    public void listarUsuarios(boolean aux) {
        if (reservasPorCliente.isEmpty()) {
            cargarArrayConArchivo();
        }
        boolean en = false;
        for (Reserva reserva : reservasPorCliente){
            if (reserva.getEstado() == aux){
                mostrarDatosUsuario(reserva);
                en = true;
            }
        }
        if(!en){
            System.out.println("No se encontraron reservas.");
        }
    }

    /**
     * El metodo busca la reserva segun su ID:
     * @param id
     * @return {@code Reserva} encontrada.
     */

    @Override
    public Reserva encontrarUsuario(int id) {
        if (reservasPorCliente.isEmpty()) {
            cargarArrayConArchivo();
        }

        for (Reserva reserva : reservasPorCliente){
            if (reserva != null && reserva.getId() == id){
                return reserva;
            }
        }

        return null;
    }

    /**
     * obtenerCantidadDeReservas obtiene la cantidad de reservas realizadas por un cliente específico.
     *
     * @param cliente el objeto Cliente del cual se desea obtener la cantidad de reservas.
     * @return la cantidad de reservas realizadas por el cliente.
     */
    public int obtenerCantidadDeReservas(Cliente cliente) {
        if (reservasPorCliente.isEmpty()) {
            cargarArrayConArchivo();
        }
        int i = 0;
        for(Reserva r : reservasPorCliente){
            if(gestionDeCliente.encontrarUsuario(r.getCliente()).equals(cliente)){
                i++;
            }
        }
        return i;
    }
}
