package com.jn.langx.ui.image.jepg.awt;


import com.jn.langx.ui.image.jepg.ImageFormatException;
import com.jn.langx.ui.image.jepg.JPEGDecodeParam;
import com.jn.langx.ui.image.jepg.JPEGEncodeParam;
import com.jn.langx.ui.image.jepg.JPEGImageEncoder;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @since 4.3.8
 */
public class JPEGImageEncoderImpl implements JPEGImageEncoder {
    private OutputStream outStream;
    private JPEGParam param;
    private boolean pack;
    private static final Class OutputStreamClass = OutputStream.class;

    public JPEGImageEncoderImpl(OutputStream var1) {
        this.outStream = null;
        this.param = null;
        this.pack = false;
        if (var1 == null) {
            throw new IllegalArgumentException("OutputStream is null.");
        } else {
            this.outStream = var1;
            this.initEncoder(OutputStreamClass);
        }
    }

    public JPEGImageEncoderImpl(OutputStream var1, JPEGEncodeParam var2) {
        this(var1);
        this.setJPEGEncodeParam(var2);
    }

    public int getDefaultColorId(ColorModel var1) {
        boolean var2 = var1.hasAlpha();
        ColorSpace var3 = var1.getColorSpace();

        switch (var3.getType()) {
            case 3:
                ColorSpace var4 = null;
                try {
                    var4 = ColorSpace.getInstance(1002);
                } catch (IllegalArgumentException var6) {
                }
                if (var3 == var4) {
                    return var2 ? 10 : 5;
                }
                return var2 ? 7 : 3;
            case 5:
                if (var2) {
                    return 7;
                }
                return 3;
            case 6:
                return 1;
            case 9:
                return 4;
            case 4:
            case 7:
            case 8:
            default:
                return 0;
        }
    }

    public synchronized OutputStream getOutputStream() {
        return this.outStream;
    }

    public synchronized void setJPEGEncodeParam(JPEGEncodeParam var1) {
        this.param = new JPEGParam(var1);
    }

    public synchronized JPEGEncodeParam getJPEGEncodeParam() {
        return (JPEGEncodeParam) this.param.clone();
    }

    public JPEGEncodeParam getDefaultJPEGEncodeParam(Raster var1, int var2) {
        JPEGParam var3 = new JPEGParam(var2, var1.getNumBands());
        var3.setWidth(var1.getWidth());
        var3.setHeight(var1.getHeight());
        return var3;
    }

    public JPEGEncodeParam getDefaultJPEGEncodeParam(BufferedImage var1) {
        ColorModel var2 = var1.getColorModel();
        int var3 = this.getDefaultColorId(var2);
        if (!(var2 instanceof IndexColorModel)) {
            return this.getDefaultJPEGEncodeParam(var1.getRaster(), var3);
        } else {
            JPEGParam var4;
            if (var2.hasAlpha()) {
                var4 = new JPEGParam(var3, 4);
            } else {
                var4 = new JPEGParam(var3, 3);
            }

            var4.setWidth(var1.getWidth());
            var4.setHeight(var1.getHeight());
            return var4;
        }
    }

    public JPEGEncodeParam getDefaultJPEGEncodeParam(int var1, int var2) {
        return new JPEGParam(var2, var1);
    }

    public JPEGEncodeParam getDefaultJPEGEncodeParam(JPEGDecodeParam var1) throws ImageFormatException {
        return new JPEGParam(var1);
    }

    public synchronized void encode(BufferedImage var1) throws IOException, ImageFormatException {
        if (this.param == null) {
            this.setJPEGEncodeParam(this.getDefaultJPEGEncodeParam(var1));
        }

        if (var1.getWidth() == this.param.getWidth() && var1.getHeight() == this.param.getHeight()) {
            if (this.param.getEncodedColorID() != this.getDefaultColorId(var1.getColorModel())) {
                throw new ImageFormatException("The encoded COLOR_ID doesn't match the BufferedImage");
            } else {
                WritableRaster var2 = var1.getRaster();
                ColorModel var3 = var1.getColorModel();
                if (var3 instanceof IndexColorModel) {
                    IndexColorModel var4 = (IndexColorModel) var3;
                    var1 = var4.convertToIntDiscrete(var2, false);
                    var2 = var1.getRaster();
                    var3 = var1.getColorModel();
                }

                this.encode( var2, var3);
            }
        } else {
            throw new ImageFormatException("Param block's width/height doesn't match the BufferedImage");
        }
    }

    public synchronized void encode(BufferedImage var1, JPEGEncodeParam var2) throws IOException, ImageFormatException {
        this.setJPEGEncodeParam(var2);
        this.encode(var1);
    }

    public void encode(Raster var1) throws IOException, ImageFormatException {
        if (this.param == null) {
            this.setJPEGEncodeParam(this.getDefaultJPEGEncodeParam(var1, 0));
        }

        if (var1.getNumBands() != var1.getSampleModel().getNumBands()) {
            throw new ImageFormatException("Raster's number of bands doesn't match the SampleModel");
        } else if (var1.getWidth() == this.param.getWidth() && var1.getHeight() == this.param.getHeight()) {
            if (this.param.getEncodedColorID() != 0 && this.param.getNumComponents() != var1.getNumBands()) {
                throw new ImageFormatException("Param block's COLOR_ID doesn't match the Raster.");
            } else {
                this.encode(var1, (ColorModel) null);
            }
        } else {
            throw new ImageFormatException("Param block's width/height doesn't match the Raster");
        }
    }

    public void encode(Raster var1, JPEGEncodeParam var2) throws IOException, ImageFormatException {
        this.setJPEGEncodeParam(var2);
        this.encode(var1);
    }

    private boolean useGiven(Raster var1) {
        SampleModel var2 = var1.getSampleModel();
        if (var2.getDataType() != 0) {
            return false;
        } else if (!(var2 instanceof ComponentSampleModel)) {
            return false;
        } else {
            ComponentSampleModel var3 = (ComponentSampleModel) var2;
            if (var3.getPixelStride() != var2.getNumBands()) {
                return false;
            } else {
                int[] var4 = var3.getBandOffsets();

                for (int var5 = 0; var5 < var4.length; ++var5) {
                    if (var4[var5] != var5) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private boolean canPack(Raster var1) {
        SampleModel var2 = var1.getSampleModel();
        if (var2.getDataType() != 3) {
            return false;
        } else if (!(var2 instanceof SinglePixelPackedSampleModel)) {
            return false;
        } else {
            SinglePixelPackedSampleModel var3 = (SinglePixelPackedSampleModel) var2;
            int[] var4 = new int[]{16711680, 65280, 255, -16777216};
            int[] var5 = var3.getBitMasks();
            if (var5.length != 3 && var5.length != 4) {
                return false;
            } else {
                for (int var6 = 0; var6 < var5.length; ++var6) {
                    if (var5[var6] != var4[var6]) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private void encode(Raster var1, ColorModel var2) throws IOException, ImageFormatException {
        SampleModel var3 = var1.getSampleModel();
        int var5 = var3.getNumBands();
        int var4;
        if (var2 == null) {
            for (var4 = 0; var4 < var5; ++var4) {
                if (var3.getSampleSize(var4) > 8) {
                    throw new ImageFormatException("JPEG encoder can only accept 8 bit data.");
                }
            }
        }

        int var6 = this.param.getEncodedColorID();
        switch (this.param.getNumComponents()) {
            case 3:
                if (var6 != 3 && this.param.findAPP0() != null) {
                    throw new ImageFormatException("3 band JFIF files imply YCbCr encoding.\nParam block indicates alternate encoding.");
                }
                break;
            case 4:
                if (var6 != 4 && this.param.findAPP0() != null) {
                    throw new ImageFormatException("4 band JFIF files imply CMYK encoding.\nParam block indicates alternate encoding.");
                }
                break;
            case 1:
                if (var6 != 1 && var6 != 0 && this.param.findAPP0() != null) {
                    throw new ImageFormatException("1 band JFIF files imply Y or unknown encoding.\nParam block indicates alternate encoding.");
                }
            case 2:
            default:
                break;
        }

        if (!this.param.isImageInfoValid()) {
            this.writeJPEGStream(this.param, var2, this.outStream, (Object) null, 0, 0);
        } else {
            DataBuffer var7 = var1.getDataBuffer();
            boolean var11 = false;
            boolean var12 = true;
            int[] var13 = null;
            if (var2 != null) {
                if (var2.hasAlpha() && var2.isAlphaPremultiplied()) {
                    var11 = true;
                    var12 = false;
                }

                var13 = var2.getComponentSize();

                for (var4 = 0; var4 < var5; ++var4) {
                    if (var13[var4] != 8) {
                        var12 = false;
                    }
                }
            }

            this.pack = false;
            Object var8;
            int var9;
            int var10;
            ComponentSampleModel var14;
            if (var12 && this.useGiven(var1)) {
                var14 = (ComponentSampleModel) var3;
                var10 = var7.getOffset() + var14.getOffset(var1.getMinX() - var1.getSampleModelTranslateX(), var1.getMinY() - var1.getSampleModelTranslateY());
                var9 = var14.getScanlineStride();
                var8 = ((DataBufferByte) var7).getData();
            } else if (var12 && this.canPack(var1)) {
                SinglePixelPackedSampleModel var22 = (SinglePixelPackedSampleModel) var3;
                var10 = var7.getOffset() + var22.getOffset(var1.getMinX() - var1.getSampleModelTranslateX(), var1.getMinY() - var1.getSampleModelTranslateY());
                var9 = var22.getScanlineStride();
                var8 = ((DataBufferInt) var7).getData();
                this.pack = true;
            } else {
                int[] var15 = new int[var5];
                float[] var16 = new float[var5];

                for (var4 = 0; var4 < var5; ++var4) {
                    var15[var4] = var4;
                    if (!var12) {
                        if (var13[var4] != 8) {
                            var16[var4] = 255.0F / (float) ((1 << var13[var4]) - 1);
                        } else {
                            var16[var4] = 1.0F;
                        }
                    }
                }

                var14 = new ComponentSampleModel(0, var1.getWidth(), var1.getHeight(), var5, var5 * var1.getWidth(), var15);
                WritableRaster var17 = Raster.createWritableRaster(var14, new Point(var1.getMinX(), var1.getMinY()));
                if (var12) {
                    var17.setRect(var1);
                } else {
                    float[] var18 = new float[var5];
                    RescaleOp var19 = new RescaleOp(var16, var18, (RenderingHints) null);
                    var19.filter(var1, var17);
                    if (var11) {
                        int[] var20 = new int[var5];

                        for (var4 = 0; var4 < var5; ++var4) {
                            var20[var4] = 8;
                        }

                        ComponentColorModel var21 = new ComponentColorModel(var2.getColorSpace(), var20, true, true, 3, 0);
                        var21.coerceData(var17, false);
                    }
                }

                var7 = var17.getDataBuffer();
                var8 = ((DataBufferByte) var7).getData();
                var10 = var7.getOffset() + var14.getOffset(0, 0);
                var9 = var14.getScanlineStride();
            }

            this.verify(var10, var9, var7.getSize());
            this.writeJPEGStream(this.param, var2, this.outStream, var8, var10, var9);
        }
    }

    private void verify(int var1, int var2, int var3) throws ImageFormatException {
        int var4 = this.param.getWidth();
        int var5 = this.param.getHeight();
        int var6 = this.pack ? 1 : this.param.getNumComponents();
        if (var4 > 0 && var5 > 0 && var5 <= 2147483647 / var4) {
            if (var2 >= 0 && var2 <= 2147483647 / var5 && var2 <= var3) {
                int var7 = (var5 - 1) * var2;
                if (var6 >= 0 && var6 <= 2147483647 / var4 && var6 <= var3 && var6 * var4 <= var2) {
                    int var8 = var4 * var6;
                    if (var8 > 2147483647 - var7) {
                        throw new ImageFormatException("Invalid raster attributes");
                    } else {
                        int var9 = var7 + var8;
                        if (var1 >= 0 && var1 <= 2147483647 - var9) {
                            int var10 = var1 + var9;
                            if (var10 > var3) {
                                throw new ImageFormatException("Computed buffer size doesn't match DataBuffer");
                            }
                        } else {
                            throw new ImageFormatException("Invalid data offset");
                        }
                    }
                } else {
                    throw new ImageFormatException("Invalid pixel stride: " + var6);
                }
            } else {
                throw new ImageFormatException("Invalid scanline stride: " + var2);
            }
        } else {
            throw new ImageFormatException("Invalid image dimensions");
        }
    }

    private int getNearestColorId(ColorModel var1) {
        ColorSpace var2 = var1.getColorSpace();
        switch (var2.getType()) {
            case 5:
                if (var1.hasAlpha()) {
                    return 6;
                }

                return 2;
            default:
                return this.getDefaultColorId(var1);
        }
    }

    private native void initEncoder(Class var1);

    private synchronized native void writeJPEGStream(JPEGEncodeParam var1, ColorModel var2, OutputStream var3, Object var4, int var5, int var6) throws IOException, ImageFormatException;

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                System.loadLibrary("jpeg");
                return null;
            }
        });
    }
}
