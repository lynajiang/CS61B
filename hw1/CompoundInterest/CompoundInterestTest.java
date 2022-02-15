import static org.junit.Assert.*;

import com.google.common.collect.ComputationException;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {

        assertEquals(CompoundInterest.numYears(2032), 11);
        assertEquals(CompoundInterest.numYears(2035), 14);
        assertEquals(CompoundInterest.numYears(2060), 39);
        assertEquals(CompoundInterest.numYears(2021), 0);
        assertEquals(CompoundInterest.numYears(2022), 1);
        assertEquals(CompoundInterest.numYears(2121), 100);

    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValue(100, 8,2031), 215.89, tolerance);
        assertEquals(CompoundInterest.futureValue(200,15,2050), 11515.09, tolerance);
        assertEquals(CompoundInterest.futureValue(10,12,2023), 12.544, tolerance);
        assertEquals(CompoundInterest.futureValue(200,-8,2050), 17.818, tolerance);
    }

    @Test
    public void testFutureValueReal() {

        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValueReal(10, 12, 2023, 3), 11.8026496, tolerance);
        assertEquals(CompoundInterest.futureValueReal(100,8,2031, 5), 129.26, tolerance);
        assertEquals(CompoundInterest.futureValueReal(200,-8,2050, 5), 4.025, tolerance);
        assertEquals(CompoundInterest.futureValueReal(1000000, 0, 2061, 3), 295712.29, tolerance);


    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.totalSavings(5000, CompoundInterest.THIS_YEAR + 2, 10), 16550, tolerance);
        assertEquals(CompoundInterest.totalSavings(4000, CompoundInterest.THIS_YEAR + 3, 20), 21472, tolerance);

    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.totalSavingsReal(5000, CompoundInterest.THIS_YEAR + 2, 10, 3), 15572, tolerance);
        assertEquals(CompoundInterest.totalSavingsReal(5000, CompoundInterest.THIS_YEAR + 2, 10, 5), 14936, tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
