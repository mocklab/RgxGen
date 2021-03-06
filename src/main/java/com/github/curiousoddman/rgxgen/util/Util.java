package com.github.curiousoddman.rgxgen.util;

/* **************************************************************************
   Copyright 2019 Vladislavs Varslavans

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
/* **************************************************************************/

import com.github.curiousoddman.rgxgen.generator.nodes.SymbolSet;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Utility methods collection
 */
public final class Util {
    private static final String SYMBOLS = Arrays.stream(SymbolSet.getAllSymbols())
                                                .reduce("", String::concat);

    private static final Pattern  EMPTY                    = Pattern.compile("");
    public static final  String[] ZERO_LENGTH_STRING_ARRAY = new String[0];

    /**
     * Splits string into array of single-character strings
     *
     * @param str string to split
     * @return array of single-character strings
     */
    public static String[] stringToCharsSubstrings(String str) {
        return EMPTY.split(str);
    }

    /**
     * Creates random string up to 10 symbols long
     *
     * @param rnd   random to be used
     * @param value seed used to select length
     * @return random string up to 10 symbols long
     */
    public static String randomString(Random rnd, String value) {
        int count = Math.abs(value.hashCode() % 10);
        StringBuilder builder = new StringBuilder(count);
        while (count >= 0) {
            builder.append(SYMBOLS.charAt(rnd.nextInt(SYMBOLS.length())));
            --count;
        }
        return builder.toString();
    }

    /**
     * Repeats text multiple times
     *
     * @param c     character to repeat
     * @param times number of times. Values less or equal to zero will result in empty string
     * @return text repeated multiple times
     */
    public static String multiplicate(char c, int times) {
        if (times < 0) {
            return "";
        }

        char[] result = new char[times];
        Arrays.fill(result, c);

        return new String(result);
    }

    /**
     * Utility class can't be instantiated
     */
    private Util() {
    }
}
