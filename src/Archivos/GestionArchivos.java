package Archivos;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;


public class GestionArchivos {

    public static void crearArchivo(String nombreArchivo){
        File file = new File(nombreArchivo);

        try {
            if(!file.exists()){
                PrintWriter salida = new PrintWriter(file);
                salida.close();
            }

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        System.out.println("El archivo se ha creado correctamente");
    }

    ///hay que hacerle cambios a este
    public static void modificarArchivo(String nombreArchivo, String contenido){
        File file = new File(nombreArchivo);

        try {
            PrintWriter salida = new PrintWriter(file);
            salida.println(contenido);
            salida.close();
            System.out.println("El archivo se modifico correctamente");
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void agregarInformacion(String nombreArchivo, String contenido){
        File file = new File(nombreArchivo);

        try {
            PrintWriter salida = new PrintWriter(new FileWriter(file, true));
            salida.println(contenido);
            salida.close();
            System.out.println("El archivo se escribio correctamente");
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray leerArchivo(String nombreArchivo){
        File file = new File(nombreArchivo);
        JSONArray arreglo = new JSONArray();

        try {
           BufferedReader entrada = new BufferedReader(new FileReader(file));
           String lineaActual = entrada.readLine();
           while (lineaActual != null){
               JSONObject aux = new JSONObject(lineaActual);
               arreglo.put(aux);
               lineaActual = entrada.readLine();
           }
           entrada.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arreglo;
    }

    public static void borrarContenido(String nombreArchivo){
        File file = new File(nombreArchivo);

        try {
            FileWriter salida = new FileWriter(file, false);
            salida.write("");
            salida.close();
            System.out.println("El archivo se limpio correctamente");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarArchivo(String nombreArchivo){
        File file = new File(nombreArchivo);

        try {
            if(file.delete()){
                System.out.println("Archivo eliminado con exito");
            }
            else {
                System.out.println("No se pudo eliminar el archivo");
            }
        }
        catch (Exception e) {
            System.out.println("Ocurrio un error al eliminar el archivo");
            e.printStackTrace();
        }
    }
}
