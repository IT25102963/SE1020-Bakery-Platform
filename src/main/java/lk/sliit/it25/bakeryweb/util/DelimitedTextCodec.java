package lk.sliit.it25.bakeryweb.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple pipe-delimited text encoding with backslash escapes.
 * Supports: \\  \|  \n  \r  \t
 */
public final class DelimitedTextCodec {
    private DelimitedTextCodec() {}

    public static String encodePipeDelimited(List<String> fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) sb.append('|');
            sb.append(escape(fields.get(i)));
        }
        return sb.toString();
    }

    public static List<String> decodePipeDelimited(String row) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();

        boolean esc = false;
        for (int i = 0; i < row.length(); i++) {
            char ch = row.charAt(i);
            if (esc) {
                switch (ch) {
                    case '\\' -> cur.append('\\');
                    case '|' -> cur.append('|');
                    case 'n' -> cur.append('\n');
                    case 'r' -> cur.append('\r');
                    case 't' -> cur.append('\t');
                    default -> cur.append(ch);
                }
                esc = false;
                continue;
            }

            if (ch == '\\') {
                esc = true;
                continue;
            }
            if (ch == '|') {
                out.add(cur.toString());
                cur.setLength(0);
                continue;
            }
            cur.append(ch);
        }
        out.add(cur.toString());
        return out;
    }

    private static String escape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '\\' -> sb.append("\\\\");
                case '|' -> sb.append("\\|");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> sb.append(ch);
            }
        }
        return sb.toString();
    }
}

