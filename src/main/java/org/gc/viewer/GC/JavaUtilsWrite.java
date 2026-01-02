package org.gc.viewer.GC;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.nio.file.Path;

public class JavaUtilsWrite {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static String toJson(Object events) {
        try {
            return MAPPER.writeValueAsString(events);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize GC events", e);
        }
    }

    public static void writeToFile(Object object, Path path){
        try {
            MAPPER.writeValue(path.toFile(), object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize GC events", e);
        }
    }
}
