package com.stonefacesoft.ottaa;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by Hector on 27/02/2015.
 * Un Picto es un objeto que consta de 3 partes
 * una imagen que puede ser tanto de la galeria de picto o una foto del usuario
 * dos textos uno es el nombre del picto y el otro es lo qu va a decir el picto (Ej. nombre = saludo, oracion = Hola, como andas?)
 * un numero que es el ID unico de cada Picto
 */
public class Picto implements Cloneable{
    private static final String TAG = "Picto";
    private Drawable Icono;
    private String Oracion;
    private String Nombre;
    private int color;
    private final int ID;

    /**
     * Constructor de la clase Picto, todos los datos que se pasan como parametros son sacados de la base de datos
     * @param ID
     * @param icono
     * @param oracion
     * @param nombre
     * @param color
     */
    public Picto(int ID,Drawable icono, String oracion, String nombre, int color){
        super();
        this.ID = ID;
        this.Icono = icono;
        this.Oracion = oracion;
        this.Nombre = nombre;
        this.color = color;
    }

    /**
     * Clona un objeto del tipo Object (Cualquier objeto se hereda de este tipo por lo tanto por polimorfismocloca cualquier tipo de objeto)
     * @return  the new object or null
     */
    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException ex) {
            Log.e(TAG, "clone: No se puede clonar");
        }
        return obj;
    }

    /**
     * Devuelve el ID unico del Picto
     * @return ID del Picto (int)
     */
    public int getNum() {return ID;}

    /**
     * Devuelve la imagen del Picto
     * @return  Icono del Picto (Drawable)
     */
    public Drawable getIcono() {return Icono;}

    /**
     * Devuelve la oracion del Picto, lo que a a imprimir en pantalla o reproducir con el TTS
     * @return  Oracion del Picto (String)
     */
    public String getOracion() {return Oracion;}

    /**
     * Devuelve el nombre del Picto
     * @return  Name of the pictagram (String)
     */
    public String getNombre() {return Nombre;}

    /**
     * Devuelve el color del Picto, es un entero que representa un color
     * @return color of the pictogram(int)
     */
    public int getColor() {return color;}

    /**
     * @set the color of the pictogram
     * @param color (int)
     */
    public void setColor(int color) {this.color = color;}

    /**
     * Setea la oracion del Picto
     * @param oracion   (String)
     */
    public void setOracion(String oracion) {this.Oracion = oracion;}

    /**
     * Set the name of the pictogram
     * @param nombre   (String)
     */
    public void setNombre(String nombre) {this.Nombre = nombre;}

    /**
     * Setea el Icono del Picto
     * @param icono     (Drawable)
     */
    public void setIcono(Drawable icono)  {this.Icono = icono;}

}
