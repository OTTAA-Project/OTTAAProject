package com.stonefacesoft.ottaa.utils;

import android.util.Log;

import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatter {

    // convert UTF-8 to internal Java String format
    public static String convertUTF8ToISO8859(String s) {
        String out = null;
        out = new String(s.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        return out;
    }

    /**
     * @param s Value
     * @return An encoded utf-8 latam string
     *
     * */
    public static String decodeCharsUTF8(String s) {
        String result = s;
        if(s == null)
            return "";
        try{
            String utf8 = new String(result.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String utf8String = utf8
                    .replace("Ã¡", "\u00E1") // Replace á
                    .replace("Ã©", "\u00E9") // Replace é
                    .replace("Ã­", "\u00ED") // Replace í
                    .replace("Ã³", "\u00F3") // Replace ó
                    .replace("Ãº", "\u00FA") // Replace ú
                    .replace("Ã±", "\u00F1") // Replace ñ
                    .replace("Ã‘", "\u00D1") // Replace Ñ
                    .replace("Ã€", "\u00C0") // Replace À
                    .replace("Ãˆ", "\u00C8") // Replace È
                    .replace("ÃŒ", "\u00CC") // Replace Ì
                    .replace("Ã’", "\u00D2") // Replace Ò
                    .replace("Ã™", "\u00D9") // Replace Ù
                    .replace("Ã¤", "\u00E4") // Replace ä
                    .replace("Ã«", "\u00EB") // Replace ë
                    .replace("Ã¯", "\u00EF") // Replace ï
                    .replace("Ã¶", "\u00F6") // Replace ö
                    .replace("Ã¼", "\u00FC") // Replace ü
                    .replace("Ã„", "\u00C4") // Replace Ä
                    .replace("Ã‹", "\u00CB") // Replace Ë
                    .replace("Ã�", "\u00CF") // Replace Ï
                    .replace("Ã–", "\u00D6") // Replace Ö
                    .replace("Ãœ", "\u00DC"); // Replace Ü
            return utf8String;
        }catch (Exception ex){
            return s;
        }
    }

    public static String decodeCharsUTF16(String s) {
        String result = s;
        result = new String(s.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_16);
        return result;
    }



}
