package com.android.stockapp.common.dbhandler;

public class Klines {
    private String code;
    private String name;
    private String date;
    private float open;
    private float close;
    private float high;
    private float low;

    public float getClose() {
        return close;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public float getOpen() {
        return open;
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpen(float open) {
        this.open = open;
    }
}
