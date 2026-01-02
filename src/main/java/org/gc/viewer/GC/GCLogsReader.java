package org.gc.viewer.GC;

import org.gc.viewer.GC.GCStateEnum.GcType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class GCLogsReader {
    public static void read() {
        Path path = Path.of("src/main/resources/logs/gc.log");
        Path outputPath = Path.of("visual/gc-report.json");


        GcParser gcParser = new G1UnifiedGcParser();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                gcParser.accept(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GCJsonObject gcJsonObject = new GCJsonObject();
        List<GcEvent> gcEvents = gcParser.getEvents();
        gcEvents.sort(Comparator.comparingLong(a -> a.gcId));
        gcJsonObject.events = gcEvents;
        gcJsonObject.meta = gcParser.getGcMetaData();

        double maxPauseInMs = gcEvents.stream().mapToDouble(a -> a.pauseMs)
                .max()
                .orElse(0.0);
        double totalPauseInMs = gcEvents
                .stream().mapToDouble(a -> a.pauseMs).sum();

        long numberOfYoungCount = gcEvents.
                stream().filter(gcEvent ->
                        (gcEvent.type == GcType.YOUNG_NORMAL ||
                                gcEvent.type == GcType.YOUNG_CONCURRENT_START ||
                                gcEvent.type == GcType.YOUNG_PREPARE_MIXED))
                .count();

        long numberOfMixedCount = gcEvents.
                stream().filter(gcEvent -> gcEvent.type == GcType.MIXED)
                .count();

        gcJsonObject.summary = new GcSummary(totalPauseInMs, maxPauseInMs, numberOfYoungCount, numberOfMixedCount);
        String jsonString = JavaUtilsWrite.toJson(gcJsonObject);
        System.out.println(jsonString);
        JavaUtilsWrite.writeToFile(gcJsonObject, outputPath);
    }
}


class GCJsonObject {
    public List<GcEvent> events;
    public GcMetaData meta;
    public GcSummary summary;
}