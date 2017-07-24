package com.mypersonalupdates.log;

import java.util.Date;

public class Restrictions {

    private Integer limit, fromID;
    private Date startTimestamp, endTimestamp;
    private boolean order;

    public Restrictions(Integer fromID, Date startTimestamp, Date endTimestamp, boolean order, Integer limit){
        this.limit = limit;
        this.fromID = fromID;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.order = order;
    }

    public Integer getFromID() {
        return this.fromID;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public Date getStartTimestamp() {
        return this.startTimestamp;
    }

    public Date getEndTimestamp() {
        return this.endTimestamp;
    }

    public boolean getOrder() {
        return this.order;
    }
}
