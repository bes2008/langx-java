package com.jn.langx.util.jar.multiplelevel;

import com.jn.langx.util.Bytes;
import com.jn.langx.util.Calendars;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A ZIP File "Central directory file header record" (CDFH).
 *
 * @see <a href="https://en.wikipedia.org/wiki/Zip_%28file_format%29">Zip File Format</a>
 */

final class CentralDirectoryFileHeader implements FileHeader, Cloneable{

    private static final AsciiBytes SLASH = new AsciiBytes("/");

    private static final byte[] NO_EXTRA = {};

    private static final AsciiBytes NO_COMMENT = new AsciiBytes("");

    private byte[] header;

    private int headerOffset;

    private AsciiBytes name;

    private byte[] extra;

    private AsciiBytes comment;

    private long localHeaderOffset;

    CentralDirectoryFileHeader() {
    }

    CentralDirectoryFileHeader(byte[] header, int headerOffset, AsciiBytes name, byte[] extra, AsciiBytes comment,
                               long localHeaderOffset) {
        this.header = header;
        this.headerOffset = headerOffset;
        this.name = name;
        this.extra = extra;
        this.comment = comment;
        this.localHeaderOffset = localHeaderOffset;
    }

    void load(byte[] data, int dataOffset, RandomAccessData variableData, int variableOffset, JarEntryFilter filter)
            throws IOException {
        // Load fixed part
        this.header = data;
        this.headerOffset = dataOffset;
        long nameLength = Bytes.littleEndianValue(data, dataOffset + 28, 2);
        long extraLength = Bytes.littleEndianValue(data, dataOffset + 30, 2);
        long commentLength = Bytes.littleEndianValue(data, dataOffset + 32, 2);
        this.localHeaderOffset = Bytes.littleEndianValue(data, dataOffset + 42, 4);
        // Load variable part
        dataOffset += 46;
        if (variableData != null) {
            data = variableData.read(variableOffset + 46, nameLength + extraLength + commentLength);
            dataOffset = 0;
        }
        this.name = new AsciiBytes(data, dataOffset, (int) nameLength);
        if (filter != null) {
            this.name = filter.apply(this.name);
        }
        this.extra = NO_EXTRA;
        this.comment = NO_COMMENT;
        if (extraLength > 0) {
            this.extra = new byte[(int) extraLength];
            System.arraycopy(data, (int) (dataOffset + nameLength), this.extra, 0, this.extra.length);
        }
        if (commentLength > 0) {
            this.comment = new AsciiBytes(data, (int) (dataOffset + nameLength + extraLength), (int) commentLength);
        }
    }

    AsciiBytes getName() {
        return this.name;
    }

    @Override
    public boolean hasName(CharSequence name, char suffix) {
        return this.name.matches(name, suffix);
    }

    boolean isDirectory() {
        return this.name.endsWith(SLASH);
    }

    @Override
    public int getMethod() {
        return (int) Bytes.littleEndianValue(this.header, this.headerOffset + 10, 2);
    }

    long getTime() {
        long datetime = Bytes.littleEndianValue(this.header, this.headerOffset + 12, 4);
        return decodeMsDosFormatDateTime(datetime);
    }

    /**
     * Decode MS-DOS Date Time details. See <a href=
     * "https://docs.microsoft.com/en-gb/windows/desktop/api/winbase/nf-winbase-dosdatetimetofiletime">
     * Microsoft's documentation</a> for more details of the format.
     *
     * @param datetime the date and time
     * @return the date and time as milliseconds since the epoch
     */
    private long decodeMsDosFormatDateTime(long datetime) {
        int year = (int) (((datetime >> 25) & 0x7f) + 1980);
        int month = (int) ((datetime >> 21) & 0x0f);
        int dayOfMonth = (int) ((datetime >> 16) & 0x1f);
        int hour = (int) ((datetime >> 11) & 0x1f);
        int minute = (int) ((datetime >> 5) & 0x3f);
        int second = (int) ((datetime << 1) & 0x3e);

        // java8 API:
        //LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth , hour, minute, second);
        //return localDateTime.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(localDateTime)) * 1000;

        // Joda-time API:
        //LocalDateTime localDateTime = new LocalDateTime(year, month, dayOfMonth, hour, minute, second);
        //return localDateTime.toDate().getTime();

        // Java 5 Calender API:
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        Calendars.setYears(calendar,year);
        Calendars.setMonths(calendar,month,true);
        Calendars.setDays(calendar,dayOfMonth);
        Calendars.setHours(calendar,hour);
        Calendars.setMinutes(calendar,minute);
        Calendars.setSeconds(calendar,second);
        return calendar.getTimeInMillis() - calendar.getTimeZone() .getRawOffset() * 1000;
    }

    long getCrc() {
        return Bytes.littleEndianValue(this.header, this.headerOffset + 16, 4);
    }

    @Override
    public long getCompressedSize() {
        return Bytes.littleEndianValue(this.header, this.headerOffset + 20, 4);
    }

    @Override
    public long getSize() {
        return Bytes.littleEndianValue(this.header, this.headerOffset + 24, 4);
    }

    byte[] getExtra() {
        return this.extra;
    }

    boolean hasExtra() {
        return this.extra.length > 0;
    }

    AsciiBytes getComment() {
        return this.comment;
    }

    @Override
    public long getLocalHeaderOffset() {
        return this.localHeaderOffset;
    }

    @Override
    public CentralDirectoryFileHeader clone() {
        byte[] header = new byte[46];
        System.arraycopy(this.header, this.headerOffset, header, 0, header.length);
        return new CentralDirectoryFileHeader(header, 0, this.name, header, this.comment, this.localHeaderOffset);
    }

    static CentralDirectoryFileHeader fromRandomAccessData(RandomAccessData data, int offset, JarEntryFilter filter)
            throws IOException {
        CentralDirectoryFileHeader fileHeader = new CentralDirectoryFileHeader();
        byte[] bytes = data.read(offset, 46);
        fileHeader.load(bytes, 0, data, offset, filter);
        return fileHeader;
    }

}
