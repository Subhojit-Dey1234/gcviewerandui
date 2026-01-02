package org.gc.viewer.GC.GCStateEnum;

public enum GcType {
    // Young-only pauses
    YOUNG_NORMAL,
    YOUNG_CONCURRENT_START,
    YOUNG_PREPARE_MIXED,

    // Mixed phase
    MIXED,

    // Full GC (rare but important)
    FULL,

    // Fallback
    UNKNOWN
}

