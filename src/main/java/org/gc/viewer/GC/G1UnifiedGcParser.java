package org.gc.viewer.GC;

import org.gc.viewer.GC.GCStateEnum.GcType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.gc.viewer.GC.GCRegex.GCRegexPattern.*;

public class G1UnifiedGcParser implements GcParser {

    private final List<GcEvent> gcEvents = new ArrayList<>();
    private GcMetaData gcMetaData = null;
    private String gcType;
    private String jvmType;

    @Override
    public void accept(String line) {
        setGcType(line);
        setJvmUsedRegex(line);
        getHeapMaxCapacity(line);
        getGcEvents(line);
    }

    private void setGcType(String line){
        if(gcType != null) return;

        Matcher gc = GC_USAGE.matcher(line);
        if(!gc.find()) return;
        gcType = gc.group(1);
    }

    private void setJvmUsedRegex(String line){
        if(jvmType != null) return;

        Matcher jvm = JVM_USED_REGEX.matcher(line);
        if(!jvm.find()) return;

        jvmType = jvm.group(1);
    }

    private void getHeapMaxCapacity(String line) {
        if (gcMetaData != null) return;

        Matcher heapCapacity = MAX_HEAP_SIZE.matcher(line);
        if(!heapCapacity.find()) return;

        long maxHeap = Long.parseLong(heapCapacity.group(1));
        gcMetaData = new GcMetaData(maxHeap, jvmType, gcType);
    }

    private void getGcEvents(String line){
        Matcher pauseMatcher = PAUSE_PATTERN.matcher(line);
        if (!pauseMatcher.find()) return;

        try {
            int gcId = Integer.parseInt(pauseMatcher.group(1));
            GcType gcType = getGcType(pauseMatcher.group(2));
            String gcCause = pauseMatcher.group(3);

            long heapBeforeGc = getMemoryUnits(pauseMatcher.group(4), pauseMatcher.group(5));
            long heapAfterGc = getMemoryUnits(pauseMatcher.group(6), pauseMatcher.group(7));

            long heapTotal = getMemoryUnits(pauseMatcher.group(8), pauseMatcher.group(9));
            double pauseTime = pauseGcInMilliSeconds(pauseMatcher.group(10), pauseMatcher.group(11));
            long uptimeMs = extractUptimeMillis(line);

            GcEvent gcEvent = new GcEvent(
                    gcId,
                    uptimeMs,
                    gcType,
                    heapBeforeGc,
                    heapAfterGc,
                    heapTotal,
                    pauseTime,
                    gcCause
            );
            gcEvents.add(gcEvent);
        } catch (Exception e) {
            System.out.println("Skipping the line because of error -> " + e.getMessage());
        }
    }

    private static long extractUptimeMillis(String line) {
        Matcher m = UPTIME_PATTERN.matcher(line);
        if (!m.find()) return -1;
        return (long) (Double.parseDouble(m.group(1)) * 1000);
    }

    private static long getMemoryUnits(String memory, String unit) {
        return switch (unit) {
            case "B" -> Long.parseLong(memory);
            case "K" -> Long.parseLong(memory) * 1024;
            case "M" -> Long.parseLong(memory) * 1024 * 1024;
            default -> throw new IllegalArgumentException("Unknown unit: " + unit);
        };
    }

    private static double pauseGcInMilliSeconds(String time, String unit) {
        return switch (unit) {
            case "s" -> Double.parseDouble(time) * 1000;
            case "ms" -> Double.parseDouble(time);
            default -> throw new IllegalArgumentException("Unknown unit: " + unit);
        };
    }

    private static GcType getGcType(String phase) {
        return switch (phase) {
            case "Normal" -> GcType.YOUNG_NORMAL;
            case "Concurrent Start" -> GcType.YOUNG_CONCURRENT_START;
            case "Prepare Mixed" -> GcType.YOUNG_PREPARE_MIXED;
            case "Mixed" -> GcType.MIXED;
            case "Full" -> GcType.FULL;
            default -> GcType.UNKNOWN;
        };
    }

    @Override
    public List<GcEvent> getEvents() {
        return gcEvents;
    }

    @Override
    public GcMetaData getGcMetaData() {
        return gcMetaData;
    }

}
