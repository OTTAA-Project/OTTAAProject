package com.stonefacesoft.ottaa.utils.constants;

/**
 * Created by Hector on 16/02/2016.
 */

/**
 * Constants used in this sample.
 */


public final class Constants {


    private Constants() {
    }

    public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 24;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 100; // 1 mile, 1.6 km

    //Constants FOR RESULT
    public static final int RC_SIGN_IN = 400;


    //2 mins
//    public static final long UN_MES = 120;
    //30 dias
    public static final long UN_MES = 2678400;
    public static final long UNA_SEMANA = 604800;
    public static final long UN_ANIO = 31536000;
    //MES 2678400
    //A&NtildeO 31536000

    public static final int CINCO_MEGAS = 5242880;

    //Permissions Constants
    public static final int PERMISSION_UBICACION = 110;
    public static final int PERMISSION_CALENDAR = 100;
    public static final int EXTERNAL_STORAGE = 120;


    //Constantes de descarga
    public static final int PICTOS_DESCARGADOS = 121;
    public static final int GRUPOS_DESCARGADOS = 122;
    public static final int FRASES_DESCARGADOS = 123;
    public static final int FRASESJUEGOS_DESCARGADOS=100;
    public static final int TIEMPO_SUBIDO = 250;
    public static final String ARCHIVO_PICTOS_DATABASE = "pictosDatabase_es.txt";
    public static final String ARCHIVO_FOTO_BACKUP = "fotosborradas.txt";

    //Constantes de datos enontrados
    public static final int PICTOS_ENCONTRADOS = 244;
    public static final int GRUPOS_ENCONTRADOS = 245;
    public static final int FRASES_ENCONTRADOS = 246;
    public static final int TODO_ENCONTRADO = 735;
    public static int FOTOS_DESCARGADOS = 124;

    public static final int FOTOS_YA_DESCARGADAS = -1;
    public static final int FOTO_DESCARGADA = 1001;
    public static int TODO_DESCARGADOBACKUP = 490;


    //Nombres de Archivos
    public static final String ARCHIVO_PICTOS = "pictos.txt";
    public static final String ARCHIVO_GRUPOS = "grupos.txt";
    public static final String ARCHIVO_FRASES = "frases.txt";
    public static final String ARCHIVO_FRASES_FAVORITAS = "favoritePhrases.txt";
    public static final String ARCHIVO_FRASES_JUEGOS = "frasesjuegos.txt";
    public static final String ARCHIVO_JUEGO="juegos.txt";
    public static final String ARCHIVO_JUEGO_DESCRIPCION="juegos_desc.txt";

    public static int TODO_DESCARGADO = 366;
    public static int VUELTAS_CARRETE = 5;

    //Tipo de tags
    public static final String HORA = "hora";
    public static final String UBICACION = "ubicacion";
    public static final String CALENDARIO = "calendario";
    public static final String EDAD = "edad";
    public static final String CLIMA = "clima";
    public static final String SEXO = "sexo";

    //NombresBaseDatosHijos
    public static final String PICTOS = "Pictos";
    public static final String Grupos = "Grupos";
    public static final String Frases = "Frases";
    public static final String FrasesFavoritas = "FrasesFavoritas";

    //Datos del usuario
    public static final String JUEGOS = "Juegos";
    public static final String PAGO = "Pago";
    public static final String GENERO = "pref_sexo";
    public static final String SKILLHAND = "skillHand";
    public static final String FECHACUMPLE = "birth_date";
    public static final String FECHAPAGO = "FechaPago";
    public static final String FECHAVENCIMIENTO="FechaVencimiento";
    public static final String PREMIUM = "premium";
    public static final String BARRIDO_BOOL = "bool_barrido";
    public static final String SUGERENCIA_BOOL="bool_sugerencias";
    public static final String PRIMERAULTIMACONEXION = "PrimeraUltimaConexion";
    public static final String PRIMERACONEXION = "PConexion";
    public static final String UCONEXION = "UConexion";

    public static final String BACKUP = "BackUp";
    public static final String PRIMERUSO = "PrimerUso";
    public static final String FOTOSUSUARIO = "FotosUsuario";
    public static final String FOTOS = "Fotos";
    public static final String USUARIOS = "Usuarios";
    public static final String EDADUSUARIO = "EdadUsuario";
    public static final String NOMBRE = "Nombre";
    public static final String AVATAR = "Avatar";
    //Constants ubicacion places

    public static final int TYPE_AIRPORT = 2;
    public static final int TYPE_AMUSEMENT_PARK = 3;
    public static final int TYPE_ATM = 6;
    public static final int TYPE_BAKERY = 7;//agregar
    public static final int TYPE_BANK = 8;
    public static final int TYPE_BAR = 9;
    public static final int TYPE_BUS_STATION = 14;
    public static final int TYPE_CAFE = 15;
    public static final int TYPE_CHURCH = 23;
    public static final int TYPE_CLOTHING_STORE = 25;//tienda
    public static final int TYPE_CONVENIENCE_STORE = 26;//
    public static final int TYPE_DENTIST = 28;
    public static final int TYPE_DEPARTMENT_STORE = 29;
    public static final int TYPE_HAIR_CARE = 45;//add this tag
    public static final int TYPE_HOSPITAL = 50;
    public static final int TYPE_INSURANCE_AGENCY = 51;
    public static final int TYPE_JEWELRY_STORE = 52;
    public static final int TYPE_LAUNDRY = 53;
    public static final int TYPE_LODGING = 59;//add this tag
    public static final int TYPE_MEAL_DELIVERY = 60;
    public static final int TYPE_MEAL_TAKEAWAY = 61;
    public static final int TYPE_MOVIE_THEATER = 64;// cine
    public static final int TYPE_MUSEUM = 66;// add
    public static final int TYPE_NIGHT_CLUB = 67;
    public static final int TYPE_PARK = 69;
    public static final int TYPE_PET_STORE = 71;
    public static final int TYPE_PHARMACY = 72;
    public static final int TYPE_PHYSIOTHERAPIST = 73;
    public static final int TYPE_RESTAURANT = 79;
    public static final int TYPE_SCHOOL = 82;
    public static final int TYPE_SHOPPING_MALL = 84;// put to store
    public static final int TYPE_STADIUM = 86;
    public static final int TYPE_STORE = 88; //use them
    public static final int TYPE_SUBWAY_STATION = 89;
    public static final int TYPE_TAXI_STAND = 91;
    public static final int TYPE_TRAIN_STATION = 92;
    public static final int TYPE_ZOO = 96;
    public static final int TELL_A_STORY = 21;

    //Salud,Recreacion,Transporte,Tienda,Establecimiento,Colegio,Juegos,Restaurant,Carniceria,Verduleria,Estadio,Cine,Ma&nacuteana,Mediodia,Tarde,Noche


}
