package ru.hse.java.team;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.lang.Math.PI;
import static java.lang.StrictMath.abs;
import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static ru.hse.java.team.SecondPartTasks.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        var array = new ArrayList<String>();
        array.add("aba");
        array.add("caba");
        array.add("abbb");
        array.add("abba");

        var result1 = new ArrayList<String>();
        result1.add("aba");
        result1.add("caba");

        assertEquals(result1, findQuotes(array, "aba"));
        assertEquals(new ArrayList<String>(),findQuotes(array, "bbbb"));
    }

    @Test
    public void testPiDividedBy4() {
        assertTrue(abs(piDividedBy4() - PI / 4) <= 1e-2);
    }

    @Test
    public void testFindPrinter() {
        var map = Map.ofEntries(
                entry("a", Arrays.asList("sup1", "sup2", "sup3")),
                entry("b", Arrays.asList("sup12", "sup2", "sup3")),
                entry("c", Arrays.asList("sup1", "sup2", "s22"))
        );
        assertEquals("b", findPrinter(map));

        map = Map.ofEntries(
                entry("a", Arrays.asList("sup1", "sup2", "sup3")),
                entry("b", Arrays.asList("sup12", "sup2", "sup3")),
                entry("c", Arrays.asList("sup10000", "sup2", "s22"))
        );
        assertEquals("c", findPrinter(map));
    }

    @Test
    public void testCalculateGlobalOrder() {
        var askOne = ImmutableMap.<String, Integer>builder().put("a", 1).put("b", 2).build();
        var askTwo = ImmutableMap.<String, Integer>builder().put("b", 3).put("c", 2).build();
        var askThree = ImmutableMap.<String, Integer>builder().put("a", 5).put("A", 2).build();
        var expected = ImmutableMap.<String, Integer>builder().put("a", 6).put("A", 2).put("b", 5).put("c", 2).build();
        assertEquals(expected, calculateGlobalOrder(List.of(askOne, askTwo, askThree)));
    }
}