/**
 * Testcases for the Pay Station system.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
package paystation.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PayStationImplTest {

    PayStation ps;

    @Before
    public void setup() {
        ps = new PayStationImpl();
    }

    /**
     * Entering 5 cents should make the display report 2 minutes parking time.
     */
    @Test
    public void shouldDisplay2MinFor5Cents()
            throws IllegalCoinException {
        ps.addPayment(5);
        assertEquals("Should display 2 min for 5 cents",
                2, ps.readDisplay());
    }

    /**
     * Entering 25 cents should make the display report 10 minutes parking time.
     */
    @Test
    public void shouldDisplay10MinFor25Cents() throws IllegalCoinException {
        ps.addPayment(25);
        assertEquals("Should display 10 min for 25 cents",
                10, ps.readDisplay());
    }

    /**
     * Verify that illegal coin values are rejected.
     */
    @Test(expected = IllegalCoinException.class)
    public void shouldRejectIllegalCoin() throws IllegalCoinException {
        ps.addPayment(17);
    }

    /**
     * Entering 10 and 25 cents should be valid and return 14 minutes parking
     */
    @Test
    public void shouldDisplay14MinFor10And25Cents()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Should display 14 min for 10+25 cents",
                14, ps.readDisplay());
    }

    /**
     * Buy should return a valid receipt of the proper amount of parking time
     */
    @Test
    public void shouldReturnCorrectReceiptWhenBuy()
            throws IllegalCoinException {
        ps.addPayment(5);
        ps.addPayment(10);
        ps.addPayment(25);
        Receipt receipt;
        receipt = ps.buy();
        assertNotNull("Receipt reference cannot be null",
                receipt);
        assertEquals("Receipt value must be 16 min.",
                16, receipt.value());
    }

    /**
     * Buy for 100 cents and verify the receipt
     */
    @Test
    public void shouldReturnReceiptWhenBuy100c()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(25);
        ps.addPayment(25);

        Receipt receipt;
        receipt = ps.buy();
        assertEquals(40, receipt.value());
    }

    /**
     * Make sure empty() returns the total amount of money entered from successfull transactions
     * @throws IllegalCoinException
     */
    @Test
    public void emptyShouldReturnTotalMoney() throws IllegalCoinException {
        ps.addPayment(25);
        ps.addPayment(10);
        ps.addPayment(5);
        ps.buy();
        ps.addPayment(25);
        ps.addPayment(25);
        ps.buy();
        assertEquals("Amount emptied should equal amount added", 90, ps.empty());
    }


    /**
     * Canceled entry does not add to the amount returned by empty.
     * @throws IllegalCoinException
     */
    @Test
    public void canceledEntryDoesNotAddAmount() throws IllegalCoinException {
        ps.addPayment(25);
        ps.addPayment(10);
        ps.addPayment(5);
        ps.buy();
        ps.addPayment(25);
        ps.addPayment(25);
        ps.cancel();
        assertEquals("Amount emptied should be equal to total before first buy", 40, ps.empty());
    }

    /**
    Verify Call to empty resets the total to zero
     */
    @Test
    public void emptyShouldResetToZero() throws IllegalCoinException{
        ps.addPayment(25);
        ps.addPayment(10);
        ps.buy();
        ps.empty();
        assertEquals("TotalMoney should be zero after emptying", 0, ps.getTotalMoney());
    }

    /**
     * Call to cancel returns a map containing one coin entered.
     * Add one coin and return that coin map
     */
    @Test
    public void cancelShouldReturnOneCoin() throws IllegalCoinException {
        ps.addPayment(10);
        Map<Integer, Integer> tempCoin = new HashMap<>();
        tempCoin.put(10, 1);
        assertEquals("Should return map of one dime entered", tempCoin, ps.cancel());
    }

    /**
     * Verify that the pay station is cleared after a buy scenario
     */
    @Test
    public void shouldClearAfterBuy()
            throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy(); // I do not care about the result
        // verify that the display reads 0
        assertEquals("Display should have been cleared",
                0, ps.readDisplay());
        // verify that a following buy scenario behaves properly
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Next add payment should display correct time",
                14, ps.readDisplay());
        Receipt r = ps.buy();
        assertEquals("Next buy should return valid receipt",
                14, r.value());
        assertEquals("Again, display should be cleared",
                0, ps.readDisplay());
    }

    /**
     * Verify that cancel clears the pay station
     */
    @Test
    public void shouldClearAfterCancel()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.cancel();
        assertEquals("Cancel should clear display",
                0, ps.readDisplay());
        ps.addPayment(25);
        assertEquals("Insert after cancel should work",
                10, ps.readDisplay());
    }

    /**
     * Verify that cancel doesn't return a null
     */
    @Test
    public void coinMapShouldntBeNull(){
        assertNotNull("Coin map should not be null", ps.cancel());
    }

    /**
     * Verify that cancel() returns correct coins (2x10 and 5, not a single quarter
     */
    @Test
    public void cancelShouldReturnCoins() throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(5);
        Map<Integer, Integer> coinMap = ps.cancel();
        assertEquals("Should return 2 dimes", Integer.valueOf(2), coinMap.get(10)); //use Integer to resolve "ambiguous method call"
        assertEquals("Should return 1 nickel", Integer.valueOf(1), coinMap.get(5));
    }

    /**
     * Call to cancel returns a map that does not contain a key for a coin not entered.
     */
    @Test
    public void shouldReturnMapNoKeyNotEntered() throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(25);
        Map<Integer, Integer> coinMap = ps.cancel();
        assertEquals("Should return 1 dime", Integer.valueOf(1), coinMap.get(10)); //use Integer to resolve "ambiguous method call"
        assertEquals("Should return 1 quarter", Integer.valueOf(1), coinMap.get(25));
        assertTrue("Should not contain any nickels", ! coinMap.containsKey(5));
    }

    /**
     * Call to buy clears coin map
     * Call cancel after buy to show that the map is empty
     */
    @Test
    public void buyShouldClearMap() throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(25);
        ps.buy();
        assertEquals("Call cancel, should return empty map", 0, ps.cancel().size());
    }
}
