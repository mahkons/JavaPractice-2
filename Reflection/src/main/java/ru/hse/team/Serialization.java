package ru.hse.team;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Serialization {

    private Serialization() {}

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    public static void serialize(@NotNull Object object, @NotNull OutputStream outputStream) {


    }

    public static <T> T deserialize(@NotNull InputStream inputStream, @NotNull Class<T> clazz) {

    }
}
