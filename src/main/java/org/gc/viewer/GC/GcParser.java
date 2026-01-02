package org.gc.viewer.GC;

import java.util.List;

public interface GcParser {
    void accept(String line);
    List<GcEvent> getEvents();
    GcMetaData getGcMetaData();
}
