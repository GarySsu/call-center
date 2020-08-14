package com.gary.dice;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Models the Dice Domain Objects
 */
@Data
public class Dice implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Dice.class);
    private int upnum;

    @Override
    public void run() {
        // Suppose the dice moves 10 times
        int count = 10;
        while (count > 0) {
            upnum = new Random().nextInt(6) + 1;
            logger.info("Dice " + Thread.currentThread().getName() + " get every upnum " + upnum);

            count--;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
