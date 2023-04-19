package com.jn.langx.ui.image.jepg;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * Signals that a truncated file was detected.  This object contains
 * the Raster/BufferedImage that has the partially decoded image data
 * in it.  There is no indication of the portion of the Raster that
 * may or may not be good.
 * <p>
 * Note that the classes in the com.sun.image.codec.jpeg package are not
 * part of the core Java APIs.  They are a part of Sun's JDK and JRE
 * distributions.  Although other licensees may choose to distribute these
 * classes, developers cannot depend on their availability in non-Sun
 * implementations.  We expect that equivalent functionality will eventually
 * be available in a core API or standard extension.
 * <p>
 *
 * @author Thomas DeWeese
 * @see JPEGImageDecoder
 * @since 4.3.8
 */
public class TruncatedFileException extends RuntimeException {
    private static final long serialVersionUID = -1L;
    private transient Raster ras = null;
    private transient BufferedImage bi = null;


    /**
     * Constructs a <code>TruncatedFileException</code> with the
     * partially decoded BufferedImage.
     *
     * @param bi the partially decoded BufferedImage (may be null).
     * @since 1.2
     */
    public TruncatedFileException(BufferedImage bi) {
        super("Premature end of input file");
        this.bi = bi;
        this.ras = bi.getData();
    }

    /**
     * Constructs an <code>TruncatedFileException</code> with the
     * partially decoded Raster
     *
     * @param ras the partially decoded Raster (may be null).
     * @since 1.2
     */
    public TruncatedFileException(Raster ras) {
        super("Premature end of input file");
        this.ras = ras;
    }

    /**
     * Allows access to the raster that was in the progress of being
     * decoded may be null, it is likely to be only partially filled
     * with image data.
     *
     * @since 1.2
     */
    public Raster getRaster() {
        return ras;
    }

    /**
     * Allows access to the BufferedImage that was in the progress of
     * being decoded, this may be null, it is likely to be only
     * partially filled with image data.
     *
     * @since 1.2
     */
    public BufferedImage getBufferedImage() {
        return bi;
    }
}
