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

    private int previousValue;
    private final int NICKEL = 5;
    private final int DIME = 10;
    private final int QUARTER = 25;

    //left side is coin value, right is number of coins
    private Map<Integer, Integer> coins;
    private Map<Integer, Integer> tempCoins;

    public PayStationImpl (){
        insertedSoFar = 0;
        timeBought = 0;
        totalMoney = 0;
        coins = new HashMap<>();
        //initializes hashmap, and per the rules, the map can't be null
        coins.put(NICKEL, 0);
        coins.put(DIME, 0);
        coins.put(QUARTER, 0);
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
                //gets current value of key for particular coin
                previousValue = coins.get(NICKEL);
                //adds one to number of particular coin added
                coins.replace(NICKEL, previousValue+1);
                break;
            case DIME:
                previousValue = coins.get(DIME);
                coins.replace(DIME, previousValue+1);
                break;
            case QUARTER:
                previousValue = coins.get(QUARTER);
                coins.replace(QUARTER, previousValue+1);
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
        tempCoins = new HashMap<>(coins);
        //tempCoins = coins; //just did not set the tempCoins correctly
        //since we can't have a key for a coin not entered, we need to remove that from the map
        if (tempCoins.get(NICKEL) == 0) {
            tempCoins.remove(NICKEL);
        }
        if (tempCoins.get(DIME) == 0) {
            tempCoins.remove(DIME);
        }
        if (tempCoins.get(QUARTER) == 0) {
            tempCoins.remove(QUARTER);
        }
        coins.replace(NICKEL, 0);
        coins.replace(DIME, 0);
        coins.replace(QUARTER, 0);
        //changed to tempCoins because shouldClearAfterCancel was failing
        return tempCoins;
    }

    public int getTotalMoney(){
        return totalMoney;
    }
    
    private void reset() {
        timeBought = insertedSoFar = 0;
    }
}
