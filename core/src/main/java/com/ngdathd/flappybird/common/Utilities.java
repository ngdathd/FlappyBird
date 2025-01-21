package com.ngdathd.flappybird.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class Utilities {
    private Utilities() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String colorToHexString(Color color) {
        int r = (int) (color.r * 255);
        int g = (int) (color.g * 255);
        int b = (int) (color.b * 255);
        int a = (int) (color.a * 255);

        StringBuilder hexString = new StringBuilder("#");

        if (a < 16) hexString.append('0');
        hexString.append(Integer.toHexString(a).toUpperCase());

        if (r < 16) hexString.append('0');
        hexString.append(Integer.toHexString(r).toUpperCase());

        if (g < 16) hexString.append('0');
        hexString.append(Integer.toHexString(g).toUpperCase());

        if (b < 16) hexString.append('0');
        hexString.append(Integer.toHexString(b).toUpperCase());

        return hexString.toString();
    }

    public static void quickSort(Array<Integer> list, int low, int high, boolean isAsc) {
        if (low < high) {
            int pivotIndex = partition(list, low, high, isAsc);
            quickSort(list, low, pivotIndex - 1, isAsc);
            quickSort(list, pivotIndex + 1, high, isAsc);
        }
    }

    private static int partition(Array<Integer> list, int low, int high, boolean isAsc) {
        int pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            boolean condition = isAsc ? list.get(j) < pivot : list.get(j) > pivot;

            if (condition) {
                i++;
                // Hoán đổi giá trị giữa list[i] và list[j]
                int temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        // Hoán đổi giá trị giữa list[i + 1] và list[high]
        int temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1;
    }
}
