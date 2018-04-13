package com.geok.langfang.shuju;

/**
 * Created by ydb on 2018/4/3.
 */

public class BumenBean {

    /**
     * ALLPOINTCOUNT : 538
     * DOPOINTCOUNT : 522
     * EVENTID : 54135722-5849-11e7-8d5e-28c2dd4c4740
     * NOTASKUSERCOUNT : 0
     * TASKUSERCOUNT : 40
     * UNITID : 12f6d2e0-b590-11e2-8f47-e41f13e36064
     * UNITNAME : 永昌作业区
     * USERCOUNT : 40
     */

    private String ALLPOINTCOUNT;
    private String DOPOINTCOUNT;
    private String EVENTID;
    private String NOTASKUSERCOUNT;
    private String TASKUSERCOUNT;
    private String UNITID;
    private String UNITNAME;
    private String USERCOUNT;

    public BumenBean(String ALLPOINTCOUNT, String DOPOINTCOUNT, String EVENTID, String NOTASKUSERCOUNT, String TASKUSERCOUNT, String UNITID, String UNITNAME, String USERCOUNT) {
        this.ALLPOINTCOUNT = ALLPOINTCOUNT;
        this.DOPOINTCOUNT = DOPOINTCOUNT;
        this.EVENTID = EVENTID;
        this.NOTASKUSERCOUNT = NOTASKUSERCOUNT;
        this.TASKUSERCOUNT = TASKUSERCOUNT;
        this.UNITID = UNITID;
        this.UNITNAME = UNITNAME;
        this.USERCOUNT = USERCOUNT;
    }

    public String getALLPOINTCOUNT() {
        return ALLPOINTCOUNT;
    }

    public void setALLPOINTCOUNT(String ALLPOINTCOUNT) {
        this.ALLPOINTCOUNT = ALLPOINTCOUNT;
    }

    public String getDOPOINTCOUNT() {
        return DOPOINTCOUNT;
    }

    public void setDOPOINTCOUNT(String DOPOINTCOUNT) {
        this.DOPOINTCOUNT = DOPOINTCOUNT;
    }

    public String getEVENTID() {
        return EVENTID;
    }

    public void setEVENTID(String EVENTID) {
        this.EVENTID = EVENTID;
    }

    public String getNOTASKUSERCOUNT() {
        return NOTASKUSERCOUNT;
    }

    public void setNOTASKUSERCOUNT(String NOTASKUSERCOUNT) {
        this.NOTASKUSERCOUNT = NOTASKUSERCOUNT;
    }

    public String getTASKUSERCOUNT() {
        return TASKUSERCOUNT;
    }

    public void setTASKUSERCOUNT(String TASKUSERCOUNT) {
        this.TASKUSERCOUNT = TASKUSERCOUNT;
    }

    public String getUNITID() {
        return UNITID;
    }

    public void setUNITID(String UNITID) {
        this.UNITID = UNITID;
    }

    public String getUNITNAME() {
        return UNITNAME;
    }

    public void setUNITNAME(String UNITNAME) {
        this.UNITNAME = UNITNAME;
    }

    public String getUSERCOUNT() {
        return USERCOUNT;
    }

    public void setUSERCOUNT(String USERCOUNT) {
        this.USERCOUNT = USERCOUNT;
    }
}
