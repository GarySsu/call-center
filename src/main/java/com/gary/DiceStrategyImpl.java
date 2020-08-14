package com.gary;

import com.gary.dice.Dice;
import com.gary.dice.DiceStatus;
import com.gary.strategy.DiceStrategy;

public class DiceStrategyImpl implements DiceStrategy {

    private Dice d1,d2;

    @Override
    public String roll() {

        d1 = new Dice();
        d2 = new Dice();

        Thread t1 = new Thread(d1);
        Thread t2 = new Thread(d2);

        t1.start();
        t2.start();

        int sum = d1.getUpnum() + d2.getUpnum();
        if(sum>=6){
            return DiceStatus.WIN.toString();
        }
        return DiceStatus.LOSE.toString();
    }

}
