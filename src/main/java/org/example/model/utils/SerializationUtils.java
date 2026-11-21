package org.example.model.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Helpers for the plain-text file formats used to persist drawings and shape libraries.
 * <p>
 * Fields are separated by {@code ;} (and library entries by {@code :}), so any user-provided
 * string (text shapes, favorite names) must be escaped before being written and unescaped
 * after being read. The escape character is the backslash; line breaks are written as
 * {@code \n} / {@code \r} so that every record always fits on a single line.
 */
public final class SerializationUtils {

    private static final char ESCAPE = '\\';

    private SerializationUtils() {
    }

    /**
     * Escapes the characters that have a special meaning in the file formats
     * ({@code \}, {@code ;}, {@code :}, line breaks).
     *
     * @param value the raw string, may be {@code null}
     * @return the escaped string, or an empty string when {@code value} is {@code null}
     */
    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(value.length() + 8);
        for (char c : value.toCharArray()) {
            switch (c) {
                case ESCAPE:
                case ';':
                case ':':
                    sb.append(ESCAPE).append(c);
                    break;
                case '\n':
                    sb.append(ESCAPE).append('n');
                    break;
                case '\r':
                    sb.append(ESCAPE).append('r');
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Reverts {@link #escape(String)}. Sequences that are not valid escapes are kept as they are,
     * which keeps files written before escaping was introduced readable.
     *
     * @param value the escaped string, may be {@code null}
     * @return the original string, or an empty string when {@code value} is {@code null}
     */
    public static String unescape(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == ESCAPE && i + 1 < value.length()) {
                char next = value.charAt(++i);
                switch (next) {
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    default:
                        sb.append(next);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Splits a string on every occurrence of {@code separator} that is not escaped.
     * The returned fields are <b>not</b> unescaped; call {@link #unescape(String)} on those
     * that may contain user-provided text.
     *
     * @param value     the string to split
     * @param separator the separator character
     * @return the raw fields, including empty ones
     */
    public static List<String> splitUnescaped(String value, char separator) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == ESCAPE && i + 1 < value.length()) {
                current.append(c).append(value.charAt(++i));
            } else if (c == separator) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields;
    }

    /**
     * Finds the first occurrence of {@code target} that is not escaped.
     *
     * @param value  the string to search
     * @param target the character to look for
     * @return the index of the first unescaped occurrence, or {@code -1} if there is none
     */
    public static int indexOfUnescaped(String value, char target) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == ESCAPE) {
                i++;
            } else if (c == target) {
                return i;
            }
        }
        return -1;
    }
}
