package com.mypersonalupdates.log;

import java.util.Date;

public class Restrictions {
    // TODO: usar Integer
    private int limit;
    private Date startTimestamp, endTimestamp;
    private boolean order;
    // TODO: agregar fromID

    // TODO: rehacer segun diagrama
    public Restrictions(int limit, Date startTimestamp, Date endTimestamp, boolean order) {
        this.limit = limit;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.order = order;
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

    // TODO: arreglar Boolean->boolean en diagrama
    public boolean getOrder() {
        return this.order;
    }

    // TODO: agregar getFromID
}
