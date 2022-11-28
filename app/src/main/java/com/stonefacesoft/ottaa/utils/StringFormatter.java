package com.stonefacesoft.ottaa.utils;

import android.util.Log;

import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    // convert internal Java String format to UTF-8
    public static String decodeCharsUTF8(String s) {
        String result = s;
        result = new String(s.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return result;
    }

    public static String decodeCharsUTF16(String s) {
        String result = s;
        result = new String(s.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_16);
        return result;
    }



}
