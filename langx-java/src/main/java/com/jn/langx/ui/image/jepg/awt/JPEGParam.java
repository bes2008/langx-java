package com.jn.langx.ui.image.jepg.awt;


import com.jn.langx.ui.image.jepg.JPEGDecodeParam;
import com.jn.langx.ui.image.jepg.JPEGEncodeParam;
import com.jn.langx.ui.image.jepg.JPEGHuffmanTable;
import com.jn.langx.ui.image.jepg.JPEGQTable;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @since 4.3.8
 */
public class JPEGParam implements JPEGEncodeParam, Cloneable {
    private static int[] defComponents = new int[]{-1, 1, 3, 3, 4, 3, 4, 4, 4, 4, 4, 4};
    private static int[][] stdCompMapping = new int[][]{{0, 0, 0, 0}, {0}, {0, 0, 0}, {0, 1, 1}, {0, 0, 0, 0}, {0, 1, 1}, {0, 0, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}};
    private static int[][] stdSubsample = new int[][]{{1, 1, 1, 1}, {1}, {1, 1, 1}, {1, 2, 2}, {1, 1, 1, 1}, {1, 2, 2}, {1, 1, 1, 1}, {1, 2, 2, 1}, {1, 1, 1, 1}, {1, 2, 2, 1}, {1, 2, 2, 1}, {1, 2, 2, 1}};
    private int width;
    private int height;
    private int encodedColorID;
    private int numComponents;
    private byte[][][] appMarkers;
    private byte[][] comMarker;
    private boolean imageInfoValid;
    private boolean tableInfoValid;
    private int[] horizontalSubsampling;
    private int[] verticalSubsampling;
    private JPEGQTable[] qTables;
    private int[] qTableMapping;
    private JPEGHuffmanTable[] dcHuffTables;
    private int[] dcHuffMapping;
    private JPEGHuffmanTable[] acHuffTables;
    private int[] acHuffMapping;
    private int restartInterval;
    private static final int app0Length = 14;

    public JPEGParam(int var1) {
        this(var1, defComponents[var1]);
    }

    public JPEGParam(JPEGDecodeParam var1) {
        this(var1.getEncodedColorID(), var1.getNumComponents());
        this.copy(var1);
    }

    public JPEGParam(JPEGEncodeParam var1) {
        this(var1.getEncodedColorID(), var1.getNumComponents());
        this.copy(var1);
    }

    public JPEGParam(int var1, int var2) {
        if (var1 != 0 && var2 != defComponents[var1]) {
            throw new IllegalArgumentException("NumComponents not in sync with COLOR_ID");
        } else {
            this.qTables = new JPEGQTable[4];
            this.acHuffTables = new JPEGHuffmanTable[4];
            this.dcHuffTables = new JPEGHuffmanTable[4];

            for (int var3 = 0; var3 < 4; ++var3) {
                this.qTables[var3] = null;
                this.dcHuffTables[var3] = null;
                this.acHuffTables[var3] = null;
            }

            this.comMarker = (byte[][]) null;
            this.appMarkers = new byte[16][][];
            this.numComponents = var2;
            this.setDefaults(var1);
        }
    }

    private void copy(JPEGDecodeParam var1) {
        if (this.getEncodedColorID() != var1.getEncodedColorID()) {
            throw new IllegalArgumentException("Argument to copy must match current COLOR_ID");
        } else if (this.getNumComponents() != var1.getNumComponents()) {
            throw new IllegalArgumentException("Argument to copy must match in number of components");
        } else {
            this.setWidth(var1.getWidth());
            this.setHeight(var1.getHeight());

            int var2;
            for (var2 = 224; var2 < 239; ++var2) {
                this.setMarkerData(var2, copyArrays(var1.getMarkerData(var2)));
            }

            this.setMarkerData(254, copyArrays(var1.getMarkerData(254)));
            this.setTableInfoValid(var1.isTableInfoValid());
            this.setImageInfoValid(var1.isImageInfoValid());
            this.setRestartInterval(var1.getRestartInterval());

            for (var2 = 0; var2 < 4; ++var2) {
                this.setDCHuffmanTable(var2, var1.getDCHuffmanTable(var2));
                this.setACHuffmanTable(var2, var1.getACHuffmanTable(var2));
                this.setQTable(var2, var1.getQTable(var2));
            }

            for (var2 = 0; var2 < var1.getNumComponents(); ++var2) {
                this.setDCHuffmanComponentMapping(var2, var1.getDCHuffmanComponentMapping(var2));
                this.setACHuffmanComponentMapping(var2, var1.getACHuffmanComponentMapping(var2));
                this.setQTableComponentMapping(var2, var1.getQTableComponentMapping(var2));
                this.setHorizontalSubsampling(var2, var1.getHorizontalSubsampling(var2));
                this.setVerticalSubsampling(var2, var1.getVerticalSubsampling(var2));
            }

        }
    }

    private void copy(JPEGEncodeParam var1) {
        this.copy((JPEGDecodeParam) var1);
    }

    protected void setDefaults(int var1) {
        this.encodedColorID = var1;
        this.restartInterval = 0;
        boolean var2 = false;
        switch (this.numComponents) {
            case 1:
                if (this.encodedColorID == 1 || this.encodedColorID == 0) {
                    var2 = true;
                }
            case 2:
            default:
                break;
            case 3:
                if (this.encodedColorID == 3) {
                    var2 = true;
                }
                break;
            case 4:
                if (this.encodedColorID == 4) {
                    var2 = true;
                }
        }

        if (var2) {
            this.addMarkerData(224, createDefaultAPP0Marker());
        }

        this.setTableInfoValid(true);
        this.setImageInfoValid(true);
        this.dcHuffTables[0] = JPEGHuffmanTable.StdDCLuminance;
        this.dcHuffTables[1] = JPEGHuffmanTable.StdDCChrominance;
        this.dcHuffMapping = new int[this.getNumComponents()];
        System.arraycopy(stdCompMapping[this.encodedColorID], 0, this.dcHuffMapping, 0, this.getNumComponents());
        this.acHuffTables[0] = JPEGHuffmanTable.StdACLuminance;
        this.acHuffTables[1] = JPEGHuffmanTable.StdACChrominance;
        this.acHuffMapping = new int[this.getNumComponents()];
        System.arraycopy(stdCompMapping[this.encodedColorID], 0, this.acHuffMapping, 0, this.getNumComponents());
        this.qTables[0] = JPEGQTable.StdLuminance.getScaledInstance(0.5F, true);
        this.qTables[1] = JPEGQTable.StdChrominance.getScaledInstance(0.5F, true);
        this.qTableMapping = new int[this.getNumComponents()];
        System.arraycopy(stdCompMapping[this.encodedColorID], 0, this.qTableMapping, 0, this.getNumComponents());
        this.horizontalSubsampling = new int[this.getNumComponents()];
        System.arraycopy(stdSubsample[this.encodedColorID], 0, this.horizontalSubsampling, 0, this.getNumComponents());
        this.verticalSubsampling = new int[this.getNumComponents()];
        System.arraycopy(stdSubsample[this.encodedColorID], 0, this.verticalSubsampling, 0, this.getNumComponents());
    }

    public Object clone() {
        JPEGParam var1 = new JPEGParam(this.getEncodedColorID(), this.getNumComponents());
        var1.copy(this);
        return var1;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setWidth(int var1) {
        this.width = var1;
    }

    public void setHeight(int var1) {
        this.height = var1;
    }

    public int getHorizontalSubsampling(int var1) {
        if (var1 >= 0 && var1 < this.getNumComponents()) {
            return this.horizontalSubsampling[var1];
        } else {
            throw new IllegalArgumentException("Component must be between 0 and number of components");
        }
    }

    public int getVerticalSubsampling(int var1) {
        if (var1 >= 0 && var1 < this.getNumComponents()) {
            return this.verticalSubsampling[var1];
        } else {
            throw new IllegalArgumentException("Component must be between 0 and number of components");
        }
    }

    public void setHorizontalSubsampling(int var1, int var2) {
        if (var1 >= 0 && var1 < this.getNumComponents()) {
            if (var2 <= 0) {
                throw new IllegalArgumentException("SubSample factor must be positive: " + var2);
            } else {
                this.horizontalSubsampling[var1] = var2;
            }
        } else {
            throw new IllegalArgumentException("Component must be between 0 and number of components: " + var1);
        }
    }

    public void setVerticalSubsampling(int var1, int var2) {
        if (var1 >= 0 && var1 < this.getNumComponents()) {
            if (var2 <= 0) {
                throw new IllegalArgumentException("SubSample factor must be positive.");
            } else {
                this.verticalSubsampling[var1] = var2;
            }
        } else {
            throw new IllegalArgumentException("Component must be between 0 and number of components");
        }
    }

    public JPEGQTable getQTable(int var1) {
        if (var1 >= 0 && var1 < 4) {
            return this.qTables[var1];
        } else {
            throw new IllegalArgumentException("tableNum must be between 0 and 3.");
        }
    }

    public JPEGQTable getQTableForComponent(int var1) {
        if (var1 >= 0 && var1 < this.qTableMapping.length) {
            return this.getQTable(this.qTableMapping[var1]);
        } else {
            throw new IllegalArgumentException("Component must be between 0 and number of components");
        }
    }

    public JPEGHuffmanTable getDCHuffmanTable(int var1) {
        if (var1 >= 0 && var1 < 4) {
            return this.dcHuffTables[var1];
        } else {
            throw new IllegalArgumentException("tableNum must be 0-3.");
        }
    }

    public JPEGHuffmanTable getDCHuffmanTableForComponent(int var1) {
        if (var1 >= 0 && var1 < this.dcHuffMapping.length) {
            return this.getDCHuffmanTable(this.dcHuffMapping[var1]);
        } else {
            throw new IllegalArgumentException("Component must be between 0 and number of components");
        }
    }

    public JPEGHuffmanTable getACHuffmanTable(int var1) {
        if (var1 >= 0 && var1 < 4) {
            return this.acHuffTables[var1];
        } else {
            throw new IllegalArgumentException("tableNum must be 0-3.");
        }
    }

    public JPEGHuffmanTable getACHuffmanTableForComponent(int var1) {
        if (var1 >= 0 && var1 < this.acHuffMapping.length) {
            return this.getACHuffmanTable(this.acHuffMapping[var1]);
        } else {
            throw new IllegalArgumentException("Component must be between 0 and number of components");
        }
    }

    public void setQTable(int var1, JPEGQTable var2) {
        if (var1 >= 0 && var1 < 4) {
            this.qTables[var1] = var2;
        } else {
            throw new IllegalArgumentException("tableNum must be between 0 and 3.");
        }
    }

    public void setDCHuffmanTable(int var1, JPEGHuffmanTable var2) {
        if (var1 >= 0 && var1 < 4) {
            this.dcHuffTables[var1] = var2;
        } else {
            throw new IllegalArgumentException("tableNum must be 0, 1, 2, or 3.");
        }
    }

    public void setACHuffmanTable(int var1, JPEGHuffmanTable var2) {
        if (var1 >= 0 && var1 < 4) {
            this.acHuffTables[var1] = var2;
        } else {
            throw new IllegalArgumentException("tableNum must be 0, 1, 2, or 3.");
        }
    }

    public int getDCHuffmanComponentMapping(int var1) {
        if (var1 >= 0 && var1 < this.getNumComponents()) {
            return this.dcHuffMapping[var1];
        } else {
            throw new IllegalArgumentException("Requested Component doesn't exist.");
        }
    }

    public int getACHuffmanComponentMapping(int var1) {
        if (var1 >= 0 && var1 < this.getNumComponents()) {
            return this.acHuffMapping[var1];
        } else {
            throw new IllegalArgumentException("Requested Component doesn't exist.");
        }
    }

    public int getQTableComponentMapping(int var1) {
        if (var1 >= 0 && var1 < this.getNumComponents()) {
            return this.qTableMapping[var1];
        } else {
            throw new IllegalArgumentException("Requested Component doesn't exist.");
        }
    }

    public void setDCHuffmanComponentMapping(int var1, int var2) {
        if (var1 >= 0 && var1 < this.getNumComponents()) {
            if (var2 >= 0 && var2 < 4) {
                this.dcHuffMapping[var1] = var2;
            } else {
                throw new IllegalArgumentException("Tables must be 0, 1, 2, or 3.");
            }
        } else {
            throw new IllegalArgumentException("Given Component doesn't exist.");
        }
    }

    public void setACHuffmanComponentMapping(int var1, int var2) {
        if (var1 >= 0 && var1 < this.getNumComponents()) {
            if (var2 >= 0 && var2 < 4) {
                this.acHuffMapping[var1] = var2;
            } else {
                throw new IllegalArgumentException("Tables must be 0, 1, 2, or 3.");
            }
        } else {
            throw new IllegalArgumentException("Given Component doesn't exist.");
        }
    }

    public void setQTableComponentMapping(int var1, int var2) {
        if (var1 >= 0 && var1 < this.getNumComponents()) {
            if (var2 >= 0 && var2 < 4) {
                this.qTableMapping[var1] = var2;
            } else {
                throw new IllegalArgumentException("Tables must be 0, 1, 2, or 3.");
            }
        } else {
            throw new IllegalArgumentException("Given Component doesn't exist.");
        }
    }

    public boolean isImageInfoValid() {
        return this.imageInfoValid;
    }

    public void setImageInfoValid(boolean var1) {
        this.imageInfoValid = var1;
    }

    public boolean isTableInfoValid() {
        return this.tableInfoValid;
    }

    public void setTableInfoValid(boolean var1) {
        this.tableInfoValid = var1;
    }

    public boolean getMarker(int var1) {
        byte[][] var2 = (byte[][]) null;
        if (var1 == 254) {
            var2 = this.comMarker;
        } else {
            if (var1 < 224 || var1 > 239) {
                throw new IllegalArgumentException("Invalid Marker ID:" + var1);
            }

            var2 = this.appMarkers[var1 - 224];
        }

        if (var2 == null) {
            return false;
        } else {
            return var2.length != 0;
        }
    }

    public byte[][] getMarkerData(int var1) {
        if (var1 == 254) {
            return this.comMarker;
        } else if (var1 >= 224 && var1 <= 239) {
            return this.appMarkers[var1 - 224];
        } else {
            throw new IllegalArgumentException("Invalid Marker ID:" + var1);
        }
    }

    public void setMarkerData(int var1, byte[][] var2) {
        if (var1 == 254) {
            this.comMarker = var2;
        } else {
            if (var1 < 224 || var1 > 239) {
                throw new IllegalArgumentException("Invalid Marker ID:" + var1);
            }

            this.appMarkers[var1 - 224] = var2;
        }

    }

    public void addMarkerData(int var1, byte[] var2) {
        if (var2 != null) {
            if (var1 == 254) {
                this.comMarker = appendArray(this.comMarker, var2);
            } else {
                if (var1 < 224 || var1 > 239) {
                    throw new IllegalArgumentException("Invalid Marker ID:" + var1);
                }

                this.appMarkers[var1 - 224] = appendArray(this.appMarkers[var1 - 224], var2);
            }

        }
    }

    public int getEncodedColorID() {
        return this.encodedColorID;
    }

    public int getNumComponents() {
        return this.numComponents;
    }

    public static int getNumComponents(int var0) {
        if (var0 >= 0 && var0 < 12) {
            return defComponents[var0];
        } else {
            throw new IllegalArgumentException("Invalid JPEGColorID.");
        }
    }

    public int getRestartInterval() {
        return this.restartInterval;
    }

    public void setRestartInterval(int var1) {
        this.restartInterval = var1;
    }

    public int getDensityUnit() {
        if (!this.getMarker(224)) {
            throw new IllegalArgumentException("No APP0 marker present");
        } else {
            byte[] var1 = this.findAPP0();
            if (var1 == null) {
                throw new IllegalArgumentException("Can't understand APP0 marker that is present");
            } else {
                return var1[7];
            }
        }
    }

    public int getXDensity() {
        if (!this.getMarker(224)) {
            throw new IllegalArgumentException("No APP0 marker present");
        } else {
            byte[] var1 = this.findAPP0();
            if (var1 == null) {
                throw new IllegalArgumentException("Can't understand APP0 marker that is present");
            } else {
                int var2 = var1[8] << 8 | var1[9] & 255;
                return var2;
            }
        }
    }

    public int getYDensity() {
        if (!this.getMarker(224)) {
            throw new IllegalArgumentException("No APP0 marker present");
        } else {
            byte[] var1 = this.findAPP0();
            if (var1 == null) {
                throw new IllegalArgumentException("Can't understand APP0 marker that is present");
            } else {
                int var2 = var1[10] << 8 | var1[11] & 255;
                return var2;
            }
        }
    }

    public void setDensityUnit(int var1) {
        Object var2 = null;
        byte[] var3;
        if (!this.getMarker(224)) {
            var3 = createDefaultAPP0Marker();
            this.addMarkerData(224, var3);
        } else {
            var3 = this.findAPP0();
            if (var3 == null) {
                throw new IllegalArgumentException("Can't understand APP0 marker that is present");
            }
        }

        var3[7] = (byte) var1;
    }

    public void setXDensity(int var1) {
        Object var2 = null;
        byte[] var3;
        if (!this.getMarker(224)) {
            var3 = createDefaultAPP0Marker();
            this.addMarkerData(224, var3);
        } else {
            var3 = this.findAPP0();
            if (var3 == null) {
                throw new IllegalArgumentException("Can't understand APP0 marker that is present");
            }
        }

        var3[8] = (byte) (var1 >>> 8 & 255);
        var3[9] = (byte) (var1 & 255);
    }

    public void setYDensity(int var1) {
        Object var2 = null;
        byte[] var3;
        if (!this.getMarker(224)) {
            var3 = createDefaultAPP0Marker();
            this.addMarkerData(224, var3);
        } else {
            var3 = this.findAPP0();
            if (var3 == null) {
                throw new IllegalArgumentException("Can't understand APP0 marker that is present");
            }
        }

        var3[10] = (byte) (var1 >>> 8 & 255);
        var3[11] = (byte) (var1 & 255);
    }

    public void setQuality(float var1, boolean var2) {
        double var3 = (double) var1;
        if (var3 <= 0.01D) {
            var3 = 0.01D;
        }

        if (var3 > 1.0D) {
            var3 = 1.0D;
        }

        if (var3 < 0.5D) {
            var3 = 0.5D / var3;
        } else {
            var3 = 2.0D - var3 * 2.0D;
        }

        this.qTableMapping = new int[this.getNumComponents()];
        System.arraycopy(stdCompMapping[this.encodedColorID], 0, this.qTableMapping, 0, this.getNumComponents());
        JPEGQTable var5 = JPEGQTable.StdLuminance;
        this.qTables[0] = var5.getScaledInstance((float) var3, var2);
        var5 = JPEGQTable.StdChrominance;
        this.qTables[1] = var5.getScaledInstance((float) var3, var2);
        this.qTables[2] = null;
        this.qTables[3] = null;
    }

    byte[] findAPP0() {
        byte[][] var1 = (byte[][]) null;
        var1 = this.getMarkerData(224);
        if (var1 == null) {
            return null;
        } else {
            for (int var2 = 0; var2 < var1.length; ++var2) {
                if (var1[var2] != null && checkAPP0(var1[var2])) {
                    return var1[var2];
                }
            }

            return null;
        }
    }

    static boolean checkAPP0(byte[] var0) {
        if (var0.length < 14) {
            return false;
        } else if (var0[0] == 74 && var0[1] == 70 && var0[2] == 73 && var0[3] == 70 && var0[4] == 0) {
            return var0[5] >= 1;
        } else {
            return false;
        }
    }

    static byte[] createDefaultAPP0Marker() {
        byte[] var0 = new byte[]{74, 70, 73, 70, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0};
        return var0;
    }

    static byte[] copyArray(byte[] var0) {
        if (var0 == null) {
            return null;
        } else {
            byte[] var1 = new byte[var0.length];
            System.arraycopy(var0, 0, var1, 0, var0.length);
            return var1;
        }
    }

    static byte[][] copyArrays(byte[][] var0) {
        if (var0 == null) {
            return (byte[][]) null;
        } else {
            byte[][] var1 = new byte[var0.length][];

            for (int var2 = 0; var2 < var0.length; ++var2) {
                if (var0[var2] != null) {
                    var1[var2] = copyArray(var0[var2]);
                }
            }

            return var1;
        }
    }

    static byte[][] appendArray(byte[][] var0, byte[] var1) {
        int var2 = 0;
        if (var0 != null) {
            var2 = var0.length;
        }

        byte[][] var3 = new byte[var2 + 1][];

        for (int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = var0[var4];
        }

        if (var1 != null) {
            var3[var2] = copyArray(var1);
        }

        return var3;
    }

    static byte[][] buildArray(Vector var0) {
        if (var0 == null) {
            return (byte[][]) null;
        } else {
            int var1 = 0;
            byte[][] var2 = new byte[var0.size()][];
            Enumeration var3 = var0.elements();

            while (var3.hasMoreElements()) {
                byte[] var4 = (byte[]) ((byte[]) var3.nextElement());
                if (var4 != null) {
                    var2[var1++] = copyArray(var4);
                }
            }

            return var2;
        }
    }

    public static int getDefaultColorId(ColorModel var0) {
        boolean var1 = var0.hasAlpha();
        ColorSpace var2 = var0.getColorSpace();

        switch (var2.getType()) {
            case 3:
                ColorSpace var3 = null;
                try {
                    var3 = ColorSpace.getInstance(1002);
                } catch (IllegalArgumentException var5) {
                }

                if (var2 == var3) {
                    return var1 ? 10 : 5;
                }

                return var1 ? 7 : 3;

            case 5:
                if (var1) {
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
}
