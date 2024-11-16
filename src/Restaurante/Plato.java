package Restaurante;

import Archivos.FormatoIncorrectoException;
import Users.Cliente;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.management.PlatformLoggingMXBean;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * La clase Restaurante.Plato tiene como campos su nombre, descripcion, disponibilidad y precio
 * tiene un constructor con todos los atributos, uno solo con nombre
 * y otro con nombre y descripcion
 * metodos:
 * otros metodos:
 *
 * @author Brenda
 * @since 2024
 * @version 2
 */
public class Plato {
    private String nombre;
    private String descripcion;
    private double precio;
    private boolean disponibilidad;
    private TipoPlato tipoPlato;

    public Plato() {
    }

    public Plato(String nombre) {
        this.nombre = nombre;
        this.descripcion = null;
        this.precio = 0;
        this.disponibilidad = false;
        this.tipoPlato = null;
    }

    public Plato(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = 0;
        this.disponibilidad = false;
        this.tipoPlato = null;
    }

    public Plato(String nombre, String descripcion, double precio, TipoPlato tipoPlato) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponibilidad = true;
        this.tipoPlato = tipoPlato;
    }

    ///Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public TipoPlato getTipoPlato() {
        return tipoPlato;
    }

    public void setTipoPlato(TipoPlato tipoPlato) {
        this.tipoPlato = tipoPlato;
    }

    //PLATO TO JSON

    public JSONObject toJson (Plato p){
        JSONObject jsonObject = null;
            try{
            jsonObject = new JSONObject();
            jsonObject.put("nombre", p.getNombre());
            jsonObject.put("descripcion", p.getDescripcion());
            jsonObject.put("precio", p.getPrecio());
            jsonObject.put("disponibilidad", p.isDisponibilidad());
            jsonObject.put("tipoPlato", p.getTipoPlato());
        }catch (
        JSONException ex){
            ex.printStackTrace();
        }
            return jsonObject;
    }

    //JSON TO PLATO

    /**
     * jsonToPlato es un metodo que tranforma un JSONObject en un objeto Plato recibe un
     * JSONObject y retorna un Plato, lanza un FormatoIncorrectoException si el formato del
     * JSONObject no tiene los parametros de un Plato.
     * @param json
     * @return platoLeido
     * @throws FormatoIncorrectoException
     */

    public Plato jsonToPlato (JSONObject json) throws FormatoIncorrectoException {

        Plato platoLeido = new Plato();
        try {
            if(json.has("nombre") && json.has("descripcion") && json.has("precio") &&
                    json.has("disponibilidad") && json.has("tipoPlato")){
                platoLeido.setNombre(json.getString("nombre"));
                platoLeido.setDescripcion(json.getString("descripcion"));
                platoLeido.setPrecio(json.getDouble("precio"));
                platoLeido.setDisponibilidad(json.getBoolean("disponibilidad"));
                platoLeido.setTipoPlato(json.getEnum(TipoPlato.class, "tipoPlato"));
            }
            else{
                throw new FormatoIncorrectoException("El formato de JSON no corresponde a un plato.");
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return platoLeido;
    }

    ///ToString

    @Override
    public String toString() {
        return "Restaurante.Plato{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", disponibilidad=" + disponibilidad +
                '}';
        
    }
}
