package com.mypersonalupdates.log;

import java.util.Date;

public class Restrictions {

    private Integer limit, fromID;
    private Date startTimestamp, endTimestamp;
    boolean order;

    public Restrictions(Integer limit, Integer fromID, Date startTimestamp, Date endTimestamp, boolean order){
        this.limit = limit;
        this.fromID = fromID;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = startTimestamp;
        this.order = order;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public Integer getFromID() {
        return this.fromID;
    }

    public Date getStartTimestamp() {
        return this.startTimestamp;
    }

    public Date getEndTimestamp() {
        return this.endTimestamp;
    }

    public boolean isOrder() {
        return this.order;
    }
}
