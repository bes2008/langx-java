package com.jn.langx.util.unit;

import com.jn.langx.util.collection.Collects;

import java.util.List;
// 长度单位
public enum MetreUnit {
    /****************************************************************
     *                           公制
     ****************************************************************/
    // 皮米
    PICOMETER(1,"pm"),
    // 纳米
    NANOMETRE(1000* PICOMETER.scale, "nm"),
    // 微米
    MICROMETRE(1000 * NANOMETRE.scale, "μm"),
    // 毫米
    MILLIMETRE(1000 * MICROMETRE.scale, "mm"),
    // 米
    METRE(1000 * MILLIMETRE.scale,"m"),
    // 千米
    KILOMETRE(1000*METRE.scale,"km"),
    // 厘米
    CENTIMETRE(10* MILLIMETRE.scale,"cm"),
    // 分米
    DECIMETRE(10* CENTIMETRE.scale,"dm"),


    /****************************************************************
     *                           天文
     ****************************************************************/
    // 光秒
    LIGHT_SECONDS(299792458 * METRE.scale,"ls"),
    // 光分
    LIGHT_MINUTES(60 * LIGHT_SECONDS.scale, "lm"),
    // 光年： 365.25 * 1 光分
    LIGHT_YEAR( 525960 * LIGHT_MINUTES.scale, "ly"),

    /****************************************************************
     *                           英尺
     ****************************************************************/
    // 英吋 , 1英吋 = 2.54 厘米
    INCH(25400 * MICROMETRE.scale,"in"),
    // 英尺
    FOOT(12* INCH.scale,"ft"),
    // 码 （yard）
    YARD(3 * FOOT.scale,"yd"),
    // 弗隆（英语：furlong，furrow-long）
    FURLONG(220*YARD.scale, "fl"),
    // 英里 （mile）
    MILE(8*FURLONG.scale, "mi"),


    /****************************************************************
     *                           航海
     ****************************************************************/
    // 海里
    NAUTICAL_MILE(1852 * METRE.scale, "nmi"),

    /****************************************************************
     *                           市制
     ****************************************************************/
    // 1 公里 = 1 千米
    // 1 里 = 500 米 = 150丈
    // 1丈 = 10 尺
    // 1尺 = 10 寸,  1米 = 3尺, 大约33.33厘米
    // 1寸 = 10 分, 大约3.33厘米
    // 1分 = 10 厘, 大约3.33毫米
    // 1厘 = 10 毫
    // 除了 尺可以与米直接转换，其它要先转换成尺
    HAO(-1,1,-1,"毫"),
    LI(-1,10 * HAO.mainlandScale,-1,"厘"),
    FEN(-1,10 * LI.mainlandScale,-1,"分"),
    CHI(-1, 10 * FEN.mainlandScale,-1,"寸"),
    ZHANG(-1, 10 * CHI.mainlandScale,-1,"丈"),

    /****************************************************************
     *                           台制
     ****************************************************************/
    // 1台丈 = 10 台尺
    // 1台尺 = 10 台寸,  1米 = 3尺
    // 1台寸 = 10 台分， 1台尺 = 303 毫米， 3台尺 = 909 毫米
    TAI_CUN(-1,-1,1,"台寸"),
    TAI_CHI(-1,-1,10* TAI_CUN.taiwanScale,"台尺"),
    TAI_ZHANG(-1,-1,10* TAI_CHI.taiwanScale,"台丈"),
    ;

    private List<String> symbols;


    // 公制尺寸
    private long scale=-1;
    // 市制尺寸
    private long mainlandScale = -1;
    // 台尺
    private long taiwanScale = -1;

    private MetreUnit(long scale, String... symbols){
        this(scale, -1, -1, symbols);
    }
    private MetreUnit(long scale, long mainlandScale, long taiwanScale, String... symbols){
        this.scale=scale;
        this.mainlandScale = mainlandScale;
        this.taiwanScale = taiwanScale;
        this.symbols = Collects.asList(symbols);
    }
    public List<String> getSymbols() {
        return symbols;
    }

    public String getStandardSymbol(){
        return getSymbols().get(0);
    }

    /**
     * 公制之间转换， 市制之间转换，台制之间转换均可使用。
     * 公制、台制、市制之间转换不能使用该方法
     * @param n   长度的数值
     * @param dstScale result unit scale
     * @param srcScale source unit scale
     */
    private static double cvtScale(double n, long srcScale, long dstScale) {
        if (srcScale == dstScale) {
            return n;
        }
        return n * srcScale / dstScale;
    }

    private static double mainlandToNonChineseMetre(long src, MetreUnit srcUnit, MetreUnit dstUnit){
        if(srcUnit==null || !srcUnit.isChineseMainlandMetre()){
            throw new IllegalArgumentException("need a chinese mainland metre scale");
        }
        if(dstUnit==null || dstUnit.isChineseMetre()){
            throw new IllegalArgumentException("need a non-chinese metre scale");
        }
        // 先转换成 市尺
        double mainlandChiMetre = cvtScale(src, srcUnit.mainlandScale, CHI.mainlandScale);
        // 再转换为 公制：米
        double metre = mainlandChiMetre /3;

        // 再转换成 目标公制
        double result = cvtScale(metre, METRE.scale, dstUnit.scale);
        return result;
    }

    private static double nonChineseToMainlandMetre(double src, MetreUnit srcUnit, MetreUnit dstUnit){
        if(srcUnit==null || srcUnit.isChineseMetre()){
            throw new IllegalArgumentException("need a non-chinese metre scale");
        }
        if(dstUnit==null || !dstUnit.isChineseMainlandMetre()){
            throw new IllegalArgumentException("need a chinese mainland metre scale");
        }
        // 先转换成 公制：米
        double metre = cvtScale(src, srcUnit.scale, METRE.scale);
        // 再转换为 市制：尺
        double chi = metre * 3;
        // 再转换成 目标市制
        double result = cvtScale(chi, METRE.mainlandScale, dstUnit.mainlandScale);
        return result;
    }

    private static double taiwanToNonChineseMetre(double src, MetreUnit srcUnit, MetreUnit dstUnit){
        if(srcUnit==null || !srcUnit.isChineseTaiwanMetre()){
            throw new IllegalArgumentException("need a chinese taiwan metre scale");
        }
        if(dstUnit==null || dstUnit.isChineseMetre()){
            throw new IllegalArgumentException("need a non-chinese metre scale");
        }
        // 先转换成 台尺
        double taiChiMetre = cvtScale(src, srcUnit.taiwanScale, TAI_CHI.taiwanScale);
        // 再转换为 公制：毫米
        double millsMetre = taiChiMetre * 303;

        // 再转换成 目标公制
        double result = cvtScale(millsMetre, MILLIMETRE.scale, dstUnit.scale);
        return result;
    }

    private static double nonChineseToTaiwanMetre(double src, MetreUnit srcUnit, MetreUnit dstUnit){
        if(srcUnit==null || srcUnit.isChineseMetre()){
            throw new IllegalArgumentException("need a non-chinese metre scale");
        }
        if(dstUnit==null || !dstUnit.isChineseTaiwanMetre()){
            throw new IllegalArgumentException("need a chinese taiwan metre scale");
        }
        // 先转换成 公制：毫米
        double metre = cvtScale(src, srcUnit.scale, MILLIMETRE.scale);
        // 再转换为 台尺
        double taiChi = metre / 303;
        // 再转换成 目标台制
        double result = cvtScale(taiChi, METRE.taiwanScale, dstUnit.taiwanScale);
        return result;
    }

    public long toNanoMetre(long value){
        double result;
        if(isChineseMetre()){
            if(isChineseMainlandMetre()){
                result= mainlandToNonChineseMetre(value, this, NANOMETRE);
            }else{
                result= taiwanToNonChineseMetre(value, this, NANOMETRE);
            }
        }else{
            result= cvtScale(value, this.scale, NANOMETRE.scale);
        }
        return new Double(result).longValue();
    }

    public double toMillsMetre(long value){
        long nanoMetre=toNanoMetre(value);
        return cvtScale(nanoMetre, NANOMETRE.scale, MILLIMETRE.scale);
    }


    // 转换成 米
    public double toMetre(long n){
        long nanoMetre = toNanoMetre(n);
        return cvtScale(nanoMetre, MetreUnit.MILLIMETRE.scale, MetreUnit.METRE.scale);
    }

    public double to(long n, MetreUnit dstUnit){
        if(isChineseMetre()){
            if(isChineseMainlandMetre()){
                if(dstUnit.isChineseMainlandMetre()){
                    return cvtScale(n, this.mainlandScale, dstUnit.mainlandScale);
                }else if(dstUnit.isChineseTaiwanMetre()){
                    // 转换成毫米
                    double millsMetre = toMillsMetre(n);
                    // 再转换成台制
                    return nonChineseToTaiwanMetre(millsMetre, MILLIMETRE, dstUnit);
                }else{
                    return mainlandToNonChineseMetre(n, this, dstUnit);
                }
            }else{
                // 台制

                if(dstUnit.isChineseTaiwanMetre()){
                    return cvtScale(n, this.taiwanScale, dstUnit.taiwanScale);
                }
                else if(dstUnit.isChineseMainlandMetre()){
                    // 先转换成毫米
                    double millsMetre = toMillsMetre(n);
                    // 再转换成市制
                    return nonChineseToMainlandMetre(millsMetre, MILLIMETRE, dstUnit);
                }else{
                    return taiwanToNonChineseMetre(n, this, dstUnit);
                }

            }
        }else{
            if(dstUnit.isChineseMetre()){
                if(dstUnit.isChineseMainlandMetre()){
                    return nonChineseToMainlandMetre(n, this, dstUnit);
                }else{
                    return nonChineseToTaiwanMetre(n, this, dstUnit);
                }
            }else{
                return cvtScale(n, this.scale, dstUnit.scale);
            }
        }

    }

    public boolean isChineseMetre(){
        return isChineseMainlandMetre() || isChineseTaiwanMetre();
    }

    public boolean isChineseMainlandMetre(){
        return this.mainlandScale>0;
    }

    public boolean isChineseTaiwanMetre(){
        return this.taiwanScale>0;
    }


}
