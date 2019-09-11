package paystation.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the pay station.
 *
 * Responsibilities:
 *
 * 1) Accept payment; 
 * 2) Calculate parking time based on payment; 
 * 3) Know earning, parking time bought; 
 * 4) Issue receipts; 
 * 5) Handle buy and cancel events.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
public class PayStationImpl implements PayStation {
    
    private int insertedSoFar;
    private int timeBought;
    private int totalMoney;

    private final int NICKEL = 5;
    private final int DIME = 10;
    private final int QUARTER = 25;

    //left side is coin value, right is number of coins
    private Map<Integer, Integer> coins;

    public PayStationImpl (){
        insertedSoFar = 0;
        timeBought = 0;
        totalMoney = 0;
        coins = new HashMap<>();
    }


    public int empty() {
        int tempTotalMoney = totalMoney;
        totalMoney = 0;
        return tempTotalMoney;
    }

    @Override
    public void addPayment(int coinValue)
            throws IllegalCoinException {

        switch (coinValue) {
            case NICKEL:
                //check if coin map contains coin value
                if (coins.containsKey(NICKEL)){
                    coins.put(NICKEL, 1);
                }
                else coins.replace(NICKEL, coins.get(NICKEL) + 1);
                break;
            case DIME:
                if (coins.containsKey(DIME)){
                    coins.put(DIME, 1);
                }
                else coins.replace(DIME, coins.get(DIME) + 1);
                break;
            case QUARTER:
                if (coins.containsKey(QUARTER)){
                    coins.put(QUARTER, 1);
                }
                else coins.replace(QUARTER, coins.get(QUARTER) + 1);
                break;
            default:
                throw new IllegalCoinException("Invalid coin: " + coinValue);
        }
        insertedSoFar += coinValue;
        timeBought = insertedSoFar / 5 * 2;
    }

    @Override
    public int readDisplay() {
        return timeBought;
    }

    @Override
    public Receipt buy() {
        totalMoney += insertedSoFar;
        Receipt r = new ReceiptImpl(timeBought);
        reset();
        return r;
    }

    @Override
    public Map<Integer, Integer> cancel(){
        timeBought = insertedSoFar = 0;
        Map<Integer, Integer> toReturn = coins;
        coins = new HashMap<>();
        return toReturn;
    }

    public int getTotalMoney(){
        return totalMoney;
    }
    
    private void reset() {
        timeBought = insertedSoFar = 0;
    }
}
