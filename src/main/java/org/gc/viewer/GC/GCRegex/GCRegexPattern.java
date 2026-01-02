package org.gc.viewer.GC.GCRegex;

import java.util.regex.Pattern;

public class GCRegexPattern {
    public static final Pattern GC_ID_PATTERN =
            Pattern.compile("GC\\((\\d+)\\)");

    public static final Pattern UPTIME_PATTERN =
            Pattern.compile("\\[(\\d+\\.\\d+)s\\]");

    public static final Pattern PAUSE_PATTERN =
            Pattern.compile(
                    "GC\\((\\d+)\\) Pause Young \\(([^)]+)\\) \\(([^)]+)\\) " +
                            "(\\d+)([BKM])->(\\d+)([BKM])\\((\\d+)([BKM])\\) ([\\d.]+)(ms|s)"
            );

    public static final Pattern MAX_HEAP_SIZE =
            Pattern.compile(
                    "Heap Max Capacity: (\\d+)([BKM])"
            );

    public static final Pattern GC_USAGE =
            Pattern.compile("Using\\s+(\\w+)");

    public static final Pattern JVM_USED_REGEX =
            Pattern.compile("Version:\\s+(\\S+)");
}
