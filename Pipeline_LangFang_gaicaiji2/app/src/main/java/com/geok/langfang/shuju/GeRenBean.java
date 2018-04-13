package com.geok.langfang.shuju;

/**
 * Created by ydb on 2018/4/4.
 */

public class GeRenBean {

    /**
     * ALLPOINTCOUNT : 18
     * DOPOINTCOUNT : 18
     * EVENTID : def72960-56ec-11e7-a408-28c2dd4c4740
     * INSPECTORID : d8779889-2168-11e5-aef8-005056855933
     * INSTASKLENGTH : 18
     * NAME : 林如荣
     * NOTPOINT : 0
     * TASKID : 169fb390-568a-11e7-a0cf-e41f13e34b20
     * UNITID : 42e8b040-b590-11e2-8f47-e41f13e36064
     * UNITNAME : 古浪作业区
     */

    private String ALLPOINTCOUNT;//总关键点数
    private String DOPOINTCOUNT;//已巡关键点数
    private String EVENTID;//唯一标识
    private String INSPECTORID;//人员ID
    private String INSTASKLENGTH;//巡检里程
    private String NAME;//巡检人
    private String NOTPOINT;//未巡关键点数
    private String TASKID;//任务ID
    private String UNITID;//部门ID
    private String UNITNAME;//部门名字

    public GeRenBean(String ALLPOINTCOUNT, String DOPOINTCOUNT, String EVENTID, String INSPECTORID, String INSTASKLENGTH, String NAME, String NOTPOINT, String TASKID, String UNITID, String UNITNAME) {
        this.ALLPOINTCOUNT = ALLPOINTCOUNT;
        this.DOPOINTCOUNT = DOPOINTCOUNT;
        this.EVENTID = EVENTID;
        this.INSPECTORID = INSPECTORID;
        this.INSTASKLENGTH = INSTASKLENGTH;
        this.NAME = NAME;
        this.NOTPOINT = NOTPOINT;
        this.TASKID = TASKID;
        this.UNITID = UNITID;
        this.UNITNAME = UNITNAME;
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

    public String getINSPECTORID() {
        return INSPECTORID;
    }

    public void setINSPECTORID(String INSPECTORID) {
        this.INSPECTORID = INSPECTORID;
    }

    public String getINSTASKLENGTH() {
        return INSTASKLENGTH;
    }

    public void setINSTASKLENGTH(String INSTASKLENGTH) {
        this.INSTASKLENGTH = INSTASKLENGTH;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getNOTPOINT() {
        return NOTPOINT;
    }

    public void setNOTPOINT(String NOTPOINT) {
        this.NOTPOINT = NOTPOINT;
    }

    public String getTASKID() {
        return TASKID;
    }

    public void setTASKID(String TASKID) {
        this.TASKID = TASKID;
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
}
