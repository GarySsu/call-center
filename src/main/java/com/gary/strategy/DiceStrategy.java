package com.gary.strategy;

/**
 * Models different strategies on which is dice play
 */
public interface DiceStrategy {

    /**
     * Roll two dices. When sum is greater than six, lead to next level
     *
     * @return
     */
    public String roll();

}
