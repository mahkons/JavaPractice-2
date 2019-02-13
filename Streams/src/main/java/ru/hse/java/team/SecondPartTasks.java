package ru.hse.java.team;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.pow;
import static java.lang.Math.random;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream()
                .filter(s -> s.contains(sequence))
                .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        final int N = 100000;
        return (double) Stream.iterate(1, n -> n + 1)
                .limit(N)
                .map(x -> pow(random(), 2))
                .map(x -> x + pow(random(), 2))
                .filter(len -> len <= 1)
                .count() / N;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        assert !compositions.isEmpty();

        return compositions.entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(),
                        String.join("", e.getValue())
                                .length()))
                .max(Comparator.comparing(AbstractMap.SimpleEntry::getValue))
                .get()
                .getKey();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        Map<String, Integer> resultingMap = new HashMap<>();

        var gg = orders.stream()
                .flatMap(e -> e.entrySet().stream())
                .map(e -> resultingMap.containsKey(e.getKey()) ?
                        resultingMap.put(e.getKey(), resultingMap.get(e.getKey()) + e.getValue())
                        : resultingMap.put(e.getKey(), e.getValue())).collect(Collectors.toList());
        return resultingMap;
    }
}