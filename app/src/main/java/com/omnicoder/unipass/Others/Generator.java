package com.omnicoder.unipass.Others;

import android.util.Log;

import java.security.SecureRandom;
import java.util.Random;

public class Generator {
    private final Random random = new SecureRandom();
    private final String characters;
    private final char[] buffer;
    private int length;

    public Generator(String characters, int length){
        this.characters= characters;

        this.length= length;
        buffer= new char[length];
    }
    public String generate() {
        for (int i = 0; i < length; ++i) {
            buffer[i] = characters.charAt(random.nextInt(characters.length()));


        }
        return new String(buffer);
    }

}


