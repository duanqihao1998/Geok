package com.geok.langfang.shuju;

/**
 * Created by ydb on 2018/4/4.
 */

public class GjdBean {
    /**
     * CHARARRIVALTIME : 2018-04-04 09:04:32
     * DEPARTMENTID : 3a998501-7b4c-11e7-9dd3-e41f13e34d4c
     * EVENTID : 8ca66184-3747-11e8-9aa3-e41f13e34b20
     * FREQINDEX : 1
     * LAT : 35.98157167
     * LINELOOPEVENTID : 1a93fe00-736f-11e7-b6c4-e41f13e34d4c
     * LINENAME : 兰定支线
     * LON : 103.84202833
     * POINTID : 1a68f880-fcfa-11e7-9d4c-0050568525ba
     * POINTNAME : 兰定线k47+100穿越大面积果园。每日必巡点。
     * POINTPOSITION : 兰定支线 K047+100.00
     * POINTSTATUS : 1
     * STATION : 39879
     */

    private String CHARARRIVALTIME;//日期
    private String DEPARTMENTID;//部门ID
    private String EVENTID;//唯一标示（数据）
    private String FREQINDEX;//巡检次数
    private String LAT;//纬度
    private String LINELOOPEVENTID;//管线ID
    private String LINENAME;//管线名称
    private String LON;//经度
    private String POINTID;//关键点ID
    private String POINTNAME;//关键点名称
    private String POINTPOSITION;//位置描述
    private String POINTSTATUS;//关键点状态
    private String STATION;//里程值
    private String UNITNAME;//所属部门
    private String INSPECTOR;//巡检人

    public GjdBean(String CHARARRIVALTIME, String DEPARTMENTID, String EVENTID, String FREQINDEX, String LAT, String LINELOOPEVENTID, String LINENAME, String LON, String POINTID, String POINTNAME, String POINTPOSITION, String POINTSTATUS, String STATION, String UNITNAME, String INSPECTOR) {
        this.CHARARRIVALTIME = CHARARRIVALTIME;
        this.DEPARTMENTID = DEPARTMENTID;
        this.EVENTID = EVENTID;
        this.FREQINDEX = FREQINDEX;
        this.LAT = LAT;
        this.LINELOOPEVENTID = LINELOOPEVENTID;
        this.LINENAME = LINENAME;
        this.LON = LON;
        this.POINTID = POINTID;
        this.POINTNAME = POINTNAME;
        this.POINTPOSITION = POINTPOSITION;
        this.POINTSTATUS = POINTSTATUS;
        this.STATION = STATION;
        this.UNITNAME = UNITNAME;
        this.INSPECTOR = INSPECTOR;
    }

    public String getUNITNAME() {
        return UNITNAME;
    }

    public void setUNITNAME(String UNITNAME) {
        this.UNITNAME = UNITNAME;
    }

    public String getINSPECTOR() {
        return INSPECTOR;
    }

    public void setINSPECTOR(String INSPECTOR) {
        this.INSPECTOR = INSPECTOR;
    }

    public String getCHARARRIVALTIME() {
        return CHARARRIVALTIME;
    }

    public void setCHARARRIVALTIME(String CHARARRIVALTIME) {
        this.CHARARRIVALTIME = CHARARRIVALTIME;
    }

    public String getDEPARTMENTID() {
        return DEPARTMENTID;
    }

    public void setDEPARTMENTID(String DEPARTMENTID) {
        this.DEPARTMENTID = DEPARTMENTID;
    }

    public String getEVENTID() {
        return EVENTID;
    }

    public void setEVENTID(String EVENTID) {
        this.EVENTID = EVENTID;
    }

    public String getFREQINDEX() {
        return FREQINDEX;
    }

    public void setFREQINDEX(String FREQINDEX) {
        this.FREQINDEX = FREQINDEX;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLINELOOPEVENTID() {
        return LINELOOPEVENTID;
    }

    public void setLINELOOPEVENTID(String LINELOOPEVENTID) {
        this.LINELOOPEVENTID = LINELOOPEVENTID;
    }

    public String getLINENAME() {
        return LINENAME;
    }

    public void setLINENAME(String LINENAME) {
        this.LINENAME = LINENAME;
    }

    public String getLON() {
        return LON;
    }

    public void setLON(String LON) {
        this.LON = LON;
    }

    public String getPOINTID() {
        return POINTID;
    }

    public void setPOINTID(String POINTID) {
        this.POINTID = POINTID;
    }

    public String getPOINTNAME() {
        return POINTNAME;
    }

    public void setPOINTNAME(String POINTNAME) {
        this.POINTNAME = POINTNAME;
    }

    public String getPOINTPOSITION() {
        return POINTPOSITION;
    }

    public void setPOINTPOSITION(String POINTPOSITION) {
        this.POINTPOSITION = POINTPOSITION;
    }

    public String getPOINTSTATUS() {
        return POINTSTATUS;
    }

    public void setPOINTSTATUS(String POINTSTATUS) {
        this.POINTSTATUS = POINTSTATUS;
    }

    public String getSTATION() {
        return STATION;
    }

    public void setSTATION(String STATION) {
        this.STATION = STATION;
    }
}
