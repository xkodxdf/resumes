package com.xkodxdf.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainStreams {

    public static void main(String[] args) {
        System.out.println(minValue(new int[]{9, 8})); // 89
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3})); // 123
        System.out.println(minValue(new int[]{1, 4, 5, 1, 6, 4, 9, 9})); // 14569

        System.out.println("\n" + oddOrEven(Arrays.asList(1, 1, 1, 2, 2, 2))); // 1, 1, 1
        System.out.println(oddOrEven(Arrays.asList(1, 1, 1, 1, 2, 2))); // 2, 2
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (e1, e2) -> e1 * 10 + e2);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> integerMap = integers.stream()
                .collect(Collectors.partitioningBy(e -> e % 2 != 0));
        return integerMap.get(true).size() % 2 != 0 ?
                integerMap.get(true) : integerMap.get(false);
    }
}