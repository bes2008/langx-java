package com.jn.langx.regexp.joni;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import com.jn.langx.text.parser.Scanner;
import com.jn.langx.util.bit.BitVector;

final class JoniRegexpScanner extends Scanner {
    private final StringBuilder sb;
    private final Map<Character, Integer> expected = new HashMap<Character, Integer>();
    private final List<JoniRegexpScanner.Capture> caps = new LinkedList<JoniRegexpScanner.Capture>();
    private final LinkedList<Integer> forwardReferences = new LinkedList<Integer>();
    private int negLookaheadLevel;
    private int negLookaheadGroup;
    private boolean inCharClass = false;
    private boolean inNegativeClass = false;
    private static final String NON_IDENT_ESCAPES = "$^*+(){}[]|\\.?-";

    private JoniRegexpScanner(String string) {
        super(string);
        this.sb = new StringBuilder(this.limit);
        this.reset(0);
        this.expected.put(']', 0);
        this.expected.put('}', 0);
    }

    private void processForwardReferences() {
        Iterator<Integer> iterator = this.forwardReferences.descendingIterator();

        while (iterator.hasNext()) {
            int pos = iterator.next();
            int num = iterator.next();
            if (num > this.caps.size()) {
                StringBuilder buffer = new StringBuilder();
                octalOrLiteral(Integer.toString(num), buffer);
                this.sb.insert(pos, buffer);
            }
        }

        this.forwardReferences.clear();
    }

    public static JoniRegexpScanner scan(String string) {
        JoniRegexpScanner scanner = new JoniRegexpScanner(string);

        try {
            scanner.disjunction();
        } catch (Exception var3) {
            throw new PatternSyntaxException(var3.getMessage(), string, scanner.position);
        }

        if (scanner.position != string.length()) {
            String p = scanner.getStringBuilder().toString();
            throw new PatternSyntaxException(string, p, p.length() + 1);
        } else {
            scanner.processForwardReferences();
            return scanner;
        }
    }

    private StringBuilder getStringBuilder() {
        return this.sb;
    }

    String getJavaPattern() {
        return this.sb.toString();
    }

    BitVector getGroupsInNegativeLookahead() {
        BitVector vec = null;

        for (int i = 0; i < this.caps.size(); ++i) {
            JoniRegexpScanner.Capture cap = this.caps.get(i);
            if (cap.negLookaheadLevel > 0) {
                if (vec == null) {
                    vec = new BitVector(this.caps.size() + 1);
                }

                vec.set(i + 1);
            }
        }

        return vec;
    }

    private boolean commit(int n) {
        switch (n) {
            case 1:
                this.sb.append(this.ch0);
                this.skip(1);
                break;
            case 2:
                this.sb.append(this.ch0);
                this.sb.append(this.ch1);
                this.skip(2);
                break;
            case 3:
                this.sb.append(this.ch0);
                this.sb.append(this.ch1);
                this.sb.append(this.ch2);
                this.skip(3);
                break;
            default:
                assert false : "Should not reach here";
        }

        return true;
    }

    private void restart(int startIn, int startOut) {
        this.reset(startIn);
        this.sb.setLength(startOut);
    }

    private void push(char ch) {
        this.expected.put(ch, this.expected.get(ch) + 1);
    }

    private void pop(char ch) {
        this.expected.put(ch, Math.min(0, this.expected.get(ch) - 1));
    }

    private void disjunction() {
        while (true) {
            this.alternative();
            if (this.ch0 != '|') {
                return;
            }

            this.commit(1);
        }
    }

    private void alternative() {
        while (this.term()) {
        }

    }

    private boolean term() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.assertion()) {
            return true;
        } else if (this.atom()) {
            this.quantifier();
            return true;
        } else {
            this.restart(startIn, startOut);
            return false;
        }
    }

    private boolean assertion() {
        int startIn = this.position;
        int startOut = this.sb.length();
        switch (this.ch0) {
            case '$':
            case '^':
                return this.commit(1);
            case '(':
                if (this.ch1 == '?' && (this.ch2 == '=' || this.ch2 == '!')) {
                    boolean isNegativeLookahead = this.ch2 == '!';
                    this.commit(3);
                    if (isNegativeLookahead) {
                        if (this.negLookaheadLevel == 0) {
                            ++this.negLookaheadGroup;
                        }

                        ++this.negLookaheadLevel;
                    }

                    this.disjunction();
                    if (isNegativeLookahead) {
                        --this.negLookaheadLevel;
                    }

                    if (this.ch0 == ')') {
                        return this.commit(1);
                    }
                }
                break;
            case '\\':
                if (this.ch1 == 'b' || this.ch1 == 'B') {
                    return this.commit(2);
                }
        }

        this.restart(startIn, startOut);
        return false;
    }

    @SuppressWarnings("unused")
    private boolean quantifier() {
        if (this.quantifierPrefix()) {
            if (this.ch0 == '?') {
                this.commit(1);
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean quantifierPrefix() {
        int startIn = this.position;
        int startOut = this.sb.length();
        switch (this.ch0) {
            case '*':
            case '+':
            case '?':
                return this.commit(1);
            case '{':
                this.commit(1);
                if (this.decimalDigits()) {
                    this.push('}');
                    if (this.ch0 == ',') {
                        this.commit(1);
                        this.decimalDigits();
                    }

                    if (this.ch0 == '}') {
                        this.pop('}');
                        this.commit(1);
                        return true;
                    }

                    this.restart(startIn, startOut);
                    return false;
                }
            default:
                this.restart(startIn, startOut);
                return false;
        }
    }

    private boolean atom() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.patternCharacter()) {
            return true;
        } else if (this.ch0 == '.') {
            return this.commit(1);
        } else {
            if (this.ch0 == '\\') {
                this.commit(1);
                if (this.atomEscape()) {
                    return true;
                }
            }

            if (this.characterClass()) {
                return true;
            } else {
                if (this.ch0 == '(') {
                    this.commit(1);
                    if (this.ch0 == '?' && this.ch1 == ':') {
                        this.commit(2);
                    } else {
                        this.caps.add(new JoniRegexpScanner.Capture(this.negLookaheadGroup, this.negLookaheadLevel));
                    }

                    this.disjunction();
                    if (this.ch0 == ')') {
                        this.commit(1);
                        return true;
                    }
                }

                this.restart(startIn, startOut);
                return false;
            }
        }
    }

    private boolean patternCharacter() {
        if (this.atEOF()) {
            return false;
        } else {
            switch (this.ch0) {
                case '$':
                case '(':
                case ')':
                case '*':
                case '+':
                case '.':
                case '?':
                case '[':
                case '\\':
                case '^':
                case '|':
                    return false;
                case ']':
                case '}':
                    int n = this.expected.get(this.ch0);
                    if (n != 0) {
                        return false;
                    }
                case '{':
                    if (!this.quantifierPrefix()) {
                        this.sb.append('\\');
                        return this.commit(1);
                    }

                    return false;
                default:
                    return this.commit(1);
            }
        }
    }

    private boolean atomEscape() {
        return this.decimalEscape() || this.characterClassEscape() || this.characterEscape() || this.identityEscape();
    }

    private boolean characterEscape() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.controlEscape()) {
            return true;
        } else {
            if (this.ch0 == 'c') {
                this.commit(1);
                if (this.controlLetter()) {
                    return true;
                }

                this.restart(startIn, startOut);
            }

            if (!this.hexEscapeSequence() && !this.unicodeEscapeSequence()) {
                this.restart(startIn, startOut);
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean scanEscapeSequence(char leader, int length) {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.ch0 != leader) {
            return false;
        } else {
            this.commit(1);

            for (int i = 0; i < length; ++i) {
                char ch0l = Character.toLowerCase(this.ch0);
                if ((ch0l < 'a' || ch0l > 'f') && !isDecimalDigit(this.ch0)) {
                    this.restart(startIn, startOut);
                    return false;
                }

                this.commit(1);
            }

            return true;
        }
    }

    private boolean hexEscapeSequence() {
        return this.scanEscapeSequence('x', 2);
    }

    private boolean unicodeEscapeSequence() {
        return this.scanEscapeSequence('u', 4);
    }

    private boolean controlEscape() {
        switch (this.ch0) {
            case 'f':
            case 'n':
            case 'r':
            case 't':
            case 'v':
                return this.commit(1);
            default:
                return false;
        }
    }

    private boolean controlLetter() {
        if ((this.ch0 < 'A' || this.ch0 > 'Z') && (this.ch0 < 'a' || this.ch0 > 'z') && (!this.inCharClass || !isDecimalDigit(this.ch0) && this.ch0 != '_')) {
            return false;
        } else {
            this.sb.setLength(this.sb.length() - 1);
            unicode(this.ch0 % 32, this.sb);
            this.skip(1);
            return true;
        }
    }

    private boolean identityEscape() {
        if (this.atEOF()) {
            throw new RuntimeException("\\ at end of pattern");
        } else {
            if (this.ch0 == 'c') {
                this.sb.append('\\');
            } else if ("$^*+(){}[]|\\.?-".indexOf(this.ch0) == -1) {
                this.sb.setLength(this.sb.length() - 1);
            }

            return this.commit(1);
        }
    }

    private boolean decimalEscape() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.ch0 == '0' && !isOctalDigit(this.ch1)) {
            this.skip(1);
            this.sb.append("\u0000");
            return true;
        } else if (!isDecimalDigit(this.ch0)) {
            this.restart(startIn, startOut);
            return false;
        } else {
            int decimalValue;
            if (this.ch0 == '0') {
                if (this.inCharClass) {
                    decimalValue = 0;

                    while (isOctalDigit(this.ch0)) {
                        decimalValue = decimalValue * 8 + this.ch0 - 48;
                        this.skip(1);
                    }

                    unicode(decimalValue, this.sb);
                } else {
                    this.decimalDigits();
                }
            } else {
                decimalValue = 0;

                while (isDecimalDigit(this.ch0)) {
                    decimalValue = decimalValue * 10 + this.ch0 - 48;
                    this.skip(1);
                }

                if (this.inCharClass) {
                    this.sb.setLength(this.sb.length() - 1);
                    octalOrLiteral(Integer.toString(decimalValue), this.sb);
                } else if (decimalValue <= this.caps.size()) {
                    JoniRegexpScanner.Capture capture = this.caps.get(decimalValue - 1);
                    if (!capture.canBeReferencedFrom(this.negLookaheadGroup, this.negLookaheadLevel)) {
                        this.sb.setLength(this.sb.length() - 1);
                    } else {
                        this.sb.append(decimalValue);
                    }
                } else {
                    this.sb.setLength(this.sb.length() - 1);
                    this.forwardReferences.add(decimalValue);
                    this.forwardReferences.add(this.sb.length());
                }
            }

            return true;
        }
    }

    private boolean characterClassEscape() {
        switch (this.ch0) {
            case 'D':
            case 'W':
            case 'd':
            case 'w':
                return this.commit(1);
            case 'S':
                /*
                if (RegExpFactory.usesJavaUtilRegex()) {
                    this.sb.setLength(this.sb.length() - 1);
                    this.sb.append(this.inNegativeClass ? "&&[" : "[^").append(Lexer.getWhitespaceRegExp()).append(']');
                    this.skip(1);
                    return true;
                }
                */
                return this.commit(1);
            case 's':
                /*
                if (RegExpFactory.usesJavaUtilRegex()) {
                    this.sb.setLength(this.sb.length() - 1);
                    if (this.inCharClass) {
                        this.sb.append(Lexer.getWhitespaceRegExp());
                    } else {
                        this.sb.append('[').append(Lexer.getWhitespaceRegExp()).append(']');
                    }

                    this.skip(1);
                    return true;
                }
                */
                return this.commit(1);
            default:
                return false;
        }
    }

    private boolean characterClass() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.ch0 == '[') {
            label78:
            {
                boolean var3;
                try {
                    this.inCharClass = true;
                    this.push(']');
                    this.commit(1);
                    if (this.ch0 == '^') {
                        this.inNegativeClass = true;
                        this.commit(1);
                    }

                    if (!this.classRanges() || this.ch0 != ']') {
                        break label78;
                    }

                    this.pop(']');
                    this.commit(1);
                    if (this.position == startIn + 2) {
                        this.sb.setLength(this.sb.length() - 1);
                        this.sb.append("^\\s\\S]");
                    } else if (this.position == startIn + 3 && this.inNegativeClass) {
                        this.sb.setLength(this.sb.length() - 2);
                        this.sb.append("\\s\\S]");
                    }

                    var3 = true;
                } finally {
                    this.inCharClass = false;
                    this.inNegativeClass = false;
                }

                return var3;
            }
        }

        this.restart(startIn, startOut);
        return false;
    }

    private boolean classRanges() {
        this.nonemptyClassRanges();
        return true;
    }

    private boolean nonemptyClassRanges() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.classAtom()) {
            if (this.ch0 == '-') {
                this.commit(1);
                if (this.classAtom() && this.classRanges()) {
                    return true;
                }
            }

            this.nonemptyClassRangesNoDash();
            return true;
        } else {
            this.restart(startIn, startOut);
            return false;
        }
    }

    private boolean nonemptyClassRangesNoDash() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.classAtomNoDash()) {
            if (this.ch0 == '-') {
                this.commit(1);
                if (this.classAtom() && this.classRanges()) {
                    return true;
                }
            }

            this.nonemptyClassRangesNoDash();
            return true;
        } else if (this.classAtom()) {
            return true;
        } else {
            this.restart(startIn, startOut);
            return false;
        }
    }

    private boolean classAtom() {
        return this.ch0 == '-' ? this.commit(1) : this.classAtomNoDash();
    }

    private boolean classAtomNoDash() {
        if (this.atEOF()) {
            return false;
        } else {
            int startIn = this.position;
            int startOut = this.sb.length();
            switch (this.ch0) {
                case '-':
                case ']':
                    return false;
                case '[':
                    this.sb.append('\\');
                    return this.commit(1);
                case '\\':
                    this.commit(1);
                    if (this.classEscape()) {
                        return true;
                    }

                    this.restart(startIn, startOut);
                    return false;
                default:
                    return this.commit(1);
            }
        }
    }

    private boolean classEscape() {
        if (this.decimalEscape()) {
            return true;
        } else if (this.ch0 == 'b') {
            this.sb.setLength(this.sb.length() - 1);
            this.sb.append('\b');
            this.skip(1);
            return true;
        } else {
            return this.characterEscape() || this.characterClassEscape() || this.identityEscape();
        }
    }

    private boolean decimalDigits() {
        if (!isDecimalDigit(this.ch0)) {
            return false;
        } else {
            while (isDecimalDigit(this.ch0)) {
                this.commit(1);
            }

            return true;
        }
    }

    private static void unicode(int value, StringBuilder buffer) {
        String hex = Integer.toHexString(value);
        buffer.append('u');

        for (int i = 0; i < 4 - hex.length(); ++i) {
            buffer.append('0');
        }

        buffer.append(hex);
    }

    private static void octalOrLiteral(String numberLiteral, StringBuilder buffer) {
        int length = numberLiteral.length();
        int octalValue = 0;

        int pos;
        for (pos = 0; pos < length && octalValue < 32; ++pos) {
            char ch = numberLiteral.charAt(pos);
            if (!isOctalDigit(ch)) {
                break;
            }

            octalValue = octalValue * 8 + ch - 48;
        }

        if (octalValue > 0) {
            buffer.append('\\');
            unicode(octalValue, buffer);
            buffer.append(numberLiteral.substring(pos));
        } else {
            buffer.append(numberLiteral);
        }

    }

    private static boolean isOctalDigit(char ch) {
        return ch >= '0' && ch <= '7';
    }

    private static boolean isDecimalDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private static class Capture {
        private final int negLookaheadLevel;
        private final int negLookaheadGroup;

        Capture(int negLookaheadGroup, int negLookaheadLevel) {
            this.negLookaheadGroup = negLookaheadGroup;
            this.negLookaheadLevel = negLookaheadLevel;
        }

        boolean canBeReferencedFrom(int group, int level) {
            return this.negLookaheadLevel == 0 || group == this.negLookaheadGroup && level >= this.negLookaheadLevel;
        }
    }
}
