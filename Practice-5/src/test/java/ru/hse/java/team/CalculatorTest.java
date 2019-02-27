package ru.hse.java.team;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class CalculatorTest {

    @Test
    void simplePlusOperation() {
        Stack<Integer> mockedStack = mock(Stack.class);
        when(mockedStack.pop()).thenReturn(3,2, 5);
        when(mockedStack.size()).thenReturn(2, 1);

        var calculator = new Calculator(mockedStack);
        assertEquals(5, calculator.evaluate("2 3 +"));

        InOrder inOrder = inOrder(mockedStack);
        inOrder.verify(mockedStack).push(2);
        inOrder.verify(mockedStack).push(3);
        inOrder.verify(mockedStack).push(5);
    }

    @Test
    void simpleMinusOperation() {
        Stack<Integer> mockedStack = mock(Stack.class);
        when(mockedStack.pop()).thenReturn(3, 2, -1);
        when(mockedStack.size()).thenReturn(2, -1);

        var calculator = new Calculator(mockedStack);
        assertEquals(-1, calculator.evaluate("2 3 -"));

        InOrder inOrder = inOrder(mockedStack);
        inOrder.verify(mockedStack).push(2);
        inOrder.verify(mockedStack).push(3);
        inOrder.verify(mockedStack).push(-1);
    }

    @Test
    void numberWithUnaryMinus() {
        Stack<Integer> mockedStack = mock(Stack.class);
        when(mockedStack.pop()).thenReturn(-1);

        var calculator = new Calculator(mockedStack);
        assertEquals(-1, calculator.evaluate("-1"));

        InOrder inOrder = inOrder(mockedStack);
        inOrder.verify(mockedStack).push(-1);
    }

    @Test
    void twoOperations() {
        Stack<Integer> mockedStack = mock(Stack.class);
        when(mockedStack.pop()).thenReturn(3, 2, 2, 5, 7);
        when(mockedStack.size()).thenReturn(2, 2, 1);

        var calculator = new Calculator(mockedStack);
        assertEquals(7, calculator.evaluate("2 3 + 2 +"));

        InOrder inOrder = inOrder(mockedStack);
        inOrder.verify(mockedStack).push(2);
        inOrder.verify(mockedStack).push(3);
        inOrder.verify(mockedStack).push(5);
        inOrder.verify(mockedStack).push(2);
        inOrder.verify(mockedStack).push(7);
    }

    @Test
    void divisionAndMultiply() {
        Stack<Integer> mockedStack = mock(Stack.class);
        when(mockedStack.pop()).thenReturn(3, 2, 2, 0, 0);
        when(mockedStack.size()).thenReturn(2, 2, 1);

        var calculator = new Calculator(mockedStack);
        assertEquals(0, calculator.evaluate("2 3 / 2 *"));

        InOrder inOrder = inOrder(mockedStack);
        inOrder.verify(mockedStack).push(2);
        inOrder.verify(mockedStack).push(3);
        inOrder.verify(mockedStack).push(0);
        inOrder.verify(mockedStack).push(2);
        inOrder.verify(mockedStack).push(0);
    }
}