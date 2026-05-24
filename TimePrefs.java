package com.beykent.akillitestanaliz;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashMap;
import java.util.Map;

public class TimePrefs {
    private static final String PREFS = "app_prefs";

    public static final String EXAM_TYT = "TYT";
    public static final String EXAM_AYT = "AYT";
    public static final String EXAM_LGS = "LGS";

    // TYT/LGS
    public static final String L_TURKCE = "TURKCE";
    public static final String L_MAT = "MAT";
    public static final String L_SOS = "SOS";
    public static final String L_FEN = "FEN";

    // AYT
    public static final String L_EDEB = "EDEB";
    public static final String L_MAT2 = "MAT2";
    public static final String L_AYT_SOS = "AYT_SOS";
    public static final String L_AYT_FEN = "AYT_FEN";

    private static String key(String exam, String lesson) {
        return "MIN_" + exam + "_" + lesson;
    }

    public static void putMinutes(Context c, String exam, String lesson, int minutes) {
        SharedPreferences sp = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putInt(key(exam, lesson), clamp(minutes)).apply();
    }

    public static int getMinutes(Context c, String exam, String lesson, int def) {
        SharedPreferences sp = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getInt(key(exam, lesson), def);
    }

    public static Map<String, Integer> getLessonMinutes(Context c, String exam) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        if (EXAM_TYT.equals(exam)) {
            map.put(L_TURKCE, getMinutes(c, exam, L_TURKCE, 55));
            map.put(L_MAT,    getMinutes(c, exam, L_MAT,    75));
            map.put(L_SOS,    getMinutes(c, exam, L_SOS,    25));
            map.put(L_FEN,    getMinutes(c, exam, L_FEN,    35));
        } else if (EXAM_AYT.equals(exam)) {
            map.put(L_EDEB,    getMinutes(c, exam, L_EDEB,    60));
            map.put(L_MAT2,    getMinutes(c, exam, L_MAT2,    80));
            map.put(L_AYT_SOS, getMinutes(c, exam, L_AYT_SOS, 40));
            map.put(L_AYT_FEN, getMinutes(c, exam, L_AYT_FEN, 40));
        } else { // LGS
            map.put(L_TURKCE, getMinutes(c, exam, L_TURKCE, 50));
            map.put(L_MAT,    getMinutes(c, exam, L_MAT,    60));
            map.put(L_SOS,    getMinutes(c, exam, L_SOS,    30));
            map.put(L_FEN,    getMinutes(c, exam, L_FEN,    40));
        }

        return map;
    }

    public static int getTotalMinutes(Context c, String exam) {
        int total = 0;
        for (int v : getLessonMinutes(c, exam).values()) total += v;
        return total;
    }

    public static String label(String lessonKey) {
        switch (lessonKey) {
            case L_TURKCE: return "Türkçe";
            case L_MAT: return "Mat";
            case L_SOS: return "Sos";
            case L_FEN: return "Fen";
            case L_EDEB: return "Edebiyat";
            case L_MAT2: return "Mat-2";
            case L_AYT_SOS: return "Sosyal";
            case L_AYT_FEN: return "Fen";
            default: return lessonKey;
        }
    }

    private static int clamp(int v) {
        if (v < 0) return 0;
        if (v > 300) return 300;
        return v;
    }
}
