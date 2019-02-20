package ru.hse.team;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SerializationTest {

    public static class BoolAndIntValue {
        boolean flag = true;
        private final static int value = 179;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BoolAndIntValue)) return false;
            BoolAndIntValue that = (BoolAndIntValue) o;
            return flag == that.flag &&
                    value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(flag, value);
        }
    }

    public static class PrivateLongAdded {
        private long some = 579;

        public long getSome() {
            return some;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PrivateLongAdded)) return false;
            PrivateLongAdded that = (PrivateLongAdded) o;
            return getSome() == that.getSome();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getSome());
        }
    }

    public static class LabeledValue extends BoolAndIntValue {
        protected String best = "The Great String!";

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LabeledValue)) return false;
            if (!super.equals(o)) return false;
            LabeledValue that = (LabeledValue) o;
            return best.equals(that.best);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), best);
        }
    }

    public static class BigClass {
        public boolean a = false;
        public byte b = 10;
        public char c = 'z';
        protected double d = 1.0;
        protected float e = -2.0f;
        protected int f = Integer.MIN_VALUE;
        private long g = Long.MAX_VALUE;
        private short h = 0;
        private String i = "Label\nHA!";

        public long getG() {
            return g;
        }

        public short getH() {
            return h;
        }

        public String getI() {
            return i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BigClass)) return false;
            BigClass bigClass = (BigClass) o;
            return a == bigClass.a &&
                    b == bigClass.b &&
                    c == bigClass.c &&
                    Double.compare(bigClass.d, d) == 0 &&
                    Float.compare(bigClass.e, e) == 0 &&
                    f == bigClass.f &&
                    getG() == bigClass.getG() &&
                    getH() == bigClass.getH() &&
                    getI().equals(bigClass.getI());
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d, e, f, getG(), getH(), getI());
        }
    }

    private BoolAndIntValue boolAndIntValue = new BoolAndIntValue();
    private PrivateLongAdded privateLongAdded = new PrivateLongAdded();
    private LabeledValue labeledValue = new LabeledValue();
    private BigClass bigClass = new BigClass();

    @Test
    void serializeAndDeserialize_simple()
            throws IllegalAccessException, IOException, NoSuchMethodException, InstantiationException, InvocationTargetException, NoSuchFieldException {
        var byteArrayOutputStream = new ByteArrayOutputStream(1000);
        Serialization.serialize(boolAndIntValue, byteArrayOutputStream);
        assertEquals(boolAndIntValue, Serialization.deserialize(
                new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), BoolAndIntValue.class));
    }

    @Test
    void serializeAndDeserialize_withPrivate()
            throws IllegalAccessException, IOException, NoSuchMethodException, InstantiationException, InvocationTargetException, NoSuchFieldException {
        var byteArrayOutputStream = new ByteArrayOutputStream(1000);
        Serialization.serialize(privateLongAdded, byteArrayOutputStream);
        assertEquals(privateLongAdded, Serialization.deserialize(
                new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), PrivateLongAdded.class));
    }

    @Test
    void serializeAndDeserialize_inheritance()
            throws IllegalAccessException, IOException, NoSuchMethodException, InstantiationException, InvocationTargetException, NoSuchFieldException {
        var byteArrayOutputStream = new ByteArrayOutputStream(1000);
        Serialization.serialize(labeledValue, byteArrayOutputStream);
        assertEquals(labeledValue, Serialization.deserialize(
                new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), LabeledValue.class));
    }

    @Test
    void serializeAndDeserialize_allProbableFieldTypes()
            throws IllegalAccessException, IOException, NoSuchMethodException, InstantiationException, InvocationTargetException, NoSuchFieldException {
        var byteArrayOutputStream = new ByteArrayOutputStream(1000);
        Serialization.serialize(bigClass, byteArrayOutputStream);
        assertEquals(bigClass, Serialization.deserialize(
                new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), BigClass.class));
    }

}