package com.geok.langfang.jsonbean;

/**
 * Created by ydb on 2018/3/12.
 */

public class GpsHui {
    String LON;
    String LAT;

    public GpsHui(String LON, String LAT) {
        this.LON = LON;
        this.LAT = LAT;
    }

    public GpsHui() {
    }

    public String getLON() {
        return LON;
    }

    public void setLON(String LON) {
        this.LON = LON;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }
}
