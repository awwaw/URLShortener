package com.rungroop.web.utils;

public class ShorteningUtil {
    public static final String ALPHABET = "Mheo9PI2qNs5Zpf80TBn7lmRbtQ4YKXHvwAEWxuzdra316OJigGLSVUCyFjkDc";
    public static final int BASE = ALPHABET.length();

    public static String idToStr(Long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.insert(0, ALPHABET.charAt((int) (num % BASE)));
            num /= BASE;
        }
        return sb.toString();
    }

    public static Long strToId(String str) {
        long num = 0L;
        for (int i = 0; i < str.length(); i++) {
            num *= BASE;
            num += ALPHABET.indexOf(str.charAt(i));
        }
        return num;
    }
}
