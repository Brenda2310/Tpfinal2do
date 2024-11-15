package Gestion;

import java.util.*;

import Archivos.FormatoIncorrectoException;
import Archivos.GestionJSON;
import Users.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
public class GestionDeCliente {

    private List<Cliente> listaDeClientes;
    private RegistroUser registroUser;

    public GestionDeCliente() {
        this.listaDeClientes = new ArrayList<Cliente>();
        GestionJSON.crearArchivoJSON("clientes.json");
        this.registroUser = new RegistroUser();
    }

    public void ingresarCliente(){
        Scanner scan = new Scanner(System.in);
        System.out.println();
        Cliente aux = registroUser.registroCliente();
        agregarYguardar(aux);
        System.out.println("\nUsers.Cliente " + aux.getNombre() + " " + aux.getApellido() + " agregado con exito!");

    }

    public void cargarArrayConArchivo(){
        JSONTokener aux = GestionJSON.leer("clientes.json");

        try {

            JSONArray arreglo = new JSONArray(aux);

            for(int i = 0; i < arreglo.length(); i++){
                JSONObject aux1 = arreglo.getJSONObject(i);
                Cliente cliente = new Cliente();
                cliente = cliente.jsonToCliente(aux1);
                listaDeClientes.add(cliente);
            }
        } catch (JSONException e){
            System.out.println("Ocurrio un error al convertir JSONObject a Users.Administrador.");
        }
    }

    public void agregarYguardar (Cliente nuevoCliente){
        cargarArrayConArchivo();
        listaDeClientes.add(nuevoCliente);
        cargarArchivoConArreglo(listaDeClientes);
    }

    public void cargarArchivoConArreglo(List<Cliente> listaDeClientes){
        JSONArray arreglo = new JSONArray();
        try {

            for (Cliente cliente : listaDeClientes){
                try {
                    JSONObject json = cliente.toJson(cliente);
                    arreglo.put(json);
                    GestionJSON.agregarElemento("clientes.json", arreglo);
                }
                catch (FormatoIncorrectoException e){
                    System.out.println(e.getMessage());
                }
            }
        } catch (JSONException e){
            System.out.println("Hubo un problema al cargar el archivo con array.");
        }
    }

    public Cliente modificarCliente (Cliente c){

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n Que desea modificar?");
        System.out.println("1. Username.");
        System.out.println("2. Contraseña.");
        System.out.println("3. Nombre.");
        System.out.println("4. Apellido.");
        System.out.println("5. DNI.");
        System.out.println("6. Telefono.");
        System.out.println("7. Direccion.");
        System.out.println("8. Email.");
        System.out.println("9. Tipo de cliente.");
        System.out.println("10 Salir.");
        int op = scanner.nextInt();
        scanner.nextLine();
        switch (op){
            case 1:

                String username = "";
                boolean usernameValido = false;

                while (!usernameValido){
                    System.out.println("Ingrese su nuevo username: ");
                    username = scanner.nextLine();
                    try {
                        Validaciones.validarNombreUsuario(username);
                        c.setUsername(username);
                        usernameValido = true;
                    }catch (DatoInvalidoException e){
                        System.out.println("Error: " + e.getMessage() + ". Por favor, intente nuevamente");
                    }
                }

                break;
            case 2:

                String contrasenia = "";
                boolean contraseniaValida = false;

                while (!contraseniaValida){
                    System.out.println("Ingrese su nueva contrasenia: ");
                    contrasenia = scanner.nextLine();
                    try {
                        Validaciones.validarContrasenia(contrasenia);
                        c.setContrasenia(contrasenia);
                        contraseniaValida = true;
                    }catch (DatoInvalidoException e){
                        System.out.println("Error: " + e.getMessage() + ". Por favor, intente nuevamente");
                    }
                }

                break;
            case 3:

                String nombre = "";
                boolean nombreValido = false;

                while (!nombreValido){
                    System.out.println("Ingrese su nuevo nombre: ");
                    nombre = scanner.nextLine();
                    try {
                        Validaciones.validarCadenas(nombre);
                        c.setNombre(nombre);
                        nombreValido = true;
                    }catch (DatoInvalidoException e){
                        System.out.println("Error: " + e.getMessage() + ". Por favor, intente nuevamente");
                    }
                }

                break;
            case 4:

                String apellido = "";
                boolean apellidoValido = false;

                while (!apellidoValido){
                    System.out.println("Ingrese su nuevo apellido: ");
                    apellido = scanner.nextLine();
                    try {
                        Validaciones.validarCadenas(apellido);
                        c.setApellido(apellido);
                        apellidoValido = true;
                    }catch (DatoInvalidoException e){
                        System.out.println("Error: " + e.getMessage() + ". Por favor, intente nuevamente");
                    }
                }

                break;
            case 5:

                String dni = "";
                boolean dniValido = false;

                while (!dniValido){
                    System.out.println("Ingrese su nuevo DNI: ");
                    dni = scanner.nextLine();
                    try {
                        Validaciones.validarDNI(dni);
                        c.setDni(dni);
                        dniValido = true;
                    }catch (DatoInvalidoException e){
                        System.out.println("Error: " + e.getMessage() + ". Por favor, intente nuevamente");
                    }
                }

                break;
            case 6:

                String telefono = "";
                boolean telefonoValido = false;

                while (!telefonoValido){
                    System.out.println("Ingrese su nuevo telefono: ");
                    telefono = scanner.nextLine();
                    try {
                        Validaciones.validarTelefono(telefono);
                        c.setTelefono(telefono);
                        telefonoValido = true;
                    }catch (DatoInvalidoException e){
                        System.out.println("Error: " + e.getMessage() + ". Por favor, intente nuevamente");
                    }
                }

                break;
            case 7:

                String direccion = "";
                boolean direccionValido = false;

                while (!direccionValido){
                    System.out.println("Ingrese su nueva direccion: ");
                    direccion = scanner.nextLine();
                    try {
                        Validaciones.validarDireccion(direccion);
                        c.setDireccion(direccion);
                        direccionValido = true;
                    }catch (DatoInvalidoException e){
                        System.out.println("Error: " + e.getMessage() + ". Por favor, intente nuevamente");
                    }
                }

                break;
            case 8:

                String email = "";
                boolean emailValido = false;

                while (!emailValido){
                    System.out.println("Ingrese su nuevo email: ");
                    email = scanner.nextLine();
                    try {
                        Validaciones.validarEmail(email);
                        c.setEmail(email);
                        emailValido = true;
                    }catch (DatoInvalidoException e){
                        System.out.println("Error: " + e.getMessage() + ". Por favor, intente nuevamente");
                    }
                }

                break;
            case 9:

                int tipoCliente = 0;
                boolean tipoClienteValido = false;

                while (!tipoClienteValido){
                    System.out.println("Ingrese su nuevo tipo de cliente: ");
                    System.out.println("1. Estandar.");
                    System.out.println("2. Premium.");
                    System.out.println("3. VIP.");
                    tipoCliente = scanner.nextInt();
                    scanner.nextLine();
                    if (tipoCliente == 1){
                        c.setTipoCliente(TipoCliente.ESTANDAR);
                        tipoClienteValido = true;
                    }else if (tipoCliente == 2) {
                        c.setTipoCliente(TipoCliente.PREMIUM);
                        tipoClienteValido = true;
                    }else if (tipoCliente == 3) {
                        c.setTipoCliente(TipoCliente.VIP);
                        tipoClienteValido = true;
                    }else {
                        System.out.println("Opcion invalida. Ingrese 1, 2 o 3.");
                    }
                }
                break;
            case 10:
                System.out.println("Saliendo del menu de modificacion de usuario...");
                break;
            default:
                System.out.println("Opcion invalida.");
                break;
        }

        return c;
    }

    public void mostrarListaDeClientes(){
        if(listaDeClientes.isEmpty()){
            cargarArrayConArchivo();
        }
        Collections.sort(listaDeClientes);
        listaDeClientes.forEach(System.out::println);

    }
}
