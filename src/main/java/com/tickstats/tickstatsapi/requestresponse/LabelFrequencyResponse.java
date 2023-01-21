package com.tickstats.tickstatsapi.requestresponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class LabelFrequencyResponse {
    public record LabelFrequency(String label, int frequency) implements Serializable {
        @Serial
        private static final long serialVersionUID = -6113115071502999836L;
    }
    private List<LabelFrequency> labelFrequency;
}
