package com.stonefacesoft.ottaa;

import android.util.Log;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.JSONutils;

import org.json.JSONException;
import org.json.JSONObject;

import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 * Created by hecto on 20/5/2016.
 */
public class NLG {

    Lexicon lexicon ;
    NLGFactory nlgFactory ;
    Realiser realiser ;

    //Flags para armar la frase
    private boolean HayVerbo;
    private boolean HayObjeto;
    private boolean HayPasado;
    private boolean HayFuturo;
    private boolean HayProgressive;
    private boolean HaySocial;
    private boolean HaySujeto1;
    private boolean HaySujeto2;
    private boolean HayObjeto1;
    private boolean HayObjeto2;
    private String FraseSocial;

    private boolean CargarSujeto;
    private boolean CargaObjeto;
    private NPPhraseSpec Subject1;
    private NPPhraseSpec Subject2;
    private NPPhraseSpec Subject3;
    private VPPhraseSpec Verbo1;
    private NPPhraseSpec Object1;
    private NPPhraseSpec Object2;

    private static final String TAG = "NLG";

    public NLG(){
        this.lexicon = Lexicon.getDefaultLexicon();
        this.nlgFactory = new NLGFactory(lexicon);
        this.realiser = new Realiser(lexicon);
    }

    public void NuevaFrase(){
        HayVerbo = false;
        HayObjeto = false;
        HayPasado = false;
        HayFuturo = false;
        HayProgressive = false;
        HaySujeto1 = false;
        HayObjeto1 = false;
        HaySocial = false;
        FraseSocial = "";
        Subject1 = null;
        Subject2 = null;
        Verbo1 = null;
        Object1 = null;
        Object2 = null;

        CargaObjeto = false;
        CargarSujeto = false;
    }


    //Se carga cada palabra y se analiza que parte forma de una oracion simple, y se prepara para ser
    // procesada por el NLG, cuando se necesite la frase lista se llama a ArmarFrase que la frase lista.
    public boolean CargarFrase (JSONObject opcion, int tipo) {
        String pictogramName = JSONutils.getNombre(opcion,"en");
            if (opcion == null) {
                return false;
            }
            switch (tipo) {
                case 3:
                    Log.d(TAG, "CargarFrase: Green 'Actions'");
                    if (HayVerbo) {
                        //Significa que hay q poner el to o algo asi
                        processPhraseTipe3( pictogramName);

                    } else {
                        Log.d(TAG, "CargarFrase: No hay verbo");
                        HayVerbo = true;
                        Verbo1 =  createVerbPhrase("",pictogramName);
                    }
                    return true;
                case 1:
                    Log.d(TAG, "CargarFrase: Yellow 'Subject'");
                    Log.d(TAG, "CargarFrase: Opcion: " + opcion);

                        if (HayVerbo) {
                            HayObjeto = true;
                        //Add objeto
                            if (HayObjeto1) {
                                Object2 = nlgFactory.createNounPhrase(pictogramName);
                                HayObjeto2 = true;
                            } else {
                                Object1 = nlgFactory.createNounPhrase(pictogramName);
                                HayObjeto1 = true;
                            }
                        } else {
                            //Add sujeto
                            if (HaySujeto1) {
                                Subject2 = nlgFactory.createNounPhrase(pictogramName);
                                CargarSujeto = true;
                            } else {
                                Subject1 = nlgFactory.createNounPhrase(pictogramName);
                                HaySujeto1 = true;
                                CargarSujeto = true;
                            }
                        }
                        return true;
                    case 2:
                        Log.d(TAG, "CargarFrase: Orange 'Object'");
                        if (HayVerbo) {
                            VPPhraseSpec verbo2 = nlgFactory.createVerbPhrase(pictogramName);

                            HayObjeto = true;
                            //Add objeto
                            if (HayObjeto1) {
                                Object2 = nlgFactory.createNounPhrase(pictogramName);
                                Object2.setDeterminer("a");
                            } else {
                                Object1 = nlgFactory.createNounPhrase(pictogramName);
                                Object1.setDeterminer("a");
                                HayObjeto1 = true;
                                CargaObjeto = true;
                            }
                        } else {
                            //Add sujeto
                            if (HaySujeto1) {
                                Subject2 = nlgFactory.createNounPhrase(pictogramName);
                                Subject2.setDeterminer("the");
                            } else {
                                Subject1 = nlgFactory.createNounPhrase(pictogramName);
                                Subject1.setDeterminer("the");
                                HaySujeto1 = true;
                                CargarSujeto = true;
                            }
                        }
                        return true;
                    case 4:
                        Log.d(TAG, "CargarFrase: Blue 'Adjective'");
                        if (HayVerbo) {
                            if (HayObjeto) {
                                //Add modifier al objeto
                                if(Object1!=null)
                                    addModifierObject(Object1,HayObjeto1,pictogramName);
                                if(Object2!=null)
                                    addModifierObject(Object2,HayObjeto2,pictogramName);

                            } else {
                                //Add modifier al verbo
                                Verbo1.addModifier(pictogramName);
                            }
                        } else {
                            //TODO HECTOR no funcion bien con unfriendly o confused, hay q agregar
                        // entradas al lexicon
                            //Add modifier al sujeto
                            if (HaySujeto1 && Subject2 != null)
                                Subject2.addModifier(pictogramName);
                            else {
                                //By shadaf
                                if (Subject1 == null) {
                                    Subject1 = nlgFactory.createNounPhrase(pictogramName);

                                    Subject1.setDeterminer("the");
                                    HaySujeto1 = true;
                                    CargarSujeto = true;

                                } else {
                                    Subject1.addModifier(pictogramName);
                                }
                            }
                        }
                        return true;
                    case 5:
                        Log.d(TAG, "CargarFrase: Violet 'Social Interaction'");
                        HaySocial = true;
                        FraseSocial = FraseSocial + " " + pictogramName;
                        return true;
                    case 6:
                        Log.d(TAG, "CargarFrase: Black 'Miscellanea'");
                        checkTimePhrase(pictogramName);
                        return true;
                    default:
                        Log.e(TAG, "CargarFrase: Error");
                        return false;
                }

    }

    public String ArmarFrase(){
        String StringFrase;
        SPhraseSpec frase = nlgFactory.createClause();
        if (CargarSujeto) {
            if (HaySujeto1 && Subject2!=null) {
                CoordinatedPhraseElement subj = nlgFactory.createCoordinatedPhrase(Subject1, Subject2);
                frase.setSubject(subj);
                Log.d(TAG, "ArmarFrase: Sujeto 2: " + subj.toString());
            } else {
                frase.setSubject(Subject1);
                Log.e(TAG, "ArmarFrase: Sujeto: " + Subject1.toString());
            }
            CargarSujeto = false;
        }
        if (CargaObjeto && !CargarSujeto) {
            if (HayObjeto1 && Object2 != null) {
                CoordinatedPhraseElement obj = nlgFactory.createCoordinatedPhrase(Object1, Object2);
                frase.setObject(obj);
                Log.d(TAG, "ArmarFrase: Objeto 2: " + obj.toString());
            } else {
                frase.setObject(Object1);
                Log.d(TAG, "ArmarFrase: Objeto: " + Object1.toString());
            }
        }
        frase.setVerb(Verbo1);
        if (HayFuturo)
            frase.setFeature(Feature.TENSE, Tense.FUTURE);
        if (HayPasado)
            frase.setFeature(Feature.TENSE, Tense.PAST);
        if (HayProgressive)
            frase.setFeature(Feature.PROGRESSIVE, true);
        if (HaySocial) {
            NLGElement Frase = nlgFactory.createSentence(FraseSocial);
            FraseSocial = realiser.realiseSentence(Frase);
        }
        //TODO Agregar el sujeto tacito para que no falle.
        StringFrase = realiser.realiseSentence(frase);
        Log.d(TAG, "ArmarFrase: realiser: " + StringFrase);

        return FraseSocial + StringFrase;
    }

    public void processPhraseTipe3(String phrase){
        VPPhraseSpec verbo2 = createVerbPhrase("",phrase);
        switch (Verbo1.getVerb().toString()) {
            case "WordElement[want:VERB]":
                Log.d(TAG, "CargarFrase: Verbo: WANT");
                Verbo1 = createVerbPhrase("want to ", phrase);
                break;
            case "WordElement[need:VERB]":
                Log.d(TAG, "CargarFrase: Verbo: NEED");
                Verbo1 = createVerbPhrase("need to ",phrase);
                break;
            case "WordElement[go:VERB]":
                if (verbo2.getVerb().toString().equals("WordElement[walk:VERB]") ||
                        verbo2.getVerb().toString().equals("WordElement[swim:VERB]") ||
                        verbo2.getVerb().toString().equals("WordElement[drive:VERB]")) {
                    Verbo1 = createVerbPhrase("go for a ",phrase);
                    break;
                } else if (verbo2.getVerb().toString().equals("WordElement[shop:VERB]") ||
                        verbo2.getVerb().toString().equals("WordElement[camp:VERB]") ||
                        verbo2.getVerb().toString().equals("WordElement[dance:VERB]") ||
                        verbo2.getVerb().toString().equals("WordElement[fish:VERB]") ||
                        verbo2.getVerb().toString().equals("WordElement[sail:VERB]")) {
                    verbo2.setFeature(Feature.PROGRESSIVE, true);
                    //TODO HECTOR Ver como solucionar esto
                    Verbo1 = createVerbPhrase("go ",phrase);;
                    break;
                } else {

                    Verbo1 = createVerbPhrase("go to ",phrase);

                    break;
                }
            case "WordElement[have:VERB]":
                Log.d(TAG, "CargarFrase: Verbo: HAVE");
                Verbo1 = createVerbPhrase("have to " ,phrase);
                break;
            case "WordElement[be:VERB]":
                Log.d(TAG, "CargarFrase: Verbo: BE");
                Verbo1 = createVerbPhrase("",phrase);
                HayProgressive = true;
                break;
            default:
                Verbo1.addModifier(phrase);
                break;
        }
    }

    public VPPhraseSpec createVerbPhrase(String word,String phrase){
        VPPhraseSpec aux = nlgFactory.createVerbPhrase(word+phrase);
        return aux;
    }

    public void checkTimePhrase(String value){
        if (value.toLowerCase().equals("yesterday") ||value.toLowerCase().equals("before"))
            HayPasado = true;
        if (value.toLowerCase().equals("tomorrow") ||value.toLowerCase().equals("after"))
            HayFuturo = true;
    }

    public void addModifierObject(NPPhraseSpec mObject,boolean value,String pictogramName){
        if (value){
            mObject.addModifier(pictogramName);
        }
        else
            mObject.addModifier(pictogramName);
    }
}
