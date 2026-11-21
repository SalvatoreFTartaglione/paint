package org.example.model.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationUtilsTest {

    @Test
    public void escapeAndUnescapeAreInverse() {
        String raw = "a;b:c\\d\nline2\r";
        assertEquals(raw, SerializationUtils.unescape(SerializationUtils.escape(raw)));
    }

    @Test
    public void escapedStringHasNoRawSeparatorsOrLineBreaks() {
        String escaped = SerializationUtils.escape("x;y:z\nw");
        assertEquals(-1, SerializationUtils.indexOfUnescaped(escaped, ';'));
        assertEquals(-1, SerializationUtils.indexOfUnescaped(escaped, ':'));
        assertFalse(escaped.contains("\n"));
    }

    @Test
    public void nullIsTreatedAsEmpty() {
        assertEquals("", SerializationUtils.escape(null));
        assertEquals("", SerializationUtils.unescape(null));
    }

    @Test
    public void splitIgnoresEscapedSeparators() {
        String line = "Text;" + SerializationUtils.escape("a;b") + ";0xff";
        assertEquals(Arrays.asList("Text", "a\\;b", "0xff"), SerializationUtils.splitUnescaped(line, ';'));
    }

    @Test
    public void splitKeepsEmptyFields() {
        assertEquals(Arrays.asList("a", "", "b", ""), SerializationUtils.splitUnescaped("a;;b;", ';'));
    }

    @Test
    public void plainTextWithoutEscapesIsUnchanged() {
        // Files written before escaping was introduced must still be readable
        assertEquals("hello world", SerializationUtils.unescape("hello world"));
    }

    @Test
    public void indexOfUnescapedSkipsEscapedCharacters() {
        assertEquals(4, SerializationUtils.indexOfUnescaped("a\\:b:c", ':'));
    }
}
