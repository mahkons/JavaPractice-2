package ru.hse.java.team;

import org.jetbrains.annotations.NotNull;

import java.util.Scanner;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;

public class Calculator {

    private final Stack<Integer> stack;

    public Calculator(@NotNull Stack<Integer> stack) {
        this.stack = stack;
    }

    private enum Operation implements IntBinaryOperator {
        PLUS    ("+", (l, r) -> l + r),
        MINUS   ("-", (l, r) -> l - r),
        MULTIPLY("*", (l, r) -> l * r),
        DIVIDE  ("/", (l, r) -> l / r);

        private final String symbol;
        private final IntBinaryOperator binaryOperator;

        private Operation(final String symbol, final IntBinaryOperator binaryOperator) {
            this.symbol = symbol;
            this.binaryOperator = binaryOperator;
        }

        public static Operation fromString(final String symbol) {
            for (Operation operation : Operation.values()) {
                if (operation.symbol.equals(symbol)) {
                    return operation;
                }
            }
            return null;
        }

        public String getSymbol() {
            return symbol;
        }

        @Override
        public int applyAsInt(final int left, final int right) {
            return binaryOperator.applyAsInt(left, right);
        }
    }

    public double evaluate(@NotNull String s) {
        stack.clear();
        var scanner = new Scanner(s);
        scanner.useDelimiter(" ");

        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                Integer number = scanner.nextInt();
                stack.push(number);
            } else {
                String token = scanner.next();
                if (Operation.fromString(token) == null) {
                    stack.clear();
                    throw new IllegalArgumentException("String parse error: not a Double, and not a valid operation");
                }
                Operation operation = Operation.fromString(token);
                if (stack.size() < 2) {
                    stack.clear();
                    throw new IllegalArgumentException("String parse error: not enough numbers in stack to perform operation");
                }
                Integer firstNumber = stack.pop();
                Integer secondNumber = stack.pop();
                stack.push(operation.applyAsInt(firstNumber, secondNumber));
            }
        }
        if (stack.empty()) {
            throw new IllegalArgumentException("String parse error: no numbers in stack after evaluation");
        }
        if (stack.size() > 2) {
            stack.clear();
            throw new IllegalArgumentException("String parse error: more than one number in stack after evaluation");
        }
        return stack.pop();
    }

}
