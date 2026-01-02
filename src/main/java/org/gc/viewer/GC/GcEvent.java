package org.gc.viewer.GC;

import org.gc.viewer.GC.GCStateEnum.GcType;

public final class GcEvent {
    public final int gcId;
    public final long uptimeMs;
    public final GcType type;
    public final long heapBeforeInBytes;
    public final long heapAfterInBytes;
    public final long heapTotalInBytes;
    public final double pauseMs;
    public final String cause;

    public GcEvent(
            int gcId,
            long uptimeMs,
            GcType type,
            long heapBeforeInBytes,
            long heapAfterInBytes,
            long heapTotalInBytes,
            double pauseMs,
            String cause) {

        this.gcId = gcId;
        this.uptimeMs = uptimeMs;
        this.type = type;
        this.heapBeforeInBytes = heapBeforeInBytes;
        this.heapAfterInBytes = heapAfterInBytes;
        this.heapTotalInBytes = heapTotalInBytes;
        this.pauseMs = pauseMs;
        this.cause = cause;
    }

    @Override
    public String toString(){
        return "GcId - " + gcId + " Gc Cycle Type - " + type + " Heap Before - " + heapAfterInBytes;
    }
}

