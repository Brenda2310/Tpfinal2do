package Users;

import Archivos.FormatoIncorrectoException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * La clase Users.EmpleadoMedioTiempo tiene como campos su nombre de usuario, contrasenia, nombre,
 * apellido, dni, telefono, direccion, email, estado, sueldo, horas trabajadas y precioXhora.
 * Hereda de la clase abstracta Empleado
 * Tiene un constructor con todos los atributos y otro null
 * Metodos: getters y setters, toString
 * Otros metodos: toJson, jsonToEmpleadoMT, calcularSueldo, calcularHorasExtra
 *
 * @author Brenda
 * @since 2024
 * @version 2
 */
public class EmpleadoMedioTiempo extends Empleado implements Comparable{
    private int horasTrabajadas;
    private double precioXhora;

    public EmpleadoMedioTiempo(){

    }

    public EmpleadoMedioTiempo(String nombre, String apellido, String dni, String telefono, String direccion, String email, double sueldo, int horasTrabajadas, double precioXhora) {
        super(nombre, apellido, dni, telefono, direccion, email, sueldo);
        this.horasTrabajadas = horasTrabajadas;
        this.precioXhora = precioXhora;
    }

    ///Getters y Setters

    public double getPrecioXhora() {
        return precioXhora;
    }

    public void setPrecioXhora(double precioXhora) {
        this.precioXhora = precioXhora;
    }

    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    /**
     * toJson es un metodo que permite transformar un objeto EmpleadoMedioTiempo a un JSONObject, recibe un
     * EmpleadoMedioTiempo y retorna un JSONObject.
     * @param e
     * @return jsonObject
     */
    public JSONObject toJson (EmpleadoMedioTiempo e){

        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject();
            jsonObject.put("id", e.getId());
            jsonObject.put("username", e.getUsername());
            jsonObject.put("contrasenia", e.getContrasenia());
            jsonObject.put("nombre", e.getNombre());
            jsonObject.put("apellido", e.getApellido());
            jsonObject.put("dni", e.getDni());
            jsonObject.put("telefono", e.getTelefono());
            jsonObject.put("direccion", e.getDireccion());
            jsonObject.put("email", e.getEmail());
            jsonObject.put("estado", e.getEstado());
            jsonObject.put("sueldo", e.getSueldo());
            jsonObject.put("horasTrabajadas", e.getHorasTrabajadas());
            jsonObject.put("precioPorHora", e.getPrecioXhora());
        }catch (JSONException ex){
            ex.printStackTrace();
        }

        return jsonObject;
    }


    /**
     * jsonToEmpleadoMT es un metodo que tranforma un JSONObject en un objeto EmpleadoMedioTiempo
     * recibe un JSONObject y retorna un EmpleadoMedioTiempo, lanza un FormatoIncorrectoException
     * si el formato del JSONObject no tiene los parametros de un EmpleadoMedioTiempo.
     * @param json
     * @return empleadoLeido
     * @throws FormatoIncorrectoException
     */
    public EmpleadoMedioTiempo jsonToEmpleadoMT (JSONObject json) throws FormatoIncorrectoException {

        EmpleadoMedioTiempo empleadoLeido = new EmpleadoMedioTiempo();
        try {
            if(json.has("id") && json.has("username") && json.has("contrasenia") &&
                    json.has("nombre") && json.has("apellido") && json.has("dni") &&
                    json.has("telefono") && json.has("direccion") && json.has("email") &&
                    json.has("estado") && json.has("sueldo") && json.has("horasTrabajadas")
                    && json.has("precioPorHora")){
                empleadoLeido.setId(json.getInt("id"));
                empleadoLeido.setUsername(json.getString("username"));
                empleadoLeido.setContrasenia(json.getString("contrasenia"));
                empleadoLeido.setNombre(json.getString("nombre"));
                empleadoLeido.setApellido(json.getString("apellido"));
                empleadoLeido.setDni(json.getString("dni"));
                empleadoLeido.setTelefono(json.getString("telefono"));
                empleadoLeido.setDireccion(json.getString("direccion"));
                empleadoLeido.setEmail(json.getString("email"));
                empleadoLeido.setEstado(json.getBoolean("estado"));
                empleadoLeido.setSueldo(json.getDouble("sueldo"));
                empleadoLeido.setHorasTrabajadas(json.getInt("horasTrabajadas"));
                empleadoLeido.setPrecioXhora(json.getDouble("precioPorHora"));
            }
            else {
                throw new FormatoIncorrectoException("El formato JSON no corresponde a un empleado de medio tiempo");
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return empleadoLeido;
    }

    /**
     * El metodo calcularSueldo multiplica las horas trabajadas por el precio por hora
     * y suma el total con el resultado del metodo calcularHorasExtra
     * @param horasExtra
     * @return (horasTrabajadas * precioXhora) + calcularHorasExtra(horasExtra);
     */
    public double calcularSueldo (int horasExtra){
        return (horasTrabajadas * precioXhora) + calcularHorasExtra(horasExtra);
    }

    /**
     *El metodo calcularHorasExtra multiplica las horas extra por el doble del
     * precio por hora, las horas extra se pagan doble
     * @param horasExtra
     * @return horasExtra * Math.pow(precioXhora, 2);
     */
    public double calcularHorasExtra (int horasExtra){
        return horasExtra * ((double) precioXhora * 2);
    }

    @Override
    public String toString() {
        return "EmpleadoMedioTiempo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", email='" + email + '\'' +
                ", estado=" + estado +
                ", sueldo=" + sueldo +
                ", precioXhora=" + precioXhora +
                ", horasTrabajadas=" + horasTrabajadas +
                "} ";
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof EmpleadoMedioTiempo) {
            EmpleadoMedioTiempo otroEmpleado = (EmpleadoMedioTiempo) o;
            return this.dni.compareTo(otroEmpleado.getDni());
        } else if (o instanceof EmpleadoTiempoCompleto) {
            EmpleadoTiempoCompleto otroEmpleado = (EmpleadoTiempoCompleto) o;
            return this.dni.compareTo(otroEmpleado.getDni());
        } else {
            throw new ClassCastException("Tipo de empleado no reconocido");
        }
    }

}

