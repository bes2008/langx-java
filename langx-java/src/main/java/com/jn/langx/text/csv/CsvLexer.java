package com.jn.langx.text.csv;

import com.jn.langx.util.Strings;

import java.io.Closeable;
import java.io.IOException;

import static com.jn.langx.text.csv.CsvConstants.*;

/**
 * Lexical analyzer.
 */
final class CsvLexer implements Closeable {

    private static final String CR_STRING = Character.toString(Strings.CR);
    private static final String LF_STRING = Character.toString(Strings.LF);

    /**
     * Constant char to use for disabling comments, escapes and encapsulation. The value -2 is used because it
     * won't be confused with an EOF signal (-1), and because the Unicode value {@code FFFE} would be encoded as two
     * chars (using surrogates) and thus there should never be a collision with a real text char.
     */
    private static final char DISABLED = '\ufffe';

    private final char delimiter;
    private final char escape;
    private final char quoteChar;
    private final char commentStart;

    private final boolean ignoreSurroundingSpaces;
    private final boolean ignoreEmptyLines;

    /**
     * The input stream
     */
    private final ExtendedBufferedReader reader;
    private String firstEol;

    String getFirstEol() {
        return firstEol;
    }

    CsvLexer(final CsvFormat format, final ExtendedBufferedReader reader) {
        this.reader = reader;
        this.delimiter = format.getDelimiter();
        this.escape = mapNullToDisabled(format.getEscapeCharacter());
        this.quoteChar = mapNullToDisabled(format.getQuoteCharacter());
        this.commentStart = mapNullToDisabled(format.getCommentMarker());
        this.ignoreSurroundingSpaces = format.getIgnoreSurroundingSpaces();
        this.ignoreEmptyLines = format.getIgnoreEmptyLines();
    }

    /**
     * Returns the next token.
     * <p>
     * A token corresponds to a term, a record change or an end-of-file indicator.
     * </p>
     *
     * @param token an existing Token object to reuse. The caller is responsible to initialize the Token.
     * @return the next token found
     * @throws IOException on stream access error
     */
    Token nextToken(final Token token) throws IOException {

        // get the last read char (required for empty line detection)
        int lastChar = reader.getLastChar();

        // read the next char and set eol
        int c = reader.read();
        /*
         * Note: The following call will swallow LF if c == CR. But we don't need to know if the last char was CR or LF
         * - they are equivalent here.
         */
        boolean eol = readEndOfLine(c);

        // empty line detection: eol AND (last char was EOL or beginning)
        if (ignoreEmptyLines) {
            while (eol && isStartOfLine(lastChar)) {
                // go on char ahead ...
                lastChar = c;
                c = reader.read();
                eol = readEndOfLine(c);
                // reached end of file without any content (empty line at the end)
                if (isEndOfFile(c)) {
                    token.type = Token.CsvTokenType.EOF;
                    // don't set token.isReady here because no content
                    return token;
                }
            }
        }

        // did we reach eof during the last iteration already ? EOF
        if (isEndOfFile(lastChar) || !isDelimiter(lastChar) && isEndOfFile(c)) {
            token.type = Token.CsvTokenType.EOF;
            // don't set token.isReady here because no content
            return token;
        }

        if (isStartOfLine(lastChar) && isCommentStart(c)) {
            final String line = reader.readLine();
            if (line == null) {
                token.type = Token.CsvTokenType.EOF;
                // don't set token.isReady here because no content
                return token;
            }
            final String comment = line.trim();
            token.content.append(comment);
            token.type =Token.CsvTokenType.COMMENT;
            return token;
        }

        // important: make sure a new char gets consumed in each iteration
        while (token.type == Token.CsvTokenType.INVALID) {
            // ignore whitespaces at beginning of a token
            if (ignoreSurroundingSpaces) {
                while (isWhitespace(c) && !eol) {
                    c = reader.read();
                    eol = readEndOfLine(c);
                }
            }

            // ok, start of token reached: encapsulated, or token
            if (isDelimiter(c)) {
                // empty token return TOKEN("")
                token.type = Token.CsvTokenType.TOKEN;
            } else if (eol) {
                // empty token return EORECORD("")
                // noop: token.content.append("");
                token.type = Token.CsvTokenType.EORECORD;
            } else if (isQuoteChar(c)) {
                // consume encapsulated token
                parseEncapsulatedToken(token);
            } else if (isEndOfFile(c)) {
                // end of file return EOF()
                // noop: token.content.append("");
                token.type = Token.CsvTokenType.EOF;
                token.isReady = true; // there is data at EOF
            } else {
                // next token must be a simple token
                // add removed blanks when not ignoring whitespace chars...
                parseSimpleToken(token, c);
            }
        }
        return token;
    }

    /**
     * Parses a simple token.
     * <p/>
     * Simple token are tokens which are not surrounded by encapsulators. A simple token might contain escaped
     * delimiters (as \, or \;). The token is finished when one of the following conditions become true:
     * <ul>
     * <li>end of line has been reached (EORECORD)</li>
     * <li>end of stream has been reached (EOF)</li>
     * <li>an unescaped delimiter has been reached (TOKEN)</li>
     * </ul>
     *
     * @param token the current token
     * @param ch    the current character
     * @return the filled token
     * @throws IOException on stream access error
     */
    private Token parseSimpleToken(final Token token, int ch) throws IOException {
        // Faster to use while(true)+break than while(token.type == INVALID)
        while (true) {
            if (readEndOfLine(ch)) {
                token.type = Token.CsvTokenType.EORECORD;
                break;
            } else if (isEndOfFile(ch)) {
                token.type = Token.CsvTokenType.EOF;
                token.isReady = true; // There is data at EOF
                break;
            } else if (isDelimiter(ch)) {
                token.type = Token.CsvTokenType.TOKEN;
                break;
            } else if (isEscape(ch)) {
                final int unescaped = readEscape();
                if (unescaped == END_OF_STREAM) { // unexpected char after escape
                    token.content.append((char) ch).append((char) reader.getLastChar());
                } else {
                    token.content.append((char) unescaped);
                }
                ch = reader.read(); // continue
            } else {
                token.content.append((char) ch);
                ch = reader.read(); // continue
            }
        }

        if (ignoreSurroundingSpaces) {
            trimTrailingSpaces(token.content);
        }

        return token;
    }

    /**
     * Parses an encapsulated token.
     * <p/>
     * Encapsulated tokens are surrounded by the given encapsulating-string. The encapsulator itself might be included
     * in the token using a doubling syntax (as "", '') or using escaping (as in \", \'). Whitespaces before and after
     * an encapsulated token are ignored. The token is finished when one of the following conditions become true:
     * <ul>
     * <li>an unescaped encapsulator has been reached, and is followed by optional whitespace then:</li>
     * <ul>
     * <li>delimiter (TOKEN)</li>
     * <li>end of line (EORECORD)</li>
     * </ul>
     * <li>end of stream has been reached (EOF)</li> </ul>
     *
     * @param token the current token
     * @return a valid token object
     * @throws IOException on invalid state: EOF before closing encapsulator or invalid character before delimiter or EOL
     */
    private Token parseEncapsulatedToken(final Token token) throws IOException {
        // save current line number in case needed for IOE
        final long startLineNumber = getCurrentLineNumber();
        int c;
        while (true) {
            c = reader.read();

            if (isEscape(c)) {
                final int unescaped = readEscape();
                if (unescaped == END_OF_STREAM) { // unexpected char after escape
                    token.content.append((char) c).append((char) reader.getLastChar());
                } else {
                    token.content.append((char) unescaped);
                }
            } else if (isQuoteChar(c)) {
                if (isQuoteChar(reader.lookAhead())) {
                    // double or escaped encapsulator -> add single encapsulator to token
                    c = reader.read();
                    token.content.append((char) c);
                } else {
                    // token finish mark (encapsulator) reached: ignore whitespace till delimiter
                    while (true) {
                        c = reader.read();
                        if (isDelimiter(c)) {
                            token.type = Token.CsvTokenType.TOKEN;
                            return token;
                        } else if (isEndOfFile(c)) {
                            token.type = Token.CsvTokenType.EOF;
                            token.isReady = true; // There is data at EOF
                            return token;
                        } else if (readEndOfLine(c)) {
                            token.type = Token.CsvTokenType.EORECORD;
                            return token;
                        } else if (!isWhitespace(c)) {
                            // error invalid char between token and next delimiter
                            throw new IOException("(line " + getCurrentLineNumber() +
                                    ") invalid char between encapsulated token and delimiter");
                        }
                    }
                }
            } else if (isEndOfFile(c)) {
                // error condition (end of file before end of token)
                throw new IOException("(startline " + startLineNumber +
                        ") EOF reached before encapsulated token finished");
            } else {
                // consume character
                token.content.append((char) c);
            }
        }
    }

    private char mapNullToDisabled(final Character c) {
        return c == null ? DISABLED : c;
    }

    /**
     * Returns the current line number
     *
     * @return the current line number
     */
    long getCurrentLineNumber() {
        return reader.getCurrentLineNumber();
    }

    /**
     * Returns the current character position
     *
     * @return the current character position
     */
    long getCharacterPosition() {
        return reader.getPosition();
    }


    /**
     * Handle an escape sequence.
     * The current character must be the escape character.
     * On return, the next character is available by calling {@link ExtendedBufferedReader#getLastChar()}
     * on the input stream.
     *
     * @return the unescaped character (as an int) or {@link CsvConstants#END_OF_STREAM} if char following the escape is
     * invalid.
     * @throws IOException if there is a problem reading the stream or the end of stream is detected:
     *                     the escape character is not allowed at end of stream
     */
    int readEscape() throws IOException {
        // the escape char has just been read (normally a backslash)
        final int ch = reader.read();
        switch (ch) {
            case 'r':
                return Strings.CR;
            case 'n':
                return Strings.LF;
            case 't':
                return Strings.TAB;
            case 'b':
                return BACKSPACE;
            case 'f':
                return Strings.FF;
            case Strings.CR:
            case Strings.LF:
            case Strings.FF:
            case Strings.TAB:
            case BACKSPACE:
                return ch;
            case END_OF_STREAM:
                throw new IOException("EOF whilst processing escape sequence");
            default:
                // Now check for meta-characters
                if (isMetaChar(ch)) {
                    return ch;
                }
                // indicate unexpected char - available from in.getLastChar()
                return END_OF_STREAM;
        }
    }

    void trimTrailingSpaces(final StringBuilder buffer) {
        int length = buffer.length();
        while (length > 0 && Character.isWhitespace(buffer.charAt(length - 1))) {
            length = length - 1;
        }
        if (length != buffer.length()) {
            buffer.setLength(length);
        }
    }

    /**
     * Greedily accepts \n, \r and \r\n This checker consumes silently the second control-character...
     *
     * @return true if the given or next character is a line-terminator
     */
    boolean readEndOfLine(int ch) throws IOException {
        // check if we have \r\n...
        if (ch == Strings.CR && reader.lookAhead() == Strings.LF) {
            // note: does not change ch outside of this method!
            ch = reader.read();
            // Save the EOL state
            if (firstEol == null) {
                this.firstEol = CRLF;
            }
        }
        // save EOL state here.
        if (firstEol == null) {
            if (ch == Strings.LF) {
                this.firstEol = LF_STRING;
            } else if (ch == Strings.CR) {
                this.firstEol = CR_STRING;
            }
        }

        return ch == Strings.LF || ch == Strings.CR;
    }

    boolean isClosed() {
        return reader.isClosed();
    }

    /**
     * @return true if the given char is a whitespace character
     */
    boolean isWhitespace(final int ch) {
        return !isDelimiter(ch) && Character.isWhitespace((char) ch);
    }

    /**
     * Checks if the current character represents the start of a line: a CR, LF or is at the start of the file.
     *
     * @param ch the character to check
     * @return true if the character is at the start of a line.
     */
    boolean isStartOfLine(final int ch) {
        return ch == Strings.LF || ch == Strings.CR || ch == UNDEFINED;
    }

    /**
     * @return true if the given character indicates end of file
     */
    boolean isEndOfFile(final int ch) {
        return ch == END_OF_STREAM;
    }

    boolean isDelimiter(final int ch) {
        return ch == delimiter;
    }

    boolean isEscape(final int ch) {
        return ch == escape;
    }

    boolean isQuoteChar(final int ch) {
        return ch == quoteChar;
    }

    boolean isCommentStart(final int ch) {
        return ch == commentStart;
    }

    private boolean isMetaChar(final int ch) {
        return ch == delimiter ||
                ch == escape ||
                ch == quoteChar ||
                ch == commentStart;
    }

    /**
     * Closes resources.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }
}
