package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Введите число");
//        Long n = scanner.nextLong();
//        Long result = 1L;
//
//        for (int i = 1; i <= n; i++) {
//            result *= i;
//        }
//        Long i = Long.MAX_VALUE * 1_000_000_000_000_000L;
//
//        System.out.println(Long.MAX_VALUE * 1_000_000_000_000_000L);

        int[] arr1 = new int[]{3, 10, 5};
        int[] arr2 = new int[]{20, 7, 15, 8};

        test(124, 231, 22);

    }

    public static void test(int a, int b, int c) {

        List<Integer> intList = new ArrayList<>();
        intList.add(a);
        intList.add(b);
        intList.add(c);

        List<String> collect = intList.stream().map(x -> x < 0 ? 0 : x > 255 ? 255 : x)
                .map(Integer::toHexString).map(x -> x.length() == 1 ? "0".concat(x) : x)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println(String.join("", collect));

    }

}
