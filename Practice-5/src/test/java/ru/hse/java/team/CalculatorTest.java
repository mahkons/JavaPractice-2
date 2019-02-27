package ru.hse.java.team;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculatorTest {

    private static final double EPSILON = 1e-6;

    @Test
    void simplePlusOperation() {
        Stack<Double> mockedStack = mock(Stack.class);
        when(mockedStack.pop()).thenReturn((double) 3,(double) 2, (double) 5);

        InOrder inOrder = inOrder(mockedStack);
        inOrder.verify(mockedStack).push((double) 2);
        inOrder.verify(mockedStack).push((double) 3);
        inOrder.verify(mockedStack).push((double) 5);

        var calculator = new Calculator(mockedStack);
        assertEquals(5.0, calculator.evaluate("2 3 +"), EPSILON);
    }

    @Test
    void simpleMinusOperation() {
        Stack<Double> mockedStack = mock(Stack.class);
        when(mockedStack.pop()).thenReturn((double) 3, (double) 2, (double) -1);

        InOrder inOrder = inOrder(mockedStack);
        inOrder.verify(mockedStack).push((double) 2);
        inOrder.verify(mockedStack).push((double) 3);
        inOrder.verify(mockedStack).push((double) -1);

        var calculator = new Calculator(mockedStack);
        assertEquals(-1, calculator.evaluate("2 3 -"));
    }

    @Test
    void numberWithUnaryMinus() {
        Stack<Double> mockedStack = mock(Stack.class);
        when(mockedStack.pop()).thenReturn((double) -1);

        InOrder inOrder = inOrder(mockedStack);
        inOrder.verify(mockedStack).push((double) -1);

        var calculator = new Calculator(mockedStack);
        assertEquals(-1, calculator.evaluate("-1"));
    }

    @Test
    void twoOperations() {
        Stack<Double> mockedStack = mock(Stack.class);
        when(mockedStack.pop()).thenReturn((double) 3, (double) 2, (double) 2,
                (double) 5, (double) 7);

        InOrder inOrder = inOrder(mockedStack);
        inOrder.verify(mockedStack).push((double) 2);
        inOrder.verify(mockedStack).push((double) 3);
        inOrder.verify(mockedStack).push((double) 5);
        inOrder.verify(mockedStack).push((double) 2);
        inOrder.verify(mockedStack).push((double) 7);

        var calculator = new Calculator(mockedStack);
        assertEquals(-1, calculator.evaluate("2 3 + 2 +"));
    }
}