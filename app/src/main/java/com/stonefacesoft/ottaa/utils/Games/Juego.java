package com.stonefacesoft.ottaa.utils.Games;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.preferences.User;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * <h3>Objetive</h3>
 * Game Object Class
 * <h3>How to declare</h3>
 * <code>Juego game=new Juego(Context,idGame,gameLevel);</code>
 * <h3>Examples of implementation</h3>
 * <h4>How to increment correct answers</h4>
 * <code>game.incrementCorrect();</code>
 * <h4>How to increment wrong andswers</h4>
 * <code>game.incrementWrong();</code>
 * <h4>How to get the Best Streak</h4>
 * <code>game.getBestStreak();</code>
 * <h4>How to get the face</h4>
 * <code>game.devolverCarita();</code>
 * <h4>How to save JsonObject</h4>
 * <code>game.guardarObjetoJson();</code>
 * */
public class Juego {

    private final String TAG = "Juego";
    private int timesRight;

    private final Context mContext;
    private final int game;
    private final int levelId;
    private int gamelevel;
    private CalculaPuntos score;//Todo propiedad puntos , getScore
    private long useTime = 0;
    private int cantEntradas;
    private int correctStreak;
    private int bestStreak;
    private boolean streak;
    private final Reloj chronometer;
    private final Json json;
    private User user;
    private SubirArchivosFirebase subirArchivosFirebase;
    private JSONObject object = new JSONObject();
    private int maxStreak;
    private int maxLevel;

    public static final int VERY_DISSATISFIED = 0;
    public static final int DISSATISFIED = 1;
    public static final int NEUTRAL = 2;
    public static final int SATISFIED = 3;
    public static final int VERY_SSATISFIED = 4;


    private final int[] iconArrayActive = {
            R.drawable.ic_sentiment_very_dissatisfied_white_24dp,
            R.drawable.ic_sentiment_dissatisfied_white_24dp,
            R.drawable.ic_sentiment_neutral_white_24dp,
            R.drawable.ic_sentiment_satisfied_white_24dp,
            R.drawable.ic_sentiment_very_satisfied_white_24dp
    };



   public Juego(Context mContext,int id,int levelId) {
       this.mContext=mContext;
       this.json=Json.getInstance();
       this.json.setmContext(this.mContext);
       this.game = id;
       this.levelId=levelId;
       this.chronometer =new Reloj();
       crearObjetoJson();
       JSONObject score = json.getObjectPuntaje(object);
       if(score!=null){
           try {
               setCalculaPuntos(score.getInt("aciertos"),score.getInt("desaciertos"));
           } catch (JSONException e) {
               e.printStackTrace();
               Log.e(TAG, "Score error" );
               setCalculaPuntos(0,0);
           }
       }else{
           setCalculaPuntos(0,0);
       }
       try {
           chronometer.setLastClock(object.getLong("time_use"));
       } catch (JSONException e) {
           chronometer.setLastClock(0);
       }
       bestStreak=getBestStreak();

   }



   public void createUser(){
       this.user =new User(this.mContext);
   }



    public void setMaxStreak(int maxStreak) {
        this.maxStreak = maxStreak;
    }

    public void setUseTime(long useTime){
       this.useTime+=useTime;
   }

   private void setCalculaPuntos(int correct,int incorrect){
       score =new CalculaPuntos();
       score.setAciertos(correct);
       score.setDesaciertos(incorrect);

   }

   public void startUseTime(){
        chronometer.setHorainicio(System.currentTimeMillis());
   }
   public void endUseTime(){
        chronometer.setHorafin(System.currentTimeMillis());
   }


    //Todo Calcular precision ya lo tenia el puntaje, el juego tiene relacion con el puntaje

    public  void incrementCorrect(){
            correctStreak++;

        if(correctStreak>=bestStreak){
            bestStreak=correctStreak;
            saveBestStreak();
        }
        score.sumarCantidadVecesCorrectas();
   }
   public void incrementWrong(){
        if(correctStreak>=bestStreak){
            bestStreak=correctStreak;
           saveBestStreak();
        }
        correctStreak=0;
        score.sumarCantidVecesIncorretas();
   }

   public void incrementTimesRight(){
       timesRight++;
   }



   public int getCorrectStreak(){
       return correctStreak;
   }



    public int getBestStreak() {
        try {
            if(object.has("best_streak"))
                bestStreak=object.getInt("best_streak");
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return bestStreak;
    }

    public int getGame() {
        return game;
    }

    public Drawable getSmiley() {
       return  mContext.getResources().getDrawable(getSmileyInteger());
    }
    public int getSmileyInteger() {
       float result = score.getResult();
        if(result>=20&& result<40)
            return iconArrayActive[1];
        else if(result>=40&& result<60)
            return iconArrayActive[2];
        else if(result>=60&& result<80)
            return iconArrayActive[3];
        else if(result>=80&& result<=100)
            return iconArrayActive[4];
        else
            return iconArrayActive[0];
    }



    public void saveBestStreak(){
        try {
            if(bestStreak>getRealStreak()) {
                object.put("best_streak", bestStreak);
                Log.d(TAG, "the best streak has been selected ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            /*try {
                object.put("best_streak",bestStreak);
                Log.d(TAG, "The best streak has been created");
            } catch (JSONException e1) {
                Log.e(TAG, "saveBestStreak Error: " + e.getMessage());
            }*/
        }
    }

    private int getRealStreak() throws JSONException {
       int value=0;
        if(object.has("best_streak"))
            value = object.getInt("best_streak");
       return value;
    }

    private void crearObjetoJson(){
        try {
            if(json.getGame(game,levelId)==null){
                object.put("game",game);
                object.put("levelId",levelId);
            }else {
                object=json.getGame(game,levelId);
                saveBestStreak();
            }
        } catch (JSONException e) {
            Log.e(TAG, "crearObjetoJson: Error: " + e.getMessage());
        }
    }

    public JSONObject getObject() {
        return object;
    }

    public CalculaPuntos getScoreClass() {
        return score;
    }

    public float getScore() {
        return score.getScore();
    }

    public Reloj getChronometer() {
        return chronometer;
    }

    public void agregarDatosConsulta(){
        try {
            saveBestStreak();
            object.put("puntaje", score.getPuntaje());
            object.accumulate("Reloj", chronometer.getRelojToJsonObject());
            object.put("time_use", chronometer.getLastUsedTime());
        } catch (JSONException e) {
            Log.e(TAG, "agregarDatosConsulta: Error" + e.getMessage());
        }

    }

    public void uploadFirebaseGameData(){
       if(user == null)
           createUser();
       if(subirArchivosFirebase==null)
           this.subirArchivosFirebase=new SubirArchivosFirebase(this.mContext);
        subirArchivosFirebase.subirJuegos(subirArchivosFirebase.getmDatabase(user.getmAuth(),Constants.JUEGOS),subirArchivosFirebase.getmStorageRef(user.getmAuth(),Constants.JUEGOS));
    }
    public void saveJsonObjects() {
        agregarDatosConsulta();
        json.agregarJuego(object);
        json.guardarJson(Constants.ARCHIVO_JUEGO);
    }

    public Json getJson() {
        return json;
    }


    public int getSmiley(int wantedSmiley) {
        return iconArrayActive[wantedSmiley];
    }

    public void setGamelevel(int gamelevel) {
        this.gamelevel = gamelevel;
    }

    public int getGamelevel() {
        return gamelevel;
    }

    public void changeLevelGame(){
       if(this.gamelevel<maxLevel)
           this.gamelevel++;
    }

    public boolean isChangeLevel(){
        return (timesRight%maxStreak)==0;
    }

    public int getTimesRight() {
        return timesRight;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }
}
