package com.example.vyyom.activevoyce;

import java.util.Random;

/**
 * Created by Vyyom on 1/4/2018.
 *
 * Generates  and validates random numbers.
 */

class Randomizer {

    static int getRandom(int size) {
        Random random = new Random();
        int n = random.nextInt(size);
        while(n < 0) {
            n = random.nextInt(size);
        }
        return n;
    }
}
