package org.gc.viewer.GC;

public class GcSummary {
    public final double totalPauseMs;
    public final double maxPauseMs;
    public final long youngCount;
    public final long mixedCount;

    public GcSummary(double totalPauseMs, double maxPauseMs, long youngCount, long mixedCount) {
        this.totalPauseMs = totalPauseMs;
        this.maxPauseMs = maxPauseMs;
        this.youngCount = youngCount;
        this.mixedCount = mixedCount;
    }
}

// "totalPauseMs": 156.4,
//    "maxPauseMs": 9.305,
//    "youngCount": 14,
//    "mixedCount": 6
