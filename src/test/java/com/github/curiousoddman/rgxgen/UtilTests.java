package com.github.curiousoddman.rgxgen;

import com.github.curiousoddman.rgxgen.util.Util;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UtilTests {

    @Test
    public void splitTest() {
        String[] abcs = Util.stringToCharsSubstrings("abc");
        assertArrayEquals(new String[]{"a", "b", "c"}, abcs);
    }

    @Test
    public void powTest() {
        List<Integer[]> data = Arrays.asList(
                new Integer[]{10, 0, 1},
                new Integer[]{1, 1, 1},
                new Integer[]{1, 2, 1},
                new Integer[]{1, 3, 1},
                new Integer[]{2, 2, 4},
                new Integer[]{2, 10, 1024}
        );

        for (Object[] datum : data) {
            assertEquals(datum[2], (int) Util.pow((int) datum[0], (int) datum[1]));
        }
    }

    @Test
    public void substringUntilTest() {
        List<Object[]> data = Arrays.asList(
                new Object[]{"()", 1, ')', ""},
                new Object[]{"(a)", 1, ')', "a"},
                new Object[]{"(a\\)b)", 1, ')', "a\\)b"},
                new Object[]{"(ac\\\\)", 1, ')', "ac\\\\"}
        );

        for (Object[] datum : data) {
            assertEquals(Arrays.toString(datum), datum[3], Util.substringUntil(datum[0].toString(), (int) datum[1], (char) datum[2]));
        }
    }
}
