
package sorting;

import java.util.Arrays;
import java.util.Random;

public class SortingBenchmarkBubbleFirst {
    public static void main(String[] args) {
        // Configuration
        final int SIZE = 7000;
        final int RUNS = 10;

        // Benchmark algorithms in specified order
        benchmarkAlgorithm("Bubble Sort", SortingBenchmarkBubbleFirst::bubbleSort, SIZE, RUNS);
        benchmarkAlgorithm("Merge Sort", SortingBenchmarkBubbleFirst::mergeSort, SIZE, RUNS);
        benchmarkAlgorithm("Quick Sort", SortingBenchmarkBubbleFirst::quickSort, SIZE, RUNS);
    }

    // ===== Benchmarking Infrastructure =====
    interface SortAlgorithm {
        void sort(int[] arr);
    }

    static void benchmarkAlgorithm(String name, SortAlgorithm algorithm, int size, int runs) {
        System.out.println("\n" + name.toUpperCase() + " (n=" + size + ", " + runs + " runs)");
        System.out.println("----------------------------------------");

        // Generate test data
        int[][] randomArrays = new int[runs][];
        int[][] partialArrays = new int[runs][];
        int[][] reversedArrays = new int[runs][];
        
        for (int i = 0; i < runs; i++) {
            randomArrays[i] = generateRandomArray(size);
            partialArrays[i] = generatePartiallySortedArray(size);
            reversedArrays[i] = generateReversedArray(size);
        }

        // Warm-up JVM (3 dummy runs)
        for (int i = 0; i < 3; i++) {
            algorithm.sort(generateRandomArray(1000));
        }

        // Benchmark
        benchmarkInputType("Random", randomArrays, algorithm, runs);
        benchmarkInputType("Partially Sorted", partialArrays, algorithm, runs);
        benchmarkInputType("Reversed", reversedArrays, algorithm, runs);
    }

    static void benchmarkInputType(String type, int[][] testData, SortAlgorithm algorithm, int runs) {
        long[] times = new long[runs];
        
        for (int i = 0; i < runs; i++) {
            int[] arr = testData[i].clone();
            long start = System.currentTimeMillis();
            algorithm.sort(arr);
            long end = System.currentTimeMillis();
            times[i] = end - start;
        }

        double avg = Arrays.stream(times).average().orElse(0);
        System.out.printf("%-16s | Avg: %6.2f ms | Min: %3d ms | Max: %3d ms%n",
                type + ":", avg, 
                Arrays.stream(times).min().orElse(0),
                Arrays.stream(times).max().orElse(0));
    }

    // ===== Sorting Algorithms (Bubble Sort first) =====
    static void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    static void mergeSort(int[] arr) {
        if (arr.length > 1) {
            int mid = arr.length / 2;
            int[] left = Arrays.copyOfRange(arr, 0, mid);
            int[] right = Arrays.copyOfRange(arr, mid, arr.length);
            mergeSort(left);
            mergeSort(right);
            
            // Merge
            int i = 0, j = 0, k = 0;
            while (i < left.length && j < right.length) {
                arr[k++] = left[i] <= right[j] ? left[i++] : right[j++];
            }
            while (i < left.length) arr[k++] = left[i++];
            while (j < right.length) arr[k++] = right[j++];
        }
    }

    static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    // ===== Utilities =====
    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // ===== Data Generators =====
    static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(size * 10);
        }
        return arr;
    }

    static int[] generatePartiallySortedArray(int size) {
        int[] arr = generateRandomArray(size);
        Arrays.sort(arr, 0, size / 2);
        return arr;
    }

    static int[] generateReversedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i;
        }
        return arr;
    }
}


