package com.mypersonalupdates;

import java.util.Date;

public class Restriction {
    private int limit;
    private Date startTimestamp, endTimestamp;
    private boolean order;

    public Restriction(int limit, Date startTimestamp, Date endTimestamp, boolean order) {
        this.limit = limit;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.order = order;
    }

    public int getLimit() {
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

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void setEndTimestamp(Date endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }
}
