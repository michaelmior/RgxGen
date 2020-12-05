package com.github.curiousoddman.rgxgen.nodes;

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

import com.github.curiousoddman.rgxgen.visitors.NodeVisitor;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.curiousoddman.rgxgen.util.Util.ZERO_LENGTH_CHARACTER_ARRAY;

/**
 * Generate Any printable character.
 */
public class SymbolSet extends Node {
    private static final int SPACE_ASCII_CODE = 32;     // First printable character in ASCII table
    private static final int DEL_ASCII_CODE   = 127;    // Bound for printable characters in ASCII table

    private static final Character[] ALL_SYMBOLS = new Character[DEL_ASCII_CODE - SPACE_ASCII_CODE];

    public static Character[] getAllSymbols() {
        return ALL_SYMBOLS.clone();
    }

    static {
        for (int i = SPACE_ASCII_CODE; i < DEL_ASCII_CODE; ++i) {
            ALL_SYMBOLS[i - SPACE_ASCII_CODE] = (char) i;
        }
    }

    /**
     * POSITIVE - add characters and ranges
     * NEGATIVE - all but characters and ranges
     */
    public enum TYPE {
        POSITIVE,
        NEGATIVE
    }

    /**
     * Range of symbols
     */
    public static class SymbolRange {
        public static final SymbolRange SMALL_LETTERS   = new SymbolSet.SymbolRange('a', 'z');
        public static final SymbolRange CAPITAL_LETTERS = new SymbolSet.SymbolRange('A', 'Z');
        public static final SymbolRange DIGITS          = new SymbolSet.SymbolRange('0', '9');

        private final int aFrom;
        private final int aTo;

        /**
         * Create range of symbols.
         *
         * @param from min character shall be less than {@code to}
         * @param to   max character, shall be greater than {@code from}
         * @apiNote No verifications are done!
         */
        public SymbolRange(char from, char to) {
            aFrom = from;
            aTo = to;
        }

        int getFrom() {
            return aFrom;
        }

        int getTo() {
            return aTo;
        }
    }

    private final Set<Character> aInitial;
    private final Set<Character> aModification;
    private final TYPE           aType;

    private Character[] aSymbols;
    private Character[] aSymbolsCaseInsensitive;

    /**
     * Symbol set containing all symbols
     */
    public SymbolSet() {
        this(".", ALL_SYMBOLS.clone(), TYPE.POSITIVE);
    }

    public SymbolSet(String pattern, Character[] symbols, TYPE type) {
        this(pattern, Collections.emptyList(), symbols, type);
    }

    public SymbolSet(String pattern, Collection<SymbolRange> symbolRanges, TYPE type) {
        this(pattern, symbolRanges, ZERO_LENGTH_CHARACTER_ARRAY, type);
    }

    /**
     * Create SymbolSet from ranges and symbols according to type
     *
     * @param pattern      original pattern for the reference
     * @param symbolRanges ranges of symbols to include/exclude
     * @param symbols      symbols to include/exclude
     * @param type         POSITIVE - include, NEGATIVE - exclude
     */
    public SymbolSet(String pattern, Collection<SymbolRange> symbolRanges, Character[] symbols, TYPE type) {
        super(pattern);
        aInitial = type == TYPE.NEGATIVE
                   ? new HashSet<>(Arrays.asList(ALL_SYMBOLS))   // First we need to add all, later we remove unnecessary
                   : new HashSet<>(ALL_SYMBOLS.length);          // Most probably it will be enough.

        aModification = new HashSet<>(Arrays.asList(symbols));
        aType = type;
        symbolRanges.stream()
                    .flatMapToInt(r -> IntStream.rangeClosed(r.getFrom(), r.getTo()))
                    .mapToObj(i -> (char) i)
                    .collect(Collectors.toCollection(() -> aModification));
    }

    private Character[] getOrInitSymbols() {
        if (aSymbols == null) {
            if (aType == TYPE.POSITIVE) {
                aInitial.addAll(aModification);
            } else {
                aInitial.removeIf(aModification::contains);
            }
            aSymbols = aInitial.toArray(ZERO_LENGTH_CHARACTER_ARRAY);
        }
        return aSymbols;
    }

    private Character[] getOrInitCaseInsensitiveSymbols() {
        if (aSymbolsCaseInsensitive == null) {
            Set<Character> caseInsensitive = new HashSet<>(aInitial);
            if (aType == TYPE.POSITIVE) {
                handleCaseSensitiveCharacters(aModification, caseInsensitive::add);
            } else {
                handleCaseSensitiveCharacters(aModification, caseInsensitive::remove);
            }
            aSymbolsCaseInsensitive = caseInsensitive.toArray(ZERO_LENGTH_CHARACTER_ARRAY);
        }
        return aSymbolsCaseInsensitive;
    }

    private static void handleCaseSensitiveCharacters(Iterable<Character> symbols, Consumer<Character> consumer) {
        for (Character c : symbols) {
            if (Character.isUpperCase(c)) {
                consumer.accept(Character.toLowerCase(c));
            } else if (Character.isLowerCase(c)) {
                consumer.accept(Character.toUpperCase(c));
            }
            consumer.accept(c);
        }
    }

    @Override
    public void visit(NodeVisitor visitor) {
        visitor.visit(this);
    }

    public Character[] getSymbols() {
        return getOrInitSymbols();
    }

    public Character[] getSymbolsCaseInsensitive() {
        return getOrInitCaseInsensitiveSymbols();
    }

    @Override
    public String toString() {
        return "SymbolSet{" + Arrays.toString(getOrInitSymbols()) + '}';
    }

    public boolean isEmpty() {
        return getOrInitSymbols().length == 0;
    }
}
