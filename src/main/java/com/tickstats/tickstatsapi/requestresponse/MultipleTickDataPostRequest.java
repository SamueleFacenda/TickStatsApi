package com.tickstats.tickstatsapi.requestresponse;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class MultipleTickDataPostRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7687873795807765915L;

    private final List<TickDataPostRequest> tickData;

}

