package com.redPackets.demo.util;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 簡單封裝一下BigDecimal四種基礎運算
 */
public class BigDecimalUtil {


    //加法
    public static BigDecimal add(BigDecimal a, BigDecimal b){
        return a.add(b, MathContext.DECIMAL128);
    }
    //減法
    public static BigDecimal subtract(BigDecimal a,BigDecimal b){
        return a.subtract(b,MathContext.DECIMAL128);
    }
    //乘法
    public static BigDecimal mutiply(BigDecimal a, BigDecimal b){
        return a.multiply(b,MathContext.DECIMAL128);
    }
    //除法
    public static BigDecimal divide(BigDecimal a,BigDecimal b){
        return a.divide(b,MathContext.DECIMAL128);
    }
}
