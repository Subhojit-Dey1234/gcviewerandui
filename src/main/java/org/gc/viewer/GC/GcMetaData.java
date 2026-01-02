package org.gc.viewer.GC;

public final class GcMetaData {
    public final long heapMaxMb;
    public final String jvm;
    public final String gc;

    public GcMetaData(long heapMaxMb, String jvm, String gc) {
        this.heapMaxMb = heapMaxMb;
        this.jvm = jvm;
        this.gc = gc;
    }
}