package com.tickstats.tickstatsapi.requestresponse;

import com.tickstats.tickstatsapi.repositories.entities.ReductedTickData;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class TickDataResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -8266174222976899732L;

    private final List<ReductedTickData> tickData;

}
