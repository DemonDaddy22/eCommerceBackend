package com.udacity.examples.Testing;

import org.junit.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import static com.udacity.examples.Testing.Helper.getCount;
import static org.junit.Assert.assertEquals;

public class HelperTest {

    @BeforeClass
    public static void setup() { System.out.println("Setup before all the tests");}

    @Before
    public void init() { System.out.println("Runs before each test");}

    @After
    public void teardown() { System.out.println("Runs after each test");}

    @AfterClass
    public static void clearAll() { System.out.println("Clear after all the tests");}

    @Test
    public void test() {
        assertEquals(5, 5);
    }

    @Test
    public void verifyGetCount() {
        List<String> empNames = Arrays.asList("Mark", "Alex", "Claire");
        long count = Helper.getCount(empNames);
        assertEquals(empNames.size(), count);
    }

    @Test
    public void verifyGetStats() {
        List<Integer> yearsOfExp = Arrays.asList(4, 2, 10, 7, 8, 5, 12, 3);
        IntSummaryStatistics stats = Helper.getStats(yearsOfExp);
        assertEquals(Collections.max(yearsOfExp), Integer.valueOf(stats.getMax()));
    }

    @Test
    public void verifySquareList() {
        List<Integer> yearsOfExp = Arrays.asList(4, 2, 10, 7, 8, 5, 12, 3);
        List<Integer> squares = Helper.getSquareList(yearsOfExp);
        assertEquals(yearsOfExp.stream().map(year -> year * year).collect(Collectors.toList()), squares);
    }
}
