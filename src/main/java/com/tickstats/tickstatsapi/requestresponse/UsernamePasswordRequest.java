package com.tickstats.tickstatsapi.requestresponse;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data @NoArgsConstructor
public class UsernamePasswordRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5926468583005150707L;

    private String username, password;
}