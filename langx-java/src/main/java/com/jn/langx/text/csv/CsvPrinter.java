package com.jn.langx.text.csv;

import com.jn.langx.util.Preconditions;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static com.jn.langx.text.csv.CsvConstants.*;

/**
 * Prints values in a {@link CsvFormat CSV format}.
 * <p>
 * <p>Values can be appended to the output by calling the {@link #print(Object)} method.
 * Values are printed according to {@link String#valueOf(Object)}.
 * To complete a record the {@link #println()} method has to be called.
 * Comments can be appended by calling {@link #printComment(String)}.
 * However a comment will only be written to the output if the {@link CsvFormat} supports comments.
 * </p>
 * <p>
 * <p>The printer also supports appending a complete record at once by calling {@link #printRecord(Object...)}
 * or {@link #printRecord(Iterable)}.
 * Furthermore {@link #printRecords(Object...)}, {@link #printRecords(Iterable)} and {@link #printRecords(ResultSet)}
 * methods can be used to print several records at once.
 * </p>
 * <p>
 * <p>Example:</p>
 * <p>
 * <pre>
 * try (CSVPrinter printer = new CSVPrinter(new FileWriter("csv.txt"), CSVFormat.EXCEL)) {
 *     printer.printRecord("id", "userName", "firstName", "lastName", "birthday");
 *     printer.printRecord(1, "john73", "John", "Doe", LocalDate.of(1973, 9, 15));
 *     printer.println();
 *     printer.printRecord(2, "mary", "Mary", "Meyer", LocalDate.of(1985, 3, 29));
 * } catch (IOException ex) {
 *     ex.printStackTrace();
 * }
 * </pre>
 * <p>
 * <p>This code will write the following to csv.txt:</p>
 * <pre>
 * id,userName,firstName,lastName,birthday
 * 1,john73,John,Doe,1973-09-15
 *
 * 2,mary,Mary,Meyer,1985-03-29
 * </pre>
 */
public final class CsvPrinter implements Flushable, Closeable {

    /**
     * The place that the values get written.
     */
    private final Appendable out;
    private final CsvFormat format;

    /**
     * True if we just began a new record.
     */
    private boolean newRecord = true;

    /**
     * Creates a printer that will print values to the given stream following the CSVFormat.
     * <p>
     * Currently, only a pure encapsulation format or a pure escaping format is supported. Hybrid formats (encapsulation
     * and escaping with a different character) are not supported.
     * </p>
     *
     * @param out    stream to which to print. Must not be null.
     * @param format the CSV format. Must not be null.
     * @throws IOException              thrown if the optional header cannot be printed.
     * @throws IllegalArgumentException thrown if the parameters of the format are inconsistent or if either out or format are null.
     */
    public CsvPrinter(final Appendable out, final CsvFormat format) throws IOException {
        Preconditions.checkNotNull(out, "out");
        Preconditions.checkNotNull(format, "format");

        this.out = out;
        this.format = format;
        // It seems a pain to have to track whether the header has already been printed or not.
        if (format.getHeaderComments() != null) {
            for (final String line : format.getHeaderComments()) {
                if (line != null) {
                    this.printComment(line);
                }
            }
        }
        if (format.getHeader() != null && !format.getSkipHeaderRecord()) {
            this.printRecord((Object[]) format.getHeader());
        }
    }

    // ======================================================
    // printing implementation
    // ======================================================

    @Override
    public void close() throws IOException {
        close(false);
    }

    /**
     * Closes the underlying stream with an optional flush first.
     *
     * @param flush whether to flush before the actual close.
     * @throws IOException If an I/O error occurs
     * @since 1.6
     */
    public void close(final boolean flush) throws IOException {
        if (flush || format.getAutoFlush()) {
            flush();
        }
        if (out instanceof Closeable) {
            ((Closeable) out).close();
        }
    }

    /**
     * Flushes the underlying stream.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void flush() throws IOException {
        if (out instanceof Flushable) {
            ((Flushable) out).flush();
        }
    }

    /**
     * Gets the target Appendable.
     *
     * @return the target Appendable.
     */
    public Appendable getOut() {
        return this.out;
    }

    /**
     * Prints the string as the next value on the line. The value will be escaped or encapsulated as needed.
     *
     * @param value value to be output.
     * @throws IOException If an I/O error occurs
     */
    public void print(final Object value) throws IOException {
        format.print(value, out, newRecord);
        newRecord = false;
    }

    /**
     * Prints a comment on a new line among the delimiter separated values.
     * <p>
     * <p>
     * Comments will always begin on a new line and occupy at least one full line. The character specified to start
     * comments and a space will be inserted at the beginning of each new line in the comment.
     * </p>
     * <p>
     * <p>
     * If comments are disabled in the current CSV format this method does nothing.
     * </p>
     * <p>
     * <p>This method detects line breaks inside the comment string and inserts {@link CsvFormat#getRecordSeparator()}
     * to start a new line of the comment. Note that this might produce unexpected results for formats that do not use
     * line breaks as record separator.</p>
     *
     * @param comment the comment to output
     * @throws IOException If an I/O error occurs
     */
    public void printComment(final String comment) throws IOException {
        if (!format.isCommentMarkerSet()) {
            return;
        }
        if (!newRecord) {
            println();
        }
        out.append(format.getCommentMarker().charValue());
        out.append(SP);
        for (int i = 0; i < comment.length(); i++) {
            final char c = comment.charAt(i);
            switch (c) {
                case CR:
                    if (i + 1 < comment.length() && comment.charAt(i + 1) == LF) {
                        i++;
                    }
                    //$FALL-THROUGH$ break intentionally excluded.
                case LF:
                    println();
                    out.append(format.getCommentMarker().charValue());
                    out.append(SP);
                    break;
                default:
                    out.append(c);
                    break;
            }
        }
        println();
    }

    /**
     * Outputs the record separator.
     *
     * @throws IOException If an I/O error occurs
     */
    public void println() throws IOException {
        format.println(out);
        newRecord = true;
    }

    /**
     * Prints the given values a single record of delimiter separated values followed by the record separator.
     * <p>
     * <p>
     * The values will be quoted if needed. Quotes and newLine characters will be escaped. This method adds the record
     * separator to the output after printing the record, so there is no need to call {@link #println()}.
     * </p>
     *
     * @param values values to output.
     * @throws IOException If an I/O error occurs
     */
    public void printRecord(final Iterable<?> values) throws IOException {
        for (final Object value : values) {
            print(value);
        }
        println();
    }

    /**
     * Prints the given values a single record of delimiter separated values followed by the record separator.
     * <p>
     * <p>
     * The values will be quoted if needed. Quotes and newLine characters will be escaped. This method adds the record
     * separator to the output after printing the record, so there is no need to call {@link #println()}.
     * </p>
     *
     * @param values values to output.
     * @throws IOException If an I/O error occurs
     */
    public void printRecord(final Object... values) throws IOException {
        format.printRecord(out, values);
        newRecord = true;
    }

    /**
     * Prints all the objects in the given collection handling nested collections/arrays as records.
     * <p>
     * <p>
     * If the given collection only contains simple objects, this method will print a single record like
     * {@link #printRecord(Iterable)}. If the given collections contains nested collections/arrays those nested elements
     * will each be printed as records using {@link #printRecord(Object...)}.
     * </p>
     * <p>
     * <p>
     * Given the following data structure:
     * </p>
     * <p>
     * <pre>
     * <code>
     * List&lt;String[]&gt; data = ...
     * data.add(new String[]{ "A", "B", "C" });
     * data.add(new String[]{ "1", "2", "3" });
     * data.add(new String[]{ "A1", "B2", "C3" });
     * </code>
     * </pre>
     * <p>
     * <p>
     * Calling this method will print:
     * </p>
     * <p>
     * <pre>
     * <code>
     * A, B, C
     * 1, 2, 3
     * A1, B2, C3
     * </code>
     * </pre>
     *
     * @param values the values to print.
     * @throws IOException If an I/O error occurs
     */
    public void printRecords(final Iterable<?> values) throws IOException {
        for (final Object value : values) {
            if (value instanceof Object[]) {
                this.printRecord((Object[]) value);
            } else if (value instanceof Iterable) {
                this.printRecord((Iterable<?>) value);
            } else {
                this.printRecord(value);
            }
        }
    }

    /**
     * Prints all the objects in the given array handling nested collections/arrays as records.
     * <p>
     * <p>
     * If the given array only contains simple objects, this method will print a single record like
     * {@link #printRecord(Object...)}. If the given collections contains nested collections/arrays those nested
     * elements will each be printed as records using {@link #printRecord(Object...)}.
     * </p>
     * <p>
     * <p>
     * Given the following data structure:
     * </p>
     * <p>
     * <pre>
     * <code>
     * String[][] data = new String[3][]
     * data[0] = String[]{ "A", "B", "C" };
     * data[1] = new String[]{ "1", "2", "3" };
     * data[2] = new String[]{ "A1", "B2", "C3" };
     * </code>
     * </pre>
     * <p>
     * <p>
     * Calling this method will print:
     * </p>
     * <p>
     * <pre>
     * <code>
     * A, B, C
     * 1, 2, 3
     * A1, B2, C3
     * </code>
     * </pre>
     *
     * @param values the values to print.
     * @throws IOException If an I/O error occurs
     */
    public void printRecords(final Object... values) throws IOException {
        printRecords(Arrays.asList(values));
    }

    /**
     * Prints all the objects in the given JDBC result set.
     *
     * @param resultSet result set the values to print.
     * @throws IOException  If an I/O error occurs
     * @throws SQLException if a database access error occurs
     */
    public void printRecords(final ResultSet resultSet) throws SQLException, IOException {
        final int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                final Object object = resultSet.getObject(i);
                print(object instanceof Clob ? ((Clob) object).getCharacterStream() : object);
            }
            println();
        }
    }
}
