package com.gary;

import com.gary.dice.Dice;
import com.gary.dice.DiceStatus;
import com.gary.strategy.DiceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiceStrategyImpl implements DiceStrategy {

    private static final Logger logger = LoggerFactory.getLogger(DiceStrategyImpl.class);

    @Override
    public String roll(Dice d1, Dice d2) {

        Thread t1 = new Thread(d1);
        Thread t2 = new Thread(d2);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int sum = d1.getUpnum() + d2.getUpnum();
        if(sum>=6){
            return DiceStatus.WIN.toString();
        }
        return DiceStatus.LOSE.toString();
    }

}
