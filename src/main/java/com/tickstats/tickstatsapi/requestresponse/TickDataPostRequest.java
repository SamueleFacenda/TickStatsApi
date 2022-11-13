package com.tickstats.tickstatsapi.requestresponse;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TickDataPostRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2213181659923711475L;

    private String label, comment = "", timestamp;
}
