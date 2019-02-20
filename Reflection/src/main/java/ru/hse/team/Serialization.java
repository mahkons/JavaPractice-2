package ru.hse.team;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * Class for serializing objects.
 * Serializes all fields
 *  including private and inherited
 * Uses reflection to get class fields
 * Class ought to have only primitive and string fields
 *   and have public constructor with no parameters
 */
public class Serialization {

    private Serialization() {}

    /**
     * Returns List of all fields in class.
     * Includes private and inherited fields
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    /**
     * Serializes given object.
     * It ought to have only primitive or string fields
     * @throws IOException when problems with writing to given output stream occurred
     * @throws IllegalArgumentException when class contains non-primitive or non-string fields
     */
    public static void serialize(@NotNull Object object, @NotNull OutputStream outputStream)
            throws IllegalAccessException, IOException {

        var dataStream = new DataOutputStream(outputStream);
        List<Field> allFields = getAllFields(object.getClass());

        for (Field field : allFields) {
            field.setAccessible(true);
            if (field.getType() == boolean.class) {
                dataStream.writeBoolean(field.getBoolean(object));
            } else if (field.getType() == byte.class) {
                dataStream.writeByte(field.getByte(object));
            } else if (field.getType() == char.class) {
                dataStream.writeChar(field.getChar(object));
            } else if (field.getType() == double.class) {
                dataStream.writeDouble(field.getDouble(object));
            } else if (field.getType() == float.class) {
                dataStream.writeFloat(field.getFloat(object));
            } else if (field.getType() == int.class) {
                dataStream.writeInt(field.getInt(object));
            } else if (field.getType() == long.class) {
                dataStream.writeLong(field.getLong(object));
            } else if (field.getType() == short.class) {
                dataStream.writeShort(field.getShort(object));
            } else if (field.getType() == String.class) {
                dataStream.writeUTF((String)field.get(object));
            } else {
                throw new IllegalArgumentException("object have a field " +
                        "which is both non-primitive and not a string");
            }
        }
    }

    /**
     * Deserializes object of given class from given input stream.
     * Class ought to have only primitive and string fields
     *  and have public constructor with no parameters
     * @throws IOException when problems with writing to given output stream occurred
     * @throws IllegalArgumentException when class contains non-primitive or non-string fields
     * @throws IllegalAccessException if constructor with no parameters is not public
     * @throws NoSuchMethodException if there is no constructor with no parameters
     * @throws InvocationTargetException if exception occured during class constructing
     */
    public static <T> T deserialize(@NotNull InputStream inputStream, @NotNull Class<T> clazz)
            throws IllegalAccessException, IOException, NoSuchMethodException,
            InstantiationException, InvocationTargetException {
        var dataStream = new DataInputStream(inputStream);
        List<Field> allFields = getAllFields(clazz);
        T object = clazz.getDeclaredConstructor().newInstance();

        for (Field field : allFields) {
            field.setAccessible(true);
            if (field.getType() == boolean.class) {
                field.setBoolean(object, dataStream.readBoolean());
            } else if (field.getType() == byte.class) {
                field.setByte(object, dataStream.readByte());
            } else if (field.getType() == char.class) {
                field.setChar(object, dataStream.readChar());
            } else if (field.getType() == double.class) {
                field.setDouble(object, dataStream.readDouble());
            } else if (field.getType() == float.class) {
                field.setFloat(object, dataStream.readFloat());
            } else if (field.getType() == int.class) {
                field.setInt(object, dataStream.readInt());
            } else if (field.getType() == long.class) {
                field.setLong(object, dataStream.readLong());
            } else if (field.getType() == short.class) {
                field.setShort(object, dataStream.readShort());
            } else if (field.getType() == String.class) {
                field.set(object, dataStream.readUTF());
            } else {
                throw new IllegalArgumentException("object have a field " +
                        "which is both non-primitive and not a string");
            }
        }
        return object;
    }
}
