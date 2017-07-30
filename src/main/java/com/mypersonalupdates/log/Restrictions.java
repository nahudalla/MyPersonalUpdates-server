package com.mypersonalupdates.log;

import java.time.Instant;

/**
 * Esta clase representa restricciones aplicables a una
 * búsqueda de actualizaciones en el log {@link Log}.
 * */
public final class Restrictions {
    private final Long limit;
    private final Long fromID;
    private final Instant startTimestamp;
    private final Instant endTimestamp;
    // Boolean (con mayúscula) ya que puede ser null en el caso que no se
    // quiera ordenar. order == true => orden ascendente;
    // order == false => orden descendente; order == null => sin orden
    private final Boolean order;

    public Restrictions(Long fromID, Instant startTimestamp, Instant endTimestamp, Boolean order, Long limit){
        this.limit = limit;
        this.fromID = fromID;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.order = order;
    }

    public Long getFromID() {
        return this.fromID;
    }

    public Long getLimit() {
        return this.limit;
    }

    public Instant getStartTimestamp() {
        return this.startTimestamp;
    }

    public Instant getEndTimestamp() {
        return this.endTimestamp;
    }

    public Boolean getOrder() {
        return this.order;
    }
}
