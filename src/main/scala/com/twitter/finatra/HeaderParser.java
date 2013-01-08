package com.twitter.finatra;

import java.util.HashMap;

public class HeaderParser {

    public static HashMap<String, String> getParsedHeaders(String headerPart) {
        final int len = headerPart.length();
        //FileItemHeadersImpl headers = newFileItemHeaders();
        HashMap<String, String> headers = new HashMap<String, String>();
        int start = 0;
        for (;;) {
            int end = parseEndOfLine(headerPart, start);
            if (start == end) {
                break;
            }
            String header = headerPart.substring(start, end);
            start = end + 2;
            while (start < len) {
                int nonWs = start;
                while (nonWs < len) {
                    char c = headerPart.charAt(nonWs);
                    if (c != ' '  &&  c != '\t') {
                        break;
                    }
                    ++nonWs;
                }
                if (nonWs == start) {
                    break;
                }
                // Continuation line found
                end = parseEndOfLine(headerPart, nonWs);
                header += " " + headerPart.substring(nonWs, end);
                start = end + 2;
            }
            parseHeaderLine(headers, header);
        }
        return headers;
    }

    public static int parseEndOfLine(String headerPart, int end) {
        int index = end;
        for (;;) {
            int offset = headerPart.indexOf('\r', index);
            if (offset == -1  ||  offset + 1 >= headerPart.length()) {
                throw new IllegalStateException(
                        "Expected headers to be terminated by an empty line.");
            }
            if (headerPart.charAt(offset + 1) == '\n') {
                return offset;
            }
            index = offset + 1;
        }
    }

    /**
     * Reads the next header line.
     * @param headers String with all headers.
     * @param header Map where to store the current header.
     */
    public static void parseHeaderLine(HashMap<String,String> headers, String header) {
        final int colonOffset = header.indexOf(':');
        if (colonOffset == -1) {
            // This header line is malformed, skip it.
            return;
        }
        String headerName = header.substring(0, colonOffset).trim();
        String headerValue =
                header.substring(header.indexOf(':') + 1).trim();
        headers.put(headerName, headerValue);
    }

}
