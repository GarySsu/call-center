package com.gary.strategy;

import com.gary.dice.Dice;

/**
 * Models different strategies on which is dice play
 */
public interface DiceStrategy {

    /**
     * Roll two dices. When sum is greater than six, lead to next level
     *
     * @return
     */
    public String roll(Dice d1, Dice d2);

}
