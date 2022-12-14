package com.tangeriness.planetarium.util;

import net.minecraft.util.math.random.Random;

public class NameUtils {
    private static final String[] SYLLABLES = new String[] { "stel", "la", "stel", "lae", "lo", "cus", "cae", "lest", "is", "cae", "lum", "cla", "ra", "crus", "ru", "ti", "lans", "ful", "gent", "pa", "tet", "punc", "tum", "fin", "is", "lu", "ceat", "so", "lar", "is", "sol", "is", "lu", "na", "ka", "len", "dis", "nox", "ves", "peri", "mane", "nu", "bes", "nu", "bib", "us", "mag", "na", "mi", "ni", "ma", "ful", "gore", "te", "ne", "bris", "lux",  "po", "lus", "po", "lar", "is", "naj", "ma", "al", "nu", "jum", "al", "fa", "da", "al", "sa", "ma", "wi", "sa", "ma", "mash", "riq", "la", "mie", "ya", "shie", "bu", "sis", "sa", "fi", "nuq", "ta", "ni", "ha", "ya", "yal", "mae", "sham", "si", "al", "shams", "al", "qa", "mar", "aq", "mar", "layl", "ak", "hir",  "al", "na", "har", "sa", "bah", "ghym", "sa", "hab", "ray", "iea", "sag", "hir",  "al", "hajm", "ai", "nbi", "har", "muz", "lim", "kha", "fi", "fa", "ea", "muwd", "qut", "bi"
    };
    public static String generateStarName(Random random) {
        int count = 2;
        if (random.nextFloat() < 0.1) {
            count--;
        }
        while (random.nextFloat() < 0.8 && random.nextInt(count) == 0) {
            count++;
        }

        String name = "";
        for (int i = 0; i < count; i++) {
            name = name + SYLLABLES[random.nextInt(SYLLABLES.length)];
        }
        return (Character.toUpperCase(name.charAt(0)) + name.substring(1));
    }
}
