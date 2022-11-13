package com.tickstats.tickstatsapi.repositories.entities;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

public record ReductedTickData(String label, Timestamp createdat) implements Serializable {

    @Serial
    private static final long serialVersionUID = 8944337624061440301L;

    public static ReductedTickData fromTickData(TickData tickData){
        return new ReductedTickData(tickData.getLabel(), tickData.getCreatedat());
    }
}

