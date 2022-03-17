package com.jn.langx.ui.image.jepg.awt;


import com.jn.langx.ui.image.jepg.ImageFormatException;
import com.jn.langx.ui.image.jepg.JPEGDecodeParam;
import com.jn.langx.ui.image.jepg.JPEGImageDecoder;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;

/**
 *  @since 4.3.8
 */
public class JPEGImageDecoderImpl implements JPEGImageDecoder {
    private static final Class InputStreamClass = InputStream.class;
    private JPEGDecodeParam param;
    private InputStream input;
    private WritableRaster aRas;
    private BufferedImage aBufImg;
    private ColorModel cm;
    private boolean unpack;
    private boolean flip;

    public JPEGImageDecoderImpl(InputStream var1) {
        this.param = null;
        this.input = null;
        this.aRas = null;
        this.aBufImg = null;
        this.cm = null;
        this.unpack = false;
        this.flip = false;
        if (var1 == null) {
            throw new IllegalArgumentException("InputStream is null.");
        } else {
            this.input = var1;
            this.initDecoder(InputStreamClass);
        }
    }

    public JPEGImageDecoderImpl(InputStream var1, JPEGDecodeParam var2) {
        this(var1);
        this.setJPEGDecodeParam(var2);
    }

    public JPEGDecodeParam getJPEGDecodeParam() {
        return this.param != null ? (JPEGDecodeParam) this.param.clone() : null;
    }

    public void setJPEGDecodeParam(JPEGDecodeParam var1) {
        this.param = (JPEGDecodeParam) var1.clone();
    }

    public synchronized InputStream getInputStream() {
        return this.input;
    }

    public synchronized Raster decodeAsRaster() throws ImageFormatException {
        try {
            this.param = this.readJPEGStream(this.input, this.param, false);
        } catch (IOException var2) {
            System.out.println("Can't open input Stream" + var2);
            var2.printStackTrace();
        }

        return this.aRas;
    }

    public synchronized BufferedImage decodeAsBufferedImage() throws ImageFormatException {
        try {
            this.param = this.readJPEGStream(this.input, this.param, true);
        } catch (IOException var2) {
            System.out.println("Can't open input Stream" + var2);
            var2.printStackTrace();
        }

        return this.aBufImg;
    }

    private native void initDecoder(Class var1);

    private synchronized native JPEGDecodeParam readJPEGStream(InputStream var1, JPEGDecodeParam var2, boolean var3) throws IOException, ImageFormatException;

    private void readTables() throws IOException {
        try {
            this.param = this.readJPEGStream(this.input, (JPEGDecodeParam) null, false);
        } catch (ImageFormatException var2) {
            var2.printStackTrace();
        }

    }

    private int getDecodedColorModel(int var1, boolean var2) throws ImageFormatException {
        int[] var4 = new int[]{8};
        int[] var5 = new int[]{8, 8, 8};
        int[] var6 = new int[]{8, 8, 8, 8};
        this.cm = null;
        this.unpack = false;
        this.flip = false;
        if (!var2) {
            return var1;
        } else {
            switch (var1) {
                case 0:
                case 4:
                case 11:
                default:
                    throw new ImageFormatException("Can't construct a BufferedImage for given COLOR_ID");
                case 1:
                    this.cm = new ComponentColorModel(ColorSpace.getInstance(1003), var4, false, false, 1, 0);
                    return var1;
                case 2:
                case 3:
                    this.unpack = true;
                    this.cm = new DirectColorModel(24, 16711680, 65280, 255);
                    return 2;
                case 5:
                    this.cm = new ComponentColorModel(ColorSpace.getInstance(1002), var5, false, false, 1, 0);
                    return var1;
                case 8:
                case 9:
                    this.flip = true;
                case 6:
                case 7:
                    this.unpack = true;
                    this.cm = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, 65280, 255, -16777216, false, 3);
                    return 6;
                case 10:
                    this.cm = new ComponentColorModel(ColorSpace.getInstance(1002), var6, true, false, 3, 0);
                    return var1;
            }
        }
    }

    private Object allocateDataBuffer(int var1, int var2, int var3) {
        Object var4;
        if (this.unpack) {
            int[] var5;
            if (var3 == 3) {
                var5 = new int[]{16711680, 65280, 255};
                this.aRas = Raster.createPackedRaster(3, var1, var2, var5, new Point(0, 0));
            } else {
                if (var3 != 4) {
                    throw new ImageFormatException("Can't unpack with anything other than 3 or 4 components");
                }

                var5 = new int[]{16711680, 65280, 255, -16777216};
                this.aRas = Raster.createPackedRaster(3, var1, var2, var5, new Point(0, 0));
            }

            var4 = ((DataBufferInt) this.aRas.getDataBuffer()).getData();
        } else {
            this.aRas = Raster.createInterleavedRaster(0, var1, var2, var3, new Point(0, 0));
            var4 = ((DataBufferByte) this.aRas.getDataBuffer()).getData();
        }

        if (this.cm != null) {
            this.aBufImg = new BufferedImage(this.cm, this.aRas, true, (Hashtable) null);
        }

        return var4;
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                System.loadLibrary("jpeg");
                return null;
            }
        });
    }
}
