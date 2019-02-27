package ru.hse.java.team;

import org.jetbrains.annotations.NotNull;

import java.util.Scanner;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;

public class Calculator {

    private final Stack<Double> stack;

    public Calculator(@NotNull Stack<Double> stack) {
        this.stack = stack;
    }

    private enum Operation implements DoubleBinaryOperator {
        PLUS    ("+", (l, r) -> l + r),
        MINUS   ("-", (l, r) -> l - r),
        MULTIPLY("*", (l, r) -> l * r),
        DIVIDE  ("/", (l, r) -> l / r);

        private final String symbol;
        private final DoubleBinaryOperator binaryOperator;

        private Operation(final String symbol, final DoubleBinaryOperator binaryOperator) {
            this.symbol = symbol;
            this.binaryOperator = binaryOperator;
        }

        public static Operation fromString(final String symbol) {
            switch (symbol) {
                case "+" :
                    return PLUS;
                case "-":
                    return MINUS;
                case "*":
                    return MULTIPLY;
                case "/":
                    return DIVIDE;
                default:
                    return null;
            }
        }

        public String getSymbol() {
            return symbol;
        }

        @Override
        public double applyAsDouble(final double left, final double right) {
            return binaryOperator.applyAsDouble(left, right);
        }
    }

    public double evaluate(@NotNull String s) {
        stack.clear();
        var scanner = new Scanner(s);
        scanner.useDelimiter(" ");

        while (scanner.hasNext()) {
            if (scanner.hasNextDouble()) {
                Double number = scanner.nextDouble();
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
                Double firstNumber = stack.pop();
                Double secondNumber = stack.pop();
                stack.push(operation.applyAsDouble(firstNumber, secondNumber));
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
